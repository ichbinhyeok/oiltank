package owner.buriedoiltank.ops;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import owner.buriedoiltank.data.ContentRepository;
import owner.buriedoiltank.data.GuideRecord;
import owner.buriedoiltank.data.IndexStatus;
import owner.buriedoiltank.data.PageType;
import owner.buriedoiltank.data.PromotionRecommendation;
import owner.buriedoiltank.data.RouteFamily;
import owner.buriedoiltank.data.RouteInventoryEntry;
import owner.buriedoiltank.data.RoutePhase;
import owner.buriedoiltank.data.SourceFreshnessStatus;
import owner.buriedoiltank.data.StateRecord;
import org.springframework.stereotype.Service;

@Service
public class RouteInventoryService {
    private final List<RouteInventoryEntry> entries;
    private final LocalDate latestVerifiedOn;

    public RouteInventoryService(ContentRepository repository, Clock clock) {
        LocalDate today = LocalDate.now(clock);
        List<RouteInventoryEntry> builtEntries = new ArrayList<>();

        for (StateRecord state : repository.states()) {
            for (RouteFamily family : RouteFamily.values()) {
                SourceFreshnessStatus freshness = state.freshnessStatus(today);
                IndexStatus indexStatus = state.launchReady() ? family.defaultIndexStatus() : IndexStatus.NOINDEX;
                RoutePhase phase = state.launchReady() ? family.phase() : RoutePhase.HELD_SUPPORT;
                builtEntries.add(new RouteInventoryEntry(
                        state.slug() + ":" + family.slug(),
                        state.name() + " " + family.displayLabel(),
                        family.pathForState(state.slug()),
                        PageType.STATE_ROUTE,
                        state.name(),
                        state.slug(),
                        family,
                        indexStatus,
                        phase,
                        freshness,
                        family.defaultScenario(),
                        family.defaultPartnerType(),
                        buildRecommendation(state, family, freshness),
                        buildRecommendationReason(state, family, freshness),
                        state.verifiedOn(),
                        state.nextReviewOn()
                ));
            }
        }

        for (GuideRecord guide : repository.guides()) {
            SourceFreshnessStatus freshness = guide.freshnessStatus(today);
            builtEntries.add(new RouteInventoryEntry(
                    "guide:" + guide.slug(),
                    guide.title(),
                    "/guides/" + guide.slug() + "/",
                    PageType.GUIDE,
                    "Guide",
                    null,
                    guide.primaryRouteFamily(),
                    guide.indexable() ? IndexStatus.INDEX : IndexStatus.NOINDEX,
                    guide.indexable() ? guide.primaryRouteFamily().phase() : RoutePhase.HELD_SUPPORT,
                    freshness,
                    guide.primaryScenario(),
                    guide.primaryPartnerType(),
                    buildGuideRecommendation(guide, freshness),
                    buildGuideReason(guide, freshness),
                    guide.verifiedOn(),
                    guide.nextReviewOn()
            ));
        }

        this.entries = builtEntries.stream()
                .sorted(Comparator.comparing(RouteInventoryEntry::path))
                .toList();
        this.latestVerifiedOn = this.entries.stream()
                .map(RouteInventoryEntry::verifiedOn)
                .filter(Objects::nonNull)
                .max(LocalDate::compareTo)
                .orElse(today);
    }

    public List<RouteInventoryEntry> entries() {
        return entries;
    }

    public Optional<RouteInventoryEntry> findStateRoute(String stateSlug, RouteFamily family) {
        return entries.stream()
                .filter(entry -> entry.pageType() == PageType.STATE_ROUTE)
                .filter(entry -> stateSlug.equals(entry.stateSlug()) && entry.routeFamily() == family)
                .findFirst();
    }

    public Optional<RouteInventoryEntry> findGuide(String slug) {
        String guidePath = "/guides/" + slug + "/";
        return entries.stream().filter(entry -> guidePath.equals(entry.path())).findFirst();
    }

    public List<RouteInventoryEntry> indexableEntries() {
        return entries.stream().filter(RouteInventoryEntry::isIndexable).toList();
    }

    public List<String> indexableSitemapPaths() {
        List<String> staticPaths = List.of(
                "/",
                "/about/",
                "/methodology/",
                "/contact/",
                "/states/",
                "/routes/",
                "/guides/",
                "/not-government-affiliated/",
                "/privacy/",
                "/terms/"
        );
        List<String> dynamicPaths = indexableEntries().stream()
                .map(RouteInventoryEntry::path)
                .toList();
        List<String> paths = new ArrayList<>(staticPaths);
        paths.addAll(dynamicPaths);
        return paths;
    }

    public LocalDate lastModifiedForPath(String path) {
        return switch (path) {
            case "/" -> latestVerifiedOn;
            case "/states/" -> maxVerifiedForEntries(entries.stream()
                    .filter(entry -> entry.pageType() == PageType.STATE_ROUTE)
                    .filter(entry -> entry.routeFamily().isOverview())
                    .filter(RouteInventoryEntry::isIndexable)
                    .toList());
            case "/routes/", "/guides/" -> maxVerifiedForEntries(entries.stream()
                    .filter(entry -> entry.pageType() == PageType.GUIDE)
                    .filter(RouteInventoryEntry::isIndexable)
                    .toList());
            case "/about/", "/methodology/", "/contact/", "/privacy/", "/terms/", "/not-government-affiliated/" -> latestVerifiedOn;
            default -> entries.stream()
                    .filter(entry -> path.equals(entry.path()))
                    .map(RouteInventoryEntry::verifiedOn)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(latestVerifiedOn);
        };
    }

    private LocalDate maxVerifiedForEntries(List<RouteInventoryEntry> filteredEntries) {
        return filteredEntries.stream()
                .map(RouteInventoryEntry::verifiedOn)
                .filter(Objects::nonNull)
                .max(LocalDate::compareTo)
                .orElse(latestVerifiedOn);
    }

    private static PromotionRecommendation buildRecommendation(StateRecord state, RouteFamily family, SourceFreshnessStatus freshness) {
        if (freshness == SourceFreshnessStatus.STALE) {
            return PromotionRecommendation.HOLD;
        }
        if (!state.launchReady()) {
            return PromotionRecommendation.HOLD;
        }
        if (family.phase() == RoutePhase.HELD_SUPPORT) {
            return PromotionRecommendation.HOLD;
        }
        return PromotionRecommendation.HOLD;
    }

    private static String buildRecommendationReason(StateRecord state, RouteFamily family, SourceFreshnessStatus freshness) {
        if (freshness == SourceFreshnessStatus.STALE) {
            return "Source freshness is stale, so promotion stays blocked until review is complete.";
        }
        if (!state.launchReady()) {
            return "State is held in launch reserve until source depth is strong enough for the public cohort.";
        }
        if (family.phase() == RoutePhase.HELD_SUPPORT) {
            return "Held until query, CTA, and lead evidence justify promotion.";
        }
        return "Phase 1 public route stays live while source freshness remains current.";
    }

    private static PromotionRecommendation buildGuideRecommendation(GuideRecord guide, SourceFreshnessStatus freshness) {
        if (freshness == SourceFreshnessStatus.STALE) {
            return PromotionRecommendation.HOLD;
        }
        return guide.indexable() ? PromotionRecommendation.HOLD : PromotionRecommendation.HOLD;
    }

    private static String buildGuideReason(GuideRecord guide, SourceFreshnessStatus freshness) {
        if (freshness == SourceFreshnessStatus.STALE) {
            return "Guide sources are stale, so the route cannot be promoted.";
        }
        return guide.indexable()
                ? "Guide supports the launch wedge and routes users into state pages."
                : "Guide stays held until the first wedge proves traction.";
    }
}

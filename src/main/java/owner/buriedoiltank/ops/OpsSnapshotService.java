package owner.buriedoiltank.ops;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.data.ContentRepository;
import owner.buriedoiltank.data.GuideRecord;
import owner.buriedoiltank.data.IndexStatus;
import owner.buriedoiltank.data.PromotionRecommendation;
import owner.buriedoiltank.data.RouteInventoryEntry;
import owner.buriedoiltank.data.StateRecord;
import owner.buriedoiltank.data.SourceFreshnessStatus;
import owner.buriedoiltank.leads.EventLogService;
import owner.buriedoiltank.leads.LeadService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class OpsSnapshotService {
    private static final List<String> ROUTE_STATUS_HEADERS = List.of(
            "route_id",
            "route_path",
            "route_family",
            "scope",
            "phase",
            "index_status",
            "source_freshness_status",
            "last_28_day_impressions",
            "last_28_day_clicks",
            "last_28_day_ctr",
            "last_28_day_cta_clicks",
            "last_28_day_lead_opens",
            "last_28_day_lead_submissions",
            "dominant_scenario",
            "promotion_recommendation",
            "recommendation_reason",
            "reviewed_on",
            "next_review_on"
    );

    private final ContentRepository contentRepository;
    private final RouteInventoryService routeInventoryService;
    private final LeadService leadService;
    private final EventLogService eventLogService;
    private final ObjectMapper objectMapper;
    private final Clock clock;
    private final Path opsRoot;
    private final Path derivedRoot;

    public OpsSnapshotService(
            ContentRepository contentRepository,
            RouteInventoryService routeInventoryService,
            LeadService leadService,
            EventLogService eventLogService,
            ObjectMapper objectMapper,
            Clock clock,
            SiteProperties siteProperties
    ) {
        this.contentRepository = contentRepository;
        this.routeInventoryService = routeInventoryService;
        this.leadService = leadService;
        this.eventLogService = eventLogService;
        this.objectMapper = objectMapper;
        this.clock = clock;
        this.opsRoot = siteProperties.getStorageRoot().resolve("ops");
        this.derivedRoot = siteProperties.getStorageRoot().resolve("derived");
    }

    public OpsSnapshots.SnapshotBundle snapshotBundle() {
        LocalDate reviewDate = LocalDate.now(clock);
        LocalDate dataWindowStart = reviewDate.minusDays(27);
        List<Map<String, String>> recentLeads = leadService.leads().stream()
                .filter(withinLast28Days())
                .toList();
        List<Map<String, String>> recentEvents = eventLogService.events().stream()
                .filter(withinLast28Days())
                .toList();

        Map<String, Long> leadsByPartner = recentLeads.stream()
                .collect(Collectors.groupingBy(row -> row.getOrDefault("partner_type", "unknown"), LinkedHashMap::new, Collectors.counting()));
        Map<String, Long> ctaClicksByRouteFamily = recentEvents.stream()
                .filter(row -> "cta_click".equals(row.get("event_type")))
                .collect(Collectors.groupingBy(row -> row.getOrDefault("route_family", "guide"), LinkedHashMap::new, Collectors.counting()));

        List<OpsSnapshots.RouteStatusSnapshot> routeStatuses = routeInventoryService.entries().stream()
                .map(entry -> buildRouteStatus(reviewDate, recentLeads, recentEvents, entry))
                .toList();

        OpsSnapshots.SourceFreshnessReviewSnapshot sourceFreshnessReviewSnapshot = buildSourceFreshnessReview(reviewDate, routeStatuses);

        List<String> staleScopes = sourceFreshnessReviewSnapshot.scopes().stream()
                .filter(scope -> SourceFreshnessStatus.STALE.slug().equals(scope.sourceFreshnessStatus()))
                .map(OpsSnapshots.FreshnessScopeSnapshot::scopeLabel)
                .distinct()
                .sorted()
                .toList();

        OpsSnapshots.AdminMetricsSnapshot adminMetricsSnapshot = new OpsSnapshots.AdminMetricsSnapshot(
                reviewDate,
                dataWindowStart,
                reviewDate,
                routeInventoryService.entries().stream().filter(RouteInventoryEntry::isIndexable).count(),
                routeInventoryService.entries().stream().filter(entry -> !entry.isIndexable()).count(),
                sourceFreshnessReviewSnapshot.staleScopeCount(),
                sourceFreshnessReviewSnapshot.freshScopeCount(),
                sourceFreshnessReviewSnapshot.staleRouteCount(),
                recentEvents.stream().filter(row -> "cta_click".equals(row.get("event_type"))).count(),
                recentEvents.stream().filter(row -> "lead_open".equals(row.get("event_type"))).count(),
                recentLeads.size(),
                leadsByPartner,
                ctaClicksByRouteFamily,
                staleScopes
        );

        OpsSnapshots.PromotionReviewSnapshot promotionReviewSnapshot =
                buildPromotionReview(reviewDate, dataWindowStart, adminMetricsSnapshot, routeStatuses, sourceFreshnessReviewSnapshot);
        return new OpsSnapshots.SnapshotBundle(
                adminMetricsSnapshot,
                routeStatuses,
                promotionReviewSnapshot,
                sourceFreshnessReviewSnapshot
        );
    }

    public String routeManifestJson() {
        return toJson(routeInventoryService.entries());
    }

    public String adminMetricsJson() {
        return toJson(snapshotBundle().adminMetricsSnapshot());
    }

    public String promotionReviewJson() {
        return toJson(snapshotBundle().promotionReviewSnapshot());
    }

    public String sourceFreshnessReviewJson() {
        return toJson(snapshotBundle().sourceFreshnessReviewSnapshot());
    }

    public String routeStatusCsv() {
        List<OpsSnapshots.RouteStatusSnapshot> rows = snapshotBundle().routeStatuses();
        StringBuilder csv = new StringBuilder(String.join(",", ROUTE_STATUS_HEADERS)).append(System.lineSeparator());
        for (OpsSnapshots.RouteStatusSnapshot row : rows) {
            csv.append(toCsvRow(List.of(
                    row.routeId(),
                    row.routePath(),
                    row.routeFamily(),
                    row.scope(),
                    row.phase(),
                    row.indexStatus(),
                    row.sourceFreshnessStatus(),
                    Long.toString(row.last28DayImpressions()),
                    Long.toString(row.last28DayClicks()),
                    Double.toString(row.last28DayCtr()),
                    Long.toString(row.last28DayCtaClicks()),
                    Long.toString(row.last28DayLeadOpens()),
                    Long.toString(row.last28DayLeadSubmissions()),
                    row.dominantScenario(),
                    row.promotionRecommendation(),
                    row.recommendationReason(),
                    row.reviewedOn().toString(),
                    row.nextReviewOn() == null ? "" : row.nextReviewOn().toString()
            ))).append(System.lineSeparator());
        }
        return csv.toString();
    }

    public void refreshSnapshots() {
        try {
            Files.createDirectories(opsRoot);
            Files.createDirectories(derivedRoot);
            Files.writeString(derivedRoot.resolve("routes.json"), routeManifestJson(), StandardCharsets.UTF_8);
            Files.writeString(opsRoot.resolve("route-status.csv"), routeStatusCsv(), StandardCharsets.UTF_8);
            Files.writeString(opsRoot.resolve("promotion-review.json"), promotionReviewJson(), StandardCharsets.UTF_8);
            Files.writeString(opsRoot.resolve("admin-metrics-snapshot.json"), adminMetricsJson(), StandardCharsets.UTF_8);
            Files.writeString(opsRoot.resolve("source-freshness-review.json"), sourceFreshnessReviewJson(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to refresh ops snapshots", exception);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        refreshSnapshots();
    }

    @EventListener(OpsRefreshRequestedEvent.class)
    public void onOpsRefreshRequested(OpsRefreshRequestedEvent ignored) {
        refreshSnapshots();
    }

    private OpsSnapshots.RouteStatusSnapshot buildRouteStatus(
            LocalDate reviewDate,
            List<Map<String, String>> recentLeads,
            List<Map<String, String>> recentEvents,
            RouteInventoryEntry entry
    ) {
        long ctaClicks = countEventsForPage(recentEvents, entry.id(), "cta_click");
        long leadOpens = countEventsForPage(recentEvents, entry.id(), "lead_open");
        long leadSubmissions = countLeadsForPage(recentLeads, entry.id());
        PromotionRecommendation recommendation = dynamicRecommendation(entry, ctaClicks, leadOpens, leadSubmissions);
        String recommendationReason = dynamicRecommendationReason(entry, recommendation, ctaClicks, leadOpens, leadSubmissions);

        return new OpsSnapshots.RouteStatusSnapshot(
                entry.id(),
                entry.path(),
                entry.routeFamily().slug(),
                entry.scopeLabel(),
                entry.phase().slug(),
                entry.indexStatus().slug(),
                entry.sourceFreshnessStatus().slug(),
                0,
                0,
                0.0d,
                ctaClicks,
                leadOpens,
                leadSubmissions,
                entry.dominantScenario().slug(),
                recommendation.slug(),
                recommendationReason,
                reviewDate,
                entry.nextReviewOn()
        );
    }

    private OpsSnapshots.PromotionReviewSnapshot buildPromotionReview(
            LocalDate reviewDate,
            LocalDate dataWindowStart,
            OpsSnapshots.AdminMetricsSnapshot adminMetricsSnapshot,
            List<OpsSnapshots.RouteStatusSnapshot> routeStatuses,
            OpsSnapshots.SourceFreshnessReviewSnapshot sourceFreshnessReviewSnapshot
    ) {
        List<String> promotedCandidateRoutes = routeStatuses.stream()
                .filter(row -> PromotionRecommendation.RECOMMEND_PROMOTE.slug().equals(row.promotionRecommendation()))
                .map(OpsSnapshots.RouteStatusSnapshot::routePath)
                .toList();
        List<String> heldRoutesStillNotReady = routeStatuses.stream()
                .filter(row -> IndexStatus.NOINDEX.slug().equals(row.indexStatus()))
                .filter(row -> PromotionRecommendation.HOLD.slug().equals(row.promotionRecommendation()))
                .map(OpsSnapshots.RouteStatusSnapshot::routePath)
                .toList();
        List<String> routesToDemoteOrMerge = routeStatuses.stream()
                .filter(row -> PromotionRecommendation.RECOMMEND_DEMOTE.slug().equals(row.promotionRecommendation()))
                .map(OpsSnapshots.RouteStatusSnapshot::routePath)
                .toList();

        List<String> blockers = buildBlockers(adminMetricsSnapshot, routeStatuses, sourceFreshnessReviewSnapshot);
        String summary;
        if (!promotedCandidateRoutes.isEmpty()) {
            summary = "At least one held route has enough CTA or lead evidence to justify promotion review.";
        } else if (!adminMetricsSnapshot.staleScopes().isEmpty()) {
            summary = "Source freshness is blocking promotion for one or more scopes. Keep held routes fail-closed until review is complete.";
        } else {
            summary = "No held route has enough evidence to widen the public surface yet. Keep the launch wedge focused on buyer-seller, sweep-first, and records-first paths.";
        }

        return new OpsSnapshots.PromotionReviewSnapshot(
                reviewDate,
                dataWindowStart,
                reviewDate,
                summary,
                promotedCandidateRoutes,
                heldRoutesStillNotReady,
                routesToDemoteOrMerge,
                blockers
        );
    }

    private List<String> buildBlockers(
            OpsSnapshots.AdminMetricsSnapshot adminMetricsSnapshot,
            List<OpsSnapshots.RouteStatusSnapshot> routeStatuses,
            OpsSnapshots.SourceFreshnessReviewSnapshot sourceFreshnessReviewSnapshot
    ) {
        List<String> blockers = new java.util.ArrayList<>();
        List<String> freshnessBlockers = sourceFreshnessReviewSnapshot.scopes().stream()
                .filter(scope -> SourceFreshnessStatus.STALE.slug().equals(scope.sourceFreshnessStatus()))
                .map(scope -> "Source review overdue for " + scope.scopeLabel() + " (" + scope.affectedRoutes() + " routes blocked).")
                .toList();
        blockers.addAll(freshnessBlockers);
        if (!adminMetricsSnapshot.staleScopes().isEmpty() && freshnessBlockers.isEmpty()) {
            blockers.add("source gap");
        }
        boolean noHeldEvidence = routeStatuses.stream()
                .filter(row -> IndexStatus.NOINDEX.slug().equals(row.indexStatus()))
                .allMatch(row -> row.last28DayCtaClicks() == 0 && row.last28DayLeadSubmissions() == 0 && row.last28DayLeadOpens() == 0);
        if (noHeldEvidence) {
            blockers.add("no query evidence");
        }
        if (adminMetricsSnapshot.ctaClicks() == 0) {
            blockers.add("weak CTA behavior");
        }
        if (adminMetricsSnapshot.leadSubmissions() == 0) {
            blockers.add("weak lead evidence");
        }
        if (adminMetricsSnapshot.leadsByPartnerType().isEmpty()) {
            blockers.add("partner gap");
        }
        return blockers;
    }

    private OpsSnapshots.SourceFreshnessReviewSnapshot buildSourceFreshnessReview(
            LocalDate reviewDate,
            List<OpsSnapshots.RouteStatusSnapshot> routeStatuses
    ) {
        List<OpsSnapshots.FreshnessScopeSnapshot> scopes = new java.util.ArrayList<>();
        for (StateRecord state : contentRepository.states()) {
            scopes.add(buildStateFreshnessScope(reviewDate, routeStatuses, state));
        }
        for (GuideRecord guide : contentRepository.guides()) {
            scopes.add(buildGuideFreshnessScope(reviewDate, routeStatuses, guide));
        }

        List<OpsSnapshots.FreshnessScopeSnapshot> sortedScopes = scopes.stream()
                .sorted(Comparator
                        .comparing((OpsSnapshots.FreshnessScopeSnapshot scope) ->
                                SourceFreshnessStatus.STALE.slug().equals(scope.sourceFreshnessStatus()) ? 0 : 1)
                        .thenComparing(scope -> scope.nextReviewOn() == null ? LocalDate.MAX : scope.nextReviewOn())
                        .thenComparing(OpsSnapshots.FreshnessScopeSnapshot::scopeLabel))
                .toList();

        long staleScopeCount = sortedScopes.stream()
                .filter(scope -> SourceFreshnessStatus.STALE.slug().equals(scope.sourceFreshnessStatus()))
                .count();
        long staleRouteCount = sortedScopes.stream()
                .filter(scope -> SourceFreshnessStatus.STALE.slug().equals(scope.sourceFreshnessStatus()))
                .mapToLong(OpsSnapshots.FreshnessScopeSnapshot::affectedRoutes)
                .sum();

        return new OpsSnapshots.SourceFreshnessReviewSnapshot(
                reviewDate,
                staleScopeCount,
                sortedScopes.size() - staleScopeCount,
                staleRouteCount,
                sortedScopes
        );
    }

    private OpsSnapshots.FreshnessScopeSnapshot buildStateFreshnessScope(
            LocalDate reviewDate,
            List<OpsSnapshots.RouteStatusSnapshot> routeStatuses,
            StateRecord state
    ) {
        List<OpsSnapshots.RouteStatusSnapshot> affectedRoutes = routeStatuses.stream()
                .filter(row -> state.name().equals(row.scope()))
                .toList();
        return buildFreshnessScope(
                reviewDate,
                "state:" + state.slug(),
                state.name(),
                "state",
                state.freshnessStatus(reviewDate),
                state.verifiedOn(),
                state.nextReviewOn(),
                affectedRoutes,
                state.sourceStack().stream().map(owner.buriedoiltank.data.SourceReference::title).toList()
        );
    }

    private OpsSnapshots.FreshnessScopeSnapshot buildGuideFreshnessScope(
            LocalDate reviewDate,
            List<OpsSnapshots.RouteStatusSnapshot> routeStatuses,
            GuideRecord guide
    ) {
        String guidePath = "/guides/" + guide.slug() + "/";
        List<OpsSnapshots.RouteStatusSnapshot> affectedRoutes = routeStatuses.stream()
                .filter(row -> guidePath.equals(row.routePath()))
                .toList();
        return buildFreshnessScope(
                reviewDate,
                "guide:" + guide.slug(),
                guide.title(),
                "guide",
                guide.freshnessStatus(reviewDate),
                guide.verifiedOn(),
                guide.nextReviewOn(),
                affectedRoutes,
                guide.sourceStack().stream().map(owner.buriedoiltank.data.SourceReference::title).toList()
        );
    }

    private OpsSnapshots.FreshnessScopeSnapshot buildFreshnessScope(
            LocalDate reviewDate,
            String scopeKey,
            String scopeLabel,
            String scopeType,
            SourceFreshnessStatus freshnessStatus,
            LocalDate verifiedOn,
            LocalDate nextReviewOn,
            List<OpsSnapshots.RouteStatusSnapshot> affectedRoutes,
            List<String> sourceTitles
    ) {
        long daysUntilReview = nextReviewOn == null ? Long.MAX_VALUE : ChronoUnit.DAYS.between(reviewDate, nextReviewOn);
        return new OpsSnapshots.FreshnessScopeSnapshot(
                scopeKey,
                scopeLabel,
                scopeType,
                freshnessStatus.slug(),
                verifiedOn,
                nextReviewOn,
                daysUntilReview,
                affectedRoutes.size(),
                affectedRoutes.stream().filter(row -> IndexStatus.INDEX.slug().equals(row.indexStatus())).count(),
                affectedRoutes.stream().filter(row -> IndexStatus.NOINDEX.slug().equals(row.indexStatus())).count(),
                affectedRoutes.stream().map(OpsSnapshots.RouteStatusSnapshot::routePath).toList(),
                sourceTitles
        );
    }

    private PromotionRecommendation dynamicRecommendation(RouteInventoryEntry entry, long ctaClicks, long leadOpens, long leadSubmissions) {
        if (entry.sourceFreshnessStatus() == SourceFreshnessStatus.STALE) {
            return PromotionRecommendation.HOLD;
        }
        if (entry.indexStatus() == IndexStatus.NOINDEX && (leadSubmissions > 0 || leadOpens >= 2 || ctaClicks >= 5)) {
            return PromotionRecommendation.RECOMMEND_PROMOTE;
        }
        return entry.promotionRecommendation();
    }

    private String dynamicRecommendationReason(
            RouteInventoryEntry entry,
            PromotionRecommendation recommendation,
            long ctaClicks,
            long leadOpens,
            long leadSubmissions
    ) {
        if (entry.sourceFreshnessStatus() == SourceFreshnessStatus.STALE) {
            return "Source freshness is stale, so promotion stays blocked until review is complete.";
        }
        if (recommendation == PromotionRecommendation.RECOMMEND_PROMOTE) {
            return "Held route is showing enough CTA or lead evidence to justify promotion review.";
        }
        if (entry.indexStatus() == IndexStatus.NOINDEX) {
            return "Held route remains blocked while evidence is limited. CTA clicks=" + ctaClicks + ", lead opens=" + leadOpens + ", lead submissions=" + leadSubmissions + ".";
        }
        return entry.recommendationReason();
    }

    private long countEventsForPage(List<Map<String, String>> events, String pageId, String eventType) {
        return events.stream()
                .filter(row -> pageId.equals(row.get("page_id")))
                .filter(row -> eventType.equals(row.get("event_type")))
                .count();
    }

    private long countLeadsForPage(List<Map<String, String>> leads, String pageId) {
        return leads.stream().filter(row -> pageId.equals(row.get("page_id"))).count();
    }

    private Predicate<Map<String, String>> withinLast28Days() {
        OffsetDateTime cutoff = OffsetDateTime.now(clock).minusDays(28);
        return row -> {
            try {
                return OffsetDateTime.parse(row.get("timestamp")).isAfter(cutoff);
            } catch (Exception exception) {
                return false;
            }
        };
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to serialize ops snapshot", exception);
        }
    }

    private static String toCsvRow(List<String> values) {
        return values.stream().map(OpsSnapshotService::escapeCsv).collect(Collectors.joining(","));
    }

    private static String escapeCsv(String raw) {
        String sanitized = raw == null ? "" : raw.replace("\r", " ").replace("\n", " ").trim();
        if (sanitized.contains(",") || sanitized.contains("\"")) {
            return "\"" + sanitized.replace("\"", "\"\"") + "\"";
        }
        return sanitized;
    }
}

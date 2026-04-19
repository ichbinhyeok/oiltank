package owner.buriedoiltank.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.Clock;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.data.ContentRepository;
import owner.buriedoiltank.data.GuideRecord;
import owner.buriedoiltank.data.RouteFamily;
import owner.buriedoiltank.data.RouteInventoryEntry;
import owner.buriedoiltank.data.Scenario;
import owner.buriedoiltank.data.SourceReference;
import owner.buriedoiltank.data.SourceFreshnessStatus;
import owner.buriedoiltank.data.StateRecord;
import owner.buriedoiltank.ops.RouteInventoryService;
import org.springframework.stereotype.Service;

@Service
public class SitePageService {
    private static final String CONTACT_EMAIL = "shinhyeok22@gmail.com";

    private final ContentRepository repository;
    private final RouteInventoryService routeInventoryService;
    private final URI baseUrl;
    private final String analyticsMeasurementId;
    private final Clock clock;
    private final ObjectMapper objectMapper;

    public SitePageService(
            ContentRepository repository,
            RouteInventoryService routeInventoryService,
            SiteProperties siteProperties,
            Clock clock,
            ObjectMapper objectMapper
    ) {
        this.repository = repository;
        this.routeInventoryService = routeInventoryService;
        this.baseUrl = siteProperties.getBaseUrl();
        this.analyticsMeasurementId = siteProperties.getAnalyticsMeasurementId();
        this.clock = clock;
        this.objectMapper = objectMapper;
    }

    public PageModels.HomePageModel homePage() {
        List<PageModels.AudienceCard> audienceCards = List.of(
                new PageModels.AudienceCard(
                        "Buyer",
                        "Buyer under contract",
                        "Keep the deal inside diligence until the file, sweep result, or leak evidence changes the route.",
                        List.of(
                                "Protect the inspection or attorney-review deadline before you debate credits.",
                                "Request closure proof, permit history, and any fuel-conversion record on day one.",
                                "Use records or a sweep before anyone pushes you into removal quotes."
                        )
                ),
                new PageModels.AudienceCard(
                        "Seller",
                        "Seller before due diligence widens",
                        "Reduce avoidable delay by cleaning up the record stack before the buyer frames the issue for you.",
                        List.of(
                                "Assemble every permit, closure, and prior oil-heat record you can find.",
                                "Separate suspected tank risk from confirmed tank facts before you price the problem.",
                                "Use the state page to decide whether records, a sweep, or disclosure questions come first."
                        )
                ),
                new PageModels.AudienceCard(
                        "Advisor",
                        "Agent or attorney carrying the deal clock",
                        "Keep the next call tied to evidence so the file does not jump from uncertainty into assumed contamination.",
                        List.of(
                                "Get the document request out before the next contingency or attorney-review call.",
                                "Write down which deadline breaks first: inspection, attorney review, financing, or closing.",
                                "Use records or a sweep before anyone sells a remediation story."
                        )
                )
        );

        List<PageModels.LinkCard> scenarioCards = List.of(
                new PageModels.LinkCard(
                        "Buyer or seller risk",
                        "Start with what is missing from the file before price or removal talk.",
                        "/guides/buried-oil-tank-home-sale/",
                        "Deal file"
                ),
                new PageModels.LinkCard(
                        "Sweep first",
                        "Use a locate or sweep when site clues and paperwork do not line up.",
                        "/guides/oil-tank-sweep-before-buying-house/",
                        "Site evidence"
                ),
                new PageModels.LinkCard(
                        "Records first",
                        "Treat missing records as a trigger to verify, not proof that no tank exists.",
                        "/guides/abandoned-oil-tank-records/",
                        "Paper trail"
                )
        );

        List<String> hotStates = List.of(
                "You are under contract and the tank question is still unresolved.",
                "A buyer, seller, or advisor needs the next honest move before a contract deadline slips.",
                "Records are missing and nobody can show clean closure proof.",
                "Old fill or vent evidence suggests a tank, but the site facts are still thin.",
                "Leak concern is starting to overtake a simple transaction question."
        );

        List<PageModels.LinkCard> decisionPathCards = List.of(
                new PageModels.LinkCard(
                        "Removal or abandonment",
                        "Use this only after the tank is confirmed and the state closure path is clear.",
                        "/guides/remove-vs-abandon-oil-tank/",
                        "Disposition"
                ),
                new PageModels.LinkCard(
                        "Leak and cleanup",
                        "Use this when odor, staining, release language, or cleanup facts overtake ordinary sale-side routing.",
                        "/guides/leaking-heating-oil-tank-what-to-do/",
                        "Escalation"
                ),
                new PageModels.LinkCard(
                        "Cost direction",
                        "Use this once the route is narrow enough that sweep, closure, removal, and cleanup are no longer being mixed together.",
                        "/guides/oil-tank-removal-cost/",
                        "Budget range"
                )
        );

        List<PageModels.StateCard> states = repository.states().stream()
                .filter(StateRecord::launchReady)
                .map(state -> new PageModels.StateCard(
                        state.name(),
                        state.slug(),
                        state.quickAnswer(),
                        "/states/" + state.slug() + "/",
                        state.commonTriggers().stream().limit(3).toList()
                ))
                .toList();

        List<PageModels.LinkCard> guideCards = repository.guides().stream()
                .filter(GuideRecord::indexable)
                .map(guide -> new PageModels.LinkCard(
                        guideHeading(guide),
                        guideMetaDescription(guide),
                        "/guides/" + guide.slug() + "/",
                        "Guide"
                ))
                .toList();

        int trackedSourceCount = (int) Stream.concat(
                        repository.states().stream()
                                .filter(StateRecord::launchReady)
                                .flatMap(state -> state.sourceStack().stream()),
                        repository.guides().stream()
                                .filter(GuideRecord::indexable)
                                .flatMap(guide -> guide.sourceStack().stream())
                )
                .map(SourceReference::id)
                .distinct()
                .count();

        return new PageModels.HomePageModel(
                meta(
                        "Buried Oil Tank Before Closing: Next Steps for Buyers and Sellers | Buried Oil Tank Verdict",
                        "Buried oil tank disclosure, records, sweep, and next-step guidance for buyers, sellers, agents, and attorneys before closing.",
                        "/",
                        true,
                        List.of(
                                jsonLd(siteSchema()),
                                jsonLd(collectionPageSchema(
                                        "Buried Oil Tank Before Closing",
                                        "Buried oil tank disclosure, records, sweep, and next-step guidance for buyers, sellers, agents, and attorneys before closing.",
                                        "/",
                                        List.of(
                                                new PageModels.LinkCard("State pages", "State-specific buried oil tank disclosure and closing steps.", "/states/", "Hub"),
                                                new PageModels.LinkCard("Route guides", "Cross-state route guides for disclosure, sweep, removal, leak, and cost questions.", "/routes/", "Hub"),
                                                new PageModels.LinkCard("Cross-state guides", "Guides for home sale, records, and oil tank sweep questions.", "/guides/", "Hub")
                                        )
                                ))
                        )
                ),
                scenarioCards,
                hotStates,
                decisionPathCards,
                states,
                guideCards,
                List.of(
                        "Suspected tank: ask for paperwork and site clues before anyone prices removal.",
                        "Confirmed tank: compare closure options only after location and basic condition are real facts.",
                        "Leak concern: move quickly into reporting or cleanup guidance instead of treating it like ordinary tank work."
                ),
                List.of(
                        "This site can help you choose the next step, but it cannot prove a property is tank-free.",
                        "Missing paperwork can still mean real sale risk.",
                        "Official state guidance and paid service recommendations are kept separate.",
                        "The public pages stay narrow so they stay useful under deadline."
                ),
                audienceCards,
                List.of(
                        "What to request before anyone argues about credits, price, or tank removal.",
                        "Which document or site check matters first.",
                        "Whether you should stay in records, order a sweep, or move into cleanup."
                ),
                trackedSourceCount
        );
    }

    public PageModels.StaticPageModel staticPage(String slug) {
        return switch (slug) {
            case "about" -> new PageModels.StaticPageModel(
                    meta(
                            "About Buried Oil Tank Verdict | Buried Oil Tank Verdict",
                            "What this buried oil tank decision-support site does, what it does not do, and how current coverage is limited.",
                            "/about/",
                            true,
                            breadcrumbPageSchemas(
                                    breadcrumbs("About", "/about/"),
                                    webpageSchema(
                                            "About Buried Oil Tank Verdict",
                                            "What this buried oil tank decision-support site does, what it does not do, and how current coverage is limited.",
                                            "/about/"
                                    )
                            )
                    ),
                    "What this site is for",
                    "Buried Oil Tank Verdict helps buyers, sellers, owners, and advisors figure out the next practical step after a buried heating-oil tank concern.",
                            List.of(
                                    "It is built for live situations: under-contract sales, missing permits, suspected tanks, and possible leak signals.",
                                    "It focuses on the first useful questions: paperwork, disclosure, sweep timing, and state rules.",
                                    "It organizes pages by state because closure, reporting, and cleanup rules can change.",
                                    "It is not a government office, law firm, or environmental consultant.",
                                    "Each page is checked against current public sources and kept inside clear limits."
                            ),
                            breadcrumbs("About", "/about/"),
                            null
            );
            case "methodology" -> new PageModels.StaticPageModel(
                    meta(
                            "Methodology for Buried Oil Tank Pages | Buried Oil Tank Verdict",
                            "How state-first buried oil tank routes, source stacks, and evidence-first guidance are separated from service routing.",
                            "/methodology/",
                            true,
                            breadcrumbPageSchemas(
                                    breadcrumbs("Methodology", "/methodology/"),
                                    webpageSchema(
                            "Methodology for Buried Oil Tank Pages",
                            "How state-first buried oil tank pages are built from public sources and kept inside clear scope limits.",
                                            "/methodology/"
                                    )
                            )
                    ),
                    "How we build each page",
                    "Every public page starts with the state source, then narrows into the question a buyer, seller, or owner actually has.",
                    List.of(
                            "We separate suspected tank, confirmed tank, and leak concern because they do not have the same next step.",
                            "We start with permits, disclosure, and site facts before talking removal, cleanup, or cost.",
                            "State environmental and homeowner guidance outrank every secondary source.",
                            "Cost and cleanup pages stay directional unless the public documents support more.",
                            "Every page gets a source check, review date, and scope check before it stays public."
                    ),
                    breadcrumbs("Methodology", "/methodology/"),
                    null
            );
            case "contact" -> new PageModels.StaticPageModel(
                    meta(
                            "Contact Buried Oil Tank Verdict | Buried Oil Tank Verdict",
                            "How to send buried oil tank source corrections, route feedback, and editorial updates.",
                            "/contact/",
                            true,
                            breadcrumbPageSchemas(
                                    breadcrumbs("Contact", "/contact/"),
                                    webpageSchema(
                                            "Contact Buried Oil Tank Verdict",
                                            "How to send buried oil tank source corrections, route feedback, and editorial updates.",
                                            "/contact/"
                                    )
                            )
                    ),
                    "Contact",
                    "Use a scenario page for next-step help. Use this page for source corrections, stale links, or launch questions.",
                    List.of(
                            "Email shinhyeok22@gmail.com for source corrections, stale links, or launch questions.",
                            "Send state source updates, stale PDFs, or broken links.",
                            "Flag any county, town, or local agency rule that changes the answer.",
                            "Use scenario-page forms when you need the next document request or first action on a live property."
                    ),
                    breadcrumbs("Contact", "/contact/"),
                    CONTACT_EMAIL
            );
            case "privacy" -> new PageModels.StaticPageModel(
                    meta(
                            "Privacy | Buried Oil Tank Verdict",
                            "How next-step checklist requests and event data are stored in the first release.",
                            "/privacy/",
                            true,
                            breadcrumbPageSchemas(
                                    breadcrumbs("Privacy", "/privacy/"),
                                    webpageSchema(
                                            "Privacy",
                            "How next-step checklist requests and event data are stored in the first release.",
                                            "/privacy/"
                                    )
                            )
                    ),
                    "Privacy",
                    "This release stores checklist requests and event logs in simple file-backed storage. There are no user accounts.",
                    List.of(
                            "Lead capture is email-first and does not require a phone number.",
                            "Event logging keeps page, state, and scenario context so the team can improve the guidance.",
                            "This release does not include user accounts or a broad provider marketplace."
                    ),
                    breadcrumbs("Privacy", "/privacy/"),
                    null
            );
            case "terms" -> new PageModels.StaticPageModel(
                    meta(
                            "Terms | Buried Oil Tank Verdict",
                            "Use conditions for this informational buried oil tank decision-support product.",
                            "/terms/",
                            true,
                            breadcrumbPageSchemas(
                                    breadcrumbs("Terms", "/terms/"),
                                    webpageSchema(
                                            "Terms",
                                            "Use conditions for this informational buried oil tank decision-support product.",
                                            "/terms/"
                                    )
                            )
                    ),
                    "Terms",
                    "The site is informational. It can help you choose the next step, but it does not replace licensed professionals or legal advice.",
                    List.of(
                            "Use licensed contractors, environmental professionals, or attorneys where the scenario requires it.",
                            "Do not treat a general page as proof that a property is clean or tank-free.",
                            "Verify current state guidance when reporting, permits, or cleanup obligations may apply."
                    ),
                    breadcrumbs("Terms", "/terms/"),
                    null
            );
            case "not-government-affiliated" -> new PageModels.StaticPageModel(
                    meta(
                            "Not Government Affiliated | Buried Oil Tank Verdict",
                            "Why this buried oil tank site separates official guidance from editorial routing and current coverage limits.",
                            "/not-government-affiliated/",
                            true,
                            breadcrumbPageSchemas(
                                    breadcrumbs("Not government affiliated", "/not-government-affiliated/"),
                                    webpageSchema(
                                            "Not Government Affiliated",
                                            "Why this buried oil tank site separates official guidance from editorial routing and current coverage limits.",
                                            "/not-government-affiliated/"
                                    )
                            )
                    ),
                    "Not government affiliated",
                    "Buried Oil Tank Verdict is an independent editorial decision-support site. It is not a state agency, cleanup fund, or municipal program.",
                    List.of(
                            "Official links appear on public pages so you can confirm the underlying rule yourself.",
                            "Paid help and editorial guidance are kept separate.",
                            "When the public source is thin or unclear, the page stays narrower."
                    ),
                    breadcrumbs("Not government affiliated", "/not-government-affiliated/"),
                    null
            );
            default -> throw new IllegalArgumentException("Unknown static page: " + slug);
        };
    }

    public PageModels.HubPageModel statesHubPage() {
        List<PageModels.LinkCard> cards = repository.states().stream()
                .filter(StateRecord::launchReady)
                .map(state -> new PageModels.LinkCard(
                        state.name(),
                        state.quickAnswer(),
                        "/states/" + state.slug() + "/",
                        state.abbreviation()
                ))
                .toList();
        List<PageModels.Breadcrumb> breadcrumbs = breadcrumbs("States", "/states/");
        return new PageModels.HubPageModel(
                meta(
                        "Buried Oil Tank State Pages | Buried Oil Tank Verdict",
                        "State-specific buried oil tank disclosure, records, sweep, and closing guidance for NJ, NY, CT, and ME.",
                        "/states/",
                        true,
                        breadcrumbPageSchemas(
                                breadcrumbs,
                                collectionPageSchema(
                                        "Buried Oil Tank State Pages",
                                        "State-specific buried oil tank disclosure, records, sweep, and closing guidance for NJ, NY, CT, and ME.",
                                        "/states/",
                                        cards
                                )
                        )
                ),
                "states",
                "State pages",
                "Buried oil tank state pages for disclosure, records, and closing steps",
                "Use the state page when disclosure, missing paperwork, or a sweep decision depends on the state's rules. Public launch coverage is NJ, NY, CT, and ME.",
                "Current launch states",
                "Choose the state that controls the next permit search or document request.",
                cards,
                "How to use these pages",
                List.of(
                        "Coverage is currently limited to NJ, NY, CT, and ME public state pages.",
                        "Every state page shows review dates and official links before the checklist opens.",
                        "Use a state page when permits, local practice, or agency language may change the next step."
                ),
                breadcrumbs
        );
    }

    public PageModels.HubPageModel guidesHubPage() {
        List<PageModels.LinkCard> cards = repository.guides().stream()
                .filter(GuideRecord::indexable)
                .map(guide -> new PageModels.LinkCard(
                        guideHeading(guide),
                        guideMetaDescription(guide),
                        "/guides/" + guide.slug() + "/",
                        "Guide"
                ))
                .toList();
        List<PageModels.Breadcrumb> breadcrumbs = breadcrumbs("Guides", "/guides/");
        return new PageModels.HubPageModel(
                meta(
                        "Buried Oil Tank Guides | Buried Oil Tank Verdict",
                        "Buried oil tank guides for home sale, records, sweep, removal, leak, and cost questions before closing.",
                        "/guides/",
                        true,
                        breadcrumbPageSchemas(
                                breadcrumbs,
                                collectionPageSchema(
                                        "Buried Oil Tank Guides",
                                        "Buried oil tank guides for home sale, records, sweep, removal, leak, and cost questions before closing.",
                                        "/guides/",
                                        cards
                                )
                        )
                ),
                "guides",
                "Cross-state guides",
                "Buried oil tank guides for records, sweep, removal, leak, and cost questions",
                "Use a guide when you still need the first smart move before permits, local rules, or agency language make the answer state-specific.",
                "Public route guides",
                "Start with the guide that matches the question in front of you.",
                cards,
                "What belongs in guides first",
                List.of(
                        "Guides answer cross-state questions before a single state page can carry the whole answer.",
                        "Removal, leak, and cost guides stay public, but they still send you back to the state page as soon as state rules take over.",
                        "Move from a guide into a state page as soon as location, permits, or reporting language becomes specific."
                ),
                breadcrumbs
        );
    }

    public PageModels.HubPageModel routesHubPage() {
        List<PageModels.LinkCard> cards = routeGuideCards();
        List<PageModels.Breadcrumb> breadcrumbs = breadcrumbs("Routes", "/routes/");
        return new PageModels.HubPageModel(
                meta(
                        "Buried Oil Tank Route Guides | Buried Oil Tank Verdict",
                        "Buried oil tank route guides for disclosure, records, sweep, removal, leak, and cost questions before closing.",
                        "/routes/",
                        true,
                        breadcrumbPageSchemas(
                                breadcrumbs,
                                collectionPageSchema(
                                        "Buried Oil Tank Route Guides",
                                        "Buried oil tank route guides for disclosure, records, sweep, removal, leak, and cost questions before closing.",
                                        "/routes/",
                                        cards
                                )
                        )
                ),
                "routes",
                "Route guides",
                "Buried oil tank route guides for disclosure, records, sweep, removal, leak, and cost direction",
                "Use this hub when you already know the question family and need the right first page before the answer turns state-specific.",
                "Route families",
                "Choose the question family that matches what you know.",
                cards,
                "How these routes work",
                List.of(
                        "First-step guides focus on sale pressure, sweep timing, and missing paperwork.",
                        "Later-step guides cover removal, leak, and cost only after the facts justify them.",
                        "Go to the state page as soon as permits, local rules, or cleanup language control the next step."
                ),
                breadcrumbs
        );
    }

    public PageModels.StatePageModel statePage(String stateSlug) {
        StateRecord state = repository.requireState(stateSlug);
        LocalDate today = LocalDate.now(clock);
        List<PageModels.Breadcrumb> breadcrumbs = breadcrumbs(state.name(), "/states/" + state.slug() + "/");
        List<RouteFamily> stateHubFamilies = List.of(
                RouteFamily.BUYER_SELLER,
                RouteFamily.SWEEP_AND_LOCATE,
                RouteFamily.RECORDS_AND_PROOF
        );
        List<PageModels.LinkCard> routeCards = stateHubFamilies.stream()
                .map(family -> routeInventoryService.findStateRoute(stateSlug, family)
                        .orElseThrow(() -> new IllegalArgumentException("Missing state hub route: " + stateSlug + " / " + family.slug())))
                .map(entry -> new PageModels.LinkCard(
                        entry.routeFamily().displayLabel(),
                        routeDescription(state, entry.routeFamily()),
                        entry.path(),
                        routeLinkBadge(entry)
                ))
                .toList();

        return new PageModels.StatePageModel(
                meta(
                        stateMetaTitle(state),
                        stateMetaDescription(state),
                        "/states/" + state.slug() + "/",
                        state.launchReady(),
                        breadcrumbPageSchemas(
                                breadcrumbs,
                                webpageSchema(
                                        stateHeading(state),
                                        stateMetaDescription(state),
                                        "/states/" + state.slug() + "/"
                                )
                        )
                ),
                stateHeading(state),
                state,
                routeCards,
                ctaFor(
                        state.slug() + ":" + RouteFamily.OVERVIEW.slug(),
                        "/states/" + state.slug() + "/",
                        RouteFamily.OVERVIEW,
                        List.of(new PageModels.StateOption(state.slug(), state.name()))
                ),
                breadcrumbs,
                stateAudienceCards(state),
                stateTodayQuestions(state),
                sourceReview(
                        state.freshnessStatus(today),
                        state.verifiedOn(),
                        state.nextReviewOn(),
                        state.name() + " state page"
                )
        );
    }

    public PageModels.RoutePageModel routePage(String stateSlug, RouteFamily family) {
        StateRecord state = repository.requireState(stateSlug);
        LocalDate today = LocalDate.now(clock);
        RouteInventoryEntry route = routeInventoryService.findStateRoute(stateSlug, family)
                .orElseThrow(() -> new IllegalArgumentException("Unknown route: " + stateSlug + " / " + family.slug()));
        List<PageModels.Breadcrumb> breadcrumbs = breadcrumbs(state.name(), "/states/" + state.slug() + "/", family.displayLabel(), route.path());

        List<PageModels.LinkCard> nextLinks = family.likelyNextSteps().stream()
                .map(next -> routeInventoryService.findStateRoute(stateSlug, next)
                        .map(entry -> new PageModels.LinkCard(
                                entry.routeFamily().displayLabel(),
                                routeDescription(state, next),
                                entry.path(),
                                routeLinkBadge(entry)
                        ))
                        .orElse(null))
                .filter(link -> link != null)
                .toList();

        return new PageModels.RoutePageModel(
                meta(
                        routeMetaTitle(state, family),
                        routeMetaDescription(state, family),
                        route.path(),
                        route.isIndexable(),
                        breadcrumbPageSchemas(
                                breadcrumbs,
                                webpageSchema(
                                        routeHeading(state, family),
                                        routeMetaDescription(state, family),
                                        route.path()
                                )
                        )
                ),
                routeHeading(state, family),
                state,
                route,
                routeQuickAnswer(state, family),
                startHereChecklist(state, family),
                whyItMatters(state, family),
                evidenceChecklist(state, family),
                whatNotToAssume(family),
                costAndTimelineNotes(state, family),
                nextLinks,
                ctaFor(route.id(), route.path(), family, List.of(new PageModels.StateOption(state.slug(), state.name()))),
                breadcrumbs,
                state.sourceStack(),
                routeAudienceCards(state, family),
                next24Hours(state, family),
                todayQuestions(state, family),
                sourceReview(route.sourceFreshnessStatus(), route.verifiedOn(), route.nextReviewOn(), route.title())
        );
    }

    public PageModels.GuidePageModel guidePage(String slug) {
        GuideRecord guide = repository.requireGuide(slug);
        LocalDate today = LocalDate.now(clock);
        List<PageModels.Breadcrumb> breadcrumbs = breadcrumbs("Guides", "/guides/", guideHeading(guide), "/guides/" + guide.slug() + "/");
        List<PageModels.StateCard> stateEntries = guide.stateSlugs().stream()
                .map(repository::requireState)
                .filter(StateRecord::launchReady)
                .map(state -> new PageModels.StateCard(
                        state.name(),
                        state.slug(),
                        state.quickAnswer(),
                        guide.primaryRouteFamily().pathForState(state.slug()),
                        state.commonTriggers().stream().limit(2).toList()
                ))
                .toList();

        List<PageModels.LinkCard> routeLinks = stateEntries.stream()
                .map(stateCard -> new PageModels.LinkCard(
                        stateCard.name() + " " + guide.primaryRouteFamily().displayLabel(),
                        "Move from the guide into the state-specific next step.",
                        stateCard.entryPath(),
                        "State page"
                ))
                .toList();

        List<PageModels.StateOption> stateOptions = stateEntries.stream()
                .map(stateCard -> new PageModels.StateOption(stateCard.slug(), stateCard.name()))
                .toList();

        return new PageModels.GuidePageModel(
                meta(
                        guideHeading(guide) + " | Buried Oil Tank Verdict",
                        guideMetaDescription(guide),
                        "/guides/" + guide.slug() + "/",
                        guide.indexable(),
                        breadcrumbPageSchemas(
                                breadcrumbs,
                                articleSchema(
                                        guideHeading(guide),
                                        guideMetaDescription(guide),
                                        "/guides/" + guide.slug() + "/",
                                        guide.verifiedOn()
                                )
                        )
                ),
                guideHeading(guide),
                guide,
                stateEntries,
                routeLinks,
                ctaFor("guide:" + guide.slug(), "/guides/" + guide.slug() + "/", guide.primaryRouteFamily(), stateOptions),
                breadcrumbs,
                guideAudienceCards(guide),
                guideTakeaways(guide),
                sourceReview(guide.freshnessStatus(today), guide.verifiedOn(), guide.nextReviewOn(), guide.title())
        );
    }

    private PageModels.PageMeta meta(String title, String description, String path, boolean indexable) {
        return meta(title, description, path, indexable, List.of());
    }

    private PageModels.PageMeta meta(String title, String description, String path, boolean indexable, List<String> structuredDataJson) {
        return new PageModels.PageMeta(
                title,
                description,
                baseUrl.resolve(path).toString(),
                indexable,
                structuredDataJson,
                baseUrl.resolve("/og-default.png").toString(),
                "Buried Oil Tank Verdict site preview",
                analyticsMeasurementId
        );
    }

    private static List<PageModels.Breadcrumb> breadcrumbs(String label, String path) {
        return List.of(
                new PageModels.Breadcrumb("Home", "/"),
                new PageModels.Breadcrumb(label, path)
        );
    }

    private static List<PageModels.Breadcrumb> breadcrumbs(String stateLabel, String statePath, String label, String path) {
        return List.of(
                new PageModels.Breadcrumb("Home", "/"),
                new PageModels.Breadcrumb(stateLabel, statePath),
                new PageModels.Breadcrumb(label, path)
        );
    }

    private static String stateHeading(StateRecord state) {
        return state.name() + " buried oil tank next steps before closing";
    }

    private static String stateMetaTitle(StateRecord state) {
        return state.name() + " buried oil tank disclosure and next steps | Buried Oil Tank Verdict";
    }

    private static String stateMetaDescription(StateRecord state) {
        return "State-specific buried oil tank disclosure, records, sweep, and next-step guidance before closing in " + state.name() + ".";
    }

    private static String routeHeading(StateRecord state, RouteFamily family) {
        return switch (family) {
            case OVERVIEW -> stateHeading(state);
            case BUYER_SELLER -> state.name() + " buried oil tank disclosure steps for buyers and sellers";
            case SWEEP_AND_LOCATE -> state.name() + " oil tank sweep and locate steps before closing";
            case RECORDS_AND_PROOF -> state.name() + " abandoned oil tank records and proof";
            case REMOVAL_VS_ABANDONMENT -> state.name() + " remove or abandon a buried oil tank";
            case LEAK_AND_CLEANUP -> state.name() + " heating oil tank leak and cleanup steps";
            case COST_DIRECTION -> state.name() + " buried oil tank cost direction";
        };
    }

    private static String routeMetaTitle(StateRecord state, RouteFamily family) {
        return routeHeading(state, family) + " | Buried Oil Tank Verdict";
    }

    private static String routeMetaDescription(StateRecord state, RouteFamily family) {
        return switch (family) {
            case OVERVIEW -> stateMetaDescription(state);
            case BUYER_SELLER -> "Buried oil tank disclosure, records, and next-step guidance for buyers and sellers before closing in " + state.name() + ".";
            case SWEEP_AND_LOCATE -> "When to order a buried oil tank sweep or locate before closing in " + state.name() + ".";
            case RECORDS_AND_PROOF -> "How to find abandoned oil tank records, closure proof, and missing paperwork in " + state.name() + ".";
            case REMOVAL_VS_ABANDONMENT -> "How to think about removal versus abandonment for a confirmed buried oil tank in " + state.name() + ".";
            case LEAK_AND_CLEANUP -> "What changes when a buried heating oil tank problem in " + state.name() + " may already be a leak or cleanup case.";
            case COST_DIRECTION -> "How to think about buried oil tank cost ranges in " + state.name() + " only after the route and evidence are clear.";
        };
    }

    private static String guideHeading(GuideRecord guide) {
        return switch (guide.slug()) {
            case "buried-oil-tank-home-sale" -> "Can You Sell a House With a Buried Oil Tank Before Closing?";
            case "abandoned-oil-tank-records" -> "How to Find Abandoned Oil Tank Records Before Closing";
            case "oil-tank-sweep-before-buying-house" -> "When to Order an Oil Tank Sweep Before Buying a House";
            case "leaking-heating-oil-tank-what-to-do" -> "What to Do if a Heating Oil Tank May Be Leaking";
            case "oil-tank-removal-cost" -> "Buried Oil Tank Removal Cost: How to Think About the Range";
            case "remove-vs-abandon-oil-tank" -> "Remove or Abandon a Buried Oil Tank?";
            default -> guide.title();
        };
    }

    private static String guideMetaDescription(GuideRecord guide) {
        return switch (guide.slug()) {
            case "buried-oil-tank-home-sale" -> "Buried oil tank home sale guidance for buyers, sellers, agents, and attorneys before closing.";
            case "abandoned-oil-tank-records" -> "How to find abandoned oil tank records, closure proof, and missing paperwork before closing.";
            case "oil-tank-sweep-before-buying-house" -> "When to order an oil tank sweep before buying a house and when records should come first.";
            case "leaking-heating-oil-tank-what-to-do" -> "What to do when a buried heating oil tank question may already be a leak or cleanup problem.";
            case "oil-tank-removal-cost" -> "How to think about buried oil tank removal cost after the state, route, and evidence are clear.";
            case "remove-vs-abandon-oil-tank" -> "How to think about removing or abandoning a buried oil tank after the tank is confirmed.";
            default -> guide.summary();
        };
    }

    private List<PageModels.LinkCard> routeGuideCards() {
        List<RouteFamily> families = List.of(
                RouteFamily.BUYER_SELLER,
                RouteFamily.SWEEP_AND_LOCATE,
                RouteFamily.RECORDS_AND_PROOF,
                RouteFamily.REMOVAL_VS_ABANDONMENT,
                RouteFamily.LEAK_AND_CLEANUP,
                RouteFamily.COST_DIRECTION
        );
        return families.stream()
                .map(this::routeGuideCard)
                .toList();
    }

    private PageModels.LinkCard routeGuideCard(RouteFamily family) {
        GuideRecord guide = repository.guides().stream()
                .filter(candidate -> candidate.primaryRouteFamily() == family)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Missing guide for route family: " + family.slug()));
        return new PageModels.LinkCard(
                family.displayLabel(),
                guideMetaDescription(guide),
                "/guides/" + guide.slug() + "/",
                switch (family) {
                    case BUYER_SELLER, SWEEP_AND_LOCATE, RECORDS_AND_PROOF -> "Core route";
                    case REMOVAL_VS_ABANDONMENT, LEAK_AND_CLEANUP, COST_DIRECTION -> "Support route";
                    case OVERVIEW -> "State hub";
                }
        );
    }

    private List<String> breadcrumbPageSchemas(List<PageModels.Breadcrumb> breadcrumbs, Map<String, Object> pageSchema) {
        return List.of(jsonLd(breadcrumbSchema(breadcrumbs)), jsonLd(pageSchema));
    }

    private String jsonLd(Map<String, Object> schema) {
        try {
            return objectMapper.writeValueAsString(schema);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to render JSON-LD", exception);
        }
    }

    private Map<String, Object> siteSchema() {
        Map<String, Object> schema = baseSchema("WebSite");
        schema.put("name", "Buried Oil Tank Verdict");
        schema.put("url", baseUrl.resolve("/").toString());
        schema.put("description", "Buried oil tank disclosure, records, sweep, and next-step guidance before closing.");
        schema.put("publisher", siteOrganization());
        return schema;
    }

    private Map<String, Object> collectionPageSchema(String title, String description, String path, List<PageModels.LinkCard> cards) {
        Map<String, Object> schema = webpageSchema("CollectionPage", title, description, path);
        schema.put("mainEntity", itemList(cards));
        return schema;
    }

    private Map<String, Object> webpageSchema(String title, String description, String path) {
        return webpageSchema("WebPage", title, description, path);
    }

    private Map<String, Object> webpageSchema(String type, String title, String description, String path) {
        Map<String, Object> schema = baseSchema(type);
        schema.put("name", title);
        schema.put("description", description);
        schema.put("url", baseUrl.resolve(path).toString());
        schema.put("author", routingDesk());
        schema.put("editor", sourceReviewDesk());
        schema.put("publisher", siteOrganization());
        schema.put("isPartOf", Map.of(
                "@type", "WebSite",
                "name", "Buried Oil Tank Verdict",
                "url", baseUrl.resolve("/").toString()
        ));
        return schema;
    }

    private Map<String, Object> articleSchema(String title, String description, String path, LocalDate verifiedOn) {
        Map<String, Object> schema = baseSchema("Article");
        schema.put("headline", title);
        schema.put("description", description);
        schema.put("url", baseUrl.resolve(path).toString());
        schema.put("mainEntityOfPage", baseUrl.resolve(path).toString());
        schema.put("author", routingDesk());
        schema.put("editor", sourceReviewDesk());
        schema.put("publisher", siteOrganization());
        if (verifiedOn != null) {
            schema.put("dateModified", verifiedOn.toString());
        }
        schema.put("isPartOf", Map.of(
                "@type", "WebSite",
                "name", "Buried Oil Tank Verdict",
                "url", baseUrl.resolve("/").toString()
        ));
        return schema;
    }

    private Map<String, Object> breadcrumbSchema(List<PageModels.Breadcrumb> breadcrumbs) {
        Map<String, Object> schema = baseSchema("BreadcrumbList");
        List<Map<String, Object>> items = Stream.iterate(0, index -> index + 1)
                .limit(breadcrumbs.size())
                .map(index -> {
                    PageModels.Breadcrumb breadcrumb = breadcrumbs.get(index);
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("@type", "ListItem");
                    item.put("position", index + 1);
                    item.put("name", breadcrumb.label());
                    item.put("item", baseUrl.resolve(breadcrumb.path()).toString());
                    return item;
                })
                .toList();
        schema.put("itemListElement", items);
        return schema;
    }

    private Map<String, Object> itemList(List<PageModels.LinkCard> cards) {
        Map<String, Object> itemList = new LinkedHashMap<>();
        itemList.put("@type", "ItemList");
        itemList.put("itemListElement", Stream.iterate(0, index -> index + 1)
                .limit(cards.size())
                .map(index -> {
                    PageModels.LinkCard card = cards.get(index);
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("@type", "ListItem");
                    item.put("position", index + 1);
                    item.put("name", card.title());
                    item.put("url", baseUrl.resolve(card.href()).toString());
                    return item;
                })
                .toList());
        return itemList;
    }

    private static Map<String, Object> baseSchema(String type) {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("@context", "https://schema.org");
        schema.put("@type", type);
        return schema;
    }

    private Map<String, Object> siteOrganization() {
        return Map.of(
                "@type", "Organization",
                "name", "Buried Oil Tank Verdict",
                "url", baseUrl.resolve("/").toString(),
                "email", "mailto:" + CONTACT_EMAIL,
                "description", "Editorial decision-support product for buried and abandoned residential heating oil tank questions before closing."
        );
    }

    private static Map<String, Object> routingDesk() {
        return Map.of(
                "@type", "Organization",
                "name", "Buried Oil Tank Verdict Routing Desk",
                "description", "Virtual editorial desk that drafts scenario routing, query framing, and next-step structure."
        );
    }

    private static Map<String, Object> sourceReviewDesk() {
        return Map.of(
                "@type", "Organization",
                "name", "Buried Oil Tank Verdict Source Review Desk",
                "description", "Virtual editorial desk that checks official state sources, review dates, and overreach risk before publication."
        );
    }

    private PageModels.CtaModel ctaFor(String pageId, String path, RouteFamily family, List<PageModels.StateOption> states) {
        return new PageModels.CtaModel(
                "Get the next-step checklist for this property",
                "Get the checklist",
                family.defaultPartnerType().helperText(),
                "Use the checklist to decide what to request next, whether a sweep belongs, and who needs the facts first.",
                ctaPreviewItems(family),
                "Email is required. Phone is optional. The checklist is informational and may point you back to official state sources or licensed professionals. It does not confirm that a property is tank-free, cleared, or legally compliant.",
                pageId,
                path,
                family.isOverview() ? RouteFamily.BUYER_SELLER.slug() : family.slug(),
                family.defaultScenario(),
                family.defaultPartnerType(),
                states,
                List.of(Scenario.BUYER_SELLER, Scenario.SWEEP_FIRST, Scenario.RECORDS_FIRST, Scenario.REMOVAL_DECISION, Scenario.LEAK_CONCERN)
        );
    }

    private static String routeLinkBadge(RouteInventoryEntry entry) {
        return switch (entry.routeFamily()) {
            case BUYER_SELLER, SWEEP_AND_LOCATE, RECORDS_AND_PROOF -> "Core route";
            case REMOVAL_VS_ABANDONMENT, LEAK_AND_CLEANUP, COST_DIRECTION -> "Support route";
            case OVERVIEW -> "State hub";
        };
    }

    private static List<String> ctaPreviewItems(RouteFamily family) {
        return switch (family) {
            case OVERVIEW -> List.of(
                    "Which deadline matters first.",
                    "Which document request or sweep question should happen first.",
                    "Whether you should stay in paperwork review, order a sweep, or move into cleanup."
            );
            case BUYER_SELLER -> List.of(
                    "What to request before credits, price, or disclosure language harden into a story.",
                    "What still needs proof before anyone treats the tank question as confirmed.",
                    "Whether you should stay in sale-side triage or move into records or sweep work."
            );
            case SWEEP_AND_LOCATE -> List.of(
                    "What site clue or paperwork gap actually justifies a sweep or locate.",
                    "What to capture before you book field work.",
                    "Which result keeps this in verification and which result changes the next step."
            );
            case RECORDS_AND_PROOF -> List.of(
                    "Which permit, closure record, or oil-to-gas document matters most first.",
                    "What missing proof still leaves the property unresolved.",
                    "Which record language would change the next step."
            );
            case REMOVAL_VS_ABANDONMENT -> List.of(
                    "What facts have to be confirmed before removal or abandonment is a real choice.",
                    "Which state rule or closure path controls the decision.",
                    "What signal would move this out of closure planning and into cleanup review."
            );
            case LEAK_AND_CLEANUP -> List.of(
                    "What leak or contamination signal actually changed the situation.",
                    "Which reporting or remediation step may now control the timeline.",
                    "Who needs the facts first before the problem gets any wider."
            );
            case COST_DIRECTION -> List.of(
                    "Which scenario is actually being priced: sweep, closure, removal, or cleanup.",
                    "Which missing fact could still move the range the most.",
                    "What has to be verified before any budget number is honest."
            );
        };
    }

    private static String routeDescription(StateRecord state, RouteFamily family) {
        return switch (family) {
            case OVERVIEW -> state.quickAnswer();
            case BUYER_SELLER -> state.buyerSellerSummary();
            case SWEEP_AND_LOCATE -> state.sweepFirstSummary();
            case RECORDS_AND_PROOF -> state.recordLookupSummary();
            case REMOVAL_VS_ABANDONMENT -> state.removalVsAbandonmentSummary();
            case LEAK_AND_CLEANUP -> state.leakReportingSummary();
            case COST_DIRECTION -> state.insuranceCleanupSummary();
        };
    }

    private static String routeQuickAnswer(StateRecord state, RouteFamily family) {
        return switch (family) {
            case OVERVIEW -> state.quickAnswer();
            case BUYER_SELLER -> "In " + state.name() + ", a buried-tank issue during a sale usually means: get the paperwork first, confirm what is actually known, and only then talk credits or removal.";
            case SWEEP_AND_LOCATE -> "In " + state.name() + ", a sweep often makes sense when site clues are stronger than the paperwork.";
            case RECORDS_AND_PROOF -> "In " + state.name() + ", missing paperwork is a reason to verify more, not proof that no tank exists.";
            case REMOVAL_VS_ABANDONMENT -> "In " + state.name() + ", removal versus abandonment depends on confirmed tank conditions and the state closure path.";
            case LEAK_AND_CLEANUP -> "In " + state.name() + ", leak concern may shift the case from routine contractor work into reporting and cleanup workflow.";
            case COST_DIRECTION -> "In " + state.name() + ", cost ranges only help after you know whether this is a sweep, closure, removal, or cleanup case.";
        };
    }

    private static List<String> whyItMatters(StateRecord state, RouteFamily family) {
        return switch (family) {
            case OVERVIEW -> List.of(
                    state.buyerSellerSummary(),
                    state.recordLookupSummary(),
                    firstOrFallback(state.strongestTriggerStates(), state.sweepFirstSummary())
            );
            case BUYER_SELLER -> List.of(
                    state.buyerSellerSummary(),
                    firstOrFallback(state.strongestTriggerStates(), state.recordLookupSummary()),
                    "Transaction timing often matters as much as the contractor quote because the wrong assumption may distort negotiations."
            );
            case SWEEP_AND_LOCATE -> List.of(
                    state.sweepFirstSummary(),
                    firstOrFallback(state.firstMoves(), state.recordLookupSummary()),
                    "A locate-first path may keep you from treating an unconfirmed tank as a removal job too early."
            );
            case RECORDS_AND_PROOF -> List.of(
                    state.recordLookupSummary(),
                    firstOrFallback(state.documentTargets(), "Permit gaps, prior fuel use, and closure documents may change the next step quickly.")
            );
            case REMOVAL_VS_ABANDONMENT -> List.of(
                    state.removalVsAbandonmentSummary(),
                    firstOrFallback(state.escalationSignals(), state.leakReportingSummary()),
                    "The answer changes once the tank is confirmed, accessed, and tied back to the state closure process."
            );
            case LEAK_AND_CLEANUP -> List.of(
                    state.leakReportingSummary(),
                    state.insuranceCleanupSummary(),
                    firstOrFallback(state.escalationSignals(), "The route changes once the facts point past simple closure.")
            );
            case COST_DIRECTION -> List.of(
                    "Directional cost only becomes honest after verification, route choice, and state process are clear.",
                    state.insuranceCleanupSummary(),
                    firstOrFallback(state.documentTargets(), "The paperwork and site facts change the range more than a general article can.")
            );
        };
    }

    private static List<String> evidenceChecklist(StateRecord state, RouteFamily family) {
        return switch (family) {
            case OVERVIEW -> List.of(
                    firstOrFallback(state.documentTargets(), "Any prior heating-fuel record, permit, or closure document"),
                    "Visible vent or fill pipes, patched foundation openings, or abandoned lines",
                    firstOrFallback(state.commonTriggers(), "Inspection notes that raise a buried-tank question before closing")
            );
            case BUYER_SELLER -> List.of(
                    "Closing timeline and any inspection contingency deadlines",
                    firstOrFallback(state.documentTargets(), "Seller disclosure language and any prior tank paperwork"),
                    "Whether the tank is suspected, confirmed, or tied to a leak concern"
            );
            case SWEEP_AND_LOCATE -> List.of(
                    "Any sweep or locate result that confirms, weakens, or rules out a buried tank on the site",
                    "Site clues that support or weaken the buried-tank theory",
                    secondOrFallback(state.documentTargets(), "Any record or inspection note that ties the site back to prior oil heat or closure work")
            );
            case RECORDS_AND_PROOF -> List.of(
                    firstOrFallback(state.documentTargets(), "Permit and closure records"),
                    secondOrFallback(state.documentTargets(), "Heating-fuel conversion history"),
                    thirdOrFallback(state.documentTargets(), "Any document that shows reporting, cleanup, or no-further-action language")
            );
            case REMOVAL_VS_ABANDONMENT -> List.of(
                    "A confirmed tank location and basic condition assessment",
                    firstOrFallback(state.documentTargets(), "State or delegated local guidance on allowed closure methods"),
                    firstOrFallback(state.escalationSignals(), "Any leak indicator that would force a cleanup path")
            );
            case LEAK_AND_CLEANUP -> List.of(
                    firstOrFallback(state.escalationSignals(), "Odor, staining, or sheen evidence"),
                    secondOrFallback(state.escalationSignals(), "Closure paperwork that mentions contamination or a release"),
                    thirdOrFallback(state.escalationSignals(), "Reporting, remediation, or fund documentation")
            );
            case COST_DIRECTION -> List.of(
                    "A verified scenario: sweep, closure, removal, or cleanup",
                    firstOrFallback(state.documentTargets(), "Any state cost reference or official fund guidance"),
                    "Site access or contamination facts that may move the range materially"
            );
        };
    }

    private static List<String> whatNotToAssume(RouteFamily family) {
        return switch (family) {
            case OVERVIEW, BUYER_SELLER -> List.of(
                    "Do not assume missing records prove there is no tank.",
                    "Do not assume a sale problem automatically means immediate removal.",
                    "Do not assume a generic cost article answers the transaction question."
            );
            case SWEEP_AND_LOCATE, RECORDS_AND_PROOF -> List.of(
                    "Do not assume a contractor quote is the same thing as proof.",
                    "Do not assume silence in local files closes the issue.",
                    "Do not assume one old pipe always means an active tank."
            );
            case REMOVAL_VS_ABANDONMENT, COST_DIRECTION -> List.of(
                    "Do not assume removal is always mandatory in every state.",
                    "Do not assume abandonment in place is always acceptable.",
                    "Do not treat a directional range as a firm quote."
            );
            case LEAK_AND_CLEANUP -> List.of(
                    "Do not assume contamination certainty from a general page.",
                    "Do not assume routine closure and cleanup are the same workflow.",
                    "Do not assume a contractor can answer every reporting question without environmental review."
            );
        };
    }

    private static List<String> costAndTimelineNotes(StateRecord state, RouteFamily family) {
        return switch (family) {
            case OVERVIEW, BUYER_SELLER, SWEEP_AND_LOCATE, RECORDS_AND_PROOF -> List.of(
                    "Start with paperwork and verification, not the biggest quote you can find.",
                    "Use the state sources before assuming the timeline forces a removal decision.",
                    firstOrFallback(state.firstMoves(), state.recordLookupSummary())
            );
            case REMOVAL_VS_ABANDONMENT -> List.of(
                    state.removalVsAbandonmentSummary(),
                    "A confirmed release may widen both cost and timeline beyond simple closure work.",
                    firstOrFallback(state.escalationSignals(), state.leakReportingSummary())
            );
            case LEAK_AND_CLEANUP -> List.of(
                    state.leakReportingSummary(),
                    "Cleanup timing may be separate from the contractor schedule for routine closure.",
                    secondOrFallback(state.escalationSignals(), state.insuranceCleanupSummary())
            );
            case COST_DIRECTION -> List.of(
                    "Ranges stay directional because the same state may have very different numbers for sweep, closure, removal, and cleanup.",
                    "Use verification first so the cost question stays honest.",
                    firstOrFallback(state.documentTargets(), state.recordLookupSummary())
            );
        };
    }

    private static List<PageModels.AudienceCard> stateAudienceCards(StateRecord state) {
        return List.of(
                new PageModels.AudienceCard(
                        "Buyer",
                        "Buyer under contract in " + state.name(),
                        "Use this page to protect diligence before anyone turns uncertainty into a price fight.",
                        List.of(
                                "Ask for every disclosure, permit, closure record, and oil-to-gas invoice tied to the property.",
                                "Use the first practical step in this state before anyone collapses the issue into one quote.",
                                firstOrFallback(state.firstMoves(), state.quickAnswer())
                        )
                ),
                new PageModels.AudienceCard(
                        "Seller",
                        "Seller trying to avoid closing delay",
                        "Bring more paperwork to the table than the buyer expects so you are not negotiating from gaps.",
                        List.of(
                                "Separate suspected tank risk from confirmed tank facts before credits get discussed.",
                                firstOrFallback(state.documentTargets(), "Gather the state records that change the answer first."),
                                "Use the page that matches the facts, not the loudest fear."
                        )
                ),
                new PageModels.AudienceCard(
                        "Advisor",
                        "Agent or attorney trying to keep the file moving",
                        "Use the state page to decide the next call before the sale turns into delay, credits, or cleanup panic.",
                        List.of(
                                "Figure out whether the issue belongs in paperwork review, a sweep question, or confirmed tank work.",
                                secondOrFallback(state.documentTargets(), "Check whether local or delegated records add missing proof."),
                                "Carry one clear document request into the next negotiation or attorney-review call."
                        )
                )
        );
    }

    private static List<String> stateTodayQuestions(StateRecord state) {
        return List.of(
                "Can you send every permit, closure, and heating-fuel conversion record tied to this property in " + state.name() + "?",
                "Has the site ever been swept, closed, removed, reported, or tied to a cleanup file in " + state.name() + "?",
                "What deadline controls the next move right now: contract, inspection, attorney review, financing, or closing?"
        );
    }

    private static List<PageModels.AudienceCard> routeAudienceCards(StateRecord state, RouteFamily family) {
        return switch (family) {
            case BUYER_SELLER -> List.of(
                    new PageModels.AudienceCard(
                            "Buyer",
                            "Buyer with a live contingency clock",
                            "Keep the negotiation attached to paperwork and proof so the story does not outrun the facts.",
                            List.of(
                                    "Pull the disclosure, permits, and closure proof before discussing credits.",
                                    "Separate suspected tank, confirmed tank, and leak concern in writing.",
                                    "Use this page to decide whether records or a sweep should happen first."
                            )
                    ),
                    new PageModels.AudienceCard(
                            "Seller",
                            "Seller answering the buried-tank question",
                            "Use proof to narrow the issue before a buyer assumes the broadest possible risk.",
                            List.of(
                                    "Package the available record stack before the next negotiation call.",
                                    "Do not concede removal logic when the evidence still belongs in verification.",
                                    firstOrFallback(state.documentTargets(), state.recordLookupSummary())
                            )
                    ),
                    new PageModels.AudienceCard(
                            "Advisor",
                            "Agent or attorney trying to keep negotiation honest",
                            "Use this page to hold the sale inside proof while the contract clock is still live.",
                            List.of(
                                    "Package the exact document request before the next call with the other side.",
                                    "Resolve weak records or uncertain closure proof before credits harden into narrative.",
                                    "If the paperwork is thin, move next into records or sweep instead of quote collection."
                            )
                    )
            );
            case SWEEP_AND_LOCATE -> List.of(
                    new PageModels.AudienceCard(
                            "Buyer",
                            "Buyer who needs physical confirmation",
                            "This route is for the moment when site clues and paperwork do not agree.",
                            List.of(
                                    "Book a locate or sweep before treating the case as confirmed removal work.",
                                    "Save every visible clue: pipes, patched walls, old lines, inspection notes.",
                                    "Use the sweep result to decide whether this stays a verification problem or becomes something bigger."
                            )
                    ),
                    new PageModels.AudienceCard(
                            "Owner",
                            "Owner preparing to list without clear proof",
                            "A sweep can answer the site question before a pre-listing conversation turns into speculative pricing.",
                            List.of(
                                    "Use physical verification when the record stack is incomplete or the site history is thin.",
                                    "Give the next buyer a cleaner answer than uncertainty.",
                                    "Only widen into closure planning after confirmation."
                            )
                    ),
                    new PageModels.AudienceCard(
                            "Advisor",
                            "Agent or attorney settling the buried-tank theory",
                            "Use this page when the property needs confirmation before the next negotiation or diligence call.",
                            List.of(
                                    "Do the confirmation work before the contract timeline gets filled with assumptions.",
                                    "Do not let one old pipe decide the whole story without verification.",
                                    firstOrFallback(state.firstMoves(), state.sweepFirstSummary())
                            )
                    )
            );
            case RECORDS_AND_PROOF -> List.of(
                    new PageModels.AudienceCard(
                            "Buyer",
                            "Buyer who needs the paper trail",
                            "This page is for missing permits, weak closure proof, and thin history during diligence.",
                            List.of(
                                    "Request permits, closure paperwork, and conversion records together.",
                                    "Treat missing paperwork as unresolved risk until the documents close the gap.",
                                    "Use this page before you jump to pricing or remediation logic."
                            )
                    ),
                    new PageModels.AudienceCard(
                            "Seller",
                            "Seller rebuilding the file",
                            "Your job is to replace uncertainty with documents before the buyer does it with assumptions.",
                            List.of(
                                    "Find every record that narrows whether the tank was closed, removed, or never proven.",
                                    "If the record stack points toward release language, switch routes quickly.",
                                    secondOrFallback(state.documentTargets(), state.recordLookupSummary())
                            )
                    ),
                    new PageModels.AudienceCard(
                            "Advisor",
                            "Agent or attorney rebuilding the proof trail",
                            "Use this page when the deal is live but the paperwork is too thin to support the next call.",
                            List.of(
                                    "Know which missing document changes the answer most in this state.",
                                    "Request the record stack before anyone prices the risk from assumption.",
                                    "Use records first when the tank is not yet physically confirmed."
                            )
                    )
            );
            case REMOVAL_VS_ABANDONMENT -> List.of(
                    new PageModels.AudienceCard(
                            "Confirmed tank",
                            "Owner with a confirmed tank",
                            "Only use this route after the tank is real, located, and tied back to the state closure path.",
                            List.of(
                                    "Confirm the physical condition and access limits before comparing options.",
                                    "Read the state closure logic before you treat abandonment as available.",
                                    "Move out of this route if release evidence points toward cleanup workflow."
                            )
                    ),
                    new PageModels.AudienceCard(
                            "Seller",
                            "Seller with a verified tank issue",
                            "The decision is no longer whether the concern is real. It is which state-valid closure path fits the facts.",
                            List.of(
                                    "Keep buyer pressure separate from actual closure requirements.",
                                    "Use state guidance, not generic contractor language, to compare the options.",
                                    firstOrFallback(state.documentTargets(), state.removalVsAbandonmentSummary())
                            )
                    ),
                    new PageModels.AudienceCard(
                            "Advisor",
                            "Agent or advisor guiding the transaction",
                            "Use this page to stop the conversation from sliding from confirmed tank into assumed contamination.",
                            List.of(
                                    "Confirm whether the route is still closure planning or already cleanup review.",
                                    "Keep the state closure path in the middle of every conversation.",
                                    "Do not sell certainty before the facts earn it."
                            )
                    )
            );
            case LEAK_AND_CLEANUP -> List.of(
                    new PageModels.AudienceCard(
                            "Leak concern",
                            "Owner or buyer facing release signals",
                            "This route exists for staining, odors, sheen, release language, or state reporting risk.",
                            List.of(
                                    "Document the signal that moved the case beyond routine closure.",
                                    "Bring environmental review in early if the facts are moving fast.",
                                    "Use the source stack before treating a general page as reporting advice."
                            )
                    ),
                    new PageModels.AudienceCard(
                            "Seller",
                            "Seller whose issue may now involve remediation",
                            "The goal is to separate ordinary contractor work from a reporting or cleanup workflow.",
                            List.of(
                                    "Treat any release signal as a process problem first, not a pricing problem.",
                                    "Preserve the timeline: what was found, when, and by whom.",
                                    firstOrFallback(state.escalationSignals(), state.leakReportingSummary())
                            )
                    ),
                    new PageModels.AudienceCard(
                            "Advisor",
                            "Agent or attorney triaging the file",
                            "Use this route to decide whether the transaction now depends on environmental process, not just tank work.",
                            List.of(
                                    "Check whether the route changed because of evidence or because of fear.",
                                    "Pull the exact state reporting and cleanup language before giving directional advice.",
                                    "Do not let a general contractor become the only source of truth."
                            )
                    )
            );
            case COST_DIRECTION -> List.of(
                    new PageModels.AudienceCard(
                            "Budgeting",
                            "Owner trying to frame the range honestly",
                            "Use this route only after the scenario is locked. Otherwise the number is noise.",
                            List.of(
                                    "Decide whether the case is sweep, closure, removal, or cleanup first.",
                                    "List the site facts that move the range: access, contamination, schedule, state process.",
                                    "Treat this page as range direction, not quote replacement."
                            )
                    ),
                    new PageModels.AudienceCard(
                            "Seller",
                            "Seller planning around the issue",
                            "Use cost direction after verification so you do not negotiate from the broadest possible number.",
                            List.of(
                                    "Tie every estimate conversation back to the verified route.",
                                    "Do not let generic articles set the frame for a live transaction.",
                                    firstOrFallback(state.documentTargets(), state.insuranceCleanupSummary())
                            )
                    ),
                    new PageModels.AudienceCard(
                            "Advisor",
                            "Advisor translating cost into the deal file",
                            "Your job is to keep cost attached to facts, not fear or internet averages.",
                            List.of(
                                    "Make clear which facts are known and which still need verification.",
                                    "Separate ordinary closure cost from leak or remediation cost.",
                                    "Avoid false precision until the route is settled."
                            )
                    )
            );
            case OVERVIEW -> stateAudienceCards(state);
        };
    }

    private static List<String> next24Hours(StateRecord state, RouteFamily family) {
        return switch (family) {
            case OVERVIEW -> List.of(
                    "Write down the deadline that matters first: inspection, attorney review, financing, or closing.",
                    firstOrFallback(state.firstMoves(), state.quickAnswer()),
                    "Pull the paperwork first before you widen into contractor or cleanup conversations."
            );
            case BUYER_SELLER -> List.of(
                    "Write down the controlling deadline: inspection, attorney review, closing, or listing date.",
                    "Request every disclosure, permit, closure, and fuel-conversion document in one shot.",
                    "Decide whether the next move is paperwork review or a sweep before price talk widens."
            );
            case SWEEP_AND_LOCATE -> List.of(
                    "Book or identify a locate path so you can verify whether a tank is on the site before you ask for removal pricing.",
                    "Photograph the site clues that support the buried-tank theory.",
                    "Use the result to keep the route narrow unless confirmation changes the case."
            );
            case RECORDS_AND_PROOF -> List.of(
                    "Request permit, closure, and conversion records from every likely source.",
                    "List what is missing instead of treating the file as passively clean.",
                    "Switch routes quickly if the paperwork surfaces release or cleanup language."
            );
            case REMOVAL_VS_ABANDONMENT -> List.of(
                    "Confirm the tank is real, located, and assessed before comparing options.",
                    "Check the state closure path that controls the decision in " + state.name() + ".",
                    "Stop and switch routes if release evidence pushes the case into cleanup workflow."
            );
            case LEAK_AND_CLEANUP -> List.of(
                    "Write down the exact leak or contamination signal that changed the route.",
                    "Pull the state reporting and cleanup language before offering directional advice.",
                    "Bring in environmental review early if the facts are moving beyond routine closure."
            );
            case COST_DIRECTION -> List.of(
                    "Lock the scenario first: sweep, closure, removal, or cleanup.",
                    "List the facts that move the range before you compare numbers.",
                    "Use cost direction as a planning tool, not as a substitute for verification."
            );
        };
    }

    private static List<String> todayQuestions(StateRecord state, RouteFamily family) {
        return switch (family) {
            case OVERVIEW -> stateTodayQuestions(state);
            case BUYER_SELLER -> List.of(
                    "Which specific documents can be sent today so we stop negotiating from assumption?",
                    "Are we treating this as suspected tank risk, confirmed tank, or a leak concern, and what fact supports that?",
                    "What transaction deadline gets hit first if we do nothing for the next 48 hours?"
            );
            case SWEEP_AND_LOCATE -> List.of(
                    "Who can perform the sweep or locate before the current deadline moves?",
                    "What physical clue is driving the buried-tank theory right now?",
                    "If the sweep is positive, what route do we enter next in " + state.name() + "?"
            );
            case RECORDS_AND_PROOF -> List.of(
                    "Which permit, closure, or conversion record is still missing?",
                    "What document would most reduce uncertainty today if we found it?",
                    "Do the records contain any language that moves this into leak or cleanup workflow?"
            );
            case REMOVAL_VS_ABANDONMENT -> List.of(
                    "What state rule or authority decides whether abandonment is even on the table?",
                    "What facts about the tank condition are confirmed and what is still assumption?",
                    "Is there any release evidence that makes this a cleanup question instead of a closure-choice question?"
            );
            case LEAK_AND_CLEANUP -> List.of(
                    "What is the actual release signal and who observed it?",
                    "Which state reporting or remediation step may now control the timeline?",
                    "Who needs the facts first: environmental professional, contractor, buyer, seller, or attorney?"
            );
            case COST_DIRECTION -> List.of(
                    "What exact scenario are we pricing: sweep, closure, removal, or remediation?",
                    "Which site facts could still move the range the most?",
                    "What evidence is missing that would make the budget direction more honest?"
            );
        };
    }

    private static List<PageModels.AudienceCard> guideAudienceCards(GuideRecord guide) {
        return switch (guide.primaryRouteFamily()) {
            case BUYER_SELLER -> List.of(
                    new PageModels.AudienceCard(
                            "Buyer",
                            "Buyer with a live deal",
                            "Use this guide to keep the sale tied to paperwork and proof, not panic.",
                            List.of(
                                    "Use the guide before price talk runs ahead of the facts.",
                                    "Pull the paperwork and separate suspicion from confirmation.",
                                    "Move into the state page that matches the facts."
                            )
                    ),
                    new PageModels.AudienceCard(
                            "Seller",
                            "Seller trying to de-risk the file",
                            "Use this guide to answer the buried-tank question before the buyer answers it for you.",
                            List.of(
                                    "Prepare the paperwork first.",
                                    "Use the state page to see whether records or a sweep comes next.",
                                    "Keep the issue narrow until the facts make it bigger."
                            )
                    ),
                    new PageModels.AudienceCard(
                            "Advisor",
                            "Agent or attorney carrying the next call",
                            "Use this guide to keep the deal inside proof, not theory, before the next negotiation widens the issue.",
                            List.of(
                                    "Get clear on which question belongs first.",
                                    "Send the document request before the next contract-side call.",
                                    "Carry a tighter story into buyer, seller, and attorney conversations."
                            )
                    )
            );
            case SWEEP_AND_LOCATE -> List.of(
                    new PageModels.AudienceCard(
                            "Buyer",
                            "Buyer who needs confirmation",
                            "Use this guide when the paperwork and the site clues are telling different stories.",
                            List.of(
                                    "Learn when a sweep belongs before a quote request.",
                                    "Use the state page after the locate result lands.",
                                    "Keep confirmation separate from cleanup fear."
                            )
                    ),
                    new PageModels.AudienceCard(
                            "Seller",
                            "Seller answering suspicion cleanly",
                            "A sweep guide is useful when you need to reduce argument, not enlarge it.",
                            List.of(
                                    "Use physical verification to narrow uncertainty.",
                                    "Avoid jumping straight to removal talk.",
                                    "Carry the result into the state page."
                            )
                    ),
                    new PageModels.AudienceCard(
                            "Advisor",
                            "Agent or attorney deciding whether confirmation belongs next",
                            "Use this guide when the deal is live and site clues are moving faster than the paperwork.",
                            List.of(
                                    "Decide whether a locate belongs now.",
                                    "Preserve the evidence that triggered the concern.",
                                    "Turn the result into a tighter next-step story."
                            )
                    )
            );
            default -> List.of(
                    new PageModels.AudienceCard(
                            "Buyer",
                            "Buyer or seller in a live transaction",
                            "Use this guide to narrow the question before you widen into quotes, cleanup talk, or delay.",
                            List.of(
                                    "Keep the paperwork and site facts in front of the conversation.",
                                    "Use the state page when the answer depends on local process.",
                                    "Do not let a generic article replace the property details."
                            )
                    ),
                    new PageModels.AudienceCard(
                            "Advisor",
                            "Agent or attorney carrying the file",
                            "This guide should help you move from uncertainty into the right state-specific page before delay hardens.",
                            List.of(
                                    "Clarify what is known, missing, and still only suspected.",
                                    "Collect the documents that matter before the next call.",
                                    "Switch pages once the evidence earns it."
                            )
                    )
            );
        };
    }

    private static List<String> guideTakeaways(GuideRecord guide) {
        return switch (guide.primaryRouteFamily()) {
            case BUYER_SELLER -> List.of(
                    "How to frame the issue before credits, price, or delay take over.",
                    "The first documents and proof requests to send today.",
                    "Which state page should own the next move once the facts are clearer."
            );
            case SWEEP_AND_LOCATE -> List.of(
                    "When a locate belongs before quotes or removal talk.",
                    "What physical clues matter and what they do not prove yet.",
                    "Which state page should own the case after the locate result."
            );
            case RECORDS_AND_PROOF -> List.of(
                    "The record stack to request before silence gets mistaken for proof.",
                    "How to tell missing paperwork from a genuinely resolved history.",
                    "When weak records mean you should switch into sweep or cleanup review."
            );
            default -> List.of(
                    "The next page that best matches the facts on the property.",
                    "The questions to ask before you widen the issue.",
                    "The source-backed boundaries for what this guide can and cannot tell you."
            );
        };
    }

    private static PageModels.SourceReviewModel sourceReview(
            SourceFreshnessStatus freshnessStatus,
            LocalDate verifiedOn,
            LocalDate nextReviewOn,
            String label
    ) {
        String statusLabel = freshnessStatus == SourceFreshnessStatus.STALE
                ? "Source check due"
                : "Within review window";
        String note = freshnessStatus == SourceFreshnessStatus.STALE
                ? label + " is outside the planned review window. Recheck the official links before you rely on this page."
                : label + " is inside the current review window. Use the official links when the next step depends on agency language or a closing deadline.";
        return new PageModels.SourceReviewModel(
                statusLabel,
                formatDate(verifiedOn),
                formatDate(nextReviewOn),
                note
        );
    }

    private static String formatDate(LocalDate value) {
        return value == null ? "Not listed" : value.toString();
    }

    private static List<String> startHereChecklist(StateRecord state, RouteFamily family) {
        return switch (family) {
            case OVERVIEW -> List.of(
                    firstOrFallback(state.strongestTriggerStates(), state.quickAnswer()),
                    firstOrFallback(state.firstMoves(), state.recordLookupSummary()),
                    firstOrFallback(state.documentTargets(), "Pull the record stack before widening the route.")
            );
            case BUYER_SELLER -> List.of(
                    firstOrFallback(state.firstMoves(), state.buyerSellerSummary()),
                    firstOrFallback(state.documentTargets(), "Pull seller disclosure and any closure paperwork first."),
                    "Keep suspected, confirmed, and leak-concern facts separate before negotiating from them."
            );
            case SWEEP_AND_LOCATE -> List.of(
                    firstOrFallback(state.firstMoves(), state.sweepFirstSummary()),
                    secondOrFallback(state.firstMoves(), "Use the sweep result to decide whether this is still only a transaction question."),
                    "Do not widen into removal talk until the locate result or the record stack supports it."
            );
            case RECORDS_AND_PROOF -> List.of(
                    firstOrFallback(state.documentTargets(), state.recordLookupSummary()),
                    secondOrFallback(state.documentTargets(), "Look for prior fuel-use and closure history before assuming the file is clean."),
                    "If the records point to a leak or cleanup file, switch to the narrower page that matches that evidence."
            );
            case REMOVAL_VS_ABANDONMENT -> List.of(
                    firstOrFallback(state.firstMoves(), state.removalVsAbandonmentSummary()),
                    "Confirm the tank and the state closure path before comparing disposition options.",
                    firstOrFallback(state.escalationSignals(), "Move to cleanup review if the facts point beyond routine closure.")
            );
            case LEAK_AND_CLEANUP -> List.of(
                    firstOrFallback(state.escalationSignals(), state.leakReportingSummary()),
                    "Separate suspected release facts from ordinary closure scheduling.",
                    "Use the source stack to verify whether the route now depends on reporting or cleanup review."
            );
            case COST_DIRECTION -> List.of(
                    firstOrFallback(state.firstMoves(), state.insuranceCleanupSummary()),
                    "Lock the scenario first: sweep, closure, removal, or cleanup.",
                    firstOrFallback(state.documentTargets(), "Use the record stack so the range stays tied to the actual route.")
            );
        };
    }

    private static String firstOrFallback(List<String> values, String fallback) {
        return valueAt(values, 0, fallback);
    }

    private static String secondOrFallback(List<String> values, String fallback) {
        return valueAt(values, 1, fallback);
    }

    private static String thirdOrFallback(List<String> values, String fallback) {
        return valueAt(values, 2, fallback);
    }

    private static String valueAt(List<String> values, int index, String fallback) {
        if (values == null || values.size() <= index || values.get(index) == null || values.get(index).isBlank()) {
            return fallback;
        }
        return values.get(index);
    }
}

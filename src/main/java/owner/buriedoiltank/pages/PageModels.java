package owner.buriedoiltank.pages;

import java.util.List;
import owner.buriedoiltank.data.PartnerType;
import owner.buriedoiltank.data.RouteFamily;
import owner.buriedoiltank.data.RouteInventoryEntry;
import owner.buriedoiltank.data.Scenario;
import owner.buriedoiltank.data.SourceReference;
import owner.buriedoiltank.data.StateRecord;
import owner.buriedoiltank.data.GuideRecord;

public final class PageModels {
    private PageModels() {
    }

    public record PageMeta(
            String title,
            String description,
            String canonicalUrl,
            boolean indexable,
            List<String> structuredDataJson,
            String socialImageUrl,
            String socialImageAlt
        ) {
    }

    public record Breadcrumb(
            String label,
            String path
    ) {
    }

    public record LinkCard(
            String title,
            String description,
            String href,
            String badge
    ) {
    }

    public record StateOption(
            String slug,
            String name
    ) {
    }

    public record StateCard(
            String name,
            String slug,
            String quickAnswer,
            String entryPath,
            List<String> highlights
    ) {
    }

    public record AudienceCard(
            String label,
            String title,
            String description,
            List<String> bullets
    ) {
    }

    public record SourceReviewModel(
            String freshnessStatus,
            String verifiedOn,
            String nextReviewOn,
            String note
    ) {
    }

    public record CtaModel(
            String heading,
            String buttonLabel,
            String helperText,
            String previewTitle,
            List<String> previewItems,
            String trustNote,
            String pageId,
            String pagePath,
            String routeFamily,
            Scenario defaultScenario,
            PartnerType defaultPartnerType,
            List<StateOption> stateOptions,
            List<Scenario> scenarios
    ) {
    }

    public record MetricCard(
            String label,
            String value
    ) {
    }

    public record RouteReviewRow(
            String path,
            String scopeLabel,
            String routeLabel,
            String phase,
            String indexStatus,
            String freshnessStatus,
            long ctaClicks,
            long leadOpens,
            long leadSubmissions,
            String recommendation,
            String recommendationReason
    ) {
    }

    public record PartnerMetric(
            String partnerLabel,
            long leadCount
    ) {
    }

    public record RouteFamilyMetric(
            String routeLabel,
            long ctaClicks
    ) {
    }

    public record FreshnessReviewRow(
            String scopeLabel,
            String scopeType,
            String freshnessStatus,
            String verifiedOn,
            String nextReviewOn,
            String reviewWindow,
            long affectedRoutes,
            long affectedIndexableRoutes,
            long affectedHeldRoutes,
            List<String> sourceTitles,
            List<String> affectedPaths
    ) {
    }

    public record HomePageModel(
            PageMeta meta,
            List<LinkCard> scenarioCards,
            List<String> hotStates,
            List<LinkCard> decisionPathCards,
            List<StateCard> states,
            List<LinkCard> guideCards,
            List<String> caseSeparationRules,
            List<String> guardrails,
            List<AudienceCard> audienceCards,
            List<String> takeaways,
            int trackedSourceCount
    ) {
    }

    public record HubPageModel(
            PageMeta meta,
            String activeNav,
            String eyebrow,
            String heading,
            String intro,
            String sectionEyebrow,
            String sectionHeading,
            List<LinkCard> cards,
            String supportEyebrow,
            List<String> supportBullets,
            List<Breadcrumb> breadcrumbs
    ) {
    }

    public record StaticPageModel(
            PageMeta meta,
            String heading,
            String intro,
            List<String> bullets,
            List<Breadcrumb> breadcrumbs,
            String contactEmail
    ) {
    }

    public record StatePageModel(
            PageMeta meta,
            String heading,
            StateRecord state,
            List<LinkCard> routeCards,
            CtaModel cta,
            List<Breadcrumb> breadcrumbs,
            List<AudienceCard> audienceCards,
            List<String> todayQuestions,
            SourceReviewModel sourceReview
    ) {
    }

    public record RoutePageModel(
            PageMeta meta,
            String heading,
            StateRecord state,
            RouteInventoryEntry route,
            String quickAnswer,
            List<String> startHereChecklist,
            List<String> whyItMatters,
            List<String> evidenceChecklist,
            List<String> whatNotToAssume,
            List<String> costAndTimelineNotes,
            List<LinkCard> nextLinks,
            CtaModel cta,
            List<Breadcrumb> breadcrumbs,
            List<SourceReference> sourceStack,
            List<AudienceCard> audienceCards,
            List<String> next24Hours,
            List<String> todayQuestions,
            SourceReviewModel sourceReview
    ) {
    }

    public record GuidePageModel(
            PageMeta meta,
            String heading,
            GuideRecord guide,
            List<StateCard> stateEntries,
            List<LinkCard> routeLinks,
            CtaModel cta,
            List<Breadcrumb> breadcrumbs,
            List<AudienceCard> audienceCards,
            List<String> takeaways,
            SourceReviewModel sourceReview
    ) {
    }

    public record AdminPageModel(
            PageMeta meta,
            List<MetricCard> metrics,
            List<PartnerMetric> partnerMetrics,
            List<RouteFamilyMetric> routeFamilyMetrics,
            List<RouteReviewRow> routeRows,
            List<FreshnessReviewRow> staleFreshnessRows,
            List<FreshnessReviewRow> freshnessRows,
            String freshnessSummary,
            String reviewSummary
    ) {
    }
}

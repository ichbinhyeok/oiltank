package owner.buriedoiltank.ops;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public final class OpsSnapshots {
    private OpsSnapshots() {
    }

    public record AdminMetricsSnapshot(
            LocalDate reviewDate,
            LocalDate dataWindowStart,
            LocalDate dataWindowEnd,
            long indexableRoutes,
            long heldRoutes,
            long staleScopeCount,
            long freshScopeCount,
            long staleRouteCount,
            long ctaClicks,
            long leadOpens,
            long leadSubmissions,
            Map<String, Long> leadsByPartnerType,
            Map<String, Long> ctaClicksByRouteFamily,
            List<String> staleScopes
    ) {
    }

    public record FreshnessScopeSnapshot(
            String scopeKey,
            String scopeLabel,
            String scopeType,
            String sourceFreshnessStatus,
            LocalDate verifiedOn,
            LocalDate nextReviewOn,
            long daysUntilReview,
            long affectedRoutes,
            long affectedIndexableRoutes,
            long affectedHeldRoutes,
            List<String> affectedPaths,
            List<String> sourceTitles
    ) {
    }

    public record SourceFreshnessReviewSnapshot(
            LocalDate reviewDate,
            long staleScopeCount,
            long freshScopeCount,
            long staleRouteCount,
            List<FreshnessScopeSnapshot> scopes
    ) {
    }

    public record RouteStatusSnapshot(
            String routeId,
            String routePath,
            String routeFamily,
            String scope,
            String phase,
            String indexStatus,
            String sourceFreshnessStatus,
            long last28DayImpressions,
            long last28DayClicks,
            double last28DayCtr,
            long last28DayCtaClicks,
            long last28DayLeadOpens,
            long last28DayLeadSubmissions,
            String dominantScenario,
            String promotionRecommendation,
            String recommendationReason,
            LocalDate reviewedOn,
            LocalDate nextReviewOn
    ) {
    }

    public record PromotionReviewSnapshot(
            LocalDate reviewDate,
            LocalDate dataWindowStart,
            LocalDate dataWindowEnd,
            String agentSummary,
            List<String> promotedCandidateRoutes,
            List<String> heldRoutesStillNotReady,
            List<String> routesToDemoteOrMerge,
            List<String> blockers
    ) {
    }

    public record SnapshotBundle(
            AdminMetricsSnapshot adminMetricsSnapshot,
            List<RouteStatusSnapshot> routeStatuses,
            PromotionReviewSnapshot promotionReviewSnapshot,
            SourceFreshnessReviewSnapshot sourceFreshnessReviewSnapshot
    ) {
    }
}

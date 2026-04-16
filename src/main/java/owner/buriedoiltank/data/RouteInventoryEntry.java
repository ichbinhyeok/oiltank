package owner.buriedoiltank.data;

import java.time.LocalDate;

public record RouteInventoryEntry(
        String id,
        String title,
        String path,
        PageType pageType,
        String scopeLabel,
        String stateSlug,
        RouteFamily routeFamily,
        IndexStatus indexStatus,
        RoutePhase phase,
        SourceFreshnessStatus sourceFreshnessStatus,
        Scenario dominantScenario,
        PartnerType partnerType,
        PromotionRecommendation promotionRecommendation,
        String recommendationReason,
        LocalDate verifiedOn,
        LocalDate nextReviewOn
) {
    public boolean isIndexable() {
        return indexStatus == IndexStatus.INDEX;
    }
}

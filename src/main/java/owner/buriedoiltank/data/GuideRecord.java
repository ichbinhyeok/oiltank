package owner.buriedoiltank.data;

import java.time.LocalDate;
import java.util.List;

public record GuideRecord(
        String slug,
        String title,
        String summary,
        String quickAnswer,
        String stateEntryPrompt,
        Scenario primaryScenario,
        PartnerType primaryPartnerType,
        RouteFamily primaryRouteFamily,
        boolean indexable,
        List<String> introPoints,
        List<String> whatNotToAssume,
        List<String> bestFor,
        List<String> beforeYouCall,
        List<String> documentsThatMatter,
        List<String> whenToEscalate,
        List<String> stateSlugs,
        List<SourceReference> sourceStack,
        LocalDate verifiedOn,
        LocalDate nextReviewOn
) {
    public SourceFreshnessStatus freshnessStatus(LocalDate today) {
        return nextReviewOn != null && nextReviewOn.isBefore(today)
                ? SourceFreshnessStatus.STALE
                : SourceFreshnessStatus.FRESH;
    }
}

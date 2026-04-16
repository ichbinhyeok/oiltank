package owner.buriedoiltank.data;

import java.time.LocalDate;
import java.util.List;

public record StateRecord(
        String name,
        String slug,
        String abbreviation,
        int marketPriorityScore,
        boolean launchReady,
        String regulatoryOwner,
        String delegatedLocalAuthorities,
        String buyerSellerSummary,
        String recordLookupSummary,
        String sweepFirstSummary,
        String removalVsAbandonmentSummary,
        String leakReportingSummary,
        String insuranceCleanupSummary,
        String quickAnswer,
        List<String> commonTriggers,
        List<String> cautions,
        List<String> strongestTriggerStates,
        List<String> firstMoves,
        List<String> documentTargets,
        List<String> escalationSignals,
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

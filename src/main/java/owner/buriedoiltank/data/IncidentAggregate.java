package owner.buriedoiltank.data;

import java.time.LocalDate;

public record IncidentAggregate(
        String stateSlug,
        String countySlug,
        String countyName,
        LocalDate windowStart,
        LocalDate windowEnd,
        long incidentCount,
        int stateRank,
        String sourceTitle,
        String sourceUrl,
        LocalDate updatedOn
) {
}

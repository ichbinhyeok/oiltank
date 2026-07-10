package owner.buriedoiltank.ops;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public record SearchMetricRow(
        String pagePath,
        LocalDate windowStart,
        LocalDate windowEnd,
        long clicks,
        long impressions,
        double ctr,
        double position,
        OffsetDateTime fetchedAt
) {
}

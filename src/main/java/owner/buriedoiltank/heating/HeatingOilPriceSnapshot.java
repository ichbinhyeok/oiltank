package owner.buriedoiltank.heating;

import java.time.LocalDate;
import java.util.List;

public record HeatingOilPriceSnapshot(
        LocalDate observedOn,
        LocalDate releasedOn,
        LocalDate nextScheduledRelease,
        String unit,
        String collectionStatus,
        String sourceUrl,
        List<HeatingOilPricePoint> points
) {
}

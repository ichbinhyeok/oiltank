package owner.buriedoiltank.tank;

import java.time.LocalDate;
import java.util.List;

public record TankSpec(
        String id,
        String manufacturer,
        String model,
        TankShape shape,
        TankOrientation orientation,
        double nominalCapacityGallons,
        double lengthInches,
        double widthInches,
        double heightInches,
        double usableCapacityGallons,
        List<GaugePoint> gaugeChart,
        String officialSourceTitle,
        String officialSourceUrl,
        LocalDate verifiedOn
) {
    public boolean hasGaugeChart() {
        return gaugeChart != null && gaugeChart.size() >= 2;
    }

    public String dimensionsLabel() {
        return format(lengthInches) + " x " + format(widthInches) + " x " + format(heightInches) + " in";
    }

    private static String format(double value) {
        return value == Math.rint(value) ? String.format("%.0f", value) : String.format("%.1f", value);
    }
}

package owner.buriedoiltank.tank;

import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TankCalculationService {
    private static final double CUBIC_INCHES_PER_GALLON = 231.0;

    public CapacityEstimate estimateCapacity(
            TankShape shape,
            double lengthInches,
            double widthInches,
            double heightInches,
            double measurementToleranceInches
    ) {
        validateDimensions(lengthInches, widthInches, heightInches);
        if (measurementToleranceInches < 0) {
            throw new IllegalArgumentException("Measurement tolerance cannot be negative");
        }
        double center = capacity(shape, lengthInches, widthInches, heightInches);
        double minimum = capacity(
                shape,
                Math.max(0.01, lengthInches - measurementToleranceInches),
                Math.max(0.01, widthInches - measurementToleranceInches),
                Math.max(0.01, heightInches - measurementToleranceInches)
        );
        double maximum = capacity(
                shape,
                lengthInches + measurementToleranceInches,
                widthInches + measurementToleranceInches,
                heightInches + measurementToleranceInches
        );
        return new CapacityEstimate(
                center,
                minimum,
                maximum,
                "Range reflects +/-" + measurementToleranceInches + " in measurement tolerance; fittings, wall thickness, and headspace are not included."
        );
    }

    public double interpolateGallons(TankSpec spec, double liquidHeightInches) {
        if (!spec.hasGaugeChart()) {
            throw new IllegalArgumentException("Tank spec has no verified gauge chart: " + spec.id());
        }
        List<GaugePoint> chart = spec.gaugeChart().stream()
                .sorted(Comparator.comparingDouble(GaugePoint::liquidHeightInches))
                .toList();
        GaugePoint first = chart.getFirst();
        GaugePoint last = chart.getLast();
        if (liquidHeightInches <= first.liquidHeightInches()) {
            return first.gallons();
        }
        if (liquidHeightInches >= last.liquidHeightInches()) {
            return last.gallons();
        }
        for (int index = 1; index < chart.size(); index++) {
            GaugePoint upper = chart.get(index);
            if (liquidHeightInches <= upper.liquidHeightInches()) {
                GaugePoint lower = chart.get(index - 1);
                double position = (liquidHeightInches - lower.liquidHeightInches())
                        / (upper.liquidHeightInches() - lower.liquidHeightInches());
                return lower.gallons() + position * (upper.gallons() - lower.gallons());
            }
        }
        return last.gallons();
    }

    public GaugeEstimate estimateFromFraction(TankSpec spec, double fraction) {
        if (fraction < 0 || fraction > 1) {
            throw new IllegalArgumentException("Gauge fraction must be between 0 and 1");
        }
        double chartHeight = spec.gaugeChart().getLast().liquidHeightInches();
        double gallons = interpolateGallons(spec, fraction * chartHeight);
        double planningCeiling = spec.nominalCapacityGallons() * 0.90;
        return new GaugeEstimate(gallons, Math.max(0, planningCeiling - gallons));
    }

    private static double capacity(TankShape shape, double length, double width, double height) {
        return switch (shape) {
            case CYLINDER -> Math.PI * Math.pow(Math.min(width, height) / 2.0, 2) * length / CUBIC_INCHES_PER_GALLON;
            case RECTANGULAR -> length * width * height / CUBIC_INCHES_PER_GALLON;
            case OBROUND -> {
                double shortAxis = Math.min(width, height);
                double longAxis = Math.max(width, height);
                double crossSection = shortAxis * (longAxis - shortAxis)
                        + Math.PI * Math.pow(shortAxis / 2.0, 2);
                yield crossSection * length / CUBIC_INCHES_PER_GALLON;
            }
        };
    }

    private static void validateDimensions(double length, double width, double height) {
        if (!Double.isFinite(length) || !Double.isFinite(width) || !Double.isFinite(height)
                || length <= 0 || width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Tank dimensions must be positive finite numbers");
        }
    }

    public record CapacityEstimate(double estimatedGallons, double minimumGallons, double maximumGallons, String explanation) {
    }

    public record GaugeEstimate(double gallonsRemaining, double planningFillGallons) {
    }
}

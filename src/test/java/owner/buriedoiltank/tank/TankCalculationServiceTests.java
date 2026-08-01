package owner.buriedoiltank.tank;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TankCalculationServiceTests {
    private final TankCalculationService service = new TankCalculationService();
    private final TankCatalog catalog = new TankCatalog();

    @Test
    void calculatesEachSupportedShapeWithItsOwnFormula() {
        var rectangular = service.estimateCapacity(TankShape.RECTANGULAR, 60, 30, 48, 0);
        var cylinder = service.estimateCapacity(TankShape.CYLINDER, 60, 38, 38, 0);
        var obround = service.estimateCapacity(TankShape.OBROUND, 60, 27, 44, 0);

        assertThat(rectangular.estimatedGallons()).isCloseTo(374.03, within(0.02));
        assertThat(cylinder.estimatedGallons()).isCloseTo(294.58, within(0.02));
        assertThat(obround.estimatedGallons()).isCloseTo(267.94, within(0.02));
    }

    @Test
    void capacityRangeExpandsAroundTheCenterEstimate() {
        var estimate = service.estimateCapacity(TankShape.OBROUND, 60, 27, 44, 0.5);

        assertThat(estimate.minimumGallons()).isLessThan(estimate.estimatedGallons());
        assertThat(estimate.maximumGallons()).isGreaterThan(estimate.estimatedGallons());
        assertThat(estimate.explanation()).contains("+/-0.5 in");
    }

    @Test
    void chartInterpolationHandlesBoundariesAndIntervals() {
        TankSpec spec = catalog.require("granby-204201p-275-vertical");

        assertThat(service.interpolateGallons(spec, 0)).isZero();
        assertThat(service.interpolateGallons(spec, 44)).isEqualTo(273.8);
        assertThat(service.interpolateGallons(spec, 22.5)).isEqualTo(140.5);
        assertThat(service.estimateFromFraction(spec, 0.5).gallonsRemaining()).isEqualTo(136.9);
    }

    @Test
    void gallonsAreMonotonicAsGaugeHeightRises() {
        TankSpec spec = catalog.require("granby-204201p-275-vertical");
        double previous = -1;
        for (double height = 0; height <= 44; height += 0.125) {
            double current = service.interpolateGallons(spec, height);
            assertThat(current).isGreaterThanOrEqualTo(previous);
            previous = current;
        }
    }

    @Test
    void everyPublishedGaugeChartIsMonotonicAndUsesItsOfficialBoundary() {
        assertThat(catalog.withGaugeCharts()).extracting(TankSpec::id)
                .containsExactlyInAnyOrder(
                        "granby-208101-138-vertical",
                        "granby-204201p-275-vertical",
                        "granby-205201p-330-vertical"
                );
        for (TankSpec spec : catalog.withGaugeCharts()) {
            double previous = -1;
            for (GaugePoint point : spec.gaugeChart()) {
                assertThat(point.gallons()).isGreaterThanOrEqualTo(previous);
                previous = point.gallons();
            }
            assertThat(service.interpolateGallons(spec, spec.gaugeChart().getLast().liquidHeightInches()))
                    .isEqualTo(spec.usableCapacityGallons());
        }
    }

    private static org.assertj.core.data.Offset<Double> within(double value) {
        return org.assertj.core.data.Offset.offset(value);
    }
}

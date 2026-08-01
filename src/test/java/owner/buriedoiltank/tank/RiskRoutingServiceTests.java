package owner.buriedoiltank.tank;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RiskRoutingServiceTests {
    private final RiskRoutingService service = new RiskRoutingService();

    @Test
    void normalConditionStaysInformationalWithoutCommercialCta() {
        var outcome = service.route(input(10, false, false, false, false, false, false, false, false));
        assertThat(outcome.riskBand()).isEqualTo(RiskRoutingService.RiskBand.INFORMATION);
        assertThat(outcome.commercial()).isFalse();
    }

    @Test
    void ageRustOrSeepageRoutesToInspectionAndReplacement() {
        var age = service.route(input(25, false, false, false, false, false, false, false, false));
        var rust = service.route(input(5, true, false, false, false, false, false, false, false));
        var seepage = service.route(input(5, false, true, false, false, false, false, false, false));

        assertThat(age.riskBand()).isEqualTo(RiskRoutingService.RiskBand.INSPECTION);
        assertThat(rust.partnerType()).isEqualTo("closure_or_removal");
        assertThat(seepage.ctaLabel()).contains("inspection");
    }

    @Test
    void odorWetSoilOrLeakOverridesAllOtherRoutes() {
        for (var input : new RiskRoutingService.Input[]{
                input(5, false, false, true, false, false, false, false, false),
                input(5, false, false, false, true, false, true, true, false),
                input(5, false, false, false, false, true, false, false, false)
        }) {
            var outcome = service.route(input);
            assertThat(outcome.riskBand()).isEqualTo(RiskRoutingService.RiskBand.URGENT);
            assertThat(outcome.partnerType()).isEqualTo("environmental_cleanup");
        }
    }

    @Test
    void saleWithUndergroundOrUnknownTankRoutesToSweep() {
        var underground = service.route(input(5, false, false, false, false, false, true, true, false));
        var unknown = service.route(input(5, false, false, false, false, false, true, false, true));

        assertThat(underground.riskBand()).isEqualTo(RiskRoutingService.RiskBand.TRANSACTION);
        assertThat(unknown.partnerType()).isEqualTo("sweep_or_locate");
    }

    private static RiskRoutingService.Input input(
            int age, boolean rust, boolean seepage, boolean odor, boolean wetSoil,
            boolean leak, boolean sale, boolean underground, boolean unknown
    ) {
        return new RiskRoutingService.Input(age, rust, seepage, odor, wetSoil, leak, sale, underground, unknown);
    }
}

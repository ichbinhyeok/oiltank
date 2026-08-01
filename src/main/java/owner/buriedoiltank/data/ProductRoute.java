package owner.buriedoiltank.data;

import java.util.List;

public record ProductRoute(
        String id,
        String path,
        String title,
        Scenario scenario,
        PartnerType partnerType
) {
    public static final List<ProductRoute> CORE = List.of(
            new ProductRoute("heating-oil-tank", "/heating-oil-tank/", "Heating-oil tank identifier", Scenario.RECORDS_FIRST, PartnerType.SWEEP_LOCATE),
            new ProductRoute("heating-oil-tank-sizes-dimensions", "/heating-oil-tank-sizes-dimensions/", "Heating-oil tank sizes and dimensions", Scenario.RECORDS_FIRST, PartnerType.SWEEP_LOCATE),
            new ProductRoute("275-gallon-oil-tank", "/275-gallon-oil-tank/", "275-gallon oil tank field guide", Scenario.RECORDS_FIRST, PartnerType.SWEEP_LOCATE),
            new ProductRoute("heating-oil-tank-charts", "/heating-oil-tank-charts/", "Official heating-oil tank chart library", Scenario.RECORDS_FIRST, PartnerType.SWEEP_LOCATE),
            new ProductRoute("oil-tank-gauge-calculator", "/oil-tank-gauge-calculator/", "Oil tank gauge calculator", Scenario.RECORDS_FIRST, PartnerType.SWEEP_LOCATE),
            new ProductRoute("heating-oil-delivery-check", "/heating-oil-delivery-check/", "Heating-oil delivery check", Scenario.RECORDS_FIRST, PartnerType.SWEEP_LOCATE),
            new ProductRoute("heating-oil-usage-calculator", "/heating-oil-usage-calculator/", "Heating-oil usage calculator", Scenario.RECORDS_FIRST, PartnerType.SWEEP_LOCATE),
            new ProductRoute("oil-tank-capacity-calculator", "/oil-tank-capacity-calculator/", "Oil tank capacity calculator", Scenario.RECORDS_FIRST, PartnerType.SWEEP_LOCATE),
            new ProductRoute("heating-oil-tank-sludge-cleaning", "/heating-oil-tank-sludge-cleaning/", "Heating-oil tank sludge and cleaning router", Scenario.REMOVAL_DECISION, PartnerType.CLOSURE_REMOVAL),
            new ProductRoute("oil-tank-replacement-planner", "/oil-tank-replacement-planner/", "Oil tank replacement planner", Scenario.REMOVAL_DECISION, PartnerType.CLOSURE_REMOVAL),
            new ProductRoute("oil-tank-replacement-cost", "/oil-tank-replacement-cost/", "Oil tank replacement cost scope builder", Scenario.REMOVAL_DECISION, PartnerType.CLOSURE_REMOVAL),
            new ProductRoute("heating-oil-tank-installation-cost", "/heating-oil-tank-installation-cost/", "Heating-oil tank installation cost scope builder", Scenario.REMOVAL_DECISION, PartnerType.CLOSURE_REMOVAL),
            new ProductRoute("basement-oil-tank-removal", "/basement-oil-tank-removal/", "Basement oil tank removal planner", Scenario.REMOVAL_DECISION, PartnerType.CLOSURE_REMOVAL)
    );
}

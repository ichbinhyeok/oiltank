package owner.buriedoiltank.heating;
import java.util.List;
public record PriceArea(String id, String name, String sourceName, String sourceNote) {
    public static final List<PriceArea> STATES = List.of(
        area("connecticut", "Connecticut", "Compare quotes for your actual town and order size."),
        area("maine", "Maine", "Keep No. 2 heating oil separate from kerosene or blended-fuel quotes."),
        area("massachusetts", "Massachusetts", "Ask whether the quote is for a one-off delivery or includes a service contract."),
        area("new-hampshire", "New Hampshire", "Compare delivery coverage and minimum quantities before choosing a per-gallon quote."),
        area("rhode-island", "Rhode Island", "Compare the complete delivered total and cash versus card terms."),
        area("vermont", "Vermont", "Confirm the fuel grade and delivery conditions with your supplier."),
        area("delaware", "Delaware", "Check that delivery fees and fuel quantities use the same basis."),
        area("maryland", "Maryland", "Use your actual service area and delivery quantity when requesting a quote."),
        area("new-jersey", "New Jersey", "Keep fuel-delivery quotes separate from tank removal or property-record work."),
        area("new-york", "New York", "NYSERDA also publishes regional credit-price survey data, a different series from this EIA benchmark."),
        area("pennsylvania", "Pennsylvania", "Ask whether taxes, payment fees and minimum-order charges are included."),
        area("north-carolina", "North Carolina", "No. 2 heating oil and K-1 kerosene have different prices; compare the fuel your system requires."),
        area("virginia", "Virginia", "Match the supplier's fuel, delivery area and payment terms before comparing quotes."),
        area("indiana", "Indiana", "The state observation is a survey benchmark, not a quote from a specific dealer."),
        area("iowa", "Iowa", "Heating oil and propane contain different energy per gallon; compare useful heat as well as fuel prices."),
        area("kentucky", "Kentucky", "Confirm whether a quoted price includes delivery and any small-order fee."),
        area("michigan", "Michigan", "Compare the correct heating fuel grade, not road-diesel prices."),
        area("minnesota", "Minnesota", "Check the fuel specification and delivery requirements with your supplier."),
        area("nebraska", "Nebraska", "Use your local delivered quote for budgeting; this average supplies context."),
        area("ohio", "Ohio", "Compare quotes at the same gallon quantity and payment method."),
        area("wisconsin", "Wisconsin", "Keep propane and heating oil separate unless you account for energy content and equipment efficiency.")
    );
    public static final List<PriceArea> ALL = java.util.stream.Stream.concat(java.util.stream.Stream.of(
        new PriceArea("us", "United States", "U.S.", "National residential benchmark."),
        new PriceArea("east-coast", "East Coast", "East Coast (PADD 1)", "Regional residential benchmark."),
        new PriceArea("new-england", "New England", "New England (PADD 1A)", "Regional residential benchmark.")), STATES.stream()).toList();
    public static final List<String> PAGE_STATES = STATES.stream().map(PriceArea::id).toList();
    private static PriceArea area(String id, String name, String note) { return new PriceArea(id, name, name, note); }
    public String path() { return "/heating-oil-prices/" + id + "/"; }
    public static PriceArea require(String id) { return ALL.stream().filter(a -> a.id.equals(id)).findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown price area")); }
}

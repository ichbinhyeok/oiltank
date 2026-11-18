package owner.buriedoiltank.heating;
import java.util.List;
/** Public supplier statements reviewed November 18, 2026; not a partner network. */
public final class HeatingOilSuppliers {
    private HeatingOilSuppliers() {}
    public record Supplier(String name, List<String> states, String area, String terms, String url) {}
    public static final List<Supplier> ALL = List.of(
        new Supplier("My Easy Oil", List.of("maine", "massachusetts", "new-hampshire"), "Selected service areas in Maine, Massachusetts and New Hampshire",
            "Website publishes prices for 150+ gallon orders. New customers and customers without a delivery in 12+ months require a tank inspection. Check the supplier's town coverage.", "https://www.myeasyoil.com/"),
        new Supplier("PayLessForOil.com", List.of("massachusetts", "connecticut"), "Western Massachusetts and North Central Connecticut",
            "Town/ZIP quote tool and online ordering. New customers register and complete a tank inspection. Request the actual current price and delivery date.", "https://paylessforoil.com/"),
        new Supplier("Reis Fuel", List.of("new-jersey"), "Northern New Jersey",
            "Supplier advertises on-demand heating-oil ordering with cash or credit payment. Check the delivery-area page and confirm any automatic-delivery discount terms.", "https://reisfuel.com/"),
        new Supplier("The Heating Oil Lady", List.of("rhode-island", "massachusetts", "connecticut"), "Listed towns in Rhode Island, southeastern Massachusetts and northeastern Connecticut",
            "Site publishes zone prices and town-specific minimums, ranging from 100 to 150 gallons. Confirm your zone, payment terms and minimum directly; page sections can differ.", "https://theoilladyhho.com/"),
        new Supplier("OK Petroleum", List.of("new-york"), "Nassau and Suffolk counties, Long Island",
            "Address/ZIP coverage checker and quantity-based online ordering. Confirm the current delivered total and applicable taxes on the supplier's site.", "https://okpetroleum.com/areas-served/"),
        new Supplier("Central Bucks Oil", List.of("pennsylvania"), "Selected towns in Bucks, Montgomery and Lehigh counties",
            "Will-call online ordering. The Doylestown page directs orders below 150 gallons to call and notes additional fees; same-day service requires a call. Confirm terms for your town.", "https://www.centralbucksoil.com/locations/doylestown-pa/")
    );
}

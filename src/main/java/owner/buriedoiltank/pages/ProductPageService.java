package owner.buriedoiltank.pages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.pages.PageModels.Breadcrumb;
import owner.buriedoiltank.pages.PageModels.LinkCard;
import owner.buriedoiltank.pages.ProductPageModels.FaqItem;
import owner.buriedoiltank.pages.ProductPageModels.ProductPageModel;
import owner.buriedoiltank.pages.ProductPageModels.SourceLink;
import owner.buriedoiltank.data.ProductRoute;
import owner.buriedoiltank.tank.TankCatalog;
import owner.buriedoiltank.tank.TankSpec;
import org.springframework.stereotype.Service;

@Service
public class ProductPageService {
    public static final List<String> CORE_PATHS = ProductRoute.CORE.stream().map(ProductRoute::path).toList();

    private final TankCatalog tankCatalog;
    private final URI baseUrl;
    private final String analyticsMeasurementId;
    private final ObjectMapper objectMapper;

    public ProductPageService(TankCatalog tankCatalog, SiteProperties siteProperties, ObjectMapper objectMapper) {
        this.tankCatalog = tankCatalog;
        this.baseUrl = siteProperties.getBaseUrl();
        this.analyticsMeasurementId = siteProperties.getAnalyticsMeasurementId();
        this.objectMapper = objectMapper;
    }

    public ProductPageModel homePage() {
        return page(
                "home",
                "/",
                "home",
                "Residential heating-oil tank tools",
                "Know the tank. Plan the next move.",
                "Identify a heating-oil tank, check a delivery, estimate fuel remaining, and route real risk to the right professional - without an email gate.",
                "hub",
                "One route from measurement to action",
                List.of(
                        "Start with the label and dimensions before assuming a tank size.",
                        "Use a verified manufacturer chart when turning a stick or gauge reading into gallons.",
                        "Cross-check a delivery ticket with before-and-after readings and keep the result in a private Tank Passport.",
                        "Treat odor, wet soil, or visible oil as a safety route - not a calculator result."
                ),
                tankCatalog.all(),
                List.of(),
                commonSources(),
                coreLinks(),
                List.of()
        );
    }

    public ProductPageModel corePage(String slug) {
        return switch (slug) {
            case "heating-oil-tank" -> page(
                    slug, "/heating-oil-tank/", "guides", "Tank identifier",
                    "Identify your residential heating-oil tank",
                    "Use the label, orientation, shape, and measured dimensions to narrow the tank before calculating fuel or planning replacement.",
                    "identifier", "What to record at the tank",
                    List.of(
                            "Photograph the manufacturer label without moving piping or fittings.",
                            "Measure overall length, width, and tank-body height - not leg or fitting height.",
                            "Note whether the obround steel tank is standing on its end or lying on its side."
                    ),
                    tankCatalog.all(),
                    List.of(new FaqItem(
                            "Can dimensions alone prove the exact tank model?",
                            "No. Shared dimensions are common. Use the manufacturer label and model number as the deciding evidence."
                    )),
                    commonSources(),
                    linksExcept(slug),
                    crumbs("Heating oil tank")
            );
            case "heating-oil-tank-sizes-dimensions" -> page(
                    slug, "/heating-oil-tank-sizes-dimensions/", "guides", "Verified size comparison",
                    "Heating-oil tank sizes and dimensions",
                    "Compare a small, verified set of residential models. Dimensions are manufacturer-published and are not a substitute for the tank label.",
                    "sizes", "How to compare the table",
                    List.of(
                            "Orientation changes which side you see as height, even when the steel shell is the same size.",
                            "Nominal gallons describe the model class; practical fill planning leaves headspace.",
                            "Clearance, base, vent, and local code requirements are separate from tank-body dimensions."
                    ),
                    tankCatalog.all(),
                    List.of(new FaqItem(
                            "Why can two 275-gallon tanks have different dimensions?",
                            "Steel obround, low-height double-wall, and tall double-wall models use different construction and footprints while sharing the same nominal capacity."
                    )),
                    commonSources(), linksExcept(slug), crumbs("Tank sizes & dimensions")
            );
            case "275-gallon-oil-tank" -> page(
                    slug, "/275-gallon-oil-tank/", "guides", "275-gallon field guide",
                    "275-gallon oil tank dimensions, gauge chart, and fill planning",
                    "The common 275-gallon label covers multiple tank bodies. Compare verified models first, then use the chart that matches the tank and orientation.",
                    "275", "Do not treat 275 as one shape",
                    List.of(
                            "A Granby vertical 275-gallon obround model is published at 60 x 27 x 44 inches.",
                            "Roth publishes both a tall 275-gallon 1000L model and a low-height 1000LH model.",
                            "The verified Granby vertical capacity chart is accurate to about +/-2% and is not for buying or selling fuel."
                    ),
                    tankCatalog.all().stream().filter(spec -> spec.nominalCapacityGallons() == 275).toList(),
                    List.of(new FaqItem(
                            "Does a quarter-tank gauge reading mean exactly 68.75 gallons?",
                            "No. An obround tank is not rectangular, so gallons do not rise in a straight line with liquid height. Use the matching capacity chart."
                    ), new FaqItem(
                            "How many gallons are at 1/4, 1/2, and 3/4 on a 275-gallon vertical tank?",
                            "For the verified Granby 204201P vertical model, 11 inches (one-quarter of the 44-inch chart height) is 58.2 gallons, 22 inches is 136.9 gallons, and 33 inches is 215.6 gallons. These values do not apply to a horizontal tank or a different model."
                    ), new FaqItem(
                            "Which 275-gallon oil tank chart is published on this page?",
                            "The inches-to-gallons table is the manufacturer-published Granby 204201P Standard 20Plus vertical chart, verified on August 1, 2026. Match the label and vertical orientation before using it."
                    )),
                    commonSources(), linksExcept(slug), crumbs("275 gallon oil tank")
            );
            case "heating-oil-tank-charts" -> page(
                    slug, "/heating-oil-tank-charts/", "guides", "Official chart library",
                    "Heating-oil tank charts: verified inches-to-gallons tables",
                    "Use manufacturer-published oil tank charts for a confirmed model and orientation. This library separates verified tables from calculated generic geometry and sends 275-gallon intent to its single canonical chart.",
                    "charts", "Choose the chart by label, not capacity alone",
                    List.of(
                            "Match the manufacturer, model, nominal capacity, dimensions, and orientation before reading any gallons column.",
                            "The 138- and 330-gallon vertical tables below reproduce reviewed manufacturer chart points; they are not linear capacity estimates.",
                            "The verified 275-gallon vertical table lives only on the 275-gallon field guide so the same chart is not duplicated across URLs.",
                            "A horizontal tank, double-wall tank, or unidentified tank needs its own supported chart. Do not rotate or substitute a vertical table."
                    ),
                    tankCatalog.withGaugeCharts().stream()
                            .filter(spec -> spec.nominalCapacityGallons() != 275)
                            .toList(),
                    List.of(new FaqItem(
                            "Which heating-oil tank charts are included here?",
                            "This page publishes the verified Granby 138- and 330-gallon vertical tables. The verified Granby 275-gallon vertical table remains on the 275-gallon field guide and is linked from this library."
                    ), new FaqItem(
                            "Can I use a chart when I only know the tank capacity?",
                            "No. Tanks with the same nominal capacity can have different bodies and orientations. Confirm the manufacturer label, model, dimensions, and orientation before choosing a chart."
                    ), new FaqItem(
                            "Are these gallons calculated from generic tank geometry?",
                            "No. The published rows come from the cited manufacturer capacity chart. The separate capacity calculator is clearly labeled as a geometry-based estimate for unidentified tanks."
                    )),
                    commonSources(), linksExcept(slug), crumbs("Heating oil tank charts")
            );
            case "oil-tank-gauge-calculator" -> page(
                    slug, "/oil-tank-gauge-calculator/", "tools", "Verified chart calculator",
                    "Oil tank gauge and stick-reading calculator",
                    "Convert a gauge fraction or measured liquid depth into remaining gallons and a conservative fill amount using a verified manufacturer chart.",
                    "gauge", "Before entering a reading",
                    List.of(
                            "Confirm the model and vertical orientation; a chart for another tank can be materially wrong.",
                            "A float gauge is approximate. A clean stick reading can help confirm it, but never open or probe a tank when a leak or unsafe condition is suspected.",
                            "The result is planning information, not a delivery authorization or fuel-sale measurement."
                    ),
                    tankCatalog.withGaugeCharts(),
                    List.of(new FaqItem(
                            "How does the calculator avoid a linear estimate?",
                            "It finds the two verified chart rows around the reading and interpolates only within that chart interval."
                    ), new FaqItem(
                            "What if the gauge is stuck or does not match a stick reading?",
                            "Do not force the float or open the tank to troubleshoot it. Recheck the model and orientation, compare only with a safe reading method, and ask a heating-oil service professional to inspect the gauge, vent, and tank when readings stay inconsistent."
                    ), new FaqItem(
                            "Does a zero reading prove the tank is empty?",
                            "No. A failed float, wrong chart, tank tilt, or reading error can produce a false low result. Treat the gauge as approximate and verify before authorizing a delivery."
                    )),
                    commonSources(), linksExcept(slug), crumbs("Gauge calculator")
            );
            case "heating-oil-delivery-check" -> page(
                    slug, "/heating-oil-delivery-check/", "tools", "Delivery ticket cross-check",
                    "Heating-oil delivery check and tank passport",
                    "Compare a delivery ticket with before-and-after tank readings using a reviewed manufacturer chart. Save a private, printable tank history in this browser - no account or address required.",
                    "delivery", "What this check can and cannot establish",
                    List.of(
                            "A printed truck-meter ticket is the transaction record. A tank reading is an independent planning check, not a replacement for a certified delivery meter.",
                            "Stick readings create a narrower estimate than coarse float-gauge fractions. Tank tilt, reading technique, chart precision, and fuel movement widen the result.",
                            "The result identifies consistency or a reason to recheck the model, orientation, readings, and ticket. It does not accuse a supplier or prove a measurement violation.",
                            "Tank Passport entries remain in localStorage on this device and never include an address or detailed values in analytics."
                    ),
                    tankCatalog.withGaugeCharts(),
                    List.of(new FaqItem(
                            "Does a difference mean the delivery ticket is wrong?",
                            "No. The tank estimate can differ because of a coarse gauge, an incorrect tank match, tank tilt, reading timing, or measurement technique. Recheck those facts and use the printed delivery ticket when contacting the supplier."
                    ), new FaqItem(
                            "Why is the expected amount a range?",
                            "The range combines reading resolution with the manufacturer chart's stated precision. It is intentionally wider for fraction-gauge readings than for careful stick depths."
                    ), new FaqItem(
                            "Where is my Tank Passport stored?",
                            "Only in this browser's localStorage. It has no account, address, server sync, or public URL. Clearing browser storage or using the clear-history control removes it."
                    )),
                    deliverySources(), linksExcept(slug), crumbs("Delivery check")
            );
            case "heating-oil-usage-calculator" -> page(
                    slug, "/heating-oil-usage-calculator/", "tools", "Fuel runway calculator",
                    "Heating-oil usage and days-remaining calculator",
                    "Turn remaining gallons and your own recent consumption rate into a planning range for days to reserve and days to empty. No weather guess and no email gate.",
                    "usage", "Use a household rate you can defend",
                    List.of(
                            "Start with chart-based remaining gallons when the tank model is known; the last gauge result can transfer in this browser session.",
                            "Use gallons per day from delivery history or divide gallons used by elapsed days. Do not substitute a national household average.",
                            "Weather, thermostat settings, insulation, burner efficiency, and hot-water use can change the actual rate, so plan a reserve instead of using the empty date as a delivery date."
                    ),
                    tankCatalog.withGaugeCharts(),
                    List.of(new FaqItem(
                            "Why does this calculator ask for my consumption rate?",
                            "A house-size or temperature shortcut can look precise while missing insulation, equipment, thermostat, and hot-water differences. Your recent delivery history is the more honest planning input."
                    ), new FaqItem(
                            "Can I use the result to order an exact delivery amount?",
                            "No. It is a planning estimate. Confirm the tank, current level, safe fill space, and delivery requirements with your supplier."
                    )),
                    usageSources(), linksExcept(slug), crumbs("Heating oil usage calculator")
            );
            case "oil-tank-capacity-calculator" -> page(
                    slug, "/oil-tank-capacity-calculator/", "tools", "Shape-based estimator",
                    "Oil tank capacity calculator",
                    "Estimate a capacity range from tank shape and measured dimensions. The result shows measurement uncertainty and never claims a model match without a label.",
                    "capacity", "Measure the tank body",
                    List.of(
                            "For an obround tank, measure the straight body length plus the full cross-section width and height.",
                            "For a cylinder, enter body length and diameter; the calculator uses the smaller width/height entry as diameter.",
                            "The displayed range assumes +/-0.5 inch measurement uncertainty and excludes wall thickness, fittings, and required headspace."
                    ),
                    tankCatalog.all(),
                    List.of(new FaqItem(
                            "Why is the result a range instead of one exact gallon number?",
                            "Small dimension errors compound across a three-dimensional shape, and external measurements include material that is not liquid volume."
                    )),
                    commonSources(), linksExcept(slug), crumbs("Capacity calculator")
            );
            case "heating-oil-tank-sludge-cleaning" -> page(
                    slug, "/heating-oil-tank-sludge-cleaning/", "risk", "Sludge and service routing",
                    "Heating-oil tank sludge and cleaning: choose the safe next step",
                    "Separate a filter or burner service problem from tank corrosion and from a possible oil release. This router does not provide DIY tank-opening, pumping, or cleaning instructions.",
                    "sludge", "Sludge is a symptom category, not one repair",
                    List.of(
                            "Repeated filter plugging, burner shutdowns, or confirmed water or debris call for a heating-oil service diagnosis before anyone promises that tank cleaning alone will solve the problem.",
                            "Water and sludge can contribute to internal corrosion. External rust, pitting, seepage, or unstable supports change the route to a tank-condition inspection or replacement review.",
                            "Strong oil odor, fresh wetness, visible oil, or oily soil is a possible release. Do not open, drain, move, or test the tank yourself.",
                            "Ask a service provider to identify the source, inspect the tank and connected components, explain fuel and waste handling, and document what was found."
                    ),
                    List.of(),
                    List.of(new FaqItem(
                            "Can I remove oil-tank sludge myself?",
                            "Do not open, drain, pump, or enter a residential heating-oil tank yourself. Fuel, vapors, waste handling, tank condition, and spill risk require qualified service and the applicable local rules."
                    ), new FaqItem(
                            "Does a clogged filter prove the tank needs replacement?",
                            "No. A qualified service technician should diagnose the filter, line, burner, water, and tank condition. Corrosion, seepage, unstable supports, odor, or visible oil changes the route."
                    ), new FaqItem(
                            "Will tank cleaning repair corrosion?",
                            "No. Removing water or sediment does not restore steel already lost to corrosion. Tank condition needs a separate inspection and replacement decision."
                    )),
                    sludgeSources(), linksExcept(slug), crumbs("Tank sludge and cleaning")
            );
            case "oil-tank-replacement-planner" -> page(
                    slug, "/oil-tank-replacement-planner/", "risk", "Condition and transaction routing",
                    "Oil tank replacement planner",
                    "Check age, corrosion, odor, seepage, leak signs, and a home-sale trigger. The result separates routine information from inspection, spill response, and sweep/removal routes.",
                    "planner", "Use observations, not guesses",
                    List.of(
                            "Odor, wet soil, or visible oil overrides ordinary age planning and opens the leak-response route.",
                            "Age alone does not prove failure, but age combined with rust, unstable supports, seepage, or unexplained fuel loss opens an inspection or replacement route.",
                            "A gauge, whistle, or line problem without tank-body distress belongs in qualified service first, not an automatic removal quote.",
                            "An underground or unknown tank during a sale opens a records and sweep/removal evaluation route.",
                            "Repair can be the right first route for an isolated service component; tank-body corrosion, seepage, unstable supports, or recurring unexplained loss requires a broader condition review."
                    ),
                    List.of(),
                    List.of(new FaqItem(
                            "Will a normal result show a removal quote form?",
                            "No. Routine monitoring and low-fuel results stay informational. Commercial routing appears only for a supported risk or transaction trigger."
                    ), new FaqItem(
                            "Does a tank have one fixed replacement age?",
                            "No. Material, installation, water, corrosion, piping, supports, and inspection history all matter. Age is a review trigger, not proof that a tank is safe or failed."
                    ), new FaqItem(
                            "Can a bad gauge mean the whole tank needs replacement?",
                            "Not by itself. A gauge or vent issue can require service while the tank body needs a separate condition assessment. Visible oil, odor, seepage, or structural corrosion changes that route."
                    ), new FaqItem(
                            "Should I repair or replace an oil tank?",
                            "Start with what failed. A qualified technician may repair an isolated gauge, filter, vent, valve, or line issue. Tank-body corrosion, pitting, seepage, unstable supports, or repeated condition problems require an inspection and replacement review rather than a component-only assumption."
                    )),
                    riskSources(), linksExcept(slug), crumbs("Replacement planner")
            );
            case "oil-tank-replacement-cost" -> page(
                    slug, "/oil-tank-replacement-cost/", "risk", "Replacement quote scope",
                    "Oil tank replacement cost: compare the complete quote scope",
                    "A national flat price hides the work that changes a residential heating-oil tank replacement. Build a site-specific quote checklist without publishing an unsupported average.",
                    "replacement-cost", "Price the project, not just the tank",
                    List.of(
                            "Separate the new tank and installation from pump-out, old-tank handling, piping, permits, access work, and any foundation repair.",
                            "Keep suspected contamination outside the routine replacement total. Odor, visible oil, seepage, or stained soil opens the leak-response route first.",
                            "A component problem such as a gauge, filter, line, or vent whistle does not automatically prove the whole tank needs replacement.",
                            "Permit, inspection, contractor-license, and documentation requirements vary by state and local authority; confirm them before comparing bids."
                    ),
                    List.of(),
                    List.of(new FaqItem(
                            "Why does this page not publish one national replacement price?",
                            "The official sources identify different tanks, site conditions, permits, piping, removal, and contamination paths. They do not support one current national installed-price range that applies to every home."
                    ), new FaqItem(
                            "Is a new tank price the same as replacement cost?",
                            "No. Equipment is one line item. A complete quote can also include pump-out, sludge handling, old-tank disposition, access protection, base work, fill and vent piping, fuel line work, permits, inspection, and commissioning."
                    ), new FaqItem(
                            "Should cleanup be included in a routine replacement quote?",
                            "A quote should explain what happens if staining or a release is discovered, but confirmed investigation and remediation should remain a separate scope instead of being hidden inside an ordinary replacement total."
                    )),
                    replacementCostSources(), linksExcept(slug), crumbs("Oil tank replacement cost")
            );
            case "heating-oil-tank-installation-cost" -> page(
                    slug, "/heating-oil-tank-installation-cost/", "risk", "Installation quote scope",
                    "Heating-oil tank installation cost: build a complete quote scope",
                    "Compare equipment and installation on the same scope: tank design, location, base, access, fill and vent, supply piping, permits, old-tank work, and commissioning.",
                    "installation-cost", "A tank price is not an installed project price",
                    List.of(
                            "Match every installation requirement to the selected manufacturer model; do not apply one tank manual to another product.",
                            "A level supporting base, inspection access, fill and vent piping, supply piping, and overfill protection are separate parts of the installed scope.",
                            "Outdoor work may add drainage, weather protection, anchoring, cover, and site-preparation requirements for the chosen system.",
                            "Ask the local code official or fire authority about permits and inspection. One state's form is evidence of local variation, not a nationwide rule."
                    ),
                    List.of(),
                    List.of(new FaqItem(
                            "Does a 275-gallon oil tank price include installation?",
                            "Not necessarily. Product-only prices can exclude delivery, base preparation, labor, piping, accessories, permits, inspection, old-tank work, and commissioning. Compare written scopes rather than headline equipment prices."
                    ), new FaqItem(
                            "Can a used heating-oil tank be installed to save money?",
                            "Do not assume so. The cited Granby installation guidance says not to install a used tank. Follow the instructions and warranty conditions for the exact manufacturer and model selected."
                    ), new FaqItem(
                            "Are permits required everywhere?",
                            "Requirements are local. Manufacturer guidance tells owners and installers to confirm applicable codes and authority requirements; Massachusetts provides one documented permit example, not a national rule."
                    )),
                    installationCostSources(), linksExcept(slug), crumbs("Heating oil tank installation cost")
            );
            case "basement-oil-tank-removal" -> page(
                    slug, "/basement-oil-tank-removal/", "risk", "Indoor removal scope planner",
                    "Basement oil tank removal: scope the job before a quote",
                    "Separate a routine aboveground basement removal from a leak response, then record the access, remaining fuel, piping, and project trigger a qualified contractor needs.",
                    "removal", "What changes a basement removal scope",
                    List.of(
                            "A routine job still includes fuel removal, sludge handling, tank cleaning, safe removal or cutting, fill and vent work, disposal, and a final check for spilled oil.",
                            "Stairs, narrow doors, finished rooms, no walkout access, remaining fuel, and connected piping change labor and containment needs; they do not create a trustworthy national flat price.",
                            "Visible oil, strong odor, indoor seepage, or staining changes the job from ordinary removal to spill response and environmental evaluation."
                    ),
                    List.of(),
                    List.of(new FaqItem(
                            "Can a basement tank be cut apart for removal?",
                            "Official removal procedures include cleaning and sludge removal before a tank is cut or taken from the building. This is contractor work; do not cut, drain, or open a heating-oil tank yourself."
                    ), new FaqItem(
                            "What should a written removal scope identify?",
                            "Ask who handles remaining oil, sludge, tank cleaning, cutting or whole-tank removal, fill and vent lines, disposal documentation, permits, protection of the route through the home, and any spill discovered after removal."
                    ), new FaqItem(
                            "Does a strong oil smell belong in a normal removal quote?",
                            "No. Odor, visible oil, seepage, or stained material can indicate a release. Use the leak-response route and the reporting guidance for your state."
                    )),
                    removalSources(), linksExcept(slug), crumbs("Basement oil tank removal")
            );
            default -> throw new IllegalArgumentException("Unknown product page: " + slug);
        };
    }

    private ProductPageModel page(
            String id,
            String path,
            String activeNav,
            String eyebrow,
            String heading,
            String intro,
            String toolKind,
            String factsHeading,
            List<String> facts,
            List<TankSpec> specs,
            List<FaqItem> faqs,
            List<SourceLink> sources,
            List<LinkCard> nextLinks,
            List<Breadcrumb> breadcrumbs
    ) {
        String title = heading + " | Oil Tank Route";
        List<String> schemas = new ArrayList<>();
        schemas.add(json(schema("charts".equals(toolKind) ? "CollectionPage" : "WebPage", heading, path, intro)));
        if (List.of("gauge", "delivery", "usage", "capacity", "sludge", "planner", "removal", "identifier").contains(toolKind)) {
            Map<String, Object> app = schema("WebApplication", heading, path, intro);
            app.put("applicationCategory", "UtilitiesApplication");
            app.put("operatingSystem", "Any");
            app.put("offers", Map.of("@type", "Offer", "price", "0", "priceCurrency", "USD"));
            schemas.add(json(app));
        }
        if (!faqs.isEmpty()) {
            Map<String, Object> faq = new LinkedHashMap<>();
            faq.put("@context", "https://schema.org");
            faq.put("@type", "FAQPage");
            faq.put("mainEntity", faqs.stream().map(item -> Map.of(
                    "@type", "Question",
                    "name", item.question(),
                    "acceptedAnswer", Map.of("@type", "Answer", "text", item.answer())
            )).toList());
            schemas.add(json(faq));
        }
        return new ProductPageModel(
                new PageModels.PageMeta(
                        title,
                        intro,
                        baseUrl.resolve(path).toString(),
                        true,
                        schemas,
                        baseUrl.resolve("/og-default.png").toString(),
                        "Oil Tank Route field-instrument diagram",
                        analyticsMeasurementId
                ),
                id, activeNav, eyebrow, heading, intro, toolKind, factsHeading, facts, specs, tankDataJson(specs), faqs, sources, nextLinks, breadcrumbs
        );
    }

    private Map<String, Object> schema(String type, String heading, String path, String description) {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("@context", "https://schema.org");
        schema.put("@type", type);
        schema.put("name", heading);
        schema.put("description", description);
        schema.put("url", baseUrl.resolve(path).toString());
        schema.put("dateModified", LocalDate.of(2026, 8, 2).toString());
        schema.put("isPartOf", Map.of("@type", "WebSite", "name", "Oil Tank Route", "url", baseUrl.resolve("/").toString()));
        return schema;
    }

    private String json(Map<String, Object> value) {
        try {
            return escapeEmbeddedJson(objectMapper.writeValueAsString(value));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to build product structured data", exception);
        }
    }

    private String tankDataJson(List<TankSpec> specs) {
        try {
            return escapeEmbeddedJson(objectMapper.writeValueAsString(specs));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to serialize verified tank data", exception);
        }
    }

    private static String escapeEmbeddedJson(String json) {
        return json.replace("&", "\\u0026")
                .replace("<", "\\u003c")
                .replace(">", "\\u003e")
                .replace("\u2028", "\\u2028")
                .replace("\u2029", "\\u2029");
    }

    private List<Breadcrumb> crumbs(String current) {
        return List.of(new Breadcrumb("Home", "/"), new Breadcrumb(current, null));
    }

    private List<SourceLink> commonSources() {
        return List.of(
                new SourceLink(
                        "Granby Standard 20Plus residential oil tanks",
                        "https://www.granbyindustries.com/en-us/petroleum-tanks/products/standard-20-plus/",
                        "Manufacturer model dimensions and orientation; verified 2026-08-01."
                ),
                new SourceLink(
                        "Granby U.S. vertical tank capacity chart",
                        "https://www.granbyindustries.com/app/uploads/2026/04/GranbyInd_Capacity-Chart_VerticalTanks_USA_v1.pdf",
                        "Manufacturer gallons-by-inch table, stated precision +/-2%; verified 2026-08-01."
                ),
                new SourceLink(
                        "Roth double-wall heating-oil tanks",
                        "https://www.roth-america.com/product/oil-storage-tanks/double-wall-heating-oil-tank/",
                        "Manufacturer nominal capacities and body dimensions; verified 2026-08-01."
                )
        );
    }

    private List<SourceLink> usageSources() {
        return List.of(
                new SourceLink(
                        "Granby U.S. vertical tank capacity chart",
                        "https://www.granbyindustries.com/app/uploads/2026/04/GranbyInd_Capacity-Chart_VerticalTanks_USA_v1.pdf",
                        "Manufacturer gallons-by-inch data used by the upstream gauge calculator; verified 2026-08-01."
                ),
                new SourceLink(
                        "U.S. Energy Information Administration: Heating oil explained",
                        "https://www.eia.gov/energyexplained/heating-oil/",
                        "Federal background on residential heating oil and household use; reviewed 2026-08-02. The calculator does not publish a national daily-use assumption."
                )
        );
    }

    private List<SourceLink> deliverySources() {
        return List.of(
                new SourceLink(
                        "Granby U.S. vertical tank capacity chart",
                        "https://www.granbyindustries.com/app/uploads/2026/04/GranbyInd_Capacity-Chart_VerticalTanks_USA_v1.pdf",
                        "Manufacturer gallons-by-inch table with stated precision of about +/-2%; verified 2026-08-01."
                ),
                new SourceLink(
                        "Delaware Department of Agriculture: Consumers cautioned on heating-oil purchases",
                        "https://news.delaware.gov/2012/10/02/consumers-cautioned-on-heating-oil-purchases/",
                        "Official consumer procedure: identify tank size, measure before and after delivery, convert with a chart, and compare with the sales ticket; reviewed 2026-08-02."
                ),
                new SourceLink(
                        "Connecticut General Statutes, Chapter 250",
                        "https://www.cga.ct.gov/2022/sup/chap_250.htm",
                        "Official requirements for metered residential fuel-oil delivery, delivery tickets, and written cost disclosure; reviewed 2026-08-02."
                )
        );
    }

    private List<SourceLink> riskSources() {
        return List.of(
                new SourceLink(
                        "Pennsylvania DEP: Tips for Residential Heating Oil Tank Owners",
                        "https://www.pa.gov/agencies/dep/residents/my-water/private-wells/tips-for-residential-heating-oil-tank-owners",
                        "Official inspection signals, corrosion checks, unusual fuel use, and leak-response guidance; reviewed 2026-08-02."
                ),
                new SourceLink(
                        "New York DEC: Underground Heating Oil Tanks - A Homeowner's Guide",
                        "https://dec.ny.gov/environmental-protection/hazardous-substance-bulk-storage/underground-heating-oil-tanks-homeowner-guide",
                        "Official age, corrosion, replacement, leak, and contractor-selection context; reviewed 2026-08-02."
                ),
                new SourceLink(
                        "MassDEP: Site Cleanup for Homeowners",
                        "https://www.mass.gov/guides/site-cleanup-for-homeowners",
                        "Official homeowner guidance for heating-oil leaks and cleanup; reviewed 2026-08-02."
                )
        );
    }

    private List<SourceLink> removalSources() {
        return List.of(
                new SourceLink(
                        "New Jersey DCA Bulletin 95-1B",
                        "https://www.nj.gov/dca/codes/publications/pdf_bulletins/b_95_1B.pdf",
                        "Official residential heating-oil tank removal procedures, including fuel, sludge, piping, disposal, and spill checks; reviewed 2026-08-02."
                ),
                new SourceLink(
                        "Pennsylvania DEP: Residential Home Heating Oil",
                        "https://www.pa.gov/agencies/dep/programs-and-services/land/site-remediation/storage-tank-cleanup-program/residential-home-heating-oil",
                        "Official homeowner removal, contractor quote, leak, and cleanup guidance; reviewed 2026-08-02."
                ),
                new SourceLink(
                        "New York DEC: Underground Heating Oil Tanks - A Homeowner's Guide",
                        "https://dec.ny.gov/environmental-protection/hazardous-substance-bulk-storage/underground-heating-oil-tanks-homeowner-guide",
                        "Official access, condition, contractor estimate, and contamination guidance; reviewed 2026-08-02."
                )
        );
    }

    private List<SourceLink> sludgeSources() {
        return List.of(
                new SourceLink(
                        "Maine DEP: Check Your Tank, Prevent a Leak",
                        "https://www.maine.gov/dep/waste/publications/check-your-tank.html",
                        "Official homeowner guidance connecting water and sludge buildup with tank corrosion and directing owners to a licensed oil-heat technician; reviewed 2026-08-02."
                ),
                new SourceLink(
                        "New York State Department of Health: Maintaining Your Home Heating Oil Tank",
                        "https://www.health.ny.gov/environmental/oil_spills/docs/oil_tank_maintenance.pdf",
                        "Official annual inspection, service-plan, gauge, valve, filter, fuel-line, support, and spill-response checklist; reviewed 2026-08-02."
                ),
                new SourceLink(
                        "Pennsylvania DEP: Residential Home Heating Oil",
                        "https://www.pa.gov/agencies/dep/programs-and-services/land/site-remediation/storage-tank-cleanup-program/residential-home-heating-oil",
                        "Official homeowner maintenance, contractor, release-response, and cleanup guidance; reviewed 2026-08-02."
                ),
                new SourceLink(
                        "MassDEP: Homeowner Oil Spill Cleanup Guide",
                        "https://www.mass.gov/doc/homeowner-oil-spill-cleanup-guide-1/download",
                        "Official prevention context for water and sludge, corrosion, professional maintenance, and spill response; reviewed 2026-08-02."
                )
        );
    }

    private List<SourceLink> replacementCostSources() {
        return List.of(
                new SourceLink(
                        "Maine DEP: Preventing heating-oil spills",
                        "https://www.maine.gov/dep/waste/abovegroundtanks/spill-prevention.html",
                        "Official corrosion, component-failure, inspection, and replacement triggers; reviewed 2026-08-02. Maine guidance is not presented as a national tank-life rule."
                ),
                new SourceLink(
                        "New York DEC: Underground Heating Oil Tanks - A Homeowner's Guide",
                        "https://dec.ny.gov/environmental-protection/hazardous-substance-bulk-storage/underground-heating-oil-tanks-homeowner-guide",
                        "Official replacement, piping, overfill, vent, contractor-estimate, and leak context; reviewed 2026-08-02."
                ),
                new SourceLink(
                        "Connecticut DEEP: Residential heating-oil tank FAQs",
                        "https://portal.ct.gov/deep/emergency-response-and-spill-prevention/residential-tanks/residential-home-heating-oil-tanks---faqs",
                        "Official contractor, piping, soil-sampling, photo, analysis, and property-record guidance; reviewed 2026-08-02."
                ),
                new SourceLink(
                        "U.S. EPA: Frequent questions about underground storage tanks",
                        "https://www.epa.gov/ust/frequent-questions-about-underground-storage-tanks",
                        "Federal UST scope and exclusions; reviewed 2026-08-02. State and local requirements can still apply."
                )
        );
    }

    private List<SourceLink> installationCostSources() {
        return List.of(
                new SourceLink(
                        "Granby UL-80 installation and maintenance guidelines",
                        "https://www.granbyindustries.com/wp-content/uploads/2017/11/SI0015_Ea-UL-80-INSTALLATION-AND-MAINTENANCE-GUIDELINES.pdf",
                        "Manufacturer scope for Granby 120-330 gallon domestic aboveground UL-80 obround tanks, including base, piping, overfill protection, inspection access, and installer guidance; reviewed 2026-08-02."
                ),
                new SourceLink(
                        "Roth EcoDWT Plus 3 installation manual",
                        "https://www.roth-america.com/app/uploads/2021/02/DWT_Installation_Manual_2023.pdf",
                        "Manufacturer permit, trained-installer, indoor/outdoor site preparation, clearance, cover, anchoring, and accessory guidance; reviewed 2026-08-02."
                ),
                new SourceLink(
                        "Massachusetts FP-056 fuel-oil permit form",
                        "https://www.mass.gov/doc/fp-056-form-1-fuel-oil-burning/download",
                        "Official example of state and fire-department permit and completion certification; reviewed 2026-08-02. It is not presented as a nationwide requirement."
                )
        );
    }

    private List<LinkCard> coreLinks() {
        return List.of(
                new LinkCard("Identify a tank", "Start with shape, orientation, label, and dimensions.", "/heating-oil-tank/", "01 / Identify"),
                new LinkCard("Compare sizes", "Use one verified model table shared by every tool.", "/heating-oil-tank-sizes-dimensions/", "02 / Compare"),
                new LinkCard("Check a 275-gallon tank", "Resolve the most common nominal size without assuming one body shape.", "/275-gallon-oil-tank/", "03 / Verify"),
                new LinkCard("Open official tank charts", "Use manufacturer inches-to-gallons rows only after the model and orientation match.", "/heating-oil-tank-charts/", "04 / Charts"),
                new LinkCard("Calculate a gauge reading", "Turn a fraction or stick depth into chart-based gallons.", "/oil-tank-gauge-calculator/", "05 / Gauge"),
                new LinkCard("Check a delivery", "Compare the truck ticket with before-and-after chart readings and keep a private tank passport.", "/heating-oil-delivery-check/", "06 / Delivery"),
                new LinkCard("Estimate days remaining", "Use your own consumption rate to turn gallons into a fuel runway.", "/heating-oil-usage-calculator/", "07 / Usage"),
                new LinkCard("Estimate capacity", "Use shape-specific geometry and a measurement range.", "/oil-tank-capacity-calculator/", "08 / Capacity"),
                new LinkCard("Route sludge symptoms", "Separate service and cleaning from corrosion review and possible release response.", "/heating-oil-tank-sludge-cleaning/", "09 / Sludge"),
                new LinkCard("Plan replacement", "Separate routine monitoring from inspection, leak, and transaction routes.", "/oil-tank-replacement-planner/", "10 / Risk"),
                new LinkCard("Compare replacement scope", "Build a complete quote checklist without a false national average.", "/oil-tank-replacement-cost/", "11 / Replace"),
                new LinkCard("Scope installation cost", "Separate the tank price from site, piping, permit, and commissioning work.", "/heating-oil-tank-installation-cost/", "12 / Install"),
                new LinkCard("Scope basement removal", "Separate routine indoor removal from leak response before requesting a quote.", "/basement-oil-tank-removal/", "13 / Remove")
        );
    }

    private List<LinkCard> linksExcept(String slug) {
        return switch (slug) {
            case "oil-tank-gauge-calculator" -> linksFor(
                    "/heating-oil-tank-charts/", "/heating-oil-delivery-check/", "/heating-oil-usage-calculator/"
            );
            case "heating-oil-tank-charts" -> linksFor(
                    "/275-gallon-oil-tank/", "/oil-tank-gauge-calculator/", "/heating-oil-tank-sizes-dimensions/"
            );
            case "275-gallon-oil-tank" -> linksFor(
                    "/heating-oil-tank-charts/", "/oil-tank-gauge-calculator/", "/heating-oil-tank-sizes-dimensions/"
            );
            case "heating-oil-delivery-check" -> linksFor(
                    "/oil-tank-gauge-calculator/", "/heating-oil-usage-calculator/", "/275-gallon-oil-tank/"
            );
            case "heating-oil-usage-calculator" -> linksFor(
                    "/oil-tank-gauge-calculator/", "/275-gallon-oil-tank/", "/oil-tank-replacement-planner/"
            );
            case "oil-tank-replacement-planner" -> List.of(
                    coreLink("/oil-tank-replacement-cost/"),
                    new LinkCard("Leak response guide", "Use odor, visible oil, seepage, or stained material to enter the safety route.", "/guides/leaking-heating-oil-tank-what-to-do/", "Safety"),
                    coreLink("/heating-oil-tank-sizes-dimensions/")
            );
            case "heating-oil-tank-sludge-cleaning" -> List.of(
                    coreLink("/oil-tank-replacement-planner/"),
                    coreLink("/oil-tank-gauge-calculator/"),
                    new LinkCard("Possible heating-oil leak", "Use the safety guide for odor, visible oil, seepage, or oily soil.", "/guides/leaking-heating-oil-tank-what-to-do/", "Safety")
            );
            case "oil-tank-replacement-cost" -> List.of(
                    coreLink("/oil-tank-replacement-planner/"),
                    coreLink("/heating-oil-tank-installation-cost/"),
                    new LinkCard("Old-tank removal cost", "Keep pump-out, cleaning, removal, and disposal separate from the new installation.", "/guides/oil-tank-removal-cost/", "Removal")
            );
            case "heating-oil-tank-installation-cost" -> List.of(
                    coreLink("/heating-oil-tank-sizes-dimensions/"),
                    coreLink("/oil-tank-replacement-cost/"),
                    new LinkCard("Old-tank removal cost", "Compare the old-tank disposition as its own written scope.", "/guides/oil-tank-removal-cost/", "Removal")
            );
            case "basement-oil-tank-removal" -> List.of(
                    coreLink("/oil-tank-replacement-cost/"),
                    new LinkCard("Removal cost scope", "Compare the work included before comparing contractor prices.", "/guides/oil-tank-removal-cost/", "Cost"),
                    coreLink("/oil-tank-replacement-planner/")
            );
            default -> coreLinks().stream()
                    .filter(link -> !link.href().equals("/" + slug + "/"))
                    .limit(3)
                    .toList();
        };
    }

    private List<LinkCard> linksFor(String... paths) {
        return java.util.Arrays.stream(paths).map(this::coreLink).toList();
    }

    private LinkCard coreLink(String path) {
        return coreLinks().stream()
                .filter(link -> path.equals(link.href()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown core link: " + path));
    }
}

package owner.buriedoiltank.pages;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import org.springframework.stereotype.Service;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.heating.*;

@Service
public class FuelPageService {
    private final HeatingOilPriceCatalog prices;
    private final SiteProperties site;
    private final ObjectMapper mapper;
    public FuelPageService(HeatingOilPriceCatalog prices, SiteProperties site, ObjectMapper mapper) {
        this.prices = prices; this.site = site; this.mapper = mapper;
    }
    public record PriceRow(PriceArea area, BigDecimal price, BigDecimal change, String changeLabel, String path) {}
    public record HistoryRow(LocalDate date, BigDecimal price, String cost150) {}
    public record BudgetRow(int gallons, String cost) {}
    public record DeliveryPage(PageModels.PageMeta meta, List<HeatingOilSuppliers.Supplier> suppliers) {}
    public record GuidePage(PageModels.PageMeta meta, FuelGuideCatalog.Guide guide, HeatingOilPriceSnapshot snapshot,
        BigDecimal price, String cost, boolean overdue, List<PriceRow> states) {}
    public record GuideHub(PageModels.PageMeta meta, List<FuelGuideCatalog.Guide> guides) {}
    public GuideHub guides() {
        return new GuideHub(editorialMeta("Heating Oil Guides: Buying, Delivery, Costs & Fuel Use", "Practical heating-oil guides with dated cost examples, order-size comparisons, contract questions and calculators for days left and useful heat.", "/heating-oil-guides/", "CollectionPage"), FuelGuideCatalog.ALL);
    }
    public GuidePage guide(String slug) {
        var guide = FuelGuideCatalog.require(slug);
        var data = prices.data();
        var price = data.latest().prices().get("us");
        var previous = data.history().get(data.history().size() - 2);
        var states = PriceArea.STATES.stream().map(a -> {
            var current = data.latest().prices().get(a.id());
            var delta = current.subtract(previous.prices().get(a.id()));
            return new PriceRow(a, current, delta, signed(delta), a.path());
        }).toList();
        return new GuidePage(editorialMeta(guide.title(), guide.description(), guide.path(), "Article"), guide,
            prices.snapshot(data), price, money(price.multiply(BigDecimal.valueOf(guide.gallons()))), prices.overdue(data), states);
    }
    private PageModels.PageMeta editorialMeta(String title, String description, String path, String type) {
        try {
            var schema = new java.util.LinkedHashMap<String,Object>();
            schema.put("@context", "https://schema.org"); schema.put("@type", type);
            schema.put("headline", title); schema.put("description", description);
            schema.put("url", site.getBaseUrl().resolve(path).toString());
            schema.put("datePublished", "2026-11-18"); schema.put("dateModified", "2026-11-18");
            schema.put("publisher", Map.of("@type", "Organization", "name", "Oil Tank Route", "url", site.getBaseUrl().toString()));
            String json = mapper.writeValueAsString(schema).replace("<", "\\u003c").replace(">", "\\u003e").replace("&", "\\u0026");
            return new PageModels.PageMeta(title + " | Oil Tank Route", description, site.getBaseUrl().resolve(path).toString(), true,
                List.of(json), site.getBaseUrl().resolve("/og-default.png").toString(), title, site.getAnalyticsMeasurementId());
        } catch (Exception error) { throw new IllegalStateException("Editorial metadata unavailable", error); }
    }
    public DeliveryPage delivery() {
        String schema;
        try {
            schema = mapper.writeValueAsString(Map.of("@context", "https://schema.org", "@type", "CollectionPage", "name", "Heating oil delivery supplier routes",
                "url", site.getBaseUrl().resolve("/heating-oil-delivery/").toString(), "dateModified", "2026-11-18",
                "mainEntity", Map.of("@type", "ItemList", "itemListElement", HeatingOilSuppliers.ALL.stream().map(s -> Map.of("@type", "WebPage", "name", s.name(), "url", s.url())).toList())))
                .replace("<", "\\u003c").replace(">", "\\u003e").replace("&", "\\u0026");
        } catch (Exception error) { throw new IllegalStateException("Supplier metadata unavailable", error); }
        return new DeliveryPage(new PageModels.PageMeta("Heating Oil Delivery: Find Suppliers & Compare Quote Terms | Oil Tank Route",
            "Find selected heating-oil suppliers in eight Northeast states. Check actual service areas, minimum orders, payment terms and current quotes on supplier sites.",
            site.getBaseUrl().resolve("/heating-oil-delivery/").toString(), true, List.of(schema), site.getBaseUrl().resolve("/og-default.png").toString(),
            "Oil Tank Route heating-oil delivery directory", site.getAnalyticsMeasurementId()), HeatingOilSuppliers.ALL);
    }
    public record PricePage(PageModels.PageMeta meta, PriceArea area, String path, String heading, String intro,
        HeatingOilPriceSnapshot snapshot, BigDecimal price, String changeLabel, String weekPercent,
        List<PriceRow> states, List<HistoryRow> history, List<BudgetRow> budgets, String chartPoints, boolean overdue, boolean hasSupplier) {}

    public PricePage page(String id) {
        if (!id.equals("us") && !PriceArea.PAGE_STATES.contains(id)) throw new IllegalArgumentException("Unpublished price area");
        var area = PriceArea.require(id);
        var data = prices.data();
        var latest = data.latest();
        var previous = data.history().get(data.history().size() - 2);
        var price = latest.prices().get(id);
        var change = price.subtract(previous.prices().get(id));
        var percent = change.multiply(BigDecimal.valueOf(100)).divide(previous.prices().get(id), 1, RoundingMode.HALF_UP);
        String path = id.equals("us") ? "/heating-oil-prices/" : area.path();
        String heading = id.equals("us") ? "Heating oil prices by state" : area.name() + " heating oil prices";
        String intro = id.equals("us") ? "EIA residential prices observed " + latest.date() + ", excluding taxes. Compare the recent trend and budget a delivery before checking a local quote."
            : area.name() + " averaged $" + price + " per gallon in EIA's " + latest.date() + " residential observation, "
                + signed(price.subtract(latest.prices().get("us"))) + " per gallon versus the U.S. benchmark. Figures exclude taxes; local dealer terms determine your delivered total.";
        String title = heading + ": $" + price + "/gal (" + latest.date() + ") | Oil Tank Route";
        var states = PriceArea.STATES.stream().map(a -> {
            var current = latest.prices().get(a.id());
            var delta = current.subtract(previous.prices().get(a.id()));
            return new PriceRow(a, current, delta, signed(delta), PriceArea.PAGE_STATES.contains(a.id()) ? a.path() : "");
        }).toList();
        var history = data.history().stream().map(row -> new HistoryRow(row.date(), row.prices().get(id), money(row.prices().get(id).multiply(BigDecimal.valueOf(150))))).toList();
        var budgets = List.of(100, 150, 200, 250).stream().map(q -> new BudgetRow(q, money(price.multiply(BigDecimal.valueOf(q))))).toList();
        var min = history.stream().map(HistoryRow::price).min(BigDecimal::compareTo).orElseThrow().doubleValue();
        var max = history.stream().map(HistoryRow::price).max(BigDecimal::compareTo).orElseThrow().doubleValue();
        var points = new java.util.ArrayList<String>();
        for (int i = 0; i < history.size(); i++) points.add(String.format(Locale.US, "%.1f,%.1f", 15 + 670.0 * i / (history.size() - 1), 155 - (history.get(i).price().doubleValue() - min) / Math.max(0.05, max - min) * 125));
        try {
            String json = mapper.writeValueAsString(Map.of("@context", "https://schema.org", "@type", "WebPage", "name", heading,
                "description", intro, "url", site.getBaseUrl().resolve(path).toString(), "dateModified", data.releasedOn().toString()))
                .replace("<", "\\u003c").replace(">", "\\u003e").replace("&", "\\u0026");
            var meta = new PageModels.PageMeta(title, intro, site.getBaseUrl().resolve(path).toString(), true, List.of(json),
                site.getBaseUrl().resolve("/og-default.png").toString(), "Oil Tank Route heating-oil price comparison", site.getAnalyticsMeasurementId());
            boolean hasSupplier = HeatingOilSuppliers.ALL.stream().anyMatch(supplier -> supplier.states().contains(id));
            return new PricePage(meta, area, path, heading, intro, prices.snapshot(data), price, signed(change), percent.toPlainString(), states, history, budgets, String.join(" ", points), prices.overdue(data), hasSupplier);
        } catch (Exception error) { throw new IllegalStateException("Price page metadata unavailable", error); }
    }
    public static String signed(BigDecimal value) { return (value.signum() >= 0 ? "+" : "-") + "$" + value.abs().setScale(3, RoundingMode.HALF_UP); }
    public static String money(BigDecimal value) { return "$" + value.setScale(2, RoundingMode.HALF_UP); }
    public String csv() {
        var out = new StringBuilder("observation_date,area,usd_per_gallon_excluding_taxes,source\n");
        for (var row : prices.data().history()) for (var area : PriceArea.ALL)
            out.append(row.date()).append(',').append(area.name()).append(',').append(row.prices().get(area.id())).append(',').append(HeatingOilPriceCatalog.SOURCE).append('\n');
        return out.toString();
    }
}

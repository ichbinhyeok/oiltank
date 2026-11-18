package owner.buriedoiltank.heating;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;
/** Reads the EIA residential table only; incomplete or changed layouts are rejected. */
public final class EiaPriceParser {
    private EiaPriceParser() {}
    public static FuelPriceData parse(String html, LocalDate checkedOn) {
        var dates = Pattern.compile("<th[^>]*class=\"Series5\"[^>]*>\\s*(\\d{2}/\\d{2}/\\d{2})\\s*</th>", Pattern.CASE_INSENSITIVE).matcher(html);
        List<LocalDate> observed = new ArrayList<>();
        while (dates.find()) observed.add(LocalDate.parse(dates.group(1), DateTimeFormatter.ofPattern("MM/dd/uu")));
        if (observed.size() < 2 || observed.size() > 12) throw new IllegalArgumentException("Unexpected EIA date columns");
        List<java.util.Map<String, BigDecimal>> values = new ArrayList<>();
        observed.forEach(d -> values.add(new LinkedHashMap<>()));
        for (String row : html.split("(?i)<tr[^>]*class=\"DataRow\"[^>]*>")) {
            var label = Pattern.compile("<td[^>]*class=\"DataStub[12]\"[^>]*>(.*?)</td>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(row);
            if (!label.find()) continue;
            String name = label.group(1).replaceAll("<[^>]+>", "").replace("&nbsp;", " ").trim();
            var area = PriceArea.ALL.stream().filter(a -> a.sourceName().equals(name)).findFirst();
            if (area.isEmpty()) continue;
            var cells = Pattern.compile("<td[^>]*class=\"(?:DataB|Current2)\"[^>]*>\\s*([0-9.]+)\\s*</td>", Pattern.CASE_INSENSITIVE).matcher(row);
            List<BigDecimal> prices = new ArrayList<>();
            while (cells.find()) prices.add(new BigDecimal(cells.group(1)));
            if (prices.size() != observed.size()) throw new IllegalArgumentException("Incomplete EIA area");
            for (int i = 0; i < prices.size(); i++) values.get(i).put(area.get().id(), prices.get(i));
        }
        List<FuelPriceData.Observation> history = new ArrayList<>();
        for (int i = 0; i < observed.size(); i++) history.add(new FuelPriceData.Observation(observed.get(i), values.get(i)));
        return new FuelPriceData(dateField(html, "(?<!Next )Release Date:"), dateField(html, "Next Release Date:"), checkedOn, List.copyOf(history));
    }
    private static LocalDate dateField(String html, String label) {
        var match = Pattern.compile(label + "\\s*(\\d{1,2}/\\d{1,2}/\\d{4})", Pattern.CASE_INSENSITIVE).matcher(html);
        if (!match.find()) throw new IllegalArgumentException("Missing EIA publication date");
        return LocalDate.parse(match.group(1), DateTimeFormatter.ofPattern("M/d/uuuu"));
    }
}

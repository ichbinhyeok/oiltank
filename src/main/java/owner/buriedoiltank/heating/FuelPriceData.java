package owner.buriedoiltank.heating;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
public record FuelPriceData(LocalDate releasedOn, LocalDate nextRelease, LocalDate checkedOn, List<Observation> history) {
    public FuelPriceData { history = List.copyOf(history); }
    public record Observation(LocalDate date, Map<String, BigDecimal> prices) {
        public Observation { prices = Map.copyOf(prices); }
    }
    public Observation latest() { return history.get(history.size() - 1); }
}

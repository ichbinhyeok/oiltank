package owner.buriedoiltank.heating;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import owner.buriedoiltank.config.SiteProperties;

@Service
public class HeatingOilPriceCatalog {
    public static final String SOURCE = "https://www.eia.gov/dnav/pet/PET_PRI_WFR_A_EPD2F_PRS_DPGAL_W.htm";
    private final ObjectMapper mapper;
    private final Path override;
    private final Clock clock;
    private volatile FuelPriceData data;
    @Value("${buried-oil-tank.prices.refresh-enabled:false}") private boolean refreshEnabled;

    @Autowired
    public HeatingOilPriceCatalog(SiteProperties properties, ObjectMapper mapper, Clock clock) {
        this.mapper = mapper;
        this.clock = clock;
        this.override = properties.getStorageRoot().resolve("market/heating-oil-prices.json");
        try (var input = getClass().getResourceAsStream("/data/normalized/heating-oil-prices.json")) {
            data = mapper.readValue(input, FuelPriceData.class);
            validate(data, LocalDate.now(clock));
        } catch (Exception error) { throw new IllegalStateException("Bundled EIA snapshot is invalid", error); }
        if (Files.isRegularFile(override)) {
            try {
                var candidate = mapper.readValue(override.toFile(), FuelPriceData.class);
                validate(candidate, LocalDate.now(clock));
                if (!candidate.latest().date().isBefore(data.latest().date())) data = candidate;
            } catch (Exception ignored) { /* Keep the reviewed baseline on invalid overrides. */ }
        }
    }

    public FuelPriceData data() { return data; }
    public boolean overdue() { return overdue(data); }
    public boolean overdue(FuelPriceData current) { return LocalDate.now(clock).isAfter(current.nextRelease().plusDays(2)); }
    public HeatingOilPriceSnapshot latest() {
        return snapshot(data);
    }
    public HeatingOilPriceSnapshot snapshot(FuelPriceData current) {
        return new HeatingOilPriceSnapshot(current.latest().date(), current.releasedOn(), current.nextRelease(),
            "U.S. dollars per gallon, excluding taxes", "EIA publishes weekly during October-March. Read the observation date; this survey is not a live dealer quote.", SOURCE,
            PriceArea.ALL.stream().map(area -> new HeatingOilPricePoint(area.id(), area.name(), current.latest().prices().get(area.id()))).toList());
    }

    @Scheduled(initialDelayString = "${buried-oil-tank.prices.initial-delay-ms:60000}", fixedDelayString = "${buried-oil-tank.prices.refresh-delay-ms:21600000}")
    public void refresh() {
        if (!refreshEnabled) return;
        try {
            var client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(15)).build();
            var response = client.send(HttpRequest.newBuilder(URI.create(SOURCE)).timeout(Duration.ofSeconds(30))
                .header("User-Agent", "OilTankRoute price refresh; https://oiltankroute.com/contact/").GET().build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200 || response.body().length() > 2_000_000) throw new IllegalStateException("EIA response unavailable");
            accept(EiaPriceParser.parse(response.body(), LocalDate.now(clock)));
        } catch (Exception error) {
            if (error instanceof InterruptedException) Thread.currentThread().interrupt();
            org.slf4j.LoggerFactory.getLogger(getClass()).warn("EIA refresh unavailable; retaining last verified snapshot ({})", error.getClass().getSimpleName());
        }
    }

    public synchronized void accept(FuelPriceData candidate) throws java.io.IOException {
        validate(candidate, LocalDate.now(clock));
        if (candidate.latest().date().isBefore(data.latest().date())) throw new IllegalArgumentException("Older EIA snapshot");
        var history = new TreeMap<LocalDate, FuelPriceData.Observation>();
        data.history().forEach(row -> history.put(row.date(), row));
        candidate.history().forEach(row -> history.put(row.date(), row));
        while (history.size() > 110) history.pollFirstEntry();
        var merged = new FuelPriceData(candidate.releasedOn(), candidate.nextRelease(), candidate.checkedOn(), java.util.List.copyOf(history.values()));
        Files.createDirectories(override.getParent());
        Path temporary = Files.createTempFile(override.getParent(), "eia-", ".json.tmp");
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(temporary.toFile(), merged);
            Files.move(temporary, override, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            data = merged;
        } finally { Files.deleteIfExists(temporary); }
    }

    public static void validate(FuelPriceData candidate, LocalDate today) {
        if (candidate == null || candidate.history() == null || candidate.history().size() < 2 || candidate.history().size() > 110
            || candidate.releasedOn() == null || candidate.nextRelease() == null || candidate.checkedOn() == null
            || candidate.checkedOn().isAfter(today) || candidate.checkedOn().isBefore(candidate.releasedOn()) || candidate.releasedOn().isAfter(today)
            || candidate.releasedOn().isBefore(candidate.latest().date()) || !candidate.nextRelease().isAfter(candidate.releasedOn()))
            throw new IllegalArgumentException("Invalid EIA snapshot dates");
        LocalDate prior = LocalDate.MIN;
        for (var row : candidate.history()) {
            if (row == null || row.date() == null || !row.date().isAfter(prior) || row.date().isAfter(today) || row.prices() == null
                || row.prices().size() != PriceArea.ALL.size()) throw new IllegalArgumentException("Invalid EIA observations");
            for (var area : PriceArea.ALL) {
                var price = row.prices().get(area.id());
                if (price == null || price.compareTo(BigDecimal.ZERO) <= 0 || price.compareTo(new BigDecimal("30")) >= 0)
                    throw new IllegalArgumentException("Invalid EIA price");
            }
            prior = row.date();
        }
    }
}

package owner.buriedoiltank.heating;

import static org.assertj.core.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import owner.buriedoiltank.config.SiteProperties;

class HeatingOilPriceCatalogTests {
    @TempDir Path storage;
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    private final Clock clock = Clock.fixed(Instant.parse("2026-11-18T12:00:00Z"), ZoneOffset.UTC);
    private HeatingOilPriceCatalog catalog() {
        var properties = new SiteProperties(); properties.setStorageRoot(storage);
        return new HeatingOilPriceCatalog(properties, mapper, clock);
    }
    private String fixture() throws Exception {
        try (var stream = getClass().getResourceAsStream("/eia-residential-2026-11-12.html")) {
            return new String(stream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
    @Test void parsesTheActualResidentialTableWithPublicationDates() throws Exception {
        var data = EiaPriceParser.parse(fixture(), LocalDate.of(2026, 11, 18));
        HeatingOilPriceCatalog.validate(data, LocalDate.of(2026, 11, 18));
        assertThat(data.history()).hasSize(6);
        assertThat(data.latest().prices()).hasSize(24);
        assertThat(data.latest().prices().get("us")).isEqualByComparingTo("5.915");
        assertThat(data.releasedOn()).isEqualTo(LocalDate.of(2026, 11, 12));
        assertThat(data.nextRelease()).isEqualTo(LocalDate.of(2026, 11, 18));
    }
    @Test void preservesLastValidSnapshotOnIncompleteOrInvalidPrices() throws Exception {
        var catalog = catalog(); var before = catalog.data();
        var rows = new ArrayList<>(before.history());
        var prices = new HashMap<>(rows.getLast().prices()); prices.put("us", new BigDecimal("-1"));
        rows.set(rows.size()-1, new FuelPriceData.Observation(before.latest().date(), prices));
        var invalid = new FuelPriceData(before.releasedOn(), before.nextRelease(), before.checkedOn(), rows);
        assertThatThrownBy(() -> catalog.accept(invalid)).isInstanceOf(IllegalArgumentException.class);
        assertThat(catalog.data()).isSameAs(before);
        prices.remove("connecticut");
        assertThatThrownBy(() -> catalog.accept(invalid)).isInstanceOf(IllegalArgumentException.class);
        assertThat(Files.exists(storage.resolve("market/heating-oil-prices.json"))).isFalse();
    }
    @Test void atomicallyPersistsRevisionsAndLoadsTheLastValidOverride() throws Exception {
        var catalog = catalog(); var before = catalog.data();
        var rows = new ArrayList<>(before.history());
        var prices = new HashMap<>(before.latest().prices()); prices.put("us", new BigDecimal("5.999"));
        rows.set(rows.size()-1, new FuelPriceData.Observation(before.latest().date(), prices));
        catalog.accept(new FuelPriceData(before.releasedOn(), before.nextRelease(), before.checkedOn(), rows));
        assertThat(catalog().data().latest().prices().get("us")).isEqualByComparingTo("5.999");
        Files.writeString(storage.resolve("market/heating-oil-prices.json"), "invalid");
        assertThat(catalog().latest().points()).filteredOn(p -> p.areaId().equals("us"))
            .extracting(HeatingOilPricePoint::dollarsPerGallon).containsExactly(new BigDecimal("5.915"));
    }
    @Test void rejectsChangedTableLayoutAndOlderObservations() throws Exception {
        assertThatThrownBy(() -> EiaPriceParser.parse("<html>unavailable</html>", LocalDate.of(2026,11,18))).isInstanceOf(IllegalArgumentException.class);
        var catalog = catalog(); var before = catalog.data();
        var older = new FuelPriceData(before.releasedOn(), before.nextRelease(), before.checkedOn(), before.history().subList(0, 5));
        assertThatThrownBy(() -> catalog.accept(older)).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("Older");
        assertThat(catalog.data()).isSameAs(before);
    }
}

package owner.buriedoiltank.heating;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class HeatingOilPriceCatalogTests {
    @Test
    void exposesACompleteDatedOfficialSnapshot() {
        var snapshot = new HeatingOilPriceCatalog().latest();

        assertThat(snapshot.observedOn()).isEqualTo(LocalDate.of(2026, 3, 30));
        assertThat(snapshot.nextScheduledRelease()).isEqualTo(LocalDate.of(2026, 10, 7));
        assertThat(snapshot.points()).hasSize(6);
        assertThat(snapshot.points()).allSatisfy(point -> {
            assertThat(point.areaId()).isNotBlank();
            assertThat(point.areaName()).isNotBlank();
            assertThat(point.dollarsPerGallon()).isGreaterThan(BigDecimal.ZERO);
        });
        assertThat(snapshot.points()).filteredOn(point -> point.areaId().equals("us"))
                .extracting(HeatingOilPricePoint::dollarsPerGallon)
                .containsExactly(new BigDecimal("5.535"));
    }
}

package owner.buriedoiltank.heating;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class HeatingOilPriceCatalog {
    // Refresh runbook: ops/heating_oil_price_refresh.md. Next required review: 2026-10-07.
    // Keep the last verified snapshot when an expected EIA release cannot be confirmed.
    private static final HeatingOilPriceSnapshot LATEST = new HeatingOilPriceSnapshot(
            LocalDate.of(2026, 3, 30),
            LocalDate.of(2026, 4, 1),
            LocalDate.of(2026, 10, 7),
            "U.S. dollars per gallon, excluding taxes",
            "EIA weekly residential price collection is paused between April and September.",
            "https://www.eia.gov/dnav/pet/PET_PRI_WFR_A_EPD2F_PRS_DPGAL_W.htm",
            List.of(
                    point("us", "United States", "5.535"),
                    point("east-coast", "East Coast", "5.583"),
                    point("new-england", "New England", "5.578"),
                    point("connecticut", "Connecticut", "5.546"),
                    point("maine", "Maine", "5.371"),
                    point("massachusetts", "Massachusetts", "5.742")
            )
    );

    public HeatingOilPriceSnapshot latest() {
        return LATEST;
    }

    private static HeatingOilPricePoint point(String id, String name, String price) {
        return new HeatingOilPricePoint(id, name, new BigDecimal(price));
    }
}

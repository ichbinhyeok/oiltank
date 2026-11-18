package owner.buriedoiltank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;
import owner.buriedoiltank.heating.PriceArea;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties={"buried-oil-tank.storage-root=target/fuel-page-tests", "buried-oil-tank.prices.refresh-enabled=false", "buried-oil-tank.notifications.enabled=false"})
@AutoConfigureMockMvc
class FuelPagesTests {
    @Autowired MockMvc mvc;
    @Test void statePricesRenderCrawlableDistinctHistoryAndBudgets() throws Exception {
        String sitemap = mvc.perform(get("/sitemap.xml")).andReturn().getResponse().getContentAsString();
        for (String state : PriceArea.PAGE_STATES) {
            String path = "/heating-oil-prices/" + state + "/";
            var result = mvc.perform(get(path)).andExpect(status().isOk())
                .andExpect(content().string(containsString(PriceArea.require(state).name() + " heating oil prices")))
                .andExpect(content().string(containsString("2026-11-09")))
                .andExpect(content().string(containsString("data-budget-form")))
                .andExpect(content().string(containsString("data.csv")))
                .andReturn().getResponse().getContentAsString();
            assertThat(result).contains("rel=\"canonical\" href=\"http://localhost:8080" + path + "\"");
            assertThat(result.split("<h1>", -1)).hasSize(2);
            assertThat(result).doesNotContain("noindex");
            assertThat(sitemap).contains("<loc>http://localhost:8080" + path + "</loc>");
            mvc.perform(get(path.substring(0,path.length()-1))).andExpect(status().isMovedPermanently()).andExpect(header().string("Location",path));
        }
        mvc.perform(get("/heating-oil-prices/not-a-state/")).andExpect(status().isNotFound());
        mvc.perform(get("/heating-oil-prices/florida/")).andExpect(status().isNotFound());
    }
    @Test void publishesReusablePublicPriceDataWithoutCustomerInformation() throws Exception {
        String csv = mvc.perform(get("/heating-oil-prices/data.csv")).andExpect(status().isOk())
            .andExpect(header().string("Content-Disposition", "attachment; filename=heating-oil-prices.csv"))
            .andReturn().getResponse().getContentAsString();
        assertThat(csv.lines().count()).isEqualTo(145);
        assertThat(csv).contains("2026-11-09,United States,5.915");
        assertThat(csv).doesNotContain("property_address", "email", "lead_id");
    }
    @Test void supplierAndModelPagesPreserveTheirActualScope() throws Exception {
        mvc.perform(get("/heating-oil-delivery/")).andExpect(status().isOk())
            .andExpect(content().string(containsString("Central Bucks Oil")))
            .andExpect(content().string(containsString("data-supplier-state")))
            .andExpect(content().string(containsString("Nassau and Suffolk")));
        mvc.perform(get("/330-gallon-oil-tank/")).andExpect(status().isOk())
            .andExpect(content().string(containsString("330-gallon vertical oil tank chart")))
            .andExpect(content().string(containsString("328.6")));
        mvc.perform(get("/roth-oil-tank-sizes/")).andExpect(status().isOk())
            .andExpect(content().string(containsString("DWT 400L")))
            .andExpect(content().string(containsString("DWT 1500L")))
            .andExpect(content().string(containsString("No reviewed Roth gallons-by-inch chart")));
    }
}

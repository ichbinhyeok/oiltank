package owner.buriedoiltank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import owner.buriedoiltank.heating.FuelGuideCatalog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties={"buried-oil-tank.storage-root=target/fuel-guide-tests", "buried-oil-tank.prices.refresh-enabled=false", "buried-oil-tank.notifications.enabled=false"})
@AutoConfigureMockMvc
class FuelGuidePagesTests {
    @Autowired MockMvc mvc;
    @Test void guidesAreDiscoverableCanonicalAndReadableWithoutJavascript() throws Exception {
        String hub = mvc.perform(get("/heating-oil-guides/")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String sitemap = mvc.perform(get("/sitemap.xml")).andReturn().getResponse().getContentAsString();
        for (var guide : FuelGuideCatalog.ALL) {
            String html = mvc.perform(get(guide.path())).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
            assertThat(html).contains("rel=\"canonical\" href=\"http://localhost:8080" + guide.path() + "\"", "\"@type\":\"Article\"", "id=\"sources\"");
            assertThat(html).doesNotContain("noindex");
            assertThat(html.split("<h1>", -1)).hasSize(2);
            for (var section : guide.sections()) assertThat(html).contains("id=\"" + section.id() + "\"");
            assertThat(hub).contains("href=\"" + guide.path() + "\"");
            assertThat(sitemap).contains("<loc>http://localhost:8080" + guide.path() + "</loc>");
            mvc.perform(get(guide.path().substring(0, guide.path().length()-1))).andExpect(status().isMovedPermanently()).andExpect(header().string("Location", guide.path()));
        }
        mvc.perform(get("/not-a-fuel-guide/")).andExpect(status().isNotFound());
    }
    @Test void orderPagesUseDatedRealDataAndUncoveredStatesDoNotImplySupplierCoverage() throws Exception {
        String order = mvc.perform(get("/100-gallons-heating-oil/")).andReturn().getResponse().getContentAsString();
        assertThat(order).contains("2026-11-09", "$591.50", "$566.70", "fuel subtotals only", "data-budget-form");
        String state = mvc.perform(get("/heating-oil-prices/wisconsin/")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertThat(state).contains("Wisconsin heating oil prices", "versus the U.S. benchmark", "View selected Northeast suppliers");
        assertThat(state).doesNotContain("/heating-oil-delivery/?state=wisconsin");
        String suppliers = mvc.perform(get("/heating-oil-delivery/")).andReturn().getResponse().getContentAsString();
        assertThat(suppliers).doesNotContain("value=\"wisconsin\"");
    }
}

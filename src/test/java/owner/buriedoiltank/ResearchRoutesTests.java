package owner.buriedoiltank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import owner.buriedoiltank.data.ResearchCatalog;
import owner.buriedoiltank.ops.RouteInventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
    "buried-oil-tank.storage-root=target/research-route-test-storage",
    "buried-oil-tank.base-url=http://localhost:8080",
    "buried-oil-tank.notification.enabled=false",
    "buried-oil-tank.admin.username=admin",
    "buried-oil-tank.admin.password=test-admin-password"
})
@AutoConfigureMockMvc
class ResearchRoutesTests {
    @Autowired MockMvc mvc;
    @Autowired RouteInventoryService inventory;
    @Autowired owner.buriedoiltank.ops.OpsSnapshotService snapshots;

    @Test void redesignedPublicShellRetainsServerRenderedIntakeAndLocalAssets() throws Exception {
        for (String path : List.of("/", "/how-it-works/", "/record-research/", "/sample-brief/", "/research-areas/nassau-ny/")) {
            String html = mvc.perform(get(path)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
            assertThat(html).contains("/site-v2.css", "/site-v2.js", "data-form-progress", "data-form-step", "name=\"propertyAddress\"", "name=\"email\"");
            assertThat(html).doesNotContain("fieldset class=\"v2-form-step\" data-form-step hidden");
            assertThat(html).contains("class=\"v2-site-header\"", "class=\"v2-header-inner\"", "not a final report deadline");
            assertThat(html).doesNotContain("class=\"site-header\"", "class=\"site-header__inner\"");
        }
        String hub = mvc.perform(get("/research-areas/")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertThat(hub).contains("data-area-search", "data-area-filter=\"new-jersey\"", "data-area-filter=\"new-york\"", "data-area-empty");
        for (String asset : List.of("/site-v2.css", "/site-v2.js", "/images/property-editorial.jpg")) mvc.perform(get(asset)).andExpect(status().isOk());
    }

    @Test void serviceExpectationsDistinguishReviewDeliveryAndAgencyWork() throws Exception {
        for (String path : List.of("/how-it-works/", "/record-research/", "/sample-brief/")) {
            String html = mvc.perform(get(path)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
            assertThat(html).contains("What do I receive, and when?", "What if no records can be found?",
                "does not place a paid order", "submission is not confirmation that we can take the case");
        }
    }

    @Test void finderIsServerRenderedAndOperationalDiagnosticsStayProtected() throws Exception {
        String finder = mvc.perform(get("/find-records/")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        for (var entry : ResearchCatalog.ALL) assertThat(finder).contains("href=\"" + entry.path() + "\"");
        assertThat(finder).contains("No email required", "Not an automatic address search");
        mvc.perform(get("/admin/exports/record-source-health.json")).andExpect(status().isUnauthorized());
        String health = mvc.perform(get("/admin/exports/record-source-health.json")
            .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:test-admin-password".getBytes(java.nio.charset.StandardCharsets.UTF_8))))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertThat(health).contains("overrides", "reviewDue", "self-service only").doesNotContain("propertyAddress", "target/research-route-test-storage");
    }

    @Test void newSourceScopesRemainVisibleInOperationalFreshnessReview() {
        String review = snapshots.sourceFreshnessReviewJson();
        for (var route : ResearchCatalog.routes()) assertThat(review).contains(route.id(), route.path(), route.nextReviewOn().toString());
    }

    @Test void productionSmokeChecksMatchTheRecordFinderContract() throws Exception {
        String workflow = Files.readString(Path.of(".github/workflows/deploy.yml"));
        String home = mvc.perform(get("/")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String finder = mvc.perform(get("/find-records/")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String sitemap = mvc.perform(get("/sitemap.xml")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertThat(workflow).contains("grep -q 'href=\"/find-records/\"'", "grep -q 'data-record-finder'",
            "grep -q 'https://oiltankroute.com/find-records/'").doesNotContain("Researched for you");
        assertThat(home).contains("href=\"/find-records/\"");
        assertThat(finder).contains("data-record-finder");
        assertThat(sitemap).contains("<loc>http://localhost:8080/find-records/</loc>");
    }

    @Test void expandedCoverageAndTrustPagesStayConnected() throws Exception {
        String hub = mvc.perform(get("/research-areas/")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertThat(hub).contains("24 areas", "Livingston", "Brookhaven", "Oyster Bay", "Portland", "data-area-filter=\"oregon\"");
        String county = mvc.perform(get("/research-areas/suffolk-ny/")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertThat(county).contains("/research-areas/brookhaven-ny/", "/research-areas/huntington-ny/", "/research-areas/islip-ny/");
        String home = mvc.perform(get("/")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertThat(home).contains("Find the right", "No email required", "/find-records/", "Optional NJ/NY research assistance");
        for (String path : List.of("/contact/", "/privacy/", "/terms/", "/methodology/")) {
            String html = mvc.perform(get(path)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
            assertThat(html).doesNotContain("BODY HEIGHT", "BODY LENGTH");
            assertThat(html).contains("/how-it-works/", "/sample-brief/");
        }
    }

    @Test void everyNewRouteRendersAndSharesCanonicalInventoryAndSitemap() throws Exception {
        String sitemap = mvc.perform(get("/sitemap.xml")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String hub = mvc.perform(get(ResearchCatalog.HUB)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        for (var route : ResearchCatalog.routes()) {
            String html = mvc.perform(get(route.path())).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
            assertThat(html).contains("rel=\"canonical\" href=\"http://localhost:8080" + route.path() + "\"", "application/ld+json");
            assertThat(html).doesNotContain("noindex");
            assertThat(sitemap).contains("<loc>http://localhost:8080" + route.path() + "</loc>");
            assertThat(inventory.entries()).anyMatch(e -> e.id().equals(route.id()) && e.path().equals(route.path()));
            mvc.perform(get(route.path().substring(0, route.path().length() - 1))).andExpect(status().isMovedPermanently());
        }
        for (var entry : ResearchCatalog.ALL) {
            String html = mvc.perform(get(entry.path())).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
            assertThat(hub).contains("href=\"" + entry.path() + "\"");
            assertThat(html).contains("data-workbench", "data-route-id=\"" + entry.slug() + "\"");
            assertThat(html).contains("id=\"check-documents\"", "data-request-scope", "Still missing?");
            if (entry.assisted()) {
                assertThat(html).contains("name=\"pageId\" value=\"" + entry.id() + "\"", "name=\"pagePath\" value=\"" + entry.path() + "\"");
                assertThat(html).contains("value=\"" + entry.question() + "\" selected");
                if (!entry.state().isBlank()) assertThat(html).contains("value=\"" + entry.state() + "\" selected");
            } else {
                assertThat(html).doesNotContain("data-research-form", "name=\"propertyAddress\"", "name=\"email\"");
                assertThat(html).contains("Self-service guidance only");
            }
            // All local destinations on the new page are real, including related and safety links.
            var matcher = Pattern.compile("href=\"(/[^\"?#]*)\"").matcher(html);
            var visited = new java.util.HashSet<String>();
            while (matcher.find()) if (visited.add(matcher.group(1))) mvc.perform(get(matcher.group(1))).andExpect(status().isOk());
        }
    }

    @Test void wrongKindsAndUnknownLocationsAreNotSoft404s() throws Exception {
        for (String path : List.of("/research-areas/unknown/", "/research-areas/missing-removal-records/", "/record-help/nassau-ny/", "/record-help/unknown/"))
            mvc.perform(get(path)).andExpect(status().isNotFound());
    }

    @Test void preservedCountyPagesLinkLocalRouteAndRetainFormOrigin() throws Exception {
        for (String county : List.of("nassau", "suffolk", "westchester")) {
            String path = "/states/new-york/counties/" + county + "/heating-oil-spill-records/";
            String html = mvc.perform(get(path)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
            assertThat(html).contains("href=\"/research-areas/" + county + "-ny/\"", "name=\"pagePath\" value=\"" + path + "\"", "name=\"pageId\" value=\"new-york:county:" + county + "\"");
        }
    }

    @Test void regionalIntakeKeepsItsOriginAndCreatesPrivateCase() throws Exception {
        String path = "/research-areas/nassau-ny/";
        var response = mvc.perform(multipart("/api/leads/capture")
            .header("Origin", "http://localhost:8080")
            .param("pageId", "research:nassau-ny").param("pagePath", path)
            .param("routeFamily", "record-research").param("scenario", "records_first").param("partnerType", "record_research")
            .param("submissionToken", "research-route-" + java.util.UUID.randomUUID())
            .param("propertyAddress", "12 Test Only Lane").param("stateSlug", "new-york")
            .param("countyMunicipality", "Nassau").param("userRole", "buyer").param("tankStatus", "unknown")
            .param("primaryQuestion", "find_records").param("hasDocuments", "no").param("email", "qa@example.com"))
            .andExpect(status().is3xxRedirection()).andReturn().getResponse();
        assertThat(response.getRedirectedUrl()).startsWith(path + "?lead=success&receipt=OTR-");
        assertThat(Files.readString(Path.of("target/research-route-test-storage/leads/leads.csv"))).contains("research:nassau-ny", path);
    }
}

package owner.buriedoiltank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import owner.buriedoiltank.ops.RouteInventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
		"buried-oil-tank.storage-root=target/test-storage",
		"buried-oil-tank.base-url=http://localhost:8080",
		"buried-oil-tank.admin.username=admin",
		"buried-oil-tank.admin.password=test-admin-password"
})
@AutoConfigureMockMvc
class BuriedOilTankVerdictApplicationTests {
	private static final Path STORAGE_ROOT = Path.of("target", "test-storage");
	private static final String ADMIN_USERNAME = "admin";
	private static final String ADMIN_PASSWORD = "test-admin-password";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RouteInventoryService routeInventoryService;

	@BeforeEach
	void cleanStorage() throws IOException {
		if (Files.exists(STORAGE_ROOT)) {
			try (var walk = Files.walk(STORAGE_ROOT)) {
				walk.sorted(Comparator.reverseOrder()).forEach(path -> {
					try {
						Files.deleteIfExists(path);
					} catch (IOException exception) {
						throw new IllegalStateException(exception);
					}
				});
			}
		}
	}

	@Test
	void contextLoads() {
		assertThat(routeInventoryService.entries()).hasSize(61);
		assertThat(routeInventoryService.indexableEntries()).hasSize(31);
		assertThat(routeInventoryService.entries())
				.filteredOn(entry -> entry.pageType() == owner.buriedoiltank.data.PageType.PRODUCT)
				.extracting(owner.buriedoiltank.data.RouteInventoryEntry::path)
				.containsExactlyInAnyOrderElementsOf(owner.buriedoiltank.pages.ProductPageService.CORE_PATHS);
	}

	@Test
	void publicPagesUseStrictSecurityHeadersWithoutInlineExecutableScripts() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(header().string("Content-Security-Policy", containsString("default-src 'self'")))
				.andExpect(header().string("Content-Security-Policy", containsString("object-src 'none'")))
				.andExpect(header().string("Content-Security-Policy", containsString("frame-ancestors 'none'")))
				.andExpect(content().string(not(containsString("document.documentElement.classList"))))
				.andExpect(content().string(not(containsString("window.dataLayer ="))));
	}

	@Test
	void knownPublicPathsRedirectToTheirTrailingSlashCanonical() throws Exception {
		mockMvc.perform(get("/heating-oil-tank").queryParam("source", "test"))
				.andExpect(status().isMovedPermanently())
				.andExpect(header().string("Location", "/heating-oil-tank/?source=test"));

		mockMvc.perform(get("/this-route-does-not-exist"))
				.andExpect(status().isNotFound());
	}

	@Test
	void launchRoutesRenderWithMetadataAndSitemapRules() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Oil Tank Route")))
				.andExpect(content().string(containsString("Know the tank. Plan the next move.")))
				.andExpect(content().string(containsString("A single data model. Twenty useful routes.")))
				.andExpect(content().string(containsString("Heating-oil tank cross-section")))
				.andExpect(content().string(containsString("Official source material")))
				.andExpect(content().string(containsString("property=\"og:image\"")))
				.andExpect(content().string(containsString("application/ld+json")))
				.andExpect(content().string(containsString("href=\"/states/\"")))
				.andExpect(content().string(containsString("href=\"/heating-oil-tank/\"")))
				.andExpect(content().string(containsString("href=\"/contact/\"")))
				.andExpect(content().string(not(containsString("href=\"/admin/\""))))
				.andExpect(content().string(not(containsString("images.unsplash.com"))));

		for (String path : List.of(
				"/heating-oil-tank/",
				"/heating-oil-tank-sizes-dimensions/",
				"/275-gallon-oil-tank/",
				"/heating-oil-tank-charts/",
				"/oil-tank-gauge-calculator/",
				"/heating-oil-delivery-check/",
				"/heating-oil-usage-calculator/",
				"/heating-oil-prices/",
				"/heating-oil-cost-calculator/",
				"/how-much-heating-oil-do-i-need/",
				"/ran-out-of-heating-oil/",
				"/oil-tank-capacity-calculator/",
				"/how-long-do-oil-tanks-last/",
				"/oil-tank-gauge-replacement/",
				"/heating-oil-tank-repair/",
				"/heating-oil-tank-sludge-cleaning/",
				"/oil-tank-replacement-planner/",
				"/oil-tank-replacement-cost/",
				"/heating-oil-tank-installation-cost/",
				"/basement-oil-tank-removal/"
		)) {
			mockMvc.perform(get(path))
					.andExpect(status().isOk())
					.andExpect(content().string(containsString("<link rel=\"canonical\" href=\"http://localhost:8080" + path + "\"")))
					.andExpect(content().string(containsString("<meta name=\"description\"")))
					.andExpect(content().string(containsString("application/ld+json")))
					.andExpect(content().string(containsString("Official source material")))
					.andExpect(content().string(not(containsString("images.unsplash.com"))));
		}

		mockMvc.perform(get("/oil-tank-gauge-calculator/"))
				.andExpect(content().string(containsString("Granby U.S. vertical tank capacity chart")))
				.andExpect(content().string(containsString("manufacturer chart")))
				.andExpect(content().string(not(containsString("275-gallon vertical oil tank chart: inches to gallons"))))
				.andExpect(content().string(not(containsString("Request a qualified follow-up"))));

		mockMvc.perform(get("/275-gallon-oil-tank/"))
				.andExpect(content().string(containsString("275-gallon vertical oil tank chart: inches to gallons")))
                .andExpect(content().string(containsString("Granby 204201P Standard 20Plus 275-gallon vertical oil tank chart")))
				.andExpect(content().string(containsString("44 in")))
				.andExpect(content().string(containsString("273.8 gal")))
				.andExpect(content().string(containsString("58.2 gallons")))
				.andExpect(content().string(containsString("href=\"/oil-tank-gauge-calculator/\"")));

		mockMvc.perform(get("/heating-oil-tank-charts/"))
				.andExpect(content().string(containsString("Heating-oil tank charts: verified inches-to-gallons tables")))
				.andExpect(content().string(containsString("Granby 208101 Standard 138-gallon vertical oil tank chart")))
				.andExpect(content().string(containsString("Granby 205201P Standard 20Plus 330-gallon vertical oil tank chart")))
				.andExpect(content().string(containsString("328.6 gal")))
				.andExpect(content().string(containsString("href=\"/275-gallon-oil-tank/#275-gallon-chart\"")))
				.andExpect(content().string(not(containsString("Granby 204201P Standard 20Plus 275-gallon vertical oil tank chart"))))
				.andExpect(content().string(containsString("\"@type\":\"CollectionPage\"")));

		mockMvc.perform(get("/heating-oil-usage-calculator/"))
				.andExpect(content().string(containsString("data-usage-form")))
				.andExpect(content().string(containsString("No weather guess and no email gate")))
				.andExpect(content().string(not(containsString("Request a qualified follow-up"))));

		mockMvc.perform(get("/heating-oil-delivery-check/"))
				.andExpect(content().string(containsString("data-delivery-form")))
				.andExpect(content().string(containsString("Tank Passport")))
				.andExpect(content().string(containsString("Delaware Department of Agriculture")))
				.andExpect(content().string(containsString("truck-meter ticket is the transaction record")))
				.andExpect(content().string(not(containsString("Request a qualified follow-up"))));

		mockMvc.perform(get("/oil-tank-replacement-planner/"))
				.andExpect(content().string(containsString("data-commercial-route")))
				.andExpect(content().string(containsString("data-lead-risk")))
				.andExpect(content().string(containsString("Start with the failed layer")))
				.andExpect(content().string(containsString("Should I repair or replace an oil tank?")))
				.andExpect(content().string(containsString("href=\"/oil-tank-replacement-cost/\"")));

		mockMvc.perform(get("/oil-tank-gauge-calculator/"))
				.andExpect(content().string(containsString("Gauge troubleshooting")))
				.andExpect(content().string(containsString("A strange reading is a diagnosis prompt")))
				.andExpect(content().string(containsString("Vent whistle is weak or silent")));

		mockMvc.perform(get("/heating-oil-tank-sludge-cleaning/"))
				.andExpect(content().string(containsString("Heating-oil tank sludge and cleaning: choose the safe next step")))
				.andExpect(content().string(containsString("data-sludge-form")))
				.andExpect(content().string(containsString("data-tool-id=\"sludge-router\"")))
				.andExpect(content().string(containsString("Maine DEP: Check Your Tank, Prevent a Leak")))
				.andExpect(content().string(containsString("This router does not provide DIY")))
				.andExpect(content().string(containsString("data-commercial-route")));

		mockMvc.perform(get("/how-long-do-oil-tanks-last/"))
				.andExpect(content().string(containsString("How long do heating-oil tanks last?")))
				.andExpect(content().string(containsString("Tank age starts the review. Condition decides the route.")))
				.andExpect(content().string(containsString("Is there a standard age when every oil tank must be replaced?")))
				.andExpect(content().string(containsString("New York State Department of Health")))
				.andExpect(content().string(not(containsString("Request a qualified follow-up"))));

		mockMvc.perform(get("/heating-oil-prices/"))
				.andExpect(content().string(containsString("Latest published residential benchmark")))
				.andExpect(content().string(containsString("2026-03-30")))
				.andExpect(content().string(containsString("$5.535")))
				.andExpect(content().string(containsString("collection is paused")))
				.andExpect(content().string(not(containsString("Request a qualified follow-up"))));

		mockMvc.perform(get("/heating-oil-cost-calculator/"))
				.andExpect(content().string(containsString("data-heating-cost-form")))
				.andExpect(content().string(containsString("EIA observation 2026-03-30")))
				.andExpect(content().string(containsString("\"@type\":\"WebApplication\"")))
				.andExpect(content().string(not(containsString("Request a qualified follow-up"))));

		mockMvc.perform(get("/how-much-heating-oil-do-i-need/"))
				.andExpect(content().string(containsString("data-order-form")))
				.andExpect(content().string(containsString("planning space")))
				.andExpect(content().string(containsString("\"@type\":\"WebApplication\"")))
				.andExpect(content().string(not(containsString("Request a qualified follow-up"))));

		mockMvc.perform(get("/ran-out-of-heating-oil/"))
				.andExpect(content().string(containsString("Check once. Then call the right service.")))
				.andExpect(content().string(containsString("do not reset again")))
				.andExpect(content().string(containsString("Possible leak or spill")))
				.andExpect(content().string(not(containsString("Request a qualified follow-up"))));

		mockMvc.perform(get("/oil-tank-gauge-replacement/"))
				.andExpect(content().string(containsString("Oil tank gauge replacement and troubleshooting")))
				.andExpect(content().string(containsString("A bad reading does not identify the failed part.")))
				.andExpect(content().string(containsString("Can I replace an oil tank gauge myself?")))
				.andExpect(content().string(containsString("href=\"/oil-tank-gauge-calculator/\"")))
				.andExpect(content().string(not(containsString("Request a qualified follow-up"))));

		mockMvc.perform(get("/heating-oil-tank-repair/"))
				.andExpect(content().string(containsString("Heating-oil tank repair: diagnose the failed layer first")))
				.andExpect(content().string(containsString("Repair a service component. Review a distressed tank body.")))
				.andExpect(content().string(containsString("Can a leaking oil tank be patched?")))
				.andExpect(content().string(containsString("href=\"/guides/leaking-heating-oil-tank-what-to-do/\"")));

		mockMvc.perform(get("/oil-tank-replacement-cost/"))
				.andExpect(content().string(containsString("Oil tank replacement cost: compare the complete quote scope")))
				.andExpect(content().string(containsString("href=\"#cost-scope\"")))
				.andExpect(content().string(containsString("id=\"cost-scope\"")))
				.andExpect(content().string(containsString("data-replacement-cost-form")))
				.andExpect(content().string(containsString("Every written replacement quote should resolve these boundaries")))
				.andExpect(content().string(containsString("Maine DEP: Preventing heating-oil spills")))
				.andExpect(content().string(containsString("href=\"/heating-oil-tank-installation-cost/\"")))
				.andExpect(content().string(not(containsString("data-planner-form"))))
				.andExpect(content().string(not(containsString("data-removal-form"))))
				.andExpect(content().string(not(containsString("\"@type\":\"WebApplication\""))));

		mockMvc.perform(get("/heating-oil-tank-installation-cost/"))
				.andExpect(content().string(containsString("Heating-oil tank installation cost: build a complete quote scope")))
				.andExpect(content().string(containsString("href=\"#cost-scope\"")))
				.andExpect(content().string(containsString("id=\"cost-scope\"")))
				.andExpect(content().string(containsString("data-installation-cost-form")))
				.andExpect(content().string(containsString("The installed total has six inspectable parts")))
				.andExpect(content().string(containsString("Granby UL-80 installation and maintenance guidelines")))
				.andExpect(content().string(containsString("href=\"/oil-tank-replacement-cost/\"")))
				.andExpect(content().string(not(containsString("data-planner-form"))))
				.andExpect(content().string(not(containsString("\"@type\":\"WebApplication\""))));

		mockMvc.perform(get("/basement-oil-tank-removal/"))
				.andExpect(content().string(containsString("data-removal-form")))
				.andExpect(content().string(containsString("name=\"tankType\" value=\"aboveground_indoor\"")))
				.andExpect(content().string(containsString("data-commercial-route")));

		mockMvc.perform(get("/states/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Buried oil tank state pages for disclosure, records, and closing steps")))
				.andExpect(content().string(containsString("Current launch states")))
				.andExpect(content().string(containsString("Choose the state that controls the next permit search or document request.")))
				.andExpect(content().string(containsString("application/ld+json")));

		mockMvc.perform(get("/guides/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Buried oil tank guides for records, sweep, removal, leak, and cost questions")))
				.andExpect(content().string(containsString("Public route guides")))
				.andExpect(content().string(containsString("Start with the guide that matches the question in front of you.")))
				.andExpect(content().string(containsString("application/ld+json")));

		mockMvc.perform(get("/routes/"))
				.andExpect(status().isMovedPermanently())
				.andExpect(redirectedUrl("/guides/"));

		List<String> states = List.of("new-jersey", "new-york");
		for (String state : states) {
			mockMvc.perform(get("/states/" + state + "/"))
					.andExpect(status().isOk())
					.andExpect(content().string(containsString("http://localhost:8080/states/" + state + "/")))
					.andExpect(content().string(containsString("application/ld+json")))
					.andExpect(content().string(containsString("Source status")))
					.andExpect(content().string(containsString("Documents that change the answer")))
					.andExpect(content().string(containsString("Keep the first step narrow.")))
					.andExpect(content().string(containsString("Jump to first steps")))
					.andExpect(content().string(containsString("Optional worksheet")))
					.andExpect(content().string(not(containsString("/states/" + state + "/cost-direction/"))))
					.andExpect(content().string(not(containsString("/states/" + state + "/leak-and-cleanup/"))))
					.andExpect(content().string(not(containsString("/states/" + state + "/removal-vs-abandonment/"))));

			for (String route : List.of("records-and-proof")) {
				mockMvc.perform(get("/states/" + state + "/" + route + "/"))
						.andExpect(status().isOk())
						.andExpect(content().string(containsString("<link rel=\"canonical\" href=\"http://localhost:8080/states/" + state + "/" + route + "/\"")))
						.andExpect(content().string(containsString("application/ld+json")))
						.andExpect(content().string(containsString("Official lookup sequence")))
						.andExpect(content().string(containsString("No address stored")))
						.andExpect(content().string(containsString("Optional worksheet")));
			}

			for (String heldRoute : List.of("removal-vs-abandonment", "leak-and-cleanup", "cost-direction")) {
				mockMvc.perform(get("/states/" + state + "/" + heldRoute + "/"))
						.andExpect(status().isOk())
						.andExpect(content().string(containsString("<meta name=\"robots\" content=\"noindex,follow\">")));
			}
		}

		for (String state : List.of("connecticut", "maine")) {
			mockMvc.perform(get("/states/" + state + "/"))
					.andExpect(status().isMovedPermanently())
					.andExpect(redirectedUrl("/states/"));
			mockMvc.perform(get("/states/" + state + "/records-and-proof/"))
					.andExpect(status().isMovedPermanently())
					.andExpect(redirectedUrl("/guides/abandoned-oil-tank-records/"));
		}

		mockMvc.perform(get("/states/new-york/buyer-seller/"))
				.andExpect(status().isMovedPermanently())
				.andExpect(redirectedUrl("/guides/buried-oil-tank-home-sale/"));
		mockMvc.perform(get("/states/new-york/sweep-and-locate/"))
				.andExpect(status().isMovedPermanently())
				.andExpect(redirectedUrl("/guides/oil-tank-sweep-before-buying-house/"));
		mockMvc.perform(get("/states/new-york/counties/westchester/heating-oil-spill-records/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Westchester County heating-oil spill records")))
				.andExpect(content().string(containsString("906")));

		mockMvc.perform(get("/states/massachusetts/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("<meta name=\"robots\" content=\"noindex,follow\">")))
				.andExpect(content().string(containsString("Public coverage stays narrower here.")))
				.andExpect(content().string(not(containsString("/states/massachusetts/cost-direction/"))));

		mockMvc.perform(get("/states/massachusetts/buyer-seller/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("<meta name=\"robots\" content=\"noindex,follow\">")));

		mockMvc.perform(get("/guides/abandoned-oil-tank-records/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Then go to your state page for permits, agency rules, and the right next call.")))
				.andExpect(content().string(containsString("What this guide gives you")))
				.andExpect(content().string(containsString("Before you call a contractor")))
				.andExpect(content().string(containsString("How to Find Abandoned Oil Tank Records Before Closing")))
				.andExpect(content().string(not(containsString("Massachusetts"))));

		for (String guide : List.of("remove-vs-abandon-oil-tank")) {
			mockMvc.perform(get("/guides/" + guide + "/"))
					.andExpect(status().isOk())
					.andExpect(content().string(containsString("<meta name=\"robots\" content=\"noindex,follow\">")))
					.andExpect(content().string(containsString("Why this page is trustworthy")))
					.andExpect(content().string(containsString("application/ld+json")));
		}

		for (String guide : List.of("leaking-heating-oil-tank-what-to-do", "oil-tank-removal-cost")) {
			mockMvc.perform(get("/guides/" + guide + "/"))
					.andExpect(status().isOk())
					.andExpect(content().string(not(containsString("<meta name=\"robots\" content=\"noindex,follow\">"))))
					.andExpect(content().string(containsString("Why this page is trustworthy")))
					.andExpect(content().string(containsString("application/ld+json")));
		}

		mockMvc.perform(get("/guides/oil-tank-removal-cost/"))
				.andExpect(content().string(containsString("Make pump-out, reusable-fuel transfer")))
				.andExpect(content().string(containsString("Waste profile, transporter receipt")));

		mockMvc.perform(get("/methodology/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("We start with permits, disclosure, and site facts before talking removal, cleanup, or cost.")))
				.andExpect(content().string(containsString("Every page gets a source check, review date, and scope check before it stays public.")));

		mockMvc.perform(get("/contact/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("mailto:shinhyeok22@gmail.com")))
				.andExpect(content().string(containsString("shinhyeok22@gmail.com")));

		mockMvc.perform(get("/states/new-jersey/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Any NJDEP case number, fund paperwork, or no-further-action letter tied to the property.")));

		mockMvc.perform(get("/states/new-jersey/buyer-seller/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Ask for the seller disclosure form, any closure permit or contractor invoice, and oil-to-gas paperwork in one request.")));

		mockMvc.perform(get("/sitemap.xml"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("/states/")))
				.andExpect(content().string(containsString("/guides/")))
				.andExpect(content().string(containsString("/oil-tank-gauge-calculator/")))
				.andExpect(content().string(containsString("/heating-oil-tank-charts/")))
				.andExpect(content().string(containsString("/heating-oil-delivery-check/")))
				.andExpect(content().string(containsString("/heating-oil-usage-calculator/")))
				.andExpect(content().string(containsString("/how-long-do-oil-tanks-last/")))
				.andExpect(content().string(containsString("/oil-tank-gauge-replacement/")))
				.andExpect(content().string(containsString("/heating-oil-tank-repair/")))
				.andExpect(content().string(containsString("/heating-oil-tank-sludge-cleaning/")))
				.andExpect(content().string(containsString("/oil-tank-replacement-cost/")))
				.andExpect(content().string(containsString("/heating-oil-tank-installation-cost/")))
				.andExpect(content().string(containsString("/basement-oil-tank-removal/")))
				.andExpect(content().string(containsString("/guides/leaking-heating-oil-tank-what-to-do/")))
				.andExpect(content().string(containsString("/guides/oil-tank-removal-cost/")))
				.andExpect(content().string(containsString("<priority>0.9</priority>")))
				.andExpect(content().string(containsString("/states/new-york/counties/westchester/heating-oil-spill-records/")))
				.andExpect(content().string(containsString("<lastmod>2026-07-10</lastmod>")))
				.andExpect(content().string(containsString("/states/new-jersey/buyer-seller/")))
				.andExpect(content().string(not(containsString("/routes/"))))
				.andExpect(content().string(not(containsString("/guides/remove-vs-abandon-oil-tank/"))))
				.andExpect(content().string(not(containsString("/privacy/"))))
				.andExpect(content().string(not(containsString("/states/new-jersey/cost-direction/"))))
				.andExpect(content().string(not(containsString("/states/massachusetts/"))));

		mockMvc.perform(get("/robots.txt"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Disallow: /admin")))
				.andExpect(content().string(containsString("Disallow: /api")))
				.andExpect(content().string(containsString("Sitemap: http://localhost:8080/sitemap.xml")));

		mockMvc.perform(get("/admin/"))
				.andExpect(status().isUnauthorized())
				.andExpect(header().string("X-Robots-Tag", "noindex, nofollow, noarchive"));
	}

	@Test
	void leadCaptureAndEventLoggingStoreScenarioContext() throws Exception {
		mockMvc.perform(post("/api/leads/event")
						.contentType("application/x-www-form-urlencoded")
						.header("Origin", "http://localhost:8080")
						.param("eventType", "cta_click")
						.param("pageId", "new-jersey:buyer-seller")
						.param("pagePath", "/states/new-jersey/buyer-seller/")
						.param("stateSlug", "new-jersey")
						.param("routeFamily", "buyer-seller")
						.param("scenario", "buyer_seller")
						.param("partnerType", "")
						.param("element", "lead-form-toggle")
						.param("referrer", ""))
				.andExpect(status().isAccepted());

		mockMvc.perform(post("/api/leads/capture")
						.contentType("application/x-www-form-urlencoded")
						.header("Origin", "http://localhost:8080")
						.param("pageId", "new-jersey:buyer-seller")
						.param("pagePath", "/states/new-jersey/buyer-seller/")
						.param("stateSlug", "new-jersey")
						.param("routeFamily", "buyer-seller")
						.param("scenario", "leak_concern")
						.param("partnerType", "")
						.param("userRole", "buyer")
						.param("tankStatus", "records_missing")
						.param("zipCode", "07030")
						.param("closingTimeline", "one_to_three_weeks")
						.param("email", "owner@example.com")
						.param("name", "Owner")
						.param("toolId", "replacement-planner")
						.param("tankType", "underground_unknown")
						.param("riskBand", "urgent")
						.param("commercialIntent", "leak_remediation")
						.param("resultSummary", "Leak response route")
						.param("notes", "Need next step before closing"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/states/new-jersey/buyer-seller/?lead=success"));

		String leadsCsv = Files.readString(STORAGE_ROOT.resolve("leads").resolve("leads.csv"));
		String leadId = leadsCsv.lines().skip(1).findFirst().orElseThrow().split(",", 2)[0];
		assertThat(leadId).isNotBlank();
		assertThat(leadsCsv).contains("owner@example.com");
		assertThat(leadsCsv).contains("environmental_cleanup");
		assertThat(leadsCsv).contains("buyer");
		assertThat(leadsCsv).contains("records_missing");
		assertThat(leadsCsv).contains("07030");
		assertThat(leadsCsv).contains("tool_id,tank_type,risk_band,commercial_intent,result_summary");
		assertThat(leadsCsv).contains("replacement-planner");
		assertThat(leadsCsv).contains("leak_remediation");

		String eventsCsv = Files.readString(STORAGE_ROOT.resolve("leads").resolve("lead_events.csv"));
		assertThat(eventsCsv).contains("cta_click");
		assertThat(eventsCsv).contains("lead_submit");
		assertThat(eventsCsv).contains("new-jersey");
		assertThat(eventsCsv).contains("buyer-seller");
		assertThat(eventsCsv).contains("environmental_cleanup");

		mockMvc.perform(get("/admin/").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(header().string("X-Robots-Tag", "noindex, nofollow, noarchive"))
				.andExpect(content().string(containsString("Operations dashboard")))
				.andExpect(content().string(containsString("Lead submissions (28d)")))
				.andExpect(content().string(containsString("Review approval and payout")))
				.andExpect(content().string(containsString("Tool funnel / 28 days")))
				.andExpect(content().string(containsString("Freshness review queue")))
				.andExpect(content().string(containsString("/states/new-jersey/buyer-seller/")));

		mockMvc.perform(post("/admin/leads/decision")
					.with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD))
					.with(csrf())
					.param("leadId", leadId)
					.param("disposition", "approved")
					.param("payoutCents", "2500")
					.param("notes", "Approved test lead"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/?decision=saved#lead-review"));

		mockMvc.perform(get("/admin/exports/route-status.csv").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(header().string("X-Robots-Tag", "noindex, nofollow, noarchive"))
				.andExpect(content().string(containsString("route_id,route_path,route_family")))
				.andExpect(content().string(containsString("/states/new-jersey/buyer-seller/")));

		mockMvc.perform(get("/admin/exports/promotion-review.json").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"agentSummary\"")))
				.andExpect(content().string(containsString("\"reviewDate\"")));

		mockMvc.perform(get("/admin/exports/source-freshness-review.json").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"scopeType\" : \"state\"")))
				.andExpect(content().string(containsString("\"scopeLabel\" : \"New Jersey\"")));

		mockMvc.perform(get("/admin/exports/admin-metrics-snapshot.json").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"leadSubmissions\" : 1")))
				.andExpect(content().string(containsString("\"approvedLeads\" : 1")))
				.andExpect(content().string(containsString("\"approvedPayoutCents\" : 2500")))
				.andExpect(content().string(containsString("\"toolId\" : \"replacement-planner\"")))
				.andExpect(content().string(containsString("\"ctaClicks\" : 1")))
				.andExpect(content().string(containsString("\"staleScopeCount\"")));

		mockMvc.perform(get("/admin/exports/routes.json").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"path\" : \"/states/new-jersey/buyer-seller/\"")))
				.andExpect(content().string(containsString("\"routeFamily\" : \"buyer-seller\"")))
				.andExpect(content().string(containsString("\"pageType\" : \"PRODUCT\"")))
				.andExpect(content().string(containsString("\"path\" : \"/oil-tank-gauge-calculator/\"")));

		mockMvc.perform(get("/admin/exports/leads.csv").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("owner@example.com")))
				.andExpect(content().string(containsString("environmental_cleanup")));

		mockMvc.perform(get("/admin/exports/lead-events.csv").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("cta_click")))
				.andExpect(content().string(containsString("lead_submit")));

		mockMvc.perform(get("/admin/exports/lead-dispositions.csv").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("approved,2500,Approved test lead")));

		assertThat(Files.exists(STORAGE_ROOT.resolve("ops").resolve("route-status.csv"))).isTrue();
		assertThat(Files.exists(STORAGE_ROOT.resolve("ops").resolve("promotion-review.json"))).isTrue();
		assertThat(Files.exists(STORAGE_ROOT.resolve("ops").resolve("admin-metrics-snapshot.json"))).isTrue();
		assertThat(Files.exists(STORAGE_ROOT.resolve("ops").resolve("source-freshness-review.json"))).isTrue();
		assertThat(Files.exists(STORAGE_ROOT.resolve("derived").resolve("routes.json"))).isTrue();
	}

	@Test
	void malformedLeadEventPayloadReturnsBadRequestInsteadOfServerError() throws Exception {
		mockMvc.perform(post("/api/leads/event")
						.contentType("application/x-www-form-urlencoded")
						.header("Origin", "http://localhost:8080")
						.param("eventType", "cta_click")
						.param("pageId", "new-jersey:buyer-seller")
						.param("pagePath", "/states/new-jersey/buyer-seller/"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void oversizedAnalyticsPayloadIsRejectedAndUnsafeReturnPathFallsBackToRoot() throws Exception {
		mockMvc.perform(post("/api/leads/event")
						.contentType("application/x-www-form-urlencoded")
						.header("Origin", "http://localhost:8080")
						.param("eventType", "x".repeat(65))
						.param("pageId", "security-test")
						.param("pagePath", "/")
						.param("scenario", "records_first"))
				.andExpect(status().isBadRequest());

		mockMvc.perform(post("/api/leads/event")
						.contentType("application/x-www-form-urlencoded")
						.header("Origin", "http://localhost:8080")
						.param("eventType", "invented_metric")
						.param("pageId", "security-test")
						.param("pagePath", "/")
						.param("scenario", "records_first"))
				.andExpect(status().isBadRequest());

		mockMvc.perform(post("/api/leads/capture")
						.contentType("application/x-www-form-urlencoded")
						.header("Origin", "http://localhost:8080")
						.param("pageId", "security-test")
						.param("pagePath", "/\\evil.example")
						.param("stateSlug", "national")
						.param("scenario", "records_first")
						.param("userRole", "homeowner")
						.param("tankStatus", "unknown")
						.param("email", "not-an-email"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/?lead=error"));
	}

	@Test
	void crossOriginLeadRequestsAreRejected() throws Exception {
		mockMvc.perform(post("/api/leads/event")
						.contentType("application/x-www-form-urlencoded")
						.header("Origin", "https://evil.example")
						.param("eventType", "cta_click")
						.param("pageId", "new-jersey:buyer-seller")
						.param("pagePath", "/states/new-jersey/buyer-seller/")
						.param("stateSlug", "new-jersey")
						.param("routeFamily", "buyer-seller")
						.param("scenario", "buyer_seller"))
				.andExpect(status().isForbidden());

		mockMvc.perform(post("/api/leads/capture")
						.contentType("application/x-www-form-urlencoded")
						.header("Origin", "https://evil.example")
						.param("pageId", "new-jersey:buyer-seller")
						.param("pagePath", "/states/new-jersey/buyer-seller/")
						.param("stateSlug", "new-jersey")
						.param("routeFamily", "buyer-seller")
						.param("scenario", "buyer_seller")
						.param("userRole", "buyer")
						.param("tankStatus", "records_missing")
						.param("email", "owner@example.com"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/states/new-jersey/buyer-seller/?lead=error"));
	}

}

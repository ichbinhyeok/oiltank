package owner.buriedoiltank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
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
		assertThat(routeInventoryService.entries()).hasSize(41);
		assertThat(routeInventoryService.indexableEntries()).hasSize(22);
	}

	@Test
	void launchRoutesRenderWithMetadataAndSitemapRules() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Buried Oil Tank")))
				.andExpect(content().string(containsString("<span class=\"hero__accent\">Before Closing</span>")))
				.andExpect(content().string(containsString("Next Steps for")))
				.andExpect(content().string(containsString("Buyers and Sellers")))
				.andExpect(content().string(containsString("suspected buried residential heating-oil tank or missing records before closing")))
				.andExpect(content().string(containsString("Use the state page or route guide before the file widens into assumption.")))
				.andExpect(content().string(containsString("Support routes")))
				.andExpect(content().string(containsString("Keep these cases separate")))
				.andExpect(content().string(containsString("Current state coverage")))
				.andExpect(content().string(containsString("Editorial standard")))
				.andExpect(content().string(containsString("Official guidance first. Transaction support after the record is clear.")))
				.andExpect(content().string(containsString("property=\"og:image\"")))
				.andExpect(content().string(containsString("application/ld+json")))
				.andExpect(content().string(containsString("href=\"/states/\"")))
				.andExpect(content().string(containsString("href=\"/routes/\"")))
				.andExpect(content().string(containsString("href=\"/guides/\"")))
				.andExpect(content().string(containsString("href=\"/contact/\"")))
				.andExpect(content().string(not(containsString("href=\"/admin/\""))))
				.andExpect(content().string(not(containsString("/states/massachusetts/"))));

		mockMvc.perform(get("/states/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Buried oil tank state pages for disclosure, records, and closing steps")))
				.andExpect(content().string(containsString("Current launch states")))
				.andExpect(content().string(containsString("Choose the state that controls the next document request.")))
				.andExpect(content().string(containsString("application/ld+json")));

		mockMvc.perform(get("/guides/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Buried oil tank guides for records, sweep, removal, leak, and cost questions")))
				.andExpect(content().string(containsString("Public route guides")))
				.andExpect(content().string(containsString("Start with the guide that matches the question on the file.")))
				.andExpect(content().string(containsString("application/ld+json")));

		mockMvc.perform(get("/routes/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Buried oil tank route guides for disclosure, records, sweep, removal, leak, and cost direction")))
				.andExpect(content().string(containsString("Choose the route family that matches the evidence on the file.")))
				.andExpect(content().string(containsString("href=\"/guides/remove-vs-abandon-oil-tank/\"")))
				.andExpect(content().string(containsString("href=\"/guides/leaking-heating-oil-tank-what-to-do/\"")))
				.andExpect(content().string(containsString("href=\"/guides/oil-tank-removal-cost/\"")))
				.andExpect(content().string(containsString("application/ld+json")));

		List<String> states = List.of("new-jersey", "new-york", "connecticut", "maine");
		for (String state : states) {
			mockMvc.perform(get("/states/" + state + "/"))
					.andExpect(status().isOk())
					.andExpect(content().string(containsString("http://localhost:8080/states/" + state + "/")))
					.andExpect(content().string(containsString("application/ld+json")))
					.andExpect(content().string(containsString("Source review status")))
					.andExpect(content().string(containsString("Documents that change the answer")))
					.andExpect(content().string(containsString("Advanced support stays contextual")))
					.andExpect(content().string(containsString("Open the file checklist")))
					.andExpect(content().string(not(containsString("/states/" + state + "/cost-direction/"))))
					.andExpect(content().string(not(containsString("/states/" + state + "/leak-and-cleanup/"))))
					.andExpect(content().string(not(containsString("/states/" + state + "/removal-vs-abandonment/"))));

			for (String route : List.of("buyer-seller", "sweep-and-locate", "records-and-proof")) {
				mockMvc.perform(get("/states/" + state + "/" + route + "/"))
						.andExpect(status().isOk())
						.andExpect(content().string(containsString("<link rel=\"canonical\" href=\"http://localhost:8080/states/" + state + "/" + route + "/\"")))
						.andExpect(content().string(containsString("application/ld+json")))
						.andExpect(content().string(containsString("Start here in this state")))
						.andExpect(content().string(containsString("Do this in the next 24 hours")))
						.andExpect(content().string(containsString("Questions to send today")))
						.andExpect(content().string(containsString("Open the file checklist")));
			}

			for (String heldRoute : List.of("removal-vs-abandonment", "leak-and-cleanup", "cost-direction")) {
				mockMvc.perform(get("/states/" + state + "/" + heldRoute + "/"))
						.andExpect(status().isOk())
						.andExpect(content().string(containsString("<meta name=\"robots\" content=\"noindex,follow\">")));
			}
		}

		mockMvc.perform(get("/states/massachusetts/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("<meta name=\"robots\" content=\"noindex,follow\">")))
				.andExpect(content().string(containsString("Public scope stays narrow")))
				.andExpect(content().string(not(containsString("/states/massachusetts/cost-direction/"))));

		mockMvc.perform(get("/states/massachusetts/buyer-seller/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("<meta name=\"robots\" content=\"noindex,follow\">")));

		mockMvc.perform(get("/guides/abandoned-oil-tank-records/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Choose a state route for record and proof guidance.")))
				.andExpect(content().string(containsString("What this guide gives you")))
				.andExpect(content().string(containsString("Before you ask for quotes")))
				.andExpect(content().string(containsString("How to Find Abandoned Oil Tank Records Before Closing")))
				.andExpect(content().string(not(containsString("Massachusetts"))));

		for (String guide : List.of("remove-vs-abandon-oil-tank", "leaking-heating-oil-tank-what-to-do", "oil-tank-removal-cost")) {
			mockMvc.perform(get("/guides/" + guide + "/"))
					.andExpect(status().isOk())
					.andExpect(content().string(not(containsString("<meta name=\"robots\" content=\"noindex,follow\">"))))
					.andExpect(content().string(containsString("Editorial standard")))
					.andExpect(content().string(containsString("application/ld+json")));
		}

		mockMvc.perform(get("/methodology/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("verify, route, then escalate sequence")))
				.andExpect(content().string(containsString("routing review, source review, and a boundary check")));

		mockMvc.perform(get("/contact/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("mailto:shinhyeok22@gmail.com")))
				.andExpect(content().string(containsString("shinhyeok22@gmail.com")));

		mockMvc.perform(get("/states/new-jersey/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Any NJDEP case reference, fund paperwork, or no-further-action language.")));

		mockMvc.perform(get("/states/new-jersey/buyer-seller/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Pull seller disclosure, prior oil-heat conversion records, and any closure paperwork before talking about credits or removal.")));

		mockMvc.perform(get("/sitemap.xml"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("/states/")))
				.andExpect(content().string(containsString("/routes/")))
				.andExpect(content().string(containsString("/guides/")))
				.andExpect(content().string(containsString("/guides/remove-vs-abandon-oil-tank/")))
				.andExpect(content().string(containsString("/guides/leaking-heating-oil-tank-what-to-do/")))
				.andExpect(content().string(containsString("/guides/oil-tank-removal-cost/")))
				.andExpect(content().string(containsString("<lastmod>2026-04-13</lastmod>")))
				.andExpect(content().string(containsString("/states/new-jersey/buyer-seller/")))
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
						.param("notes", "Need next step before closing"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/states/new-jersey/buyer-seller/?lead=success"));

		String leadsCsv = Files.readString(STORAGE_ROOT.resolve("leads").resolve("leads.csv"));
		assertThat(leadsCsv).contains("owner@example.com");
		assertThat(leadsCsv).contains("environmental_cleanup");
		assertThat(leadsCsv).contains("buyer");
		assertThat(leadsCsv).contains("records_missing");
		assertThat(leadsCsv).contains("07030");

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
				.andExpect(content().string(containsString("Freshness review queue")))
				.andExpect(content().string(containsString("/states/new-jersey/buyer-seller/")));

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
				.andExpect(content().string(containsString("\"ctaClicks\" : 1")))
				.andExpect(content().string(containsString("\"staleScopeCount\" : 0")));

		mockMvc.perform(get("/admin/exports/routes.json").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"path\" : \"/states/new-jersey/buyer-seller/\"")))
				.andExpect(content().string(containsString("\"routeFamily\" : \"buyer-seller\"")));

		mockMvc.perform(get("/admin/exports/leads.csv").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("owner@example.com")))
				.andExpect(content().string(containsString("environmental_cleanup")));

		mockMvc.perform(get("/admin/exports/lead-events.csv").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("cta_click")))
				.andExpect(content().string(containsString("lead_submit")));

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

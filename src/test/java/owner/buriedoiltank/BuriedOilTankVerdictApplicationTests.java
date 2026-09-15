package owner.buriedoiltank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
import owner.buriedoiltank.web.ApiRequestProtectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

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

	@Autowired
	private ApiRequestProtectionService apiRequestProtectionService;

	@BeforeEach
	void cleanStorage() throws IOException {
		((java.util.Map<?, ?>) ReflectionTestUtils.getField(apiRequestProtectionService, "requestBuckets")).clear();
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
		assertThat(routeInventoryService.entries()).hasSize(92);
		assertThat(routeInventoryService.indexableEntries()).hasSize(62);
		assertThat(routeInventoryService.entries())
				.filteredOn(entry -> entry.id().startsWith("service:"))
				.extracting(owner.buriedoiltank.data.RouteInventoryEntry::path)
				.containsExactlyInAnyOrder("/", "/how-it-works/", "/record-research/", "/sample-brief/", "/tools/");
		assertThat(routeInventoryService.entries())
				.filteredOn(entry -> "records-and-proof".equals(entry.routeFamilySlug()))
				.extracting(owner.buriedoiltank.data.RouteInventoryEntry::partnerType)
				.containsOnly(owner.buriedoiltank.data.PartnerType.RECORD_RESEARCH);
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
				.andExpect(header().string("Content-Security-Policy", containsString("https://static.cloudflareinsights.com")))
				.andExpect(header().string("Content-Security-Policy", containsString("object-src 'none'")))
				.andExpect(header().string("Content-Security-Policy", containsString("frame-ancestors 'none'")))
				.andExpect(header().string("Referrer-Policy", "same-origin"))
				.andExpect(content().string(not(containsString("document.documentElement.classList"))))
				.andExpect(content().string(not(containsString("window.dataLayer ="))));
	}

	@Test
	void propertyResearchIntakeRequiresCaseFieldsAndStoresTheOperationalBriefInput() throws Exception {
		mockMvc.perform(post("/api/leads/capture")
						.contentType("application/x-www-form-urlencoded")
						.header("Origin", "http://localhost:8080")
						.param("pageId", "service:home")
						.param("pagePath", "/")
						.param("routeFamily", "record-research")
						.param("scenario", "records_first")
						.param("partnerType", "record_research")
						.param("stateSlug", "new-jersey")
						.param("userRole", "buyer")
						.param("tankStatus", "unknown")
						.param("email", "buyer@example.com"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/?lead=error"));

		mockMvc.perform(post("/api/leads/capture")
						.contentType("application/x-www-form-urlencoded")
						.header("Origin", "http://localhost:8080")
						.param("pageId", "service:home")
						.param("pagePath", "/")
						.param("routeFamily", "record-research")
						.param("scenario", "records_first")
						.param("partnerType", "record_research")
						.param("submissionToken", "research-case-1")
						.param("propertyAddress", "197 N Fullerton Ave")
						.param("stateSlug", "new-jersey")
						.param("countyMunicipality", "Montclair, Essex County")
						.param("userRole", "buyer")
						.param("tankStatus", "unknown")
						.param("primaryQuestion", "interpret_documents")
						.param("deadline", "2026-09-30")
						.param("hasDocuments", "yes")
						.param("email", "buyer@example.com")
						.param("notes", "Need the agency file explained"))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", matchesPattern("/\\?lead=success&receipt=OTR-[A-F0-9]{8}")));

		String leadsCsv = Files.readString(STORAGE_ROOT.resolve("leads").resolve("leads.csv"));
		assertThat(leadsCsv).contains("property_address,county_municipality,primary_question,deadline,has_documents,submission_token");
		assertThat(leadsCsv).contains("197 N Fullerton Ave");
		assertThat(leadsCsv).contains("Montclair, Essex County");
		assertThat(leadsCsv).contains("interpret_documents");
		assertThat(leadsCsv).contains("2026-09-30");
		assertThat(leadsCsv).contains("record_research");
		String eventsCsv = Files.readString(STORAGE_ROOT.resolve("leads").resolve("lead_events.csv"));
		assertThat(eventsCsv).contains("research_form_submit_success");
		assertThat(eventsCsv).contains("document_interpretation_request");
		assertThat(eventsCsv).contains("qualified_case");
		assertThat(Files.readString(STORAGE_ROOT.resolve("leads").resolve("notification-attempts.csv")))
				.contains("disabled,notifications_disabled");
		assertThat(Files.readString(STORAGE_ROOT.resolve("leads").resolve("customer-receipt-attempts.csv")))
				.contains("disabled,notifications_disabled");
	}

	@Test
	void propertyResearchIntakeAcceptsSameOriginRefererWhenBrowserOriginIsNull() throws Exception {
		mockMvc.perform(post("/api/leads/capture")
					.contentType("application/x-www-form-urlencoded")
					.header("Origin", "null")
					.header("Referer", "http://localhost:8080/sample-brief/")
					.param("pageId", "service:sample-brief")
					.param("pagePath", "/sample-brief/")
					.param("routeFamily", "record-research")
					.param("scenario", "records_first")
					.param("partnerType", "record_research")
					.param("propertyAddress", "197 N Fullerton Ave")
					.param("stateSlug", "new-jersey")
					.param("countyMunicipality", "Montclair, Essex County")
					.param("userRole", "buyer")
					.param("tankStatus", "documents_conflict")
					.param("primaryQuestion", "interpret_documents")
					.param("deadline", "2026-09-30")
					.param("hasDocuments", "yes")
					.param("email", "buyer.qa@example.com"))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", matchesPattern("/sample-brief/\\?lead=success&receipt=OTR-[A-F0-9]{8}")));
	}

	@Test
	void duplicateResearchSubmissionTokenStoresAndCountsOnlyOneSuccessfulCase() throws Exception {
		for (int attempt = 0; attempt < 2; attempt++) {
			mockMvc.perform(post("/api/leads/capture")
						.contentType("application/x-www-form-urlencoded")
						.header("Origin", "http://localhost:8080")
						.param("pageId", "service:record-research")
						.param("pagePath", "/record-research/")
						.param("routeFamily", "record-research")
						.param("scenario", "records_first")
						.param("partnerType", "record_research")
						.param("submissionToken", "same-browser-submission")
						.param("propertyAddress", "12 Duplicate Lane")
						.param("stateSlug", "new-york")
						.param("countyMunicipality", "Albany County")
						.param("userRole", "owner")
						.param("tankStatus", "unknown")
						.param("primaryQuestion", "find_records")
						.param("hasDocuments", "no")
						.param("email", "duplicate@example.com"))
					.andExpect(status().is3xxRedirection())
					.andExpect(header().string("Location", matchesPattern("/record-research/\\?lead=success&receipt=OTR-[A-F0-9]{8}")));
		}

		assertThat(Files.readAllLines(STORAGE_ROOT.resolve("leads").resolve("leads.csv"))).hasSize(2);
		List<String> events = Files.readAllLines(STORAGE_ROOT.resolve("leads").resolve("lead_events.csv"));
		assertThat(events.stream().filter(line -> line.contains("research_form_submit_success")).count()).isEqualTo(1);
		assertThat(events.stream().filter(line -> line.contains("qualified_case")).count()).isEqualTo(1);
		assertThat(Files.readAllLines(STORAGE_ROOT.resolve("leads").resolve("notification-attempts.csv"))).hasSize(3);
		assertThat(Files.readAllLines(STORAGE_ROOT.resolve("leads").resolve("customer-receipt-attempts.csv"))).hasSize(3);
	}

	@Test
	void publicEventEndpointRejectsServerConfirmedSuccessEventsAndSanitizesReferrers() throws Exception {
		mockMvc.perform(post("/api/leads/event")
					.contentType("application/x-www-form-urlencoded")
					.header("Origin", "http://localhost:8080")
					.param("eventType", "research_form_submit_success")
					.param("pageId", "service:home")
					.param("pagePath", "/")
					.param("scenario", "records_first")
					.param("partnerType", "record_research"))
				.andExpect(status().isBadRequest());

		mockMvc.perform(post("/api/leads/event")
					.contentType("application/x-www-form-urlencoded")
					.header("Origin", "http://localhost:8080")
					.param("eventType", "research_form_submit_attempt")
					.param("pageId", "service:home")
					.param("pagePath", "/")
					.param("scenario", "records_first")
					.param("partnerType", "record_research")
					.param("referrer", "https://search.example/results?q=197+N+Fullerton&email=private@example.com"))
				.andExpect(status().isAccepted());

		String eventsCsv = Files.readString(STORAGE_ROOT.resolve("leads").resolve("lead_events.csv"));
		assertThat(eventsCsv).contains("https://search.example");
		assertThat(eventsCsv).doesNotContain("197+N+Fullerton", "private@example.com", "research_form_submit_success");
	}

	@Test
	void adminShowsFullResearchCaseAndSupportsOperationalStatusAndNotificationRetry() throws Exception {
		mockMvc.perform(post("/api/leads/capture")
					.contentType("application/x-www-form-urlencoded")
					.header("Origin", "http://localhost:8080")
					.param("pageId", "service:sample-brief")
					.param("pagePath", "/sample-brief/")
					.param("routeFamily", "record-research")
					.param("scenario", "records_first")
					.param("partnerType", "record_research")
					.param("submissionToken", "admin-case")
					.param("propertyAddress", "197 N Fullerton Ave")
					.param("stateSlug", "new-jersey")
					.param("countyMunicipality", "Montclair, Essex County")
					.param("userRole", "buyer")
					.param("tankStatus", "documents_conflict")
					.param("primaryQuestion", "interpret_documents")
					.param("deadline", "2026-09-30")
					.param("hasDocuments", "yes")
					.param("email", "case@example.com")
					.param("notes", "Need the agency file explained"))
				.andExpect(status().is3xxRedirection());

		String leadId = Files.readAllLines(STORAGE_ROOT.resolve("leads").resolve("leads.csv"))
				.get(1).split(",", 2)[0];
		mockMvc.perform(post("/admin/cases/status")
					.with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD))
					.with(csrf())
					.param("leadId", leadId)
					.param("status", "researching")
					.param("notes", "Searching municipal portal"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/?case=saved#case-desk"));

		mockMvc.perform(post("/admin/cases/notification/retry")
					.with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD))
					.with(csrf())
					.param("leadId", leadId))
				.andExpect(status().is3xxRedirection());

		mockMvc.perform(get("/admin/").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("197 N Fullerton Ave")))
				.andExpect(content().string(containsString("Montclair, Essex County")))
				.andExpect(content().string(containsString("documents_conflict")))
				.andExpect(content().string(containsString("interpret_documents")))
				.andExpect(content().string(containsString("2026-09-30")))
				.andExpect(content().string(containsString("case@example.com")))
				.andExpect(content().string(containsString("Need the agency file explained")))
				.andExpect(content().string(containsString("researching")))
				.andExpect(content().string(containsString("mail: disabled")));

		mockMvc.perform(get("/admin/exports/case-status.csv").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("researching")));
		mockMvc.perform(get("/admin/exports/notification-attempts.csv").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("disabled,notifications_disabled")));
		mockMvc.perform(get("/admin/exports/admin-metrics-snapshot.json").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"successfulSubmissions\" : 1")))
				.andExpect(content().string(containsString("\"researching\" : 1")))
				.andExpect(content().string(containsString("\"interpret_documents\" : 1")));
	}

	@Test
	void multipartIntakeKeepsDocumentAndActivityPrivateBehindAdminAuthentication() throws Exception {
		MockMultipartFile document = new MockMultipartFile(
				"documents", "closure-letter.pdf", "application/pdf", "%PDF-1.7 private evidence".getBytes());
		mockMvc.perform(multipart("/api/leads/capture")
					.file(document)
					.header("Origin", "http://localhost:8080")
					.param("pageId", "service:sample-brief")
					.param("pagePath", "/sample-brief/")
					.param("routeFamily", "record-research")
					.param("scenario", "records_first")
					.param("partnerType", "record_research")
					.param("submissionToken", "document-case")
					.param("propertyAddress", "197 N Fullerton Ave")
					.param("stateSlug", "new-jersey")
					.param("countyMunicipality", "Montclair, Essex County")
					.param("userRole", "buyer")
					.param("tankStatus", "documents_conflict")
					.param("primaryQuestion", "interpret_documents")
					.param("hasDocuments", "yes")
					.param("email", "documents@example.com"))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", matchesPattern("/sample-brief/\\?lead=success&receipt=OTR-[A-F0-9]{8}")));

		String[] registerRow = Files.readAllLines(STORAGE_ROOT.resolve("operations").resolve("case-documents.csv"))
				.get(1).split(",");
		String documentId = registerRow[1];
		String leadId = registerRow[2];
		String downloadPath = "/admin/cases/" + leadId + "/documents/" + documentId;
		mockMvc.perform(get(downloadPath)).andExpect(status().isUnauthorized());
		mockMvc.perform(get(downloadPath).with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(header().string("Content-Disposition", containsString("closure-letter.pdf")))
				.andExpect(content().bytes("%PDF-1.7 private evidence".getBytes()));

		mockMvc.perform(get("/admin/").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("closure-letter.pdf")))
				.andExpect(content().string(containsString("Case activity")))
				.andExpect(content().string(containsString("document-received")))
				.andExpect(content().string(containsString("mail: disabled")));
	}

	@Test
	void privacyExplainsPrivateUploadsAnalyticsExclusionAndOperationalDeletion() throws Exception {
		mockMvc.perform(get("/privacy/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("actual uploaded PDF")))
				.andExpect(content().string(containsString("original filename")))
				.andExpect(content().string(containsString("excluded from GA4")))
				.andExpect(content().string(containsString("request deletion")))
				.andExpect(content().string(containsString("does not provide")));
	}

	@Test
	void serviceAnalyticsEventsAcceptOnlyNonPiiCaseContext() throws Exception {
		for (String eventType : List.of(
				"service_cta_view",
				"service_cta_click",
				"research_form_start",
				"research_form_submit_attempt"
		)) {
			mockMvc.perform(post("/api/leads/event")
							.contentType("application/x-www-form-urlencoded")
							.header("Origin", "http://localhost:8080")
							.param("eventType", eventType)
							.param("pageId", "service:home")
							.param("pagePath", "/")
							.param("stateSlug", "new-jersey")
							.param("routeFamily", "record-research")
							.param("scenario", "records_first")
							.param("partnerType", "record_research")
							.param("element", "hero"))
					.andExpect(status().isAccepted());
		}

		String eventsCsv = Files.readString(STORAGE_ROOT.resolve("leads").resolve("lead_events.csv"));
		assertThat(eventsCsv).contains("service_cta_view");
		assertThat(eventsCsv).contains("research_form_submit_attempt");
		assertThat(eventsCsv).doesNotContain("research_form_submit_success");
		assertThat(eventsCsv).doesNotContain("qualified_case");
		assertThat(eventsCsv).doesNotContain("197 N Fullerton");
		assertThat(eventsCsv).doesNotContain("buyer@example.com");
	}

	@Test
	void knownPublicPathsRedirectToTheirTrailingSlashCanonical() throws Exception {
		mockMvc.perform(get("/heating-oil-tank").queryParam("source", "test"))
				.andExpect(status().isMovedPermanently())
				.andExpect(header().string("Location", "/heating-oil-tank/?source=test"));

		mockMvc.perform(get("/record-research"))
				.andExpect(status().isMovedPermanently())
				.andExpect(header().string("Location", "/record-research/"));

		mockMvc.perform(get("/this-route-does-not-exist"))
				.andExpect(status().isNotFound());
	}

	@Test
	void launchRoutesRenderWithMetadataAndSitemapRules() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Oil Tank Route")))
				.andExpect(content().string(containsString("Researched for you.")))
				.andExpect(content().string(containsString("Start a property record check")))
				.andExpect(content().string(containsString("Property address")))
				.andExpect(content().string(containsString("The result, made useful")))
				.andExpect(content().string(containsString("enctype=\"multipart/form-data\"")))
				.andExpect(content().string(not(containsString("A single data model. Twenty useful routes."))))
				.andExpect(content().string(containsString("property=\"og:image\"")))
				.andExpect(content().string(containsString("application/ld+json")))
				.andExpect(content().string(containsString("href=\"/states/\"")))
				.andExpect(content().string(containsString("href=\"/tools/\"")))
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
					.andExpect(content().string(containsString("Start a property record check")))
					.andExpect(content().string(not(containsString("images.unsplash.com"))));
		}

		mockMvc.perform(get("/tools/"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("Residential heating-oil tools")))
				.andExpect(content().string(containsString("A single data model. Twenty useful routes.")))
				.andExpect(content().string(containsString("<link rel=\"canonical\" href=\"http://localhost:8080/tools/\"")));

		for (String path : List.of("/how-it-works/", "/record-research/", "/sample-brief/")) {
			mockMvc.perform(get(path))
					.andExpect(status().isOk())
					.andExpect(content().string(containsString("<link rel=\"canonical\" href=\"http://localhost:8080" + path + "\"")))
					.andExpect(content().string(containsString("Start a property record check")))
					.andExpect(content().string(containsString("data-research-form")));
		}

		mockMvc.perform(get("/sample-brief/"))
				.andExpect(content().string(containsString("REP-NJ-001")))
				.andExpect(content().string(containsString("Evidence review")))
				.andExpect(content().string(containsString("does not establish removal method, soil condition, or present tank absence")))
				.andExpect(content().string(containsString("Fictional composite")));

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
						.andExpect(content().string(containsString("Property-specific research")))
						.andExpect(content().string(containsString("Identifiers to keep")))
						.andExpect(content().string(containsString("data-research-form")));
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
				.andExpect(content().string(containsString("We cross-check assessor, GIS, permit, environmental, conversion, and indexed document sources")))
				.andExpect(content().string(containsString("The brief states confirmed facts, unresolved gaps, conflicts, plain-English meaning, and prioritized next actions.")));

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
				.andExpect(content().string(containsString("/how-it-works/")))
				.andExpect(content().string(containsString("/record-research/")))
				.andExpect(content().string(containsString("/sample-brief/")))
				.andExpect(content().string(containsString("/tools/")))
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
				.andExpect(content().string(containsString("Property research case desk")))
				.andExpect(content().string(containsString("Successful submissions (28d)")))
				.andExpect(content().string(containsString("Service funnel / 28 days")))
				.andExpect(content().string(containsString("Utility funnel / 28 days")))
				.andExpect(content().string(not(containsString("Review approval and payout"))))
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
				.andExpect(content().string(containsString("\"successfulSubmissions\" : 0")))
				.andExpect(content().string(containsString("\"toolId\" : \"replacement-planner\"")))
				.andExpect(content().string(containsString("\"staleScopeCount\"")));

		mockMvc.perform(get("/admin/exports/routes.json").with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("\"path\" : \"/states/new-jersey/buyer-seller/\"")))
				.andExpect(content().string(containsString("\"routeFamily\" : \"buyer-seller\"")))
				.andExpect(content().string(containsString("\"pageType\" : \"SERVICE\"")))
				.andExpect(content().string(containsString("\"path\" : \"/sample-brief/\"")))
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

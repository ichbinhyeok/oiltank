package owner.buriedoiltank.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import owner.buriedoiltank.config.RecordResearchNotificationProperties;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.leads.CaseActivityService;
import owner.buriedoiltank.leads.DocumentStorageService;
import owner.buriedoiltank.leads.CustomerReceiptService;
import owner.buriedoiltank.leads.EventLogService;
import owner.buriedoiltank.leads.LeadCaptureRequest;
import owner.buriedoiltank.leads.LeadService;
import owner.buriedoiltank.leads.RecordResearchNotificationService;
import owner.buriedoiltank.leads.ResearchMailGateway;
import owner.buriedoiltank.ops.CsvStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.multipart.MultipartFile;

class LeadApiControllerConcurrencyTests {
    @Test
    void retryAfterPostSaveActivityFailureRepairsActivityAndQueuesMissingNotification(@TempDir Path tempDir) {
        Fixture fixture = fixture(tempDir, true);
        MockMultipartFile document = pdf();

        assertThat(submit(fixture.controller, request("retry-token"), document)).isEqualTo("redirect:/?lead=error");
        assertThat(fixture.leadService.leads()).hasSize(1);
        assertThat(fixture.documentStorageService.forLead(fixture.leadService.leads().getFirst().get("lead_id"))).hasSize(1);
        assertThat(fixture.gateway.sendCount.get()).isZero();
        assertThat(fixture.notificationService.latestByLeadId()).isEmpty();

        assertThat(submit(fixture.controller, request("retry-token"), document))
                .startsWith("redirect:/?lead=success&receipt=OTR-");
        String leadId = fixture.leadService.leads().getFirst().get("lead_id");
        assertThat(fixture.leadService.leads()).hasSize(1);
        assertThat(fixture.documentStorageService.forLead(leadId)).hasSize(1);
        assertThat(fixture.caseActivityService.forLead(leadId))
                .extracting(row -> row.get("activity_type"))
                .containsExactlyInAnyOrder("intake", "document-received", "customer-update");
        assertThat(fixture.gateway.sendCount.get()).isEqualTo(2);
        assertThat(fixture.notificationService.latestByLeadId().get(leadId)).containsEntry("status", "sent");
    }

    @Test
    void concurrentDuplicateSubmissionsStoreAndNotifyExactlyOnce(@TempDir Path tempDir) throws Exception {
        Fixture fixture = fixture(tempDir, false);
        CountDownLatch start = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            Future<String> first = executor.submit(() -> {
                start.await();
                return submit(fixture.controller, request("concurrent-token"), pdf());
            });
            Future<String> second = executor.submit(() -> {
                start.await();
                return submit(fixture.controller, request("concurrent-token"), pdf());
            });
            start.countDown();

            assertThat(List.of(first.get(), second.get()))
                    .allSatisfy(result -> assertThat(result).startsWith("redirect:/?lead=success&receipt=OTR-"));
        }

        String leadId = fixture.leadService.leads().getFirst().get("lead_id");
        assertThat(fixture.leadService.leads()).hasSize(1);
        assertThat(fixture.documentStorageService.forLead(leadId)).hasSize(1);
        assertThat(fixture.caseActivityService.forLead(leadId)).hasSize(3);
        assertThat(fixture.gateway.sendCount.get()).isEqualTo(2);
        assertThat(fixture.notificationService.latestByLeadId().get(leadId)).containsEntry("status", "sent");
    }

    private static String submit(LeadApiController controller, LeadCaptureRequest request, MultipartFile document) {
        MockHttpServletRequest servletRequest = new MockHttpServletRequest("POST", "/api/leads/capture");
        servletRequest.addHeader("Origin", "http://localhost:8080");
        return controller.captureLead(
                request,
                new BeanPropertyBindingResult(request, "request"),
                List.of(document),
                servletRequest
        );
    }

    private static LeadCaptureRequest request(String submissionToken) {
        LeadCaptureRequest request = new LeadCaptureRequest();
        request.setPageId("service:home");
        request.setPagePath("/");
        request.setRouteFamily("record-research");
        request.setScenario("records_first");
        request.setPartnerType("record_research");
        request.setSubmissionToken(submissionToken);
        request.setPropertyAddress("197 N Fullerton Ave");
        request.setStateSlug("new-jersey");
        request.setCountyMunicipality("Montclair, Essex County");
        request.setUserRole("buyer");
        request.setTankStatus("documents_conflict");
        request.setPrimaryQuestion("interpret_documents");
        request.setDeadline("2026-09-30");
        request.setHasDocuments("yes");
        request.setEmail("buyer@example.test");
        request.setNotes("Need the file explained");
        return request;
    }

    private static MockMultipartFile pdf() {
        return new MockMultipartFile(
                "documents", "closure-letter.pdf", "application/pdf", "%PDF-1.7 test evidence".getBytes());
    }

    private static Fixture fixture(Path tempDir, boolean failFirstActivity) {
        SiteProperties site = new SiteProperties();
        site.setStorageRoot(tempDir);
        site.setBaseUrl(URI.create("http://localhost:8080"));
        Clock clock = Clock.fixed(Instant.parse("2026-09-15T00:00:00Z"), ZoneOffset.UTC);
        CsvStore csvStore = new CsvStore();
        EventLogService events = new EventLogService(site, csvStore, clock, event -> { });
        LeadService leads = new LeadService(site, csvStore, clock, events);
        DocumentStorageService documents = new DocumentStorageService(site, csvStore, clock);
        CaseActivityService activities = failFirstActivity
                ? new FailOnceCaseActivityService(site, csvStore, clock)
                : new CaseActivityService(site, csvStore, clock, event -> { });
        RecordResearchNotificationProperties mail = new RecordResearchNotificationProperties();
        mail.setEnabled(true);
        mail.setRecipient("operator@example.test");
        mail.setSender("intake@example.test");
        mail.setSmtpHost("smtp.gmail.com");
        mail.setSmtpUsername("operator@gmail.com");
        mail.setSmtpPassword("app-password");
        CountingGateway gateway = new CountingGateway();
        RecordResearchNotificationService notifications = new RecordResearchNotificationService(
                site, csvStore, clock, mail, gateway, new SyncTaskExecutor(), documents);
        CustomerReceiptService customerReceipts = new CustomerReceiptService(
                site, csvStore, clock, mail, gateway, new SyncTaskExecutor(), activities);
        LeadApiController controller = new LeadApiController(
                leads, events, new ApiRequestProtectionService(site, clock), notifications, documents, activities,
                customerReceipts);
        return new Fixture(controller, leads, documents, activities, notifications, gateway);
    }

    private record Fixture(
            LeadApiController controller,
            LeadService leadService,
            DocumentStorageService documentStorageService,
            CaseActivityService caseActivityService,
            RecordResearchNotificationService notificationService,
            CountingGateway gateway
    ) { }

    private static final class CountingGateway implements ResearchMailGateway {
        private final AtomicInteger sendCount = new AtomicInteger();

        @Override
        public void send(String recipient, String sender, String subject, String body) {
            sendCount.incrementAndGet();
        }
    }

    private static final class FailOnceCaseActivityService extends CaseActivityService {
        private final AtomicBoolean first = new AtomicBoolean(true);

        private FailOnceCaseActivityService(SiteProperties site, CsvStore csvStore, Clock clock) {
            super(site, csvStore, clock, event -> { });
        }

        @Override
        public synchronized boolean recordIfAbsent(String leadId, String activityType, String sourceId,
                                                   String routeId, String agency, String channel, String outcome,
                                                   String notes, String nextAction, String checkDate) {
            if (first.compareAndSet(true, false)) throw new IllegalStateException("simulated activity write failure");
            return super.recordIfAbsent(leadId, activityType, sourceId, routeId, agency, channel, outcome,
                    notes, nextAction, checkDate);
        }
    }
}

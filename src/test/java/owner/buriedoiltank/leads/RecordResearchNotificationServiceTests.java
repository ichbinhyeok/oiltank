package owner.buriedoiltank.leads;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import owner.buriedoiltank.config.RecordResearchNotificationProperties;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.ops.CsvStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.task.SyncTaskExecutor;

class RecordResearchNotificationServiceTests {
    @Test
    void restartRequiresOperatorReviewBeforeRetryingUnknownDelivery(@TempDir Path tempDir) {
        CapturingGateway gateway = new CapturingGateway(false);
        service(tempDir, gateway);
        new CsvStore().append(tempDir.resolve("leads/notification-attempts.csv"),
                java.util.List.of("timestamp", "lead_id", "status", "error_code"),
                java.util.List.of("2026-09-14T00:00:00Z", "case-1", "queued", ""));
        RecordResearchNotificationService restarted = service(tempDir, gateway);
        assertThat(restarted.latestByLeadId().get("case-1"))
                .containsEntry("status", "interrupted")
                .containsEntry("error_code", "delivery_unknown_after_restart");
        assertThat(gateway.sendCount.get()).isZero();
        assertThat(restarted.queueIfNeeded(researchCase())).isFalse();
        assertThat(restarted.retryAfterOperatorReview(researchCase())).isTrue();
        assertThat(restarted.retryAfterOperatorReview(researchCase())).isFalse();
        assertThat(gateway.sendCount.get()).isEqualTo(1);
    }

    @Test
    void sendsTheFullCaseToTheConfiguredOperatorAndTracksSuccess(@TempDir Path tempDir) {
        CapturingGateway gateway = new CapturingGateway(false);
        RecordResearchNotificationService service = service(tempDir, gateway);

        service.queue(researchCase());
        service.queue(researchCase());

        assertThat(gateway.recipient).isEqualTo("operator@example.test");
        assertThat(gateway.sendCount.get()).isEqualTo(1);
        assertThat(gateway.body).contains("197 N Fullerton Ave", "buyer@example.test", "Need the file explained");
        assertThat(service.latestByLeadId().get("case-1"))
                .containsEntry("status", "sent")
                .containsEntry("error_code", "");
    }

    @Test
    void recordsDeliveryFailureWithoutDiscardingTheCaseAndCanRetry(@TempDir Path tempDir) {
        CapturingGateway gateway = new CapturingGateway(true);
        RecordResearchNotificationService service = service(tempDir, gateway);
        Map<String, String> savedCase = researchCase();

        service.queue(savedCase);
        assertThat(savedCase).containsEntry("lead_id", "case-1");
        assertThat(service.latestByLeadId().get("case-1"))
                .containsEntry("status", "failed")
                .containsEntry("error_code", "smtp_delivery_failed");

        gateway.fail = false;
        service.queue(savedCase);
        assertThat(service.latestByLeadId().get("case-1")).containsEntry("status", "sent");
        assertThat(service.attemptsCsv()).contains("failed,smtp_delivery_failed").contains("sent,");
    }

    @Test
    void disabledStateIsRetryableAfterConfigurationIsEnabled(@TempDir Path tempDir) {
        CapturingGateway gateway = new CapturingGateway(false);
        RecordResearchNotificationProperties mail = configuredMail();
        mail.setEnabled(false);
        RecordResearchNotificationService service = service(tempDir, gateway, mail);

        assertThat(service.queueIfNeeded(researchCase())).isTrue();
        assertThat(service.latestByLeadId().get("case-1")).containsEntry("status", "disabled");
        mail.setEnabled(true);
        assertThat(service.queueIfNeeded(researchCase())).isTrue();
        assertThat(service.latestByLeadId().get("case-1")).containsEntry("status", "sent");
        assertThat(gateway.sendCount.get()).isEqualTo(1);
    }

    private static RecordResearchNotificationService service(Path tempDir, ResearchMailGateway gateway) {
        return service(tempDir, gateway, configuredMail());
    }

    private static RecordResearchNotificationService service(
            Path tempDir, ResearchMailGateway gateway, RecordResearchNotificationProperties mail) {
        SiteProperties site = new SiteProperties();
        site.setStorageRoot(tempDir);
        return new RecordResearchNotificationService(
                site, new CsvStore(), Clock.fixed(Instant.parse("2026-09-14T00:00:00Z"), ZoneOffset.UTC),
                mail, gateway, new SyncTaskExecutor(),
                new DocumentStorageService(site, new CsvStore(),
                        Clock.fixed(Instant.parse("2026-09-14T00:00:00Z"), ZoneOffset.UTC))
        );
    }

    private static RecordResearchNotificationProperties configuredMail() {
        RecordResearchNotificationProperties mail = new RecordResearchNotificationProperties();
        mail.setEnabled(true);
        mail.setRecipient("operator@example.test");
        mail.setSender("intake@example.test");
        mail.setSmtpHost("smtp.example.test");
        mail.setSmtpUsername("smtp-user");
        mail.setSmtpPassword("app-password");
        return mail;
    }

    private static Map<String, String> researchCase() {
        Map<String, String> row = new LinkedHashMap<>();
        row.put("lead_id", "case-1");
        row.put("timestamp", "2026-09-14T00:00:00Z");
        row.put("route_family", "record-research");
        row.put("property_address", "197 N Fullerton Ave");
        row.put("state_slug", "new-jersey");
        row.put("county_municipality", "Montclair, Essex County");
        row.put("user_role", "buyer");
        row.put("tank_status", "documents_conflict");
        row.put("primary_question", "interpret_documents");
        row.put("deadline", "2026-09-30");
        row.put("has_documents", "yes");
        row.put("email", "buyer@example.test");
        row.put("notes", "Need the file explained");
        return row;
    }

    private static final class CapturingGateway implements ResearchMailGateway {
        private boolean fail;
        private String recipient;
        private String body;
        private final AtomicInteger sendCount = new AtomicInteger();

        private CapturingGateway(boolean fail) { this.fail = fail; }

        @Override
        public void send(String recipient, String sender, String subject, String body) {
            if (fail) throw new IllegalStateException("simulated delivery failure");
            sendCount.incrementAndGet();
            this.recipient = recipient;
            this.body = body;
        }
    }
}

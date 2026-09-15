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

class CustomerReceiptServiceTests {
    @Test
    void restartRequiresOperatorReviewBeforeRetryingUnknownDelivery(@TempDir Path tempDir) {
        Fixture original = fixture(tempDir, false);
        new CsvStore().append(tempDir.resolve("leads/customer-receipt-attempts.csv"),
                java.util.List.of("timestamp", "lead_id", "status", "error_code"),
                java.util.List.of("2026-09-15T00:00:00Z", "case-1", "queued", ""));
        Fixture restarted = fixture(tempDir, false);
        assertThat(restarted.service.latestByLeadId().get("case-1"))
                .containsEntry("status", "interrupted")
                .containsEntry("error_code", "delivery_unknown_after_restart");
        assertThat(restarted.gateway.sendCount.get()).isZero();
        assertThat(restarted.service.queueIfNeeded(researchCase())).isFalse();
        assertThat(restarted.service.retryAfterOperatorReview(researchCase())).isTrue();
        assertThat(restarted.service.retryAfterOperatorReview(researchCase())).isFalse();
        assertThat(restarted.gateway.sendCount.get()).isEqualTo(1);
    }

    @Test
    void sendsOnePrivacyLimitedReceiptAndRecordsTheCustomerUpdate(@TempDir Path tempDir) {
        Fixture fixture = fixture(tempDir, false);

        assertThat(fixture.service.queueIfNeeded(researchCase())).isTrue();
        assertThat(fixture.service.queueIfNeeded(researchCase())).isFalse();

        assertThat(fixture.gateway.sendCount.get()).isEqualTo(1);
        assertThat(fixture.gateway.recipient).isEqualTo("buyer@example.test");
        assertThat(fixture.gateway.subject).contains("OTR-CASE1");
        assertThat(fixture.gateway.body)
                .contains("initial route review within 2 business days", "Agency response times vary")
                .doesNotContain("197 N Fullerton Ave");
        assertThat(fixture.service.latestByLeadId().get("case-1")).containsEntry("status", "sent");
        assertThat(fixture.activities.forLead("case-1"))
                .singleElement()
                .satisfies(row -> assertThat(row)
                        .containsEntry("activity_type", "customer-update")
                        .containsEntry("outcome", "receipt sent"));
    }

    @Test
    void failedReceiptCanRetryWithoutResendingAfterSuccess(@TempDir Path tempDir) {
        Fixture fixture = fixture(tempDir, true);

        fixture.service.queueIfNeeded(researchCase());
        assertThat(fixture.service.latestByLeadId().get("case-1")).containsEntry("status", "failed");
        assertThat(fixture.activities.forLead("case-1"))
                .extracting(row -> row.get("activity_type"))
                .containsExactly("bounce");

        fixture.gateway.fail = false;
        fixture.service.queueIfNeeded(researchCase());
        fixture.service.queueIfNeeded(researchCase());
        assertThat(fixture.gateway.sendCount.get()).isEqualTo(1);
        assertThat(fixture.service.latestByLeadId().get("case-1")).containsEntry("status", "sent");
        assertThat(fixture.activities.forLead("case-1"))
                .extracting(row -> row.get("activity_type"))
                .containsExactly("bounce", "customer-update");
    }

    private static Fixture fixture(Path tempDir, boolean fail) {
        SiteProperties site = new SiteProperties();
        site.setStorageRoot(tempDir);
        CsvStore csvStore = new CsvStore();
        Clock clock = Clock.fixed(Instant.parse("2026-09-15T00:00:00Z"), ZoneOffset.UTC);
        CaseActivityService activities = new CaseActivityService(site, csvStore, clock, event -> { });
        RecordResearchNotificationProperties mail = new RecordResearchNotificationProperties();
        mail.setEnabled(true);
        mail.setRecipient("operator@example.test");
        mail.setSender("intake@example.test");
        mail.setSmtpHost("smtp.example.test");
        mail.setSmtpUsername("smtp-user");
        mail.setSmtpPassword("app-password");
        CapturingGateway gateway = new CapturingGateway(fail);
        CustomerReceiptService service = new CustomerReceiptService(
                site, csvStore, clock, mail, gateway, new SyncTaskExecutor(), activities);
        return new Fixture(service, activities, gateway);
    }

    private static Map<String, String> researchCase() {
        Map<String, String> row = new LinkedHashMap<>();
        row.put("lead_id", "case-1");
        row.put("timestamp", "2026-09-15T00:00:00Z");
        row.put("route_family", "record-research");
        row.put("property_address", "197 N Fullerton Ave");
        row.put("state_slug", "new-jersey");
        row.put("county_municipality", "Montclair, Essex County");
        row.put("primary_question", "interpret_documents");
        row.put("deadline", "2026-09-30");
        row.put("email", "buyer@example.test");
        return row;
    }

    private record Fixture(
            CustomerReceiptService service,
            CaseActivityService activities,
            CapturingGateway gateway
    ) { }

    private static final class CapturingGateway implements ResearchMailGateway {
        private boolean fail;
        private String recipient;
        private String subject;
        private String body;
        private final AtomicInteger sendCount = new AtomicInteger();

        private CapturingGateway(boolean fail) {
            this.fail = fail;
        }

        @Override
        public void send(String recipient, String sender, String subject, String body) {
            if (fail) throw new IllegalStateException("simulated receipt failure");
            sendCount.incrementAndGet();
            this.recipient = recipient;
            this.subject = subject;
            this.body = body;
        }
    }
}

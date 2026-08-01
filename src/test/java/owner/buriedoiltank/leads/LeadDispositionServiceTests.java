package owner.buriedoiltank.leads;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.ops.CsvStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.context.ApplicationEventPublisher;

class LeadDispositionServiceTests {
    @Test
    void keepsAnAppendOnlyAuditAndUsesLatestDecision(@TempDir Path tempDir) {
        SiteProperties properties = new SiteProperties();
        properties.setBaseUrl(URI.create("https://example.test"));
        properties.setStorageRoot(tempDir);
        ApplicationEventPublisher publisher = event -> { };
        LeadDispositionService service = new LeadDispositionService(
                properties,
                new CsvStore(),
                Clock.fixed(Instant.parse("2026-08-01T00:00:00Z"), ZoneOffset.UTC),
                publisher
        );

        service.record("lead-1", "approved", null, "Qualified homeowner");
        service.record("lead-1", "rejected", 9000, "Duplicate");

        assertThat(service.decisions()).hasSize(2);
        assertThat(service.latestByLeadId().get("lead-1"))
                .containsEntry("disposition", "rejected")
                .containsEntry("payout_cents", "0")
                .containsEntry("notes", "Duplicate");
        assertThat(service.decisionsCsv()).startsWith("timestamp,lead_id,disposition,payout_cents,notes");
    }
}

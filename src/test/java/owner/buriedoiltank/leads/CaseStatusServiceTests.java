package owner.buriedoiltank.leads;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.ops.CsvStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CaseStatusServiceTests {
    @Test
    void keepsAppendOnlyCaseHistoryAndUsesLatestStatus(@TempDir Path tempDir) {
        SiteProperties properties = new SiteProperties();
        properties.setStorageRoot(tempDir);
        CaseStatusService service = new CaseStatusService(
                properties, new CsvStore(),
                Clock.fixed(Instant.parse("2026-09-14T00:00:00Z"), ZoneOffset.UTC), event -> { }
        );

        service.record("case-1", "researching", "Started portal search");
        service.record("case-1", "agency-pending", "Request confirmation saved");

        assertThat(service.latestByLeadId().get("case-1"))
                .containsEntry("status", "agency-pending")
                .containsEntry("notes", "Request confirmation saved");
        assertThat(service.statusCsv().lines()).hasSize(3);
        assertThatThrownBy(() -> service.record("case-1", "approved", "legacy status"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

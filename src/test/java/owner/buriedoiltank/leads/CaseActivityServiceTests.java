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

class CaseActivityServiceTests {
    @Test
    void keepsCaseLinkedImmutableResearchHistory(@TempDir Path tempDir) {
        SiteProperties properties = new SiteProperties();
        properties.setStorageRoot(tempDir);
        CaseActivityService service = new CaseActivityService(properties, new CsvStore(),
                Clock.fixed(Instant.parse("2026-09-15T00:00:00Z"), ZoneOffset.UTC), event -> { });

        service.record("case-1", "agency-request", "nj-montclair-records", "Montclair Clerk",
                "portal", "acknowledged", "PR-24-0187", "Requested permit and closure file",
                "Check for response", "2026-09-22");
        service.record("case-1", "agency-reply", "nj-montclair-records", "Montclair Clerk",
                "email", "records released", "PR-24-0187", "Permit index received",
                "Interpret final inspection", "");

        assertThat(service.forLead("case-1")).hasSize(2);
        assertThat(service.activityCsv()).contains("agency-request", "agency-reply", "PR-24-0187");
        assertThatThrownBy(() -> service.record("case-1", "made-up", "", "", "", "", "", "", "", ""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void recordIfAbsentRepairsMissingActivityWithoutDuplicatingIt(@TempDir Path tempDir) {
        SiteProperties properties = new SiteProperties();
        properties.setStorageRoot(tempDir);
        CaseActivityService service = new CaseActivityService(properties, new CsvStore(),
                Clock.fixed(Instant.parse("2026-09-15T00:00:00Z"), ZoneOffset.UTC), event -> { });

        assertThat(service.recordIfAbsent("case-1", "intake", "submission:token-1", "", "", "web-form",
                "saved", "Intake saved", "Start research", "")).isTrue();
        assertThat(service.recordIfAbsent("case-1", "intake", "submission:token-1", "", "", "web-form",
                "saved", "Intake saved", "Start research", "")).isFalse();
        assertThat(service.forLead("case-1")).hasSize(1);
    }
}

package owner.buriedoiltank.leads;

import java.nio.file.Path;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.ops.CsvStore;
import owner.buriedoiltank.ops.OpsRefreshRequestedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CaseStatusService {
    private static final List<String> HEADERS = List.of("timestamp", "lead_id", "status", "notes");
    private final Path statusPath;
    private final CsvStore csvStore;
    private final Clock clock;
    private final ApplicationEventPublisher eventPublisher;

    public CaseStatusService(SiteProperties siteProperties, CsvStore csvStore, Clock clock,
                             ApplicationEventPublisher eventPublisher) {
        this.statusPath = siteProperties.getStorageRoot().resolve("leads").resolve("case-status.csv");
        this.csvStore = csvStore;
        this.clock = clock;
        this.eventPublisher = eventPublisher;
        csvStore.ensureFile(statusPath, HEADERS);
    }

    public void record(String leadId, String statusValue, String notes) {
        if (leadId == null || leadId.isBlank()) throw new IllegalArgumentException("Lead ID is required");
        CaseStatus status = CaseStatus.fromSlug(statusValue);
        csvStore.append(statusPath, HEADERS, List.of(
                OffsetDateTime.now(clock).toString(),
                leadId.trim(),
                status.slug(),
                notes == null ? "" : notes
        ));
        eventPublisher.publishEvent(new OpsRefreshRequestedEvent("case status updated"));
    }

    public Map<String, Map<String, String>> latestByLeadId() {
        Map<String, Map<String, String>> latest = new LinkedHashMap<>();
        for (Map<String, String> row : csvStore.readAll(statusPath)) {
            String leadId = row.getOrDefault("lead_id", "");
            if (!leadId.isBlank()) latest.put(leadId, row);
        }
        return latest;
    }

    public String statusCsv() { return csvStore.readRaw(statusPath); }
}

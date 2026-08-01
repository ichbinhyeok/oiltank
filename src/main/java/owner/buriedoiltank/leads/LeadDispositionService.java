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
public class LeadDispositionService {
    private static final List<String> HEADERS = List.of(
            "timestamp", "lead_id", "disposition", "payout_cents", "notes"
    );

    private final Path decisionsPath;
    private final CsvStore csvStore;
    private final Clock clock;
    private final ApplicationEventPublisher eventPublisher;

    public LeadDispositionService(
            SiteProperties siteProperties,
            CsvStore csvStore,
            Clock clock,
            ApplicationEventPublisher eventPublisher
    ) {
        this.decisionsPath = siteProperties.getStorageRoot().resolve("leads").resolve("lead-dispositions.csv");
        this.csvStore = csvStore;
        this.clock = clock;
        this.eventPublisher = eventPublisher;
        csvStore.ensureFile(decisionsPath, HEADERS);
    }

    public void record(String leadId, String dispositionValue, Integer payoutCents, String notes) {
        if (leadId == null || leadId.isBlank()) {
            throw new IllegalArgumentException("Lead ID is required");
        }
        LeadDisposition disposition = LeadDisposition.fromSlug(dispositionValue);
        if (disposition == LeadDisposition.PENDING) {
            payoutCents = 0;
        } else if (disposition == LeadDisposition.REJECTED) {
            payoutCents = 0;
        } else if (payoutCents == null) {
            payoutCents = 2500;
        }
        if (payoutCents < 0 || payoutCents > 100_000) {
            throw new IllegalArgumentException("Payout must be between $0 and $1,000");
        }
        csvStore.append(decisionsPath, HEADERS, List.of(
                OffsetDateTime.now(clock).toString(),
                leadId.trim(),
                disposition.slug(),
                Integer.toString(payoutCents),
                notes == null ? "" : notes
        ));
        eventPublisher.publishEvent(new OpsRefreshRequestedEvent("lead disposition updated"));
    }

    public Map<String, Map<String, String>> latestByLeadId() {
        Map<String, Map<String, String>> latest = new LinkedHashMap<>();
        for (Map<String, String> row : csvStore.readAll(decisionsPath)) {
            String leadId = row.getOrDefault("lead_id", "");
            if (!leadId.isBlank()) {
                latest.put(leadId, row);
            }
        }
        return latest;
    }

    public List<Map<String, String>> decisions() {
        return csvStore.readAll(decisionsPath);
    }

    public String decisionsCsv() {
        return csvStore.readRaw(decisionsPath);
    }
}

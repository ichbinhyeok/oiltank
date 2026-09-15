package owner.buriedoiltank.leads;

import java.nio.file.Path;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.ops.CsvStore;
import owner.buriedoiltank.ops.OpsRefreshRequestedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CaseActivityService {
    private static final List<String> HEADERS = List.of(
            "timestamp", "activity_id", "lead_id", "activity_type", "route_id",
            "agency", "channel", "outcome", "source_id", "notes", "next_action", "check_date"
    );

    private final Path activityPath;
    private final CsvStore csvStore;
    private final Clock clock;
    private final ApplicationEventPublisher eventPublisher;

    public CaseActivityService(SiteProperties siteProperties, CsvStore csvStore, Clock clock,
                               ApplicationEventPublisher eventPublisher) {
        this.activityPath = siteProperties.getStorageRoot().resolve("operations").resolve("case-activity.csv");
        this.csvStore = csvStore;
        this.clock = clock;
        this.eventPublisher = eventPublisher;
        csvStore.ensureFile(activityPath, HEADERS);
    }

    public synchronized void record(String leadId, String activityType, String routeId, String agency, String channel,
                       String outcome, String sourceId, String notes, String nextAction, String checkDate) {
        if (leadId == null || leadId.isBlank()) throw new IllegalArgumentException("Lead ID is required");
        CaseActivityType type = CaseActivityType.fromSlug(activityType);
        csvStore.append(activityPath, HEADERS, List.of(
                OffsetDateTime.now(clock).toString(), UUID.randomUUID().toString(), leadId, type.slug(),
                safe(routeId), safe(agency), safe(channel), safe(outcome), safe(sourceId), safe(notes),
                safe(nextAction), safe(checkDate)
        ));
        eventPublisher.publishEvent(new OpsRefreshRequestedEvent("case activity recorded"));
    }

    public synchronized boolean recordIfAbsent(String leadId, String activityType, String sourceId,
                                               String routeId, String agency, String channel, String outcome,
                                               String notes, String nextAction, String checkDate) {
        CaseActivityType type = CaseActivityType.fromSlug(activityType);
        boolean alreadyRecorded = forLead(leadId).stream()
                .anyMatch(row -> type.slug().equals(row.get("activity_type"))
                        && safe(sourceId).equals(row.get("source_id")));
        if (alreadyRecorded) return false;
        record(leadId, type.slug(), routeId, agency, channel, outcome, sourceId, notes, nextAction, checkDate);
        return true;
    }

    public List<Map<String, String>> forLead(String leadId) {
        return csvStore.readAll(activityPath).stream()
                .filter(row -> leadId.equals(row.get("lead_id")))
                .toList();
    }

    public String activityCsv() { return csvStore.readRaw(activityPath); }

    private static String safe(String value) { return value == null ? "" : value; }
}

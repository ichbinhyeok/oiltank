package owner.buriedoiltank.leads;

import java.nio.file.Path;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import owner.buriedoiltank.config.RecordResearchNotificationProperties;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.ops.CsvStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class RecordResearchNotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordResearchNotificationService.class);
    private static final List<String> HEADERS = List.of("timestamp", "lead_id", "status", "error_code");
    private final Path attemptsPath;
    private final CsvStore csvStore;
    private final Clock clock;
    private final RecordResearchNotificationProperties properties;
    private final ResearchMailGateway mailGateway;
    private final TaskExecutor taskExecutor;
    private final DocumentStorageService documentStorageService;

    public RecordResearchNotificationService(
            SiteProperties siteProperties,
            CsvStore csvStore,
            Clock clock,
            RecordResearchNotificationProperties properties,
            ResearchMailGateway mailGateway,
            @Qualifier("researchNotificationExecutor") TaskExecutor taskExecutor,
            DocumentStorageService documentStorageService
    ) {
        this.attemptsPath = siteProperties.getStorageRoot().resolve("leads").resolve("notification-attempts.csv");
        this.csvStore = csvStore;
        this.clock = clock;
        this.properties = properties;
        this.mailGateway = mailGateway;
        this.taskExecutor = taskExecutor;
        this.documentStorageService = documentStorageService;
        csvStore.ensureFile(attemptsPath, HEADERS);
        // The executor is process-local. Never automatically resend an uncertain delivery.
        latestByLeadId().forEach((leadId, attempt) -> {
            if ("queued".equals(attempt.get("status"))) {
                record(leadId, "interrupted", "delivery_unknown_after_restart");
            }
        });
    }

    public void queue(Map<String, String> researchCase) {
        queueIfNeeded(researchCase);
    }

    public synchronized boolean queueIfNeeded(Map<String, String> researchCase) {
        return queueIfNeeded(researchCase, false);
    }

    public synchronized boolean retryAfterOperatorReview(Map<String, String> researchCase) {
        return queueIfNeeded(researchCase, true);
    }

    private boolean queueIfNeeded(Map<String, String> researchCase, boolean operatorReviewed) {
        String leadId = researchCase.getOrDefault("lead_id", "");
        if (leadId.isBlank() || !"record-research".equals(researchCase.get("route_family"))) return false;
        String latestStatus = latestByLeadId().getOrDefault(leadId, Map.of()).getOrDefault("status", "not-attempted");
        if ("interrupted".equals(latestStatus) && !operatorReviewed) return false;
        if ("queued".equals(latestStatus) || "sent".equals(latestStatus)) {
            return false;
        }
        if (!properties.isEnabled()) {
            record(leadId, "disabled", "notifications_disabled");
            return true;
        }
        String configurationIssue = properties.configurationIssue();
        if (configurationIssue != null) {
            record(leadId, "failed", configurationIssue);
            LOGGER.warn("Record-research notification configuration is incomplete; the saved case remains available in admin");
            return true;
        }
        record(leadId, "queued", "");
        try {
            taskExecutor.execute(() -> dispatch(researchCase));
        } catch (RuntimeException exception) {
            record(leadId, "failed", "queue_rejected");
            LOGGER.warn("Record-research notification could not be queued; the saved case remains available in admin");
        }
        return true;
    }

    void dispatch(Map<String, String> researchCase) {
        String leadId = researchCase.getOrDefault("lead_id", "");
        try {
            mailGateway.send(
                    properties.getRecipient(),
                    properties.getSender(),
                    "New Oil Tank Route research case — " + safe(researchCase.get("state_slug"))
                            + " — " + safe(researchCase.get("primary_question")),
                    messageBody(researchCase, documentStorageService.forLead(leadId).size())
            );
            record(leadId, "sent", "");
        } catch (RuntimeException exception) {
            record(leadId, "failed", "smtp_delivery_failed");
            LOGGER.warn("Record-research notification delivery failed; the saved case remains available in admin");
        }
    }

    public Map<String, Map<String, String>> latestByLeadId() {
        Map<String, Map<String, String>> latest = new LinkedHashMap<>();
        for (Map<String, String> row : csvStore.readAll(attemptsPath)) {
            String leadId = row.getOrDefault("lead_id", "");
            if (!leadId.isBlank()) latest.put(leadId, row);
        }
        return latest;
    }

    public String attemptsCsv() { return csvStore.readRaw(attemptsPath); }

    private void record(String leadId, String status, String errorCode) {
        csvStore.append(attemptsPath, HEADERS, List.of(
                OffsetDateTime.now(clock).toString(), leadId, status, errorCode
        ));
    }

    private static String messageBody(Map<String, String> row, int attachedDocumentCount) {
        return """
                A new property-specific record research case was saved.

                Received: %s
                Case ID: %s
                Property: %s
                State: %s
                County / municipality: %s
                Role: %s
                Tank status: %s
                Primary question: %s
                Deadline: %s
                Documents available: %s
                Secure documents uploaded: %d
                Customer email: %s
                Notes: %s

                Open the protected Oil Tank Route admin dashboard to update case status.
                """.formatted(
                safe(row.get("timestamp")), safe(row.get("lead_id")), safe(row.get("property_address")),
                safe(row.get("state_slug")), safe(row.get("county_municipality")), safe(row.get("user_role")),
                safe(row.get("tank_status")), safe(row.get("primary_question")), safe(row.get("deadline")),
                safe(row.get("has_documents")), attachedDocumentCount, safe(row.get("email")), safe(row.get("notes"))
        );
    }

    private static String safe(String value) {
        return value == null || value.isBlank() ? "Not provided" : value.replace('\r', ' ').replace('\n', ' ').trim();
    }
}

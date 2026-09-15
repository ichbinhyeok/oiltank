package owner.buriedoiltank.leads;

import java.nio.file.Path;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
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
public class CustomerReceiptService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerReceiptService.class);
    private static final List<String> HEADERS = List.of("timestamp", "lead_id", "status", "error_code");

    private final Path attemptsPath;
    private final CsvStore csvStore;
    private final Clock clock;
    private final RecordResearchNotificationProperties properties;
    private final ResearchMailGateway mailGateway;
    private final TaskExecutor taskExecutor;
    private final CaseActivityService caseActivityService;

    public CustomerReceiptService(
            SiteProperties siteProperties,
            CsvStore csvStore,
            Clock clock,
            RecordResearchNotificationProperties properties,
            ResearchMailGateway mailGateway,
            @Qualifier("researchNotificationExecutor") TaskExecutor taskExecutor,
            CaseActivityService caseActivityService
    ) {
        this.attemptsPath = siteProperties.getStorageRoot().resolve("leads").resolve("customer-receipt-attempts.csv");
        this.csvStore = csvStore;
        this.clock = clock;
        this.properties = properties;
        this.mailGateway = mailGateway;
        this.taskExecutor = taskExecutor;
        this.caseActivityService = caseActivityService;
        csvStore.ensureFile(attemptsPath, HEADERS);
        // The executor is process-local. Never automatically resend an uncertain delivery.
        latestByLeadId().forEach((leadId, attempt) -> {
            if ("queued".equals(attempt.get("status"))) {
                record(leadId, "interrupted", "delivery_unknown_after_restart");
            }
        });
    }

    public synchronized boolean queueIfNeeded(Map<String, String> researchCase) {
        return queueIfNeeded(researchCase, false);
    }

    public synchronized boolean retryAfterOperatorReview(Map<String, String> researchCase) {
        return queueIfNeeded(researchCase, true);
    }

    private boolean queueIfNeeded(Map<String, String> researchCase, boolean operatorReviewed) {
        String leadId = researchCase.getOrDefault("lead_id", "");
        String customerEmail = researchCase.getOrDefault("email", "");
        if (leadId.isBlank() || customerEmail.isBlank()
                || !"record-research".equals(researchCase.get("route_family"))) return false;
        String latestStatus = latestByLeadId().getOrDefault(leadId, Map.of()).getOrDefault("status", "not-attempted");
        if ("interrupted".equals(latestStatus) && !operatorReviewed) return false;
        if ("queued".equals(latestStatus) || "sent".equals(latestStatus)) return false;
        if (!properties.isEnabled()) {
            record(leadId, "disabled", "notifications_disabled");
            return true;
        }
        String configurationIssue = properties.configurationIssue();
        if (configurationIssue != null) {
            record(leadId, "failed", configurationIssue);
            LOGGER.warn("Customer receipt configuration is incomplete; the saved case remains available in admin");
            return true;
        }
        record(leadId, "queued", "");
        try {
            taskExecutor.execute(() -> dispatch(researchCase));
        } catch (RuntimeException exception) {
            record(leadId, "failed", "queue_rejected");
            LOGGER.warn("Customer receipt could not be queued; the saved case remains available in admin");
        }
        return true;
    }

    void dispatch(Map<String, String> researchCase) {
        String leadId = researchCase.getOrDefault("lead_id", "");
        try {
            mailGateway.send(
                    researchCase.getOrDefault("email", ""),
                    properties.getSender(),
                    "We received your Oil Tank Route request — " + receiptCode(leadId),
                    receiptBody(researchCase)
            );
        } catch (RuntimeException exception) {
            record(leadId, "failed", "smtp_delivery_failed");
            try {
                caseActivityService.record(
                        leadId, CaseActivityType.BOUNCE.slug(), "", "Oil Tank Route", "email",
                        "customer receipt failed", "customer-receipt:" + OffsetDateTime.now(clock),
                        "Customer intake confirmation could not be delivered",
                        "Verify the customer email and retry the receipt", ""
                );
            } catch (RuntimeException activityException) {
                LOGGER.warn("Customer receipt failure could not be added to case activity");
            }
            LOGGER.warn("Customer receipt delivery failed; the saved case remains available in admin");
            return;
        }

        record(leadId, "sent", "");
        try {
            caseActivityService.recordIfAbsent(
                    leadId, CaseActivityType.CUSTOMER_UPDATE.slug(), "customer-receipt:" + leadId,
                    "", "Oil Tank Route", "email", "receipt sent",
                    "Customer intake confirmation sent", "Begin initial route review", ""
            );
        } catch (RuntimeException activityException) {
            LOGGER.warn("Sent customer receipt could not be added to case activity");
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

    public String attemptsCsv() {
        return csvStore.readRaw(attemptsPath);
    }

    public static String receiptCode(String leadId) {
        String compact = leadId == null ? "" : leadId.replaceAll("[^A-Za-z0-9]", "");
        if (compact.isBlank()) return "OTR-PENDING";
        return "OTR-" + compact.substring(0, Math.min(8, compact.length())).toUpperCase(Locale.ROOT);
    }

    private String receiptBody(Map<String, String> row) {
        return """
                We received your property research request.

                Receipt: %s
                Received: %s
                State: %s
                County / municipality: %s
                Primary question: %s
                Deadline supplied: %s

                What happens next
                - We confirm the parcel and jurisdiction.
                - We review the relevant public-record sources and any uploaded documents.
                - We identify missing-record routes and explain confirmed findings, open gaps, and next actions.

                Beta target: initial route review within 2 business days. Agency response times vary and can extend the final research timeline.

                Keep this receipt for follow-up. This research is not a safety certification, legal opinion, title report, or environmental determination.
                """.formatted(
                receiptCode(row.get("lead_id")), safe(row.get("timestamp")), safe(row.get("state_slug")),
                safe(row.get("county_municipality")), safe(row.get("primary_question")), safe(row.get("deadline"))
        );
    }

    private void record(String leadId, String status, String errorCode) {
        csvStore.append(attemptsPath, HEADERS, List.of(
                OffsetDateTime.now(clock).toString(), leadId, status, errorCode
        ));
    }

    private static String safe(String value) {
        return value == null || value.isBlank() ? "Not provided" : value.replace('\r', ' ').replace('\n', ' ').trim();
    }
}

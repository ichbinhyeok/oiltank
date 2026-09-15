package owner.buriedoiltank.leads;

import java.nio.file.Path;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.data.PartnerType;
import owner.buriedoiltank.data.Scenario;
import owner.buriedoiltank.ops.CsvStore;
import org.springframework.stereotype.Service;

@Service
public class LeadService {
    private static final List<String> HEADERS = List.of(
            "lead_id",
            "timestamp",
            "page_id",
            "page_path",
            "state_slug",
            "route_family",
            "scenario",
            "partner_type",
            "user_role",
            "tank_status",
            "zip_code",
            "closing_timeline",
            "name",
            "email",
            "phone",
            "notes",
            "tool_id",
            "tank_type",
            "risk_band",
            "commercial_intent",
            "result_summary",
            "property_address",
            "county_municipality",
            "primary_question",
            "deadline",
            "has_documents",
            "submission_token"
    );

    private final Path leadsPath;
    private final CsvStore csvStore;
    private final Clock clock;
    private final EventLogService eventLogService;

    public LeadService(SiteProperties siteProperties, CsvStore csvStore, Clock clock, EventLogService eventLogService) {
        this.leadsPath = siteProperties.getStorageRoot().resolve("leads").resolve("leads.csv");
        this.csvStore = csvStore;
        this.clock = clock;
        this.eventLogService = eventLogService;
        this.csvStore.ensureFile(leadsPath, HEADERS);
    }

    public synchronized CapturedLead captureLead(LeadCaptureRequest request) {
        String submissionToken = blankIfNull(request.getSubmissionToken()).trim();
        if (!submissionToken.isBlank()) {
            Map<String, String> existing = leads().stream()
                    .filter(row -> submissionToken.equals(row.get("submission_token")))
                    .findFirst()
                    .orElse(null);
            if (existing != null) {
                return new CapturedLead(existing.get("lead_id"), existing.get("timestamp"), false);
            }
        }
        Scenario scenario = Scenario.fromSlug(request.getScenario());
        PartnerType partnerType = request.getPartnerType() == null || request.getPartnerType().isBlank()
                ? scenario.defaultPartnerType()
                : PartnerType.fromSlug(request.getPartnerType());
        String leadId = UUID.randomUUID().toString();
        String timestamp = OffsetDateTime.now(clock).toString();
        csvStore.append(leadsPath, HEADERS, List.of(
                leadId,
                timestamp,
                request.getPageId(),
                request.getPagePath(),
                request.getStateSlug(),
                blankIfNull(request.getRouteFamily()),
                scenario.slug(),
                partnerType.slug(),
                blankIfNull(request.getUserRole()),
                blankIfNull(request.getTankStatus()),
                blankIfNull(request.getZipCode()),
                blankIfNull(request.getClosingTimeline()),
                blankIfNull(request.getName()),
                request.getEmail(),
                blankIfNull(request.getPhone()),
                blankIfNull(request.getNotes()),
                blankIfNull(request.getToolId()),
                blankIfNull(request.getTankType()),
                blankIfNull(request.getRiskBand()),
                blankIfNull(request.getCommercialIntent()),
                blankIfNull(request.getResultSummary()),
                blankIfNull(request.getPropertyAddress()),
                blankIfNull(request.getCountyMunicipality()),
                blankIfNull(request.getPrimaryQuestion()),
                blankIfNull(request.getDeadline()),
                blankIfNull(request.getHasDocuments()),
                submissionToken
        ));

        recordStoredEvent("lead_submit", request, scenario, partnerType);
        if ("record-research".equals(request.getRouteFamily())) {
            recordStoredEvent("research_form_submit_success", request, scenario, partnerType);
            if ("interpret_documents".equals(request.getPrimaryQuestion())) {
                recordStoredEvent("document_interpretation_request", request, scenario, partnerType);
            }
            if (List.of("new-jersey", "new-york").contains(request.getStateSlug())) {
                recordStoredEvent("qualified_case", request, scenario, partnerType);
            }
        }
        return new CapturedLead(leadId, timestamp, true);
    }

    public List<Map<String, String>> leads() {
        return csvStore.readAll(leadsPath);
    }

    public String leadsCsv() {
        return csvStore.readRaw(leadsPath);
    }

    public Map<String, String> requireLead(String leadId) {
        return leads().stream()
                .filter(row -> leadId.equals(row.get("lead_id")))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown lead ID"));
    }

    private void recordStoredEvent(String eventType, LeadCaptureRequest request, Scenario scenario, PartnerType partnerType) {
        LeadEventRequest event = new LeadEventRequest();
        event.setEventType(eventType);
        event.setPageId(request.getPageId());
        event.setPagePath(request.getPagePath());
        event.setStateSlug(request.getStateSlug());
        event.setRouteFamily(request.getRouteFamily());
        event.setScenario(scenario.slug());
        event.setPartnerType(partnerType.slug());
        event.setElement("lead-form");
        event.setToolId(request.getToolId());
        eventLogService.recordEvent(event);
    }

    private static String blankIfNull(String value) {
        return value == null ? "" : value;
    }

    public record CapturedLead(String leadId, String timestamp, boolean newlyStored) {
    }
}

package owner.buriedoiltank.leads;

import java.nio.file.Path;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.data.PartnerType;
import owner.buriedoiltank.data.Scenario;
import owner.buriedoiltank.ops.CsvStore;
import org.springframework.stereotype.Service;

@Service
public class LeadService {
    private static final List<String> HEADERS = List.of(
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
            "notes"
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

    public void captureLead(LeadCaptureRequest request) {
        Scenario scenario = Scenario.fromSlug(request.getScenario());
        PartnerType partnerType = request.getPartnerType() == null || request.getPartnerType().isBlank()
                ? scenario.defaultPartnerType()
                : PartnerType.fromSlug(request.getPartnerType());
        csvStore.append(leadsPath, HEADERS, List.of(
                OffsetDateTime.now(clock).toString(),
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
                blankIfNull(request.getNotes())
        ));

        LeadEventRequest event = new LeadEventRequest();
        event.setEventType("lead_submit");
        event.setPageId(request.getPageId());
        event.setPagePath(request.getPagePath());
        event.setStateSlug(request.getStateSlug());
        event.setRouteFamily(request.getRouteFamily());
        event.setScenario(scenario.slug());
        event.setPartnerType(partnerType.slug());
        event.setElement("lead-form");
        eventLogService.recordEvent(event);
    }

    public List<Map<String, String>> leads() {
        return csvStore.readAll(leadsPath);
    }

    public String leadsCsv() {
        return csvStore.readRaw(leadsPath);
    }

    private static String blankIfNull(String value) {
        return value == null ? "" : value;
    }
}

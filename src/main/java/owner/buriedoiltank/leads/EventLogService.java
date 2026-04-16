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
import owner.buriedoiltank.ops.OpsRefreshRequestedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class EventLogService {
    private static final List<String> HEADERS = List.of(
            "timestamp",
            "event_type",
            "page_id",
            "page_path",
            "state_slug",
            "route_family",
            "scenario",
            "partner_type",
            "element",
            "referrer"
    );

    private final Path eventsPath;
    private final CsvStore csvStore;
    private final Clock clock;
    private final ApplicationEventPublisher eventPublisher;

    public EventLogService(
            SiteProperties siteProperties,
            CsvStore csvStore,
            Clock clock,
            ApplicationEventPublisher eventPublisher
    ) {
        this.eventsPath = siteProperties.getStorageRoot().resolve("leads").resolve("lead_events.csv");
        this.csvStore = csvStore;
        this.clock = clock;
        this.eventPublisher = eventPublisher;
        this.csvStore.ensureFile(eventsPath, HEADERS);
    }

    public void recordEvent(LeadEventRequest request) {
        Scenario scenario = Scenario.fromSlug(request.getScenario());
        PartnerType partnerType = request.getPartnerType() == null || request.getPartnerType().isBlank()
                ? scenario.defaultPartnerType()
                : PartnerType.fromSlug(request.getPartnerType());
        csvStore.append(eventsPath, HEADERS, List.of(
                OffsetDateTime.now(clock).toString(),
                request.getEventType(),
                request.getPageId(),
                request.getPagePath(),
                blankIfNull(request.getStateSlug()),
                blankIfNull(request.getRouteFamily()),
                scenario.slug(),
                partnerType.slug(),
                blankIfNull(request.getElement()),
                blankIfNull(request.getReferrer())
        ));
        eventPublisher.publishEvent(new OpsRefreshRequestedEvent("lead-event-recorded"));
    }

    public List<Map<String, String>> events() {
        return csvStore.readAll(eventsPath);
    }

    public String eventsCsv() {
        return csvStore.readRaw(eventsPath);
    }

    private static String blankIfNull(String value) {
        return value == null ? "" : value;
    }
}

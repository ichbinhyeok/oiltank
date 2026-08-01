package owner.buriedoiltank.ops;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.data.ContentRepository;
import owner.buriedoiltank.leads.EventLogService;
import owner.buriedoiltank.leads.LeadService;
import owner.buriedoiltank.leads.LeadDispositionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.context.ApplicationEventPublisher;

class OpsSnapshotServiceTests {
    @Test
    void sourceFreshnessReviewMarksScopesAndRoutesStaleWhenReviewDateHasPassed(@TempDir Path tempDir) {
        Clock fixedClock = Clock.fixed(Instant.parse("2026-06-10T00:00:00Z"), ZoneId.of("Asia/Seoul"));
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        ContentRepository contentRepository = new ContentRepository(objectMapper);
        RouteInventoryService routeInventoryService = new RouteInventoryService(contentRepository, fixedClock);
        CsvStore csvStore = new CsvStore();

        SiteProperties siteProperties = new SiteProperties();
        siteProperties.setBaseUrl(URI.create("http://localhost:8080"));
        siteProperties.setStorageRoot(tempDir);

        ApplicationEventPublisher eventPublisher = event -> {
        };
        EventLogService eventLogService = new EventLogService(siteProperties, csvStore, fixedClock, eventPublisher);
        LeadService leadService = new LeadService(siteProperties, csvStore, fixedClock, eventLogService);
        LeadDispositionService leadDispositionService = new LeadDispositionService(siteProperties, csvStore, fixedClock, eventPublisher);
        SearchMetricsRepository searchMetricsRepository = new SearchMetricsRepository(csvStore, siteProperties, fixedClock);
        OpsSnapshotService opsSnapshotService = new OpsSnapshotService(
                contentRepository,
                routeInventoryService,
                leadService,
                leadDispositionService,
                eventLogService,
                searchMetricsRepository,
                objectMapper,
                fixedClock,
                siteProperties
        );

        OpsSnapshots.SnapshotBundle snapshotBundle = opsSnapshotService.snapshotBundle();

        assertThat(snapshotBundle.sourceFreshnessReviewSnapshot().staleScopeCount()).isEqualTo(4);
        assertThat(snapshotBundle.sourceFreshnessReviewSnapshot().freshScopeCount()).isEqualTo(7);
        assertThat(snapshotBundle.sourceFreshnessReviewSnapshot().staleRouteCount()).isEqualTo(22);
        assertThat(snapshotBundle.adminMetricsSnapshot().staleScopeCount()).isEqualTo(4);
        assertThat(snapshotBundle.adminMetricsSnapshot().staleRouteCount()).isEqualTo(22);
        assertThat(snapshotBundle.sourceFreshnessReviewSnapshot().scopes())
                .filteredOn(scope -> "stale".equals(scope.sourceFreshnessStatus()))
                .allMatch(scope -> scope.daysUntilReview() < 0);
        assertThat(snapshotBundle.promotionReviewSnapshot().blockers())
                .anySatisfy(blocker -> assertThat(blocker).contains("Source review overdue for Connecticut"));
    }
}

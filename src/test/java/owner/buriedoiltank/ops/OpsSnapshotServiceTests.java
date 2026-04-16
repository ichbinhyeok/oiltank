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
        OpsSnapshotService opsSnapshotService = new OpsSnapshotService(
                contentRepository,
                routeInventoryService,
                leadService,
                eventLogService,
                objectMapper,
                fixedClock,
                siteProperties
        );

        OpsSnapshots.SnapshotBundle snapshotBundle = opsSnapshotService.snapshotBundle();

        assertThat(snapshotBundle.sourceFreshnessReviewSnapshot().staleScopeCount()).isEqualTo(11);
        assertThat(snapshotBundle.sourceFreshnessReviewSnapshot().freshScopeCount()).isZero();
        assertThat(snapshotBundle.sourceFreshnessReviewSnapshot().staleRouteCount()).isEqualTo(41);
        assertThat(snapshotBundle.adminMetricsSnapshot().staleScopeCount()).isEqualTo(11);
        assertThat(snapshotBundle.adminMetricsSnapshot().staleRouteCount()).isEqualTo(41);
        assertThat(snapshotBundle.sourceFreshnessReviewSnapshot().scopes())
                .allMatch(scope -> scope.daysUntilReview() < 0);
        assertThat(snapshotBundle.promotionReviewSnapshot().blockers())
                .anySatisfy(blocker -> assertThat(blocker).contains("Source review overdue for New Jersey"));
    }
}

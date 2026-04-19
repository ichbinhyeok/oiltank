package owner.buriedoiltank.ops;

import java.net.URI;
import java.util.List;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.pages.PageModels;
import owner.buriedoiltank.data.SourceFreshnessStatus;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final OpsSnapshotService opsSnapshotService;
    private final URI baseUrl;

    public AdminService(
            OpsSnapshotService opsSnapshotService,
            SiteProperties siteProperties
    ) {
        this.opsSnapshotService = opsSnapshotService;
        this.baseUrl = siteProperties.getBaseUrl();
    }

    public PageModels.AdminPageModel buildPage() {
        OpsSnapshots.SnapshotBundle snapshotBundle = opsSnapshotService.snapshotBundle();
        OpsSnapshots.AdminMetricsSnapshot adminMetrics = snapshotBundle.adminMetricsSnapshot();

        List<PageModels.MetricCard> metrics = List.of(
                new PageModels.MetricCard("Indexable routes", Long.toString(adminMetrics.indexableRoutes())),
                new PageModels.MetricCard("Held routes", Long.toString(adminMetrics.heldRoutes())),
                new PageModels.MetricCard("Stale scopes", Long.toString(adminMetrics.staleScopeCount())),
                new PageModels.MetricCard("Stale routes", Long.toString(adminMetrics.staleRouteCount())),
                new PageModels.MetricCard("CTA clicks (28d)", Long.toString(adminMetrics.ctaClicks())),
                new PageModels.MetricCard("Lead opens (28d)", Long.toString(adminMetrics.leadOpens())),
                new PageModels.MetricCard("Lead submissions (28d)", Long.toString(adminMetrics.leadSubmissions()))
        );

        List<PageModels.PartnerMetric> partnerMetrics = adminMetrics.leadsByPartnerType().entrySet().stream()
                .map(entry -> new PageModels.PartnerMetric(entry.getKey(), entry.getValue()))
                .toList();

        List<PageModels.RouteFamilyMetric> routeFamilyMetrics = adminMetrics.ctaClicksByRouteFamily().entrySet().stream()
                .map(entry -> new PageModels.RouteFamilyMetric(entry.getKey(), entry.getValue()))
                .toList();

        List<PageModels.RouteReviewRow> routeRows = snapshotBundle.routeStatuses().stream()
                .map(row -> new PageModels.RouteReviewRow(
                        row.routePath(),
                        row.scope(),
                        row.routeFamily(),
                        row.phase(),
                        row.indexStatus(),
                        row.sourceFreshnessStatus(),
                        row.last28DayCtaClicks(),
                        row.last28DayLeadOpens(),
                        row.last28DayLeadSubmissions(),
                        row.promotionRecommendation(),
                        row.recommendationReason()
                ))
                .toList();

        List<PageModels.FreshnessReviewRow> freshnessRows = snapshotBundle.sourceFreshnessReviewSnapshot().scopes().stream()
                .map(row -> new PageModels.FreshnessReviewRow(
                        row.scopeLabel(),
                        row.scopeType(),
                        row.sourceFreshnessStatus(),
                        formatDate(row.verifiedOn()),
                        formatDate(row.nextReviewOn()),
                        reviewWindow(row.daysUntilReview()),
                        row.affectedRoutes(),
                        row.affectedIndexableRoutes(),
                        row.affectedHeldRoutes(),
                        row.sourceTitles(),
                        row.affectedPaths()
                ))
                .toList();

        List<PageModels.FreshnessReviewRow> staleFreshnessRows = freshnessRows.stream()
                .filter(row -> SourceFreshnessStatus.STALE.slug().equals(row.freshnessStatus()))
                .toList();

        String freshnessSummary = adminMetrics.staleScopeCount() == 0
                ? "All current state and guide scopes are within the review window."
                : adminMetrics.staleScopeCount() + " scopes are past review and blocking "
                + adminMetrics.staleRouteCount() + " route variants until source review is refreshed.";

        return new PageModels.AdminPageModel(
                new PageModels.PageMeta(
                        "Admin | Buried Oil Tank Verdict",
                        "Lead, CTA, and route-promotion review surface.",
                        baseUrl.resolve("/admin/").toString(),
                        false,
                        List.of(),
                        baseUrl.resolve("/og-default.png").toString(),
                        "Buried Oil Tank Verdict site preview",
                        ""
                ),
                metrics,
                partnerMetrics,
                routeFamilyMetrics,
                routeRows,
                staleFreshnessRows,
                freshnessRows,
                freshnessSummary,
                snapshotBundle.promotionReviewSnapshot().agentSummary()
        );
    }

    private static String formatDate(java.time.LocalDate value) {
        return value == null ? "Not scheduled" : value.toString();
    }

    private static String reviewWindow(long daysUntilReview) {
        if (daysUntilReview == Long.MAX_VALUE) {
            return "No review date";
        }
        if (daysUntilReview < 0) {
            return Math.abs(daysUntilReview) + " days overdue";
        }
        if (daysUntilReview == 0) {
            return "Due today";
        }
        return daysUntilReview + " days remaining";
    }
}

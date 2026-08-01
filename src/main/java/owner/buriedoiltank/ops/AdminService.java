package owner.buriedoiltank.ops;

import java.net.URI;
import java.util.List;
import java.util.Map;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.leads.LeadDispositionService;
import owner.buriedoiltank.leads.LeadService;
import owner.buriedoiltank.pages.PageModels;
import owner.buriedoiltank.data.SourceFreshnessStatus;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final OpsSnapshotService opsSnapshotService;
    private final LeadService leadService;
    private final LeadDispositionService leadDispositionService;
    private final URI baseUrl;

    public AdminService(
            OpsSnapshotService opsSnapshotService,
            LeadService leadService,
            LeadDispositionService leadDispositionService,
            SiteProperties siteProperties
    ) {
        this.opsSnapshotService = opsSnapshotService;
        this.leadService = leadService;
        this.leadDispositionService = leadDispositionService;
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
                new PageModels.MetricCard("Lead submissions (28d)", Long.toString(adminMetrics.leadSubmissions())),
                new PageModels.MetricCard("Approved leads (28d)", adminMetrics.approvedLeads() + " / 40"),
                new PageModels.MetricCard("Pending review", Long.toString(adminMetrics.pendingLeads())),
                new PageModels.MetricCard("Approved revenue (28d)", money(adminMetrics.approvedPayoutCents()) + " / $1,000")
        );

        List<PageModels.PartnerMetric> partnerMetrics = adminMetrics.leadsByPartnerType().entrySet().stream()
                .map(entry -> new PageModels.PartnerMetric(entry.getKey(), entry.getValue()))
                .toList();

        List<PageModels.RouteFamilyMetric> routeFamilyMetrics = adminMetrics.ctaClicksByRouteFamily().entrySet().stream()
                .map(entry -> new PageModels.RouteFamilyMetric(entry.getKey(), entry.getValue()))
                .toList();

        List<PageModels.ToolFunnelMetric> toolFunnels = adminMetrics.toolFunnels().stream()
                .map(row -> new PageModels.ToolFunnelMetric(
                        row.toolId(), row.starts(), row.completes(), row.resultViews(),
                        row.commercialTriggers(), row.resultCtaClicks(), row.leadSubmissions()
                ))
                .toList();

        Map<String, Map<String, String>> latestDispositions = leadDispositionService.latestByLeadId();
        List<PageModels.LeadReviewRow> leadRows = leadService.leads().reversed().stream()
                .filter(row -> !row.getOrDefault("lead_id", "").isBlank())
                .limit(100)
                .map(row -> {
                    Map<String, String> decision = latestDispositions.getOrDefault(row.get("lead_id"), Map.of());
                    return new PageModels.LeadReviewRow(
                            row.getOrDefault("lead_id", ""),
                            row.getOrDefault("timestamp", ""),
                            row.getOrDefault("email", ""),
                            row.getOrDefault("phone", ""),
                            row.getOrDefault("tool_id", ""),
                            row.getOrDefault("risk_band", ""),
                            row.getOrDefault("commercial_intent", ""),
                            row.getOrDefault("result_summary", ""),
                            decision.getOrDefault("disposition", "pending"),
                            parseInt(decision.get("payout_cents")),
                            decision.getOrDefault("notes", "")
                    );
                })
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
                        "Admin | Oil Tank Route",
                        "Approval, payout, tool-funnel, and route review surface.",
                        baseUrl.resolve("/admin/").toString(),
                        false,
                        List.of(),
                        baseUrl.resolve("/og-default.png").toString(),
                        "Oil Tank Route site preview",
                        ""
                ),
                metrics,
                partnerMetrics,
                routeFamilyMetrics,
                toolFunnels,
                leadRows,
                routeRows,
                staleFreshnessRows,
                freshnessRows,
                freshnessSummary,
                snapshotBundle.promotionReviewSnapshot().agentSummary()
        );
    }

    private static String money(long cents) {
        return String.format("$%,.2f", cents / 100.0);
    }

    private static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception exception) {
            return 0;
        }
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

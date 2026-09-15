package owner.buriedoiltank.ops;

import java.net.URI;
import java.util.List;
import java.util.Map;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.data.SourceFreshnessStatus;
import owner.buriedoiltank.leads.CaseStatus;
import owner.buriedoiltank.leads.CaseStatusService;
import owner.buriedoiltank.leads.CaseActivityService;
import owner.buriedoiltank.leads.DocumentStorageService;
import owner.buriedoiltank.leads.CustomerReceiptService;
import owner.buriedoiltank.leads.LeadService;
import owner.buriedoiltank.leads.RecordResearchNotificationService;
import owner.buriedoiltank.config.RecordResearchNotificationProperties;
import owner.buriedoiltank.pages.PageModels;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final OpsSnapshotService opsSnapshotService;
    private final LeadService leadService;
    private final CaseStatusService caseStatusService;
    private final RecordResearchNotificationService notificationService;
    private final RecordResearchNotificationProperties notificationProperties;
    private final CustomerReceiptService customerReceiptService;
    private final DocumentStorageService documentStorageService;
    private final CaseActivityService caseActivityService;
    private final URI baseUrl;

    public AdminService(
            OpsSnapshotService opsSnapshotService,
            LeadService leadService,
            CaseStatusService caseStatusService,
            RecordResearchNotificationService notificationService,
            RecordResearchNotificationProperties notificationProperties,
            CustomerReceiptService customerReceiptService,
            DocumentStorageService documentStorageService,
            CaseActivityService caseActivityService,
            SiteProperties siteProperties
    ) {
        this.opsSnapshotService = opsSnapshotService;
        this.leadService = leadService;
        this.caseStatusService = caseStatusService;
        this.notificationService = notificationService;
        this.notificationProperties = notificationProperties;
        this.customerReceiptService = customerReceiptService;
        this.documentStorageService = documentStorageService;
        this.caseActivityService = caseActivityService;
        this.baseUrl = siteProperties.getBaseUrl();
    }

    public PageModels.AdminPageModel buildPage() {
        OpsSnapshots.SnapshotBundle snapshotBundle = opsSnapshotService.snapshotBundle();
        OpsSnapshots.AdminMetricsSnapshot adminMetrics = snapshotBundle.adminMetricsSnapshot();

        List<PageModels.MetricCard> metrics = List.of(
                new PageModels.MetricCard("Successful submissions (28d)", Long.toString(adminMetrics.successfulSubmissions())),
                new PageModels.MetricCard("Open research cases", Long.toString(adminMetrics.openCases())),
                new PageModels.MetricCard("Qualified NJ/NY cases", Long.toString(adminMetrics.qualifiedCases())),
                new PageModels.MetricCard("Document interpretation", Long.toString(adminMetrics.documentInterpretationRequests())),
                new PageModels.MetricCard("Service CTA views", Long.toString(adminMetrics.serviceCtaViews())),
                new PageModels.MetricCard("Service CTA clicks", Long.toString(adminMetrics.serviceCtaClicks())),
                new PageModels.MetricCard("Research form starts", Long.toString(adminMetrics.researchFormStarts())),
                new PageModels.MetricCard("Indexable routes", Long.toString(adminMetrics.indexableRoutes())),
                new PageModels.MetricCard("Held routes", Long.toString(adminMetrics.heldRoutes())),
                new PageModels.MetricCard("Stale scopes", Long.toString(adminMetrics.staleScopeCount()))
        );

        List<PageModels.PartnerMetric> statusMetrics = breakdown(adminMetrics.casesByStatus());
        List<PageModels.PartnerMetric> stateMetrics = breakdown(adminMetrics.submissionsByState());
        List<PageModels.PartnerMetric> pageMetrics = breakdown(adminMetrics.submissionsByPage());
        List<PageModels.PartnerMetric> questionMetrics = breakdown(adminMetrics.submissionsByQuestion());

        List<PageModels.ToolFunnelMetric> toolFunnels = adminMetrics.toolFunnels().stream()
                .map(row -> new PageModels.ToolFunnelMetric(
                        row.toolId(), row.starts(), row.completes(), row.resultViews(),
                        row.commercialTriggers(), row.resultCtaClicks(), row.leadSubmissions()
                ))
                .toList();

        Map<String, Map<String, String>> latestStatuses = caseStatusService.latestByLeadId();
        Map<String, Map<String, String>> latestNotifications = notificationService.latestByLeadId();
        Map<String, Map<String, String>> latestCustomerReceipts = customerReceiptService.latestByLeadId();
        List<PageModels.CaseReviewRow> caseRows = leadService.leads().reversed().stream()
                .filter(row -> "record-research".equals(row.get("route_family")))
                .filter(row -> !row.getOrDefault("lead_id", "").isBlank())
                .limit(100)
                .map(row -> {
                    Map<String, String> status = latestStatuses.getOrDefault(row.get("lead_id"), Map.of());
                    Map<String, String> notification = latestNotifications.getOrDefault(row.get("lead_id"), Map.of());
                    Map<String, String> customerReceipt = latestCustomerReceipts.getOrDefault(row.get("lead_id"), Map.of());
                    String leadId = row.get("lead_id");
                    List<PageModels.CaseDocumentRow> documents = documentStorageService.forLead(leadId).stream()
                            .map(document -> new PageModels.CaseDocumentRow(
                                    document.getOrDefault("document_id", ""),
                                    document.getOrDefault("original_name", "Document"),
                                    document.getOrDefault("content_type", ""),
                                    sizeLabel(document.getOrDefault("size_bytes", "0"))))
                            .toList();
                    List<PageModels.CaseActivityRow> activities = caseActivityService.forLead(leadId).reversed().stream()
                            .limit(20)
                            .map(activity -> new PageModels.CaseActivityRow(
                                    activity.getOrDefault("timestamp", ""), activity.getOrDefault("activity_type", ""),
                                    activity.getOrDefault("route_id", ""), activity.getOrDefault("agency", ""),
                                    activity.getOrDefault("channel", ""), activity.getOrDefault("outcome", ""),
                                    activity.getOrDefault("source_id", ""), activity.getOrDefault("notes", ""),
                                    activity.getOrDefault("next_action", ""), activity.getOrDefault("check_date", "")))
                            .toList();
                    return new PageModels.CaseReviewRow(
                            row.getOrDefault("lead_id", ""),
                            row.getOrDefault("timestamp", ""),
                            row.getOrDefault("property_address", ""),
                            row.getOrDefault("state_slug", ""),
                            row.getOrDefault("county_municipality", ""),
                            row.getOrDefault("user_role", ""),
                            row.getOrDefault("tank_status", ""),
                            row.getOrDefault("primary_question", ""),
                            row.getOrDefault("deadline", ""),
                            row.getOrDefault("has_documents", ""),
                            row.getOrDefault("email", ""),
                            row.getOrDefault("notes", ""),
                            status.getOrDefault("status", CaseStatus.INTAKE.slug()),
                            status.getOrDefault("notes", ""),
                            notification.getOrDefault("status", "not-attempted"),
                            notification.getOrDefault("error_code", ""),
                            customerReceipt.getOrDefault("status", "not-attempted"),
                            customerReceipt.getOrDefault("error_code", ""),
                            documents,
                            activities
                    );
                })
                .toList();

        List<PageModels.RouteReviewRow> routeRows = snapshotBundle.routeStatuses().stream()
                .map(row -> new PageModels.RouteReviewRow(
                        row.routePath(), row.scope(), row.routeFamily(), row.phase(), row.indexStatus(),
                        row.sourceFreshnessStatus(), row.last28DayCtaClicks(), row.last28DayLeadOpens(),
                        row.last28DayLeadSubmissions(), row.promotionRecommendation(), row.recommendationReason()
                ))
                .toList();

        List<PageModels.FreshnessReviewRow> freshnessRows = snapshotBundle.sourceFreshnessReviewSnapshot().scopes().stream()
                .map(row -> new PageModels.FreshnessReviewRow(
                        row.scopeLabel(), row.scopeType(), row.sourceFreshnessStatus(), formatDate(row.verifiedOn()),
                        formatDate(row.nextReviewOn()), reviewWindow(row.daysUntilReview()), row.affectedRoutes(),
                        row.affectedIndexableRoutes(), row.affectedHeldRoutes(), row.sourceTitles(), row.affectedPaths()
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
                        "Research operations | Oil Tank Route",
                        "Protected property-research case queue, funnel, notification, and route review surface.",
                        baseUrl.resolve("/admin/").toString(), false, List.of(),
                        baseUrl.resolve("/og-default.png").toString(), "Oil Tank Route site preview", ""
                ),
                metrics, statusMetrics, stateMetrics, pageMetrics, questionMetrics, toolFunnels, caseRows,
                routeRows, staleFreshnessRows, freshnessRows, freshnessSummary,
                snapshotBundle.promotionReviewSnapshot().agentSummary(),
                notificationReadiness()
        );
    }

    private String notificationReadiness() {
        if (!notificationProperties.isEnabled()) return "Operator alerts and customer receipts are disabled. Cases still persist here, but no email will be sent.";
        String issue = notificationProperties.configurationIssue();
        return issue == null
                ? "Operator alerts and customer receipts are enabled and fully configured."
                : "Email delivery needs configuration: " + issue.replace('_', ' ') + ".";
    }

    private static String sizeLabel(String rawBytes) {
        try {
            long bytes = Long.parseLong(rawBytes);
            if (bytes >= 1024 * 1024) return String.format(java.util.Locale.US, "%.1f MB", bytes / 1048576d);
            return Math.max(1, bytes / 1024) + " KB";
        } catch (NumberFormatException ignored) {
            return "Unknown size";
        }
    }

    private static List<PageModels.PartnerMetric> breakdown(Map<String, Long> values) {
        return values.entrySet().stream()
                .map(entry -> new PageModels.PartnerMetric(entry.getKey(), entry.getValue()))
                .toList();
    }

    private static String formatDate(java.time.LocalDate value) {
        return value == null ? "Not scheduled" : value.toString();
    }

    private static String reviewWindow(long daysUntilReview) {
        if (daysUntilReview == Long.MAX_VALUE) return "No review date";
        if (daysUntilReview < 0) return Math.abs(daysUntilReview) + " days overdue";
        if (daysUntilReview == 0) return "Due today";
        return daysUntilReview + " days remaining";
    }
}

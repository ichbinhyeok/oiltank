package owner.buriedoiltank.web;

import java.net.URI;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.leads.EventLogService;
import owner.buriedoiltank.leads.LeadService;
import owner.buriedoiltank.leads.CaseStatusService;
import owner.buriedoiltank.leads.CaseActivityService;
import owner.buriedoiltank.leads.CaseActivityType;
import owner.buriedoiltank.leads.DocumentStorageService;
import owner.buriedoiltank.leads.CustomerReceiptService;
import owner.buriedoiltank.leads.RecordResearchNotificationService;
import owner.buriedoiltank.ops.OpsSnapshotService;
import owner.buriedoiltank.ops.RouteInventoryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import java.nio.charset.StandardCharsets;

@Controller
public class OpsController {
    private final RouteInventoryService routeInventoryService;
    private final OpsSnapshotService opsSnapshotService;
    private final LeadService leadService;
    private final CaseStatusService caseStatusService;
    private final RecordResearchNotificationService notificationService;
    private final CustomerReceiptService customerReceiptService;
    private final CaseActivityService caseActivityService;
    private final DocumentStorageService documentStorageService;
    private final EventLogService eventLogService;
    private final URI baseUrl;

    public OpsController(
            RouteInventoryService routeInventoryService,
            OpsSnapshotService opsSnapshotService,
            LeadService leadService,
            CaseStatusService caseStatusService,
            RecordResearchNotificationService notificationService,
            CustomerReceiptService customerReceiptService,
            CaseActivityService caseActivityService,
            DocumentStorageService documentStorageService,
            EventLogService eventLogService,
            SiteProperties siteProperties
    ) {
        this.routeInventoryService = routeInventoryService;
        this.opsSnapshotService = opsSnapshotService;
        this.leadService = leadService;
        this.caseStatusService = caseStatusService;
        this.notificationService = notificationService;
        this.customerReceiptService = customerReceiptService;
        this.caseActivityService = caseActivityService;
        this.documentStorageService = documentStorageService;
        this.eventLogService = eventLogService;
        this.baseUrl = siteProperties.getBaseUrl();
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String robots() {
        return """
                User-agent: *
                Allow: /
                Disallow: /admin
                Disallow: /api
                Sitemap: %s
                """.formatted(baseUrl.resolve("/sitemap.xml"));
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public ResponseEntity<String> sitemap() {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");
        for (String path : routeInventoryService.indexableSitemapPaths()) {
            xml.append("<url>");
            xml.append("<loc>").append(baseUrl.resolve(path)).append("</loc>");
            xml.append("<lastmod>").append(routeInventoryService.lastModifiedForPath(path)).append("</lastmod>");
            String priority = routeInventoryService.sitemapPriorityForPath(path);
            if (priority != null) {
                xml.append("<priority>").append(priority).append("</priority>");
            }
            xml.append("</url>");
        }
        xml.append("</urlset>");
        return ResponseEntity.ok(xml.toString());
    }

    @GetMapping(value = "/admin/exports/routes.json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> routeManifest() {
        opsSnapshotService.refreshSnapshots();
        return ResponseEntity.ok(opsSnapshotService.routeManifestJson());
    }

    @GetMapping(value = "/admin/exports/route-status.csv", produces = "text/csv")
    @ResponseBody
    public ResponseEntity<String> routeStatusCsv() {
        opsSnapshotService.refreshSnapshots();
        return ResponseEntity.ok(opsSnapshotService.routeStatusCsv());
    }

    @GetMapping(value = "/admin/exports/promotion-review.json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> promotionReview() {
        opsSnapshotService.refreshSnapshots();
        return ResponseEntity.ok(opsSnapshotService.promotionReviewJson());
    }

    @GetMapping(value = "/admin/exports/source-freshness-review.json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> sourceFreshnessReview() {
        opsSnapshotService.refreshSnapshots();
        return ResponseEntity.ok(opsSnapshotService.sourceFreshnessReviewJson());
    }

    @GetMapping(value = "/admin/exports/admin-metrics-snapshot.json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> adminMetricsSnapshot() {
        opsSnapshotService.refreshSnapshots();
        return ResponseEntity.ok(opsSnapshotService.adminMetricsJson());
    }

    @GetMapping(value = "/admin/exports/leads.csv", produces = "text/csv")
    @ResponseBody
    public ResponseEntity<String> leadsCsv() {
        return ResponseEntity.ok(leadService.leadsCsv());
    }

    @GetMapping(value = "/admin/exports/lead-events.csv", produces = "text/csv")
    @ResponseBody
    public ResponseEntity<String> leadEventsCsv() {
        return ResponseEntity.ok(eventLogService.eventsCsv());
    }

    @GetMapping(value = "/admin/exports/case-status.csv", produces = "text/csv")
    @ResponseBody
    public ResponseEntity<String> caseStatusCsv() {
        return ResponseEntity.ok(caseStatusService.statusCsv());
    }

    @GetMapping(value = "/admin/exports/notification-attempts.csv", produces = "text/csv")
    @ResponseBody
    public ResponseEntity<String> notificationAttemptsCsv() {
        return ResponseEntity.ok(notificationService.attemptsCsv());
    }

    @GetMapping(value = "/admin/exports/customer-receipt-attempts.csv", produces = "text/csv")
    @ResponseBody
    public ResponseEntity<String> customerReceiptAttemptsCsv() {
        return ResponseEntity.ok(customerReceiptService.attemptsCsv());
    }

    @GetMapping(value = "/admin/exports/case-activity.csv", produces = "text/csv")
    @ResponseBody
    public ResponseEntity<String> caseActivityCsv() {
        return ResponseEntity.ok(caseActivityService.activityCsv());
    }

    @GetMapping(value = "/admin/exports/case-documents.csv", produces = "text/csv")
    @ResponseBody
    public ResponseEntity<String> caseDocumentsCsv() {
        return ResponseEntity.ok(documentStorageService.metadataCsv());
    }

    @GetMapping("/admin/cases/{leadId}/documents/{documentId}")
    @ResponseBody
    public ResponseEntity<Resource> downloadCaseDocument(
            @org.springframework.web.bind.annotation.PathVariable String leadId,
            @org.springframework.web.bind.annotation.PathVariable String documentId
    ) {
        try {
            DocumentStorageService.StoredDocument document = documentStorageService.require(leadId, documentId);
            String safeName = document.originalName().replace("\"", "");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" +
                            java.net.URLEncoder.encode(safeName, StandardCharsets.UTF_8).replace("+", "%20"))
                    .contentType(MediaType.parseMediaType(document.contentType()))
                    .body(new FileSystemResource(document.path()));
        } catch (IllegalArgumentException exception) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Document not found");
        }
    }

    @PostMapping("/admin/cases/status")
    public String updateCaseStatus(
            @RequestParam String leadId,
            @RequestParam String status,
            @RequestParam(required = false) String notes
    ) {
        boolean knownResearchCase = leadService.leads().stream()
                .anyMatch(row -> leadId.equals(row.get("lead_id"))
                        && "record-research".equals(row.get("route_family")));
        if (!knownResearchCase) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Unknown lead ID"
            );
        }
        caseStatusService.record(leadId, status, notes);
        caseActivityService.record(leadId, CaseActivityType.STATUS_CHANGE.slug(), "", "", "admin",
                status, "", notes, "", "");
        return "redirect:/admin/?case=saved#case-desk";
    }

    @PostMapping("/admin/cases/activity")
    public String recordCaseActivity(
            @RequestParam String leadId,
            @RequestParam String activityType,
            @RequestParam(required = false) String routeId,
            @RequestParam(required = false) String agency,
            @RequestParam(required = false) String channel,
            @RequestParam(required = false) String outcome,
            @RequestParam(required = false) String sourceId,
            @RequestParam(required = false) String notes,
            @RequestParam(required = false) String nextAction,
            @RequestParam(required = false) String checkDate
    ) {
        requireResearchCase(leadId);
        try {
            caseActivityService.record(leadId, activityType, routeId, agency, channel, outcome,
                    sourceId, notes, nextAction, checkDate);
        } catch (IllegalArgumentException exception) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, exception.getMessage());
        }
        return "redirect:/admin/?activity=saved#case-desk";
    }

    @PostMapping("/admin/cases/notification/retry")
    public String retryCaseNotification(@RequestParam String leadId) {
        java.util.Map<String, String> researchCase;
        try {
            researchCase = leadService.requireLead(leadId);
        } catch (IllegalArgumentException exception) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Unknown case ID"
            );
        }
        if (!"record-research".equals(researchCase.get("route_family"))) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Case is not a record-research intake"
            );
        }
        notificationService.retryAfterOperatorReview(researchCase);
        return "redirect:/admin/?notification=queued#case-desk";
    }

    @PostMapping("/admin/cases/customer-receipt/retry")
    public String retryCustomerReceipt(@RequestParam String leadId) {
        java.util.Map<String, String> researchCase = requireResearchCase(leadId);
        customerReceiptService.retryAfterOperatorReview(researchCase);
        return "redirect:/admin/?receipt=queued#case-desk";
    }

    private java.util.Map<String, String> requireResearchCase(String leadId) {
        try {
            java.util.Map<String, String> researchCase = leadService.requireLead(leadId);
            if (!"record-research".equals(researchCase.get("route_family"))) {
                throw new IllegalArgumentException("Case is not a record-research intake");
            }
            return researchCase;
        } catch (IllegalArgumentException exception) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Unknown case ID");
        }
    }
}

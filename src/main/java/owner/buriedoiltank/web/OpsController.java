package owner.buriedoiltank.web;

import java.net.URI;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.leads.EventLogService;
import owner.buriedoiltank.leads.LeadService;
import owner.buriedoiltank.leads.LeadDispositionService;
import owner.buriedoiltank.ops.OpsSnapshotService;
import owner.buriedoiltank.ops.RouteInventoryService;
import owner.buriedoiltank.pages.ProductPageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OpsController {
    private final RouteInventoryService routeInventoryService;
    private final OpsSnapshotService opsSnapshotService;
    private final LeadService leadService;
    private final LeadDispositionService leadDispositionService;
    private final EventLogService eventLogService;
    private final URI baseUrl;

    public OpsController(
            RouteInventoryService routeInventoryService,
            OpsSnapshotService opsSnapshotService,
            LeadService leadService,
            LeadDispositionService leadDispositionService,
            EventLogService eventLogService,
            SiteProperties siteProperties
    ) {
        this.routeInventoryService = routeInventoryService;
        this.opsSnapshotService = opsSnapshotService;
        this.leadService = leadService;
        this.leadDispositionService = leadDispositionService;
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
        appendSitemapUrl(xml, "/", "2026-08-02", "1.0");
        for (String path : ProductPageService.CORE_PATHS) {
            appendSitemapUrl(xml, path, "2026-08-02", "0.9");
        }
        for (String path : routeInventoryService.indexableSitemapPaths()) {
            if (path.equals("/") || ProductPageService.CORE_PATHS.contains(path)) {
                continue;
            }
            xml.append("<url>");
            xml.append("<loc>").append(baseUrl.resolve(path)).append("</loc>");
            xml.append("<lastmod>").append(routeInventoryService.lastModifiedForPath(path)).append("</lastmod>");
            xml.append("</url>");
        }
        xml.append("</urlset>");
        return ResponseEntity.ok(xml.toString());
    }

    private void appendSitemapUrl(StringBuilder xml, String path, String lastModified, String priority) {
        xml.append("<url>");
        xml.append("<loc>").append(baseUrl.resolve(path)).append("</loc>");
        xml.append("<lastmod>").append(lastModified).append("</lastmod>");
        xml.append("<priority>").append(priority).append("</priority>");
        xml.append("</url>");
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

    @GetMapping(value = "/admin/exports/lead-dispositions.csv", produces = "text/csv")
    @ResponseBody
    public ResponseEntity<String> leadDispositionsCsv() {
        return ResponseEntity.ok(leadDispositionService.decisionsCsv());
    }

    @PostMapping("/admin/leads/decision")
    public String decideLead(
            @RequestParam String leadId,
            @RequestParam String disposition,
            @RequestParam(required = false) Integer payoutCents,
            @RequestParam(required = false) String notes
    ) {
        boolean knownLead = leadService.leads().stream()
                .anyMatch(row -> leadId.equals(row.get("lead_id")));
        if (!knownLead) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Unknown lead ID"
            );
        }
        leadDispositionService.record(leadId, disposition, payoutCents, notes);
        return "redirect:/admin/?decision=saved#lead-review";
    }
}

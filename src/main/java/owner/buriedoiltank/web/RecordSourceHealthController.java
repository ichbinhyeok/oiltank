package owner.buriedoiltank.web;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import owner.buriedoiltank.data.RecordsDataRepository;
import owner.buriedoiltank.data.ResearchCatalog;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Covered by the existing /admin authentication rule; no network scans or case mutations. */
@RestController
public class RecordSourceHealthController {
    private final RecordsDataRepository records;
    private final Clock clock;
    public RecordSourceHealthController(RecordsDataRepository records, Clock clock) { this.records = records; this.clock = clock; }

    @GetMapping("/admin/exports/record-source-health.json")
    public ResponseEntity<Map<String, Object>> health() {
        LocalDate today = LocalDate.now(clock);
        List<Map<String, Object>> routes = ResearchCatalog.ALL.stream().map(records::resolveResearchEntry).map(entry -> Map.<String, Object>of(
            "routeId", entry.id(), "path", entry.path(), "instructionsReviewed", entry.reviewedOn().toString(),
            "reviewDue", entry.reviewedOn().plusDays(90).toString(), "overdue", today.isAfter(entry.reviewedOn().plusDays(90)),
            "support", entry.assisted() ? "NJNY intake review" : "self-service only",
            "sources", entry.steps().stream().map(step -> Map.of("title", step.title(), "url", step.sourceUrl())).toList()
        )).toList();
        return ResponseEntity.ok().header("Cache-Control", "no-store").header("X-Robots-Tag", "noindex")
            .body(Map.of("reviewDate", today.toString(), "overrides", records.overrideHealth(), "routes", routes,
                "limits", "Instruction review is not a live link/portal test. No network polling or customer lookup is performed. See launch source evidence for UI checks."));
    }
}

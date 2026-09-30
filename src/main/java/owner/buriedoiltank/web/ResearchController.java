package owner.buriedoiltank.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.data.ResearchCatalog;
import owner.buriedoiltank.pages.PageModels.PageMeta;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ResearchController {
    private final SiteProperties properties;
    private final ObjectMapper mapper;
    private final owner.buriedoiltank.data.RecordsDataRepository records;
    public ResearchController(SiteProperties properties, ObjectMapper mapper, owner.buriedoiltank.data.RecordsDataRepository records) {
        this.properties = properties;
        this.mapper = mapper;
        this.records = records;
    }

    @GetMapping({"/research-areas/", "/research-areas"})
    public String directory(Model model) {
        model.addAttribute("meta", meta("Local oil tank records | NJ, NY & Portland", "Local and county record routes for New Jersey, New York and Portland, Oregon. Find the correct oil tank record holder before requesting a file. Human assistance is NJ/NY only.", ResearchCatalog.HUB));
        return "researchDirectory";
    }

    @GetMapping({"/research-areas/{slug}/", "/research-areas/{slug}"})
    public String area(@PathVariable String slug, Model model) { return detail(slug, "area", model); }

    @GetMapping({"/record-help/{slug}/", "/record-help/{slug}"})
    public String problem(@PathVariable String slug, Model model) { return detail(slug, "problem", model); }

    @GetMapping({"/records/{slug}/", "/records/{slug}"})
    public String task(@PathVariable String slug, Model model) { return detail(slug, "task", model); }

    @GetMapping({"/find-records/", "/find-records"})
    public String finder(Model model) {
        model.addAttribute("meta", meta("Find oil tank records by place and document | Oil Tank Route",
            "Find official oil tank record routes, prepare a private worksheet and draft a missing-document request. No email needed. Not an automatic property search.", "/find-records/"));
        return "recordFinder";
    }

    private String detail(String slug, String kind, Model model) {
        var entry = ResearchCatalog.find(slug);
        if (entry == null || !kind.equals(entry.kind())) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        entry = records.resolveResearchEntry(entry);
        model.addAttribute("entry", entry);
        model.addAttribute("meta", meta(entry.title() + " | Oil Tank Route", entry.summary(), entry.path(), entry.reviewedOn()));
        return "researchDetail";
    }

    @GetMapping({"/research-examples/", "/research-examples"})
    public String examples(Model model) {
        model.addAttribute("meta", meta("Oil tank record research walkthroughs | Oil Tank Route", "Two public-source walkthroughs show how Nassau and NYC change the research plan. No invented customer outcomes or safety conclusions.", "/research-examples/"));
        return "researchExamples";
    }

    private PageMeta meta(String title, String description, String path) {
        return meta(title, description, path, java.time.LocalDate.of(2026, 9, 30));
    }
    private PageMeta meta(String title, String description, String path, java.time.LocalDate reviewed) {
        String base = properties.getBaseUrl().toString().replaceAll("/+$", "");
        try {
            String schema = mapper.writeValueAsString(Map.of("@context", "https://schema.org", "@type", "WebPage",
                "name", title, "description", description, "url", base + path,
                "dateModified", reviewed.toString(),
                "publisher", Map.of("@type", "Organization", "name", "Oil Tank Route", "url", base + "/")));
            return new PageMeta(title, description, base + path, true, List.of(schema), base + "/og-default.png",
                "Oil Tank Route record research", properties.getAnalyticsMeasurementId());
        } catch (JsonProcessingException e) { throw new IllegalStateException("Cannot serialize research metadata", e); }
    }
}

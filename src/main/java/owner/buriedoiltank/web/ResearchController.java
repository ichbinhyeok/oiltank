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
    public ResearchController(SiteProperties properties, ObjectMapper mapper) {
        this.properties = properties;
        this.mapper = mapper;
    }

    @GetMapping({"/research-areas/", "/research-areas"})
    public String directory(Model model) {
        model.addAttribute("meta", meta("Local oil tank record research | NJ & NY", "Local and county research routes, five missing-record questions, and source-backed walkthroughs. Find the correct oil tank record holder before requesting a file.", ResearchCatalog.HUB));
        return "researchDirectory";
    }

    @GetMapping({"/research-areas/{slug}/", "/research-areas/{slug}"})
    public String area(@PathVariable String slug, Model model) { return detail(slug, "area", model); }

    @GetMapping({"/record-help/{slug}/", "/record-help/{slug}"})
    public String problem(@PathVariable String slug, Model model) { return detail(slug, "problem", model); }

    private String detail(String slug, String kind, Model model) {
        var entry = ResearchCatalog.find(slug);
        if (entry == null || !kind.equals(entry.kind())) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        model.addAttribute("entry", entry);
        model.addAttribute("meta", meta(entry.title() + " | Oil Tank Route", entry.summary(), entry.path()));
        return "researchDetail";
    }

    @GetMapping({"/research-examples/", "/research-examples"})
    public String examples(Model model) {
        model.addAttribute("meta", meta("Oil tank record research walkthroughs | Oil Tank Route", "Two public-source walkthroughs show how Nassau and NYC change the research plan. No invented customer outcomes or safety conclusions.", "/research-examples/"));
        return "researchExamples";
    }

    private PageMeta meta(String title, String description, String path) {
        String base = properties.getBaseUrl().toString().replaceAll("/+$", "");
        try {
            String schema = mapper.writeValueAsString(Map.of("@context", "https://schema.org", "@type", "WebPage",
                "name", title, "description", description, "url", base + path,
                "dateModified", ResearchCatalog.REVIEWED.toString(),
                "publisher", Map.of("@type", "Organization", "name", "Oil Tank Route", "url", base + "/")));
            return new PageMeta(title, description, base + path, true, List.of(schema), base + "/og-default.png",
                "Oil Tank Route record research", properties.getAnalyticsMeasurementId());
        } catch (JsonProcessingException e) { throw new IllegalStateException("Cannot serialize research metadata", e); }
    }
}

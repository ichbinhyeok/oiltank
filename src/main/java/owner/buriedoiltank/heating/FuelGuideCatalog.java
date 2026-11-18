package owner.buriedoiltank.heating;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.HashSet;

/** Public editorial content; no customer or operational case data belongs here. */
public final class FuelGuideCatalog {
    private FuelGuideCatalog() {}
    public record Link(String title, String url) {}
    public record Section(String id, String heading, List<String> paragraphs, List<String> bullets,
                          List<String> columns, List<List<String>> rows) {}
    public record Guide(String slug, String category, String title, String description, String answer,
                        String tool, int gallons, List<Section> sections, List<Link> related, List<Link> sources) {
        public String path() { return "/" + slug + "/"; }
    }
    public static final List<Guide> ALL = load();
    public static Guide require(String slug) {
        return ALL.stream().filter(g -> g.slug().equals(slug)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown fuel guide"));
    }
    private static List<Guide> load() {
        try (var stream = FuelGuideCatalog.class.getResourceAsStream("/data/normalized/heating-oil-guides.json")) {
            var guides = new ObjectMapper().readValue(stream, new TypeReference<List<Guide>>() {});
            var paths = new HashSet<String>();
            for (var guide : guides) {
                if (!guide.slug().matches("[a-z0-9]+(?:-[a-z0-9]+)*") || !paths.add(guide.path())
                    || guide.sections().size() < 4 || guide.sources().isEmpty())
                    throw new IllegalArgumentException("Invalid fuel guide");
                var sections = new HashSet<String>();
                for (var section : guide.sections()) {
                    if (!section.id().matches("[a-z0-9-]+") || !sections.add(section.id()))
                        throw new IllegalArgumentException("Invalid section anchor");
                    if (section.rows().stream().anyMatch(row -> row.size() != section.columns().size()))
                        throw new IllegalArgumentException("Invalid comparison table");
                }
            }
            return List.copyOf(guides);
        } catch (Exception error) { throw new IllegalStateException("Public fuel guides unavailable", error); }
    }
}

package owner.buriedoiltank.data;

import static org.assertj.core.api.Assertions.assertThat;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;

class ResearchCatalogTests {
    @Test void launchCohortIsDifferentiatedAndInternallyConnected() {
        assertThat(ResearchCatalog.AREAS).hasSize(16);
        assertThat(ResearchCatalog.PROBLEMS).hasSize(5);
        assertThat(ResearchCatalog.ALL).extracting(ResearchCatalog.Entry::slug).doesNotHaveDuplicates();
        assertThat(ResearchCatalog.ALL).extracting(ResearchCatalog.Entry::title).doesNotHaveDuplicates();
        assertThat(ResearchCatalog.routes()).extracting(ServiceRoute::path).doesNotHaveDuplicates().hasSize(23);
        for (var entry : ResearchCatalog.ALL) {
            assertThat(entry.steps()).hasSizeGreaterThanOrEqualTo(3);
            assertThat(entry.identifiers()).isNotBlank();
            assertThat(entry.request()).contains("[");
            assertThat(entry.boundary()).isNotBlank();
            assertThat(entry.question()).isIn("find_records", "verify_claim", "interpret_documents", "agency_follow_up", "choose_next_step");
            assertThat(entry.related()).hasSizeGreaterThanOrEqualTo(2);
            for (String related : entry.related()) assertThat(ResearchCatalog.find(related)).isNotNull();
            for (var step : entry.steps()) {
                assertThat(step.body().length()).isGreaterThan(170);
                var uri = URI.create(step.sourceUrl());
                assertThat(uri.getScheme()).isEqualTo("https");
                assertThat(uri.getHost()).isNotBlank();
                assertThat(step.sourceLabel()).isNotBlank();
            }
        }
        assertThat(ResearchCatalog.areaCount("new-jersey")).isEqualTo(8);
        assertThat(ResearchCatalog.areaCount("new-york")).isEqualTo(8);
    }
}

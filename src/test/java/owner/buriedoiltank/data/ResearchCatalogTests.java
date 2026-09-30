package owner.buriedoiltank.data;

import static org.assertj.core.api.Assertions.assertThat;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;

class ResearchCatalogTests {
    @Test void launchCohortIsDifferentiatedAndInternallyConnected() {
        assertThat(ResearchCatalog.AREAS).hasSize(24);
        assertThat(ResearchCatalog.PROBLEMS).hasSize(5);
        assertThat(ResearchCatalog.ALL).extracting(ResearchCatalog.Entry::slug).doesNotHaveDuplicates();
        assertThat(ResearchCatalog.ALL).extracting(ResearchCatalog.Entry::title).doesNotHaveDuplicates();
        assertThat(ResearchCatalog.TASKS).hasSize(8);
        assertThat(ResearchCatalog.routes()).extracting(ServiceRoute::path).doesNotHaveDuplicates().hasSize(40);
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
        assertThat(ResearchCatalog.areaCount("new-jersey")).isEqualTo(10);
        assertThat(ResearchCatalog.areaCount("new-york")).isEqualTo(13);
        assertThat(ResearchCatalog.areaCount("oregon")).isEqualTo(1);
    }

    @Test void everyRouteHasAnEvidenceTargetAndDocumentSpecificDraftScope() {
        for (var entry : ResearchCatalog.ALL) {
            var guide = RecordEvidence.forEntry(entry);
            assertThat(guide.target()).isNotBlank();
            assertThat(guide.firstRequest()).isNotBlank();
            assertThat(guide.checks()).hasSizeGreaterThanOrEqualTo(2);
            for (var check : guide.checks()) {
                assertThat(check.document()).isNotBlank();
                assertThat(check.compare()).isNotBlank();
                assertThat(check.gap()).isNotBlank();
            }
        }
        assertThat(RecordEvidence.forEntry(ResearchCatalog.find("nassau-ny")).firstRequest()).contains("verification letter");
        assertThat(RecordEvidence.forEntry(ResearchCatalog.find("portland-or")).firstRequest()).contains("Fire & Rescue");
        assertThat(ResearchCatalog.find("portland-or").assisted()).isFalse();
        assertThat(ResearchCatalog.find("njdep-nfa-letter").steps()).anyMatch(s -> s.body().contains("NFA is not needed"));
    }
}

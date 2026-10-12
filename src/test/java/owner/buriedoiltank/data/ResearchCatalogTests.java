package owner.buriedoiltank.data;

import static org.assertj.core.api.Assertions.assertThat;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;

class ResearchCatalogTests {
    @Test void launchCohortIsDifferentiatedAndInternallyConnected() {
        assertThat(ResearchCatalog.AREAS).hasSize(36);
        assertThat(ResearchCatalog.PROBLEMS).hasSize(10);
        assertThat(ResearchCatalog.ALL).extracting(ResearchCatalog.Entry::slug).doesNotHaveDuplicates();
        assertThat(ResearchCatalog.ALL).extracting(ResearchCatalog.Entry::title).doesNotHaveDuplicates();
        assertThat(ResearchCatalog.TASKS).hasSize(17);
        assertThat(ResearchCatalog.routes()).extracting(ServiceRoute::path).doesNotHaveDuplicates().hasSize(67);
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
        assertThat(ResearchCatalog.areaCount("new-york")).isEqualTo(15);
        assertThat(ResearchCatalog.areaCount("oregon")).isEqualTo(1);
        assertThat(ResearchCatalog.areaCount("washington")).isEqualTo(3);
        assertThat(ResearchCatalog.areaCount("massachusetts")).isEqualTo(3);
        assertThat(ResearchCatalog.areaCount("connecticut")).isEqualTo(4);
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

    @Test void searchGuidesUseValidResolvedStepPositionsAndPreserveServiceBoundaries() {
        int count = 0;
        for (var entry : ResearchCatalog.ALL) {
            var guide = RecordSearchGuide.forEntry(entry);
            if (guide == null) continue;
            count++;
            assertThat(guide.startStep()).isBetween(0, entry.steps().size() - 1);
            assertThat(guide.action()).isNotBlank();
            assertThat(guide.expected()).isNotBlank();
            assertThat(guide.fallback()).isNotBlank();
            assertThat(guide.answers()).isNotEmpty();
            assertThat(entry.updatedOn()).isEqualTo(RecordSearchGuide.UPDATED);
        }
        assertThat(count).isEqualTo(30);
        assertThat(ResearchCatalog.AREAS.stream().filter(e -> e.state().equals("washington"))).allMatch(e -> !e.assisted());
        assertThat(ResearchCatalog.find("new-rochelle-ny").assisted()).isTrue();
        // Editorial improvement must not pretend a blocked NJDEP live instruction check succeeded.
        assertThat(ResearchCatalog.find("njdep-nfa-letter").reviewedOn()).isBefore(RecordSearchGuide.UPDATED);
        assertThat(ResearchCatalog.find("connecticut-deep-spill-documents").steps()).anyMatch(s -> s.sourceUrl().equals("https://deep-react.service.ct.gov/"));
    }

    @Test void broaderRoutesHaveDistinctCustodyLimitsAndSpecificEvidence() {
        assertThat(ExpandedRecordRoutes.PROFILES).hasSize(17);
        for (var profile : ExpandedRecordRoutes.PROFILES) {
            var entry = ResearchCatalog.find(profile.entry().slug());
            assertThat(entry).isNotNull();
            assertThat(RecordEvidence.forEntry(entry)).isEqualTo(profile.evidence());
            assertThat(RecordSearchGuide.forEntry(entry)).isEqualTo(profile.search());
            assertThat(entry.recordFamily()).isEqualTo(profile.family());
            assertThat(entry.reviewedOn()).isEqualTo(RecordSearchGuide.UPDATED);
            if (!entry.state().equals("new-york")) assertThat(entry.assisted()).isFalse();
        }
        assertThat(ResearchCatalog.states()).hasSize(12);
        for (String state : ResearchCatalog.states()) assertThat(ResearchCatalog.stateLabel(state)).doesNotContain("-");
        assertThat(ResearchCatalog.find("wilton-ct").boundary()).contains("October 2018");
        assertThat(ResearchCatalog.find("wellesley-ma").steps()).anyMatch(s -> s.body().contains("FP 291"));
        assertThat(ResearchCatalog.find("pennsylvania-dep-tank-files").boundary()).contains("home-oil-tank", "excluded");
        assertThat(ResearchCatalog.find("nysdec-pbs-registration-records").recordFamily()).isEqualTo("registration");
    }
}

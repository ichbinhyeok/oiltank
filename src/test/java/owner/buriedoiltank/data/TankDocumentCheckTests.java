package owner.buriedoiltank.data;

import static org.assertj.core.api.Assertions.assertThat;
import java.net.URI;
import org.junit.jupiter.api.Test;

class TankDocumentCheckTests {
    @Test void situationsUseKnownDocumentTargetsAndPreserveExistingProblemIds() {
        var ids = TankDocumentCheck.DOCUMENTS.stream().map(TankDocumentCheck.Document::id).toList();
        assertThat(ids).doesNotHaveDuplicates();
        assertThat(TankDocumentCheck.SITUATIONS).extracting(TankDocumentCheck.Situation::id).doesNotHaveDuplicates();
        for (var situation : TankDocumentCheck.SITUATIONS) {
            assertThat(situation.targets()).isNotEmpty().doesNotHaveDuplicates().allMatch(ids::contains);
            assertThat(situation.answer()).isNotBlank();
            assertThat(situation.next()).isNotBlank();
        }
        for (var entry : ResearchCatalog.PROBLEMS) assertThat(TankDocumentCheck.situation(entry.slug()).id()).isEqualTo(entry.slug());
        assertThat(TankDocumentCheck.situation("unknown").id()).isEqualTo("home-sale");
    }
    @Test void stateGuidanceLinksOnlyRealRoutesAndDoesNotExpandAssistance() {
        for (var region : TankDocumentCheck.REGIONS) {
            assertThat(URI.create(region.sourceUrl()).getScheme()).isEqualTo("https");
            assertThat(TankDocumentCheck.routes(region)).doesNotContainNull();
            for (var entry : TankDocumentCheck.routes(region)) assertThat(entry.state()).isEqualTo(region.id());
            if (region.id().equals("oregon") || region.id().equals("washington"))
                assertThat(TankDocumentCheck.routes(region)).allMatch(e -> !e.assisted());
        }
        assertThat(TankDocumentCheck.REGIONS.get(0).routes()).isEmpty();
        assertThat(TankDocumentCheck.REGIONS).filteredOn(r -> r.id().equals("new-jersey")).singleElement()
            .satisfies(r -> assertThat(r.distinction()).contains("NFA is not needed", "does not establish"));
    }
    @Test void newProblemsHaveDifferentEvidenceAndPreserveUnchangedSourceReviewDates() {
        assertThat(TransactionResearch.ENTRIES).hasSize(5);
        for (var entry : TransactionResearch.ENTRIES) {
            assertThat(ResearchCatalog.find(entry.slug())).isEqualTo(entry);
            assertThat(entry.updatedOn()).isEqualTo(RecordSearchGuide.UPDATED);
            assertThat(RecordEvidence.forEntry(entry).checks()).hasSizeGreaterThanOrEqualTo(2);
        }
        assertThat(RecordEvidence.forEntry(ResearchCatalog.find("oil-tank-soil-test-records")).firstRequest()).contains("Sampling");
        assertThat(RecordEvidence.forEntry(ResearchCatalog.find("oil-tank-open-permit")).firstRequest()).doesNotContain("Sampling");
        assertThat(ResearchCatalog.find("missing-removal-records").reviewedOn()).isEqualTo(ResearchCatalog.REVIEWED);
        assertThat(ResearchCatalog.find("missing-removal-records").updatedOn()).isEqualTo(RecordSearchGuide.UPDATED);
    }
}

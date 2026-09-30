package owner.buriedoiltank.data;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import owner.buriedoiltank.config.SiteProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RecordsDataRepositoryTests {
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    void partialOverrideUpdatesMatchingRouteAndPreservesBundledRoutes(@TempDir Path tempDir) throws Exception {
        RecordsDataRepository repository = repository(tempDir);
        RecordLookupSource original = repository.lookupSources("new-jersey").getFirst();
        RecordLookupSource changed = copy(original, "Locally reviewed parcel route", original.agency());
        writeOverride(tempDir, List.of(changed));

        assertThat(repository.lookupSources("new-jersey")).hasSize(5);
        assertThat(repository.lookupSources("new-york")).hasSize(4);
        assertThat(repository.lookupSources("new-jersey"))
                .filteredOn(source -> RecordsDataRepository.routeKey(source).equals(RecordsDataRepository.routeKey(original)))
                .extracting(RecordLookupSource::title)
                .containsExactly("Locally reviewed parcel route");
    }

    @Test
    void partialOverrideAddsANewStableKeyWithoutReplacingTheBaseline(@TempDir Path tempDir) throws Exception {
        RecordsDataRepository repository = repository(tempDir);
        RecordLookupSource original = repository.lookupSources("new-jersey").getFirst();
        RecordLookupSource added = copy(original, "County archive", "Example County Archive");
        writeOverride(tempDir, List.of(added));

        assertThat(repository.lookupSources("new-jersey")).hasSize(6);
        assertThat(repository.lookupSources("new-jersey")).extracting(RecordLookupSource::title)
                .contains(original.title(), "County archive");
    }

    @Test
    void malformedDuplicateAndIncompleteOverridesSafelyFallBackToBaseline(@TempDir Path tempDir) throws Exception {
        RecordsDataRepository repository = repository(tempDir);
        Path override = overridePath(tempDir);
        Files.createDirectories(override.getParent());

        Files.writeString(override, "{not-json");
        assertBaseline(repository);
        assertThat(repository.overrideHealth().get("route-intelligence")).contains("INVALID");

        RecordLookupSource original = repository.lookupSources("new-jersey").getFirst();
        writeOverride(tempDir, List.of(original, original));
        assertBaseline(repository);

        Files.writeString(override, "[{\"stateSlug\":\"new-jersey\",\"title\":\"\"}]");
        assertBaseline(repository);
    }

    private RecordsDataRepository repository(Path tempDir) {
        SiteProperties properties = new SiteProperties();
        properties.setStorageRoot(tempDir);
        return new RecordsDataRepository(objectMapper, properties);
    }

    @Test void invalidAggregateDiagnosticsAgreeWithPublicFallback(@TempDir Path tempDir) throws Exception {
        RecordsDataRepository repository = repository(tempDir);
        var baseline = repository.incidentAggregates("new-york");
        assertThat(baseline).isNotEmpty();
        Path override = tempDir.resolve("records/new-york-counties.json");
        Files.createDirectories(override.getParent());
        for (String invalid : List.of("null", "[null]", "{not-json")) {
            Files.writeString(override, invalid);
            assertThat(repository.overrideHealth().get("incident-aggregate")).contains("INVALID");
            assertThat(repository.incidentAggregates("new-york")).isEqualTo(baseline);
        }
    }

    @Test void sourceChangePropagatesToEveryMatchingResearchStepWithoutWritingCases(@TempDir Path tempDir) throws Exception {
        RecordsDataRepository repository = repository(tempDir);
        var original = repository.lookupSources("new-jersey").stream().filter(s -> s.title().equals("NJDEP DataMiner")).findFirst().orElseThrow();
        var changed = new RecordLookupSource(original.stateSlug(), "Reviewed DataMiner entry", original.agency(),
            original.jurisdiction(), original.sourceType(), "https://njems.nj.gov/DataMiner/",
            original.searchBy(), original.usefulFor(), "Rehearsal note", original.identifiers(), original.requestMethod(),
            original.requestRequirements(), original.fee(), original.responseTime(), original.fallbackRoute(), original.verifiedOn());
        writeOverride(tempDir, List.of(changed));
        int matches = 0;
        for (var entry : ResearchCatalog.ALL) {
            var resolved = repository.resolveResearchEntry(entry);
            for (int i = 0; i < entry.steps().size(); i++) {
                if (entry.steps().get(i).sourceUrl().replaceAll("/+$", "").equals(original.url())) {
                    matches++;
                    assertThat(resolved.steps().get(i).sourceUrl()).isEqualTo(changed.url());
                    assertThat(resolved.steps().get(i).body()).contains("Rehearsal note");
                }
            }
        }
        assertThat(matches).isGreaterThan(1);
        assertThat(Files.exists(tempDir.resolve("operations"))).isFalse();
    }

    private void writeOverride(Path tempDir, List<RecordLookupSource> rows) throws Exception {
        Path override = overridePath(tempDir);
        Files.createDirectories(override.getParent());
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(override.toFile(), rows);
    }

    private static Path overridePath(Path tempDir) {
        return tempDir.resolve("records").resolve("route-intelligence.json");
    }

    private static void assertBaseline(RecordsDataRepository repository) {
        assertThat(repository.lookupSources("new-jersey")).hasSize(5);
        assertThat(repository.lookupSources("new-york")).hasSize(4);
    }

    private static RecordLookupSource copy(RecordLookupSource source, String title, String agency) {
        return new RecordLookupSource(
                source.stateSlug(), title, agency, source.jurisdiction(), source.sourceType(), source.url(),
                source.searchBy(), source.usefulFor(), source.caveat(), source.identifiers(), source.requestMethod(),
                source.requestRequirements(), source.fee(), source.responseTime(), source.fallbackRoute(), source.verifiedOn()
        );
    }
}

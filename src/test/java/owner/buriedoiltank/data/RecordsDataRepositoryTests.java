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

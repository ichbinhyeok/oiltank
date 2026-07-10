package owner.buriedoiltank.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import owner.buriedoiltank.config.SiteProperties;
import org.springframework.stereotype.Repository;

@Repository
public class RecordsDataRepository {
    private final List<RecordLookupSource> lookupSources;
    private final List<IncidentAggregate> incidentAggregates;
    private final ObjectMapper objectMapper;
    private final Path incidentOverridePath;

    public RecordsDataRepository(ObjectMapper objectMapper, SiteProperties siteProperties) {
        this.objectMapper = objectMapper;
        this.incidentOverridePath = siteProperties.getStorageRoot().resolve("records").resolve("new-york-counties.json");
        this.lookupSources = read(objectMapper, "data/normalized/records/lookup-sources.json", new TypeReference<>() {});
        this.incidentAggregates = read(objectMapper, "data/normalized/incidents/new-york-counties.json", new TypeReference<>() {});
    }

    public List<RecordLookupSource> lookupSources(String stateSlug) {
        return lookupSources.stream().filter(source -> stateSlug.equals(source.stateSlug())).toList();
    }

    public List<IncidentAggregate> incidentAggregates(String stateSlug) {
        return activeIncidentAggregates().stream()
                .filter(row -> stateSlug.equals(row.stateSlug()))
                .sorted(java.util.Comparator.comparingInt(IncidentAggregate::stateRank))
                .limit(5)
                .toList();
    }

    public IncidentAggregate requireCounty(String stateSlug, String countySlug) {
        return activeIncidentAggregates().stream()
                .filter(row -> stateSlug.equals(row.stateSlug()) && countySlug.equals(row.countySlug()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown incident county: " + stateSlug + " / " + countySlug));
    }

    private List<IncidentAggregate> activeIncidentAggregates() {
        if (Files.notExists(incidentOverridePath)) {
            return incidentAggregates;
        }
        try {
            List<IncidentAggregate> override = objectMapper.readValue(incidentOverridePath.toFile(), new TypeReference<>() {});
            return override.isEmpty() ? incidentAggregates : override;
        } catch (IOException ignored) {
            return incidentAggregates;
        }
    }

    private static <T> T read(ObjectMapper objectMapper, String path, TypeReference<T> type) {
        try (InputStream input = RecordsDataRepository.class.getClassLoader().getResourceAsStream(path)) {
            if (input == null) {
                throw new IllegalStateException("Missing records data: " + path);
            }
            return objectMapper.readValue(input, type);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read records data: " + path, exception);
        }
    }
}

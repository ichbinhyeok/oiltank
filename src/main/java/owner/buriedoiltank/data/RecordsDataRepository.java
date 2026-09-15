package owner.buriedoiltank.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import owner.buriedoiltank.config.SiteProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class RecordsDataRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordsDataRepository.class);
    private final List<RecordLookupSource> lookupSources;
    private final List<IncidentAggregate> incidentAggregates;
    private final ObjectMapper objectMapper;
    private final Path incidentOverridePath;
    private final Path routeOverridePath;

    public RecordsDataRepository(ObjectMapper objectMapper, SiteProperties siteProperties) {
        this.objectMapper = objectMapper;
        this.incidentOverridePath = siteProperties.getStorageRoot().resolve("records").resolve("new-york-counties.json");
        this.routeOverridePath = siteProperties.getStorageRoot().resolve("records").resolve("route-intelligence.json");
        List<RecordLookupSource> bundledSources = read(objectMapper, "data/normalized/records/lookup-sources.json", new TypeReference<>() {});
        validateSources(bundledSources, "bundled baseline");
        this.lookupSources = List.copyOf(bundledSources);
        this.incidentAggregates = read(objectMapper, "data/normalized/incidents/new-york-counties.json", new TypeReference<>() {});
    }

    public List<RecordLookupSource> lookupSources(String stateSlug) {
        return activeLookupSources().stream().filter(source -> stateSlug.equals(source.stateSlug())).toList();
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

    private List<RecordLookupSource> activeLookupSources() {
        if (Files.notExists(routeOverridePath)) {
            return lookupSources;
        }
        try {
            List<RecordLookupSource> override = objectMapper.readValue(routeOverridePath.toFile(), new TypeReference<>() {});
            if (override.isEmpty()) {
                return lookupSources;
            }
            validateSources(override, "route-intelligence override");
            Map<String, RecordLookupSource> merged = new LinkedHashMap<>();
            lookupSources.forEach(source -> merged.put(routeKey(source), source));
            override.forEach(source -> merged.put(routeKey(source), source));
            return List.copyOf(merged.values());
        } catch (IOException | RuntimeException exception) {
            LOGGER.warn("Ignored invalid route-intelligence override and kept the bundled baseline ({})",
                    exception.getClass().getSimpleName());
            return lookupSources;
        }
    }

    static String routeKey(RecordLookupSource source) {
        return String.join("|",
                normalizeKeyPart(source.stateSlug()),
                normalizeKeyPart(source.jurisdiction()),
                normalizeKeyPart(source.sourceType()),
                normalizeKeyPart(source.agency()));
    }

    private static String normalizeKeyPart(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
    }

    private static void validateSources(List<RecordLookupSource> sources, String sourceLabel) {
        if (sources == null) {
            throw new IllegalArgumentException(sourceLabel + " is null");
        }
        Set<String> keys = new HashSet<>();
        for (RecordLookupSource source : sources) {
            if (source == null
                    || isBlank(source.stateSlug())
                    || isBlank(source.title())
                    || isBlank(source.agency())
                    || isBlank(source.jurisdiction())
                    || isBlank(source.sourceType())
                    || isBlank(source.url())
                    || isBlank(source.caveat())
                    || isBlank(source.requestMethod())
                    || isBlank(source.requestRequirements())
                    || isBlank(source.fee())
                    || isBlank(source.responseTime())
                    || isBlank(source.fallbackRoute())
                    || isBlank(source.verifiedOn())
                    || isEmpty(source.searchBy())
                    || isEmpty(source.usefulFor())
                    || isEmpty(source.identifiers())) {
                throw new IllegalArgumentException(sourceLabel + " contains an incomplete route record");
            }
            URI uri = URI.create(source.url());
            if (!("https".equalsIgnoreCase(uri.getScheme()) || "http".equalsIgnoreCase(uri.getScheme())) || uri.getHost() == null) {
                throw new IllegalArgumentException(sourceLabel + " contains an invalid public URL");
            }
            LocalDate.parse(source.verifiedOn());
            String key = routeKey(source);
            if (!keys.add(key)) {
                throw new IllegalArgumentException(sourceLabel + " contains duplicate route key: " + key);
            }
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static boolean isEmpty(List<String> values) {
        return values == null || values.isEmpty() || values.stream().anyMatch(RecordsDataRepository::isBlank);
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

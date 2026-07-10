package owner.buriedoiltank.ops;

import java.nio.file.Path;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.Optional;
import owner.buriedoiltank.config.SiteProperties;
import org.springframework.stereotype.Repository;

@Repository
public class SearchMetricsRepository {
    private final CsvStore csvStore;
    private final Path metricsPath;
    private final Clock clock;

    public SearchMetricsRepository(CsvStore csvStore, SiteProperties properties, Clock clock) {
        this.csvStore = csvStore;
        this.metricsPath = properties.getStorageRoot().resolve("ops").resolve("gsc-route-metrics.csv");
        this.clock = clock;
    }

    public Optional<SearchMetricRow> currentForPath(String pagePath) {
        OffsetDateTime staleBefore = OffsetDateTime.now(clock).minusDays(14);
        return metricRows().stream()
                .map(this::parse)
                .flatMap(Optional::stream)
                .filter(row -> pagePath.equals(row.pagePath()))
                .filter(row -> !row.fetchedAt().isBefore(staleBefore))
                .max(Comparator.comparing(SearchMetricRow::fetchedAt));
    }

    private java.util.List<java.util.Map<String, String>> metricRows() {
        if (Files.exists(metricsPath)) {
            return csvStore.readAll(metricsPath);
        }
        try (InputStream input = SearchMetricsRepository.class.getClassLoader()
                .getResourceAsStream("data/normalized/search/gsc-route-metrics.csv")) {
            if (input == null) {
                return java.util.List.of();
            }
            Path temporary = Files.createTempFile("gsc-route-metrics-", ".csv");
            try {
                Files.writeString(temporary, new String(input.readAllBytes(), StandardCharsets.UTF_8), StandardCharsets.UTF_8);
                return csvStore.readAll(temporary);
            } finally {
                Files.deleteIfExists(temporary);
            }
        } catch (Exception ignored) {
            return java.util.List.of();
        }
    }

    private Optional<SearchMetricRow> parse(java.util.Map<String, String> row) {
        try {
            return Optional.of(new SearchMetricRow(
                    row.get("page_path"),
                    java.time.LocalDate.parse(row.get("window_start")),
                    java.time.LocalDate.parse(row.get("window_end")),
                    Long.parseLong(row.get("clicks")),
                    Long.parseLong(row.get("impressions")),
                    Double.parseDouble(row.get("ctr")),
                    Double.parseDouble(row.get("position")),
                    OffsetDateTime.parse(row.get("fetched_at"))
            ));
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }
}

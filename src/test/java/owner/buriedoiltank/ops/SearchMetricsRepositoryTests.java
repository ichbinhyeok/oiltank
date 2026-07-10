package owner.buriedoiltank.ops;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import owner.buriedoiltank.config.SiteProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SearchMetricsRepositoryTests {
    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-07-11T00:00:00Z"), ZoneOffset.UTC);

    @Test
    void returnsCurrentMetricsAndTreatsMissingOrStaleDataAsUnavailable(@TempDir Path tempDir) throws Exception {
        SiteProperties properties = new SiteProperties();
        properties.setBaseUrl(URI.create("https://oiltankroute.com"));
        properties.setStorageRoot(tempDir);
        Path metrics = tempDir.resolve("ops").resolve("gsc-route-metrics.csv");
        Files.createDirectories(metrics.getParent());
        Files.writeString(metrics, String.join("\n",
                "page_path,window_start,window_end,clicks,impressions,ctr,position,fetched_at",
                "/guides/abandoned-oil-tank-records/,2026-06-11,2026-07-08,3,210,0.0142857,12.4,2026-07-10T00:00:00Z",
                "/guides/buried-oil-tank-home-sale/,2026-05-20,2026-06-16,1,40,0.025,9.8,2026-06-20T00:00:00Z"
        ));

        SearchMetricsRepository repository = new SearchMetricsRepository(new CsvStore(), properties, CLOCK);

        assertThat(repository.currentForPath("/guides/abandoned-oil-tank-records/"))
                .get()
                .satisfies(row -> {
                    assertThat(row.impressions()).isEqualTo(210);
                    assertThat(row.clicks()).isEqualTo(3);
                    assertThat(row.position()).isEqualTo(12.4);
                });
        assertThat(repository.currentForPath("/guides/buried-oil-tank-home-sale/")).isEmpty();
        assertThat(repository.currentForPath("/missing/")).isEmpty();
    }
}

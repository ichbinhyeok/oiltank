package owner.buriedoiltank.ops;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CsvStoreMigrationTests {
    @TempDir
    Path directory;

    @Test
    void backsUpAndMigratesExistingRowsWhenHeadersExpand() throws Exception {
        Path csv = directory.resolve("leads").resolve("leads.csv");
        Files.createDirectories(csv.getParent());
        Files.writeString(csv, "timestamp,email\n2026-08-01,owner@example.com\n", StandardCharsets.UTF_8);

        CsvStore store = new CsvStore();
        store.ensureFile(csv, List.of("timestamp", "email", "tool_id"));

        assertThat(Files.readString(csv)).startsWith("timestamp,email,tool_id");
        assertThat(Files.readString(csv)).contains("2026-08-01,owner@example.com,");
        try (var files = Files.list(csv.getParent())) {
            assertThat(files.map(path -> path.getFileName().toString()))
                    .anyMatch(name -> name.startsWith("leads.csv.backup-"));
        }
    }
}

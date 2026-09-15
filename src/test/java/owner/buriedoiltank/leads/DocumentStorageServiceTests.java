package owner.buriedoiltank.leads;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Path;
import java.nio.file.Files;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.ops.CsvStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

class DocumentStorageServiceTests {
    @Test
    void storesPrivateDocumentWithIntegrityMetadataAndDeduplicatesRetry(@TempDir Path tempDir) {
        DocumentStorageService service = service(tempDir);
        MockMultipartFile document = new MockMultipartFile(
                "documents", "closure-letter.pdf", "application/pdf", "%PDF-1.7 example".getBytes());

        assertThat(service.store("case-1", List.of(document))).isEqualTo(1);
        assertThat(service.store("case-1", List.of(document))).isZero();
        assertThat(service.forLead("case-1")).singleElement().satisfies(row -> {
            assertThat(row.get("original_name")).isEqualTo("closure-letter.pdf");
            assertThat(row.get("sha256")).hasSize(64);
        });
        String documentId = service.forLead("case-1").getFirst().get("document_id");
        assertThat(service.require("case-1", documentId).path()).exists();
    }

    @Test
    void rejectsUnsupportedDocumentTypes(@TempDir Path tempDir) {
        DocumentStorageService service = service(tempDir);
        MockMultipartFile executable = new MockMultipartFile(
                "documents", "records.exe", "application/octet-stream", new byte[]{1, 2, 3});
        assertThatThrownBy(() -> service.validate(List.of(executable)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("PDF, JPG, and PNG");
    }

    @Test
    void rejectsAFileWhoseDeclaredTypeDoesNotMatchItsContent(@TempDir Path tempDir) {
        DocumentStorageService service = service(tempDir);
        MockMultipartFile spoofed = new MockMultipartFile(
                "documents", "records.pdf", "application/pdf", "not a pdf".getBytes());
        assertThatThrownBy(() -> service.validate(List.of(spoofed)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("does not match");
    }

    @Test
    void concurrentRetriesStoreOneDocumentAndOneRegisterRow(@TempDir Path tempDir) throws Exception {
        DocumentStorageService service = service(tempDir);
        MockMultipartFile document = new MockMultipartFile(
                "documents", "closure-letter.pdf", "application/pdf", "%PDF-1.7 concurrent".getBytes());
        CountDownLatch start = new CountDownLatch(1);
        try (var executor = Executors.newFixedThreadPool(2)) {
            Future<Integer> first = executor.submit(() -> { start.await(); return service.store("case-1", List.of(document)); });
            Future<Integer> second = executor.submit(() -> { start.await(); return service.store("case-1", List.of(document)); });
            start.countDown();

            assertThat(first.get() + second.get()).isEqualTo(1);
        }
        assertThat(service.forLead("case-1")).hasSize(1);
        try (var files = Files.list(tempDir.resolve("cases/case-1/documents"))) {
            assertThat(files).hasSize(1);
        }
    }

    private static DocumentStorageService service(Path tempDir) {
        SiteProperties properties = new SiteProperties();
        properties.setStorageRoot(tempDir);
        return new DocumentStorageService(properties, new CsvStore(),
                Clock.fixed(Instant.parse("2026-09-15T00:00:00Z"), ZoneOffset.UTC));
    }
}

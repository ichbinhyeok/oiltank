package owner.buriedoiltank.leads;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import owner.buriedoiltank.config.SiteProperties;
import owner.buriedoiltank.ops.CsvStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentStorageService {
    private static final long MAX_FILE_BYTES = 8L * 1024 * 1024;
    private static final long MAX_TOTAL_BYTES = 20L * 1024 * 1024;
    private static final int MAX_FILES = 3;
    private static final Map<String, String> EXTENSIONS = Map.of(
            "application/pdf", ".pdf",
            "image/jpeg", ".jpg",
            "image/png", ".png"
    );
    private static final List<String> HEADERS = List.of(
            "timestamp", "document_id", "lead_id", "original_name", "content_type",
            "size_bytes", "sha256", "relative_path"
    );

    private final Path storageRoot;
    private final Path metadataPath;
    private final CsvStore csvStore;
    private final Clock clock;

    public DocumentStorageService(SiteProperties siteProperties, CsvStore csvStore, Clock clock) {
        this.storageRoot = siteProperties.getStorageRoot().toAbsolutePath().normalize();
        this.metadataPath = storageRoot.resolve("operations").resolve("case-documents.csv");
        this.csvStore = csvStore;
        this.clock = clock;
        csvStore.ensureFile(metadataPath, HEADERS);
    }

    public void validate(List<MultipartFile> documents) {
        List<MultipartFile> present = present(documents);
        if (present.size() > MAX_FILES) throw new IllegalArgumentException("A maximum of three documents is allowed");
        long total = 0;
        for (MultipartFile document : present) {
            if (document.getSize() > MAX_FILE_BYTES) throw new IllegalArgumentException("A document exceeds 8 MB");
            total += document.getSize();
            if (!EXTENSIONS.containsKey(normalizeType(document.getContentType()))) {
                throw new IllegalArgumentException("Only PDF, JPG, and PNG documents are accepted");
            }
            if (!hasExpectedSignature(document, normalizeType(document.getContentType()))) {
                throw new IllegalArgumentException("The uploaded file content does not match its document type");
            }
        }
        if (total > MAX_TOTAL_BYTES) throw new IllegalArgumentException("Documents exceed 20 MB in total");
    }

    public synchronized int store(String leadId, List<MultipartFile> documents) {
        validate(documents);
        int stored = 0;
        for (MultipartFile document : present(documents)) {
            byte[] bytes = read(document);
            String digest = sha256(bytes);
            boolean exists = forLead(leadId).stream().anyMatch(row -> digest.equals(row.get("sha256")));
            if (exists) continue;
            String documentId = UUID.randomUUID().toString();
            String contentType = normalizeType(document.getContentType());
            String relative = "cases/" + leadId + "/documents/" + documentId + EXTENSIONS.get(contentType);
            Path target = storageRoot.resolve(relative).normalize();
            if (!target.startsWith(storageRoot)) throw new IllegalStateException("Unsafe document path");
            try {
                Files.createDirectories(target.getParent());
                Path temporary = Files.createTempFile(target.getParent(), documentId, ".upload");
                Files.write(temporary, bytes);
                Files.move(temporary, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                throw new IllegalStateException("Failed to store case document", exception);
            }
            csvStore.append(metadataPath, HEADERS, List.of(
                    OffsetDateTime.now(clock).toString(), documentId, leadId,
                    safeOriginalName(document.getOriginalFilename()), contentType,
                    Long.toString(bytes.length), digest, relative
            ));
            stored++;
        }
        return stored;
    }

    public List<Map<String, String>> forLead(String leadId) {
        return csvStore.readAll(metadataPath).stream()
                .filter(row -> leadId.equals(row.get("lead_id")))
                .toList();
    }

    public StoredDocument require(String leadId, String documentId) {
        Map<String, String> row = forLead(leadId).stream()
                .filter(item -> documentId.equals(item.get("document_id")))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown document"));
        Path path = storageRoot.resolve(row.get("relative_path")).normalize();
        if (!path.startsWith(storageRoot) || Files.notExists(path)) throw new IllegalArgumentException("Unknown document");
        return new StoredDocument(path, row.get("original_name"), row.get("content_type"));
    }

    public String metadataCsv() { return csvStore.readRaw(metadataPath); }

    private static List<MultipartFile> present(List<MultipartFile> documents) {
        if (documents == null) return List.of();
        return documents.stream().filter(document -> document != null && !document.isEmpty()).toList();
    }

    private static byte[] read(MultipartFile document) {
        try (InputStream input = document.getInputStream()) {
            return input.readAllBytes();
        } catch (IOException exception) {
            throw new IllegalArgumentException("Could not read uploaded document", exception);
        }
    }

    private static String sha256(byte[] bytes) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes));
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }

    private static String normalizeType(String contentType) {
        return contentType == null ? "" : contentType.toLowerCase().split(";", 2)[0].trim();
    }

    private static boolean hasExpectedSignature(MultipartFile document, String contentType) {
        try (InputStream input = document.getInputStream()) {
            byte[] header = input.readNBytes(8);
            return switch (contentType) {
                case "application/pdf" -> startsWith(header, new byte[]{'%', 'P', 'D', 'F'});
                case "image/jpeg" -> startsWith(header, new byte[]{(byte) 0xff, (byte) 0xd8, (byte) 0xff});
                case "image/png" -> startsWith(header, new byte[]{(byte) 0x89, 'P', 'N', 'G', 0x0d, 0x0a, 0x1a, 0x0a});
                default -> false;
            };
        } catch (IOException exception) {
            throw new IllegalArgumentException("Could not inspect uploaded document", exception);
        }
    }

    private static boolean startsWith(byte[] value, byte[] prefix) {
        if (value.length < prefix.length) return false;
        for (int index = 0; index < prefix.length; index++) {
            if (value[index] != prefix[index]) return false;
        }
        return true;
    }

    private static String safeOriginalName(String name) {
        String base = name == null ? "document" : name.replace('\\', '/');
        base = base.substring(base.lastIndexOf('/') + 1).replaceAll("[\\p{Cntrl}]", "").trim();
        if (base.isBlank()) base = "document";
        return base.substring(0, Math.min(base.length(), 180));
    }

    public record StoredDocument(Path path, String originalName, String contentType) {}
}

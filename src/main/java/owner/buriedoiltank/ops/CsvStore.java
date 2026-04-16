package owner.buriedoiltank.ops;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CsvStore {
    public synchronized void ensureFile(Path path, List<String> headers) {
        try {
            Files.createDirectories(path.getParent());
            if (Files.notExists(path)) {
                Files.writeString(path, String.join(",", headers) + System.lineSeparator(), StandardCharsets.UTF_8);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to initialize CSV file " + path, exception);
        }
    }

    public synchronized void append(Path path, List<String> headers, List<String> values) {
        ensureFile(path, headers);
        if (headers.size() != values.size()) {
            throw new IllegalArgumentException("CSV header/value mismatch for " + path);
        }
        String line = values.stream().map(CsvStore::escape).reduce((left, right) -> left + "," + right).orElse("");
        try {
            Files.writeString(
                    path,
                    line + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.APPEND
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to append CSV file " + path, exception);
        }
    }

    public synchronized List<Map<String, String>> readAll(Path path) {
        if (Files.notExists(path)) {
            return List.of();
        }
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            if (lines.isEmpty()) {
                return List.of();
            }
            List<String> headers = split(lines.get(0));
            List<Map<String, String>> rows = new ArrayList<>();
            for (int i = 1; i < lines.size(); i++) {
                if (lines.get(i).isBlank()) {
                    continue;
                }
                List<String> values = split(lines.get(i));
                Map<String, String> row = new LinkedHashMap<>();
                for (int column = 0; column < headers.size(); column++) {
                    row.put(headers.get(column), column < values.size() ? values.get(column) : "");
                }
                rows.add(row);
            }
            return rows;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read CSV file " + path, exception);
        }
    }

    public synchronized String readRaw(Path path) {
        if (Files.notExists(path)) {
            return "";
        }
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read CSV file " + path, exception);
        }
    }

    private static String escape(String raw) {
        String sanitized = raw == null ? "" : raw.replace("\r", " ").replace("\n", " ").trim();
        if (!sanitized.isEmpty() && isSpreadsheetFormulaPrefix(sanitized.charAt(0))) {
            sanitized = "'" + sanitized;
        }
        if (sanitized.contains(",") || sanitized.contains("\"")) {
            return "\"" + sanitized.replace("\"", "\"\"") + "\"";
        }
        return sanitized;
    }

    private static boolean isSpreadsheetFormulaPrefix(char firstCharacter) {
        return firstCharacter == '='
                || firstCharacter == '+'
                || firstCharacter == '-'
                || firstCharacter == '@';
    }

    private static List<String> split(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (character == '"' && (i + 1) < line.length() && line.charAt(i + 1) == '"') {
                current.append('"');
                i++;
                continue;
            }
            if (character == '"') {
                inQuotes = !inQuotes;
                continue;
            }
            if (character == ',' && !inQuotes) {
                parts.add(current.toString());
                current = new StringBuilder();
                continue;
            }
            current.append(character);
        }
        parts.add(current.toString());
        return parts;
    }
}

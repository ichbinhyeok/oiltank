package owner.buriedoiltank.data;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Repository;

@Repository
public class ContentRepository {
    private final Map<String, StateRecord> statesBySlug;
    private final Map<String, GuideRecord> guidesBySlug;

    public ContentRepository(ObjectMapper objectMapper) {
        ObjectMapper mapper = objectMapper.copy()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.statesBySlug = indexStates(loadResources(mapper, "classpath:data/normalized/states/*.json", StateRecord.class));
        this.guidesBySlug = indexGuides(loadResources(mapper, "classpath:data/normalized/guides/*.json", GuideRecord.class));
    }

    public List<StateRecord> states() {
        return statesBySlug.values().stream()
                .sorted(Comparator.comparingInt(StateRecord::marketPriorityScore).reversed())
                .toList();
    }

    public StateRecord requireState(String slug) {
        StateRecord state = statesBySlug.get(slug);
        if (state == null) {
            throw new IllegalArgumentException("Unknown state slug: " + slug);
        }
        return state;
    }

    public List<GuideRecord> guides() {
        return guidesBySlug.values().stream()
                .sorted(Comparator.comparing(GuideRecord::title))
                .toList();
    }

    public GuideRecord requireGuide(String slug) {
        GuideRecord guide = guidesBySlug.get(slug);
        if (guide == null) {
            throw new IllegalArgumentException("Unknown guide slug: " + slug);
        }
        return guide;
    }

    private static Map<String, StateRecord> indexStates(List<StateRecord> records) {
        Map<String, StateRecord> indexed = new LinkedHashMap<>();
        for (StateRecord record : records) {
            indexed.put(record.slug(), record);
        }
        return indexed;
    }

    private static Map<String, GuideRecord> indexGuides(List<GuideRecord> records) {
        Map<String, GuideRecord> indexed = new LinkedHashMap<>();
        for (GuideRecord record : records) {
            indexed.put(record.slug(), record);
        }
        return indexed;
    }

    private static <T> List<T> loadResources(ObjectMapper mapper, String pattern, Class<T> type) {
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(pattern);
            Arrays.sort(resources, Comparator.comparing(Resource::getFilename));
            return Arrays.stream(resources)
                    .map(resource -> readResource(mapper, resource, type))
                    .toList();
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read resources for pattern " + pattern, exception);
        }
    }

    private static <T> T readResource(ObjectMapper mapper, Resource resource, Class<T> type) {
        try (InputStream inputStream = resource.getInputStream()) {
            return mapper.readValue(inputStream, type);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read resource " + resource.getDescription(), exception);
        }
    }
}

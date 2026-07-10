package owner.buriedoiltank.data;

import java.util.List;

public record RecordLookupSource(
        String stateSlug,
        String title,
        String agency,
        String url,
        List<String> searchBy,
        List<String> usefulFor,
        String caveat
) {
}

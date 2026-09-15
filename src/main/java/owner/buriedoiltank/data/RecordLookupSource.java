package owner.buriedoiltank.data;

import java.util.List;

public record RecordLookupSource(
        String stateSlug,
        String title,
        String agency,
        String jurisdiction,
        String sourceType,
        String url,
        List<String> searchBy,
        List<String> usefulFor,
        String caveat,
        List<String> identifiers,
        String requestMethod,
        String requestRequirements,
        String fee,
        String responseTime,
        String fallbackRoute,
        String verifiedOn
) {
}

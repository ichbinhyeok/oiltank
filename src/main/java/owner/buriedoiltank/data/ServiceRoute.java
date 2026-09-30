package owner.buriedoiltank.data;

import java.time.LocalDate;
import java.util.List;

public record ServiceRoute(
        String id,
        String title,
        String path,
        String scopeLabel,
        String sitemapPriority,
        LocalDate verifiedOn,
        LocalDate nextReviewOn
) {
    public static final List<ServiceRoute> CORE = List.of(
            route("service:home", "Find oil tank records and removal documents", "/", "Record finder home", "1.0"),
            route("service:how-it-works", "How oil tank record research works", "/how-it-works/", "Service workflow", "0.9"),
            route("service:record-research", "Property-specific oil tank record research", "/record-research/", "Research method", "0.9"),
            route("service:sample-brief", "Sample oil tank transaction brief", "/sample-brief/", "Sample brief", "0.9"),
            route("service:tools", "Residential heating-oil tools", "/tools/", "Utility acquisition hub", "0.7")
    );

    private static ServiceRoute route(String id, String title, String path, String scopeLabel, String priority) {
        return new ServiceRoute(
                id,
                title,
                path,
                scopeLabel,
                priority,
                id.equals("service:home") ? LocalDate.of(2026, 9, 30) : LocalDate.of(2026, 9, 14),
                LocalDate.of(2027, 3, 14)
        );
    }
}

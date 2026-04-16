package owner.buriedoiltank.data;

public enum SourceFreshnessStatus {
    FRESH("fresh"),
    STALE("stale");

    private final String slug;

    SourceFreshnessStatus(String slug) {
        this.slug = slug;
    }

    public String slug() {
        return slug;
    }
}

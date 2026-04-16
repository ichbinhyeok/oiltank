package owner.buriedoiltank.data;

public enum IndexStatus {
    INDEX("index"),
    NOINDEX("noindex");

    private final String slug;

    IndexStatus(String slug) {
        this.slug = slug;
    }

    public String slug() {
        return slug;
    }
}

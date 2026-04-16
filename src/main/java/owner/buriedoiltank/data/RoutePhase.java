package owner.buriedoiltank.data;

public enum RoutePhase {
    PHASE_1_PUBLIC("phase_1_public"),
    HELD_SUPPORT("held_support"),
    FUTURE_CANDIDATE("future_candidate");

    private final String slug;

    RoutePhase(String slug) {
        this.slug = slug;
    }

    public String slug() {
        return slug;
    }
}

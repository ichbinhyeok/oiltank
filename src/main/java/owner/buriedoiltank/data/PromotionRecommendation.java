package owner.buriedoiltank.data;

public enum PromotionRecommendation {
    HOLD("hold"),
    RECOMMEND_PROMOTE("recommend_promote"),
    RECOMMEND_BUILD("recommend_build"),
    RECOMMEND_DEMOTE("recommend_demote");

    private final String slug;

    PromotionRecommendation(String slug) {
        this.slug = slug;
    }

    public String slug() {
        return slug;
    }
}

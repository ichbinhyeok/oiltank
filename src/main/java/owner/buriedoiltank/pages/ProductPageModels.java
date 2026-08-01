package owner.buriedoiltank.pages;

import java.util.List;
import owner.buriedoiltank.tank.TankSpec;

public final class ProductPageModels {
    private ProductPageModels() {
    }

    public record ProductPageModel(
            PageModels.PageMeta meta,
            String pageId,
            String activeNav,
            String eyebrow,
            String heading,
            String intro,
            String toolKind,
            String factsHeading,
            List<String> facts,
            List<TankSpec> specs,
            String tankDataJson,
            List<FaqItem> faqs,
            List<SourceLink> sources,
            List<PageModels.LinkCard> nextLinks,
            List<PageModels.Breadcrumb> breadcrumbs
    ) {
        public boolean isTool(String kind) {
            return toolKind.equals(kind);
        }
    }

    public record FaqItem(String question, String answer) {
    }

    public record SourceLink(String title, String url, String note) {
    }
}

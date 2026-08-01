package owner.buriedoiltank.leads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LeadEventRequest {
    @NotBlank
    @Size(max = 64)
    private String eventType;
    @NotBlank
    @Size(max = 160)
    private String pageId;
    @NotBlank
    @Size(max = 512)
    private String pagePath;
    @Size(max = 80)
    private String stateSlug;
    @Size(max = 80)
    private String routeFamily;
    @NotBlank
    @Size(max = 64)
    private String scenario;
    @Size(max = 64)
    private String partnerType;
    @Size(max = 160)
    private String element;
    @Size(max = 1024)
    private String referrer;
    @Size(max = 80)
    private String toolId;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public String getStateSlug() {
        return stateSlug;
    }

    public void setStateSlug(String stateSlug) {
        this.stateSlug = stateSlug;
    }

    public String getRouteFamily() {
        return routeFamily;
    }

    public void setRouteFamily(String routeFamily) {
        this.routeFamily = routeFamily;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(String partnerType) {
        this.partnerType = partnerType;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
    }
}

package owner.buriedoiltank.leads;

import jakarta.validation.constraints.NotBlank;

public class LeadEventRequest {
    @NotBlank
    private String eventType;
    @NotBlank
    private String pageId;
    @NotBlank
    private String pagePath;
    private String stateSlug;
    private String routeFamily;
    @NotBlank
    private String scenario;
    private String partnerType;
    private String element;
    private String referrer;

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
}

package owner.buriedoiltank.leads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LeadCaptureRequest {
    @NotBlank
    @Size(max = 160)
    private String pageId;
    @NotBlank
    @Size(max = 512)
    private String pagePath;
    @NotBlank
    @Size(max = 80)
    private String stateSlug;
    @Size(max = 80)
    private String routeFamily;
    @NotBlank
    @Size(max = 64)
    private String scenario;
    @Size(max = 64)
    private String partnerType;
    @NotBlank
    @Size(max = 64)
    private String userRole;
    @NotBlank
    @Size(max = 80)
    private String tankStatus;
    @Size(max = 16)
    private String zipCode;
    @Size(max = 80)
    private String closingTimeline;
    @Size(max = 120)
    private String name;
    @NotBlank
    @Email
    @Size(max = 254)
    private String email;
    @Size(max = 40)
    private String phone;
    @Size(max = 2000)
    private String notes;
    @Size(max = 80)
    private String toolId;
    @Size(max = 80)
    private String tankType;
    @Size(max = 40)
    private String riskBand;
    @Size(max = 80)
    private String commercialIntent;
    @Size(max = 240)
    private String resultSummary;

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

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getTankStatus() {
        return tankStatus;
    }

    public void setTankStatus(String tankStatus) {
        this.tankStatus = tankStatus;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getClosingTimeline() {
        return closingTimeline;
    }

    public void setClosingTimeline(String closingTimeline) {
        this.closingTimeline = closingTimeline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getToolId() {
        return toolId;
    }

    public void setToolId(String toolId) {
        this.toolId = toolId;
    }

    public String getTankType() {
        return tankType;
    }

    public void setTankType(String tankType) {
        this.tankType = tankType;
    }

    public String getRiskBand() {
        return riskBand;
    }

    public void setRiskBand(String riskBand) {
        this.riskBand = riskBand;
    }

    public String getCommercialIntent() {
        return commercialIntent;
    }

    public void setCommercialIntent(String commercialIntent) {
        this.commercialIntent = commercialIntent;
    }

    public String getResultSummary() {
        return resultSummary;
    }

    public void setResultSummary(String resultSummary) {
        this.resultSummary = resultSummary;
    }
}

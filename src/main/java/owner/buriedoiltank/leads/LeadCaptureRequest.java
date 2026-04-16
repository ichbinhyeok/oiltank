package owner.buriedoiltank.leads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LeadCaptureRequest {
    @NotBlank
    private String pageId;
    @NotBlank
    private String pagePath;
    @NotBlank
    private String stateSlug;
    private String routeFamily;
    @NotBlank
    private String scenario;
    private String partnerType;
    @NotBlank
    private String userRole;
    @NotBlank
    private String tankStatus;
    private String zipCode;
    private String closingTimeline;
    private String name;
    @NotBlank
    @Email
    private String email;
    private String phone;
    private String notes;

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
}

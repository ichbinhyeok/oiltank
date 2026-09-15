package owner.buriedoiltank.leads;

import java.util.Arrays;

public enum CaseActivityType {
    INTAKE("intake", "Intake saved"),
    ONLINE_FINDING("online-finding", "Online finding"),
    AGENCY_REQUEST("agency-request", "Agency request"),
    AGENCY_REPLY("agency-reply", "Agency reply"),
    BOUNCE("bounce", "Bounce / failed route"),
    REFERRAL("referral", "Referral / new custodian"),
    CUSTOMER_UPDATE("customer-update", "Customer update"),
    DOCUMENT_RECEIVED("document-received", "Document received"),
    BRIEF_DELIVERED("brief-delivered", "Brief delivered"),
    ROUTE_LESSON("route-lesson", "Reusable route lesson"),
    STATUS_CHANGE("status-change", "Status change");

    private final String slug;
    private final String label;

    CaseActivityType(String slug, String label) {
        this.slug = slug;
        this.label = label;
    }

    public String slug() { return slug; }
    public String label() { return label; }

    public static CaseActivityType fromSlug(String value) {
        return Arrays.stream(values())
                .filter(type -> type.slug.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown case activity type"));
    }
}

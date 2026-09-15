package owner.buriedoiltank.leads;

import java.util.Arrays;

public enum CaseStatus {
    INTAKE("intake", "Intake"),
    RESEARCHING("researching", "Researching"),
    AGENCY_PENDING("agency-pending", "Agency pending"),
    WAITING_ON_CUSTOMER("waiting-on-customer", "Waiting on customer"),
    BRIEF_DELIVERED("brief-delivered", "Brief delivered"),
    CLOSED("closed", "Closed");

    private final String slug;
    private final String label;

    CaseStatus(String slug, String label) {
        this.slug = slug;
        this.label = label;
    }

    public String slug() { return slug; }
    public String label() { return label; }

    public static CaseStatus fromSlug(String value) {
        if (value == null || value.isBlank()) return INTAKE;
        return Arrays.stream(values())
                .filter(status -> status.slug.equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown case status"));
    }
}

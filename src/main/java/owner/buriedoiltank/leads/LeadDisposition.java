package owner.buriedoiltank.leads;

public enum LeadDisposition {
    PENDING,
    APPROVED,
    REJECTED;

    public String slug() {
        return name().toLowerCase();
    }

    public static LeadDisposition fromSlug(String value) {
        if (value == null || value.isBlank()) {
            return PENDING;
        }
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Unknown lead disposition: " + value, exception);
        }
    }
}

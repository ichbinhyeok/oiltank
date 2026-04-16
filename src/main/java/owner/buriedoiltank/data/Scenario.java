package owner.buriedoiltank.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum Scenario {
    BUYER_SELLER(
            "buyer_seller",
            "Buyer or seller risk",
            "Work out what documents and proof matter before price or removal talk.",
            PartnerType.SWEEP_LOCATE
    ),
    SWEEP_FIRST(
            "sweep_first",
            "Sweep first",
            "Use a locate or sweep when site clues and records conflict.",
            PartnerType.SWEEP_LOCATE
    ),
    RECORDS_FIRST(
            "records_first",
            "Records first",
            "Start with permits, closure paperwork, and prior fuel records before treating silence as proof.",
            PartnerType.SWEEP_LOCATE
    ),
    REMOVAL_DECISION(
            "removal_decision",
            "Removal or abandonment decision",
            "Move to closure or removal routing after the tank is confirmed and the state workflow is clear.",
            PartnerType.CLOSURE_REMOVAL
    ),
    LEAK_CONCERN(
            "leak_concern",
            "Leak or contamination concern",
            "Escalate early when staining, odors, or closure documents suggest a release path.",
            PartnerType.ENVIRONMENTAL_CLEANUP
    );

    private final String slug;
    private final String displayLabel;
    private final String helperText;
    private final PartnerType defaultPartnerType;

    Scenario(String slug, String displayLabel, String helperText, PartnerType defaultPartnerType) {
        this.slug = slug;
        this.displayLabel = displayLabel;
        this.helperText = helperText;
        this.defaultPartnerType = defaultPartnerType;
    }

    @JsonValue
    public String slug() {
        return slug;
    }

    public String displayLabel() {
        return displayLabel;
    }

    public String helperText() {
        return helperText;
    }

    public PartnerType defaultPartnerType() {
        return defaultPartnerType;
    }

    @JsonCreator
    public static Scenario fromSlug(String value) {
        return Arrays.stream(values())
                .filter(candidate -> candidate.slug.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown scenario: " + value));
    }
}

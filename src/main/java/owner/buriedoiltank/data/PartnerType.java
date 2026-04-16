package owner.buriedoiltank.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum PartnerType {
    SWEEP_LOCATE(
            "sweep_or_locate",
            "Tank sweep or locate specialist",
            "Start with locate or sweep work when records and physical clues do not line up."
    ),
    CLOSURE_REMOVAL(
            "closure_or_removal",
            "Closure or removal contractor",
            "Use a closure or removal contractor when the tank is confirmed and the next choice is disposition."
    ),
    ENVIRONMENTAL_CLEANUP(
            "environmental_cleanup",
            "Environmental consultant or cleanup specialist",
            "Use an environmental specialist when a suspected leak or spill moves beyond ordinary contractor scope."
    );

    private final String slug;
    private final String displayLabel;
    private final String helperText;

    PartnerType(String slug, String displayLabel, String helperText) {
        this.slug = slug;
        this.displayLabel = displayLabel;
        this.helperText = helperText;
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

    @JsonCreator
    public static PartnerType fromSlug(String value) {
        return Arrays.stream(values())
                .filter(candidate -> candidate.slug.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown partner type: " + value));
    }
}

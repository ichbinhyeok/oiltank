package owner.buriedoiltank.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.List;

public enum RouteFamily {
    OVERVIEW(
            "overview",
            "",
            "Overview",
            RoutePhase.PHASE_1_PUBLIC,
            IndexStatus.INDEX,
            Scenario.BUYER_SELLER,
            PartnerType.SWEEP_LOCATE
    ),
    BUYER_SELLER(
            "buyer-seller",
            "buyer-seller",
            "Buyer and seller",
            RoutePhase.PHASE_1_PUBLIC,
            IndexStatus.INDEX,
            Scenario.BUYER_SELLER,
            PartnerType.SWEEP_LOCATE
    ),
    SWEEP_AND_LOCATE(
            "sweep-and-locate",
            "sweep-and-locate",
            "Sweep and locate",
            RoutePhase.PHASE_1_PUBLIC,
            IndexStatus.INDEX,
            Scenario.SWEEP_FIRST,
            PartnerType.SWEEP_LOCATE
    ),
    RECORDS_AND_PROOF(
            "records-and-proof",
            "records-and-proof",
            "Records and proof",
            RoutePhase.PHASE_1_PUBLIC,
            IndexStatus.INDEX,
            Scenario.RECORDS_FIRST,
            PartnerType.SWEEP_LOCATE
    ),
    REMOVAL_VS_ABANDONMENT(
            "removal-vs-abandonment",
            "removal-vs-abandonment",
            "Removal vs abandonment",
            RoutePhase.HELD_SUPPORT,
            IndexStatus.NOINDEX,
            Scenario.REMOVAL_DECISION,
            PartnerType.CLOSURE_REMOVAL
    ),
    LEAK_AND_CLEANUP(
            "leak-and-cleanup",
            "leak-and-cleanup",
            "Leak and cleanup",
            RoutePhase.HELD_SUPPORT,
            IndexStatus.NOINDEX,
            Scenario.LEAK_CONCERN,
            PartnerType.ENVIRONMENTAL_CLEANUP
    ),
    COST_DIRECTION(
            "cost-direction",
            "cost-direction",
            "Cost direction",
            RoutePhase.HELD_SUPPORT,
            IndexStatus.NOINDEX,
            Scenario.REMOVAL_DECISION,
            PartnerType.CLOSURE_REMOVAL
    );

    private final String slug;
    private final String pathSegment;
    private final String displayLabel;
    private final RoutePhase phase;
    private final IndexStatus defaultIndexStatus;
    private final Scenario defaultScenario;
    private final PartnerType defaultPartnerType;

    RouteFamily(
            String slug,
            String pathSegment,
            String displayLabel,
            RoutePhase phase,
            IndexStatus defaultIndexStatus,
            Scenario defaultScenario,
            PartnerType defaultPartnerType
    ) {
        this.slug = slug;
        this.pathSegment = pathSegment;
        this.displayLabel = displayLabel;
        this.phase = phase;
        this.defaultIndexStatus = defaultIndexStatus;
        this.defaultScenario = defaultScenario;
        this.defaultPartnerType = defaultPartnerType;
    }

    @JsonValue
    public String slug() {
        return slug;
    }

    public String pathSegment() {
        return pathSegment;
    }

    public String displayLabel() {
        return displayLabel;
    }

    public RoutePhase phase() {
        return phase;
    }

    public IndexStatus defaultIndexStatus() {
        return defaultIndexStatus;
    }

    public Scenario defaultScenario() {
        return defaultScenario;
    }

    public PartnerType defaultPartnerType() {
        return defaultPartnerType;
    }

    public boolean isOverview() {
        return this == OVERVIEW;
    }

    public String pathForState(String stateSlug) {
        if (isOverview()) {
            return "/states/" + stateSlug + "/";
        }
        return "/states/" + stateSlug + "/" + pathSegment + "/";
    }

    public List<RouteFamily> likelyNextSteps() {
        return switch (this) {
            case OVERVIEW -> List.of(BUYER_SELLER, SWEEP_AND_LOCATE, RECORDS_AND_PROOF);
            case BUYER_SELLER -> List.of(SWEEP_AND_LOCATE, RECORDS_AND_PROOF, COST_DIRECTION);
            case SWEEP_AND_LOCATE -> List.of(RECORDS_AND_PROOF, REMOVAL_VS_ABANDONMENT, LEAK_AND_CLEANUP);
            case RECORDS_AND_PROOF -> List.of(SWEEP_AND_LOCATE, BUYER_SELLER, REMOVAL_VS_ABANDONMENT);
            case REMOVAL_VS_ABANDONMENT -> List.of(LEAK_AND_CLEANUP, COST_DIRECTION, RECORDS_AND_PROOF);
            case LEAK_AND_CLEANUP -> List.of(RECORDS_AND_PROOF, REMOVAL_VS_ABANDONMENT, COST_DIRECTION);
            case COST_DIRECTION -> List.of(RECORDS_AND_PROOF, SWEEP_AND_LOCATE, BUYER_SELLER);
        };
    }

    @JsonCreator
    public static RouteFamily fromSlug(String value) {
        return Arrays.stream(values())
                .filter(candidate -> candidate.slug.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown route family: " + value));
    }

    public static RouteFamily fromPathSegment(String value) {
        return Arrays.stream(values())
                .filter(candidate -> candidate.pathSegment.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown route path segment: " + value));
    }
}

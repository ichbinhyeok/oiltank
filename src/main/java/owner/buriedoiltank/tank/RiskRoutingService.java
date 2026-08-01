package owner.buriedoiltank.tank;

import org.springframework.stereotype.Service;

@Service
public class RiskRoutingService {
    public Outcome route(Input input) {
        if (input.visibleLeak() || input.wetSoil() || input.oilOdor()) {
            return new Outcome(
                    RiskBand.URGENT,
                    "Stop and protect people and property",
                    "Do not touch, drain, or test the tank. Keep ignition sources away, ventilate only if it is safe, and contact the local fire department or environmental spill line when an active release is possible.",
                    "leak_concern",
                    "environmental_cleanup",
                    "Find leak or remediation help"
            );
        }
        if (input.homeSale() && (input.undergroundTank() || input.tankLocationUnknown())) {
            return new Outcome(
                    RiskBand.TRANSACTION,
                    "Verify the tank before the deal moves",
                    "Keep the question inside due diligence. Start with records and a qualified tank sweep or locate; move to removal only after the tank and state path are confirmed.",
                    "sweep_first",
                    "sweep_or_locate",
                    "Find a sweep or removal evaluation"
            );
        }
        if (input.ageYears() >= 20 || input.visibleRust() || input.indoorSeepage()) {
            return new Outcome(
                    RiskBand.INSPECTION,
                    "Plan an inspection or replacement review",
                    "Age, corrosion, or indoor seepage deserves a qualified heating-oil tank inspection. A replacement contractor can confirm condition and code requirements before work is priced.",
                    "removal_decision",
                    "closure_or_removal",
                    "Find inspection or replacement help"
            );
        }
        return new Outcome(
                RiskBand.INFORMATION,
                "Monitor and keep the tank documented",
                "No commercial trigger is indicated by these answers. Keep checking the gauge, protect the fill and vent, and schedule routine service with your heating provider.",
                null,
                null,
                null
        );
    }

    public record Input(
            int ageYears,
            boolean visibleRust,
            boolean indoorSeepage,
            boolean oilOdor,
            boolean wetSoil,
            boolean visibleLeak,
            boolean homeSale,
            boolean undergroundTank,
            boolean tankLocationUnknown
    ) {
    }

    public record Outcome(
            RiskBand riskBand,
            String heading,
            String explanation,
            String scenario,
            String partnerType,
            String ctaLabel
    ) {
        public boolean commercial() {
            return scenario != null;
        }
    }

    public enum RiskBand {
        INFORMATION,
        INSPECTION,
        TRANSACTION,
        URGENT
    }
}

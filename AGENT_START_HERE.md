# Agent Start Here

## Project
BuriedOilTankVerdict

## Current implementation direction
- Preferred stack: `Spring Boot` + `jte`
- Preferred storage: raw `CSV` and `JSON` source files plus normalized and derived `JSON`
- No runtime database in phase 1
- The primary product is a property-specific oil tank record research and transaction brief service; the 20 heating-oil utilities are the supporting acquisition library at `/tools/`
- The app runs on `Spring Boot` with file-backed content, persistent route intelligence, research intake, event logging, ops snapshots, and deployable precompiled `jte` templates

## What this folder contains
- A self-contained product and implementation packet for a state-first buried or abandoned residential heating oil tank decision site
- Enough context for a new agent to start implementation without chat history
- A working server-rendered implementation with route inventory, lead capture, event logging, admin review, and export surfaces

## Read order
1. `ops/context_tracker.md`
2. `spec/11_record_research_operating_model.md`
3. `ops/route_intelligence.md`
4. `ops/wedge_focus_2026-04-13.md`
5. `ops/source_audit_2026-04-13.md`
6. `ops/persona_council_2026-04-13.md`
7. `ops/promotion_review_system_2026-04-13.md`
8. `ops/route_promotion_board.md`
9. `README.md`
10. `spec/00_strategy.md`
11. `spec/01_query_and_user_map.md`
12. `spec/02_site_architecture.md`
13. `spec/03_data_and_operations.md`
14. `spec/04_commercial_model.md`
15. `spec/05_editorial_rules_and_execution.md`
16. `spec/06_indexing_quality_and_analytics.md`
17. `spec/07_technical_architecture.md`
18. `spec/08_delivery_and_handoff.md`
19. `spec/09_launch_surface_and_route_inventory.md`
20. `spec/10_acceptance_test_matrix.md`

## Rules for any future agent
- The core business is property-specific research: parcel resolution, public-record search, agency routing, document interpretation, and a plain-English transaction brief.
- The primary conversion is `Start a property record check`; do not substitute a generic contact or removal-lead CTA.
- The service canonical URLs are `/`, `/how-it-works/`, `/record-research/`, and `/sample-brief/`. State records routes support jurisdiction-specific acquisition.
- Existing utility canonicals stay distinct while they answer distinct calculations or model-specific questions. Consolidation requires evidence of true duplicate intent, not merely similar keywords.
- Detailed intake data is operational PII. GA4 events may include route, state, role, question family, and qualification state, but never address, email, free text, or document contents.
- The founder-led beta starts with NJ and NY route intelligence. Update the persistent route-intelligence file as cases reveal actual custodians, identifiers, fees, timing, and fallback paths.
- The legacy supporting state-content cohort is `NJ`, `NY`, `CT`, and `ME`; founder-led record-research route intelligence starts with `NJ` and `NY`. `MA` stays reserve until source depth is stronger.
- State hubs should foreground only the three core public routes. Advanced support routes stay contextual until evidence justifies widening the visible surface.
- `removal versus abandon` and `leak or contamination` routes are support-layer until the first wedge proves traction.
- If analytics or admin metrics exist, start every review session by checking `ops/route_promotion_board.md`.
- Future agents must produce a user-facing `promotion recommendation` summary when any held route has enough evidence.
- Future agents may recommend promotion, but should not silently widen the public index surface without surfacing the recommendation first.
- This is not a generic home-heating site.
- This is not a broad environmental education site.
- Official state environmental, remediation, and buyer-seller guidance outrank every secondary source.
- State pages come first. County and city pages only exist when they materially change the answer or the route has clear commercial value.
- If strategy changes, update the relevant spec file and `ops/context_tracker.md`.

## Minimum handoff standard
- Update `Current status`
- Update `Latest decisions`
- Update `What changed this session`
- Update `Next recommended tasks`
- Update `Open questions`
- Update `ops/route_promotion_board.md` if any route recommendation status changes

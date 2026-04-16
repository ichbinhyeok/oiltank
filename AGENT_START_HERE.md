# Agent Start Here

## Project
BuriedOilTankVerdict

## Current implementation direction
- Preferred stack: `Spring Boot` + `jte`
- Preferred storage: raw `CSV` and `JSON` source files plus normalized and derived `JSON`
- No runtime database in phase 1
- The app scaffold exists and runs locally on `Spring Boot` with file-backed content, ops snapshots, and deployable precompiled `jte` templates

## What this folder contains
- A self-contained product and implementation packet for a state-first buried or abandoned residential heating oil tank decision site
- Enough context for a new agent to start implementation without chat history
- A working server-rendered implementation with route inventory, lead capture, event logging, admin review, and export surfaces

## Read order
1. `ops/context_tracker.md`
2. `ops/wedge_focus_2026-04-13.md`
3. `ops/source_audit_2026-04-13.md`
4. `ops/persona_council_2026-04-13.md`
5. `ops/promotion_review_system_2026-04-13.md`
6. `ops/route_promotion_board.md`
7. `README.md`
8. `spec/00_strategy.md`
9. `spec/01_query_and_user_map.md`
10. `spec/02_site_architecture.md`
11. `spec/03_data_and_operations.md`
12. `spec/04_commercial_model.md`
13. `spec/05_editorial_rules_and_execution.md`
14. `spec/06_indexing_quality_and_analytics.md`
15. `spec/07_technical_architecture.md`
16. `spec/08_delivery_and_handoff.md`
17. `spec/09_launch_surface_and_route_inventory.md`
18. `spec/10_acceptance_test_matrix.md`

## Rules for any future agent
- The canonical SEO unit is `state + transaction-stage route`, not a generic national oil-tank article.
- The primary wedge is post-trigger:
  - buried tank found or suspected
  - missing records during sale
- Phase 1 public wedge is narrower:
  - buyer-seller risk
  - sweep first
  - records first
- Public launch cohort is `NJ`, `NY`, `CT`, and `ME`. `MA` stays reserve until source depth is stronger.
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

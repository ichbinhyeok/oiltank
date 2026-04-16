# Context Tracker

## Current status
- Design packet created.
- Wedge locked around buried or abandoned residential heating oil tank triggers during sale, inspection, or records review.
- Spring Boot plus `jte` application scaffold created under `owner.buriedoiltank`.
- File-backed normalized state and guide data now load from `src/main/resources/data/normalized`.
- Runtime route inventory, public rendering, lead capture, event logging, robots, sitemap, and admin review surface are implemented.
- Runtime ops snapshots now persist `route-status.csv`, `promotion-review.json`, `admin-metrics-snapshot.json`, and derived `routes.json`.
- Admin export endpoints now expose current route manifest and ops snapshots under `/admin/exports/*`.
- Source freshness review now persists as a first-class ops artifact with scope-level due dates, blocked-route counts, and source titles.
- Launch-cohort state hubs and guide pages now carry richer transaction copy with state-specific trigger, first-move, document-target, and escalation sections.
- Integration tests cover rendering, metadata, sitemap and robots rules, lead capture, event logging, and admin visibility.

## Latest decisions
- Canonical page unit is `state + transaction-stage route`.
- Public launch cohort is `NJ`, `NY`, `CT`, and `ME`.
- `MA` stays in reserve until source depth is stronger and should not ship as an indexable state in the first public cohort.
- The primary wedge is not generic tank ownership. It is `home sale + buried tank suspicion + missing records + next action`.
- Phase 1 public focus is narrower still: `buyer-seller risk + sweep first + records first`.
- Initial monetization should prioritize tank-sweep and records-checklist routing first, with closure or removal routing following once confirmed-tank paths prove traction.
- County or city overlays should only ship when official process or commercial value meaningfully changes the answer.
- Runtime-derived `storage/derived/routes.json` is sufficient for phase 1. Do not check in a parallel repo-level derived artifact yet.
- Packaged deploys should use generated `jte` template classes rather than runtime template compilation.
- Keep public-facing copy close to `oil tank` language for phase 1 query clarity. Use `tank sweep` and `records` phrasing inside route and CTA copy, not as a full brand shift.

## What changed this session
- Created the `BuriedOilTankVerdict` design packet under `C:\Development\Owner\BuriedOilTankVerdict`.
- Wrote strategy, query map, architecture, data, commercial, editorial, indexing, technical, launch-surface, and acceptance docs.
- Locked the product direction around post-trigger decision support rather than broad heating-oil education.
- Tightened the launch wedge so Phase 1 behaves like a home-sale tank-risk engine, not a full oil-tank category site.
- Added an agent-driven promotion review system so future sessions check metrics and recommend held-route promotion without relying on human memory.
- Scaffolded the app with Spring Boot `3.5.6` plus `jte`.
- Added five launch-state records, six evergreen guide records, and raw seed files for states, guides, partners, costs, markets, and source anchors.
- Implemented home, trust, state hub, state route, guide, robots, sitemap, and admin surfaces.
- Implemented file-backed lead capture and event logging under `storage/leads`.
- Added MockMvc integration coverage for rendering, metadata, lead capture, event logging, and admin review.
- Added persisted ops snapshot generation under `storage/ops` and `storage/derived`.
- Added export endpoints for route manifest, route status, promotion review, and admin metrics snapshots.
- Extended tests to verify exports and persisted snapshot files.
- Added a source freshness review snapshot and admin queue so stale scope blockers are visible by state or guide, not just as a generic stale label.
- Expanded the five launch-state hubs and six guide records with richer operational copy, and updated templates so the new sections render on state, guide, and route pages.
- Moved `MA` out of the first public cohort in code and docs while keeping it available as reserve inventory.
- Updated the build so packaged `java -jar` deploys render `jte` views correctly with generated template classes.
- Tightened state hubs so the public front door only foregrounds buyer-seller, sweep, and records routes while advanced support routes stay contextual.
- Synthesized the persona-council conclusions into the home and methodology surfaces so the product now foregrounds hot trigger states, verify-route-escalate flow, and suspected-versus-confirmed-versus-leak separation.

## Next recommended tasks
- Add visual QA across mobile and desktop once browser tooling or permissions are available.
- Decide whether early local overlays should start with `NJ` counties once post-launch demand is visible.

## Open questions
- Should early local pages be county-first in `NJ` or remain state-first until demand data appears?

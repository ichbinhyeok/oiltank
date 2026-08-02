# Context Tracker

## Current status
- The 2026-08-01 full pivot to `Oil Tank Route` is implemented: the public front door is now a residential heating-oil tank utility, while existing state and buried-tank guides remain supporting resources.
- Nine indexable core product routes now share a reviewed `TankSpec` catalog, shape calculation service, verified gauge-chart interpolation, delivery cross-check, local Tank Passport, and risk-routing rules. They are first-class records in the route manifest and admin performance table.
- The Modern Field Instrument JTE layout, self-hosted licensed fonts, tank-section SVG language, mobile-first public CSS, and dependency-free ES-module tools are live.
- Lead and event CSV storage now migrates expanded headers with a timestamped backup and accepts optional tool/risk/commercial context.
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
- The EIA price snapshot requires its next review on 2026-10-07; follow `ops/heating_oil_price_refresh.md`, then review weekly during the October-March heating season.
- The 21-query U.S. Google Ads validation is complete. The combined central overlap-adjusted denominator is 107,772 searches/month, equal to 107.8 clicks/day at the owner's 3% blended CTR assumption; the 50% overlap sensitivity is 82.9/day.

## Latest decisions
- Brand is `Oil Tank Route`; the product boundary is residential heating-oil tanks, not the broad industrial UST market.
- Utility results are ungated. Normal gauge/fuel and low-risk planner results do not display removal lead capture.
- Odor/wet soil/visible oil routes to leak/remediation; age/rust/indoor seepage routes to inspection/replacement; home-sale underground/unknown routes to sweep/removal evaluation.
- General Sans, Source Sans 3, and IBM Plex Mono are self-hosted with license notices; public dark mode is not part of v1.
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
- Added `DESIGN.md`, design tokens, self-hosted WOFF2 assets, license notices, and a complete public CSS/JTE layout rebuild.
- Added the `TankSpec` catalog, obround/cylinder/rectangular capacity formulas, and Granby 138-, 275-, and 330-gallon vertical gauge-chart interpolation from the official U.S. capacity chart.
- Added the nine core URLs, SSR metadata/JSON-LD/canonicals, sessionStorage handoff, privacy-safe tool analytics, and conditional lead routing.
- Expanded lead CSV context and added safe header migration with timestamped backups. Lead approval/rejection and payout are recorded in an append-only disposition log and surfaced against the 40-lead/$1,000 target.
- Removed duplicated tank/chart values from browser JavaScript; JTE now emits the Java catalog as page-scoped JSON consumed by the dependency-free ES module.
- Added the core URLs to the top of the sitemap and retained existing state/guide URLs as resource inventory.
- Replaced public stock photography with tank-section and measurement SVGs.
- Added calculation, monotonic interpolation, risk combination, CSV migration, route metadata, and regression tests.
- Completed browser QA at 360, 768, and 1440 pixels across the core routes, including keyboard entry, JS-disabled content, tool flows, conditional CTA behavior, and performance checks.
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
- Use `analysis/price_delivery_volume_assessment_2026-08-02.md` as the current SEO denominator. Prioritize the national price hub and a credible delivery workflow; do not turn state, ZIP, city, cheap-oil, or cash-oil modifiers into thin pages.
- Track the price/delivery cohort separately in GSC. Replace the 3% model only after it has enough impressions to measure query-weighted CTR, and keep 100/day as a target rather than a forecast until rankings support it.
- On or after 2026-10-07, run the EIA price refresh checklist in `ops/heating_oil_price_refresh.md`. Keep the last verified snapshot if the expected official release is unavailable.
- Deploy behind the existing canonical host and verify production Core Web Vitals and event ingestion.
- Add additional manufacturer models only after a current official dimension table and compatible gauge chart are reviewed.
- Decide whether early local overlays should start with `NJ` counties once post-launch demand is visible.

## Open questions
- Which accepted-lead buyer will explicitly accept replacement/HVAC intent in addition to removal?
- Should early local pages remain state-first until the new utility routes produce live GSC demand?

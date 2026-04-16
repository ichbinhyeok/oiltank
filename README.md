# Buried Oil Tank Verdict

Working internal project: `BuriedOilTankVerdict`  
Suggested package root: `owner.buriedoiltank`

**Date:** 2026-04-13 (Asia/Seoul)  
**Purpose:** This folder now contains both the design packet and a working US-focused **buried or abandoned residential heating oil tank decision site** centered on home-sale, inspection, records, and next-action workflows.

## What you are building
A state-first decision site for buyers, sellers, homeowners, and agents who already have a buried-tank trigger:

- tank suspected before closing
- old fill pipe or vent discovered
- records missing
- remove versus abandon decision needed
- leak or contamination concern

The product should tell the user what the likely next step is, what the state process actually says, what evidence matters, how cost direction changes by scenario, and whether they need a tank sweep, closure contractor, or environmental cleanup path first.

## Phase 1 launch wedge
Phase 1 is narrower than the full category.

The launch wedge is:

- buyer or seller in a live or near-term transaction
- buried tank suspected or records missing
- need to know whether to get a sweep before closing

This means the first public build should behave more like:

- `home-sale tank risk decision engine`

and less like:

- `complete oil tank knowledge base`

## Why this concept is attractive
- The trigger is urgent and commercial.
- Search intent is transaction-heavy, not broad informational curiosity.
- State and program variance is real enough to create a durable page system.
- Revenue can start with tank sweep and contractor routing before any large sponsor marketplace exists.
- Compared with many home-service topics, each qualified lead can be worth meaningful money even at low traffic.

## Product thesis
Do not build `heating oil tank information`.

Build a **post-trigger transaction and remediation decision engine** for people trying to answer:

- Is there really a buried tank here?
- What records do I need before I buy or sell?
- Remove or abandon in place?
- Is a leak likely?
- Which professional should I call first?

## File map
- `AGENT_START_HERE.md` - read order and handoff rules for any future agent
- `ops/context_tracker.md` - current status, decisions, and next tasks
- `ops/wedge_focus_2026-04-13.md` - current primary wedge and the narrow operating loop for the first build phase
- `ops/source_audit_2026-04-13.md` - official-source anchor map and how each source should shape the product
- `ops/persona_council_2026-04-13.md` - forced debate across demand, SERP, funnel, risk, and sponsor perspectives
- `ops/promotion_review_system_2026-04-13.md` - how future agents should review metrics and recommend route promotion
- `ops/route_promotion_board.md` - current held-route board and recommendation status
- `spec/00_strategy.md` - market thesis, positioning, wedge, and rollout philosophy
- `spec/01_query_and_user_map.md` - jobs-to-be-done, trigger states, query families, and first user map
- `spec/02_site_architecture.md` - canonical entities, URL graph, route families, and internal linking
- `spec/03_data_and_operations.md` - data model, source hierarchy, verification workflow, and refresh cadence
- `spec/04_commercial_model.md` - CTA logic, partner types, lead intake, and sponsor packaging
- `spec/05_editorial_rules_and_execution.md` - writing rules, trust guardrails, and page-family ship criteria
- `spec/06_indexing_quality_and_analytics.md` - indexing gates, route quality rules, and measurement plan
- `spec/07_technical_architecture.md` - system boundaries, package map, rendering model, and services
- `spec/08_delivery_and_handoff.md` - workstreams, milestones, and implementation order
- `spec/09_launch_surface_and_route_inventory.md` - first launch-surface page inventory
- `spec/10_acceptance_test_matrix.md` - launch-critical tests and definition of done

## Recommended build stack
- `Spring Boot` + `jte`
- Server-rendered state and guide pages with file-backed content
- File-based pipeline using raw `CSV` plus normalized and derived `JSON`
- No runtime database in phase 1
- Java runtime baseline: `21`

## Current implementation state
- Spring Boot plus `jte` application scaffold is live under `owner.buriedoiltank`
- Runtime route inventory is generated from normalized state and guide records
- Lead capture and event logging persist to `storage/leads`
- Ops snapshots persist to `storage/ops` and `storage/derived`
- Admin exports are available under `/admin/exports/*`
- Packaged runtime now uses generated `jte` template classes, so `java -jar` is deployable without template recompilation at runtime

## Production persistence notes
- Lead submissions are stored in `leads.csv`
- CTA and lead funnel events are stored in `lead_events.csv`
- Admin shows aggregate metrics and exports raw CSV plus JSON snapshots under `/admin/exports/*`
- On production deploys, do not keep `buried-oil-tank.storage-root` inside the release directory
- For Oracle VM plus GitHub Actions deploys, point storage to a stable path such as `/var/lib/buried-oil-tank-verdict`
- The app now supports this through `BURIED_OIL_TANK_STORAGE_ROOT` and a `prod` profile default

Example production environment:

```bash
export SPRING_PROFILES_ACTIVE=prod
export BURIED_OIL_TANK_BASE_URL=https://your-domain.example
export BURIED_OIL_TANK_STORAGE_ROOT=/var/lib/buried-oil-tank-verdict
export BURIED_OIL_TANK_ADMIN_USERNAME=admin
export BURIED_OIL_TANK_ADMIN_PASSWORD='replace-this'
```

Example systemd service fragment:

```ini
[Service]
WorkingDirectory=/opt/buried-oil-tank-verdict/current
Environment=SPRING_PROFILES_ACTIVE=prod
Environment=BURIED_OIL_TANK_BASE_URL=https://your-domain.example
Environment=BURIED_OIL_TANK_STORAGE_ROOT=/var/lib/buried-oil-tank-verdict
Environment=BURIED_OIL_TANK_ADMIN_USERNAME=admin
Environment=BURIED_OIL_TANK_ADMIN_PASSWORD=replace-this
ExecStart=/usr/bin/java -jar /opt/buried-oil-tank-verdict/current/target/buried-oil-tank-verdict-0.0.1-SNAPSHOT.jar
Restart=always
```

## Recommended launch cohort
Start with four public states where the topic is both real and monetizable:

- `New Jersey`
- `New York`
- `Connecticut`
- `Maine`

Keep `Massachusetts` in launch reserve until the source depth is strong enough to justify indexable transaction pages.

Reason:

- heating-oil market density remains meaningful in the Northeast
- official process or cleanup guidance exists
- property-sale and record uncertainty are real search triggers
- sponsor and lead paths are commercially plausible

## Core route families
- state hub
- state buyer-seller guide
- state tank sweep guide
- state records and disclosure guide
- state remove versus abandon guide
- state leak and reporting guide
- state cost direction guide
- evergreen national trust guides

## Phase 1 launch families
- state hub
- state buyer-seller guide
- state tank sweep guide
- state records and disclosure guide
- evergreen guides tied to home sale and missing records

Everything else should start as support-layer or `noindex` inventory until the first wedge proves traction.
State hubs should foreground only the three core public routes so the front door stays narrow and transaction-first.

## Recommended monetization order
1. Tank sweep / locate leads
2. Certified closure or removal contractor leads
3. Environmental consultant or spill-remediation leads
4. Select state sponsors once route-level demand is proven

## Agent read order
1. `AGENT_START_HERE.md`
2. `ops/context_tracker.md`
3. `ops/persona_council_2026-04-13.md`
4. This file
5. `spec/00_strategy.md` through `spec/10_acceptance_test_matrix.md`

## Build principles
- This is a transaction and remediation product, not a heating-oil blog.
- Home-sale and missing-record routes are the first wedge.
- State pages are canonical. County overlays are selective, not default.
- Every page must answer:
  - what this probably means
  - what to verify next
  - who to call first
  - what mistake to avoid
- Official guidance and commercial routing must stay visibly separate.
- Broad cost pages do not ship unless they are tied to a concrete trigger and evidence stack.

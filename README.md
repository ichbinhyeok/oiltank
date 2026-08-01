# Oil Tank Route

Oil Tank Route is a server-rendered residential heating-oil tank utility and commercial-routing product. The August 2026 pivot replaces the old buried-tank blog front door with shared tank identification, verified size data, gauge interpolation, delivery-ticket cross-checking, private tank history, shape-based capacity estimation, and condition/transaction routing. Existing state and buried-tank guides remain available as supporting resources.

Business target: 40 approved leads per month at $25 each ($1,000/month), with 100 organic clicks/day treated as the success ceiling rather than the base forecast.

Working internal project: `BuriedOilTankVerdict`  
Suggested package root: `owner.buriedoiltank`

**Date:** 2026-08-02 (Asia/Seoul)
**Purpose:** Working U.S.-focused **residential heating-oil tank field utility** with a supporting buried/unknown-tank transaction resource library.

## Current product routes

- `/heating-oil-tank/` - identification start
- `/heating-oil-tank-sizes-dimensions/` - one verified size comparison
- `/275-gallon-oil-tank/` - 275-gallon model and chart distinctions
- `/heating-oil-tank-charts/` - official manufacturer chart library
- `/oil-tank-gauge-calculator/` - chart-interpolated gauge/stick result
- `/heating-oil-delivery-check/` - before/after chart range, ticket comparison, and local Tank Passport
- `/heating-oil-usage-calculator/` - household-rate fuel runway
- `/oil-tank-capacity-calculator/` - shape formula and measurement range
- `/heating-oil-tank-sludge-cleaning/` - service, corrosion, and possible-release router
- `/oil-tank-replacement-planner/` - information, inspection, transaction, or urgent routing
- `/oil-tank-replacement-cost/` - replacement quote-scope comparator
- `/heating-oil-tank-installation-cost/` - installation quote-scope comparator
- `/basement-oil-tank-removal/` - routine indoor removal scope versus release response

All public pages use JTE SSR. Calculator behavior is dependency-free ES module JavaScript, and results never require an email. See `DESIGN.md` for the Modern Field Instrument system.

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
- Runtime route inventory contains 54 records: 41 supporting state/guide records plus 13 first-class product routes
- Lead capture and event logging persist to `storage/leads`
- Approval/rejection decisions and payout cents use an append-only audit log, leaving original lead rows unchanged
- The admin dashboard reports progress toward 40 approved leads and $1,000 in 28 days, plus per-tool start-to-lead funnels
- Ops snapshots persist to `storage/ops` and `storage/derived`
- Admin exports are available under `/admin/exports/*`
- Packaged runtime now uses generated `jte` template classes, so `java -jar` is deployable without template recompilation at runtime

## Production persistence notes
- Lead submissions are stored in `leads.csv`
- CTA and lead funnel events are stored in `lead_events.csv`
- Lead approval, rejection, payout, and decision notes are stored in `lead-dispositions.csv`
- Admin shows aggregate metrics and exports raw CSV plus JSON snapshots under `/admin/exports/*`
- Gauge calculations read the same server-rendered `TankSpec` data used by the SSR tables; verified Granby 138-, 275-, and 330-gallon vertical charts are supported
- On production deploys, do not keep `buried-oil-tank.storage-root` inside the release directory
- For Oracle VM plus GitHub Actions deploys, point storage to a stable path such as `/var/lib/buried-oil-tank-verdict`
- The app now supports this through `BURIED_OIL_TANK_STORAGE_ROOT` and a `prod` profile default

Example production environment:

```bash
export SPRING_PROFILES_ACTIVE=prod
export BURIED_OIL_TANK_BASE_URL=https://oiltankroute.com
export BURIED_OIL_TANK_STORAGE_ROOT=/var/lib/buried-oil-tank-verdict
export BURIED_OIL_TANK_ADMIN_USERNAME=admin
export BURIED_OIL_TANK_ADMIN_PASSWORD='replace-this'
```

Example systemd service fragment:

```ini
[Service]
WorkingDirectory=/opt/buried-oil-tank-verdict/current
Environment=SPRING_PROFILES_ACTIVE=prod
Environment=BURIED_OIL_TANK_BASE_URL=https://oiltankroute.com
Environment=BURIED_OIL_TANK_STORAGE_ROOT=/var/lib/buried-oil-tank-verdict
Environment=BURIED_OIL_TANK_ADMIN_USERNAME=admin
Environment=BURIED_OIL_TANK_ADMIN_PASSWORD=replace-this
ExecStart=/usr/bin/java -jar /opt/buried-oil-tank-verdict/current/target/buried-oil-tank-verdict-0.0.1-SNAPSHOT.jar
Restart=always
```

Production host policy:

- Canonical public host is `https://oiltankroute.com`
- `www.oiltankroute.com` should redirect to the apex domain
- The app now redirects non-canonical hosts to the configured `buried-oil-tank.base-url`

OCI deploy defaults:

- Docker image: `shinhyeok22/oiltank`
- Local-only OCI container port mapping: `127.0.0.1:8097 -> 8080`; the public reverse proxy must terminate the client connection and replace forwarded headers
- Required GitHub secrets:
  - `DOCKERHUB_USERNAME`
  - `DOCKERHUB_TOKEN`
  - `OCI_HOST`
  - `OCI_USERNAME`
  - `OCI_KEY`
  - `APP_ADMIN_USERNAME`
  - `APP_ADMIN_PASSWORD`
Search Console metrics use a directly reviewed snapshot bundled at `data/normalized/search/gsc-route-metrics.csv`. A newer `storage/ops/gsc-route-metrics.csv` can override it without an app restart. Missing, malformed, or more than fourteen-day-old metrics are shown as unavailable instead of zero; no GitHub Actions credential is required.

New York county aggregates use the reviewed bundled snapshot and can be refreshed manually with `ops/fetch_ny_incidents.py`. The runtime reads `storage/records/new-york-counties.json` when present and falls back safely if the override is missing or malformed.

Run the same fetch locally with:

```bash
python ops/fetch_gsc_metrics.py --credentials path/to/service-account.json
```

## Recommended launch cohort
The focused public cohort is:

- `New Jersey`
- `New York`

New Jersey keeps its state, buyer/seller, sweep, and records routes. New York keeps its state and records routes, plus Westchester, Nassau, and Suffolk incident pages; its buyer/seller and sweep URLs consolidate into the national guides. Connecticut and Maine consolidate into the state or national guide hubs. Massachusetts remains in launch reserve.

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
- New Jersey state, buyer/seller, sweep, and records pages
- New York state, records, and top-three county incident pages
- evergreen records, home-sale, and sweep guides

The leak-response and removal-cost guides are indexable because they now have distinct, source-backed intent. The overlapping remove-versus-abandon guide remains `noindex,follow` support inventory until it earns a distinct query role.

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

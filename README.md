# Oil Tank Route

Oil Tank Route is a founder-led **property-specific oil tank record research and transaction brief service** for U.S. buyers, sellers, owners, agents, and attorneys. A user sends a property and the question blocking a decision; the service resolves the parcel, searches municipal and environmental record layers, identifies the correct agency request route, interprets supplied documents, and returns confirmed facts, unresolved gaps, and prioritized next actions.

The existing heating-oil utilities and state/guide library remain live as organic acquisition and decision-support surfaces. They no longer define the homepage or the core business model. The first service cohort is New Jersey and New York, and intake is a free founder-led beta.

Working internal project: `BuriedOilTankVerdict`  
Suggested package root: `owner.buriedoiltank`

**Date:** 2026-09-14 (Asia/Seoul)
**Purpose:** U.S.-focused **oil tank record research, agency routing, document interpretation, and transaction briefs**, supported by a residential heating-oil utility library.

## Primary service routes

- `/` - service promise, evidence workflow, public-data demonstration, and research intake
- `/how-it-works/` - parcel-to-brief research sequence
- `/record-research/` - source matrix, agency-routing rules, and deliverables
- `/sample-brief/` - visibly fictional composite brief showing evidence, gaps, and next actions
- `/research-areas/` - searchable directory of 16 differentiated NJ/NY research routes
- `/research-examples/` - public-source walkthroughs, not completed customer cases
- `/states/new-jersey/records-and-proof/` - NJ parcel, municipal, NJDEP, and OPRA routing
- `/states/new-york/records-and-proof/` - NY parcel, municipal, DEC, spill, and FOIL routing
- `/tools/` - preserved 20-route heating-oil utility library

## Current product routes

- `/heating-oil-tank/` - identification start
- `/heating-oil-tank-sizes-dimensions/` - one verified size comparison
- `/275-gallon-oil-tank/` - 275-gallon model and chart distinctions
- `/heating-oil-tank-charts/` - official manufacturer chart library
- `/oil-tank-gauge-calculator/` - chart-interpolated gauge/stick result
- `/heating-oil-delivery-check/` - before/after chart range, ticket comparison, and local Tank Passport
- `/heating-oil-usage-calculator/` - household-rate fuel runway
- `/heating-oil-prices/` - dated EIA residential price benchmark and seasonal-status hub
- `/heating-oil-cost-calculator/` - local supplier quote subtotal and dated benchmark comparison
- `/how-much-heating-oil-do-i-need/` - gauge-context order-space calculator
- `/ran-out-of-heating-oil/` - no-heat checks, reset boundary, and qualified restart route
- `/oil-tank-capacity-calculator/` - shape formula and measurement range
- `/how-long-do-oil-tanks-last/` - evidence-led lifespan and condition boundary
- `/oil-tank-gauge-replacement/` - gauge diagnosis and qualified-service boundary
- `/heating-oil-tank-repair/` - component repair, tank condition, and release routing
- `/heating-oil-tank-sludge-cleaning/` - service, corrosion, and possible-release router
- `/oil-tank-replacement-planner/` - information, inspection, transaction, or urgent routing
- `/oil-tank-replacement-cost/` - replacement quote-scope comparator
- `/heating-oil-tank-installation-cost/` - installation quote-scope comparator
- `/basement-oil-tank-removal/` - routine indoor removal scope versus release response

All public pages use JTE SSR. Calculator behavior is dependency-free ES module JavaScript, and results never require an email. See `DESIGN.md` for the current white/deep-green property-research design; utilities retain their functional layouts.

## What you are building
A property-first research service for buyers, sellers, homeowners, agents, and attorneys who have an unresolved oil-tank record question:

- tank suspected before closing
- old fill pipe or vent discovered
- records missing
- remove versus abandon decision needed
- leak or contamination concern

The service should establish the exact property, search the available evidence layers, preserve the route for records that are not online, distinguish evidence from assumptions, and explain whether records, document interpretation, a sweep, confirmed-tank action, or urgent leak response comes next.

## Phase 1 service wedge

- buyer, seller, owner, agent, or attorney with a real property and decision deadline
- records missing, documents unclear, or listing/seller claims unverified
- NJ and NY public-record routes first
- a founder-produced research brief, not an automated legal or environmental conclusion

## Why this concept is attractive
- The trigger is urgent and commercial.
- Search intent is transaction-heavy, not broad informational curiosity.
- State and program variance is real enough to create a durable page system.
- Revenue can start with tank sweep and contractor routing before any large sponsor marketplace exists.
- Compared with many home-service topics, each qualified lead can be worth meaningful money even at low traffic.

## Product thesis
Build a **property-specific research desk** for people trying to answer:

- Is there really a buried tank here?
- What records do I need before I buy or sell?
- Remove or abandon in place?
- Is a leak likely?
- Which source or agency can establish the next fact?
- What do the files actually establish, and what remains unresolved?
- Do records, a sweep, or confirmed-tank action come next?

## File map
- `AGENT_START_HERE.md` - read order and handoff rules for any future agent
- `ops/context_tracker.md` - current status, decisions, and next tasks
- `ops/wedge_focus_2026-04-13.md` - current primary wedge and the narrow operating loop for the first build phase
- `ops/source_audit_2026-04-13.md` - official-source anchor map and how each source should shape the product
- `ops/persona_council_2026-04-13.md` - forced debate across demand, SERP, funnel, risk, and sponsor perspectives
- `ops/promotion_review_system_2026-04-13.md` - how future agents should review metrics and recommend route promotion
- `ops/route_promotion_board.md` - current held-route board and recommendation status
- `ops/heating_oil_price_refresh.md` - EIA price refresh schedule, source boundary, update checklist, and completion log
- `ops/price_delivery_keyword_validation_handoff.md` - completed 21-keyword Google Ads validation, settings, output files, and demand gate
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
- The service-first homepage, workflow, research-method page, public-data sample brief, and detailed intake are implemented.
- NJ/NY route intelligence is bundled as a reviewed baseline and can be updated persistently at `storage/records/route-intelligence.json`; see `ops/route_intelligence.md`.
- Privacy-safe service funnel measurement distinguishes browser submit attempts from server-confirmed submissions; property address, email, notes, document names, and document contents are excluded from analytics payloads.
- All 20 existing utility routes remain available under the `/tools/` acquisition hub and include a contextual record-research handoff.
- Spring Boot plus `jte` application scaffold is live under `owner.buriedoiltank`
- Runtime route inventory contains 92 records, including 16 area routes and 5 problem routes. The sitemap contains 67 canonical URLs, including static trust pages.
- Lead capture and event logging persist to `storage/leads`; secure customer documents and their integrity register persist outside the release under `storage/cases` and `storage/operations`.
- Record-research cases use append-only operational states: intake, researching, agency pending, waiting on customer, brief delivered, and closed.
- The protected admin dashboard shows the full case intake, notification state/retry, service funnel by page/state/question, and supporting utility funnels without requiring a CSV download.
- Ops snapshots persist to `storage/ops` and `storage/derived`
- Admin exports are available under `/admin/exports/*`
- Packaged runtime now uses generated `jte` template classes, so `java -jar` is deployable without template recompilation at runtime

## Production persistence notes
- Lead submissions are stored in `leads.csv`
- CTA and lead funnel events are stored in `lead_events.csv`
- Case status changes and operator notes are stored in `case-status.csv`; findings, requests, replies, referrals, deliveries, and route lessons are stored in the append-only `case-activity.csv`; operator-email attempts are stored in `notification-attempts.csv`.
- The intake accepts up to three PDF/JPG/PNG documents (8 MB each, 20 MB total). Content signatures are checked before private storage, and downloads remain behind admin authentication.
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
export BURIED_OIL_TANK_NOTIFICATION_ENABLED=true
export BURIED_OIL_TANK_NOTIFICATION_TO=operator@example.com
export BURIED_OIL_TANK_NOTIFICATION_FROM=intake@example.com
export BURIED_OIL_TANK_SMTP_HOST=smtp.example.com
export BURIED_OIL_TANK_SMTP_PORT=587
export BURIED_OIL_TANK_SMTP_USERNAME=smtp-user
export BURIED_OIL_TANK_SMTP_PASSWORD='replace-this'
export BURIED_OIL_TANK_SMTP_STARTTLS=true
```

Mail is disabled by default, so local development and tests require no SMTP setup. A valid case is persisted before notification is queued. Disabled, configuration, queue, and delivery outcomes remain visible in admin and can be retried without resubmitting or losing the case. Keep all SMTP credentials in environment variables or the host secret manager.

The GitHub deployment requires `APP_GMAIL_USERNAME` and `APP_GMAIL_APP_PASSWORD` secrets, enables Gmail SMTP notification in the generated production `.env`, and fails before deployment when either secret is absent. The post-deploy canary also verifies the service positioning, representative evidence packet, and multipart document intake.

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

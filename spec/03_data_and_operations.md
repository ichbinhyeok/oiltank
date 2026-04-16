# 03 Data And Operations

## Data philosophy
The moat is not prose. The moat is a verified, state-aware decision dataset that can render many routes without letting claims drift.

## Recommended raw data files
- `data/raw/states/seed-state-rules.csv`
- `data/raw/markets/seed-market-overlays.csv`
- `data/raw/costs/seed-cost-bands.csv`
- `data/raw/guides/guides.json`
- `data/raw/partners/seed-partner-types.csv`
- `data/raw/sources/*.json`

## Recommended normalized outputs
- `data/normalized/states/{state}.json`
- `data/normalized/markets/{market}.json`
- `data/normalized/sources/{sourceId}.json`
- `data/derived/routes.json`

## Recommended ops files
- `data/ops/route-status.csv`
- `data/ops/promotion-review.json`
- `data/ops/admin-metrics-snapshot.json`

These exist so a future agent can review route status and recommend promotion without relying on human memory.

## Route-status schema
Each route record should capture:

- route id
- route path
- route family
- state or market scope
- phase:
  - phase_1_public
  - held_support
  - future_candidate
- index status:
  - index
  - noindex
  - not_built
- source freshness status
- last 28-day impressions
- last 28-day clicks
- last 28-day ctr
- last 28-day cta clicks
- last 28-day lead opens
- last 28-day lead submissions
- dominant scenario
- promotion recommendation:
  - hold
  - recommend_promote
  - recommend_build
  - recommend_demote
- recommendation reason
- reviewed on
- next review on

## Promotion-review schema
Each review artifact should capture:

- review date
- data window
- agent summary
- promoted candidate routes
- held routes still not ready
- routes to demote or merge
- blockers:
  - source gap
  - partner gap
  - no query evidence
  - weak CTA behavior
  - weak lead evidence

## State record schema
Each state record should capture:

- state name and slug
- heating-oil market priority score
- regulatory owner
- delegated local authorities if any
- buyer-seller guidance summary
- record or lookup path summary
- sweep-first logic summary
- removal versus abandonment summary
- leak or contamination reporting summary
- insurance or cleanup caveat summary
- source ids
- verified date
- next review date

## Market overlay schema
Only use when needed.

Fields:

- market name
- state
- market type:
  - county
  - metro
  - delegated authority
- why the overlay exists
- what answer changes locally
- sponsor density score
- route families allowed

## Cost-band schema
Fields:

- state or market scope
- scenario:
  - sweep
  - removal
  - closure
  - leak cleanup
- min directional band
- max directional band
- assumptions
- confidence score
- source ids

## Partner type schema
Fields:

- partner type slug
- display label
- what problem it solves
- what trigger state it matches
- lead priority

## Source workflow
1. Capture raw official source URLs and notes
2. Normalize into structured source records
3. Link source ids to state and market records
4. Render routes only from normalized records
5. Fail route promotion when `verifiedOn` or `nextReviewOn` rules are stale

## Source hierarchy
1. State environmental agency pages and PDFs
2. State homeowner or cleanup guidance
3. Local delegated authority pages
4. State consumer or insurance guidance
5. Secondary market-cost references

## Review cadence
- Core launch states: every `45` days
- Secondary or reserve states: every `90` days
- Cost references: every `60` days
- Partner and sponsor roster: every `30` days once launched

## Operational principles
- Do not create data records for routes you are not ready to support editorially.
- Do not create local overlays without a written `why this changes the answer` note.
- If a state lacks strong official clarity, keep the route narrower or do not index it.

## Launch data backlog
Phase 1 should complete:

- five state records
- at least six evergreen guide records
- one directional cost record per launch state for each high-value scenario
- one route manifest covering state hubs and core route families

## Ops review needs
The eventual admin or ops page should expose:

- stale source records
- routes blocked from index
- leads by partner type
- CTA click counts by route family
- missing-source gaps for launch states

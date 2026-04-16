# Promotion Review System - 2026-04-13

## Why this exists
The founder should not have to remember when a held route is ready.

The future agent should:

1. read the wedge rules
2. read the current route board
3. inspect metrics
4. tell the user whether any held route deserves promotion

This is a recommendation system, not an auto-publish system.

## What the agent must review
- route status
- source freshness
- impressions and clicks
- CTA behavior
- lead submissions by scenario
- partner availability

## Required review outputs
Every review should produce one of these for each held route family:

- `hold`
- `recommend_promote`
- `recommend_build`
- `recommend_demote`

And every decision must include a reason.

## Default held route families
- `removal-vs-abandonment`
- `leak-and-cleanup`
- `cost-direction`
- `market overlays`
- `partner finder pages`

## Promotion recommendation logic

### Recommend promote
Use this only when:

- the route already exists but is held `noindex`
- source freshness is green
- the indexed parent routes show repeated evidence that users want this next step
- the route has a real commercial and editorial purpose

### Recommend build
Use this when:

- the route does not yet exist
- query or lead evidence shows the route family is now justified
- the route can launch without violating the narrow wedge

### Hold
Use this when:

- the route idea is still valid
- but source, query, CTA, or lead evidence is still too thin

### Recommend demote
Use this when:

- an indexed route family attracts weak-intent traffic
- the route duplicates a broader page
- or the route lost its source or commercial justification

## Review cadence
- at the start of any later strategy or implementation session once metrics exist
- every `14` days after launch if no one has reviewed recently

## User-facing recommendation format
When a route qualifies, the agent should tell the user:

1. which route family
2. which states or markets
3. what evidence triggered the recommendation
4. whether the action is `promote`, `build`, `hold`, or `demote`
5. what risk still exists

## Fail-closed rules
- stale sources mean no promotion
- no scenario evidence means no promotion
- no partner path means no promotion
- broad-cost curiosity alone means no promotion

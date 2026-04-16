# Prebuild Persona Review - 2026-04-13

## Scope reviewed
- `README.md`
- `ops/wedge_focus_2026-04-13.md`
- `spec/00_strategy.md`
- `spec/01_query_and_user_map.md`
- `spec/04_commercial_model.md`
- `spec/09_launch_surface_and_route_inventory.md`

## Findings

### P1 - Phase 1 scope drift risk
`spec/01_query_and_user_map.md` still marked `remove-versus-abandon` and `leak-and-cleanup` as Tier 1 page families even though the launch-surface spec already kept them as support or `noindex`.

Risk:

- a future agent could overbuild Phase 1
- the public launch could drift from `buyer-seller + sweep + records`

Action taken:

- moved those route families to Tier 2
- added an explicit Phase 1 rule under query-to-route mapping

### P1 - Traction metric drift
`ops/wedge_focus_2026-04-13.md` and `spec/06_indexing_quality_and_analytics.md` treated `removal` clicks as a first proof signal.

Risk:

- the product starts optimizing for quote bait too early
- the first commercial read becomes noisier than the first editorial wedge

Action taken:

- changed the early traction language to `tank sweep` and `records/checklist` CTA behavior

### P2 - Commercial-scope ambiguity
`spec/04_commercial_model.md` described the full commercial model correctly, but it did not clearly separate Phase 1 public scope from later confirmed-tank or cleanup scenarios.

Risk:

- implementation mixes launch focus with full-category ambition
- CTA logic can become too aggressive too early

Action taken:

- added an explicit `Phase 1 commercial scope` section
- tightened `First-cash model` wording around sweep or records-led early leads

### P2 - Handoff ambiguity for future agents
`AGENT_START_HERE.md` and `ops/context_tracker.md` still implied that removal and leak routes were part of the current primary wedge.

Risk:

- the next implementation agent receives mixed signals
- routing and page inventory may widen before proof

Action taken:

- narrowed the handoff language to Phase 1 public scope
- kept removal and leak routes as support-layer work

## Five-person review

### Demand strategist
- Approves.
- The wedge is now sharp enough.
- `Buyer or seller in transaction + suspected tank or missing records` is a real pain moment.

### SERP strategist
- Approves after the Tier 1 fix.
- The launch surface now matches the likely first-query wins instead of pretending the whole category should ship at once.

### Funnel strategist
- Approves after the CTA-metric fix.
- Phase 1 should win with `sweep first` and `records first`, then graduate users into closure or cleanup only when evidence exists.

### Risk strategist
- Approves.
- The package is safer when it does not act like every user already needs removal or remediation.

### Builder-operator
- Approves.
- The docs are now coherent enough for a new agent to scaffold without reopening strategy questions on day one.

## Final verdict
Pass.

The package is buildable now.

The only rule that matters is this:

- launch the `buyer-seller + sweep + records` wedge first
- treat `remove-versus-abandon`, `leak-and-cleanup`, and broad cost routes as later promotions

# Final Extended Persona Review - 2026-04-13

## Purpose
Pressure-test whether the `BuriedOilTankVerdict` package is actually ready for implementation, not just directionally good.

## Personas in the room
- Demand realist
- SERP realist
- Content strategist
- Funnel operator
- Sponsor seller
- Risk operator
- Technical architect
- Ops pessimist
- Portfolio allocator
- Judge

## Findings before final sign-off

### P2 - State-hub navigation could widen too early
`spec/02_site_architecture.md` said every state hub links into all core route families.

Why that mattered:

- a future implementation could surface `remove`, `leak`, and `cost` routes too prominently
- the package could drift away from the narrow Phase 1 wedge without anyone noticing

Action taken:

- changed the rule so state hubs must link into all Phase 1 public route families first
- allowed held routes only as contextual support links

### P3 - Cost direction wording was slightly too broad
`spec/02_site_architecture.md` implied every state route page should carry cost direction or timeline treatment.

Why that mattered:

- some routes should stay verification-first
- forcing cost framing everywhere can make the site feel more like quote bait than a decision engine

Action taken:

- narrowed the rule to `when the scenario supports it`

## Extended debate

### Demand realist
- Pass.
- The wedge is narrow enough to map to a real transaction-time pain moment.

### SERP realist
- Pass.
- The package no longer pretends the full category should ship on day one.
- Phase 1 now matches the likely search-entry points.

### Content strategist
- Pass.
- The editorial rules are strict enough to prevent generic heating-oil drift.

### Funnel operator
- Pass.
- Primary motion is still `verify first`, not `quote first`.
- That is the correct conversion posture for trust and lead quality.

### Sponsor seller
- Pass.
- The eventual commercial ladder is intact:
  - sweep
  - closure or removal
  - cleanup
- But the docs no longer force the business to monetize every scenario immediately.

### Risk operator
- Pass.
- The package consistently separates suspicion, confirmation, and contamination.

### Technical architect
- Pass.
- The file-backed architecture is lean enough for a solo-founder portfolio and still leaves room for later admin review.

### Ops pessimist
- Pass with discipline.
- The package is ready only if the first build really stays inside the launch surface and does not start adding markets or route families opportunistically.

### Portfolio allocator
- Pass.
- This is good enough to implement without reopening strategy every week.
- That matters because the founder is running multiple projects at once.

### Judge
- Final verdict: `ready`
- Confidence: `9.0 / 10`

## Final conclusion
The `BuriedOilTankVerdict` document package is ready for implementation.

No more strategic rewrites are needed before scaffolding.

The next useful work is product implementation, not more idea debate.

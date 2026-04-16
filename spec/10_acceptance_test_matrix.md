# 10 Acceptance Test Matrix

## Goal
This matrix defines what must work before the first public release can be considered credible.

## Rendering tests
- Home route renders with state-entry modules
- Every launch-state hub renders without missing source or CTA blocks
- Every Phase 1 route family renders for every launch state
- Guide pages render with at least one state-entry path

## Metadata tests
- Every indexable route emits a canonical URL
- Every held route emits `noindex`
- Sitemap includes only indexable routes
- Robots file blocks admin and API paths

## Content-quality tests
- Every public page contains:
  - quick answer
  - what-not-to-assume section
  - official source stack
  - primary CTA
- No page claims certainty where the source does not support it

## Lead-flow tests
- Lead form opens from every core route family
- Lead submit success writes storage
- Lead submit error state renders cleanly
- Scenario field changes partner-type routing correctly

## Analytics tests
- CTA click logging records route family and state
- Lead events store scenario and route context
- Admin summary shows lead counts and click counts
- Route-status review shows whether held routes are `hold`, `recommend_promote`, `recommend_build`, or `recommend_demote`

## Mobile and UX tests
- home works on mobile width
- state pages keep primary CTA visible without layout breakage
- forms stay email-first and do not require phone

## Source freshness tests
- stale state sources block index promotion
- stale route records appear in admin review
- promotion recommendations fail closed when source freshness is stale

## Launch pass condition
Release is acceptable only if:

1. all launch-state hubs pass rendering and metadata tests
2. all Phase 1 public pages contain source stacks and next-action CTAs
3. lead capture is working end to end
4. no route family depends on unsupported or vague claims

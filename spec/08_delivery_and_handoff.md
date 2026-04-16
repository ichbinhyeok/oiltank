# 08 Delivery And Handoff

## Delivery objective
Ship a narrow but commercially credible first version that can:

- render state and guide pages
- route users by scenario
- capture leads
- log enough route analytics to learn quickly

## Workstreams

### Workstream 1 - Scaffold
- create Spring Boot plus jte app
- create package root
- create data loading skeleton
- create public trust pages

### Workstream 2 - Data and routes
- create state records
- create guide records
- generate route inventory
- wire canonical and robots behavior

### Workstream 3 - Commercial routing
- create CTA logic
- create lead form and persistence
- create event logging
- create minimal admin view
- create route-status and promotion-review surfaces

### Workstream 4 - QA and launch
- render checks
- metadata checks
- lead form smoke checks
- mobile and desktop visual QA

## Implementation order
1. scaffold app and route inventory
2. implement home and state hub rendering
3. implement state route pages
4. implement guides
5. implement CTA routing and lead capture
6. implement admin review and route-promotion review
7. run end-to-end QA

## Phase gates

### Gate 1
- home renders
- one state hub renders
- one guide renders
- route inventory exists

### Gate 2
- four launch-state hubs render
- core route families render for each state
- source stack visible

### Gate 3
- lead capture works
- CTA tracking works
- trust pages exist
- route-status review exists

### Gate 4
- first launch cohort passes acceptance matrix

## Handoff checklist for any future agent
- update `ops/context_tracker.md`
- update route inventory if states change
- update source audit when official anchor sources change
- note any route families intentionally held `noindex`
- review `ops/route_promotion_board.md` if metrics exist
- record whether any held route should be promoted, built, or still held

## Scope discipline
If implementation pressure appears, cut breadth before quality:

- fewer states
- fewer route families
- stronger pages

Never do the reverse.

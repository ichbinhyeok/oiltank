# 02 Site Architecture

## Canonical entities

### State
The primary SEO and content entity.

Fields:

- name
- slug
- regional demand score
- regulatory owner
- delegated local authorities
- buyer-seller relevance
- sweep relevance
- leak-reporting path

### Route family
A repeatable decision path inside a state.

Core families:

- overview
- buyer-seller
- sweep-and-locate
- records-and-proof
- removal-vs-abandonment
- leak-and-cleanup
- cost-direction

### Guide
An evergreen cross-state page that explains a scenario while routing the user into a state page.

### Local overlay
A county or metro page that only exists when local regulation or local buyer-seller workflow materially changes the answer.

### Partner type
- sweep or locate specialist
- closure or removal contractor
- environmental consultant or cleanup specialist

## URL graph

### Core public routes
- `/`
- `/states/{state}/`
- `/states/{state}/buyer-seller/`
- `/states/{state}/sweep-and-locate/`
- `/states/{state}/records-and-proof/`
- `/states/{state}/removal-vs-abandonment/`
- `/states/{state}/leak-and-cleanup/`
- `/states/{state}/cost-direction/`

### Evergreen guides
- `/guides/buried-oil-tank-home-sale/`
- `/guides/oil-tank-sweep-before-buying-house/`
- `/guides/abandoned-oil-tank-records/`
- `/guides/remove-vs-abandon-oil-tank/`
- `/guides/leaking-heating-oil-tank-what-to-do/`
- `/guides/oil-tank-removal-cost/`

### Support and trust routes
- `/about/`
- `/methodology/`
- `/contact/`
- `/privacy/`
- `/terms/`
- `/not-government-affiliated/`

### Admin and ops
- `/admin/`
- `/admin/exports/*`

## Canonical rules
- State routes are canonical by default.
- Local overlay pages are canonical only when they contain materially different official workflow or route-level commercial evidence.
- Thin market pages stay `noindex`.
- Query-parameter variants must redirect to clean canonical URLs.

## Page modules

### Home
- wedge headline
- scenario selector
- state entry points
- strongest trust guides
- clear explanation of what the site does not do

### State hub
- quick state answer
- scenario cards
- official process summary
- route family navigation
- state-specific cautions
- sponsor or lead CTA block

### State route page
- quick answer
- why this route matters in this state
- what evidence changes the answer
- what not to assume
- cost direction or timeline section when the scenario supports it
- next-step CTA
- official source stack

### Guide page
- scenario explanation
- cross-state cautions
- state-entry chooser
- lead CTA when the scenario supports it

## Internal linking rules
- Every guide links into at least three state routes.
- Every state hub links into all Phase 1 public route families.
- Held or `noindex` route families can be linked contextually, but should not displace the primary Phase 1 navigation.
- Every route page links:
  - back to the state hub
  - sideways to the next likely route
  - down to a CTA
- Leak pages must link to records and removal pages.
- Buyer-seller pages must link to sweep and records pages before cost pages.

## CTA architecture
- Each page gets one primary CTA based on scenario.
- Secondary CTA only exists when the next-best action differs materially from the primary path.
- CTAs should never offer `all options` at once.

## Structured data
- `WebPage` on all public pages
- `BreadcrumbList` on state and guide routes
- `FAQPage` only when the page genuinely contains scenario-specific FAQ blocks
- Avoid fake `LocalBusiness` schema on editorial pages

## Route-quality principle
If a route cannot clearly differ from a generic national page or a state official PDF, it should not ship yet.

# 01 Query And User Map

## Primary user segments

### Buyer under contract
Questions:

- Is there a buried tank here?
- Do I need a sweep before closing?
- Who pays if there is a tank?
- What records should I ask for?

Commercial value:

- very high

### Seller preparing to list or already under contract
Questions:

- Should I remove it now?
- What documents will buyers ask for?
- Is abandonment enough?
- What happens if I have no records?

Commercial value:

- high

### Homeowner not in sale yet but suspicious
Questions:

- What do these vent pipes or stains mean?
- Who checks for a buried tank?
- Do I need removal or just records?

Commercial value:

- medium to high

### Leak or contamination concern
Questions:

- Is this just an old tank issue or a reportable spill?
- Who handles cleanup?
- What does this do to the property value?

Commercial value:

- very high

## Trigger states
The site should always classify the user into one of these trigger states:

1. `suspected_tank_during_sale`
2. `records_missing`
3. `confirmed_tank_need_next_step`
4. `removal_vs_abandonment_decision`
5. `leak_or_cleanup_concern`

## Query families

### Buyer-seller and closing
- buried oil tank before buying house
- underground heating oil tank home sale
- buying house with old oil tank
- seller buried oil tank disclosure
- who pays for oil tank removal buyer or seller

### Sweep and locate
- oil tank sweep before buying house
- underground oil tank sweep near me
- tank locate service before closing
- how to know if a house has a buried oil tank

### Records and proof
- abandoned oil tank records
- heating oil tank closure records
- no further action letter oil tank
- how to find oil tank records

### Remove versus abandon
- remove or abandon underground oil tank
- can heating oil tank be abandoned in place
- old underground oil tank closure requirements

### Leak and cleanup
- leaking heating oil tank what to do
- oil tank contamination cleanup
- underground heating oil tank leak reporting
- soil contamination from oil tank

### Cost and responsibility
- underground oil tank removal cost
- oil tank sweep cost
- cleanup cost after oil tank leak
- insurance cover old oil tank leak

## Query strategy rules
- Always prefer `trigger + state` over broad national phrasing.
- The first SEO wins should come from long-tail decision queries, not head terms like `oil tank removal`.
- If a query can be satisfied entirely by a single official answer with no real next-action ambiguity, it is not a priority route.

## Priority page families

### Tier 1
- state hub
- state buyer-seller page
- state tank sweep page
- state records page

### Tier 2
- state remove-versus-abandon page
- state leak and cleanup page
- state cost page
- local delegated-authority overlay page
- guide pages for buyer-seller negotiation, records, and proof

### Tier 3
- selective county or metro pages
- insurance support pages
- attorney or dispute-adjacent support content

## Query-to-route mapping
- `Do I need a sweep?` -> state sweep page
- `What records matter?` -> state records page
- `What should buyer or seller do next?` -> state buyer-seller page
- `Should I remove or abandon?` -> state remove-versus-abandon page
- `What if it leaked?` -> state leak and cleanup page

Phase 1 rule:

- `remove-versus-abandon` and `leak-and-cleanup` routes are support routes first.
- They should not displace the launch wedge built around buyer-seller, sweep, and records pages.

## The user map the site should support

### Buyer path
1. Learns tank may exist
2. Needs evidence and records
3. Needs sweep or locate
4. Moves to removal or negotiation only after verification

### Seller path
1. Wants to de-risk listing or closing
2. Needs record or closure proof
3. Needs removal or closure contractor
4. May need cleanup specialist if evidence suggests a leak

### Leak path
1. Concern or contamination clue appears
2. Needs reporting and risk triage
3. Needs environmental specialist or certified closure help
4. Needs directional cost and timeline context

## First keyword rule
If a page does not answer a `what next right now` question, it probably should not be in the launch surface.

# Record research operating model

**Effective:** 2026-09-14
**Status:** Current product direction. This document supersedes older utility-first positioning where the two conflict.

## Promise

Oil Tank Route turns an unclear property-level oil tank question into a source-backed transaction brief. The founder resolves the parcel, searches public record layers, identifies the correct custodian and request route for missing files, interprets what supplied records establish, and returns facts, unresolved gaps, and prioritized next actions.

The output is research support, not a safety certification, title opinion, legal opinion, environmental determination, or substitute for an on-site professional.

## First cohort

- New Jersey and New York
- buyer, seller, owner, agent, or attorney
- live property and a decision or transaction deadline
- one of three primary jobs: find records, interpret documents, or decide whether records, a sweep, or confirmed-tank action comes next
- active leaks and immediate hazards leave this funnel for the safety route

## Research sequence

1. Identify: normalize address, municipality/county, parcel identifiers, and prior-address or owner clues.
2. Cross-check: assessor/GIS, municipal permit/property systems, environmental cases, spill data, fuel-conversion clues, and indexed documents.
3. Route: record exact agency, portal, request method, identifiers, requirements, fees, expected timing, and fallback.
4. Explain: separate confirmed facts, reported claims, interpretation, conflicts, unavailable sources, and unresolved gaps.
5. Prioritize: state the next request or professional action in deadline order.

## Public demonstration rule

The sample brief uses public information and must remain visibly labeled as a demonstration. Listing language is a claim, not a verified condition. Unavailable or silent systems remain unresolved; they are never converted into a negative finding.

## Intake and analytics boundary

Operational intake stores property address, state, municipality/county, role, known status, primary question, deadline, document availability, email, optional notes, and up to three private PDF/JPG/PNG source documents. Uploaded files are stored outside the release directory with random server filenames and SHA-256 integrity metadata. Analytics events use only non-PII funnel dimensions. Address, email, free text, document names, and document contents never enter GA4 payloads.

Required funnel events and ownership:

- `service_cta_view`
- `service_cta_click`
- `research_form_start`
- `research_form_submit_attempt` — browser intent only
- `research_form_submit_success` — emitted internally only after the server persists a new, idempotent case; the browser may mirror this to GA4 after the success redirect
- `document_interpretation_request` — server-confirmed case classification, mirrored to GA4 after success
- `qualified_case` — server-confirmed NJ/NY case classification, mirrored to GA4 after success

The public event endpoint rejects server-confirmed event names. A stable per-form submission token prevents refreshes or repeated POSTs from creating another case or success event. Internal referrers retain origin only; paths and query strings are discarded.

## Case operations and notification

Every saved research case starts at `intake` and may move through `researching`, `agency-pending`, `waiting-on-customer`, `brief-delivered`, and `closed`. Status changes are append-only. The protected admin surface displays the complete intake, current case state, notification outcome, and service demand by entry page, state, and primary question.

Research operations also use an append-only case activity ledger. Online findings, requests, bounces, referrals, agency replies, customer updates, document receipt, delivery, and reusable route lessons preserve the route ID, agency/channel, source or request ID, outcome, next action, and check date. This ledger is the historical evidence; route intelligence is the current verified reusable conclusion.

Operator email is disabled by default and configured only through `BURIED_OIL_TANK_NOTIFICATION_*` and `BURIED_OIL_TANK_SMTP_*` environment variables. Persistence completes before mail is queued. A disabled, misconfigured, rejected, or failed mail attempt never changes the successful intake result; it is recorded without PII in `notification-attempts.csv`, shown in admin, and can be retried there.

## Route intelligence loop

The bundled JSON is a reviewed baseline. Production may override it at `storage/records/route-intelligence.json`. After a case exposes a more accurate custodian, identifier, fee, response-time expectation, or fallback route, update the persistent file without adding client PII, verify the public link, and record the verification date.

## URL roles

- `/` is the service landing page and primary conversion surface.
- `/how-it-works/`, `/record-research/`, and `/sample-brief/` explain process, evidence, and output.
- NJ/NY records-and-proof pages carry jurisdiction-specific search intent into the same service.
- `/tools/` is the utility hub. Existing size, chart, price, cost, usage, delivery, and condition routes remain distinct canonicals while each serves a distinct job.

## Beta success signals

Evaluate the service on qualified property submissions, question mix, state mix, deadlines, document-interpretation demand, brief completion time, agency-route reuse, and whether briefs resolve the user's next decision. Do not use raw CTA clicks or broad utility traffic as the primary business outcome.

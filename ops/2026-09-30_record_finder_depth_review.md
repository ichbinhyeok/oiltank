# Record Finder depth review — 2026-09-30

Local follow-up requested after the owner asked whether the pivot was sufficient. No external messaging, customer-case research, publication, Search Console or IndexNow submission. Existing September 16 production remains unchanged.

## What changed and why

- 37 detail pages now have an SSR evidence target and document-comparison checklist, before the worksheet. These are editorial aids, not authentication or a safety verdict. Eight agency tasks, Nassau and the three added localities have distinct document targets; other routes share an explicitly general permit/completion comparison.
- Default prepared requests use the current route's document scope instead of asking generically for a permit everywhere. User-entered scope takes precedence.
- A listing without a file is a separate outcome from obtaining a candidate document. Empty, inaccessible, wrong-office, pending and scoped no-record replies remain separate.
- Saved source outcomes now carry source titles. When a legacy worksheet's source count changes, retain its private identifiers/notes but reset ambiguous statuses with a visible explanation. Adding an eligibility step must not assign an old result to the wrong source.
- Finder supports local text filtering as well as jurisdiction/family filters; search terms are not sent to analytics or placed in URLs. Directory states/counts derive from the catalog, including Portland's self-service-only route.
- NJ NFA route starts by checking applicability. Official FAQ says no NFA is needed for a closed UHOT with no discharge evidence that passed municipal inspection. A blank database query does not establish those conditions.

## Candidate decisions, official evidence

### South Orange NJ — HOLD → KEEP

- https://www.southorange.org/FAQ.aspx?QID=123 (redirects to Building FAQs): prospective buyer record inquiries routed through Clerk OPRA, not a new permit application.
- https://www.southorange.org/161/Clerks-Office : current online OPRA link.
- https://www.southorange.org/686/Oil-Tank-Abandonment : location survey, inspections, waste/scrap bills of lading and environmental references make a specific document trail. Public text avoids promising the FAQ's historical turnaround or treating permit rules as proof a historic file exists.

### Town of Babylon NY — HOLD → KEEP

- https://townofbabylonny.gov/DocumentCenter/View/174/Freedom-of-Information-Law-FOIL-Application : both pages reviewed. Explicit Environmental category for tank removal/storage tanks, distinct Building/Fire categories; complete SCTM, physical address, review-only and current submission instructions.
- https://www.townofbabylonny.gov/133/Assessors-Office : parcel/property-record context. Do not route Village cases as if Town custody were established.
- The earlier generic-only classification missed the form's tank-specific category; corrected rather than retained to defend a prior decision.

### Portland OR — new KEEP

- https://www.portland.gov/ppd/public-records : residential tank installation/removal permits belong to Fire & Rescue / Public Safety, not a general Building file. Historic city limits can affect coverage; DEQ decommissioning/cleanup is separate.
- https://www.portland.gov/public-records/portal-guide : request entry versus My Records Request Center for status/delivered documents.
- https://multco.us/info/permit-records : county jurisdiction distinction independently checked; no extra Multnomah near-duplicate page made.
- Self-service only; no expansion of NJ/NY human assistance.

Other nine originally held candidates remain HOLD. North Hempstead rechecked, but the old form/current portal path still was not verified sufficiently for a new page. Bellevue search findings were not used to expand scope into an unreviewed new service area.

## Search-intent / competition evidence (qualitative, not volume)

Searches covered NJDEP NFA copies, Nassau verification letters, South Orange oil tank records and Portland oil tank records. Official source results dominate specific document queries; contractor/property-advice pages and discussions also appear. No reliable keyword volumes, rank tracking or conversion data were acquired. Search results are not a demand forecast.

The product's useful distinction must be correct custodian selection, document versus index/status separation, and a specific missing-file action—not a rewritten summary competing with the official source. Preserve one canonical for each job: Nassau letter stays on the existing county page; Portland links to Oregon tasks rather than duplicating the state certificate guide. National generic 'oil tank' demand is not claimed.

## External workflow checks in this follow-up

- Oregon live public PLC module → HOT Clean Decommissioning filter → real result's PLC Document control → new report window. Reload of that window returned HTTP 200, `application/pdf`, Content-Length 424109. This verifies a document response from the actual UI path, not its relevance to a customer or the completeness of all attachments. No record contents or property identifiers are published in this report. The headless snapshot body was empty for PDF; the favicon 404 is not a failed document response.
- Nassau existing verification portal navigation timed out in this environment. Official county page/search evidence still distinguishes Print Verification Letter from scheduling new work. Keep fallback guidance and mark delivery unverified; timeout is not 'no records'. No actual work appointment or request was submitted.
- NJDEP FAQ/NFA direct reader returned 403; official indexed excerpts supplied the eligibility and retrieval instructions. Do not label this a successful live DataMiner document retrieval. Sources: https://dep.nj.gov/srp/unregulated/unregulated-faqs/ and https://dep.nj.gov/srp/unregulated/nfa-letter/ .
- South Orange live Clerk link opened the GovPilot OPRA form and its fields. No fields were filled, no Submit was clicked, and any pre-rendered form number is not a sent-request receipt.
- Earlier Maine/CT/Seattle/filter checks remain as described in the initial verification report. They are not silently upgraded to end-to-end document retrieval.

## Verification and release

- Full Maven package: **65 tests, zero failures/errors**, local Java 23 targeting 21. Initial run caught the old inventory-count assertion (106 → 109; indexable entries 76 → 79), then the full run passed. No test was skipped to make that run pass.
- Real preview sitemap: **84 unique URLs**, all HTTP 200, self-canonical, exactly one H1 and no unintended noindex. Shared route tests verify every detail's internal links and NJ/NY assistance boundary.
- Browser: Portland/Nassau/NFA text filters, no-match state, Oregon + local-family conjunction, and actual result navigation passed. Oregon local directory filter shows Portland only.
- 30 bounds checks: finder, directory, Portland, South Orange and NFA at 320/390/768/800/960/1440; actual rendered content element bounds passed. Mobile evidence section visually inspected.
- Legacy three-source NFA worksheet retained synthetic identifiers/notes and reset all ambiguous outcomes after the fourth source was added, with an explanation. New listing-only outcome persisted across reload; custom document scope overrode the default; clear removed the synthetic address. URL remained clean.
- No-JS Portland: official links and evidence checklist readable, address input disabled, interactive actions hidden, no assisted intake. The fieldset itself is not a valid `isDisabled` probe in this browser; the child input and disabled attribute were checked directly.
- Final request-grammar and on-page navigation polish repackaged separately after the full suite; JavaScript syntax checked. Local QA uses isolated storage and notifications disabled. No live GA4/GSC outcomes or production Java 21 CI/security gate are claimed.
- Final rebuilt preview on port 18081 rechecked: 84-URL crawl still passes; route-specific request grammar, checklist navigation, finder filtering and full print-draft text/visibility pass. This preview uses the actual rebuilt assets, not browser response substitution.

## Remaining decisions / non-claims

This closes the follow-up implementation and local QA scope, not organic-acquisition proof. Do not claim every external portal yielded every document; Nassau's live timeout and NJDEP reader restrictions remain explicit. No search-volume estimates or success promises. Nine local candidates are still held; 50–60 was planning capacity, not a page quota.

For release, use the existing CI/security, private-backup and rollback process. After publication, evaluate document/local cohorts separately using GSC impressions, queries and clicks, and privacy-safe source-open/draft events. Do not label a click a successful record retrieval, a self-reported document a verified match, or traffic a paid conversion. Do not repeatedly submit unchanged URLs as a substitute for improving usefulness.

# Broader record utility — October 11, 2026

Status: local implementation; not a production deployment or evidence of SEO recovery. Builds on `2026-10-11_record_route_expansion.md`, preserving that earlier pass and all existing URLs.

## Objective and scope

Keep the approved record-finder pivot. Expand useful institution/document search entrances in one substantive cohort, with exact local custody differences and a real retrieval aid. No paid traffic, agency outreach, customer messages, automatic case acceptance or new assisted-service states.

New cohort: **17 details** (9 agency/document routes + 8 local routes). Total: **58 details = 36 local + 17 agency/document + 5 problem routes**, 61 catalog URLs including three hubs, expected 105 full sitemap URLs. This is a coverage count, not a traffic forecast or arbitrary minimum for SEO success. Coverage spans selected programs in 12 states, not every municipality in those states.

## Research-backed differences

All links below are primary public sources. Editorial review establishes the described route, not availability of a particular property's file or successful request fulfillment.

| New route | Distinct search / document problem | Official evidence |
|---|---|---|
| Massachusetts MassDEP | RTN versus actual eDEP submittals; local Fire work files separate | [Contaminated-property lookup](https://www.mass.gov/find-out-about-a-contaminated-property), [record access](https://www.mass.gov/guides/making-a-public-records-request-at-massdep) |
| Vermont ERT | Separate Spills/Hazardous Sites/UST programs; recent-500 display is not an all-years search | [ERT](https://anrweb.vt.gov/dec/ert/Default.aspx), [Spills](https://anrweb.vt.gov/dec/ert/Spills.aspx), [ANR records](https://anr.vermont.gov/about-us/public-records) |
| Rhode Island DEM | Emergency Response oil spills versus UST/LUST files; inventory ≠ delivered reports | [File review](https://dem.ri.gov/environmental-protection-bureau/customer-and-technical-assistance/file-review), [inventories](https://dem.ri.gov/environmental-protection-bureau/land-revitalization-and-sustainable-materials-management/inventories) |
| Maryland MDE | Partial values without wildcard characters; include closed cases; case ID ≠ facility ID | [Oil Control Case Information](https://mes-mde.mde.state.md.us/caseinformation/), [Open MDE](https://mde.maryland.gov/Pages/Open-MDE.aspx) |
| Virginia DEQ | Petroleum-release list versus annual UST statistics; older incidents can be unmapped | [Petroleum resources](https://www.deq.virginia.gov/land-waste/petroleum-tanks), [published layer scope](https://gisdata.deq.virginia.gov/mapper_ext/DataFactSheets/Land_Datasets/PetroleumReleases_10302020.pdf) |
| Pennsylvania DEP | Home heating-oil tanks expressly excluded from registered-tank lists; Site ID versus Other ID | [Tank data](https://www.pa.gov/agencies/dep/programs-and-services/land/storage-tanks/storage-tank-data), [informal file review](https://www.pa.gov/services/dep/request-general-or-informal-file-review-of-department-of-environmental-protection-records) |
| Washington Ecology | Cleanup sites versus regulated UST facilities; preserve CSID/FSID and actual issuer | [Cleanup and Tank Search](https://apps.ecology.wa.gov/cleanupsearch/), [all cleanup sites](https://apps.ecology.wa.gov/cleanupsearch/reports/cleanup/all) |
| Washington PLIA | Existing public project/opinion versus new assistance or insurance application | [Public search entry](https://plia.wa.gov/), [technical-assistance opinions](https://plia.wa.gov/technical-assistance-program/), [records access](https://plia.wa.gov/about-plia/public-records-requests/) |
| NYSDEC PBS | Registration facility/tank IDs versus spill number; small-tank and delegated-county limits | [PBS FAQ](https://dec.ny.gov/environmental-protection/hazardous-substance-bulk-storage/forms-instructions/pbs-registration-renewal-faq), [homeowner guide](https://dec.ny.gov/environmental-protection/hazardous-substance-bulk-storage/underground-heating-oil-tanks-homeowner-guide) |
| Boston | Fire Special Hazards tank categories; current JustFOIA, not obsolete GovQA | [Special Hazards](https://www.boston.gov/departments/fire-prevention/special-hazards), [current records portal guidance](https://www.boston.gov/departments/public-records) |
| Newton | Combined tank/burner category cannot prove tank removal; municipal Fire record holder | [Fire categories](https://www.newtonma.gov/government/fire/fire-prevention/forms-and-applications), [public records](https://www.newtonma.gov/government/city-clerk/public-records-requests) |
| Wellesley | Signed disposal receipt FP 291, separately from permit and inspection | [Removal documentation](https://www.wellesleyma.gov/791/Permits-and-Fees), [records access](https://www.wellesleyma.gov/1813/Public-Records) |
| Scarsdale | Official-linked property information versus new oil-tank application; FOIL missing attachments | [Property/application entry points](https://www.scarsdale.gov/675/Applications-for-Permits), [FOIL](https://www.scarsdale.gov/519/FOIL---Request-Public-Records) |
| Greenwich | Fire oil-tank FOIA, separate Building permit history and report lab/location attachments | [Fire](https://www.greenwichct.gov/480/Fire-Marshals-Division), [property resources](https://www.greenwichct.gov/3173/Property-Information-Resources), [Fire FAQs](https://www.greenwichct.gov/m/faq?cat=47) |
| Westport | Dedicated COs/Tank/Spill archive versus permit/inspection portal | [Building archive entry](https://www.westportct.gov/government/departments-a-z/building-department), [oil-tank guidance](https://www.westportct.gov/government/departments-a-z/building-department/residential-oil-tank-guidelines) |
| Stamford | Fire holds records when supplied, not a complete removal inventory | [Fire records instructions](https://www.stamfordfire.com/freedom-of-information-records-request-information/) |
| Wilton | Historical oil-tank collection through October 2018; not another archive's cutoff | [Historical records and current permitting](https://www.wiltonct.gov/planning-zoning-department/pages/online-permitting-and-licensing-portal-and-historical-records) |

Direct reading succeeded for Vermont ERT, Rhode Island, Maryland, Ecology, PLIA, NYSDEC, Scarsdale, Greenwich Fire, Stamford and Boston Public Records. Mass.gov, Newton, Westport, Wilton and some other municipal/agency endpoints blocked direct automated reading; their official indexed instruction text was reviewed instead. Virginia/PA guidance was also checked through official indexed text. These are not end-to-end portal retrieval tests. Request forms were not submitted. No fixed fee, response time or universal legal eligibility is promised. Future source-health checks must preserve BLOCKED/UNVERIFIED distinctions, not convert access errors into no-record findings.

Held: NH OneStop without a confirmed current retrieval path; Cambridge/Somerville without sufficiently distinct verified tank-file guidance; Fairfield CT based only on older material. Previous held local candidates remain held. No invented agency URL or unsupported historic coverage is published to meet a page quota.

## Direct Seattle utility

Primary dataset: [Seattle residential UST records](https://data.seattle.gov/Built-Environment/Underground-Storage-Tank-UST-Records-Residential/xvj2-ai6y). Its live metadata confirms the field names, residential permit scope, 1996 start, and missing-follow-up-report caveat. API response verified HTTP 200 and `Access-Control-Allow-Origin: *` before implementation. [Socrata's text datatype documentation](https://dev.socrata.com/docs/datatypes/text) confirms case-sensitive endpoint handling with upper() and doubled single-quote literal escaping used by this query builder.

- Explicit submit only; fixed Seattle endpoint and allowlisted columns. No proxy, intake submission or automatic query on page load/typing.
- House-number prefix plus bounded street tokens; escaped apostrophes, no user wildcard/query syntax. Candidate matching is intentionally not an identity guarantee; alternate address formats need the official source.
- Fetches at most 21 rows and displays at most 20 with a truncation warning. No-result, transport/rate-limit/timeout/invalid-response states are distinct.
- Permit issue date, decommissioning date, work method, fill material and tank size stay separate. Missing completion details produce an attachment/report follow-up, not a completed finding. A populated row is still not the underlying signed report.
- Terms go directly from the browser to Seattle Open Data only on Search. No credentials, referrer, app-side query storage, URL parameters or query-value analytics. Provider logging is disclosed before submission and in Privacy. Clear cannot delete provider logs.
- CSP allows only the exact Seattle data host for connections; no wildcard, backend request destination or user-controlled source URL.
- Reset aborts pending requests and clears results; generation checks prevent stale results overwriting a newer search. DOM output uses textContent, not HTML interpretation.

## Product / SEO contract

New shared profiles hold route content, search action, evidence checks, draft scope and record-family classification. The existing Entry source links still feed the finder, official actions, worksheet and operational source overrides. Finder filters distinguish completion, environmental and registration intent from route type; state options derive from actual coverage. Every route remains a crawlable server-rendered link without JavaScript. The homepage shows a small set of representative document entrances plus the full finder, not an increasingly long unfiltered task list.

The frontend skill influenced the implementation: retain the restrained green/white utility and make lookup/interpretation the primary action, without another decorative redesign. Existing URLs and private operational storage remain untouched.

Demand caveat: official-route availability and distinctive instructions do not establish keyword volume, ranking opportunity or conversions. Seattle/Brookhaven's earlier clicks motivate useful record retrieval, but do not prove the new states will respond equivalently. Evaluate new agency and local cohorts separately using final GSC data after an authorized release. Do not infer absence from delayed reporting or claim sitemap resubmission guarantees indexing.

## Verification

- Final `.\mvnw.cmd verify -q`: exit 0; **71 tests, zero failures/errors/skips**. Catalog differentiation, relationships, guide/evidence/draft scopes, all catalog HTTP/canonical/redirect/link checks, assisted-service boundaries, metadata, explicit Seattle disclosure and narrow CSP permission pass.
- Packaged preview at `http://localhost:18082`, task-owned PID 52544, separate copied `target/broad-record-preview.jar`, isolated ignored QA storage, notifications/analytics disabled. Only the prior task-owned preview process was replaced; no customer files or production system were changed.
- Runtime sitemap: **105 URLs**, all HTTP 200, single H1, matching self canonical and no noindex. Existing source-review dates remain distinct from actual editorial updates. JavaScript syntax checks pass for both modified/new modules; `git diff --check` has no whitespace errors (existing Windows line-ending warnings only).
- Browser: **21 pages × 1440/768/375 widths = 63 layout checks**, covering all 17 new routes, Seattle, home, finder and directory. No measured page/control horizontal overflow, header-brand containment failures or uncaught page errors. CT completion=4, CT environmental=1, PA registration=1; unsupported combination produces explicit no-coverage guidance.
- Seattle mocked states: empty, populated, incomplete, result cap, invalid JSON, wrong response shape, malformed row, transport failure, HTTP 429, invalid wildcard input, reset/stale response and timeout. Candidate output renders HTML-like source text safely as text. Lookup changes neither worksheet/session nor local storage and never adds terms to the app URL. No automatic request while typing. The expected mocked transport/429 browser console messages are not uncaught application errors.
- A deliberately synthetic non-property query completed through the **real Seattle endpoint from the browser**, confirming query syntax, CORS and CSP end to end. Its empty response is not a real-property search or proof of source completeness. Populated-result interpretation used synthetic mocked rows, not an actual customer file retrieval.
- No-JavaScript browser: all 58 finder links visible, direct-query form hidden, official-source fallback visible. Native deep link lands below the sticky header. An initial screenshot reused a same-document focused/scroll state; a fresh finder→Seattle deep-link rehearsal confirmed correct positioning and was added to the final checks. No application scroll workaround was needed.
- Final CLI rehearsal: `output/playwright/broad-record-qa.js` (ignored). Inspected screenshots: `output/playwright/broad-seattle-desktop.png` and `output/playwright/broad-finder-mobile.png` (ignored). Mobile filter wording was shortened, and the Seattle reading navigation now includes the lookup itself.
- No deployment, commit/push, Search Console submission, manual indexing or IndexNow action was performed. Next safe step is an authorized reviewed release through normal CI/security/private-backup gates, then live verification and updated sitemap submission. SEO recovery and new-cohort demand remain unproven until actual post-release evidence exists.

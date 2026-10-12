# Record-route depth and next regional cohort — 2026-10-11

## Authorization and objective

Owner approved all proposed work: deepen Seattle and Brookhaven, improve eight agency/document search landings, and implement a researched next regional cohort. This is an implementation record, not a deployment or agency-request receipt. No customer case research, private-store changes, paid acquisition, outreach or external sending.

## Decisions and inventory

- Preserve all existing canonical paths and assisted-intake scope (NJ/NY only).
- Eight agency pages now have document-specific search titles, immediate official start actions, preparation, expected evidence, empty/incomplete-result fallback and visible record questions. The action resolves its URL from the same source-overridden Entry as the numbered route and worksheet; there is no second hardcoded outbound URL list.
- Seattle: exact SFD field names, issue versus decommissioning dates, method versus removal, blank follow-up report gap, pre-1996 coverage, commercial and city-limit boundaries. Links to separate nearby custody routes rather than claiming countywide SFD coverage.
- Brookhaven: completed Digital Services transition, pre-Sep-28-2026 application-help threshold, existing-file FOIL versus new applications, parcel/document-specific request, separate Suffolk Health and DEC custody. Removed stale “upcoming portal change” language.
- Connecticut: added DEEP's 2026 REACT cleanup status/document route without replacing the older Document Search Portal. Existing worksheet restoration matches source titles, so the inserted step does not inherit the old next step's outcome.
- Four new locals: New Rochelle NY, unincorporated King County WA, Bellevue WA, Tacoma WA. Each has its own custody distinction, identifiers, sources, request outline, evidence target and limits.
- Current catalog: 28 local + 8 agency/document + 5 problem = **41 detail pages**, plus directory/finder/examples = **44 catalog URLs**. Expected full sitemap **88**, not 88 record pages. No arbitrary 50–60-page target was reached by thin filler.

## Regional candidate audit

| Candidate | Decision | Official evidence / distinction |
| --- | --- | --- |
| New Rochelle NY | KEEP | [Fire Code Enforcement](https://newrochelleny.gov/384/Code-Enforcement) names removal inspection/tank-status records; [Clerk FOIL](https://www.newrochelleny.gov/675/FOIL) routes the request to departments. This resolves the earlier HOLD's missing tank-specific record target. |
| Unincorporated King County WA | KEEP | [Permit-record research](https://kingcounty.gov/en/dept/local-services/buildings-property/public-notices-permit-records-search/permit-records-research) distinguishes city custody and pre-annexation files; County fuel-tank handout distinguishes residential removal/abandonment certification. Mailing city is not legal jurisdiction. |
| Bellevue WA | KEEP | [Fire F-07 handout](https://bellevuewa.gov/sites/default/files/media/pdf_document/F-07_Decommis_Oil_Tanks.pdf) identifies the report retained as a public record; [City public records](https://bellevuewa.gov/city-government/departments/city-clerks-office/public-records) is separate from [Development Services](https://bellevuewa.gov/city-government/departments/city-clerks-office/public-records/development-services-records). No assertion that every property report was filed. |
| Tacoma WA | KEEP | [Fire historic FAQ](https://cms.tacoma.gov/Fire/Website%202013/Prevention%20Division/Prevention%20Permits/Permits/Permit%20support%20docs/Permit%20Frequently%20Asked%20Questions.pdf) identifies recorded decommissioning certification; current [Public Records Office](https://tacoma.gov/government/departments/public-records-office/) supplies channel instructions. [Health scope](https://tpchd.org/ufaq/what-about-home-heating-oil-tanks/) explicitly excludes small heating-oil tank records. Do not copy old permit fees or claim no permit is required. |
| Kirkland WA | HOLD | Official Fire Prevention bulletins identify a residential-heating-oil permit exception; general UST permit language is not interchangeable. No distinct historic report retrieval workflow sufficiently established here. |
| Shoreline WA | HOLD | Search did not establish a distinctive tank-document retrieval route. Not a claim that no records exist. |
| Hempstead / North Hempstead NY | HOLD | Generic FOIL and permitting evidence did not resolve the prior historical tank-file/current-channel custody gap. Do not substitute a neighboring village's portal. |

## Source-check ledger (public, non-case)

- Seattle: official `data.seattle.gov/api/views/xvj2-ai6y.json` read directly; verified dataset description, code 6103, 1996 start and named columns. No customer-address query or downloaded property evidence.
- Brookhaven: live Building and FOIL instructions read, including old-application threshold and agencies not held. Portal forms were not submitted.
- Oregon: current DEQ HOT-record fact sheet read; clean PLC filters, Projects leak type, short address keywords and Apr-3-2024 older-file caveat retained. No new integrated portal search or proof of exhaustive downloads.
- NYSDEC: current FOIL guidance read, including online resources and GovQA tracking. Spill listing remains distinct from reports.
- Maine: current DEP File Room/Reports guidance read; registration, finalized spill documents, query-value/wildcard and unscanned-file distinction retained.
- CT: current FOIA instructions read, including REACT's 2026 cleanup uploads, no-sign-in property search and older submissions in the document portal. Portal endpoint also returned HTTP 200; that alone is not a retrieval test.
- NJDEP: direct reader returned 403 for NFA/OPRA; official indexed NFA/FAQ and DocMiner announcements corroborated prior guidance. Keep prior Sep-30 source-review dates rather than advancing them on blocked access. Editorial `dateModified` and sitemap lastmod advance for actual new content independently.
- New local evidence: official instruction text/search excerpts reviewed. Public Bellevue/City Tacoma/New Rochelle/CT endpoints returned HTTP 200. No customer document availability is claimed; request submission and agency fulfillment were not tested.

## UX and maintenance contract

Visual thesis: calm green/white record utility, with the official action visible before the long workflow. Content plan: exact record question → official start → identifiers and target/fallback → detailed steps/evidence → private worksheet and request outline. Interaction thesis: retain existing sticky reading orientation and focus/hover affordances; no new ornamental animation or signup gate.

`RecordSearchGuide` holds editorial titles, start-step positions and answers. `ResearchCatalog.Entry` remains source truth for public links, finder, worksheet and route inventory. Dates distinguish editorial updates from instruction review. Existing protected case desk, append-only ledger and source-override file remain untouched.

## Verification

- Final `.\mvnw.cmd -q verify`: exit 0; **69 tests, 0 failures/errors/skips**. Includes all catalog routes, canonical inventory/sitemap, internal links, assistance boundaries, protected diagnostics, guide step positions, metadata and changed-versus-unchanged lastmod assertions.
- Packaged runtime at `http://localhost:18082`, isolated ignored `target/october-record-qa-storage`, notifications and analytics disabled. Preview executes a separate copied JAR so a build cannot overwrite the running archive.
- Runtime sitemap: **88 URLs**; all returned HTTP 200, one H1, self canonical, no noindex. Home, finder and improved NJ NFA lastmod Oct 11; untouched Montclair remains Sep 16. Source-review dates are not blanket-advanced.
- Browser: 13 improved routes × widths **1440, 768, 375**, height 900 = **39 checks**, zero page errors, no measured primary-action/worksheet-control overflow, primary action in initial viewport. Desktop Seattle and mobile Brookhaven screenshots inspected; stored in ignored `output/playwright/`.
- Finder WA + local family: exactly 3 local matches (unincorporated King County, Bellevue, Tacoma). Seattle remains the separate agency/document route.
- Synthetic non-property worksheet rehearsal: draft and empty-source next action updated; default session storage only, no device save, no property text in URL. Navigation/restoration retained identifiers and outcome; Clear emptied fields/outcomes and both worksheet stores. No external request sent.
- An intermediate verification package attempt collided with the running build JAR on Windows. Stopped only the task-owned preview process, rebuilt, switched to a copied preview archive, and repeated checks. Final verification succeeded; no production system or private customer storage was affected.
- Not deployed or committed/pushed by this turn. No sitemap resubmission, manual indexing or IndexNow submission is claimed. Next safe release step: reviewed diff and normal deployment/security/private-backup gates, then live verification and updated-sitemap submission.

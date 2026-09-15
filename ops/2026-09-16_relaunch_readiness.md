# Record-research relaunch: implementation and publication gate

## What is being tested

This is a full-service repositioning, not a small landing-page A/B test. The original informational contractor-lead model did not produce useful oil-tank search demand. Septic's broad county coverage revealed pockets of demand for hands-on record retrieval and interpretation. Oil Tank Route now launches a substantial, differentiated acquisition cohort around that service, then evaluates which localities and questions produce useful cases.

Breadth is an intentional discovery mechanism. Do not remove most routes merely because another locality receives the first inquiry. Do not create more local pages by substituting place names without verified differences in sources, identifiers, record types, or request channels.

## Local scope implemented

The immutable `ResearchCatalog` is the shared source for public pages, related links, inventory IDs, sitemap paths, review dates, and form defaults. These are public guidance observations, not operational case lessons or claims that records were retrieved.

| Area | Distinct starting point | Boundary |
|---|---|---|
| Montclair, NJ | SDL property portal, Township OPRA, separate NJDEP file | Portal result is not the full archive |
| Maplewood, NJ | Explicit Certificate of Oil Tank Removal record category | Requestable category is not proof a certificate exists |
| Summit, NJ | SDL permit link followed by non-police request portal | Permit issuance is not completion |
| Wayne, NJ | Self Service Portal, separate property/map identifiers, OPRA fallback | No claimed universal historical coverage |
| Nassau, NY | Small heating-oil tank verification route distinct from bulk storage | Do not schedule new work to research historic records |
| Suffolk, NY | Full district/section/block/lot; county Health file vs municipality | Not a verified building-office route for every town/village |
| Westchester, NY | Dated PBS map, closure-document categories, program contacts | June 2017 map is not current tank inventory; applicability needs confirmation |
| NYC | BIS + DOB NOW + FDNY tank report categories | Confirm conflicting webpage/form submission guidance before sending/paying |

Five question pages: missing removal records; seller removal claims; conflicting documents; agency request routing; records versus a physical tank survey. Two public-source walkthroughs share one examples page. One directory makes all 13 detail pages discoverable without JavaScript.

Total: **15 new indexable URLs**, not including existing service pages or the three preserved NY county incident URLs. Existing county URLs now have dedicated inventory IDs, local-research links, and correct intake origin attribution. They retain their distinct incident-data intent rather than being silently redirected to a different subject.

## Evidence integrity and freshness

- Official source links appear next to the relevant guidance, with a September 16, 2026 review date. Recheck by December 16, 2026, or immediately on a broken link, agency correction, or routing failure.
- Nassau and NYC walkthroughs explicitly state that no property was searched and no agency request was sent.
- The sample brief is explicitly fictional/illustrative, not a redacted completed customer delivery.
- A verified source page is not proof of a working end-to-end agency fulfillment process. Account access, fee approvals, archive coverage, exemptions, and processing times may remain untested.
- Public catalog additions must not become `route-lesson` case activities unless there is an actual relevant case event. Actual cases continue to follow `record_research_operations.md` and their private append-only history.
- No customer evidence, credentials, or uploaded records belong in the catalog, this document, or git.

## Before/after measurement baseline

GSC audit obtained before this relaunch:

| Period | Clicks | Impressions | Average position |
|---|---:|---:|---:|
| 2026-07-20 through 2026-08-16 | 5 | 805 | 49.6373 |
| 2026-08-17 through 2026-09-13 | 8 | 2,438 | 47.1296 |

All eight latest-period clicks landed on utility pages: 275-gallon (2), heating-oil-tank (2), gauge replacement (2), cost calculator (1), tank sizes (1). This is not evidence of traction for the research service. The inspected NJ records page was unknown to Google. The 275-gallon URL was indexed; last observed crawl August 28, 2026.

The production audit still showed the older utility homepage and 404s for `/record-research/`, `/how-it-works/`, and `/sample-brief/`. These observations predate publishing the rebuild. Do not describe later local test results as a production improvement.

## Publication gate — not executed

Local verification completed on September 16:

- Maven packaged build passed: **57 tests, 0 failures, 0 errors, 0 skipped**. A clean package also passed before the final analytics/freshness integration; the final package was rebuilt and the complete suite rerun after those changes.
- All 15 new URLs rendered with canonical metadata and sitemap inclusion. Integration checks followed their internal links, verified source-page form defaults, rejected unknown/wrong-kind slugs, and preserved three county intake origins.
- Packaged-app Chromium check: 15 URLs at both 320px and 1440px (30 viewport checks); no horizontal overflow, missing H1, missing detail source blocks, or console errors detected. Representative 390px mobile screenshots were visually reviewed as well.
- A local, fake Nassau intake produced a receipt, returned to the Nassau page, focused the confirmation, and cleared the saved draft. Notifications were disabled. Final-build CTA telemetry was observed with `research:nassau-ny`, its original path, and `new-york`.
- `git diff --check` passed. No commit, push, production deployment, or real email was performed.
- Local review server: `http://localhost:18080/`; test data and screenshots are under ignored `output/`. The local browser view is not the production site.

The owner's prior review-before-deployment condition remains in force. Local implementation and tests do not authorize commit, push, merge, production deployment, GSC sitemap mutation, or real email sending.

After owner review and explicit publication authorization:

1. Review the entire dirty branch, not just the new catalog. Keep private storage and credentials out of the diff. Build the tested artifact.
2. Verify production storage backups, persistent volume paths, base URL `https://oiltankroute.com`, admin protection, upload limits, and notification configuration. Confirm both operator and customer receipt readiness without printing credentials.
3. Deploy the coherent service release. Record deploy timestamp and revision as the measurement anchor. Existing utility canonicals remain unchanged.
4. Check production home, service pages, all 15 new URLs, three legacy county pages, assets, canonical/robots output, slash redirects, and sitemap. Confirm that unknown locality URLs return 404, not a generic 200 page.
5. Submit one explicitly authorized labeled test intake; verify private case, receipt, notification status, and the actual origin page. Never silently send a real agency request as a smoke test.
6. Confirm the existing GSC property and sitemap submission state; submit the canonical sitemap if needed. Inspect representative homepage, directory, NJ area, NY area, problem, and preserved utility URLs. An indexing request is not proof of indexing or ranking.
7. Record GA4 connection/access status separately. Do not claim GA4 validation without access and an observed event. The app's first-party case/event store remains available for qualified-case attribution.

## Evaluate after publication

Use three separate views; a rising total is not enough:

- **Discovery:** new service URL crawling/indexing, valid sitemap, canonical selection, and excluded-page reasons. If discovery fails, investigate response status, linking, rendered content, robots, and canonical selection before changing service positioning.
- **Search demand:** clicks/impressions/query intent by locality and problem page, separated from utilities. Compare complete equal-length periods, retain zero-result routes, and disclose GSC lag/anonymized queries. Position changes on a new query mix are not direct ranking comparisons.
- **Service value:** qualified requests, question mix, usable record findings, referrals, pending requests, deliveries, and customer follow-up by origin page and actual case jurisdiction. A form submission is not a successful delivered case.

At 7 days, inspect discovery and technical failures; do not pronounce the business experiment won or lost. At 28 days, compare a complete search window and case cohort, with pending cases still visible. These are review checkpoints, not ranking deadlines. Investigate stronger localities deeply while retaining the broad researched starting cohort unless evidence exposes poor content or incorrect coverage.

No recurring automation was created. No search recovery is guaranteed. Publishing meaningful, discoverable service content gives search engines new material to assess; it is not a ranking reset switch.

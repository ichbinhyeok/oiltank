# Record Finder local verification — 2026-09-30

Follow-up supersedes the initial counts below: **65 passing tests, 40 catalog URLs and 84 full sitemap URLs**. See `2026-09-30_record_finder_depth_review.md` for new local evidence, document-response verification, browser checks and remaining external-source limitations. The rest of this file preserves the initial implementation audit.

Branch: `codex/record-finder-pivot`. Production is still the September 16 deployment. No commit, push, deployment, Search Console submission, IndexNow submission, customer intake or agency message was performed in this implementation turn.

## Build and automated regression

- Maven full package/test: **64 tests, 0 failures, 0 errors** on local Java 23 targeting release 21. This is not a claim that the production Java 21 CI/security scan ran.
- Preserved 20 utilities and legacy route behavior; all new research routes checked for canonical, sitemap membership, related local destinations and assisted-intake boundary.
- New protected source-health endpoint returns 401 without auth; authenticated test verifies health/review information without customer identifiers/private paths.
- Operational override compatibility tests retained. Added a DataMiner source-change rehearsal: one stable-key override updates every matching detailed source and leaves other baseline sources present. No private case directory is created.
- `node --check` passed for worksheet and shared app JavaScript. Final static-only corrections repackaged after tests.
- `git diff --check` passed. Existing unrelated dirty deployment/history files preserved.

## Browser checks

Synthetic fixtures only, isolated local `target/pivot-qa-private`, notifications disabled. No real form submission.

- Five worksheet flows: NJDEP NFA, Nassau verification, Oregon clean, Oregon leak, Seattle. Address/document preparation, per-source empty outcome, reload restoration, explicit device save and clear passed. NJ/Nassau retain intake; OR/Seattle do not.
- Sixth flow: unsupported jurisdiction shows a coverage limitation and manual custodian next step rather than accepting national research.
- CT portal route included; its browser UI and no-intake boundary checked.
- All six widths (320, 390, 768, 800, 960, 1440) checked on home, finder, Nassau, Oregon clean and CT. Check actual visible-element bounds, not just `scrollWidth`.
- Found and fixed a mobile grid intrinsic-width defect caused by the sticky/on-page nav; initial document-overflow-only checks missed it because the legacy body clips overflow.
- Storage denial leaves the worksheet usable and reports save failure. Clipboard denial selects the draft and gives manual-copy instructions. No false “copied” success.
- Expired device state is removed on reopening. Keyboard address → parcel order verified. Explicit clear removes the selected route's session/device state.
- No-JS browser context: three official links, request outline and limits remain readable; private fields disabled; unsupported-state intake absent.
- Print emulation found the legacy calculator-passport rule hiding all other content. Scoped worksheet print visibility and a full wrapping text version of the draft added; no change to calculator print scope. The test requests printing; it does not prove a physical printer completed a job.
- Synthetic HTML-like input stays literal in inputs/drafts. Worksheet analytics capture contained only route/document/outcome values, not the synthetic property marker; URL unchanged. This verifies the payload contract, **not live GA4 ingestion**.
- Reduced-motion preference respected by same-page navigation; sticky public header height used in anchor positioning.

## SEO and source evidence

- Local sitemap crawl: **81 distinct URLs**, all HTTP 200, self-canonical, one H1 and no unintended noindex. 81 includes utilities/guides, not 81 record pages.
- Record-specific catalog has **34 details + 3 hubs/examples = 37 URLs**. Additional legacy record/service surfaces are preserved without being double-counted into this cohort.
- Public original-source checks and 16-candidate decisions are in `2026-09-30_record_finder_launch_inventory.md`. NJDEP reader access was restricted; not every external portal was tested end to end. Do not describe public guidance as automatic data retrieval or live document verification.
- Oregon real PLC/project modules and relevant filters checked; Maine real registration search reached explicit no-match using a synthetic non-property token; CT real query form loaded; Seattle public dataset metadata and public landing checked.
- Broken-link/portal monitoring is not scheduled. Source-health reports known configuration/override/review status, not live network uptime.

## Remaining release work / limits

- Preview: `http://localhost:18081/`, using a copied build artifact and isolated storage. The prior `18080` environment was not stopped or overwritten.
- Production release still requires the existing Java 21 CI/security gate, private-storage backup, deploy/rollback procedure and public smoke check. Only then submit the new sitemap / changed URLs; don't repeatedly submit unchanged URLs.
- Actual GA4 receipt, GSC discovery/indexing and organic demand remain unverified for this unshipped cohort. No ranking/revenue guarantee.
- 11 extra municipal candidates are HOLD, with reasons, instead of creating weak locality copies. The planning capacity of 50–60 record pages was not achieved and is not disguised by the full sitemap total.
- Review ownership remains with the owner; 90-day source review defaults are visible. Maintenance labor has not been timed sufficiently to offer a reliable monthly workload estimate. No automation or external follow-up was silently created.

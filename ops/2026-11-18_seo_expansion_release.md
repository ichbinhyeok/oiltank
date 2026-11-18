# Heating oil SEO expansion — November 18, 2026

Latest scope: the owner requested a larger content pass. See `2026-11-18_seo_content_wave.md` for the 21-state/eight-guide expansion and its newer verification. The first-wave counts and preview below are historical receipts.

Authority: owner requested further actual work after reviewing the SEO expansion research. This release implements the broader acquisition direction locally. It does not claim production deployment, indexing gains, customer inquiries or revenue.

## Implemented scope

- Rebuilt the homepage's first sections around prices, tank calculators and records; existing private intake and document/record entry paths remain.
- Added eight state-price URLs: Connecticut, Massachusetts, Maine, New Hampshire, Rhode Island, New York, New Jersey and Pennsylvania. National tables include the 21 states in the reviewed EIA residential release.
- Price pages include actual six-observation history, previous-observation changes, an accessible chart/table, 100/150/200/250-gallon subtotals, tax/fee budget calculation, source dates and CSV download. Initial observations Oct 5–Nov 9; source release Nov 12, next scheduled release Nov 18. Source revisions are retained at the date they affect.
- Replaced the March hard-coded price catalog with a validated bundled baseline and persistent override at `storage/market/heating-oil-prices.json`. Production checks the fixed EIA HTTPS source every six hours after an initial minute. Complete validation and atomic persistence precede replacement; errors retain the last valid snapshot. Local/tests disable refresh by default. The public page flags a missed expected release after two days.
- Added a supplier directory using six businesses' own public coverage/order pages, spanning selected areas of eight states. A state filter narrows those statements; it does not claim address matching or complete statewide coverage. No order/contact/payment is submitted. Listings show source-check dates and direct supplier destinations; no live-price or partner-network claim.
- Added 330-gallon and Roth size pages; expanded the shared model set from six to nine with Roth 110/165/400-gallon published dimensions. No unsupported Roth inch-to-gallons chart was introduced.
- Kept full 275/330 charts on their respective model pages; chart library links to these destinations and retains the 138 table. Added print controls.
- Put a functioning gauge calculator on the tools hub and corrected its page attribution. The cost calculator now compares two complete quotes with quoted fuel tax and fees. Planning space can transfer from the order calculator in the same browser session.
- Added concise, source-consistent three-step workflows to CT DEEP, NYSDEC spill, NJDEP NFA, NYSDEC PBS, Seattle and King County record pages. Existing instructions retain their actual source-review dates; content-modified metadata reflects the November edits.
- Registered the new canonicals, slash redirects and sitemap entries, and extended deploy canaries for prices, one state page, the supplier directory and the 330 chart.

## Evidence and verification

Official source: https://www.eia.gov/dnav/pet/PET_PRI_WFR_A_EPD2F_PRS_DPGAL_W.htm . The seed and test fixture were fetched directly on Nov 18 and include source publication/next-release dates. Manufacturer and supplier receipts are documented in the research note and their linked public destinations.

Final verification:

- Full `mvnw.cmd -q verify`: 85 tests, zero failures/errors, exit 0. Local Java 23; compilation targets Java 21. No production Java 21 CI or image scan is claimed.
- Final JS adjustment preserves the tank model and remaining gallons across gauge → usage → order → quote. Both JS files pass `node --check`; final packaged build with this adjustment exits 0. The complete browser flow was checked against that final package.
- Direct HTTP audit of all 122 sitemap URLs on the packaged preview: all 200, self-canonical, one H1, no HTML noindex, no orphan within the crawled link graph. This is not a Google-indexing claim.
- Browser: 33 layouts (11 representative pages × 375/768/1440), one H1, no page-wide horizontal overflow, valid JSON-LD. Gauge on the tools hub appears before its route directory.
- Budget: 200 gallons × $5, 5% quoted fuel tax and $10 fees produces $1,060. Server accepts the event with the endpoint's intended 202 response and persists it; no gallons, price, tax, fees, email or address fields appear in that event payload.
- Complete flow: 275 model half-height → stored 136.9 gallons (rounded UI 137) → usage calculation → order planning → quote quantity. With 100 gallons, 10 reserve and 3/day, the runway is 30.0 days; order space at an 80% target on 275 is 120 gallons and transfers to the quote form.
- Quote comparison: 200 gallons at $5 + 5% fuel tax + $10 fees = $1,060; $4.90 + the same fuel tax + $25 fees = $1,054; difference $6.
- Supplier state handoff selects OK Petroleum for NY; MA filter exposes three sourced suppliers. No external order or contact form was submitted.
- JavaScript-disabled CT page retains six history rows and 21 state rows. Final functional browser run: zero page errors and zero failed local responses.
- Actual Java HTTP refresh fetched EIA and persisted 24 valid area series/six observations to the isolated runtime cache. This verifies the source → parser → validation → persistence flow, not future source uptime.
- Desktop/mobile screenshots were visually reviewed. A capture taken mid-entrance animation was replaced with a completed-animation screenshot. The first budget QA discovered a missing event scenario; it was repaired and the final event/persistence checks above passed.

Final preview: `http://localhost:18119/`, task-owned Java PID 30808, JAR `output/seo-expansion-20261118/preview-final.jar`, isolated storage `target/seo-expansion-preview-final-storage`. Customer/operator email and analytics are disabled; source refresh is enabled. The older task preview was stopped. Initial build/preview JAR sharing caused a local repackaging conflict; the final preview runs a separate copy so future builds cannot replace its running artifact.

Final preview JAR SHA-256: `E690594A95B99BBE868925AFB86F133BD3555621A105624A4552D59676537F62`.

Receipts: ignored `output/seo-expansion-20261118/verify.log`, `final-package.log`, `browser-qa-summary.json`, `local-final/summary.json`; screenshots and browser script under `output/playwright/`. Production remains unchanged; no commit/push/PR, deployment, customer/agency message, Search Console write or IndexNow submission was performed.

## Operating notes

- Runtime refresh is enabled by the prod profile and can be disabled with `BURIED_OIL_TANK_PRICE_REFRESH_ENABLED=false`.
- Refresh preserves older dates and source revisions while retaining at most 110 observations. A successful HTTP response alone is insufficient: dates, all 24 area series, numeric bounds and ordering must validate.
- The CSV contains public source facts only. Budget values are not submitted in analytics; the event carries page/state/tool context only.
- Supplier facts were checked Nov 18 and should be reviewed by Dec 18. Prices and slots remain on supplier sites. Their licensed status and service quality were not independently assessed.
- There is no forecast of a particular ranking, CTR or income. Compare the newly published price/tool/record cohorts separately after an authorized deployment.

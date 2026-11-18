# November 18: second SEO content expansion

Owner direction: expand the SEO/content implementation more substantially. This supplement supersedes the first wave's eight-state scope and preview/counts once the final verification below is completed. Work is local; production and search-engine indexing are separate outcomes.

## Implemented scope

- Expand dated EIA price/history/budget destinations from eight to all 21 states in the validated source dataset. Each uses its actual state series, comparison with the U.S. benchmark, six observations, editable delivery budget and state-specific quote note. Do not fabricate pages for states without data.
- Add `/heating-oil-guides/` and eight purpose-specific guides: 100-gallon orders, 150-gallon orders, cash/COD, automatic versus will-call, minimum delivery, fixed versus capped prices, gallons-to-days, and oil versus propane.
- New guide editorial text totals 4,504 words by a simple word-token count, excluding common template text, navigation and sources. Each has a direct answer, five substantive sections, worked examples, related actions and public source links. This count is a scope receipt, not a ranking signal.
- Add editable 100/150-gallon budgets with all 21 state subtotals, a reserve-aware days calculator, and a useful-heat comparison with equipment efficiencies and propane break-even price. Worked prices, tax rates, efficiencies and consumption scenarios are explicitly illustrative. No claimed national household consumption average or universal installed gauge price.
- Expand the existing gauge replacement destination with a five-symptom comparison table, replacement-quote scope, information to provide for service and planning with an unreliable gauge. Keep this intent on its existing canonical instead of adding a competing stuck-gauge URL.
- Connect homepage, price pages, delivery directory, tools and contextual product pages to the guide cluster. Add Article/CollectionPage metadata, canonical routes, sitemap registration, slash redirects and print controls.
- Supplier directory remains six selected suppliers in parts of eight Northeast states. The 13 extra price states do not imply supplier coverage. State filters are derived from actual supplier coverage; uncovered price pages link to the explicitly Northeast directory without a false state match.

Inventory: 169 registered entries / 139 indexable entries; five additional static sitemap destinations give **144 sitemap URLs**, versus 122 after the first local wave and 111 in the last verified production release. This wave adds 22 URLs (13 state destinations + eight guides + one hub).

## Source basis and limits

- EIA dated public residential series: https://www.eia.gov/dnav/pet/PET_PRI_WFR_A_EPD2F_PRS_DPGAL_W.htm . Last bundled observation November 9, release November 12, next scheduled November 18; dates remain visible. Automatic refresh/cache from the first wave is retained.
- EIA conversion factors: https://www.eia.gov/energyexplained/units-and-calculators/british-thermal-units.php . Retrieved November 18; sample factors 138,500 Btu/US gallon oil and 91,452 propane. Useful-heat formulas and all worked comparisons are transparent calculations from labeled inputs.
- EIA heating demand context: https://www.eia.gov/energyexplained/units-and-calculators/degree-days.php , retrieved November 18. No weather forecasting feed or house-size consumption claim was added.
- Massachusetts consumer overview: https://www.mass.gov/info-details/understanding-un-regulated-utilities . Current official search excerpt supports fixed/capped definitions; direct retrieval returned 403. The guide asks contract questions and uses illustrative arithmetic; it does not restate state legal rights as national rules.
- New supplier-plan examples: https://www.fieldingsoil.com/automatic-delivery and https://welchheatingoil.com/fuel-delivery/ , retrieved November 18. The guides separate scheduling, payment, price protection and service. No dealer rate or future delivery availability was copied as an offer.
- Existing source-checked suppliers and Granby chart remain attributed on the pages. The old Maine legal PDF was inspected during research but dated 2004 and was not used to publish current legal rights or a national minimum-delivery rule. NY maintenance PDF returned 403 during this wave; existing source-ledger review dates were not relabeled as a fresh retrieval.

No new outbound orders, agency/customer communications, private intake inspection, production writes or search submissions are part of this work. Actual inquiries remain unverified because the owner cancelled that lookup.

## Verification

- Full `mvnw.cmd -q verify` exits 0: **87 tests, zero failures/errors**. Local Java 23 compiles to Java 21; this is not a production Java 21 CI or image-security result. Both public JS modules pass `node --check`.
- Final separate-copy packaged preview: `http://localhost:18120/`, Java PID **34516**, unified execution session 50774; JAR `output/seo-expansion-20261118/preview-content.jar`, SHA-256 `25B9490B34C81075E2D0899A0CD8370E4E0709627E23A766309F3BEF9D91EF91`. Task-owned older preview on 18119 was identity-checked and stopped. Other user processes were not stopped.
- Preview uses isolated `target/seo-content-preview-storage`, disabled email and GA measurement ID, and disabled periodic source refresh to keep the browser arithmetic checks reproducible. Production refresh remains enabled by the existing production configuration; the first wave already verified live EIA fetch/parse/validation/persistence.
- HTTP audit: **144/144** sitemap URLs return 200, self-canonical, exactly one H1 and no HTML noindex. No orphan in the crawled internal graph; no duplicate title or meta description. These checks do not establish Google indexing or future traffic. Latest `local-final` receipts now describe port 18120 and supersede that folder's first-wave output.
- Browser: **69 layouts** at widths 375/768/1440 (36 guide/new-state/gauge combinations plus 33 baseline homepage/price/supplier/tool/record combinations). No page-wide horizontal overflow, page errors or failed local responses; JSON-LD parses. New-guide jump links resolve to real anchors.
- New calculator checks: 100 × $5 + 5% fuel tax + $20 fee = **$545**; 100 remaining − 10 reserve at 3/day = **30.0 days**; reserve above balance clamps to **0.0** and zero daily rate fails browser validity. Oil $5 at 85% and propane $3 at 90% yields **$42.47/$36.45 per useful million Btu** and **$3.496/gallon** break-even propane; reversing the quoted price advantage correctly reverses the lower-cost fuel.
- Existing complete flow still works: gauge → usage → available order space → quote quantity. A 275 model half-height stores 136.9 gallons (UI 137); 100 remaining with an 80% nominal target produces 120 planning gallons transferred to the quote form. The original two-quote $1,060/$1,054 comparison still differs by $6.
- JavaScript-disabled order guide retains all 21 state budget rows and four consumption scenarios; existing price history/state tables remain server-rendered. Supplier NY handoff and MA three-supplier filter pass.
- The three new guide-calculator event types receive 202 and persist in isolated synthetic storage. Payloads contain page/state/tool context only, with no entered quantities, prices, efficiencies, tax, fees or contact details. The final isolated log has 18 synthetic tool events from the two browser passes; these are not customer inquiries.
- Desktop and mobile guide/hub/calculator screenshots were visually reviewed. Receipts: ignored `output/seo-expansion-20261118/verify-content-final.log`, `content-browser-summary.json`, `content-base-browser-summary.json`, `content-http-audit.log`, `local-final/summary.json`; browser scripts/screenshots under `output/playwright/seo-content-*`.
- Production remains unchanged. No commit, push, PR, deployment, customer/agency send, Search Console write or IndexNow submission occurred.

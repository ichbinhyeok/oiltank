# SEO expansion and positioning research — 2026-11-18

Status: RESEARCH AND RECOMMENDATION ONLY. The owner stopped the administrator-data check and requested ways to strengthen SEO, broaden positioning, and expand impressions and clicks. Production case access failed at authentication; real inquiry counts remain unknown. No customer research, sending, case-state changes, application changes, deployment, or search submissions occurred.

Follow-up: the owner subsequently authorized implementation. The local expansion and completed verification are documented in `2026-11-18_seo_expansion_release.md`. The research status above describes this original investigation, not the later implementation.

## Recommendation

Expand Oil Tank Route's public positioning to residential heating-oil prices, tank tools, and property records. Keep the existing record product and URLs. Build a substantial coordinated release around current fuel costs and useful tank calculations; do not keep all acquisition work confined to municipal record routes.

The strongest expansion is a dated, regularly refreshed state-price and delivery-budget product. Pair it with deeper manufacturer-backed tank tables and immediate calculator results. Improve the record pages that already receive clicks in the same release. This is a proposed acquisition strategy, not an approved change to NJ/NY assisted-service coverage or free beta pricing.

## Current evidence

Search Console read on November 18, final web data, domain property `sc-domain:oiltankroute.com`, no country/device filter:

| Window | Property clicks | Property impressions |
|---|---:|---:|
| Sep 20–Oct 17 | 20 | 2,166 |
| Oct 18–Nov 14 | 55 | 3,505 |

Property totals were separately verified without dimensions. Page aggregation for the recent window totals 56 clicks and 3,630 impressions and must not be substituted for the property total. Named queries expose only 2 clicks; most click-query attribution is withheld. Do not infer the exact queries behind all page clicks.

Recent page observations:

| Page | Clicks | Impressions | Average position |
|---|---:|---:|---:|
| King County WA | 7 | 68 | 7.88 |
| CT DEEP spill documents | 6 | 289 | 6.93 |
| Record Finder | 4 | 207 | 9.09 |
| NJDEP NFA letter | 4 | 68 | 11.07 |
| 275-gallon tank | 3 | 258 | 7.77 |
| NYSDEC spill documents | 2 | 365 | 9.64 |
| Seattle residential records | 2 | 172 | 12.41 |
| Heating-oil usage calculator | 1 | 135 | 7.61 |
| Gauge replacement | 1 | 361 | 34.51 |
| Heating-oil prices | 0 | 26 | 8.69 |

The document-gap checker returned no page row in this window. URL Inspection nevertheless reports it indexed, last crawled Nov 11. Its lack of returned impressions is not established to be an indexing failure.

## Specific defects and missed opportunities

1. **Price freshness is broken.** Direct production HTTP inspection confirms the title still says March 30, 2026; the page retains October 7 as the next release. The source catalog hard-codes that snapshot and the runbook's only recorded review is August 2. EIA currently publishes a November 9 residential observation, including 21 state values. The stale page is not explained by the current seasonal publication pause.
2. **A useful size page currently lacks indexing.** URL Inspection reports `Crawled - currently not indexed` for `/heating-oil-tank-sizes-dimensions/`, last crawled Aug 25. A recent `oil tank sizes` query instead landed on `/heating-oil-tank/` at position 74.67, 48 impressions. This suggests weak query-to-page alignment; it does not prove a single cause or permanent deindexing throughout the reporting window.
3. **The global navigation favors one audience.** The checker receives links from all 111 crawled pages; the price page receives links from four. The header foregrounds document checking and records on active-tank tools too. These are counts of linking pages, not measurements of PageRank. Promote prices, tank charts/calculators, and records as explicit navigation destinations.
4. **The tool hub does not immediately answer calculator intent.** It calls itself an acquisition utility library and presents twenty routes. `heating oil tank calculator` appeared against `/tools/` at position 54.78, 23 impressions. Put model/measurement selection and a usable calculation at the entry surface, with clear links to the existing capacity and level calculators.
5. **Record pages have near-term opportunities.** NYSDEC spill documents receives 365 impressions and 2 clicks; CT DEEP receives 289 and 6. Improve the immediate official search action, query-matched opening, document examples, and request fallback on these existing URLs. A generic title rewrite alone is insufficient; NYSDEC's existing title already mentions the spill database.

## Public crawl and index checks

Direct HTTP audit of all 111 current sitemap URLs: 111 fetched, all HTTP 200, self-canonical, exactly one H1, no HTML noindex, and no sitemap URL without a link from another crawled page. These are public technical checks, not evidence that all URLs are indexed or rank well. No performance or browser-interaction certification is implied.

URL Inspection: price hub indexed; checker indexed; tank-size comparison currently not indexed. Inspection results and raw public crawl are saved in ignored `output/seo-expansion-20261118/`.

## Demand: larger than records, but use the actual evidence

The August 2 Google Ads UI capture targeted the United States, Google network, historical period July 2025–June 2026. These are dated broad bands, not current exact volumes. Advertising competition is not organic ranking difficulty. Close variants overlap.

| Query | Historical monthly range | Implication |
|---|---:|---|
| heating oil prices | 10,000–100,000 | Largest clearly relevant expansion for an official-price and budget product |
| heating oil delivery near me | 10,000–100,000 | Large opportunity only with real supplier/coverage answers |
| cash heating oil | 10,000–100,000 | Mix of generic purchase intent and the established CashHeatingOil brand; do not count all as obtainable generic demand |
| home heating oil prices | 1,000–10,000 | Same product family; do not add naively to the head term |
| heating oil price today | 1,000–10,000 | Requires explicit observation freshness and a distinction from today's dealer quote |
| oil tank sizes | 1,000–10,000 | Expand the existing manufacturer comparison and resolve its indexing/query alignment |
| 275 gallon oil tank | 1,000–10,000 | Improve an existing page already getting clicks |
| heating oil usage calculator | 100–1,000 | Practical tool and internal workflow; not the main traffic denominator |

Evidence files: `analysis/google_ads_price_delivery_keyword_validation_us_2026-08-02.csv`, `analysis/google_ads_utility_keywords_us_2026-08-01.csv`.

The older 107,772 monthly pool and 107.8 clicks/day estimate combines geometric band midpoints, assumed overlap, 3% CTR, and the existing universe. It is a scenario, not a site forecast. In particular, branded cash-oil intent and local supplier intent need separate treatment. Do not reuse it as a traffic promise.

## Opportunity ranking

| Priority | Product | What earns exposure and clicks | Competition / prerequisite |
|---|---|---|---|
| 1 | Current state heating-oil prices + delivery budget | State-specific history, weekly/yearly changes, 100/150/200/250-gallon cost, dated downloadable data, clear quote comparison | Large opportunity, competitive; EIA row republishing alone is weak |
| 2 | Tank size, chart, gauge and remaining-days tools | Manufacturer model coverage, orientation matching, instant result, printable chart and practical reorder workflow | Niche specialists already provide charts and calculators; improve completeness and usability together |
| 3 | Existing official-record search entrances | Direct search route, exact identifier fields, file retrieval instructions and missing-result handling | Already receives relevant clicks; useful supporting growth, not the entire expansion thesis |
| 4 | Real local supplier discovery and quote comparison | Verified dealer areas, publicly posted prices, volume/payment terms, last check, direct supplier destination | High-demand strategic expansion; requires real supplier data, not a ZIP form over state averages |
| 5 | Oil-versus-propane/heat-pump operating cost | Useful-heat comparison from user rates and equipment efficiency | Adjacent option; current exact demand not verified, so it is not the primary expansion justification |

## Coordinated release proposal

The proposed scope covers **17 existing pages and 11 new destinations**, with the supplier destination dependent on real coverage data. Execute the shared data and navigation work together rather than serially waiting for one additional lead per page.

### Existing pages: 17

Homepage and `/tools/`: give existing-tank users an obvious entry to prices and calculators, alongside the record audience. Preserve property-research intake and its actual acceptance terms.

Nine existing utility URLs:

- `/heating-oil-prices/`: national benchmark, actual observation/release dates, history and state selection.
- `/heating-oil-tank-sizes-dimensions/`: expand the six-model comparison with current Granby and Roth primary specifications; make it the canonical size-comparison target.
- `/275-gallon-oil-tank/`: retain one main 275 destination; improve the visible chart, orientation choice, and links into level/order calculations.
- `/oil-tank-gauge-calculator/`: immediate inches/fraction-to-gallons result for supported models.
- `/oil-tank-capacity-calculator/`: retain geometry/capacity intent separately from a known-model gauge reading.
- `/heating-oil-usage-calculator/`: allow the existing gauge-reading data flow, delivery-history rate calculation and useful remaining-days range.
- `/how-much-heating-oil-do-i-need/`: connect current gallons, conservative target and quote comparison.
- `/heating-oil-cost-calculator/`: compare complete supplier quotes, quantities, payment terms and actual fees supplied by the user.
- `/oil-tank-gauge-replacement/`: strengthen observed stuck/not-working intent with manufacturer-supported diagnosis boundaries and a clear alternative measurement route.

Six existing record URLs: CT DEEP spill documents, NYSDEC spill documents, NJDEP NFA, NYSDEC PBS, Seattle residential records, and King County WA. Improve the task-specific opening and retrieval utility; preserve existing scope distinctions.

### New destinations: 11

Eight state-price destinations under `/heating-oil-prices/`: Connecticut, Massachusetts, Maine, New Hampshire, Rhode Island, New York, New Jersey, Pennsylvania. Each must contain actual state series/history, change calculations, delivery-budget calculations, source interpretation and relevant state resources. EIA state rows were directly verified for all eight. The NYSERDA survey also provides state/region comparison and a distinct credit-price basis. Maine and Massachusetts direct state-page access returned 403 in this research; do not claim those feeds were verified end to end.

Two model-family destinations: `/330-gallon-oil-tank/` and `/roth-oil-tank-sizes/`. These answer different physical model questions using manufacturer material. Expand the existing chart hub rather than create duplicate generic chart pages. Do not copy a competitor's computed 550/1,000-gallon geometry as manufacturer-certified chart data.

One distinct supplier-discovery destination: `/heating-oil-delivery/`. It must answer where/how to obtain fuel, separately from `/heating-oil-delivery-check/`, which checks a completed delivery. Populate verified supplier coverage and quote terms before publication; without them, keep this destination in research rather than publishing a placeholder.

### Shared implementation requirements

- Fetch and validate official recurring price data into a persistent cache/snapshot; preserve the last valid observation on failure and show its real date. Derive seasonal status from the actual source schedule.
- Keep EIA benchmark, NYSERDA credit survey and supplier COD quotes as separately labeled series. Never relabel a statewide weekly average as a live ZIP price.
- Add visible history and meaningful calculations before multiplying state URLs. Use real content-change dates in sitemap lastmod.
- Give each intent a clear canonical destination, anchored from the relevant navigation and related tools. Preserve existing established URLs.
- Make important results and source tables crawlable; progressive JS can add interactions without hiding the main answer.
- Track price/state/tool use and organic landing cohorts independently of property-research submissions. Review actual clicks per family after release; no guaranteed ranking or CTR lift is assumed.

## Current competition and usable primary sources

Live search sampling on November 18 is directional discovery, not a localized Google ranking audit or numeric difficulty score.

- EIA currently supplies state heating-oil prices and previous-week/year changes: https://www.eia.gov/petroleum/heatingoilpropane/
- EIA historical table: https://www.eia.gov/dnav/pet/PET_PRI_WFR_A_EPD2F_PRS_DPGAL_W.htm . The web reader returned an older cached table than the main update page; verify observation alignment when implementing ingestion.
- NYSERDA prices and survey definitions: https://www.nyserda.ny.gov/energy-prices/home-heating-oil/average-home-heating-oil-prices
- Granby standard tank model source: https://www.granbyindustries.com/en-us/products/standard-residential-oil-storage-tank-ul-80/
- Granby vertical chart: https://www.granbyindustries.com/wp-content/uploads/2017/04/GranbyInd_Capacity-Chart_VerticalTanks_USA_v1.pdf
- Roth tank specifications: https://www.roth-america.com/product/oil-storage-tanks/double-wall-heating-oil-tank/
- HomeHeat already combines supplier prices, regional navigation and tank tools: https://www.gethomeheat.com/prices . Its coverage counts vary within the page; do not treat the publisher's counts as independently audited.
- CashHeatingOil already provides ZIP-based supplier pricing and dealer ordering: https://www.cashheatingoil.com/ . This supports commercial purchase intent and the need to separate brand demand, not any claim about its revenue.
- OilTankCharts already provides multiple charts, calculators and printable PDFs: https://oiltankcharts.com/ . A basic calculator or printable chart alone is not a new competitive advantage.
- An actual supplier publishes quantity/payment-dependent prices and dated history: https://lutzpetroleum.com/heating-oil-prices/ . This shows the useful fields a genuine supplier comparison needs; it is not a general-market price estimate.

## What is ready and what remains uncertain

Ready: current production defects, public technical baseline, current indexing evidence for three URLs, dated demand bands, primary sources for the proposed state and manufacturer expansions, and a concrete coordinated scope.

Uncertain: fresh exact keyword volumes, attainable rankings, delivery-supplier coverage/data-refresh effort, conversion and revenue. Actual customer inquiries remain uninspected at the owner's direction. No more administrator access is part of this research.

# Source notes — rank-independent SEO growth

## Decision

Average position 2 is a stretch outcome, not a base-case operating assumption. At the central deduplicated search pool of 32,644 searches/month, the public CTR benchmark produces about 100.5 clicks/day at position 2, but only 39.8 at position 3, 16.4 at position 4, and 12.7 at position 5.

For 100 clicks/day, the required monthly search opportunity is about 32.5k at position 2, 82.0k at position 3, 198.7k at position 4, and 256.4k at position 5. Position 4 therefore requires 6.1 times the current central adjusted pool; position 5 requires 7.9 times.

## Evidence reviewed

- Existing Google Ads exports: `analysis/google_ads_utility_keywords_us_2026-08-01.csv` and `analysis/google_ads_expanded_keyword_validation_us_2026-08-01.csv`.
- Existing deduplication model: 50,222 raw monthly searches, 32,644 central adjusted searches after a 35% semantic-overlap allowance.
- Existing CTR benchmark in `analysis/traffic_ceiling_assessment_2026-08-01.md`: P2 9.24%, P3 3.66%, P4 1.51%, P5 1.17%, P10 0.53%.
- Live SERP review on August 2, 2026: tank replacement cost is dominated by large home-service publishers; tank charts include an exact-match niche competitor and official manufacturer documents.
- EIA reports about 4.79 million U.S. households used heating oil as their primary heating fuel in winter 2023–24 and about 82% were in the Northeast.
- EIA and participating state agencies publish weekly residential heating-oil price series during the October–March heating season.
- Existing U.S. comparison products already cover supplier discovery and price comparison, so the adjacent market is not uncontested.

## Interpretation

Adding more pages inside the already-counted tank size, chart, gauge, removal, and replacement variants does not multiply the search denominator. It mostly redistributes overlapping impressions. The only adjacent product found with a plausible route to a materially larger household workflow is a heating-oil price and delivery decision engine. That conclusion is based on household population, official recurring data, and observed commercial competitors—not on validated keyword volume.

The missing input is exact U.S. Google Ads demand for price, delivery, emergency delivery, and order-sizing queries. The accompanying `keyword_validation_batch.csv` is therefore a validation queue, not an authorization to publish pages.

## Decision gates

1. Do not build a price/delivery surface until the new Ads export is available.
2. Proceed only if the deduplicated adjacent cluster is at least 100,000 searches/month, or at least 50,000 with a credible top-three path and verified lead-buyer coverage.
3. Sample at least 20 U.S. SERPs. Require a meaningful weakness in at least 30% of them: stale data, non-U.S. results, thin directories, or pages without transparent official benchmarks.
4. Publish state pages only where official recurring data and genuine state-specific intent both exist. Do not create city pages from modifiers alone.
5. Treat oil-to-heat-pump and oil-to-gas content as lead-value expansion, not the main traffic denominator; current validated exports show only 10–100 monthly for the heat-pump term and 100–1,000 for several gas-conversion terms.

## Sources

- U.S. EIA, Use of heating oil: https://www.eia.gov/energyexplained/heating-oil/use-of-heating-oil.php
- U.S. EIA, Heating Oil and Propane Update: https://www.eia.gov/petroleum/heatingoilpropane/
- EIA residential weekly price series: https://www.eia.gov/dnav/pet/PET_PRI_WFR_A_EPD2F_PRS_DPGAL_W.htm
- Massachusetts home heating fuel prices: https://www.mass.gov/info-details/massachusetts-home-heating-fuels-prices
- Maine heating fuel prices: https://www.maine.gov/energy/heating-fuel-prices
- HomeHeat comparison surface: https://www.gethomeheat.com/
- CheapHeatingOil dealer directory: https://cheapheatingoil.com/find-a-dealer/
- Current exact-match tank-chart competitor: https://oiltankcharts.com/


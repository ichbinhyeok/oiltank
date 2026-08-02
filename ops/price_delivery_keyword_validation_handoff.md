# Price and delivery keyword validation handoff

## Status: completed 2026-08-02

The 21 price, delivery, calculator, emergency, and conversion candidates were validated in Google Ads Keyword Planner with United States targeting and the July 2025 through June 2026 historical period.

Completed outputs:

- `analysis/google_ads_price_delivery_keyword_validation_us_2026-08-02.csv`
- `analysis/price_delivery_volume_assessment_2026-08-02.md`
- Central combined adjusted denominator: **107,772 searches/month**
- At 3% CTR: **107.8 clicks/day**
- Decision: demand gate passes centrally, but the 50% overlap sensitivity is only 82.9 clicks/day and ranking/conversion remain unproven

The persistent ad-block warning was a false-positive overlay: planner actions and data loading worked after the extension was removed. The official signed CSV download itself was blocked by the controlled Chrome client at `storage.googleapis.com`, so the saved local CSV is an exact 21-row planner-grid capture rather than an untouched downloaded file.

## Source candidates

Canonical candidate file:

- `analysis/seo_rank_independent_growth_2026-08-02/keyword_validation_batch.csv`

Paste these exact keywords into **Get search volume and forecasts**:

```text
heating oil prices
home heating oil prices
heating oil price today
heating oil prices near me
heating oil prices by state
heating oil prices by zip code
average heating oil price
heating oil delivery
heating oil delivery near me
emergency heating oil delivery
same day heating oil delivery
cheap heating oil
cash heating oil
heating oil companies near me
heating oil suppliers near me
heating oil price calculator
heating oil cost calculator
how much heating oil do i need
ran out of heating oil
oil heat to heat pump conversion
oil heat vs heat pump cost
```

## Google Ads settings

- Location: United States
- Language: English
- Search network: Google
- Historical period: July 2025 through June 2026, matching the existing Ads exports
- Keep the account's reported ranges and bid fields; do not silently replace ranges with invented point estimates

Use the historical metrics view. Export the result rather than manually transcribing values when download is available.

## Required outputs

1. Save the untouched Google Ads export as:
   `analysis/google_ads_price_delivery_keyword_validation_us_2026-08-02.csv`
2. Save the deduplicated decision memo as:
   `analysis/price_delivery_volume_assessment_2026-08-02.md`
3. Update `ops/context_tracker.md` with the verified adjusted search pool and decision.
4. If the cluster passes, update the SEO denominator used in the current traffic assessment. Do not create city/state thin pages from modifiers.

## Calculation and decision gate

Existing verified, overlap-adjusted pool: **32,644 searches/month**.

- Estimate new cluster low/base/high from the Google ranges using the same method as `analysis/traffic_ceiling_assessment_2026-08-01.md`.
- Group close variants before adding them to the existing pool.
- Document the overlap assumption explicitly; use 35% as the central sensitivity only if the new export provides no better evidence.
- At the user's 3% blended CTR assumption:
  - clicks/month = adjusted searches/month x 0.03
  - clicks/day = clicks/month / 30
  - 100 clicks/day requires 100,000 adjusted searches/month

Product expansion passes only when either:

1. the combined adjusted pool reaches at least 100,000 searches/month; or
2. it reaches at least 50,000 with a credible top-three SERP opening and confirmed lead-buyer coverage.

Price and delivery traffic must not be counted as approved removal leads unless the buyer explicitly accepts that intent.

## Browser continuation

After the site-specific ad-block exception is active:

1. Reload the open Google Ads Keyword Planner tab.
2. Confirm the `Turn off ad blockers` dialog is gone.
3. Open **Get search volume and forecasts**.
4. Enter the exact 21-keyword batch above.
5. Apply the settings above and retrieve historical metrics.
6. Export and continue with the required outputs.

The task is not complete until the raw export, deduplicated denominator, 3% CTR forecast, and pass/fail decision are all saved.

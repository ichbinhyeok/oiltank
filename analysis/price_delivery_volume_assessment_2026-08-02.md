# Price and delivery search-volume assessment — 2026-08-02

## Verdict

**The demand gate passes at the central 35% overlap assumption, but only narrowly and only as a traffic denominator.** The combined overlap-adjusted pool is about **107,772 searches/month**, which produces **107.8 clicks/day at a 3% blended organic CTR**. A more conservative 50% overlap case produces 82.9 clicks/day, so 100/day is now plausible rather than proven.

The validated leverage is concentrated in three broad terms: `heating oil prices`, `heating oil delivery near me`, and `cash heating oil`, each reported at 10,000–100,000 monthly searches. This supports a small number of strong price/delivery products, not state, ZIP, city, cheap-oil, or cash-oil doorway pages.

## Google Ads observation

- Source: Google Ads Keyword Planner, historical metrics view
- Location: United States
- Network: Google
- Period: July 2025 through June 2026
- Account language control: All languages (disabled in this saved-plan view); all submitted queries were English
- Observed: August 2, 2026
- Raw UI capture: `analysis/google_ads_price_delivery_keyword_validation_us_2026-08-02.csv`

The planner generated a signed CSV export, but the controlled Chrome client blocked the `storage.googleapis.com` download with `ERR_BLOCKED_BY_CLIENT`. The local CSV therefore preserves all 21 rows and displayed fields transcribed from the planner grid; it is not represented as the untouched downloaded artifact.

## Measured bands

| Band | Keywords | Base midpoint contribution |
|---|---:|---:|
| 10,000–100,000 | 3 | 94,869 |
| 1,000–10,000 | 6 | 18,972 |
| 100–1,000 | 5 | 1,580 |
| 10–100 | 5 | 160 |
| No measurable band | 2 | 0 |
| **New raw base** | **21** | **115,581** |

The same range method as the existing traffic assessment is used: lower bound for low, geometric midpoint rounded to 32/316/3,162/31,623 for base, and upper bound for high.

| New cluster scenario | Searches/month before semantic overlap |
|---|---:|
| Low | 36,550 |
| Base | 115,581 |
| High | 365,500 |

## Deduplicated denominator and CTR 3% forecast

The previously reviewed utility and commercial universe has a raw base of 50,222 searches/month and an overlap-adjusted central pool of 32,644. There are no exact duplicate keywords between that reviewed universe and this 21-query batch. Semantic and close-variant overlap is not observable in the range-only account, so the combined raw base of 165,803 is shown at the same 20%/35%/50% sensitivity used in the existing model.

| Semantic overlap | Combined adjusted searches/month | Clicks/month at 3% | Clicks/day |
|---:|---:|---:|---:|
| 20% | 132,642 | 3,979 | 132.6 |
| **35% central** | **107,772** | **3,233** | **107.8** |
| 50% | 82,902 | 2,487 | 82.9 |

At 3% CTR, clicks/day equals adjusted monthly searches divided by 1,000. The 100-click/day threshold therefore requires 100,000 adjusted monthly searches.

## What the data supports building

1. Keep `/heating-oil-prices/` as the national official-benchmark hub and refresh its EIA evidence on schedule.
2. Treat `/heating-oil-delivery-check/` as the delivery workflow, but do not claim that it captures supplier-discovery demand until it provides a credible local supplier-routing answer.
3. Keep `/heating-oil-cost-calculator/`, `/how-much-heating-oil-do-i-need/`, and `/ran-out-of-heating-oil/` as supporting jobs. Their exact-match bands are only 10–100; they earn their place through workflow usefulness and internal linking, not standalone TAM.
4. Do not create thin state, ZIP, city, `cheap heating oil`, or `cash heating oil` pages. Add those vocabularies and decision guidance to the two strong hubs where the answer is genuinely supported.
5. Do not count price/delivery visitors as removal leads. Buyer acceptance for fuel-delivery or HVAC intent remains a separate commercial gate.

## Risks

- Five delivery/price-local queries show a 90% three-month decline, consistent with strong seasonality in the July snapshot.
- Broad bands make the central estimate sensitive to midpoint choice and close-variant overlap.
- Local packs, paid ads, supplier directories, and freshness features can reduce organic CTR below 3% even at strong conventional rankings.
- The denominator says enough searches exist. It does not demonstrate rankings, 3% realized CTR, or the conversion rate needed for 40 approved leads/month.

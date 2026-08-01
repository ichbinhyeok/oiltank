# Source notes

- Decision: which additional query clusters should enter the SEO roadmap after the chart library.
- Audience: Oil Tank Route product owner.
- Source window: Google Ads Keyword Planner, United States, July 2025 through June 2026.
- Current route comparison: `src/main/java/owner/buriedoiltank/data/ProductRoute.java`, reviewed August 2, 2026.
- Validation: keyword bands and bid values were read directly from the two reviewed CSV exports. Current routes were checked to distinguish new work from expansion of an existing URL.
- Chart map: "remaining demand" asks how many reviewed variants appear in each lifecycle cluster; it uses a sorted bar of keyword counts, not summed search volume, with one blue palette root. Source: `ads_cluster_counts.csv`; delivery: portable HTML report.
- Recommendation confidence: share with caveats. Demand bands are verified; organic difficulty and accepted-lead conversion are not.
- SERP review: `serp_review.md`, snapshot reviewed August 2, 2026. It establishes relative result composition only, not a numeric ranking probability.
- HTML delivery blocker: the packaged report verifier reports `horizontal_overflow` at 1440px for this artifact and reproduces the same failure on the previously delivered `high_leverage_pages_2026-08-01/artifact.json`; the canonical artifact remains available, but no HTML report is claimed as verified.
- Required executive roles mapped as follows: Executive Summary; replacement/removal/lifecycle findings; Recommended next sequence; Further Questions; Caveats and Assumptions.

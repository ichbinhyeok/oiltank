# Google Ads keyword opportunity — United States

Date: 2026-08-01  
Source window: 2025-07 through 2026-06  
Account state: billing configured, no ad campaign created or active

## Decision

The market has enough search demand to continue, but the current generic information architecture is not sufficient for 100 organic clicks per day. The strongest opening is a state-specific removal lead page for Rhode Island, followed by national removal-cost and underground-removal tools.

Free Keyword Planner data is range-based and close variants overlap. The ranges must not be summed as if every keyword represents unique searches.

## Highest-priority clusters

| Priority | Cluster | Monthly range | Ads competition | Live SERP assessment | Decision |
|---:|---|---:|---|---|---|
| 1 | oil tank removal rhode island | 100–1,000 | Medium | Thin local results, small service sites, BBB category, and programmatic lead pages; one result shows placeholder-style contact data | Build first |
| 2 | oil tank removal | 1,000–10,000 | High | Valuable but broad and nationally competitive | Build a hub, not a thin lead page |
| 3 | oil tank removal cost | 100–1,000 | Low | Angi and HomeAdvisor dominate, but a niche contractor also ranks | Replace the generic guide with a source-backed calculator |
| 4 | underground oil tank removal / cost | 100–1,000 each | Low | More specific than the head term and commercially aligned | Build two tightly linked pages |
| 5 | oil tank removal new jersey | 100–1,000 | High | Strong local contractor intent | Improve the existing NJ route; do not create duplicate pages |
| 6 | oil tank sweep / oil tank sweep NJ | 100–1,000 each | Medium | Exact-match local operators show prices, reviews, equipment, and service proof | Lead opportunity is high, but top-10 difficulty is high without real local proof |
| 7 | buried oil tank | 100–1,000 | Low | Informational and ambiguous | Use as a supporting hub, not the main revenue page |
| 8 | oil tank inspection | 1,000–10,000 | Low | Intent is mixed with industrial, aboveground, marine, and EPA inspection content | Do not treat the full range as residential lead demand |

## Traffic ceiling

- Direct validation of 95 Google-autocomplete candidates produced 11 keywords in the 100–1,000 range, 60 in the 10–100 range, 1 in the 0–10 range, and 23 with no measurable range.
- Those candidates span a raw 1,700–17,010 monthly searches before deduplication.
- The separate head term `oil tank removal` is 1,000–10,000 monthly, but overlaps with many removal variants.
- Therefore 3,000 organic clicks per month is possible only if the site ranks across several clusters. It is not supported by one landing page or by the current long-tail guides alone.

## Pages to build or upgrade

1. `/states/rhode-island/oil-tank-removal/`
   - Official RIDEM requirements and contractor qualifications
   - Aboveground versus underground decision path
   - Permit, disposal, soil-testing, and leak escalation steps
   - City/service-area module and verified-call CTA
   - Real pricing inputs or clearly sourced ranges

2. Upgrade `/guides/oil-tank-removal-cost/`
   - Interactive inputs: state, above/underground, size, access, oil remaining, soil testing, contamination
   - Separate base removal from remediation exposure
   - Cite state agencies and explain what the estimate excludes

3. Create an underground-removal hub and cost page
   - `underground oil tank removal`
   - `underground oil tank removal cost`
   - Avoid duplicating the general cost page; each page must answer a distinct intent

4. Upgrade the existing New Jersey sweep route
   - Target `oil tank sweep nj` without making a competing duplicate URL
   - Add real service proof: equipment, report turnaround, coverage, price, insurance, certification, and verified contractor routing

5. Create a residential inspection explainer
   - Explicitly distinguish a real-estate tank sweep from industrial tank inspection
   - Target `underground oil tank inspection` and support the NJ sweep page

## What not to build

- New standalone pages for `buried oil tank records`, `selling house with oil tank`, or similar phrases with no measurable volume.
- Thin city pages copied from a state template.
- A national `oil tank inspection` lead page that ignores the mixed industrial intent.
- Duplicate NJ pages competing with the existing state route.

## Go/no-go test

Build the Rhode Island removal page and the upgraded cost tool first. Continue expanding only if Search Console shows non-brand impressions entering the top 20 for the target clusters. Contractor outreach should begin before traffic arrives so the CTA represents a real verified service rather than a placeholder lead form.

## Data files

- `google_ads_keyword_planner_us_2026-08-01.csv`: initial 29-keyword batch
- `google_ads_autocomplete_validation_us_2026-08-01.csv`: 95 valid Google-autocomplete candidates


# SepticPath → Oil Tank Route: SEO-only comparison

Date: September 30, 2026. Scope: search acquisition and public service workflows; no outreach, customer research, deployment, price changes, or paid-acquisition proposals.

## Decision

Preserve the records-service pivot, but deepen its search acquisition product. Septic demonstrates that an independent service can attract organic traffic around official record searches. It does not prove equal demand for residential oil-tank records or guarantee a replication of rankings.

The evidence supports a combination of broad geography, institution/document-specific search entrances, useful self-service workflows, and iteration over months. It does not support attributing success solely to a headline, design, number of pages, or the latest concierge offer.

## Measured evidence

GSC final web, all countries/devices, September 1–27, 2026:

| Scope | Clicks | Impressions | Notes |
| --- | ---: | ---: | --- |
| SepticPath property/date total | 680 | 23,823 | 27 observed days |
| Oil Tank Route property/date total | 13 | 2,458 | Includes days before September 16 relaunch; not an age-matched pivot comparison |
| Septic TDEC page | 157 | 6,247 | Named institutional search entrance |
| Septic Indiana records page | 44 | 847 | State record lookup |
| Septic NC records page | 30 | 1,237 | State record lookup |
| Septic DHEC lookup page | 23 | 549 | Named institutional lookup |
| Septic county record page family | 210 | 6,002 | 238 returned pages; 88 with at least one click |

Page-family decomposition uses disjoint pathname rules: county records, state records, remaining record/permit-lookup/permit-search paths, and other. Their page-row totals are 682 clicks and 25,516 impressions, not the property/date totals. These aggregation grains must not be mixed. The record-search families account for 632 of 682 page-aggregated clicks; this is not a count of customers or a conversion rate.

Visible query-page examples: `tn septic permit search` 22 clicks; `tdec septic permit search` 11; `hamilton county septic inspection records` 4. Query pagination returned 1,045 rows, but anonymized queries remain unavailable. Oil Tank's visible new-service queries include general FOIL/portal navigation and two low-impression tank-specific terms; this does not establish that all its unreported queries are irrelevant.

Current county sitemap: 327 Septic URLs, versus 16 new Oil Tank NJ/NY area routes documented in the September 16 expansion. These are published-scope counts, not equivalent markets, indexed-page counts, or an instruction to manufacture 327 oil-tank pages.

## History matters

- April 4 commit `556ddaa`: transfer workflow and county records.
- June 28–30 commits: records lookup alignment, state/county expansion, contextual request-builder tools (`9513d8f`).
- July 10: address-first finder (`90c685c`), TDEC relay, NC direct routes.
- Historical GSC page reruns: May 23–June 19, 104 clicks; June 20–July 17, 311 clicks. These reproduce the total clicks in the July 20 historical export. Record-family definitions in that export differ from this audit's regex and are not treated as identical.
- September design and assisted-service changes came after those earlier search signals. Commit dates are implementation dates, not verified production/crawl dates.

## What actually differs

1. **Search entrances.** Septic has institution, document-task, state, and county entrances. Oil Tank mostly packages local research guidance and general state sources, alongside older heating-oil utilities. Named NJDEP NFA/DocMiner and NYSDEC task entrances are not equivalently developed.
2. **Workflow depth.** Oil Tank already has meaningful official links, identifier guidance, a copyable request outline, limitations and intake. Do not describe it as an empty landing-page site. Septic additionally supports county/office routing, contextual request preparation and found/empty/blocked/wrong-office return states on reviewed workflows.
3. **Breadth and maturity.** Septic's broad county layer yields scattered organic clicks and sits under stronger institution/state entrances. Oil Tank's 16 routes have not replicated that breadth and have a much younger service cohort. Neither fact alone proves the cause of ranking differences.
4. **Do not blame the service CTA.** The current TDEC and county pages also lead with assisted investigation. A simplistic 'self-service hero wins, service hero loses' conclusion is contradicted by the current public pages and timing.
5. **Do not dismiss government navigation.** Septic demonstrably earns clicks on agency-name searches. Oil Tank should distinguish tank/document-specific agency intent from unrelated general FOIL searches, not reject all official-portal intent.

## Concrete implementation package proposed

This is a scoped recommendation, not implementation completed in this audit.

### A. Institutional/document task entrances

- NJDEP oil-tank NFA retrieval: DataMiner versus older-file request, identifier capture, missing-record branch. Do not imply every removed tank requires or has an NFA.
- NJDEP document retrieval: upgrade existing state records workflow; separate a dedicated page only when it answers a genuinely different task from the NFA route.
- NYSDEC spill/case lookup: address/identifier search instructions, document follow-up, match limitations. Never infer a complete home-tank inventory or safety result from a no-match.
- Nassau verification letter: upgrade the existing Nassau URL rather than create a competing duplicate. Foreground historical verification lookup, not scheduling new work.

Official task feasibility is verified; this audit has not measured external keyword volumes. Low or absent Oil Tank GSC exposure is not proof of zero demand. These are evidence-grounded acquisition candidates, not guaranteed winners.

### B. Shared action flow across all 16 existing areas

- Start from jurisdiction and document sought; show the applicable public source and exact identifiers needed without requiring an email or research intake.
- Prepare a local request/checklist from entered details, keeping private property identifiers out of analytics and public URLs.
- Support found file / no online result / wrong office / blocked portal / request pending as distinct return states.
- Preserve the current human-research handoff as the next option; do not make a new manual operator task a condition for every visitor to obtain value.
- Clearly label this as routing/request assistance, not an automatic comprehensive property search. An interactive widget alone is not an SEO intervention with proven lift.

### C. Broad coverage, without arbitrary page multiplication

- Retain the existing 16 routes and older useful tools.
- Inventory additional NJ/NY municipal/county record custodians and accessible document workflows in one substantive expansion pass.
- Publish separate local routes where custodian, procedure, record type or fallback is materially different. Group places sharing the same task instead of spinning place-name text.
- Build institution → state/local route → document problem links in both directions. Avoid splitting existing Nassau and related intent across duplicate URLs.
- Recheck Brookhaven's September 28 portal transition before making further public claims; current code still describes it as upcoming.
- Choose the resulting count from verified useful coverage; 16 is not a proven ceiling and 327 is not a target.

### D. Evaluate SEO only

Measure institution/document and local-route cohorts separately: relevant query impressions, clicks, official-route use, returned-result use, and optional research intake. Preserve query-anonymity gaps. Annotate release and first recrawl separately; compare complete 14/28-day windows without promising success within those periods. Do not repeatedly resubmit unchanged pages or treat average-position changes as causal proof.

## Verification and boundaries

- Live HTTP 200 checked for Oil Tank home, Nassau, Brookhaven and NJ records; live HTML and local templates inspected. Web reader could not load Oil Tank URLs, so direct HTTP supplied that evidence. This is not end-to-end browser interaction testing.
- Septic live sitemap counted; TDEC/Indiana public content read. Web cache dates differ, so current text is not treated as historical design evidence.
- Quantitative transformations independently recomputed using standard Python. The companion notebook code was executed as a sequential Python audit; a Jupyter kernel/visual notebook preview was not available (nbformat/nbclient missing). No claim of a kernel or visual notebook check.
- In accordance with Septic AGENTS.md, 44 immutable growth snapshots were appended and validated at ledger revision 354. No customer states, case conclusions, mail checkpoints or external messages changed.
- Application code, deployment and Search Console settings were not modified.

Evidence companion: `septic-oiltank-search-evidence.json` and `septic-oiltank-comparison.ipynb` in the current Codex visualization output directory. The raw evidence is also retained in ignored Septic operational storage. Do not commit private operational files.

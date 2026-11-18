# Heating-oil price refresh runbook

## November 18 runtime refresh (current implementation)

This supersedes the manual hard-coded-catalog procedure below once the November expansion is deployed. It is currently verified locally; do not assume production is upgraded.

- The prod profile enables `BURIED_OIL_TANK_PRICE_REFRESH_ENABLED` by default. A startup check after one minute and subsequent six-hour checks fetch the fixed public EIA residential table, without API credentials.
- The bundled baseline includes 24 area series (21 states plus national/East Coast/New England) and six weekly observations, Oct 5–Nov 9, released Nov 12. It is actual source data, not estimated prices.
- Validated observations persist atomically to `storage/market/heating-oil-prices.json`. The storage root must remain outside the release. Invalid HTTP responses, missing areas, malformed dates, invalid values and older snapshots retain the last valid data.
- Source publication and next-release dates come from EIA. Public pages keep the observation date visible and flag a release more than two days overdue. Do not advance dates merely to make the page appear current.
- To refresh the reviewed bundled baseline during maintenance, run `python ops/fetch_eia_price_snapshot.py` with requests/BeautifulSoup installed and inspect the resulting JSON and public EIA fixture before committing. The source-check date uses the current Korea date; fixture filenames use the actual EIA release date. Update dated test expectations when changing the reviewed seed.
- Verify the price page, one state page, CSV download and cost-calculator benchmark after release. Check runtime cache persistence and source dates separately from the public page's HTTP status.

The older process and completion record below remain historical evidence.

## Next required review

- **Trigger date:** 2026-10-07, after the EIA resumes weekly residential heating-oil price publication.
- **Owner action:** Check the two official EIA pages below before changing any published value.
- **Recurring cadence:** Weekly during the October-March heating season. Check once at the beginning of April to record the final seasonal observation and next announced restart date.
- **Do not create a live-price claim:** The site must continue to display the observation date, release date, unit, tax boundary, and collection status beside every benchmark.

## Official sources

1. EIA Heating Oil and Propane Update: https://www.eia.gov/petroleum/heatingoilpropane/
2. EIA Weekly Heating Oil and Propane Prices table: https://www.eia.gov/dnav/pet/PET_PRI_WFR_A_EPD2F_PRS_DPGAL_W.htm

Use the table's residential heating-oil series in U.S. dollars per gallon excluding taxes. Do not substitute supplier ads, search snippets, crowdsourced prices, wholesale prices, or another unit.

## Update procedure

1. Confirm the newest observation date, EIA release date, next scheduled release, unit, and whether collection is active or seasonally paused.
2. Record only areas displayed by the official table and only values visible in the reviewed release.
3. Update `HeatingOilPriceCatalog.LATEST` in `src/main/java/owner/buriedoiltank/heating/HeatingOilPriceCatalog.java`.
4. Update assertions in `src/test/java/owner/buriedoiltank/heating/HeatingOilPriceCatalogTests.java` and the dated route assertions in `BuriedOilTankVerdictApplicationTests.java`.
5. Search for the previous observation date and price before finishing:

   ```powershell
   rg "2026-03-30|5\.535" src README.md ops
   ```

6. Run `./mvnw.cmd test` on Windows or `./mvnw test` on Unix.
7. Browser-check `/heating-oil-prices/` and `/heating-oil-cost-calculator/` at 360px and desktop width. Confirm the date is visible without interaction, the calculator labels the benchmark as dated, and the page has no horizontal overflow.
8. After deployment, verify both URLs and `sitemap.xml` on the canonical host.

## Safe failure rule

If EIA has not published the expected release, its table is unavailable, or the unit/series cannot be verified, keep the last verified snapshot. Update only the collection-status text if the official schedule supports that change. Never insert an estimated value to make the page appear current.

## Completion record

Append one line for every review, including no-change reviews:

| Reviewed on | Latest observation | Result | Reviewer note |
|---|---|---|---|
| 2026-08-02 | 2026-03-30 | Initial snapshot published | EIA collection paused April-September; next scheduled release 2026-10-07. |

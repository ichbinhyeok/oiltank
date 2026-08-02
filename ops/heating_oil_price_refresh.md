# Heating-oil price refresh runbook

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

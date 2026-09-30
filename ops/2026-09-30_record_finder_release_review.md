# Record Finder release review — 2026-09-30

Scope: `codex/record-finder-pivot` into `main`, based on `fd3f06a`. This is pre-release evidence, not a production or search-submission receipt.

## Review and repairs

- Reviewed the public catalog/controller/template integration, source overrides, protected diagnostics, local worksheet persistence, output escaping, analytics allowlist, assistance boundaries and deployment contracts.
- Independent read-only adversarial review found no concrete release blocker. This was a same-model independent review, not a claimed cross-model review or live security scan.
- Replaced the obsolete production homepage-copy check with finder-link, rendered-finder and sitemap contracts. Added a regression test that checks both workflow assertions and rendered responses.
- Fixed malformed incident overrides containing null rows: diagnostics and public routes now agree on using bundled fallback. Regression tests cover null JSON, a null row and malformed JSON.
- Updated directory metadata to reflect Portland alongside NJ/NY. No migration, pricing, account or agency-sending changes.

## Coverage map

Final local `mvnw --batch-mode verify` passed: **67 tests, 0 failures, 0 errors, 0 skipped**, plus JAR packaging, at 2026-09-30 20:52 KST (Java 23 targeting 21). Worksheet/shared JS syntax and staged diff whitespace checks passed. Local evidence log: ignored `target/release-final-verify.log`.

| Changed path | Evidence |
| --- | --- |
| Catalog, internal destinations, canonical/slash handling, sitemap | ResearchRoutesTests and ResearchCatalogTests; earlier actual-preview 84-URL crawl |
| Unsupported route kind and assistance boundaries | 404 tests; every detail's NJ/NY intake presence or absence checked |
| Source override propagation and invalid fallback | RecordsDataRepositoryTests, including new null-row regression |
| Protected source-health export | unauthenticated 401 and authenticated non-PII response tests |
| Finder, source outcomes, draft, browser storage, copy/print | documented synthetic browser checks in record_finder_verification and depth_review |
| No JS, mobile bounds, empty/filter states, old saved sequence | documented browser checks; not claimed as a committed browser automation suite |
| Production entry checks | new productionSmokeChecksMatchTheRecordFinderContract test |

No instrumented line-coverage percentage is claimed. Live GA4 ingestion, actual Google indexing, all external portal deliveries and production security/backup checks remain unverified until their respective steps run.

## Plan audit

Implemented: task/local route finder, shared private worksheet, document distinctions, contextual drafts, separate reported outcomes, evidence checklists, source fallback, protected freshness diagnostics, preserved legacy routes/tools, NJ/NY assistance boundary and public privacy guidance. Existing workflow retains security scan and private backup.

Changed with the accepted cohort decision: 40 record-catalog URLs (37 details plus 3 hubs/examples), not the planning capacity of 50–60. Full sitemap has 84 URLs, including retained utilities/guides. Nine additional locality candidates remain HOLD for insufficient differentiated evidence.

Release-stage work remains: Java 21 PR verification, explicit pre-merge confirmation, production deployment/security/backup/canary, then sitemap resubmission and representative manual indexing requests. Submission will not be reported as indexing or ranking success.

## Version and documentation

The repository has no VERSION or CHANGELOG file; deploy identity is the immutable Git SHA and the existing Maven artifact version remains `0.0.1-SNAPSHOT`. Do not introduce an unrelated four-part release scheme. README, DESIGN, the approved proposal and cohort/verification reports describe the current scope while retaining dated historical evidence.

Customer storage, screenshots, generated test data and unrelated September 16 deployment notes are excluded from the release commit. No customer or agency messages are sent.

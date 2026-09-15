# Release preparation — 2026-09-16

Status: locally verified; not deployed, pushed, or submitted to search engines.

- Production Docker exclusions corrected to include the homepage JPG and touch icon.
- Deployment canary text updated to the approved service design; image and Brookhaven route checks added.
- Removed image pruning so rollback images are retained.
- Added a Java 21 pull-request verification workflow.
- Added a public IndexNow ownership file. Submit only after its production URL and the canonical sitemap are verified.
- Independent bounded review found a restart-recovery bug in both mail queues.
  Restart now appends an interrupted/unknown-delivery status, blocks automatic resend,
  and exposes explicit protected retry with a sent-mailbox warning.
- Two new regression tests cover restart recovery, zero automatic sends, operator retry,
  and duplicate suppression. Reviewer recheck found no remaining P1/P2 in that correction.
- Fresh local Maven clean verify: 62 tests, 0 failures, 0 errors, 0 skipped; packaged successfully.
  Local runtime was Java 23 targeting Java 21; CI must still verify the Java 21 runtime.
- No tracked private storage or output files; known credential-signature scan found no matches.

## Remaining release work

The ship skill requires a pause after committing pre-landing fixes and a fresh ship invocation.
Create the PR, pass CI, complete the pre-merge readiness gate, and deploy the exact reviewed SHA.
Before replacement, preserve the production private storage, environment and rollback image/config.
Verify production routes, image, canonical redirects, protected admin, sitemap and IndexNow key.
Only then resubmit the Google sitemap, request representative URL indexing in Search Console,
and submit the verified canonical URLs through IndexNow. None of those submissions has happened yet.

The existing Google sitemap was healthy (no errors/warnings); preserve and resubmit its canonical
URL rather than deleting it. Request acceptance does not establish actual indexing or rankings.

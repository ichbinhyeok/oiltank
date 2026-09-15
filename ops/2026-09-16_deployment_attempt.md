# Deployment attempt — 2026-09-16 (Asia/Seoul)

Status: BLOCKED by production-image vulnerability gate. No production replacement occurred.

- PR #4 merged with explicit owner confirmation.
- Merge SHA: 64b378bf7c8b2aadff1507b3bb4a190a427962f4.
- Merged: 2026-09-15T20:14:46Z.
- Workflow: https://github.com/ichbinhyeok/oiltank/actions/runs/35018446562
- Tests, secret presence validation and ARM64 image build/push succeeded.
- Trivy production-image scan failed; gate was not bypassed.
- Private backup, compose transfer and OCI deployment were all skipped.
- Public homepage still returned HTTP 200 after the failed run.
- Docker Hub contains the built image (SHA tag and latest), but the running production service was not updated.

## Scanner findings

| Component | Installed | Fixed version reported | Findings |
|---|---|---|---|
| tomcat-embed-core | 10.1.55 | 10.1.58 | CRITICAL: CVE-2026-65182, CVE-2026-65905, CVE-2026-68525 |
| libcrypto3 / libssl3 | 3.5.7-r0 | 3.5.8-r0 | HIGH: CVE-2026-14456 (both packages) |

These are scanner-reported findings, not evidence of exploitation. Verify vendor fixes,
update the dependencies/runtime packages, run the full test suite and image scan again,
then deploy after the required release gate. No dependency changes or revert were made in this attempt.

## Search follow-through remains pending

Do not submit the unreleased sitemap/URLs. Google sitemap resubmission, representative manual
indexing requests and Bing IndexNow batch submission have not happened.
Authenticated Search Console tab is retained; current known report baseline is 33 indexed
and 20 excluded, last updated 2026-09-04. Existing redirects and intentional noindex pages
must remain excluded. The 67-URL production verification script is output/verify-production.ps1.
## Authorized remediation

The owner authorized patching and retrying after this failed run.
Tomcat is overridden to 10.1.59: Apache states that 10.1.58's release vote failed,
so 10.1.59 is the published release containing the fixes.
The runtime Docker stage requires libssl3 and libcrypto3 at least 3.5.8-r0.
The HIGH/CRITICAL Trivy gate remains unchanged. Patched image validation and
production deployment are still pending.

Source: https://tomcat.apache.org/security-10.html

### Remediation verification

- `mvnw.cmd --batch-mode verify`: PASS, 62 tests, zero failures/errors/skips.
- Dependency tree: Tomcat core, EL and WebSocket all resolve to 10.1.59.
- `git diff --check`: PASS.
- Local Docker engine is unavailable; runtime APK resolution and image scanning
  must be verified by the unchanged deployment security gate before OCI replacement.

# Document-check and broader record utility release — 2026-10-11

## Authority and release scope

Owner requested deployment of the reviewed localhost:18083 version. Release branch `codex/document-check-release`, target `main`, production `https://oiltankroute.com`. The prior release is `70411cf`; no production change is claimed until the deployment workflow and public checks pass.

This release includes all three October implementation passes: deeper retrieval guidance and four local routes; broader agency/local custody routes, filters and direct Seattle dataset lookup; document-gap checker, five distinct question entrances and revised home/navigation. Final public inventory: 63 details (36 local / 17 document / 10 problem), 67 catalog URLs and 111 sitemap URLs. Existing utilities and URLs remain. NJ/NY assisted intake boundary and private case operations remain unchanged.

Exclude unrelated September deployment notes and all ignored `storage`, `output`, `target` and browser evidence. No real customer/agency message or customer intake will be used as a release test. Search Console/IndexNow writes are not part of this deployment-only request.

## Workflow and safety

Use existing GitHub PR Java 21 verify, then merge only after the land-and-deploy readiness confirmation. Main push triggers `.github/workflows/deploy.yml`: tests, ARM64 image, unchanged HIGH/CRITICAL Trivy gate, private production storage/config backup, OCI deployment and health/public checks. Do not bypass security, force push or delete operational data. Existing compose bind mounts and backup step were reviewed, not changed.

`origin/main` fetched and merged without conflicts before final local verification. The September feature branch and all unrelated dirty files are preserved. New branch was created from the current checkout so the approved implementation was not lost.

Skill adaptation: global installed gstack checklists are used because this repository has no vendored `.agents/skills/gstack` copy. The repo-mode helper errors on a Windows path; treat ownership as unknown and leave unrelated files alone. Existing Java/Maven test and commit-SHA Docker release conventions are authoritative; no new four-part VERSION scheme, TODO system, package release or unrelated documentation migration is introduced. Existing project documentation and this release record provide the release notes.

## Review and test evidence

- Primary structured review checks data safety, enum/route consumers, source overrides, privacy, metadata, persistence and deployment compatibility. Three independent read-only parallel audits cover code/security, UI/privacy, and catalog/source/override consistency, as required by the large-diff ship checklist. Cross-model tooling is not available; these are same-model independent audits, not a claimed four-model review.
- Existing local verification before this release: 76 tests, 110 checker combinations, 45 responsive layouts, no-JavaScript fallback, all 111 URLs and final three-width result positioning. Seattle synthetic/mock query states and a real synthetic provider request were verified in the preceding broader-utility pass. See the three dated implementation records for exact scope and limitations.
- Fresh post-base-merge `.\mvnw.cmd verify -q`: exit 0; 76 tests, zero failures/errors; packaging succeeded. Three parallel audits and the primary code/design review found no actionable defect. No application code changed after this verification. PR Java 21 CI: pending.
- Full line/branch coverage is not instrumented; test totals are not a coverage percentage. Java integration tests cover catalog/rendering/redirect/schema/assistance/privacy contracts; browser scripts cover interactive and failure paths. LLM evals do not apply to this non-LLM feature.
- Production image scan, backup, deployment and public verification: pending. New route demand and SEO recovery remain unproven.

## Next gate

Create and attach the release PR, wait for its Java 21 CI, collect the exact revision/readiness report, then obtain the required final merge confirmation. No claim of deployment or successful security scan before the workflow actually completes.

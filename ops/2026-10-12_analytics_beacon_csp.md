# Narrow Google tag image policy repair — 2026-10-12

Owner explicitly selected “해당 요청만 좁게 수정하고 테스트·재배포 (권장)” after PR #9's successful deployment. Current production is `1f335e3647aada21b98d32aa371651f70cff38b6`; the original canary recorded one blocked image request to `https://www.googletagmanager.com/td`. Core GA4 collect returned 204 in two fresh contexts, so this is not a claim of total analytics or service failure.

## Scope and authority

- Base `main`; branch `codex/analytics-beacon-csp`.
- Add only `https://www.googletagmanager.com/td` to `img-src`. No Google-wide wildcard, full-host image permission, unsafe-inline/eval, script/connect policy change, dependency/workflow change, new analytics tag, or customer-data change.
- Exact path matching preserves the observed endpoint's query parameters. It is not a query allowlist. CSP can ignore path restrictions after a redirect; do not claim this closes every redirected-image route or authenticates Google's server behavior.
- Google lists Tag Manager image access in its [official CSP guidance](https://developers.google.com/tag-platform/security/guides/csp?hl=en). Choosing the observed exact path instead of Google's broader host recommendation is our least-privilege scope decision, grounded in the initial production evidence.
- [W3C CSP path matching](https://www.w3.org/TR/CSP/#match-paths) and [CSP2 matching notes](https://www.w3.org/TR/CSP2/#source-list-paths-and-redirects) describe exact versus trailing-slash matching, query treatment and redirect limits.

## Regression evidence

New integration test asserts every CSP directive on home and document checker, allowing the one image path while keeping all other directive values unchanged. Before the implementation, the selected test failed specifically because `/td` was missing (one test, one failure, zero errors).

Deterministic browser probe against the unchanged packaged PR #9 preview at localhost:18084 confirmed RED: `/td?otr-csp-probe=1` could not load; no external probe request reached the browser routing handler. Probes for `/td/extra`, an unrelated path, a different subdomain and a lookalike host were also blocked. The harness intercepts any admitted probe and fulfills a local one-pixel image; it sends zero probe traffic to Google or the other hosts. Negative probe contexts intentionally generate CSP console violations and are separate from ordinary-page canary checks.

Patched verification: full local `mvnw verify` exited zero, 79 tests across 21 suites, zero failures/errors/skips; packaged prod-profile app on localhost:18085 uses isolated ignored storage and disables notifications/analytics. The deterministic browser probe is GREEN: the exact `/td` endpoint with a query loads, while the four different-path/host probes remain blocked; only one locally fulfilled probe reached the route handler. Full public QA passed 111 sitemap URLs, widths 375/768/1440, checker/reset/focus/NJ caveat, 63 finder routes, six slash redirects, Seattle control, no-JS guidance and unauthenticated admin export 401. Zero ordinary-page errors/console errors. Single local load measurements: checker 52/40/41 ms, home 126 ms (not production Web Vitals). Packaged-JAR Trivy v0.70.0 preflight: one Java target, 76 packages, zero selected HIGH/CRITICAL findings under the unchanged severity/ignore-unfixed/exit-code policy; this is not the production OS/image scan.

Primary source/diff review confirms only the image directive changes in runtime code; the regression test fixes every other directive value. No independent-agent review is claimed. Java 21 CI is pending. This note does not establish publication. Production remains at PR #9 until a later exact-SHA deployment passes the unchanged image gate, private backup and public canary. Pre-merge readiness confirmation is still required by the deployment skill. Existing Maven/commit-SHA version convention retained; no feature addition or separate VERSION/CHANGELOG change.

Unrelated dirty September files and existing status/release documents remain preserved and excluded from this repair. No customer case desk, private evidence, real intake, outbound email, Search Console or IndexNow operation is in scope.

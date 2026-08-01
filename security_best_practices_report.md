# Oil Tank Route security and release audit

Date: 2026-08-02  
Scope: Spring Boot/JTE application, lead APIs, admin surface, public JavaScript, container, and deployment workflow.

## Release decision

The application code passes the local security and regression gate after the fixes below. Production deployment remains gated by the GitHub Actions image scan and public canary; sitemap submission must happen only after those checks pass on the deployed domain.

## Fixed findings

### Critical

- Removed a real-looking administrator password embedded in `AdminSecurityProperties`. Production still requires the environment-provided password and the deployment workflow now rejects missing secrets before building or deploying.
- Updated the deployment canary from the retired brand string to `Oil Tank Route`; the old check would have rejected a healthy pivot deployment.

### High

- Bounded in-memory rate-limit buckets and stopped reading the raw `X-Forwarded-For` header in application code. The production container port is now bound to localhost so clients cannot bypass the trusted reverse-proxy boundary directly.
- Added maximum lengths to every lead and analytics field, a 32 KB form-post limit, a 16 KB request-header limit, and an analytics event allowlist.
- Added a strict Content Security Policy and removed inline executable JavaScript. Google Analytics is loaded from the same dependency-free script after validating the measurement ID.
- Hardened lead return-path validation against external, backslash, query, fragment, and traversal-shaped redirects.
- Escaped JSON embedded in `application/json` and `application/ld+json` script blocks.
- Pinned every GitHub Action to a full commit SHA. Added a SHA-pinned Trivy image gate that blocks deployment on fixable HIGH or CRITICAL OS or library findings.

### Moderate

- Upgraded Spring Boot 3.5.6 to the final 3.5.x OSS patch, 3.5.16.
- Overrode Jackson BOM 2.21.4 with 2.21.5 after OSV reported three moderate deserialization issues fixed in 2.21.5.
- Limited actuator exposure to health without details and added production static-asset cache control.

## Validation evidence

- `mvnw package`: 25 tests, 0 failures, packaged JAR successful.
- CycloneDX SBOM: 55 Maven components.
- OSV batch scan after the Jackson update: 0 known vulnerabilities across those 55 components.
- Production Dockerfile: image build successful.
- Browser QA: 360, 768, and 1440 CSS pixels; no horizontal overflow; no console warnings or errors; gauge calculator returns a chart-interpolated result and stores only session context.
- Keyboard QA: the first Tab reaches the skip link and Enter moves focus to `main#main-content`.
- Local browser lab check: CLS 0, LCP 116 ms, 424,676 transferred bytes. These local numbers are smoke-test evidence, not field Core Web Vitals.

## Residual risks and operating requirements

- Rate limiting is per application instance and in memory. It is sufficient for the current single-instance v1 but must move to a shared store before horizontal scaling.
- Admin authentication is HTTP Basic over TLS with no MFA. Keep `/admin` behind HTTPS and consider an identity-aware proxy if operator count grows.
- The result renderer uses `innerHTML` with application-generated constants, numbers, and reviewed tank metadata. No user-controlled or persisted text currently reaches this sink; replace it with DOM construction before allowing user-authored result content.
- The public reverse proxy must replace, not append untrusted, forwarded headers. The application container is intentionally localhost-only.
- Trivy could not complete reliably in the local Docker Desktop session. The same scan is therefore a mandatory CI deployment gate, not an optional report.

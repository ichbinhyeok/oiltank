@import gg.jte.Content
@import owner.buriedoiltank.pages.PageModels.PageMeta
@param PageMeta meta
@param String activeNav
@param Content content
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${meta.title()}</title>
    <meta name="description" content="${meta.description()}">
    <link rel="canonical" href="${meta.canonicalUrl()}">
    <meta property="og:title" content="${meta.title()}">
    <meta property="og:description" content="${meta.description()}">
    <meta property="og:url" content="${meta.canonicalUrl()}">
    <meta property="og:type" content="${"home".equals(activeNav) ? "website" : "article"}">
    <meta property="og:image" content="${meta.socialImageUrl()}">
    <meta property="og:image:alt" content="${meta.socialImageAlt()}">
    <meta name="twitter:card" content="summary_large_image">
    <meta name="twitter:title" content="${meta.title()}">
    <meta name="twitter:description" content="${meta.description()}">
    <meta name="twitter:image" content="${meta.socialImageUrl()}">
    @if (!"admin".equals(activeNav) && meta.analyticsMeasurementId() != null && !meta.analyticsMeasurementId().isBlank())
        <meta name="otr-analytics-id" content="${meta.analyticsMeasurementId()}">
    @endif
    @if (!meta.indexable())
        <meta name="robots" content="noindex,follow">
    @endif
    @for (var schema : meta.structuredDataJson())
        <script type="application/ld+json">$unsafe{schema}</script>
    @endfor
    <link rel="preload" href="/fonts/source-sans-3.woff2" as="font" type="font/woff2" crossorigin>
    <link rel="preload" href="/fonts/general-sans-600.woff2" as="font" type="font/woff2" crossorigin>
    <link rel="icon" href="/favicon.svg" type="image/svg+xml">
    <link rel="icon" href="/favicon.ico" sizes="32x32">
    <link rel="apple-touch-icon" href="/apple-touch-icon.png">
    <link rel="stylesheet" href="/app.css">
</head>
<body class="${"admin".equals(activeNav) ? "page page--admin" : "page"}">
<a class="skip-link" href="#main-content">Skip to main content</a>
<header class="${"admin".equals(activeNav) ? "site-header site-header--admin" : "site-header"}">
    <div class="site-header__inner">
        <a class="brand" href="/">
            <svg class="brand__mark" viewBox="0 0 42 26" aria-hidden="true"><path d="M6 3h25a6 6 0 0 1 0 20H6z"/><path d="M15 3v20M24 3v20"/></svg>
            <span><span class="brand__title">Oil Tank Route</span><span class="brand__tag">Residential field tools</span></span>
        </a>
        @if ("admin".equals(activeNav))
            <div class="admin-toolbar" aria-label="Admin">
                <span class="admin-toolbar__label">Protected dashboard</span>
                <a class="admin-toolbar__link" href="/admin/exports/admin-metrics-snapshot.json">Metrics JSON</a>
            </div>
        @else
            <div class="site-header__rail">
                <nav class="site-nav" aria-label="Primary">
                    <a class="${"tools".equals(activeNav) ? "site-nav__link site-nav__link--active" : "site-nav__link"}" href="/oil-tank-gauge-calculator/">Tools</a>
                    <a class="${("guide".equals(activeNav) || "guides".equals(activeNav)) ? "site-nav__link site-nav__link--active" : "site-nav__link"}" href="/heating-oil-tank/">Tank Guides</a>
                    <a class="${"risk".equals(activeNav) ? "site-nav__link site-nav__link--active" : "site-nav__link"}" href="/oil-tank-replacement-planner/">Risk &amp; Replacement</a>
                    <a class="${("state".equals(activeNav) || "states".equals(activeNav)) ? "site-nav__link site-nav__link--active" : "site-nav__link"}" href="/states/">State Resources</a>
                </nav>
            </div>
        @endif
    </div>
</header>
<main class="site-main" id="main-content" tabindex="-1">
    ${content}
</main>
@if (!"admin".equals(activeNav))
    <footer class="site-footer">
        <div class="site-footer__inner">
            <div class="site-footer__brand">
                <p>Residential heating-oil tank measurements, risk routing, and sourced next steps.</p>
                <span>Results are planning information - not a substitute for a qualified inspection, delivery decision, or emergency response.</span>
            </div>
            <div class="site-footer__links">
                <a href="/methodology/">Methodology</a>
                <a href="/guides/">Resources</a>
                <a href="/contact/">Contact</a>
                <a href="/privacy/">Privacy</a>
                <a href="/terms/">Terms</a>
                <a href="/not-government-affiliated/">Not government affiliated</a>
            </div>
        </div>
    </footer>
@endif
<script src="/app.js"></script>
<script type="module" src="/tools.js"></script>
</body>
</html>

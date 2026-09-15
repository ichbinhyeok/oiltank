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
    @if (!"admin".equals(activeNav))
    <link rel="stylesheet" href="/site-v2.css">
    @endif
</head>
<body class="${"admin".equals(activeNav) ? "page page--admin" : "page page--v2"}">
<a class="skip-link" href="#main-content">Skip to main content</a>
<header class="${"admin".equals(activeNav) ? "site-header site-header--admin" : "v2-site-header"}">
    <div class="${"admin".equals(activeNav) ? "site-header__inner" : "v2-header-inner"}">
        <a class="brand" href="/">
            <svg class="brand__mark" viewBox="0 0 32 32" aria-hidden="true"><path d="M5 26V6h13l9 9v11H5Z"/><path d="M18 6v9h9M11 20h10"/></svg>
            <span><span class="brand__title">Oil Tank Route<span class="v2-brand-dot">.</span></span><span class="brand__tag">Property record research</span></span>
        </a>
        @if ("admin".equals(activeNav))
            <div class="admin-toolbar" aria-label="Admin">
                <span class="admin-toolbar__label">Protected dashboard</span>
                <a class="admin-toolbar__link" href="/admin/exports/admin-metrics-snapshot.json">Metrics JSON</a>
            </div>
        @else
            <div class="v2-header-rail">
                <details class="v2-mobile-menu"><summary>Menu <span aria-hidden="true">＋</span></summary><nav aria-label="Mobile navigation"><a href="/how-it-works/">How it works</a><a href="/research-areas/">Research areas</a><a href="/sample-brief/">Sample brief</a><a href="/#start-check">Start a property record check ↗</a></nav></details>
                <nav class="v2-desktop-nav" aria-label="Primary">
                    <a class="v2-nav-link" aria-current="${"how".equals(activeNav) ? "page" : "false"}" href="/how-it-works/">How it works</a>
                    <a class="v2-nav-link" aria-current="${"areas".equals(activeNav) ? "page" : "false"}" href="/research-areas/">Research areas</a>
                    <a class="v2-nav-link" aria-current="${"brief".equals(activeNav) ? "page" : "false"}" href="/sample-brief/">Sample brief</a>
                    <a class="v2-nav-cta" href="/#start-check">Start a check <span aria-hidden="true">↗</span></a>
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
                <a class="v2-footer-wordmark" href="/">Oil Tank Route.</a>
                <p>Property-specific oil tank record research, agency routing, and transaction briefs.</p>
                <span>Research distinguishes confirmed facts from gaps. It is not a safety certification, legal opinion, or environmental determination.</span>
            </div>
            <div class="site-footer__links">
                <a href="/methodology/">Methodology</a>
                <a href="/guides/">Resources</a>
                <a href="/states/">State routes</a>
                <a href="/record-research/">Research scope</a>
                <a href="/research-areas/">Local research routes</a>
                <a href="/research-examples/">Source walkthroughs</a>
                <a href="/tools/">Tools</a>
                <a href="/contact/">Contact</a>
                <a href="/privacy/">Privacy</a>
                <a href="/terms/">Terms</a>
                <a href="/not-government-affiliated/">Not government affiliated</a>
            </div>
        </div>
    </footer>
@endif
<script src="/app.js"></script>
@if (!"admin".equals(activeNav))
<script src="/site-v2.js" defer></script>
@endif
<script type="module" src="/tools.js"></script>
</body>
</html>

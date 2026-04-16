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
    @if (!meta.indexable())
        <meta name="robots" content="noindex,follow">
    @endif
    @for (var schema : meta.structuredDataJson())
        <script type="application/ld+json">$unsafe{schema}</script>
    @endfor
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&family=Newsreader:ital,opsz,wght@0,6..72,400;0,6..72,500;0,6..72,600;0,6..72,700;1,6..72,600;1,6..72,700&display=swap" rel="stylesheet">
    <link rel="icon" href="/favicon.svg" type="image/svg+xml">
    <link rel="icon" href="/favicon.ico" sizes="32x32">
    <link rel="apple-touch-icon" href="/apple-touch-icon.png">
    <script>document.documentElement.classList.add('js');</script>
    <link rel="stylesheet" href="/app.css">
</head>
<body class="${"admin".equals(activeNav) ? "page page--admin" : "page"}">
<header class="${"admin".equals(activeNav) ? "site-header site-header--admin" : "site-header"}">
    <div class="site-header__inner">
        <a class="brand" href="/">Buried Oil Tank Verdict</a>
        @if ("admin".equals(activeNav))
            <div class="admin-toolbar" aria-label="Admin">
                <span class="admin-toolbar__label">Protected dashboard</span>
                <a class="admin-toolbar__link" href="/admin/exports/admin-metrics-snapshot.json">Metrics JSON</a>
            </div>
        @else
            <div class="site-header__rail">
                <nav class="site-nav" aria-label="Primary">
                    <a class="${"home".equals(activeNav) ? "site-nav__link site-nav__link--active" : "site-nav__link"}" href="/">Overview</a>
                    <a class="${("state".equals(activeNav) || "states".equals(activeNav)) ? "site-nav__link site-nav__link--active" : "site-nav__link"}" href="/states/">States</a>
                    <a class="${("route".equals(activeNav) || "routes".equals(activeNav)) ? "site-nav__link site-nav__link--active" : "site-nav__link"}" href="/routes/">Routes</a>
                    <a class="${("guide".equals(activeNav) || "guides".equals(activeNav)) ? "site-nav__link site-nav__link--active" : "site-nav__link"}" href="/guides/">Guides</a>
                    <a class="${"static".equals(activeNav) ? "site-nav__link site-nav__link--active" : "site-nav__link"}" href="/methodology/">Methodology</a>
                </nav>
                <a class="site-header__action" href="${("state".equals(activeNav) || "route".equals(activeNav) || "guide".equals(activeNav)) ? "#case-checklist" : "/states/"}">${("state".equals(activeNav) || "route".equals(activeNav) || "guide".equals(activeNav)) ? "Next-step checklist" : "Find your state"}</a>
            </div>
        @endif
    </div>
</header>
<main class="site-main">
    ${content}
</main>
@if (!"admin".equals(activeNav))
    <footer class="site-footer">
        <div class="site-footer__inner">
            <div class="site-footer__brand">
                <p>State-first next-action guidance for buried and abandoned residential heating oil tank triggers.</p>
                <span>Built for live files, missing records, sweep-first questions, and contract-side timing pressure.</span>
            </div>
            <div class="site-footer__links">
                <a href="/contact/">Contact</a>
                <a href="/privacy/">Privacy</a>
                <a href="/terms/">Terms</a>
                <a href="/not-government-affiliated/">Not government affiliated</a>
            </div>
        </div>
    </footer>
@endif
<script src="/app.js"></script>
</body>
</html>

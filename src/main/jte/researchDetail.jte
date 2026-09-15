@import owner.buriedoiltank.data.ResearchCatalog
@import owner.buriedoiltank.data.ResearchCatalog.Entry
@import owner.buriedoiltank.pages.PageModels.PageMeta
@param PageMeta meta
@param Entry entry
@template.layout.page(meta = meta, activeNav = "areas", content = @`
<header class="v2-detail-header" data-page-id="${entry.id()}" data-state-slug="${entry.state()}"><div class="v2-shell"><nav class="v2-breadcrumb" aria-label="Breadcrumb"><a href="/research-areas/">Research areas</a><span>/</span><span>${entry.locality()}</span></nav><div class="v2-detail-heading"><div><p class="v2-eyebrow">${entry.locality()}</p><h1>${entry.title()}</h1><p>${entry.summary()}</p></div><div class="v2-detail-stamp"><span class="v2-status-dot"></span><span>Source-backed guidance<br><strong>Reviewed Sep 16, 2026</strong></span></div></div></div></header>
<div class="v2-shell v2-research-layout">
    <aside class="v2-research-nav"><p class="v2-filter-label">In this research route</p><nav aria-label="On this page"><a href="#prepare">Before you search</a><a href="#research-sequence">Sources &amp; sequence</a><a href="#request-plan">Request outline</a><a href="#limitations">What remains uncertain</a></nav><div class="v2-research-assist"><h2>Want us to<br>work through it?</h2><p>Send the property and the question. We’ll take the next research step.</p><a class="v2-button" href="#start-check" data-service-cta data-cta-location="local-route-hero">Start a check <span>↗</span></a><small>Free founder-led beta</small></div></aside>
    <div class="v2-research-body"><section id="prepare" class="v2-preparation"><p class="v2-eyebrow">Before you search</p><h2>Start with the right identifiers.</h2><p>${entry.identifiers()}</p><small>Bring what you have. Missing identifiers can be part of the route review.</small></section>
    <section id="research-sequence" class="v2-research-steps research-steps">
    @for (int i = 0; i < entry.steps().size(); i++)
        !{var step = entry.steps().get(i);}
        <article><span class="v2-step-number">0${i + 1}</span><div><h2>${step.title()}</h2><p>${step.body()}</p><a class="research-source v2-source" href="${step.sourceUrl()}" rel="external"><span>Official source</span><strong>${step.sourceLabel()}</strong><b aria-hidden="true">↗</b></a></div></article>
    @endfor
    </section>
    <section id="request-plan" class="v2-request"><div class="v2-request__head"><div><p class="v2-eyebrow">Make the request specific</p><h2>A ready-to-adapt outline.</h2></div><button type="button" data-copy-request hidden>Copy outline</button></div><blockquote data-request-text>${entry.request()}</blockquote><p>Replace the bracketed fields and follow the agency’s current form, disclosure, and fee instructions. This is a draft, not a sent request.</p><span class="v2-copy-status" data-copy-status role="status"></span></section>
    <section id="limitations" class="v2-limits"><h2>Keep the limits visible.</h2><p>${entry.boundary()}</p><a href="/guides/leaking-heating-oil-tank-what-to-do/">Active leak or strong oil odor? Use the safety route. ↗</a></section>
    <section class="v2-related"><p class="v2-eyebrow">Continue your research</p>
    @for (var municipal : ResearchCatalog.AREAS)
        @if (municipal.related().contains(entry.slug()) && !entry.related().contains(municipal.slug()))
        <a href="${municipal.path()}">${municipal.locality()} — record route <span>↗</span></a>
        @endif
    @endfor
    @for (var slug : entry.related())
        !{var related = ResearchCatalog.find(slug);}
        <a href="${related.path()}">${related.title()} <span>↗</span></a>
    @endfor
    <a href="/research-examples/">See two public-source walkthroughs <span>↗</span></a><a href="/sample-brief/">Explore the illustrative brief <span>↗</span></a></section>
    </div>
</div>
@template.partials.intakeSection(pageId = entry.id(), pagePath = entry.path(), defaultState = entry.state(), defaultLocality = entry.kind().equals("area") ? entry.locality() : "", defaultQuestion = entry.question())
`)

@import owner.buriedoiltank.data.ResearchCatalog
@import owner.buriedoiltank.data.ResearchCatalog.Entry
@import owner.buriedoiltank.data.RecordSearchGuide
@import owner.buriedoiltank.data.TankDocumentCheck
@import owner.buriedoiltank.pages.PageModels.PageMeta
@param PageMeta meta
@param Entry entry
!{var searchGuide = RecordSearchGuide.forEntry(entry);}
@template.layout.page(meta = meta, activeNav = entry.kind().equals("problem") ? "documents" : "finder", content = @`
<header class="v2-detail-header" data-page-id="${entry.id()}" data-state-slug="${entry.state()}"><div class="v2-shell"><nav class="v2-breadcrumb" aria-label="Breadcrumb"><a href="/find-records/">Find records</a><span>/</span><span>${entry.locality()}</span></nav><div class="v2-detail-heading"><div><p class="v2-eyebrow">${entry.locality()}</p><h1>${entry.title()}</h1><p>${entry.summary()}</p>
@if (entry.kind().equals("problem"))
<div class="rf-start-actions"><a class="v2-button" href="#document-check">Build my document checklist ↓</a><a href="#research-sequence">Read the official-source steps ↓</a></div>
@elseif (searchGuide != null)
<div class="rf-start-actions" data-record-start>
@if (entry.slug().equals("seattle-residential-tank-records"))
<a class="v2-button" href="#live-search">Search Seattle records here <span aria-hidden="true">↓</span></a><a href="${entry.steps().get(searchGuide.startStep()).sourceUrl()}" rel="external">Open the official dataset ↗</a>
@else
<a class="v2-button" href="${entry.steps().get(searchGuide.startStep()).sourceUrl()}" rel="external">${searchGuide.action()} <span aria-hidden="true">↗</span></a>
@endif
<a href="#prepare">What to prepare ↓</a></div><p class="rf-start-note">Official links open the agency site. No records request is submitted from this page.</p>
@endif
</div><div class="v2-detail-stamp"><span class="v2-status-dot"></span><span>Source instructions reviewed<br><strong>${entry.reviewedOn().toString()}</strong><br>Not a live availability guarantee</span></div></div></div></header>
<div class="v2-shell v2-research-layout">
    <aside class="v2-research-nav"><p class="v2-filter-label">In this record route</p><nav aria-label="On this page">
    @if (entry.slug().equals("seattle-residential-tank-records"))
    <a href="#live-search">Search Seattle records</a>
    @endif
    @if (entry.kind().equals("problem"))
    <a href="#document-check">Check document gaps</a>
    @endif
    <a href="#prepare">Before you search</a><a href="#research-sequence">Official sources</a><a href="#check-documents">Check your documents</a><a href="#worksheet">Your worksheet</a><a href="#request-plan">Request outline</a><a href="#limitations">Limits</a></nav><div class="v2-research-assist"><h2>Find it.<br>Keep the trail.</h2><p>Use the official routes and private worksheet. No email required.</p><a class="v2-button" href="#worksheet">Prepare your search <span>↗</span></a><a href="/find-records/">Change place or document ↗</a></div></aside>
    <div class="v2-research-body">
    @if (entry.kind().equals("problem"))
    <section class="dc-short-answer"><h2>The short answer</h2><p>${TankDocumentCheck.situation(entry.slug()).answer()}</p><p>${TankDocumentCheck.situation(entry.slug()).next()}</p><a href="/guides/buried-oil-tank-home-sale/">Buying or selling? Assemble the wider document file ↗</a></section>
    @template.partials.tankDocumentCheck(situationId = entry.slug())
    @endif
    @if (entry.slug().equals("seattle-residential-tank-records"))
    @template.partials.seattleRecordSearch()
    @endif
    <section id="prepare" class="v2-preparation"><p class="v2-eyebrow">Before you search</p><h2>Start with the right identifiers.</h2><p>${entry.identifiers()}</p><small>Bring what you have. Missing identifiers can be part of the route review.</small></section>
    @if (searchGuide != null)
    <section class="rf-search-outcome" aria-label="Search target and fallback"><dl><dt>What you are looking for</dt><dd>${searchGuide.expected()}</dd><dt>If the result is empty or incomplete</dt><dd>${searchGuide.fallback()} <a href="#request-plan">Adapt the request outline ↓</a></dd></dl></section>
    @endif
    <section id="research-sequence" class="v2-research-steps research-steps">
    @for (int i = 0; i < entry.steps().size(); i++)
        !{var step = entry.steps().get(i);}
        <article><span class="v2-step-number">0${i + 1}</span><div><h2>${step.title()}</h2><p>${step.body()}</p><a class="research-source v2-source" href="${step.sourceUrl()}" rel="external"><span>Official source</span><strong>${step.sourceLabel()}</strong><b aria-hidden="true">↗</b></a></div></article>
    @endfor
    </section>
    @template.partials.recordEvidence(entry = entry)
    @if (searchGuide != null)
    <section class="rf-route-answers" aria-label="Common record questions"><h2>Before you interpret the result</h2>
    @for (var answer : searchGuide.answers())
        <article><h3>${answer.question()}</h3><p>${answer.answer()}</p></article>
    @endfor
    </section>
    @endif
    @template.partials.recordWorkbench(entry = entry)
    <section id="request-plan" class="v2-request"><div class="v2-request__head"><div><p class="v2-eyebrow">Make the request specific</p><h2>A ready-to-adapt outline.</h2></div><button type="button" data-copy-request hidden>Copy outline</button></div><blockquote data-request-text>${entry.request()}</blockquote><p>Replace the bracketed fields and follow the agency’s current form, disclosure, and fee instructions. This is a draft, not a sent request.</p><span class="v2-copy-status" data-copy-status role="status"></span></section>
    <section id="limitations" class="v2-limits"><h2>Keep the limits visible.</h2><p>${entry.boundary()}</p><a href="/guides/leaking-heating-oil-tank-what-to-do/">Active leak or strong oil odor? Use the safety route. ↗</a></section>
    <section class="v2-related"><p class="v2-eyebrow">Continue your research</p>
    <a href="/tank-document-check/">Work through another document question <span>↗</span></a>
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
@if (entry.assisted())
@template.partials.intakeSection(pageId = entry.id(), pagePath = entry.path(), defaultState = entry.state(), defaultLocality = entry.kind().equals("area") ? entry.locality() : "", defaultQuestion = entry.question())
@endif
`)

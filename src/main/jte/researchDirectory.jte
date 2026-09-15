@import owner.buriedoiltank.data.ResearchCatalog
@import owner.buriedoiltank.pages.PageModels.PageMeta
@param PageMeta meta
@template.layout.page(meta = meta, activeNav = "areas", content = @`
<div class="v2-directory" data-page-id="research:areas">
<header class="v2-shell v2-directory__header"><p class="v2-eyebrow">Research areas</p><h1>A local starting point<br>for your property.</h1><p>Find the portals, record holders, and request sequence for your area. County routes and municipal routes are complementary—not interchangeable.</p></header>
<section class="v2-shell v2-explorer" data-area-explorer>
    <aside class="v2-explorer__filters"><div data-area-controls hidden><label for="area-search">Find an area</label><div class="v2-search"><span aria-hidden="true">⌕</span><input id="area-search" type="search" placeholder="Town, county, or state" data-area-search autocomplete="off"></div><p class="v2-filter-label">Location</p><div class="v2-filters" role="group" aria-label="Filter research areas"><button type="button" data-area-filter="all" aria-pressed="true">All areas <span>${ResearchCatalog.AREAS.size()}</span></button><button type="button" data-area-filter="new-jersey" aria-pressed="false">New Jersey <span>${ResearchCatalog.areaCount("new-jersey")}</span></button><button type="button" data-area-filter="new-york" aria-pressed="false">New York <span>${ResearchCatalog.areaCount("new-york")}</span></button></div></div><div class="v2-aside-note"><span class="v2-status-dot"></span><p>Public guidance reviewed<br><strong>September 16, 2026</strong></p></div><p class="v2-fine">These are researched routes, not claims of local offices or completed customer cases.</p></aside>
    <div class="v2-explorer__results"><div class="v2-results-heading"><h2>Research routes</h2><span data-area-count aria-live="polite">${ResearchCatalog.AREAS.size()} areas</span></div>
    @for (var state : java.util.List.of("new-jersey", "new-york"))
    <section id="${state}" data-area-group="${state}"><h3 class="v2-region-label">${state.equals("new-jersey") ? "New Jersey" : "New York"}</h3>
    @for (var entry : ResearchCatalog.AREAS)
        @if (entry.state().equals(state))
        <a class="v2-area-row" href="${entry.path()}" data-area-row data-state="${state}" data-search="${entry.locality() + " " + (state.equals("new-jersey") ? "New Jersey NJ" : "New York NY")}"><div class="v2-area-monogram" aria-hidden="true">${entry.locality().substring(0, 1)}</div><div><h4>${entry.locality()}</h4><p>${entry.summary()}</p><span>View sources &amp; request steps</span></div><span class="v2-row-arrow" aria-hidden="true">↗</span></a>
        @endif
    @endfor
    </section>
    @endfor
    <div class="v2-empty" data-area-empty hidden><h3>No matching area yet.</h3><p>Try a county or state, or send the property for an initial route review.</p><a href="/#start-check">Ask about your property ↗</a></div>
    <div class="v2-coverage-note"><h3>Don’t see your municipality?</h3><p>We review other NJ and NY locations at intake. Coverage is confirmed before research begins.</p><a href="/#start-check">Send your property question ↗</a><div><a href="/states/new-jersey/records-and-proof/">NJ statewide sources</a><a href="/states/new-york/records-and-proof/">NY statewide sources</a></div></div>
    </div>
</section>
</div>
<section class="v2-section v2-soft" id="questions"><div class="v2-shell"><div class="v2-section-head"><div><p class="v2-eyebrow">Or start with a question</p><h2>What’s holding<br>up your next step?</h2></div><a class="v2-text-link" href="/research-examples/">See how we research ↗</a></div><div class="v2-question-rows">
@for (var entry : ResearchCatalog.PROBLEMS)
<a href="${entry.path()}"><span>${entry.locality()}</span><h3>${entry.title()}</h3><b aria-hidden="true">↗</b></a>
@endfor
</div></div></section>
`)

@import owner.buriedoiltank.pages.PageModels.RecordsNavigatorPageModel
@param RecordsNavigatorPageModel page
@template.layout.page(meta = page.meta(), activeNav = "state", content = @`
<section class="hero hero--detail records-hero" data-page-id="${page.sourceId()}" data-state-slug="${page.stateSlug()}">
    <div class="hero__inner hero__inner--detail">
        <div class="hero__copy hero__copy--detail prose">
            @template.partials.breadcrumbs(page.breadcrumbs())
            <div class="section__eyebrow">${page.eyebrow()}</div>
            <h1>${page.heading()}</h1>
            <p class="hero__lede">${page.intro()}</p>
            <div class="status-row">
                <span class="badge">Official-source workflow</span>
                <span class="badge">Property-specific research</span>
                <span class="badge">${page.stateName()}</span>
            </div>
            <div class="hero__actions hero__actions--detail">
                <a class="button" href="#start-check" data-service-cta data-cta-location="state-records-hero">Start a property record check</a>
            </div>
        </div>
        <aside class="hero__aside hero__aside--detail">
            <div class="support-panel records-callout">
                <span class="detail-card__label">What you leave with</span>
                <strong>Case identifiers, source documents, and a defensible next step.</strong>
                <p>The tool does not decide whether a property is safe or tank-free.</p>
            </div>
        </aside>
    </div>
</section>
<section class="service-section service-section--paper"><div class="service-shell">
    <p class="service-kicker">Local files beyond statewide databases</p><h2>Use the property's local research route.</h2>
    <p>Incident totals describe a dataset, not this property's condition. Local permits, verification letters, and agency requests need their own research path.</p>
    <div class="research-question-list">
    @for (var route : owner.buriedoiltank.data.ResearchCatalog.AREAS)
        @if (route.state().equals(page.stateSlug()) && (page.countyName() == null || page.countyName().isBlank() || route.locality().toLowerCase().contains(page.countyName().toLowerCase())))
        <a href="${route.path()}"><h3>${route.title()}</h3><p>${route.summary()}</p></a>
        @endif
    @endfor
    </div><a href="/research-areas/">Browse all researched areas and missing-record questions →</a>
</div></section>

@if (!page.incidentAggregates().isEmpty())
<section class="section section--soft" data-analytics-view="county_incident_view" data-page-id="${page.countyName() == null ? page.stateSlug() + ":records-and-proof" : page.stateSlug() + ":county:" + page.countyName().toLowerCase()}">
    <div class="section__inner">
        <div class="section__header">
            <div>
                <div class="section__eyebrow">Official incident data</div>
                <h2>${page.countyName() == null ? "Where reported residential fuel-oil incidents concentrate" : page.countyName() + " County data snapshot"}</h2>
            </div>
        </div>
        <div class="incident-grid">
            @for (var row : page.incidentAggregates())
                <article class="metric-panel">
                    <span class="detail-card__label">#${row.stateRank()} in current comparison</span>
                    <strong class="metric-panel__value">${row.incidentCount()}</strong>
                    <h3>${row.countyName()} County</h3>
                    <p>Private-dwelling fuel-oil incidents reported from ${row.windowStart().toString()} through ${row.windowEnd().toString()}.</p>
                    @if (page.countyName() == null && row.stateRank() <= 3)
                        <a href="/states/new-york/counties/${row.countySlug()}/heating-oil-spill-records/">Open county record guide</a>
                    @endif
                </article>
            @endfor
        </div>
        <p class="data-note">Counts are reproducible aggregates from <a href="${page.incidentAggregates().get(0).sourceUrl()}" rel="noopener" data-analytics-event="official_lookup_click">${page.incidentAggregates().get(0).sourceTitle()}</a>. Updated ${page.incidentAggregates().get(0).updatedOn().toString()}. They are not counts of tanks, affected homes, or contamination findings.</p>
    </div>
</section>
@endif

<section id="official-sources" class="section" data-page-id="${page.countyName() == null ? page.stateSlug() + ":records-and-proof" : page.stateSlug() + ":county:" + page.countyName().toLowerCase()}">
    <div class="section__inner">
        <div class="section__header">
            <div>
                <div class="section__eyebrow">Official lookup sequence</div>
                <h2>Open the source, keep the identifiers, then check the gaps.</h2>
            </div>
        </div>
        <div class="source-action-grid">
            @for (var source : page.lookupSources())
                <article class="support-panel source-action-card">
                    <span class="detail-card__label">${source.jurisdiction()} · ${source.sourceType()}</span>
                    <h3>${source.title()}</h3>
                    <p><strong>${source.agency()}</strong></p>
                    <strong>Search by</strong>
                    <ul class="guardrail-list guardrail-list--dense">
                        @for (var item : source.searchBy())
                            <li>${item}</li>
                        @endfor
                    </ul>
                    <dl class="route-intelligence">
                        <div><dt>Identifiers to keep</dt><dd>${java.lang.String.join(", ", source.identifiers())}</dd></div>
                        <div><dt>Request route</dt><dd>${source.requestMethod()}</dd></div>
                        <div><dt>Request conditions</dt><dd>${source.requestRequirements()}</dd></div>
                        <div><dt>Fee / timing</dt><dd>${source.fee()} · ${source.responseTime()}</dd></div>
                        <div><dt>Fallback</dt><dd>${source.fallbackRoute()}</dd></div>
                    </dl>
                    <p class="data-note">${source.caveat()}</p>
                    <p class="data-note">Route verified ${source.verifiedOn()}.</p>
                    <a class="button button--secondary" href="${source.url()}" target="_blank" rel="noopener" data-analytics-event="official_lookup_click" data-source-title="${source.title()}">Open official lookup</a>
                </article>
            @endfor
        </div>
    </div>
</section>

<section class="section section--contrast">
    <div class="section__inner two-column">
        <div class="support-panel support-panel--dark">
            <div class="section__eyebrow section__eyebrow--inverse">Checklist</div>
            <ol class="guardrail-list" data-records-checklist data-page-id="${page.countyName() == null ? page.stateSlug() + ":records-and-proof" : page.stateSlug() + ":county:" + page.countyName().toLowerCase()}">
                @for (var item : page.checklist())
                    <li><label><input type="checkbox" data-records-check> ${item}</label></li>
                @endfor
            </ol>
        </div>
        <div class="support-panel support-panel--dark">
            <div class="section__eyebrow section__eyebrow--inverse">Limits</div>
            <ul class="guardrail-list">
                @for (var item : page.limits())
                    <li>${item}</li>
                @endfor
            </ul>
        </div>
    </div>
</section>

<section class="service-section service-section--form" id="start-check" data-service-cta-view data-page-id="${page.stateSlug()}:record-research">
    <div class="service-shell form-layout">
        <div class="form-layout__copy"><p class="service-kicker">${page.stateName()} record research</p><h2>Send the property, not just the question.</h2><p>The founder will use this route intelligence to research the specific parcel, request missing files when needed, and explain the resulting evidence.</p></div>
        @template.partials.researchForm(pageId = page.sourceId(), pagePath = page.sourcePath(), defaultState = page.stateSlug(), defaultLocality = page.countyName() == null ? "" : page.countyName())
    </div>
</section>
`)

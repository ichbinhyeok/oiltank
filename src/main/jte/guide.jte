@import owner.buriedoiltank.pages.PageModels.GuidePageModel
@param GuidePageModel page
@template.layout.page(meta = page.meta(), activeNav = "guide", content = @`
<section class="hero hero--detail">
    <div class="hero__inner hero__inner--detail">
        <div class="hero__copy hero__copy--detail prose">
            @template.partials.breadcrumbs(page.breadcrumbs())
            <div class="section__eyebrow">Cross-state guide</div>
            <h1>${page.heading()}</h1>
            <p class="hero__lede">${page.guide().quickAnswer()}</p>
            <div class="hero__actions hero__actions--detail">
                <a class="button" href="#case-checklist" data-primary-cta>Open the file checklist</a>
            </div>
        </div>
        <div class="hero__aside">
            <div class="hero__media hero__media--detail">
                <img class="hero__image" src="https://images.unsplash.com/photo-1457298483369-0a95d2b17fcd?auto=format&fit=crop&w=1400&q=80" alt="Close-up of a fountain pen nib on paper" width="1400" height="1167" loading="eager" fetchpriority="high" decoding="async">
            </div>
            <div class="info-card">
                <span class="info-card__label">What this guide gives you</span>
                <ul class="mini-list">
                    @for (var item : page.takeaways())
                        <li>${item}</li>
                    @endfor
                </ul>
            </div>
        </div>
    </div>
</section>

@template.partials.sourceReview(page.sourceReview())
@template.partials.audienceCards("Who this guide helps first", "Use the guide to tighten a live deal before you drop into a state route.", page.audienceCards())

<section class="section section--soft">
    <div class="section__inner two-column">
        <div class="support-panel">
            <div class="section__eyebrow">How to use this guide</div>
            <ul class="guardrail-list">
                @for (var point : page.guide().introPoints())
                    <li>${point}</li>
                @endfor
            </ul>
        </div>
        <div class="support-panel">
            <div class="section__eyebrow">What not to assume</div>
            <ul class="guardrail-list">
                @for (var point : page.guide().whatNotToAssume())
                    <li>${point}</li>
                @endfor
            </ul>
        </div>
    </div>
</section>

<section class="section">
    <div class="section__inner">
        <div class="section__header">
            <div>
                <div class="section__eyebrow">Use this guide when</div>
                <h2>Use the guide first when the state-specific route is still too early.</h2>
            </div>
        </div>
        <div class="two-column">
            <div class="support-panel">
                <ul class="guardrail-list">
                    @for (var point : page.guide().bestFor())
                        <li>${point}</li>
                    @endfor
                </ul>
            </div>
            <div class="support-panel">
                <div class="section__eyebrow">Before you ask for quotes</div>
                <ul class="guardrail-list">
                    @for (var point : page.guide().beforeYouCall())
                        <li>${point}</li>
                    @endfor
                </ul>
            </div>
        </div>
    </div>
</section>

<section class="section section--soft">
    <div class="section__inner two-column">
        <div class="support-panel">
            <div class="section__eyebrow">Documents that matter first</div>
            <ul class="guardrail-list">
                @for (var point : page.guide().documentsThatMatter())
                    <li>${point}</li>
                @endfor
            </ul>
        </div>
        <div class="support-panel">
            <div class="section__eyebrow">When to switch routes</div>
            <ul class="guardrail-list">
                @for (var point : page.guide().whenToEscalate())
                    <li>${point}</li>
                @endfor
            </ul>
        </div>
    </div>
</section>

<section id="states" class="section">
    <div class="section__inner">
        <div class="section__header">
            <div>
                <div class="section__eyebrow">${page.guide().stateEntryPrompt()}</div>
                <h2>Drop into the state page once the evidence narrows the answer.</h2>
            </div>
        </div>
        <div class="card-grid">
            @for (var state : page.stateEntries())
                <a class="card card--state" href="${state.entryPath()}">
                    <strong>${state.name()}</strong>
                    <p>${state.quickAnswer()}</p>
                    <ul class="mini-list">
                        @for (var point : state.highlights())
                            <li>${point}</li>
                        @endfor
                    </ul>
                </a>
            @endfor
        </div>
    </div>
</section>

<section id="routes" class="section section--contrast">
    <div class="section__inner">
        <div class="section__header">
            <div>
                <div class="section__eyebrow section__eyebrow--inverse">State route links</div>
                <h2>Use these route surfaces after the file is specific enough.</h2>
            </div>
        </div>
        <div class="route-grid route-grid--secondary">
            @for (var card : page.routeLinks())
                <a class="route-card route-card--secondary" href="${card.href()}">
                    <span class="route-card__badge">${card.badge()}</span>
                    <strong>${card.title()}</strong>
                    <p>${card.description()}</p>
                </a>
            @endfor
        </div>
    </div>
</section>

@template.partials.sourceStack(page.guide().sourceStack())
@template.partials.leadForm(page.cta())
`)

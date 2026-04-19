@import owner.buriedoiltank.pages.PageModels.GuidePageModel
@param GuidePageModel page
@template.layout.page(meta = page.meta(), activeNav = "guide", content = @`
<section class="hero hero--detail">
    <div class="hero__inner hero__inner--detail">
        <div class="hero__copy hero__copy--detail prose">
            @template.partials.breadcrumbs(page.breadcrumbs())
            <div class="section__eyebrow">Cross-state starting point</div>
            <h1>${page.heading()}</h1>
            <p class="hero__lede">${page.guide().quickAnswer()}</p>
            <div class="hero__actions hero__actions--detail">
                <a class="button" href="#first-steps" data-primary-cta>Jump to first steps</a>
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
@template.partials.audienceCards("Who this guide helps first", "Use this guide when you need the first smart move before the answer turns state-specific.", page.audienceCards())

<section id="first-steps" class="section section--soft">
    <div class="section__inner two-column">
        <div class="support-panel">
            <div class="section__eyebrow">Start here</div>
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
                <div class="section__eyebrow">When this guide is useful</div>
                <h2>Start here when you still need a clean first move.</h2>
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
                <div class="section__eyebrow">Before you call a contractor</div>
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
                <h2>Go to the state page once the paperwork or site clues make the answer specific.</h2>
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
                <div class="section__eyebrow section__eyebrow--inverse">State-specific next steps</div>
                <h2>Open these once permits, local rules, or cleanup language start controlling the answer.</h2>
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

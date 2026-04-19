@import owner.buriedoiltank.pages.PageModels.StatePageModel
@param StatePageModel page
@template.layout.page(meta = page.meta(), activeNav = "state", content = @`
<section class="hero hero--detail">
    <div class="hero__inner hero__inner--detail">
        <div class="hero__copy hero__copy--detail">
            @template.partials.breadcrumbs(page.breadcrumbs())
            <div class="section__eyebrow">${page.state().abbreviation()} paperwork, permits, and next steps</div>
            <h1>${page.heading()}</h1>
            <p class="hero__lede">${page.state().quickAnswer()}</p>
            <p>Use this page when disclosure, missing paperwork, or a sweep decision depends on ${page.state().name()} rules.</p>
            <div class="status-row">
                <span class="badge">State page</span>
                <span class="badge">${page.sourceReview().freshnessStatus()}</span>
                <span class="badge">${page.state().abbreviation()}</span>
            </div>
            <div class="hero__actions hero__actions--detail">
                <a class="button" href="#first-steps" data-primary-cta>Jump to first steps</a>
            </div>
        </div>
        <div class="hero__aside hero__aside--detail">
            <div class="hero__media hero__media--detail">
                <img class="hero__image" src="https://images.unsplash.com/photo-1768119997334-bc76e6f60e7d?auto=format&fit=crop&w=1400&q=80" alt="Rows of filing cabinets used to symbolize state records and archives" width="1400" height="1167" loading="eager" fetchpriority="high" decoding="async">
                <div class="hero__stamp hero__stamp--dark">
                    <strong>Primary agency source</strong>
                    <span>${page.state().regulatoryOwner()}</span>
                </div>
            </div>
            <div class="info-cluster info-cluster--detail">
                <div class="info-card">
                    <span class="info-card__label">Who sets the rule</span>
                    <strong>${page.state().regulatoryOwner()}</strong>
                </div>
                <div class="info-card">
                    <span class="info-card__label">What may change locally</span>
                    <p>${page.state().delegatedLocalAuthorities()}</p>
                </div>
            </div>
        </div>
    </div>
    <div class="section__inner">
        <div class="detail-grid detail-grid--hero">
            <div class="detail-card">
                <span class="detail-card__label">Review status</span>
                <p><strong>${page.sourceReview().freshnessStatus()}</strong> official review status</p>
            </div>
            <div class="detail-card">
                <span class="detail-card__label">Best first step</span>
                <p>${page.state().firstMoves().get(0)}</p>
            </div>
            <div class="detail-card">
                <span class="detail-card__label">Ask for this first</span>
                <p>${page.state().documentTargets().get(0)}</p>
            </div>
        </div>
    </div>
</section>

@template.partials.sourceReview(page.sourceReview())
@template.partials.audienceCards("Who should use this state page", "Use the state page to decide what to request next and who needs the facts first.", page.audienceCards())

<section id="first-steps" class="section section--soft">
    <div class="section__inner two-column">
        <div class="support-panel">
            <div class="section__eyebrow">Common sale-side situations in this state</div>
            <ul class="guardrail-list">
                @for (var item : page.state().strongestTriggerStates())
                    <li>${item}</li>
                @endfor
            </ul>
        </div>
        <div class="support-panel">
            <div class="section__eyebrow">Best first move in this state</div>
            <ul class="guardrail-list">
                @for (var item : page.state().firstMoves())
                    <li>${item}</li>
                @endfor
            </ul>
        </div>
    </div>
</section>

<section class="section">
    <div class="section__inner">
        <div class="section__header">
            <div>
                <div class="section__eyebrow">Questions to send today</div>
                <h2>Move the sale with document requests that change the answer.</h2>
            </div>
        </div>
        <ul class="guardrail-list guardrail-list--dense">
            @for (var item : page.todayQuestions())
                <li>${item}</li>
            @endfor
        </ul>
    </div>
</section>

<section class="section section--contrast">
    <div class="section__inner">
        <div class="section__header">
            <div>
                <div class="section__eyebrow section__eyebrow--inverse">Route families</div>
                <h2>Choose the page that matches what you know right now.</h2>
            </div>
        </div>
        <div class="route-grid">
            @for (var card : page.routeCards())
                <a class="route-card" href="${card.href()}">
                    <span class="route-card__badge">${card.badge()}</span>
                    <strong>${card.title()}</strong>
                    <p>${card.description()}</p>
                </a>
            @endfor
        </div>
        <div class="support-note">
            @if (page.state().launchReady())
                <strong>Keep the first step narrow.</strong>
                <p>Do not jump into removal, leak, or cost pages until the paperwork or locate result gives you a reason.</p>
            @else
                <strong>Public coverage stays narrower here.</strong>
                <p>Start with paperwork, disclosure, and confirmation. Move into cleanup or closure detail only after the documents justify it.</p>
            @endif
        </div>
    </div>
</section>

<section class="section section--soft">
    <div class="section__inner two-column">
        <div class="support-panel">
            <div class="section__eyebrow">Documents that change the answer</div>
            <ul class="guardrail-list">
                @for (var item : page.state().documentTargets())
                    <li>${item}</li>
                @endfor
            </ul>
        </div>
        <div class="support-panel">
            <div class="section__eyebrow">Signs this may be more than a paperwork problem</div>
            <ul class="guardrail-list">
                @for (var item : page.state().escalationSignals())
                    <li>${item}</li>
                @endfor
            </ul>
        </div>
    </div>
</section>

<section class="section">
    <div class="section__inner two-column">
        <div class="support-panel">
            <div class="section__eyebrow">Common triggers</div>
            <ul class="guardrail-list">
                @for (var trigger : page.state().commonTriggers())
                    <li>${trigger}</li>
                @endfor
            </ul>
        </div>
        <div class="support-panel">
            <div class="section__eyebrow">Easy mistakes</div>
            <ul class="guardrail-list">
                @for (var caution : page.state().cautions())
                    <li>${caution}</li>
                @endfor
            </ul>
        </div>
    </div>
</section>

@template.partials.sourceStack(page.state().sourceStack())
@template.partials.leadForm(page.cta())
`)

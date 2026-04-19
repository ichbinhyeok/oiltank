@import owner.buriedoiltank.pages.PageModels.RoutePageModel
@param RoutePageModel page
@template.layout.page(meta = page.meta(), activeNav = "route", content = @`
<section class="hero hero--detail">
    <div class="hero__inner hero__inner--detail">
        <div class="hero__copy hero__copy--detail prose">
            @template.partials.breadcrumbs(page.breadcrumbs())
            <div class="section__eyebrow">${page.route().routeFamily().displayLabel()}</div>
            <h1>${page.heading()}</h1>
            <p class="hero__lede">${page.quickAnswer()}</p>
            <div class="status-row">
                <span class="badge">${page.route().isIndexable() ? "Core route" : "Support route"}</span>
                <span class="badge">${page.sourceReview().freshnessStatus()}</span>
                <span class="badge">${page.state().abbreviation()}</span>
            </div>
            <div class="hero__actions hero__actions--detail">
                <a class="button" href="#first-steps" data-primary-cta>Jump to first steps</a>
            </div>
        </div>
        <div class="hero__aside hero__aside--detail">
            <div class="hero__media hero__media--detail">
                <img class="hero__image" src="https://images.unsplash.com/photo-1772732414979-f2f48b908fbe?auto=format&fit=crop&w=1400&q=80" alt="Topographic terrain model used to symbolize site verification" width="1400" height="1167" loading="eager" fetchpriority="high" decoding="async">
                <div class="hero__stamp hero__stamp--dark">
                    <strong>Before you spend money</strong>
                    <span>Check the paperwork, site clues, and state rules before you treat this like removal or cleanup.</span>
                </div>
            </div>
            <div class="info-cluster info-cluster--detail">
                <div class="info-card">
                    <span class="info-card__label">State-specific lens</span>
                    <p>${page.state().name()} rules and local agencies can change what the right first call is.</p>
                </div>
                <div class="info-card">
                    <span class="info-card__label">What this page does</span>
                    <strong>It helps you pick the next step, not guess the whole outcome.</strong>
                </div>
            </div>
        </div>
    </div>
    <div class="section__inner">
        <div class="detail-grid detail-grid--hero">
            <div class="detail-card">
                <span class="detail-card__label">Page purpose</span>
                <p>Get the next step right before you widen the problem.</p>
            </div>
            <div class="detail-card">
                <span class="detail-card__label">Next 24 hours</span>
                <p>${page.next24Hours().get(0)}</p>
            </div>
            <div class="detail-card">
                <span class="detail-card__label">What changes the answer</span>
                <p>${page.evidenceChecklist().get(0)}</p>
            </div>
        </div>
    </div>
</section>

@template.partials.sourceReview(page.sourceReview())
@template.partials.audienceCards("Use this page when", "Use the page that matches the question, your role, and the deadline you are trying to protect.", page.audienceCards())

<section id="first-steps" class="section section--soft">
    <div class="section__inner two-column">
        <div class="support-panel">
            <div class="section__eyebrow">Start here in this state</div>
            <ul class="guardrail-list">
                @for (var item : page.startHereChecklist())
                    <li>${item}</li>
                @endfor
            </ul>
        </div>
        <div class="support-panel">
            <div class="section__eyebrow">Why this route matters</div>
            <ul class="guardrail-list">
                @for (var item : page.whyItMatters())
                    <li>${item}</li>
                @endfor
            </ul>
        </div>
    </div>
</section>

<section class="section">
    <div class="section__inner two-column">
        <div class="support-panel">
            <div class="section__eyebrow">Do this in the next 24 hours</div>
            <ul class="guardrail-list">
                @for (var item : page.next24Hours())
                    <li>${item}</li>
                @endfor
            </ul>
        </div>
        <div class="support-panel">
            <div class="section__eyebrow">Questions to send today</div>
            <ul class="guardrail-list">
                @for (var item : page.todayQuestions())
                    <li>${item}</li>
                @endfor
            </ul>
        </div>
    </div>
</section>

<section class="section section--soft">
    <div class="section__inner">
        <div class="support-panel">
            <div class="section__eyebrow">Evidence that changes the answer</div>
            <ul class="guardrail-list">
                @for (var item : page.evidenceChecklist())
                    <li>${item}</li>
                @endfor
            </ul>
        </div>
    </div>
</section>

<section class="section">
    <div class="section__inner two-column">
        <div class="support-panel">
            <div class="section__eyebrow">What not to assume</div>
            <ul class="guardrail-list">
                @for (var item : page.whatNotToAssume())
                    <li>${item}</li>
                @endfor
            </ul>
        </div>
        <div class="support-panel">
            <div class="section__eyebrow">Cost and timeline direction</div>
            <ul class="guardrail-list">
                @for (var item : page.costAndTimelineNotes())
                    <li>${item}</li>
                @endfor
            </ul>
        </div>
    </div>
</section>

<section class="section section--contrast">
    <div class="section__inner">
        <div class="section__header">
            <div>
                <div class="section__eyebrow section__eyebrow--inverse">Next likely routes</div>
                <h2>Open the next page only after this one answers the real question.</h2>
            </div>
        </div>
        <div class="route-grid route-grid--secondary">
            @for (var card : page.nextLinks())
                <a class="route-card route-card--secondary" href="${card.href()}">
                    <span class="route-card__badge">${card.badge()}</span>
                    <strong>${card.title()}</strong>
                    <p>${card.description()}</p>
                </a>
            @endfor
        </div>
    </div>
</section>

@template.partials.sourceStack(page.sourceStack())
@template.partials.leadForm(page.cta())
`)

@import owner.buriedoiltank.pages.PageModels.HomePageModel
@param HomePageModel page
@template.layout.page(meta = page.meta(), activeNav = "home", content = @`
<section class="hero hero--home">
    <div class="hero__inner hero__inner--home">
        <div class="hero__copy">
            <div class="hero__status">Start here</div>
            <div class="section__eyebrow">For buyers, sellers, and agents trying to keep a deal on track</div>
            <h1>
                <span class="hero__title-line">Figure out the</span>
                <span class="hero__title-line"><span class="hero__accent">next step</span></span>
                <span class="hero__title-line hero__title-line--lead">before closing</span>
            </h1>
            <p class="hero__lede">If a buried heating-oil tank might be on the property, start by figuring out whether you need records, a tank sweep, or a confirmed-tank path. This site is built to help you make that call quickly.</p>
            <div class="hero__actions">
                <a class="button" href="#start-here">Choose your situation</a>
                <a class="button button--secondary" href="#states">Browse state pages</a>
            </div>
        </div>
        <div class="hero__aside hero__aside--home">
            <div class="hero__media hero__media--home">
                <img class="hero__image" src="https://images.unsplash.com/photo-1618179131275-e90e3dfde775?auto=format&fit=crop&w=1400&q=80" alt="Historic residential house exterior in a dark editorial treatment" width="1400" height="1400" loading="eager" fetchpriority="high" decoding="async">
                <div class="hero__stamp hero__stamp--dark">
                    <strong>Start with the smallest useful question</strong>
                    <span>Do you need records, a sweep, or a confirmed-tank decision? Answer that first.</span>
                </div>
            </div>
            <div class="hero__docket">
                <div class="docket-row">
                    <span class="docket-row__label">What you get</span>
                    <p>A fast path to the next action instead of a long oil-tank explainer.</p>
                </div>
                <div class="docket-row">
                    <span class="docket-row__label">Who this helps</span>
                    <p>Buyers, sellers, owners, agents, and attorneys trying to keep a live deal on track.</p>
                </div>
                <div class="docket-row">
                    <span class="docket-row__label">What to avoid</span>
                    <p>Jumping into quotes or cleanup talk before you know which path this property is actually on.</p>
                </div>
            </div>
        </div>
    </div>
</section>

<section id="start-here" class="section section--contrast">
    <div class="section__inner">
        <div class="section__header section__header--spaced">
            <div>
                <div class="section__eyebrow section__eyebrow--inverse">Start here</div>
                <h2>Choose the situation that matches what you know right now.</h2>
            </div>
        </div>
        <div class="route-grid">
            @for (var card : page.scenarioCards())
                <a class="route-card" href="${card.href()}">
                    <span class="route-card__badge">${card.badge()}</span>
                    <strong>${card.title()}</strong>
                    <p>${card.description()}</p>
                </a>
            @endfor
        </div>
        <div class="section__header section__header--spaced">
            <div>
                <div class="section__eyebrow section__eyebrow--inverse">Then go deeper</div>
                <h3>Use these once you already know the lane you are in.</h3>
            </div>
        </div>
        <div class="route-grid route-grid--secondary">
            @for (var card : page.decisionPathCards())
                <a class="route-card route-card--secondary" href="${card.href()}">
                    <span class="route-card__badge">${card.badge()}</span>
                    <strong>${card.title()}</strong>
                    <p>${card.description()}</p>
                </a>
            @endfor
        </div>
    </div>
</section>

<section id="states" class="section section--soft">
    <div class="section__inner">
        <div class="section__header">
            <div>
                <div class="section__eyebrow">Current state coverage</div>
                <h2>Start with the state that controls the permits, paperwork, and next call.</h2>
            </div>
            <a class="section__action" href="/states/">View all state pages</a>
        </div>
        <div class="card-grid">
            @for (var state : page.states())
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

<section class="section">
    <div class="section__inner feature-split">
        <div class="feature-copy">
                <div class="section__eyebrow">If the deal clock is running</div>
                <h2>Do these three things before you talk price, removal, or cleanup.</h2>
            <ol class="number-list">
                @for (var i = 0; i < page.takeaways().size(); i++)
                    <li>
                        <span class="number-list__index">${java.lang.String.format("%02d", i + 1)}</span>
                        <p>${page.takeaways().get(i)}</p>
                    </li>
                @endfor
            </ol>
        </div>
        <div class="file-panel">
            <div class="file-panel__header">
                <div class="section__eyebrow">Start with these requests today</div>
                <h3>Ask for the documents that change the answer.</h3>
                <p>Most buried-tank fights get expensive because the paperwork request happens too late.</p>
            </div>
            <div class="file-panel__list">
                <div class="file-item">
                    <span class="file-item__label">Ask now</span>
                    <strong>Seller disclosure and prior oil-heat history</strong>
                    <p>Start with what the seller already said, omitted, or cannot support in writing.</p>
                </div>
                <div class="file-item">
                    <span class="file-item__label">Pull next</span>
                    <strong>Permit trail, closure proof, or conversion paperwork</strong>
                    <p>Missing paper is a routing clue. It is not proof that a tank never existed.</p>
                </div>
                <div class="file-item">
                    <span class="file-item__label">Order when needed</span>
                    <strong>Sweep or locate only after the file points that way</strong>
                    <p>Use field work to narrow the question, not to skip the document request.</p>
                </div>
            </div>
            <div class="file-panel__boundary">
                <strong>Do not widen yet</strong>
                <ul class="mini-list">
                    <li>Removal quotes before a tank is confirmed.</li>
                    <li>Cleanup narratives before release facts exist.</li>
                    <li>Price talk before the record stack is clear.</li>
                </ul>
            </div>
        </div>
    </div>
</section>

<section class="section">
    <div class="section__inner case-layout">
        <div class="case-layout__copy">
            <div class="section__eyebrow">Keep these cases separate</div>
            <h2>Keep these cases separate before anyone prices the wrong problem.</h2>
            <p>Separate sale-side paperwork from confirmed tank work and from actual leak concerns. Mixing them together is how a manageable issue turns into a bigger one.</p>
        </div>
        <div class="case-grid">
            @for (var i = 0; i < page.caseSeparationRules().size(); i++)
                <div class="case-note">
                    <span class="case-note__index">${java.lang.String.format("%02d", i + 1)}</span>
                    <p>${page.caseSeparationRules().get(i)}</p>
                </div>
            @endfor
        </div>
    </div>
</section>

<section id="guides" class="section">
    <div class="section__inner">
        <div class="section__header">
            <div>
                <div class="section__eyebrow">Cross-state guides</div>
                <h2>Use a national guide when you still need the first smart move before the state page gets specific.</h2>
            </div>
            <a class="section__action" href="/guides/">View all guides</a>
        </div>
        <div class="card-grid">
            @for (var guide : page.guideCards())
                <a class="card" href="${guide.href()}">
                    <strong>${guide.title()}</strong>
                    <p>${guide.description()}</p>
                    <span class="badge">${guide.badge()}</span>
                </a>
            @endfor
        </div>
    </div>
</section>

<section class="section section--proof">
    <div class="section__inner trust-band">
        <div class="trust-band__copy">
            <div class="section__eyebrow section__eyebrow--inverse">Trust boundary</div>
            <h2>Official guidance first. Transaction support after the record is clear.</h2>
            <p>Start with permits, disclosure records, and state guidance. Bring in contractor, removal, or cleanup decisions only after the facts are clearer.</p>
            <div class="hero__actions trust-band__actions">
                <a class="button button--secondary button--inverse" href="/methodology/">View methodology</a>
                <a class="button" href="/states/">Find your state</a>
            </div>
        </div>
        <div class="trust-band__grid">
            @for (var item : page.guardrails())
                <div class="trust-note">
                    <span class="trust-note__label">${java.lang.String.format("%02d", page.guardrails().indexOf(item) + 1)}</span>
                    <p>${item}</p>
                </div>
            @endfor
        </div>
    </div>
</section>

<section class="section section--tight">
    <div class="section__inner">
        @template.partials.editorialTeam()
    </div>
</section>
`)

@import owner.buriedoiltank.pages.PageModels.HomePageModel
@param HomePageModel page
@template.layout.page(meta = page.meta(), activeNav = "home", content = @`
<section class="hero hero--home">
    <div class="hero__inner hero__inner--home">
        <div class="hero__copy">
            <div class="hero__status">Live closing guidance</div>
            <div class="section__eyebrow">Buried oil tank disclosure, records, and closing steps</div>
            <h1>
                <span class="hero__title-line">Buried Oil Tank</span>
                <span class="hero__title-line"><span class="hero__accent">Before Closing</span></span>
                <span class="hero__title-line">What Buyers and Sellers</span>
                <span class="hero__title-line">Should Do Next</span>
            </h1>
            <p class="hero__lede">Use this when a buried oil tank question changes disclosure language, records requests, sweep timing, financing pressure, or the next step before closing.</p>
            <div class="hero__proofline">
                <span>Official state source review</span>
                <span>Current coverage: NJ, NY, CT, ME only</span>
            </div>
            <div class="hero__actions">
                <a class="button" href="#states">Start with your state</a>
                <a class="button button--secondary" href="/methodology/">See how the review works</a>
            </div>
        </div>
        <div class="hero__aside hero__aside--home">
            <div class="hero__media hero__media--home">
                <img class="hero__image" src="https://images.unsplash.com/photo-1618179131275-e90e3dfde775?auto=format&fit=crop&w=1400&q=80" alt="Historic residential house exterior in a dark editorial treatment" width="1400" height="1400" loading="eager" fetchpriority="high" decoding="async">
                <div class="hero__stamp hero__stamp--dark">
                    <strong>Disclosure and evidence first</strong>
                    <span>Do not widen into removal or cleanup until records, sweep results, and the state process align.</span>
                </div>
            </div>
            <div class="hero__docket">
                <div class="docket-row">
                    <span class="docket-row__label">Workflow</span>
                    <p>Verify first. Route second. Escalate only when the evidence changes.</p>
                </div>
                <div class="docket-row">
                    <span class="docket-row__label">Who carries this</span>
                    <p>Buyer, seller, owner, agent, or attorney carrying a live deal clock.</p>
                </div>
                <div class="docket-row">
                    <span class="docket-row__label">Boundary</span>
                    <p>Records, sweep, confirmed tank, and leak concern stay in separate routes.</p>
                </div>
            </div>
        </div>
    </div>
</section>

<section class="section section--proof section--tight">
    <div class="section__inner proof-strip">
        <div class="proof-chip">
            <span class="proof-chip__label">Current coverage</span>
            <p>${page.states().size()} state pages and ${page.guideCards().size()} cross-state guides are live.</p>
        </div>
        <div class="proof-chip">
            <span class="proof-chip__label">Primary sources tracked</span>
            <p>${page.trackedSourceCount()} official source references are mapped across public state and guide surfaces.</p>
        </div>
        <div class="proof-chip">
            <span class="proof-chip__label">Page standard</span>
            <p>Each state and route page shows a verified date, next review date, and source stack before the memo request opens.</p>
        </div>
    </div>
</section>

<section class="section section--tight">
    <div class="section__inner">
        @template.partials.editorialTeam()
    </div>
</section>

<section id="states" class="section section--soft">
    <div class="section__inner">
        <div class="section__header">
            <div>
                <div class="section__eyebrow">Current state coverage</div>
                <h2>Start with the state that controls the next document request.</h2>
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

@template.partials.audienceCards("Who this helps first", "Use the state page or route guide before the file widens into assumption.", page.audienceCards())

<section class="section">
    <div class="section__inner feature-split">
        <div class="feature-copy">
            <div class="section__eyebrow">Use this when the deal is live</div>
            <h2>Use this when disclosure, financing, or closing timing can slip.</h2>
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
                <div class="section__eyebrow">Live file triage</div>
                <h3>What the next request should pull into the file.</h3>
                <p>Keep the first pass narrow enough to separate disclosure, records, sweep timing, and true escalation.</p>
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

<section id="routes" class="section section--contrast">
    <div class="section__inner">
        <div class="section__header section__header--spaced">
            <div>
                <div class="section__eyebrow section__eyebrow--inverse">Route families</div>
                <h2>Compare the route that matches the evidence on the file.</h2>
            </div>
            <a class="section__action section__action--inverse" href="/routes/">View all route guides</a>
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
                <div class="section__eyebrow section__eyebrow--inverse">Support routes</div>
                <h3>Use these only after the file is specific enough to justify them.</h3>
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

<section class="section">
    <div class="section__inner case-layout">
        <div class="case-layout__copy">
            <div class="section__eyebrow">Keep these cases separate</div>
            <h2>Keep these cases separate before anyone prices the wrong problem.</h2>
            <p>Operational clarity comes from separating transaction friction from true cleanup escalation. Mix them together and the file gets slower, more expensive, and less honest.</p>
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
                <h2>Use the national guide when the state page is not the first honest surface yet.</h2>
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
            <p>This surface starts with state language, disclosure records, and evidence. Contractor, cleanup, or pricing decisions come after the route is clear.</p>
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
`)

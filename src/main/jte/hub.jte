@import owner.buriedoiltank.pages.PageModels.HubPageModel
@param HubPageModel page
@template.layout.page(meta = page.meta(), activeNav = page.activeNav(), content = @`
<section class="hero hero--detail">
    <div class="hero__inner hero__inner--detail">
        <div class="hero__copy hero__copy--detail prose">
            @template.partials.breadcrumbs(page.breadcrumbs())
            <div class="section__eyebrow">${page.eyebrow()}</div>
            <h1>${page.heading()}</h1>
            <p class="hero__lede">${page.intro()}</p>
        </div>
        <div class="hero__aside">
            <div class="hero__media hero__media--detail">
                @if ("states".equals(page.activeNav()))
                    <img class="hero__image" src="https://images.unsplash.com/photo-1768119997334-bc76e6f60e7d?auto=format&fit=crop&w=1400&q=80" alt="Rows of filing cabinets used to symbolize state records and archives" width="1400" height="1167" loading="eager" fetchpriority="high" decoding="async">
                @else
                    <img class="hero__image" src="https://images.unsplash.com/photo-1457298483369-0a95d2b17fcd?auto=format&fit=crop&w=1400&q=80" alt="Close-up of a fountain pen nib on paper" width="1400" height="1167" loading="eager" fetchpriority="high" decoding="async">
                @endif
            </div>
        </div>
    </div>
</section>

@if ("routes".equals(page.activeNav()))
    <section class="section section--tight">
        <div class="section__inner route-summary">
            <div class="route-summary__card">
                <span class="proof-chip__label">Public guides live</span>
                <strong>${page.cards().size()} route guides</strong>
                <p>Buyer-seller, sweep, records, disposition, leak, and cost each have a dedicated public path.</p>
            </div>
            <div class="route-summary__card">
                <span class="proof-chip__label">Working rule</span>
                <strong>3 core routes. 3 support routes.</strong>
                <p>Stay in core routes until confirmation or release facts make the file narrower.</p>
            </div>
            <div class="route-summary__card">
                <span class="proof-chip__label">State handoff</span>
                <strong>Move to a state page when agency language controls the next move.</strong>
                <p>Once closure, reporting, or cleanup process turns state-specific, the guide should stop being the lead surface.</p>
            </div>
        </div>
    </section>
@endif

<section class="section section--soft">
    <div class="section__inner">
        <div class="section__header">
            <div>
                <div class="section__eyebrow">${page.sectionEyebrow()}</div>
                <h2>${page.sectionHeading()}</h2>
            </div>
        </div>
        <div class="card-grid">
            @for (var card : page.cards())
                <a class="card" href="${card.href()}">
                    <strong>${card.title()}</strong>
                    <p>${card.description()}</p>
                    <span class="badge">${card.badge()}</span>
                </a>
            @endfor
        </div>
    </div>
</section>

<section class="section">
    <div class="section__inner">
        <div class="support-panel">
            <div class="section__eyebrow">${page.supportEyebrow()}</div>
            <ul class="guardrail-list">
                @for (var bullet : page.supportBullets())
                    <li>${bullet}</li>
                @endfor
            </ul>
        </div>
    </div>
</section>

<section class="section section--tight">
    <div class="section__inner">
        @template.partials.editorialTeam()
    </div>
</section>
`)

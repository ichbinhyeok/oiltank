@import owner.buriedoiltank.pages.PageModels.StaticPageModel
@param StaticPageModel page
@template.layout.page(meta = page.meta(), activeNav = "static", content = @`
<section class="hero hero--detail">
    <div class="hero__inner hero__inner--detail">
        <div class="hero__copy hero__copy--detail prose">
            @template.partials.breadcrumbs(page.breadcrumbs())
            <div class="section__eyebrow">Trust</div>
            <h1>${page.heading()}</h1>
            <p class="hero__lede">${page.intro()}</p>
        </div>
        <div class="hero__aside">
            <div class="hero__media hero__media--detail">
                <img class="hero__image" src="https://images.unsplash.com/photo-1450101499163-c8848c66ca85?auto=format&fit=crop&w=1400&q=80" alt="Property notes and paperwork on a desk" width="1400" height="933" loading="eager" fetchpriority="high" decoding="async">
            </div>
        </div>
    </div>
</section>

<section class="section section--soft">
    <div class="section__inner">
        <div class="support-panel">
            <div class="section__eyebrow">What matters here</div>
            <ul class="guardrail-list">
                @for (var bullet : page.bullets())
                    <li>${bullet}</li>
                @endfor
            </ul>
            @if (page.contactEmail() != null)
                <div class="contact-card">
                    <span class="contact-card__label">Editorial contact</span>
                    <a class="contact-card__email" href="mailto:${page.contactEmail()}">${page.contactEmail()}</a>
                    <p>Use this address for source corrections, stale links, launch questions, or factual updates.</p>
                </div>
            @endif
        </div>
    </div>
</section>
`)

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
                <img class="hero__image" src="https://images.unsplash.com/photo-1767972159674-460283907c21?auto=format&fit=crop&w=1400&q=80" alt="Gavel on a dark legal surface" width="1400" height="1167" loading="eager" fetchpriority="high" decoding="async">
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
        </div>
    </div>
</section>
`)

@import owner.buriedoiltank.pages.PageModels.StaticPageModel
@param StaticPageModel page
@template.layout.page(meta = page.meta(), activeNav = "static", content = @`
<header class="v2-service-header"><div class="v2-shell">
    @template.partials.breadcrumbs(page.breadcrumbs())
    <p class="v2-eyebrow">Oil Tank Route / Service information</p>
    <h1>${page.heading()}</h1><p>${page.intro()}</p>
</div></header>
<section class="v2-section v2-shell"><div class="v2-service-information">
    <ul class="v2-information-list">
    @for (var bullet : page.bullets())
        <li>${bullet}</li>
    @endfor
    </ul>
    @if (page.contactEmail() != null)
    <section class="v2-contact-section"><p class="v2-eyebrow">Direct contact</p><h2>Questions before you share a property?</h2>
    <p>Ask about research scope, a time-sensitive decision, a source correction, or your existing request. Include your receipt number if you already submitted a case.</p>
    <a class="v2-contact-email" href="mailto:${page.contactEmail()}">${page.contactEmail()}</a>
    <p>For a new property, use the private intake so the address, question, and documents stay together. Do not post property documents in a public comment.</p>
    <a class="v2-button" href="/#start-check">Start a property record check <span>↗</span></a>
    </section>
    @endif
    <nav class="v2-trust-links" aria-label="Service information"><a href="/how-it-works/">How the research works ↗</a><a href="/sample-brief/">What the brief contains ↗</a><a href="/research-areas/">Find your jurisdiction ↗</a></nav>
</div></section>
`)

@import owner.buriedoiltank.pages.PageModels.HomePageModel
@import owner.buriedoiltank.data.ResearchCatalog
@param HomePageModel page
@template.layout.page(meta = page.meta(), activeNav = "home", content = @`
<section class="v2-hero" data-page-id="service:home">
    <img class="v2-hero__image" src="/images/property-editorial.jpg" width="1672" height="941" alt="Illustrative northeastern home framed by mature trees" fetchpriority="high">
    <div class="v2-shell v2-hero__content">
        <p class="v2-eyebrow">Oil Tank Route / Property record research</p>
        <h1>Oil tank records.<br>Researched for you.</h1>
        <p>We research missing permits, removal paperwork, and conflicting records—then show what the evidence supports and what remains unresolved.</p>
        <a class="v2-button v2-button--white" href="#start-check" data-service-cta data-cta-location="hero">Start a property record check <span aria-hidden="true">↗</span></a>
        <div class="v2-hero__meta"><span>New Jersey &amp; New York</span><span>Free founder-led beta</span></div>
    </div>
    <div class="v2-hero__caption">Illustrative image · not a customer property</div>
</section>
<section class="v2-intro v2-shell">
    <p class="v2-eyebrow">A research service. A human on your side.</p>
    <h2>You don’t need another<br>list of places to look.</h2>
    <div><p>We match the property, work through the relevant sources, and compare the documents. You receive a source-backed brief—not just links to government websites.</p><a class="v2-text-link" href="/how-it-works/">See what we handle <span>↗</span></a></div>
</section>
@template.partials.researchStandard()
<section class="v2-section v2-soft"><div class="v2-shell">
    <div class="v2-section-head"><div><p class="v2-eyebrow">Start with your question</p><h2>What’s missing<br>from the picture?</h2></div><p>A missing file, a seller’s claim, or documents that don’t agree. Each calls for a different next step.</p></div>
    <div class="v2-intents">
        <a href="/record-help/missing-removal-records/"><span class="v2-index">01 / Locate</span><h3>“I can’t find the<br>removal records.”</h3><p>Trace the permit, inspection, and completion file.</p><span class="v2-circle-arrow" aria-hidden="true">↗</span></a>
        <a href="/record-help/conflicting-tank-documents/"><span class="v2-index">02 / Understand</span><h3>“I have paperwork.<br>Not an answer.”</h3><p>Separate what the documents prove from what they leave open.</p><span class="v2-circle-arrow" aria-hidden="true">↗</span></a>
        <a href="/record-help/seller-says-tank-removed/"><span class="v2-index">03 / Verify</span><h3>“The seller says<br>it was removed.”</h3><p>Match the claim to the property’s actual record trail.</p><span class="v2-circle-arrow" aria-hidden="true">↗</span></a>
    </div>
    <a class="v2-urgent" href="/guides/leaking-heating-oil-tank-what-to-do/">Oil, strong odor, or an active leak? Skip research and use the safety route. ↗</a>
</div></section>
<section class="v2-section v2-shell v2-delivery">
    <div><p class="v2-eyebrow">The result, made useful</p><h2>A clear brief.<br>Not a document dump.</h2><p>Every finding has a source. Every open question has a next action. You see what’s known, what’s pending, and where professional input belongs.</p><a class="v2-button" href="/sample-brief/">Explore the sample brief <span>↗</span></a><small>Illustrative format. Not a completed customer case.</small></div>
    <div class="v2-brief-preview"><header><span class="v2-mini-brand">Oil Tank Route</span><span>Illustrative brief</span></header><h3>Property history,<br>with the gaps visible.</h3><div class="v2-preview-row"><span class="v2-status-dot"></span><div><strong>Documented</strong><p>Permit and inspection references, matched to the property.</p></div></div><div class="v2-preview-row"><span class="v2-status-dot v2-status-dot--open"></span><div><strong>Still unresolved</strong><p>Missing closeout evidence and the limits of the available file.</p></div></div><div class="v2-preview-next"><span>Recommended next action</span><strong>Request the underlying completion record.</strong><span aria-hidden="true">↗</span></div></div>
</section>
<section class="v2-section v2-green"><div class="v2-shell v2-area-feature"><div><p class="v2-eyebrow">Local knowledge matters</p><h2>Same question.<br>Different record holders.</h2><p>From a Nassau verification letter to a Montclair property file, the right route starts with the jurisdiction.</p><a class="v2-button v2-button--white" href="/research-areas/">Find your research area <span>↗</span></a></div><div class="v2-region-list"><a href="/research-areas/#new-jersey"><span>New Jersey</span><small>${ResearchCatalog.areaCount("new-jersey")} municipal routes · including Essex, Bergen, Union &amp; Passaic</small><b aria-hidden="true">↗</b></a><a href="/research-areas/#new-york"><span>New York</span><small>County &amp; city routes, plus four Long Island towns</small><b aria-hidden="true">↗</b></a><p>${ResearchCatalog.AREAS.size()} researched starting points. Other NJ/NY locations reviewed at intake.</p></div></div></section>
@template.partials.intakeSection(pageId = "service:home", pagePath = "/")
`)

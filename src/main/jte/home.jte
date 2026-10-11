@import owner.buriedoiltank.pages.PageModels.HomePageModel
@import owner.buriedoiltank.data.ResearchCatalog
@param HomePageModel page
@template.layout.page(meta = page.meta(), activeNav = "home", content = @`
<section class="v2-shell rf-home" data-page-id="service:home">
    <div class="rf-heading"><p class="v2-eyebrow">Oil tank paperwork · Before a purchase or sale</p>
        <h1>Oil tank paperwork.<br>Know what’s missing.</h1>
        <p>The seller says it was removed. The tank was filled in place. The permit has no final record. Turn the unresolved question into a document checklist and the right official search.</p>
        <div class="rf-start"><a class="v2-button" href="/tank-document-check/">Check your document gaps <span aria-hidden="true">↗</span></a><p class="rf-caption">Free · No email required</p><a href="/find-records/">I know the record I need ↗</a></div>
        <p class="rf-caption">${ResearchCatalog.ALL.size()} routes across ${ResearchCatalog.states().size()} states · Coverage varies by agency and document.<br>Guided official sources, plus direct Seattle residential dataset lookup.</p>
    </div>
    <figure><img src="/images/property-editorial.jpg" width="1672" height="941" alt="Illustrative home framed by mature trees"><figcaption class="rf-caption">The house is only part of the story.<br>Illustrative image, not a customer property.</figcaption></figure>
</section>
<section class="v2-shell dc-home-entrances"><article><p class="v2-eyebrow">Start with the question</p><h2>Have paperwork.<br>Still have questions?</h2><p>Select the document categories you hold. Get a self-reported research checklist with state-specific distinctions—not a safety verdict.</p><a href="/tank-document-check/">Check documents ↗</a></article><article><p class="v2-eyebrow">Start with the record</p><h2>Find the right<br>official record route.</h2><p>Already know you need a permit, verification letter or environmental file? Choose the actual record holder.</p><a href="/find-records/">Find records by place and document ↗</a></article></section>
<section class="v2-shell rf-directory"><p class="v2-eyebrow">Start with the document</p><h2>Different records. Different answers.</h2>
@for (var entry : ResearchCatalog.TASKS.stream().filter(e -> java.util.List.of("njdep-nfa-letter", "nysdec-spill-documents", "oregon-clean-decommissioning", "seattle-residential-tank-records", "massdep-oil-release-files", "nysdec-pbs-registration-records").contains(e.slug())).toList())
<a class="rf-route" href="${entry.path()}"><span>${entry.locality()}</span><div><h3>${entry.title()}</h3><p>${entry.summary()}</p></div><b aria-hidden="true">↗</b></a>
@endfor
<a class="rf-route" href="/research-areas/nassau-ny/"><span>Nassau County · New York</span><div><h3>Find a small-tank removal verification letter</h3><p>Look up a previous removal, not a new removal appointment.</p></div><b aria-hidden="true">↗</b></a>
<a class="v2-text-link" href="/find-records/">Browse all ${ResearchCatalog.ALL.size()} record routes ↗</a>
</section>
<section class="v2-section v2-soft"><div class="v2-shell">
    <div class="v2-section-head"><div><p class="v2-eyebrow">Start with your question</p><h2>What’s missing<br>from the picture?</h2></div><p>A missing file, a seller’s claim, or documents that don’t agree. Each calls for a different next step.</p></div>
    <div class="v2-intents">
        <a href="/record-help/missing-removal-records/"><span class="v2-index">01 / Locate</span><h3>“I can’t find the<br>removal records.”</h3><p>Trace the permit, inspection, and completion file.</p><span class="v2-circle-arrow" aria-hidden="true">↗</span></a>
        <a href="/record-help/conflicting-tank-documents/"><span class="v2-index">02 / Understand</span><h3>“I have paperwork.<br>Not an answer.”</h3><p>Separate what the documents prove from what they leave open.</p><span class="v2-circle-arrow" aria-hidden="true">↗</span></a>
        <a href="/record-help/seller-says-tank-removed/"><span class="v2-index">03 / Verify</span><h3>“The seller says<br>it was removed.”</h3><p>Match the claim to the property’s actual record trail.</p><span class="v2-circle-arrow" aria-hidden="true">↗</span></a>
    </div>
    <div class="dc-home-entrances"><article><h3>“It was filled with sand.”</h3><p>Closure in place is not removal. Locate the work and sampling file.</p><a href="/record-help/oil-tank-filled-with-sand/">Check a filled-in-place tank claim ↗</a></article><article><h3>“The house uses gas now.”</h3><p>Heating conversion does not establish what happened to the old tank.</p><a href="/record-help/oil-to-gas-tank-records/">Trace the former tank separately ↗</a></article></div>
    <a class="v2-urgent" href="/guides/leaking-heating-oil-tank-what-to-do/">Oil, strong odor, or an active leak? Skip research and use the safety route. ↗</a>
</div></section>
<section class="v2-section v2-shell v2-delivery">
    <div><p class="v2-eyebrow">Optional NJ/NY research assistance</p><h2>Want a human<br>to work through it?</h2><p>If the record trail is still unresolved, ask us to review your NJ or NY property question. Our founder-led beta checks coverage before accepting research. A source-backed brief separates findings from gaps.</p><a class="v2-button" href="#start-check">Ask for research help <span>↗</span></a><small>Free beta · NJ/NY only · No automatic case acceptance</small><a href="/sample-brief/">See an illustrative brief ↗</a></div>
    <div class="v2-brief-preview"><header><span class="v2-mini-brand">Oil Tank Route</span><span>Illustrative brief</span></header><h3>Property history,<br>with the gaps visible.</h3><div class="v2-preview-row"><span class="v2-status-dot"></span><div><strong>Documented</strong><p>Permit and inspection references, matched to the property.</p></div></div><div class="v2-preview-row"><span class="v2-status-dot v2-status-dot--open"></span><div><strong>Still unresolved</strong><p>Missing closeout evidence and the limits of the available file.</p></div></div><div class="v2-preview-next"><span>Recommended next action</span><strong>Request the underlying completion record.</strong><span aria-hidden="true">↗</span></div></div>
</section>
<section class="v2-section v2-green"><div class="v2-shell v2-area-feature"><div><p class="v2-eyebrow">Local knowledge matters</p><h2>Same question.<br>Different record holders.</h2><p>From a Nassau verification letter to a Montclair property file, the right route starts with the jurisdiction.</p><a class="v2-button v2-button--white" href="/research-areas/">Find your research area <span>↗</span></a></div><div class="v2-region-list"><a href="/research-areas/#new-jersey"><span>New Jersey</span><small>${ResearchCatalog.areaCount("new-jersey")} municipal routes · including Essex, Bergen, Union &amp; Passaic</small><b aria-hidden="true">↗</b></a><a href="/research-areas/#new-york"><span>New York</span><small>County, city and town-specific record routes</small><b aria-hidden="true">↗</b></a><p>${ResearchCatalog.AREAS.size()} researched starting points. Other NJ/NY locations reviewed at intake.</p></div></div></section>
@template.partials.intakeSection(pageId = "service:home", pagePath = "/")
`)

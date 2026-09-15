@param String pageId
@param String pagePath
@param String defaultState = ""
@param String defaultLocality = ""
@param String defaultQuestion = ""
<section class="v2-section v2-intake" id="start-check" data-service-cta-view><div class="v2-shell v2-intake__grid">
    <div class="v2-intake__copy"><p class="v2-eyebrow">Start a property record check</p><h2>Start with<br>what you know.</h2><p>Send the property and your question. First, we confirm whether we can help and which records to pursue. You do not need an existing file to get started.</p><dl><div><dt>Cost</dt><dd>Free during the founder-led beta. Any agency fees are identified separately before a paid request.</dd></div><div><dt>First response</dt><dd>Initial route review target: 2 business days—not a final report deadline.</dd></div><div><dt>Your records</dt><dd>Private intake. No public case pages.</dd></div></dl><small>We confirm the scope and any request authorization before agency outreach. Agency timing varies; a record may be unavailable. Research is not a safety certification or legal opinion.</small></div>
    @template.partials.researchForm(pageId = pageId, pagePath = pagePath, defaultState = defaultState, defaultLocality = defaultLocality, defaultQuestion = defaultQuestion)
</div></section>

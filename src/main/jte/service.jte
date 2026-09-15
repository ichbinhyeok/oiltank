@import owner.buriedoiltank.pages.PageModels.ServicePageModel
@import owner.buriedoiltank.data.ResearchCatalog
@param ServicePageModel page
@template.layout.page(meta = page.meta(), activeNav = page.activeNav(), content = @`
<header class="v2-service-header" data-page-id="${page.pageId()}"><div class="v2-shell">
    <p class="v2-eyebrow">${page.eyebrow()}</p>
    @if (page.isKind("how"))
    <h1>From an open question<br>to a clear next step.</h1><p>One founder-led research process, from the property identifiers to the records that matter.</p>
    @elseif (page.isKind("research"))
    <h1>We follow the records.<br>You see the whole trail.</h1><p>Property-specific research across permits, public databases, agency files, and the documents already in your hands.</p>
    @else
    <h1>A useful answer has<br>its evidence attached.</h1><p>Explore the format of a property research brief: findings, their limits, and a practical next action.</p>
    @endif
    <a class="v2-button" href="${page.isKind("brief") ? "#sample-report" : "#start-check"}" data-service-cta data-cta-location="service-page-hero">${page.isKind("brief") ? "Explore the sample" : "Start a property record check"} <span>↗</span></a>
</div></header>
@if (page.isKind("how"))
<section class="v2-section v2-shell"><p class="v2-eyebrow">How it works</p><h2>Four steps.<br>No unexplained handoffs.</h2><div class="v2-process">
<article><span class="v2-index">01 / Tell us</span><h3>Start with the property.</h3><p>Share the address, the question, and any records you have. We identify the parcel and confirm the research scope.</p></article>
<article><span class="v2-index">02 / Research</span><h3>Follow the evidence.</h3><p>We cross-check property, permit, and environmental sources. If a file is missing, we identify its custodian and request route.</p></article>
<article><span class="v2-index">03 / Reconcile</span><h3>Explain what matches.<br>Keep conflicts visible.</h3><p>Documents are matched by property, date, and reference. We do not force conflicting records into a single conclusion.</p></article>
<article><span class="v2-index">04 / Deliver</span><h3>Get a clear next step.</h3><p>Your brief separates documented facts, pending requests, open gaps, and the questions that need a professional.</p></article>
</div></section>
<section class="v2-section v2-soft"><div class="v2-shell v2-scope-grid"><article><h3>A real person reviews the route.</h3><p>The free founder-led beta targets an initial route review within 2 business days. Agency response times may extend the final answer.</p></article><article><h3>Research has clear boundaries.</h3><p>We do not certify tank absence, physical safety, environmental condition, or legal compliance. Urgent leak concerns belong on the safety route.</p></article></div></section>
@endif
@if (page.isKind("research"))
<section class="v2-section v2-shell"><div class="v2-section-head"><div><p class="v2-eyebrow">Different sources. Different answers.</p><h2>No single portal<br>is the whole file.</h2></div><a class="v2-text-link" href="/research-areas/">Find your local route ↗</a></div><div class="v2-scope-grid">
<article><h3>Property &amp; parcel</h3><p>Resolve the exact property, jurisdiction, and identifiers before matching any record.</p></article>
<article><h3>Permits &amp; inspections</h3><p>Look for installation, removal, abandonment, conversion, and completion documents.</p></article>
<article><h3>Environmental records</h3><p>Follow incident and case identifiers without treating a blank search as proof of absence.</p></article>
<article><h3>Agency-held files</h3><p>Identify the custodian, channel, required identifiers, fees, and fallback route for missing records.</p></article>
<article><h3>Your existing documents</h3><p>Compare what you already have with the record trail. Separate claims from recorded actions.</p></article>
<article><h3>The transaction brief</h3><p>Bring the facts, gaps, pending requests, and next actions together in plain English.</p></article>
</div><a class="v2-text-link" href="/research-examples/">See two source-backed walkthroughs ↗</a></section>
@endif
@if (page.isKind("brief"))
<section class="v2-report-wrap" id="sample-report"><div class="v2-shell"><div class="v2-report">
<aside class="v2-report-nav"><a href="#report-summary">Overview</a><a href="#report-evidence">Evidence review</a><a href="#report-gaps">Open questions</a><a href="#report-actions">Next actions</a><p>Illustrative composite<br>Not a customer case<br>Not an agency record</p></aside>
<article class="v2-report-main"><header><strong class="v2-mini-brand">Oil Tank Route.</strong><span>Sample / REP-NJ-001</span></header>
<p class="v2-report-notice">Fictional composite. All property details and findings below demonstrate the format only. No retrieved record or completed customer investigation is claimed.</p>
<section id="report-summary"><p class="v2-eyebrow">Property research brief / Montclair, NJ</p><h2>“The seller says the tank was removed. What supports that?”</h2><p>The illustrative file includes a tank-related municipal permit and final inspection. That supports administrative completion of the documented work, but does not establish removal method, soil condition, or present tank absence.</p></section>
<section id="report-evidence"><p class="v2-eyebrow">Evidence review</p><h3>What each document adds.</h3><dl class="v2-evidence-rows"><div><dt>Seller disclosure</dt><dd>States removal during a fuel conversion. A claim to test—not independent verification.</dd></div><div><dt>Municipal permit</dt><dd>Matched to the fictional parcel and marked finaled. Supports permitted closeout; underlying attachments still matter.</dd></div><div><dt>Inspection entry</dt><dd>Records a completed final inspection. Does not, by itself, say whether soil testing occurred.</dd></div><div><dt>Environmental search</dt><dd>No linked incident in the illustrative search. A limited result, not an environmental clearance.</dd></div></dl></section>
<section id="report-gaps"><p class="v2-eyebrow">Still open</p><h3>Keep these questions in the decision.</h3><ul><li>Was the tank removed or closed in place?</li><li>What was its condition during the work?</li><li>Were soil samples taken, and where are the results?</li><li>Do current conditions call for physical investigation?</li></ul></section>
<section id="report-actions"><p class="v2-eyebrow">Prioritized next actions</p><h3>The next useful move.</h3><ol><li><strong>Obtain the contractor closeout packet.</strong> Look for work method, disposal receipt, observations, and any soil results.</li><li><strong>Bring the evidence to the transaction team.</strong> Describe municipal closeout accurately, without calling it environmental clearance.</li><li><strong>Keep professional verification open.</strong> A records gap may need an inspector, environmental professional, or attorney—not another database search.</li></ol><a class="v2-text-link" href="/research-examples/">See actual public-source walkthroughs ↗</a></section>
<p class="v2-fine">Research is not a safety certification, title report, legal opinion, or environmental determination.</p>
</article></div></div></section>
@endif
<section class="v2-section v2-green"><div class="v2-shell v2-area-feature"><div><p class="v2-eyebrow">Start in the right place</p><h2>Your jurisdiction<br>changes the research.</h2><p>Explore the actual sources, identifiers, and request sequence for ${ResearchCatalog.AREAS.size()} NJ and NY areas.</p></div><div><a class="v2-button v2-button--white" href="/research-areas/">Explore research areas <span>↗</span></a><p><a href="/research-examples/">Or see our source walkthroughs ↗</a></p></div></div></section>
<section class="v2-section v2-shell"><div class="v2-service-questions"><p class="v2-eyebrow">Before you send a property</p><h2>What to expect.</h2>
<details open><summary>What do I receive, and when?</summary><p>The first response is a route review: whether we can help, the proposed scope, and missing information. The target is 2 business days. After research, the brief brings together matched records or references, findings, unresolved gaps, pending requests, and next actions. A final brief date depends on the scope and agency availability.</p></details>
<details><summary>What if no records can be found?</summary><p>An empty search is not proof that a tank never existed or was safely removed. The brief explains which sources were checked, what was unavailable, and the next request or professional question. We cannot guarantee that a removal or closure record exists.</p></details>
<details><summary>Will you contact agencies or charge me?</summary><p>The research service is free during the founder-led beta. Agency records may carry separate fees. We identify the request route and confirm scope, authorization, and any fee before a paid request; submitting this form does not place a paid order.</p></details>
<details><summary>Can this replace a tank sweep or meet my closing deadline?</summary><p>No. Records research does not determine present physical conditions or guarantee a transaction deadline. Include your deadline so the initial review can flag timing limits. You may still need an on-site professional or your transaction team.</p></details>
<details><summary>Is my property covered?</summary><p>We currently focus on New Jersey and New York. The <a href="/research-areas/">published research areas</a> are researched starting points, not identical databases. Other locations are reviewed for feasibility at intake; submission is not confirmation that we can take the case.</p></details>
</div></section>
@template.partials.intakeSection(pageId = page.pageId(), pagePath = page.pagePath())
`)

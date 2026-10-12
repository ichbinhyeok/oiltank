@import owner.buriedoiltank.data.ResearchCatalog
@import owner.buriedoiltank.data.TankDocumentCheck
@import owner.buriedoiltank.pages.PageModels.PageMeta
@param PageMeta meta
@template.layout.page(meta = meta, activeNav = "documents", content = @`
<header class="v2-shell rf-heading dc-heading" data-page-id="research:document-check"><p class="v2-eyebrow">Oil tank paperwork · Before a purchase or sale</p><h1>Check the documents.<br>Keep the gaps visible.</h1><p>Removal certificate missing? Tank filled in place? A permit without a final inspection? Start with the question—not a list of agencies.</p><div class="rf-start-actions"><a class="v2-button" href="#document-check">Check my document gaps ↓</a><a href="/find-records/">I know the record I need ↗</a></div><p class="rf-caption">Free · No email required · Self-reported checklist, not document verification</p></header>
<div class="v2-shell dc-workspace">@template.partials.tankDocumentCheck()</div>
<section class="v2-shell rf-directory"><p class="v2-eyebrow">Read the answer first</p><h2>Common paperwork problems.</h2>
@for (var entry : ResearchCatalog.PROBLEMS)
    <a class="rf-route" href="${entry.path()}"><span>${entry.locality()}</span><div><h3>${entry.title()}</h3><p>${TankDocumentCheck.situation(entry.slug()).answer()}</p></div><b aria-hidden="true">↗</b></a>
@endfor
<a class="rf-route" href="/guides/buried-oil-tank-home-sale/"><span>Buyer or seller</span><div><h3>Assemble a tank document file before a home sale</h3><p>Separate the recorded history, unresolved questions and professional decisions.</p></div><b aria-hidden="true">↗</b></a>
</section>
`)

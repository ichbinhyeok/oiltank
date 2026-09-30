@import owner.buriedoiltank.data.ResearchCatalog.Entry
@import owner.buriedoiltank.data.RecordEvidence
@param Entry entry
!{var guide = RecordEvidence.forEntry(entry);}
<section id="check-documents" class="rf-evidence">
    <p class="v2-eyebrow">After the search</p><h2>What should you actually come away with?</h2>
    <p>${guide.target()}</p>
    @for (var check : guide.checks())
    <article><h3>${check.document()}</h3><p>${check.compare()}</p><p><strong>Still missing?</strong> ${check.gap()}</p></article>
    @endfor
    <p class="rf-caption">Comparison checklist by Oil Tank Route, based on the official routes above. Not an agency certification, document authentication or a property-safety finding.</p>
</section>

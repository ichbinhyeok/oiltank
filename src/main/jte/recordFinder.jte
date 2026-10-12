@import owner.buriedoiltank.data.ResearchCatalog
@import owner.buriedoiltank.pages.PageModels.PageMeta
@param PageMeta meta
@template.layout.page(meta = meta, activeNav = "finder", content = @`
<header class="v2-shell rf-heading"><p class="v2-eyebrow">The record finder</p><h1>The right record.<br>The right place to look.</h1><p>Choose a document or a jurisdiction. Get official routes, a private worksheet and a specific next step—even when the search comes back empty.</p><p class="rf-caption">Free to use · No email required · Selected public-record routes, not nationwide coverage</p><a href="/records/seattle-residential-tank-records/#live-search">Seattle property? Search the City's residential dataset here ↗</a></header>
<section class="v2-shell rf-directory" data-record-finder>
    <div class="dc-short-answer"><h2>Not sure which document you need?</h2><p>Start with the seller's claim, an open permit or the paperwork you already have.</p><a href="/tank-document-check/">Build a document-gap checklist first ↗</a></div>
    <label class="rf-search" data-finder-search-control hidden>Find a town, agency or document<input type="search" data-finder-search placeholder="Try Portland, Nassau, NFA or decommissioning" maxlength="100" autocomplete="off"></label>
    <div class="rf-filter" data-finder-controls hidden><label>Jurisdiction<select data-finder-state><option value="">All supported places</option>
    @for (var state : ResearchCatalog.states())
    <option value="${state}">${ResearchCatalog.stateLabel(state)}</option>
    @endfor
    <option value="unsupported">Somewhere else / unsure</option></select></label><label>Record needed<select data-finder-family><option value="">Any record</option><option value="completion">Removal / completion</option><option value="environmental">Spill / environmental closure</option><option value="registration">Tank registration</option><option value="gap">Missing / conflicting papers</option></select></label><label>Route type<select data-finder-kind><option value="">All route types</option><option value="task">Agency / document-specific</option><option value="area">Local record holder</option><option value="problem">Missing or conflicting documents</option></select></label><p data-finder-count role="status">${ResearchCatalog.ALL.size()} routes</p></div>
    <div data-finder-empty hidden class="rf-next"><h2>No verified route for that combination yet.</h2><p>Choose the agency/document family for state-held files, or a local route for permits. If your place is not covered, identify the legal jurisdiction and ask its building or fire record custodian which office holds the existing file. We do not claim nationwide coverage or accept research outside NJ/NY.</p><a href="/record-help/agency-record-request/">Prepare a focused records request ↗</a></div>
    @for (var kind : java.util.List.of("task", "area", "problem"))
    <section data-finder-group><h2>${kind.equals("task") ? "Find a specific document" : kind.equals("area") ? "Follow a local record trail" : "Work through a missing piece"}</h2>
    @for (var entry : ResearchCatalog.ALL)
    @if (entry.kind().equals(kind))
    <a class="rf-route" href="${entry.path()}" data-finder-route data-state="${entry.state()}" data-kind="${entry.kind()}" data-family="${entry.recordFamily()}" data-route-id="${entry.slug()}"><span>${entry.locality()}</span><div><h3>${entry.title()}</h3><p>${entry.summary()}</p><small>${entry.assisted() ? "Self-service + optional NJ/NY assistance" : "Self-service guidance"}</small></div><b aria-hidden="true">↗</b></a>
    @endif
    @endfor
    </section>
    @endfor
</section>
<section class="v2-shell rf-explain"><h2>Which document answers your question?</h2><dl><dt>Permit</dt><dd>Authorization for the described work—not proof it was finished.</dd><dt>Final inspection / completion</dt><dd>The issuing agency's record of the stated work and scope. Match it to the permit.</dd><dt>NFA / environmental closure</dt><dd>Correspondence about a specific environmental case, not every tank on the parcel.</dd><dt>Spill listing</dt><dd>A reported incident reference. Get the underlying report before drawing conclusions.</dd></dl><p>County, municipal and state files can all be relevant. A missing online result does not replace a physical investigation.</p><a href="/guides/leaking-heating-oil-tank-what-to-do/">Active leak or strong odor? Use the safety route ↗</a></section>
`)

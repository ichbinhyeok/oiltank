@import owner.buriedoiltank.data.TankDocumentCheck
@param String situationId = "home-sale"
!{var initial = TankDocumentCheck.situation(situationId);}
<section class="dc-check" id="document-check" data-document-check>
    <div class="dc-check-heading"><p class="v2-eyebrow">Free document-gap checklist</p><h2>What does your paperwork<br>still leave open?</h2><p>Choose your situation and the document categories you have. Get research targets and the relevant official routes—not a safety or closing verdict.</p></div>
    <p class="rf-caption" id="dc-privacy">No address, email or upload. Choices stay in this page's memory; they are not saved, added to the URL or sent by this checker. Copy is optional and puts the checklist on your clipboard.</p>
    <p class="dc-safety"><a href="/guides/leaking-heating-oil-tank-what-to-do/">Active oil, strong odor or suspected leakage? Leave the paperwork workflow and use the safety route. ↗</a></p>
    <form data-document-form hidden autocomplete="off" aria-describedby="dc-privacy dc-limit">
        <div class="dc-fields"><label for="dc-situation">What are you trying to resolve?<select id="dc-situation" data-document-situation>
        @for (var situation : TankDocumentCheck.SITUATIONS)
            <option value="${situation.id()}" selected="${initial.id().equals(situation.id())}" data-answer="${situation.answer()}" data-next="${situation.next()}" data-targets="${String.join(",", situation.targets())}">${situation.label()}</option>
        @endfor
        </select></label><label for="dc-region">Where is the property?<select id="dc-region" data-document-region>
        @for (var region : TankDocumentCheck.REGIONS)
            <option value="${region.id()}">${region.label()}</option>
        @endfor
        </select></label></div>
        <fieldset><legend>Which documents do you have a copy of?</legend><p>Leave unchecked if you only heard about it or saw a status in an index. None selected is fine. Selecting a category does not verify the document.</p><div class="dc-documents">
        @for (var doc : TankDocumentCheck.DOCUMENTS)
            <label><input type="checkbox" value="${doc.id()}" data-document-choice data-label="${doc.label()}" data-match="${doc.match()}"><span><strong>${doc.label()}</strong><small>${doc.meaning()}</small></span></label>
        @endfor
        </div></fieldset>
        <div class="rf-actions"><button type="submit">Build my research checklist ↓</button><button type="button" data-document-reset>Reset choices</button></div>
    </form>
    <noscript><p>Interactive selection needs JavaScript. You can still use the document definitions and location-specific routes below; nothing needs to be submitted.</p></noscript>
    <p id="dc-limit" class="rf-caption">Research targets are not universally required documents. No document is authenticated here. This checklist does not certify compliance, no contamination, tank absence or readiness to close.</p>
    <p class="rf-status" data-document-status role="status" aria-live="polite"></p>
    <section class="dc-result" data-document-result hidden aria-labelledby="dc-result-heading">
        <h3 id="dc-result-heading" tabindex="-1">Your research checklist</h3><p class="dc-context" data-document-context></p><p data-document-answer></p>
        <h4>Document categories to ask about</h4><p>Based on your selections—not a determination of missing legal requirements. Environmental papers apply only if a relevant case exists.</p><ul data-document-gaps></ul>
        <h4>Copies you selected still need these checks</h4><ul data-document-matches></ul>
        <h4>Next action</h4><p data-document-next></p>
        <p>Request outline: “Please provide the existing [specific missing document] associated with [permit/case reference] at [property], during [dates]. I already hold [list]. Please identify the custodian or any retention/coverage limit.” Replace the placeholders privately; this checker does not collect them or send a request.</p>
        <div class="rf-actions"><button type="button" data-document-copy>Copy checklist</button></div>
    </section>
    <section class="dc-regions" aria-label="Location-specific document routes"><h3>Location changes the paperwork.</h3><p>These are starting routes, not proof that a file exists. Use a local route only when its named jurisdiction matches the property; the office and historical jurisdiction still matter.</p>
    @for (var region : TankDocumentCheck.REGIONS)
        <details data-document-region-guide="${region.id()}"><summary>${region.label()}</summary><p>${region.distinction()}</p><a href="${region.sourceUrl()}" rel="external">${region.sourceLabel()} ↗</a><ul>
        @for (var route : TankDocumentCheck.routes(region))
            <li><a href="${route.path()}">${route.title()} ↗</a></li>
        @endfor
        </ul><a href="/find-records/">Choose the exact local or agency record route ↗</a></details>
    @endfor
    </section>
    <details class="dc-definitions"><summary>What each document can—and cannot—answer</summary><dl>
    @for (var doc : TankDocumentCheck.DOCUMENTS)
        <dt>${doc.label()}</dt><dd>${doc.meaning()} ${doc.match()}</dd>
    @endfor
    </dl></details>
</section>

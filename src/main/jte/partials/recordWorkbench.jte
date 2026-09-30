@import owner.buriedoiltank.data.ResearchCatalog.Entry
@import owner.buriedoiltank.data.RecordEvidence
@param Entry entry
<section class="rf-workbench" id="worksheet" data-workbench data-route-id="${entry.slug()}" data-document-type="${entry.documentType()}">
    <p class="v2-eyebrow">Your private record worksheet</p><h2>Keep the next step clear.</h2>
    <p>Prepare search keys here, open the official sources above, then record what happened. Nothing is searched or sent automatically. A postal city or ZIP does not establish legal jurisdiction.</p>
    <p>Suggested document scope for this route: <span data-request-scope>${RecordEvidence.forEntry(entry).firstRequest()}</span>. Narrow this to the files you still need.</p>
    <noscript><p>Interactive saving and draft preparation require JavaScript. You can still follow every official route above, copy the request outline below and print this page. Do not enter personal details in disabled fields.</p></noscript>
    <fieldset disabled data-worksheet-fields>
        <legend>Optional identifiers — stay in your browser</legend>
        <div class="rf-fields">
            <label>Property address (optional)<input data-field="address" maxlength="240" autocomplete="off" spellcheck="false"></label>
            <label>Parcel / block and lot<input data-field="parcel" maxlength="160" autocomplete="off"></label>
            <label>Permit / incident / project number<input data-field="reference" maxlength="160" autocomplete="off"></label>
            <label>Approximate work dates<input data-field="dates" maxlength="100" autocomplete="off"></label>
        </div>
        <label>Specific documents still needed<textarea data-field="documents" rows="3" maxlength="1200" placeholder="For example: the final inspection attached to the permit, not just its index entry."></textarea></label>
        <h3>What happened at each source?</h3>
        <p>These are your notes, not verified agency findings. Leave a source as “Not checked” until you use it.</p>
        @for (int i = 0; i < entry.steps().size(); i++)
        <label class="rf-source-status">${entry.steps().get(i).title()}
            <select data-outcome="${i}">
                <option value="unchecked">Not checked</option><option value="index-only">Found a listing, but no document yet</option><option value="found">Obtained a candidate document</option>
                <option value="empty">Online search returned nothing</option><option value="blocked">Could not access the system</option>
                <option value="wrong-office">Wrong jurisdiction / office</option><option value="pending">Request sent by me — awaiting reply</option>
                <option value="no-records">Agency replied: no records in searched scope</option>
            </select>
        </label>
        @endfor
        <label>Search date / request reference / next check (optional)<textarea data-field="notes" rows="3" maxlength="1200" placeholder="Record the terms you tried, request ID, reply scope and a date to check again."></textarea></label>
    </fieldset>
    <div class="rf-next"><h3>Your next actions</h3><div data-next-actions>
        <p>Start with the identifiers and first applicable source. An online blank, an inaccessible portal and an agency no-record reply are three different outcomes.</p>
    </div><p><strong>No result is not proof of no tank.</strong> A candidate document still needs its property, issuer, dates and stated scope checked.</p></div>
    <div data-worksheet-actions hidden>
        <label>Prepared request draft<textarea data-draft rows="8" readonly aria-describedby="draft-boundary"></textarea></label>
        <pre class="rf-print-draft" data-print-draft></pre>
        <p id="draft-boundary">Review and edit bracketed fields after copying. Use the agency’s current official form, disclosures and fee instructions. Drafted ≠ sent ≠ acknowledged ≠ delivered.</p>
        <div class="rf-actions"><button type="button" data-copy-draft>Copy request draft</button><button type="button" data-print>Print worksheet</button><button type="button" data-save>Save on this device for 7 days</button><button type="button" data-clear>Clear this worksheet</button></div>
        <p data-storage-note>Default: this tab’s session only. Device saving is optional; avoid it on shared devices. No property details enter the page URL or analytics.</p>
        <p class="rf-status" data-workbench-status role="status" aria-live="polite"></p>
    </div>
    <p class="rf-support">${entry.assisted() ? "Optional human research is available for NJ/NY, subject to intake review. The worksheet is separate and is not attached automatically." : "Self-service guidance only here. Human research assistance is currently limited to New Jersey and New York."}</p>
</section>

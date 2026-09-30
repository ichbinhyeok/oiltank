/* Private, progressively enhanced worksheet. No property data leaves this module. */
(() => {
  const outcomes = {
    unchecked: 'Not checked: use this source only if it applies to your document. Keep it open until checked.',
    found: 'Candidate found (your report): compare property, parcel, issuer, dates and scope. Keep the actual file; an index or permit alone may not establish completion.',
    'index-only': 'Listing only (your report): keep the permit/case reference, then open the document or attachment control. If no file is available, request the named document. A listing is not delivery of the underlying file.',
    empty: 'Empty online search: retry a shorter street name, prior address or known reference. Check the source coverage, then request the specific missing document. This is not an agency no-record reply.',
    blocked: 'Access failed: this says nothing about the records. Try the official instructions or contact the listed custodian. Do not bypass access controls.',
    'wrong-office': 'Wrong office: confirm legal municipality and document issuer. Ask for the correct custodian, then choose another route. Postal city and ZIP are not sufficient.',
    pending: 'Pending (your report): retain your request ID, sent date and next check date. An acknowledgment is not delivery. This site has not sent a message.',
    'no-records': 'Agency no-record reply (your report): preserve the reply, dates and exact scope searched. Check other relevant custodians; it does not establish that no tank exists.'
  };
  const emit = (name, route, documentType, outcome) => {
    // Deliberate allowlist: never fields, textarea contents, titles or external query strings.
    try { window.gtag?.('event', name, {route_id: route, document_type: documentType, ...(outcome ? {outcome} : {})}); } catch { /* Analytics is optional. */ }
  };
  document.querySelectorAll('[data-record-finder]').forEach(root => {
    const state = root.querySelector('[data-finder-state]');
    const kind = root.querySelector('[data-finder-kind]');
    const search = root.querySelector('[data-finder-search]');
    const rows = [...root.querySelectorAll('[data-finder-route]')];
    root.querySelector('[data-finder-controls]').hidden = false;
    root.querySelector('[data-finder-search-control]').hidden = false;
    const filter = () => {
      let count = 0;
      const terms = search.value.trim().toLowerCase().split(/\s+/).filter(Boolean);
      rows.forEach(row => {
        const searchable = `${row.textContent} ${row.dataset.state} ${row.dataset.routeId}`.toLowerCase().replaceAll('-', ' ');
        row.hidden = !!((state.value && row.dataset.state !== state.value && !(row.dataset.state === '' && state.value !== 'unsupported')) || (kind.value && row.dataset.kind !== kind.value) || !terms.every(term => searchable.includes(term)));
        if (!row.hidden) count++;
      });
      root.querySelectorAll('[data-finder-group]').forEach(group => { group.hidden = ![...group.querySelectorAll('[data-finder-route]')].some(row => !row.hidden); });
      root.querySelector('[data-finder-count]').textContent = `${count} matching routes`;
      root.querySelector('[data-finder-empty]').hidden = count > 0;
    };
    state.addEventListener('change', filter); kind.addEventListener('change', filter);
    search.addEventListener('input', filter);
    rows.forEach(row => row.addEventListener('click', () => emit('route_selected', row.dataset.routeId, row.dataset.kind)));
    filter();
  });
  document.querySelectorAll('[data-workbench]').forEach(root => {
    const key = `otr-worksheet-v1:${root.dataset.routeId}`;
    const week = 7 * 24 * 60 * 60 * 1000;
    const fields = [...root.querySelectorAll('[data-field]')];
    const selects = [...root.querySelectorAll('[data-outcome]')];
    const sources = [...document.querySelectorAll('#research-sequence article')].map(article => ({
      title: article.querySelector('h2').textContent.trim(), url: article.querySelector('a.research-source').href
    }));
    const status = root.querySelector('[data-workbench-status]');
    const draft = root.querySelector('[data-draft]');
    const requestScope = root.querySelector('[data-request-scope]').textContent.trim();
    const note = root.querySelector('[data-storage-note]');
    let savedUntil = 0;
    const event = (name, outcome) => emit(name, root.dataset.routeId, root.dataset.documentType, outcome);
    const collect = () => ({
      version: 1, expires: savedUntil || Date.now() + week,
      fields: Object.fromEntries(fields.map(field => [field.dataset.field, field.value.slice(0, field.maxLength)])),
      outcomes: selects.map(select => select.value), sourceTitles: sources.map(source => source.title)
    });
    const restore = storage => {
      const raw = storage.getItem(key);
      if (!raw) return null;
      const data = JSON.parse(raw);
      if (data.version !== 1 || !Number.isFinite(data.expires) || data.expires <= Date.now() || data.expires > Date.now() + week || !data.fields || !Array.isArray(data.outcomes)) {
        storage.removeItem(key); return null;
      }
      return data;
    };
    let device = null, session = null;
    try { device = restore(localStorage); } catch { status.textContent = 'Device storage unavailable; copying and printing still work.'; }
    try { session = restore(sessionStorage); } catch { status.textContent = 'Tab storage unavailable; copying and printing still work.'; }
    {
      const data = session || device;
      if (data) {
        fields.forEach(field => { const value = data.fields[field.dataset.field]; field.value = typeof value === 'string' ? value.slice(0, field.maxLength) : ''; });
        selects.forEach((select, i) => {
          // Never shift an old result onto a newly inserted source step.
          const index = Array.isArray(data.sourceTitles) ? data.sourceTitles.indexOf(sources[i].title)
            : data.outcomes.length === selects.length ? i : -1;
          const value = index >= 0 ? data.outcomes[index] : 'unchecked';
          select.value = Object.hasOwn(outcomes, value) ? value : 'unchecked';
        });
        status.textContent = 'Worksheet restored. Source outcomes are your notes, not verified findings.';
        if (!data.sourceTitles && data.outcomes.length !== selects.length) status.textContent += ' The source sequence changed; check source outcomes again. Your identifiers and notes were kept.';
      }
      if (device) savedUntil = device.expires;
    }
    const render = () => {
      const data = collect();
      const next = root.querySelector('[data-next-actions]');
      next.replaceChildren();
      selects.forEach((select, i) => {
        const p = document.createElement('p');
        p.textContent = `${i + 1}. ${sources[i].title} — ${outcomes[select.value]}`;
        next.append(p);
      });
      const f = {...data.fields, documents: (data.fields.documents.trim() || requestScope).replace(/^the\s+/i, '')};
      draft.value = `DRAFT — NOT SENT\nRoute: ${document.querySelector('h1').textContent.trim()}\n\nPlease provide existing ${f.documents.trim() || '[specify the missing permit, final inspection, completion document or case report]'} for:\nProperty: ${f.address.trim() || '[address]'}\nParcel/block/lot: ${f.parcel.trim() || '[if known]'}\nPermit/case/project: ${f.reference.trim() || '[if known]'}\nWork/date range: ${f.dates.trim() || '[date range]'}\n\nIf this office does not hold the specified records, please identify the appropriate custodian. Please advise of any fees before chargeable work. I will use the agency’s current form and required disclosures.\n\nMy search notes (review before including):\n${f.notes || '[search terms, date, request reference and missing file]'}\n\nSource log — my reports, not verified findings:\n${sources.map((source, i) => `${source.title}\n${source.url}\n${selects[i].selectedOptions[0].textContent}`).join('\n\n')}`;
      root.querySelector('[data-print-draft]').textContent = draft.value;
      note.textContent = savedUntil > Date.now()
        ? `Device saving enabled until ${new Date(savedUntil).toLocaleString()}. Clear this worksheet to delete its saved details. Changes keep the same expiry.`
        : 'Default: this tab’s session only. Save explicitly to resume on this device for 7 days. Avoid saving on shared devices. No property details enter URLs or analytics.';
    };
    const persist = () => {
      try {
        if (savedUntil && savedUntil <= Date.now()) { localStorage.removeItem(key); savedUntil = 0; }
        const json = JSON.stringify(collect());
        sessionStorage.setItem(key, json);
        if (savedUntil) localStorage.setItem(key, json);
      } catch { status.textContent = 'Changes could not be saved. Keep this tab open or copy/print now.'; }
    };
    root.querySelector('[data-worksheet-fields]').disabled = false;
    root.querySelector('[data-worksheet-actions]').hidden = false;
    fields.forEach(field => field.addEventListener('input', () => { persist(); render(); }));
    selects.forEach(select => select.addEventListener('change', () => { persist(); render(); event('search_outcome_reported', select.value); }));
    document.querySelectorAll('#research-sequence a.research-source').forEach(link => link.addEventListener('click', () => event('official_route_opened')));
    document.querySelectorAll('a[href="#start-check"]').forEach(link => link.addEventListener('click', () => event('assist_cta_clicked')));
    root.querySelector('[data-save]').addEventListener('click', () => {
      try {
        savedUntil = Date.now() + week;
        localStorage.setItem(key, JSON.stringify(collect()));
        status.textContent = 'Saved on this device for up to 7 days. Nothing was sent to an agency.';
      } catch { savedUntil = 0; status.textContent = 'Device saving failed. Copy or print instead; your current work is still here.'; }
      render();
    });
    root.querySelector('[data-clear]').addEventListener('click', () => {
      let failed = false;
      for (const name of ['sessionStorage', 'localStorage']) { try { window[name].removeItem(key); } catch { failed = true; } }
      savedUntil = 0; fields.forEach(field => { field.value = ''; }); selects.forEach(select => { select.value = 'unchecked'; });
      render(); status.textContent = failed ? 'Visible worksheet cleared. Storage could not be accessed; use your browser’s site-data controls to remove saved data.' : 'This worksheet and its saved copies are cleared.';
    });
    root.querySelector('[data-copy-draft]').addEventListener('click', async () => {
      try { await navigator.clipboard.writeText(draft.value); status.textContent = 'Draft copied. Review it before using the official channel. Nothing has been sent.'; event('request_draft_copied'); }
      catch { draft.focus(); draft.select(); status.textContent = 'Automatic copy unavailable. The draft is selected: use your device’s Copy command.'; }
    });
    root.querySelector('[data-print]').addEventListener('click', () => { event('worksheet_print_requested'); window.print(); });
    render();
  });
})();

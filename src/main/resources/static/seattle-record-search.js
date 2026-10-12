/* Explicit, direct public-data query. No analytics, storage, proxy or automatic requests. */
(() => {
  const root = document.querySelector('[data-seattle-search]');
  if (!root) return;
  const form = root.querySelector('[data-seattle-form]');
  const status = root.querySelector('[data-seattle-status]');
  const results = root.querySelector('[data-seattle-results]');
  let pending = null, generation = 0;
  form.hidden = false;
  const clean = value => typeof value === 'string' ? value.slice(0, 240).trim() : '';
  const date = value => /^\d{4}-\d{2}-\d{2}(?:T|$)/.test(clean(value)) ? clean(value).slice(0, 10) : 'Not published';
  const text = (tag, value) => { const node = document.createElement(tag); node.textContent = value; return node; };
  const stop = () => { generation++; pending?.abort(); pending = null; root.removeAttribute('aria-busy'); };
  form.addEventListener('reset', () => { stop(); results.replaceChildren(); status.textContent = 'Search and displayed results cleared.'; });
  form.addEventListener('submit', async event => {
    event.preventDefault();
    if (!form.reportValidity()) return;
    const house = form.elements.house.value.trim().toUpperCase();
    const street = form.elements.street.value.trim().toUpperCase().replace(/\s+/g, ' ');
    // Reject query syntax and wildcards; apostrophes are escaped as SQL string literals.
    if (!/^\d{1,6}[A-Z]?$/.test(house) || !/^[A-Z0-9 .'-]{2,60}$/.test(street) || !/[A-Z0-9]{2}/.test(street)) {
      status.textContent = 'Use a house number and a street name or number with letters, numbers, spaces, periods, apostrophes or hyphens. Try the official dataset for other address formats.';
      return;
    }
    stop();
    const current = generation;
    const controller = new AbortController(); pending = controller;
    let timedOut = false;
    const timeout = setTimeout(() => { timedOut = true; controller.abort(); }, 15000);
    const escaped = value => value.replaceAll("'", "''");
    const where = [`upper(address) like '${escaped(house)} %'`, ...street.split(' ').filter(Boolean).map(token => `upper(address) like '%${escaped(token)}%'`)].join(' AND ');
    const url = new URL('https://data.seattle.gov/resource/xvj2-ai6y.json');
    url.searchParams.set('$where', where);
    url.searchParams.set('$select', 'address,permit_number,date_issued,date_decommissioned,type_decommissioned,fill_material,tank_size');
    url.searchParams.set('$order', 'date_issued DESC,permit_number,address');
    url.searchParams.set('$limit', '21');
    results.replaceChildren(); root.setAttribute('aria-busy', 'true');
    status.textContent = 'Searching Seattle Open Data…';
    try {
      const response = await fetch(url, {signal: controller.signal, credentials: 'omit', referrerPolicy: 'no-referrer', cache: 'no-store'});
      if (!response.ok) throw new Error(response.status === 429 ? 'busy' : 'unavailable');
      const rows = await response.json();
      if (!Array.isArray(rows) || rows.length > 21 || rows.some(row => !row || typeof row !== 'object' || Array.isArray(row) || !clean(row.address))) throw new Error('invalid');
      if (current !== generation) return;
      if (!rows.length) {
        status.textContent = 'No candidate rows returned for these terms. This is not a no-tank or no-records finding. Try a shorter street name, check the address and jurisdiction, then request unpublished or older files if needed.';
        return;
      }
      status.textContent = rows.length > 20 ? 'Showing 20 candidate rows; more may exist. Narrow the street terms before drawing conclusions.' : `${rows.length} candidate row${rows.length === 1 ? '' : 's'} returned. Confirm the address, tank and work dates; a row is not a completion certificate.`;
      rows.slice(0, 20).forEach(row => {
        const article = document.createElement('article');
        article.append(text('h3', clean(row.address)));
        const dl = document.createElement('dl');
        [ ['Permit reference', clean(row.permit_number) || 'Not published'], ['Date issued (not completion)', date(row.date_issued)], ['Decommissioning date', date(row.date_decommissioned)], ['Decommissioning method', clean(row.type_decommissioned) || 'Not published'], ['Fill material', clean(row.fill_material) || 'Not published / not applicable'], ['Tank size (as published)', clean(row.tank_size) || 'Not published'] ].forEach(([label, value]) => dl.append(text('dt', label), text('dd', value)));
        article.append(dl);
        article.append(text('p', !clean(row.date_decommissioned) || !clean(row.type_decommissioned)
          ? 'Completion details are incomplete in this row. Request the follow-up report and recorded work method; do not substitute the permit issue date.'
          : 'This row records a decommissioning date and method. Removal and abandonment in place are different actions. Obtain the underlying report if you need documentary proof.'));
        results.append(article);
      });
    } catch (error) {
      if (current !== generation) return;
      status.textContent = timedOut ? 'Seattle lookup timed out. No search outcome was established. Retry or use the official dataset.'
        : error.message === 'busy' ? 'Seattle Open Data is rate-limiting requests. No search outcome was established. Try again later or open the official dataset.'
        : 'Seattle lookup is unavailable or returned an unexpected response. This is not an empty search result. Use the official dataset or records-request route.';
    } finally {
      clearTimeout(timeout);
      if (current === generation) { pending = null; root.removeAttribute('aria-busy'); }
    }
  });
})();

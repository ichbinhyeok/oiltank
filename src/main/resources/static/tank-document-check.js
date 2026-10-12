(() => {
  'use strict';
  document.querySelectorAll('[data-document-check]').forEach(root => {
    const form = root.querySelector('[data-document-form]');
    const situation = root.querySelector('[data-document-situation]');
    const region = root.querySelector('[data-document-region]');
    const result = root.querySelector('[data-document-result]');
    const status = root.querySelector('[data-document-status]');
    const choices = [...root.querySelectorAll('[data-document-choice]')];
    const guides = [...root.querySelectorAll('[data-document-region-guide]')];
    if (!form || !situation || !region || !result || !status) return;
    let copyGeneration = 0;
    const fill = (selector, value) => { root.querySelector(selector).textContent = value; };
    const list = (selector, texts) => {
      const node = root.querySelector(selector);
      node.replaceChildren(...texts.map(text => { const li = document.createElement('li'); li.textContent = text; return li; }));
    };
    const invalidate = () => {
      copyGeneration++;
      result.hidden = true;
      status.textContent = '';
      guides.forEach(guide => { guide.hidden = false; guide.open = false; });
    };
    form.addEventListener('change', () => {
      const hadResult = !result.hidden;
      invalidate();
      if (hadResult) status.textContent = 'Choices changed. Build a new checklist to update the result.';
    });
    form.addEventListener('submit', event => {
      event.preventDefault();
      copyGeneration++;
      const selected = situation.selectedOptions[0];
      const targets = new Set(selected.dataset.targets.split(','));
      const gaps = choices.filter(choice => targets.has(choice.value) && !choice.checked);
      const held = choices.filter(choice => choice.checked);
      fill('[data-document-context]', `${selected.textContent} · ${region.selectedOptions[0].textContent}`);
      fill('[data-document-answer]', selected.dataset.answer);
      fill('[data-document-next]', selected.dataset.next);
      list('[data-document-gaps]', gaps.length ? gaps.map(choice => `${choice.dataset.label}: ${choice.dataset.match}`) : ['You selected all target categories for this question. That does not establish completeness, authenticity or adequacy; match the underlying documents and their scope.']);
      list('[data-document-matches]', held.length ? held.map(choice => `${choice.dataset.label}: ${choice.dataset.match}`) : ['No document copies selected. Start by identifying the work, issuer and known reference; do not substitute a seller statement or index status for the file.']);
      guides.forEach(guide => { guide.hidden = guide.dataset.documentRegionGuide !== region.value; guide.open = !guide.hidden; });
      result.hidden = false;
      status.textContent = 'Checklist built from your selections. No documents or agency records were checked.';
      result.scrollIntoView({block: 'start', behavior: 'instant'});
      root.querySelector('#dc-result-heading').focus({preventScroll: true});
    });
    root.querySelector('[data-document-reset]').addEventListener('click', () => {
      form.reset();
      invalidate();
      result.querySelectorAll('[data-document-gaps], [data-document-matches]').forEach(node => node.replaceChildren());
      ['[data-document-context]', '[data-document-answer]', '[data-document-next]'].forEach(selector => fill(selector, ''));
      status.textContent = 'Choices and generated checklist cleared from this page. Any earlier clipboard copy remains outside the page.';
    });
    root.querySelector('[data-document-copy]').addEventListener('click', async () => {
      if (result.hidden) return;
      const generation = ++copyGeneration;
      const guide = guides.find(item => !item.hidden);
      const links = guide ? [...guide.querySelectorAll('a')].map(link => `${link.textContent.trim()}: ${link.href}`).join('\n') : '';
      const text = ['Oil Tank Route — self-reported research checklist', result.innerText.replace('Copy checklist', '').trim(), guide?.innerText || '', links, 'Limits: not authenticated; not a safety, compliance, tank-free or transaction determination.', `${location.origin}${location.pathname}`].join('\n\n');
      try {
        if (!navigator.clipboard?.writeText) throw new Error('Clipboard unavailable');
        await navigator.clipboard.writeText(text);
        if (generation === copyGeneration) status.textContent = 'Checklist copied. Your clipboard may be stored or synced by your device.';
      } catch {
        if (generation === copyGeneration) status.textContent = 'Clipboard unavailable. Select and copy the visible checklist manually.';
      }
    });
    form.reset();
    form.hidden = false;
  });
})();

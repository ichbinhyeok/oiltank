/* Progressive enhancements: every route and form remains usable without JS. */
(() => {
  document.querySelectorAll('.v2-mobile-menu').forEach(menu => {
    menu.querySelectorAll('a').forEach(link => link.addEventListener('click', () => { menu.open = false; }));
    menu.addEventListener('keydown', event => {
      if (event.key === 'Escape') { menu.open = false; menu.querySelector('summary').focus(); }
    });
  });
  document.querySelectorAll('[data-area-explorer]').forEach(explorer => {
    explorer.querySelector('[data-area-controls]').hidden = false;
    const search = explorer.querySelector('[data-area-search]');
    const buttons = [...explorer.querySelectorAll('[data-area-filter]')];
    const rows = [...explorer.querySelectorAll('[data-area-row]')];
    let state = 'all';
    const render = () => {
      const query = search.value.trim().toLocaleLowerCase();
      let count = 0;
      rows.forEach(row => {
        row.hidden = !((state === 'all' || row.dataset.state === state) && row.dataset.search.toLocaleLowerCase().includes(query));
        if (!row.hidden) count++;
      });
      explorer.querySelectorAll('[data-area-group]').forEach(group => {
        group.hidden = ![...group.querySelectorAll('[data-area-row]')].some(row => !row.hidden);
      });
      buttons.forEach(button => button.setAttribute('aria-pressed', String(button.dataset.areaFilter === state)));
      explorer.querySelector('[data-area-count]').textContent = `${count} ${count === 1 ? 'area' : 'areas'}`;
      explorer.querySelector('[data-area-empty]').hidden = count > 0;
    };
    search.addEventListener('input', render);
    buttons.forEach(button => button.addEventListener('click', () => { state = button.dataset.areaFilter; render(); }));
    // Hash links remain ordinary links; no hidden state is required for deep links.
  });

  document.querySelectorAll('[data-copy-request]').forEach(button => {
    if (!navigator.clipboard?.writeText) return;
    button.hidden = false;
    button.addEventListener('click', async () => {
      const section = button.closest('.v2-request');
      const status = section.querySelector('[data-copy-status]');
      try {
        await navigator.clipboard.writeText(section.querySelector('[data-request-text]').textContent.trim());
        status.textContent = 'Outline copied. Replace the bracketed fields before sending.';
      } catch { status.textContent = 'Copy is unavailable. Select and copy the outline text below.'; }
    });
  });

  document.querySelectorAll('[data-research-form]').forEach(form => {
    const steps = [...form.querySelectorAll('[data-form-step]')];
    if (steps.length !== 3) return;
    const progress = form.querySelector('[data-form-progress]');
    const actions = form.querySelector('[data-form-actions]');
    const back = form.querySelector('[data-form-back]');
    const next = form.querySelector('[data-form-next]');
    const submit = form.querySelector('button[type=submit]');
    let index = 0;
    form.noValidate = true;
    progress.hidden = false;
    actions.hidden = false;
    const show = (value, focus = true) => {
      index = value;
      steps.forEach((step, i) => { step.hidden = i !== index; });
      [...progress.children].forEach((item, i) => {
        if (i === index) item.setAttribute('aria-current', 'step');
        else item.removeAttribute('aria-current');
      });
      back.hidden = index === 0;
      next.hidden = index === steps.length - 1;
      submit.hidden = index !== steps.length - 1;
      if (focus) steps[index].querySelector('legend').focus({preventScroll:true});
    };
    const firstInvalid = step => [...step.querySelectorAll('input,select,textarea')].find(field => !field.checkValidity());
    const advance = () => {
      const invalid = firstInvalid(steps[index]);
      if (invalid) { invalid.reportValidity(); return; }
      show(Math.min(index + 1, steps.length - 1));
    };
    next.addEventListener('click', advance);
    back.addEventListener('click', () => show(Math.max(0, index - 1)));
    // Capture before analytics/draft listeners: intermediate steps are not submissions.
    form.addEventListener('submit', event => {
      if (index < steps.length - 1) {
        event.preventDefault(); event.stopImmediatePropagation(); advance(); return;
      }
      for (let i = 0; i < steps.length; i++) {
        const invalid = firstInvalid(steps[i]);
        if (invalid) {
          event.preventDefault(); event.stopImmediatePropagation(); show(i); invalid.reportValidity(); return;
        }
      }
    }, true);
    show(0, false);
  });

  const nav = document.querySelector('.v2-research-nav nav');
  if (nav) {
    const links = [...nav.querySelectorAll('a[href^="#"]')];
    let queued = false;
    const update = () => {
      const current = [...links].reverse().find(link => document.querySelector(link.hash)?.getBoundingClientRect().top <= 160) || links[0];
      links.forEach(link => {
        if (link === current) link.setAttribute('aria-current', 'true');
        else link.removeAttribute('aria-current');
      });
      queued = false;
    };
    window.addEventListener('scroll', () => { if (!queued) { queued = true; requestAnimationFrame(update); } }, {passive:true});
    window.addEventListener('resize', update);
    update();
  }
})();

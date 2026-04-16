const scenarioPartnerMap = {
  buyer_seller: {
    partner: "sweep_or_locate",
    helper: "Start with locate or sweep work when records and physical clues do not line up."
  },
  sweep_first: {
    partner: "sweep_or_locate",
    helper: "Use a locate or sweep when site clues and records conflict."
  },
  records_first: {
    partner: "sweep_or_locate",
    helper: "Start with permits, closure paperwork, and prior fuel records before treating silence as proof."
  },
  removal_decision: {
    partner: "closure_or_removal",
    helper: "Use a closure or removal contractor when the tank is confirmed and the next choice is disposition."
  },
  leak_concern: {
    partner: "environmental_cleanup",
    helper: "Use an environmental specialist when a suspected leak or spill moves beyond ordinary contractor scope."
  }
};

function postEvent(data) {
  const body = new URLSearchParams(data);
  fetch("/api/leads/event", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
    },
    body,
    keepalive: true
  }).catch(() => {});
}

function bindAnchors() {
  const anchors = document.querySelectorAll('a[href^="#"], a[href^="/#"]');

  anchors.forEach((anchor) => {
    anchor.addEventListener("click", (event) => {
      if (event.defaultPrevented || event.button !== 0 || event.metaKey || event.ctrlKey || event.shiftKey || event.altKey) {
        return;
      }

      const url = new URL(anchor.href, window.location.origin);
      if (url.origin !== window.location.origin || url.pathname !== window.location.pathname || !url.hash) {
        return;
      }

      const target = document.querySelector(url.hash);
      if (!target) {
        return;
      }

      event.preventDefault();
      const headerHeight = document.querySelector(".site-header")?.getBoundingClientRect().height ?? 0;
      const offset = headerHeight + 16;
      const top = Math.max(target.getBoundingClientRect().top + window.scrollY - offset, 0);

      window.history.pushState({}, "", url.hash);
      window.scrollTo({
        top,
        behavior: "smooth"
      });
    });
  });
}

function bindCta(root) {
  const openButton = root.querySelector("[data-cta-open]");
  const form = root.querySelector("[data-lead-form]");
  const scenarioInput = root.querySelector("[data-scenario-input]");
  const partnerInput = root.querySelector("[data-partner-type-input]");
  const stateInput = root.querySelector("[data-state-input]");
  const partnerHelper = root.querySelector("[data-partner-helper]");
  const statusNode = root.querySelector("[data-form-status]");

  if (!openButton || !form || !scenarioInput || !partnerInput || !partnerHelper) {
    return;
  }

  const syncPartnerType = () => {
    const next = scenarioPartnerMap[scenarioInput.value];
    if (!next) {
      return;
    }
    partnerInput.value = next.partner;
    partnerHelper.textContent = next.helper;
  };

  openButton.addEventListener("click", () => {
    syncPartnerType();
    form.dataset.collapsed = "false";
    openButton.hidden = true;
    const firstField = form.querySelector('select[name="userRole"], input[name="email"]');
    if (firstField) {
      firstField.focus();
    }
    const payload = {
      eventType: "lead_open",
      pageId: openButton.dataset.pageId,
      pagePath: openButton.dataset.pagePath,
      routeFamily: openButton.dataset.routeFamily,
      scenario: scenarioInput.value,
      partnerType: partnerInput.value,
      stateSlug: stateInput ? stateInput.value : "",
      element: "lead-form-toggle",
      referrer: document.referrer
    };
    postEvent({ ...payload, eventType: "cta_click" });
    postEvent(payload);
  });

  scenarioInput.addEventListener("change", syncPartnerType);
  syncPartnerType();

  const query = new URLSearchParams(window.location.search);
  if (query.get("lead") === "success") {
    form.dataset.collapsed = "false";
    openButton.hidden = true;
    statusNode.hidden = false;
    statusNode.textContent = "Your file checklist was recorded.";
  } else if (query.get("lead") === "busy") {
    form.dataset.collapsed = "false";
    openButton.hidden = true;
    statusNode.hidden = false;
    statusNode.textContent = "The checklist is busy right now. Wait a minute and send it again.";
  } else if (query.get("lead") === "error") {
    form.dataset.collapsed = "false";
    openButton.hidden = true;
    statusNode.hidden = false;
    statusNode.textContent = "Check the file details and try again.";
  } else {
    form.dataset.collapsed = "true";
  }
}

bindAnchors();
document.querySelectorAll("[data-cta-root]").forEach(bindCta);

const scenarioPartnerMap = {
  buyer_seller: {
    partner: "sweep_or_locate",
    helper: "Useful when you want the main records, sweep, and next-step prompts in one shareable list."
  },
  sweep_first: {
    partner: "sweep_or_locate",
    helper: "Useful when you need to decide whether site clues actually justify a sweep or locate."
  },
  records_first: {
    partner: "sweep_or_locate",
    helper: "Useful when the paperwork trail is thin and you want a cleaner request list before anyone assumes too much."
  },
  removal_decision: {
    partner: "closure_or_removal",
    helper: "Useful once the tank is confirmed and you are comparing closure paths."
  },
  leak_concern: {
    partner: "environmental_cleanup",
    helper: "Useful once odor, staining, or release evidence makes this more than a paperwork issue."
  }
};

function pushAnalyticsEvent(name, params = {}) {
  if (typeof window.gtag !== "function") {
    return;
  }

  window.gtag("event", name, {
    page_title: document.title,
    page_path: window.location.pathname,
    ...params
  });
}

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

function buildAnalyticsPayload(data) {
  return {
    page_id: data.pageId,
    page_path: data.pagePath,
    route_family: data.routeFamily || "",
    scenario: data.scenario || "",
    partner_type: data.partnerType || "",
    state_slug: data.stateSlug || "",
    element: data.element || ""
  };
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

function bindPrimaryCtas() {
  document.querySelectorAll("[data-primary-cta]").forEach((cta) => {
    cta.addEventListener("click", () => {
      pushAnalyticsEvent("primary_cta_click", {
        element: "primary-cta",
        link_text: cta.textContent?.trim() || "",
        destination: cta.getAttribute("href") || ""
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

  const buildPayload = (eventType, element) => ({
    eventType,
    pageId: openButton.dataset.pageId,
    pagePath: openButton.dataset.pagePath,
    routeFamily: openButton.dataset.routeFamily,
    scenario: scenarioInput.value,
    partnerType: partnerInput.value,
    stateSlug: stateInput ? stateInput.value : "",
    element,
    referrer: document.referrer
  });

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
    const leadOpenPayload = buildPayload("lead_open", "lead-form-toggle");
    const ctaClickPayload = { ...leadOpenPayload, eventType: "cta_click" };

    postEvent(ctaClickPayload);
    postEvent(leadOpenPayload);
    pushAnalyticsEvent("cta_click", buildAnalyticsPayload(ctaClickPayload));
    pushAnalyticsEvent("lead_open", buildAnalyticsPayload(leadOpenPayload));
  });

  scenarioInput.addEventListener("change", syncPartnerType);
  syncPartnerType();

  form.addEventListener("submit", () => {
    syncPartnerType();
    const submitPayload = buildPayload("lead_submit", "lead-form-submit");
    pushAnalyticsEvent("lead_submit", buildAnalyticsPayload(submitPayload));
  });

  const query = new URLSearchParams(window.location.search);
  if (query.get("lead") === "success") {
    form.dataset.collapsed = "false";
    openButton.hidden = true;
    statusNode.hidden = false;
    statusNode.textContent = "Your worksheet request was recorded.";
    pushAnalyticsEvent("lead_submit_result", {
      ...buildAnalyticsPayload(buildPayload("lead_submit_result", "lead-form-submit")),
      result: "success"
    });
  } else if (query.get("lead") === "busy") {
    form.dataset.collapsed = "false";
    openButton.hidden = true;
    statusNode.hidden = false;
    statusNode.textContent = "The worksheet is busy right now. Wait a minute and send it again.";
    pushAnalyticsEvent("lead_submit_result", {
      ...buildAnalyticsPayload(buildPayload("lead_submit_result", "lead-form-submit")),
      result: "busy"
    });
  } else if (query.get("lead") === "error") {
    form.dataset.collapsed = "false";
    openButton.hidden = true;
    statusNode.hidden = false;
    statusNode.textContent = "Check the file details and try again.";
    pushAnalyticsEvent("lead_submit_result", {
      ...buildAnalyticsPayload(buildPayload("lead_submit_result", "lead-form-submit")),
      result: "error"
    });
  } else {
    form.dataset.collapsed = "true";
  }
}

bindAnchors();
bindPrimaryCtas();
document.querySelectorAll("[data-cta-root]").forEach(bindCta);

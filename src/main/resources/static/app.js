function initializeAnalytics() {
  const measurementId = document.querySelector('meta[name="otr-analytics-id"]')?.content?.trim();
  if (!measurementId || !/^G-[A-Z0-9]+$/.test(measurementId)) {
    return;
  }

  window.dataLayer = window.dataLayer || [];
  window.gtag = window.gtag || function gtag() {
    window.dataLayer.push(arguments);
  };
  window.gtag("js", new Date());
  window.gtag("config", measurementId);

  const script = document.createElement("script");
  script.async = true;
  script.src = `https://www.googletagmanager.com/gtag/js?id=${encodeURIComponent(measurementId)}`;
  document.head.append(script);
}

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
    partner: "record_research",
    helper: "Useful when the paperwork trail is thin and you want the correct record route and evidence gaps organized first."
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
      if (anchor.classList.contains("skip-link") && typeof target.focus === "function") {
        target.focus({ preventScroll: true });
      }
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

function researchEventPayload(element, eventType) {
  const root = element.closest("[data-page-id]");
  const elementLabel = element.dataset.sourceTitle
    || element.dataset.analyticsEvent
    || element.dataset.analyticsView
    || eventType;
  return {
    eventType,
    pageId: root?.dataset.pageId || document.body.dataset.pageId || `research:${window.location.pathname}`,
    pagePath: window.location.pathname,
    routeFamily: "records-and-proof",
    scenario: "records_first",
    partnerType: "record_research",
    stateSlug: window.location.pathname.includes("new-jersey") ? "new-jersey" : "new-york",
    element: elementLabel,
    referrer: document.referrer
  };
}

function bindResearchEvents() {
  document.querySelectorAll("[data-analytics-event]").forEach((element) => {
    element.addEventListener("click", () => {
      const eventType = element.dataset.analyticsEvent;
      const payload = researchEventPayload(element, eventType);
      postEvent(payload);
      pushAnalyticsEvent(eventType, buildAnalyticsPayload(payload));
    });
  });

  document.querySelectorAll("[data-analytics-view]").forEach((element) => {
    const eventType = element.dataset.analyticsView;
    const payload = researchEventPayload(element, eventType);
    postEvent(payload);
    pushAnalyticsEvent(eventType, buildAnalyticsPayload(payload));
  });

  document.querySelectorAll("[data-records-checklist]").forEach((checklist) => {
    const checks = [...checklist.querySelectorAll("[data-records-check]")];
    checks.forEach((check) => check.addEventListener("change", () => {
      if (checklist.dataset.completed === "true" || !checks.every((item) => item.checked)) {
        return;
      }
      checklist.dataset.completed = "true";
      const payload = researchEventPayload(checklist, "records_checklist_complete");
      postEvent(payload);
      pushAnalyticsEvent("records_checklist_complete", buildAnalyticsPayload(payload));
    }));
  });
}

function serviceEventPayload(eventType, element, form) {
  const root = element?.closest("[data-page-id]") || document.querySelector("[data-page-id]");
  return {
    eventType,
    pageId: form?.elements?.pageId?.value || root?.dataset.pageId || `service:${window.location.pathname}`,
    pagePath: window.location.pathname,
    routeFamily: "record-research",
    scenario: "records_first",
    partnerType: "record_research",
    stateSlug: form?.elements?.stateSlug?.value || root?.dataset.stateSlug || "national",
    element: element?.dataset?.ctaLocation || eventType,
    referrer: document.referrer
  };
}

function emitServiceEvent(eventType, element, form) {
  const payload = serviceEventPayload(eventType, element, form);
  postEvent(payload);
  pushAnalyticsEvent(eventType, buildAnalyticsPayload(payload));
}

function researchDraftKey(form) {
  return `otrResearchDraft:${form.elements.pagePath?.value || window.location.pathname}`;
}

function storeResearchDraft(form) {
  try {
    const values = {};
    Array.from(form.elements).forEach((field) => {
      if (!field.name || field.name === "documents" || ["submit", "button"].includes(field.type)) return;
      values[field.name] = field.value;
    });
    sessionStorage.setItem(researchDraftKey(form), JSON.stringify({
      values,
      hadFiles: (form.elements.documents?.files?.length || 0) > 0
    }));
  } catch (ignored) {
    // The server remains the source of truth when browser session storage is unavailable.
  }
}

function restoreResearchDraft(form) {
  try {
    const draft = JSON.parse(sessionStorage.getItem(researchDraftKey(form)) || "{}");
    Object.entries(draft.values || {}).forEach(([name, value]) => {
      const field = form.elements.namedItem(name);
      if (field && typeof field.value === "string") field.value = value;
    });
    return draft;
  } catch (ignored) {
    return {};
  }
}

function clearResearchDraft(form) {
  try {
    sessionStorage.removeItem(researchDraftKey(form));
  } catch (ignored) {
    // Nothing else is required when browser session storage is unavailable.
  }
}

function cleanLeadQuery(query) {
  query.delete("lead");
  query.delete("receipt");
  const cleanQuery = query.toString();
  window.history.replaceState({}, "", window.location.pathname + (cleanQuery ? `?${cleanQuery}` : "") + window.location.hash);
}

function bindResponsiveBriefs() {
  if (!window.matchMedia("(max-width: 47.99rem)").matches) return;
  document.querySelectorAll("[data-brief-details]").forEach((details) => details.removeAttribute("open"));
}

function bindServiceAnalytics() {
  document.querySelectorAll("[data-service-cta]").forEach((cta) => {
    cta.addEventListener("click", () => emitServiceEvent("service_cta_click", cta));
  });

  const viewTargets = document.querySelectorAll("[data-service-cta-view]");
  if ("IntersectionObserver" in window) {
    const observer = new IntersectionObserver((entries) => {
      entries.filter((entry) => entry.isIntersecting).forEach((entry) => {
        emitServiceEvent("service_cta_view", entry.target);
        observer.unobserve(entry.target);
      });
    }, { threshold: 0.25 });
    viewTargets.forEach((target) => observer.observe(target));
  } else {
    viewTargets.forEach((target) => emitServiceEvent("service_cta_view", target));
  }

  document.querySelectorAll("[data-research-form]").forEach((form) => {
    const tokenInput = form.querySelector("[data-submission-token]");
    if (tokenInput && !tokenInput.value) {
      tokenInput.value = typeof crypto.randomUUID === "function"
        ? crypto.randomUUID()
        : `${Date.now()}-${Math.random().toString(16).slice(2)}`;
    }
    let started = false;
    const markStarted = () => {
      if (started) return;
      started = true;
      emitServiceEvent("research_form_start", form, form);
    };
    form.addEventListener("focusin", markStarted);
    form.addEventListener("input", markStarted);
    form.addEventListener("submit", () => {
      storeResearchDraft(form);
      const receiptContext = {
        attempted: true,
        pageId: form.elements.pageId?.value || "service:unknown",
        pagePath: form.elements.pagePath?.value || window.location.pathname,
        stateSlug: form.elements.stateSlug?.value || "national",
        primaryQuestion: form.elements.primaryQuestion?.value || ""
      };
      sessionStorage.setItem("otrResearchReceiptContext", JSON.stringify(receiptContext));
      emitServiceEvent("research_form_submit_attempt", form, form);
    });

    const query = new URLSearchParams(window.location.search);
    const status = form.querySelector("[data-form-status]");
    if (!status) return;
    if (query.get("lead") === "success") {
      status.hidden = false;
      const receipt = /^OTR-[A-Z0-9]{1,8}$/.test(query.get("receipt") || "")
        ? query.get("receipt")
        : "OTR-PENDING";
      status.textContent = `Property received — receipt ${receipt}. Keep this number for follow-up. The initial route review target is within 2 business days; agency timing may extend the final answer.`;
      status.dataset.state = "success";
      try {
        const receiptContext = JSON.parse(sessionStorage.getItem("otrResearchReceiptContext") || "{}");
        if (receiptContext.attempted === true) {
          const successPayload = {
            pageId: receiptContext.pageId || "service:unknown",
            pagePath: receiptContext.pagePath || window.location.pathname,
            routeFamily: "record-research",
            scenario: "records_first",
            partnerType: "record_research",
            stateSlug: receiptContext.stateSlug || "national",
            element: "lead-form"
          };
          pushAnalyticsEvent("research_form_submit_success", buildAnalyticsPayload(successPayload));
          if (receiptContext.primaryQuestion === "interpret_documents") {
            pushAnalyticsEvent("document_interpretation_request", buildAnalyticsPayload(successPayload));
          }
          if (["new-jersey", "new-york"].includes(receiptContext.stateSlug)) {
            pushAnalyticsEvent("qualified_case", buildAnalyticsPayload(successPayload));
          }
        }
      } catch (ignored) {
        // The server remains the source of truth when browser session storage is unavailable.
      }
      sessionStorage.removeItem("otrResearchReceiptContext");
      clearResearchDraft(form);
      cleanLeadQuery(query);
      status.focus();
    } else if (query.get("lead") === "busy") {
      sessionStorage.removeItem("otrResearchReceiptContext");
      const draft = restoreResearchDraft(form);
      status.hidden = false;
      status.textContent = "The intake is busy. Your entries were restored. Wait a minute, then submit again."
        + (draft.hadFiles ? " Please reattach your files; browsers cannot restore file selections." : "");
      status.dataset.state = "error";
      cleanLeadQuery(query);
      status.focus();
    } else if (query.get("lead") === "error") {
      sessionStorage.removeItem("otrResearchReceiptContext");
      const draft = restoreResearchDraft(form);
      status.hidden = false;
      status.textContent = "We restored your entries. Check the required fields and attached-file limits, then try again."
        + (draft.hadFiles ? " Please reattach your files; browsers cannot restore file selections." : "");
      status.dataset.state = "error";
      cleanLeadQuery(query);
      status.focus();
    }
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
  if (stateInput && openButton.dataset.routeFamily === "records-and-proof") {
    stateInput.addEventListener("change", () => {
      const payload = buildPayload("records_state_select", "records-state-select");
      postEvent(payload);
      pushAnalyticsEvent("records_state_select", buildAnalyticsPayload(payload));
    });
  }
  syncPartnerType();

  form.addEventListener("submit", () => {
    syncPartnerType();
    const submitPayload = buildPayload("lead_submit", "lead-form-submit");
    pushAnalyticsEvent("lead_submit", buildAnalyticsPayload(submitPayload));
    if (openButton.dataset.routeFamily === "records-and-proof") {
      const recordsPayload = buildPayload("result_email_submit", "records-result-email");
      postEvent(recordsPayload);
      pushAnalyticsEvent("result_email_submit", buildAnalyticsPayload(recordsPayload));
    } else if (openButton.dataset.routeFamily === "sweep-and-locate") {
      const sweepPayload = buildPayload("sweep_readiness_complete", "sweep-readiness-form");
      postEvent(sweepPayload);
      pushAnalyticsEvent("sweep_readiness_complete", buildAnalyticsPayload(sweepPayload));
    }
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

initializeAnalytics();
bindAnchors();
bindPrimaryCtas();
bindResearchEvents();
bindResponsiveBriefs();
bindServiceAnalytics();
document.querySelectorAll("[data-cta-root]").forEach(bindCta);

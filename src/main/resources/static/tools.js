const GALLONS_PER_CUBIC_INCH = 1 / 231;

function readTankSpecs() {
  const node = document.getElementById("tank-data");
  if (!node) return [];
  try {
    return JSON.parse(node.textContent || "[]");
  } catch (_) {
    return [];
  }
}

const tankSpecs = readTankSpecs();

function dimensionsLabel(spec) {
  return `${spec.lengthInches} x ${spec.widthInches} x ${spec.heightInches} in`;
}

function chartMaximumHeight(spec) {
  return spec.gaugeChart.at(-1).liquidHeightInches;
}

function interpolateChart(spec, height) {
  const chart = spec.gaugeChart;
  const bounded = Math.max(chart[0].liquidHeightInches, Math.min(chartMaximumHeight(spec), height));
  const upperIndex = chart.findIndex((point) => point.liquidHeightInches >= bounded);
  if (upperIndex <= 0) return chart[0].gallons;
  const lower = chart[upperIndex - 1];
  const upper = chart[upperIndex];
  if (upper.liquidHeightInches === bounded) return upper.gallons;
  const position = (bounded - lower.liquidHeightInches) / (upper.liquidHeightInches - lower.liquidHeightInches);
  return lower.gallons + position * (upper.gallons - lower.gallons);
}

function eventPayload(root, eventType, scenario = "records_first", partnerType = "sweep_or_locate") {
  return {
    eventType,
    pageId: root.dataset.pageId,
    pagePath: root.dataset.pagePath,
    stateSlug: "national",
    routeFamily: root.dataset.toolId,
    scenario,
    partnerType,
    element: root.dataset.toolId,
    referrer: document.referrer,
    toolId: root.dataset.toolId
  };
}

function postEvent(payload) {
  const body = new URLSearchParams();
  Object.entries(payload).forEach(([key, value]) => body.set(key, value || ""));
  fetch("/api/leads/event", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8" },
    body,
    credentials: "same-origin",
    keepalive: true
  }).catch(() => {});
  if (typeof window.gtag === "function") {
    window.gtag("event", payload.eventType, {
      page_id: payload.pageId,
      page_path: payload.pagePath,
      tool_id: payload.toolId,
      risk_band: payload.riskBand || "",
      commercial_intent: payload.commercialIntent || ""
    });
  }
}

function saveSession(toolId, values) {
  try {
    sessionStorage.setItem(`oilTankRoute:${toolId}`, JSON.stringify(values));
    sessionStorage.setItem("oilTankRoute:lastContext", JSON.stringify({ toolId, ...values }));
  } catch (_) {
    // Calculators still work when browser storage is blocked.
  }
}

function loadLastContext() {
  try {
    return JSON.parse(sessionStorage.getItem("oilTankRoute:lastContext") || "null");
  } catch (_) {
    return null;
  }
}

function loadSession(toolId) {
  try {
    return JSON.parse(sessionStorage.getItem(`oilTankRoute:${toolId}`) || "null");
  } catch (_) {
    return null;
  }
}

function announceResult(root, heading, bodyHtml, primary) {
  const result = root.querySelector("[data-tool-result]");
  result.hidden = false;
  const headingNode = result.querySelector("[data-result-heading]");
  if (headingNode) headingNode.textContent = heading;
  const body = result.querySelector("[data-result-body]");
  if (body) body.innerHTML = bodyHtml;
  const primaryNode = result.querySelector("[data-result-primary]");
  if (primaryNode && primary !== undefined) primaryNode.textContent = primary;
  result.focus({ preventScroll: true });
  result.scrollIntoView({ behavior: window.matchMedia("(prefers-reduced-motion: reduce)").matches ? "auto" : "smooth", block: "nearest" });
}

function bindLifecycle(root, form) {
  let started = false;
  form.addEventListener("focusin", () => {
    if (started) return;
    started = true;
    postEvent(eventPayload(root, "tool_start"));
  }, { once: true });
}

function finish(root, context = {}) {
  postEvent({ ...eventPayload(root, "tool_complete", context.scenario, context.partnerType), ...context });
  postEvent({ ...eventPayload(root, "result_view", context.scenario, context.partnerType), ...context });
}

function bindIdentifier(root) {
  const form = root.querySelector("[data-identifier-form]");
  if (!form) return;
  bindLifecycle(root, form);
  form.addEventListener("submit", (event) => {
    event.preventDefault();
    const values = Object.fromEntries(new FormData(form));
    const matches = tankSpecs.filter((spec) =>
      (values.capacity === "any" || spec.nominalCapacityGallons === Number(values.capacity)) &&
      (values.orientation === "any" || spec.orientation === values.orientation)
    );
    const list = matches.length
      ? `<ul class="result-list">${matches.map((spec) => `<li><strong>${spec.manufacturer} ${spec.model}</strong><span>${spec.nominalCapacityGallons} gal / ${spec.orientation.toLowerCase()} / ${dimensionsLabel(spec)}</span></li>`).join("")}</ul><p>Confirm one of these candidates against the tank label before using a gauge chart.</p>`
      : "<p>No reviewed model matches both selections. Recheck the label and dimensions; do not force a match.</p>";
    saveSession(root.dataset.toolId, { ...values, candidateId: matches.length === 1 ? matches[0].id : "" });
    announceResult(root, `${matches.length} verified candidate${matches.length === 1 ? "" : "s"}`, list);
    finish(root);
  });
}

function bindGauge(root) {
  const form = root.querySelector("[data-gauge-form]");
  if (!form) return;
  bindLifecycle(root, form);
  const type = form.elements.readingType;
  const reading = form.elements.reading;
  const tank = form.elements.tankId;
  const prior = loadLastContext();
  if (prior?.candidateId && [...form.elements.tankId.options].some((option) => option.value === prior.candidateId)) {
    form.elements.tankId.value = prior.candidateId;
  }
  const syncReading = () => {
    const fraction = type.value === "fraction";
    const selected = tankSpecs.find((spec) => spec.id === tank.value);
    const maximum = selected ? chartMaximumHeight(selected) : 44;
    reading.max = fraction ? "1" : String(maximum);
    reading.step = fraction ? "0.125" : "0.1";
    reading.value = fraction ? "0.25" : String(maximum / 4);
  };
  type.addEventListener("change", syncReading);
  tank.addEventListener("change", syncReading);
  syncReading();
  form.addEventListener("submit", (event) => {
    event.preventDefault();
    const values = Object.fromEntries(new FormData(form));
    const spec = tankSpecs.find((candidate) => candidate.id === values.tankId);
    if (!spec?.gaugeChart?.length) {
      announceResult(root, "Choose a verified chart", "<p>The selected tank has no chart data on this route.</p>");
      return;
    }
    const raw = Number(values.reading);
    const maximumHeight = chartMaximumHeight(spec);
    const height = values.readingType === "fraction" ? raw * maximumHeight : raw;
    if (!Number.isFinite(height) || height < 0 || height > maximumHeight) {
      announceResult(root, "Check the reading", `<p>Enter a gauge fraction from 0 to 1 or a stick depth from 0 to ${maximumHeight} inches.</p>`);
      return;
    }
    const gallons = interpolateChart(spec, height);
    const fill = Math.max(0, spec.nominalCapacityGallons * 0.9 - gallons);
    saveSession(root.dataset.toolId, {
      ...values,
      candidateId: spec.id,
      gallonsRemaining: Math.round(gallons * 10) / 10,
      nominalCapacityGallons: spec.nominalCapacityGallons
    });
    announceResult(
      root,
      "Estimated gallons remaining",
      `<dl class="result-metrics"><div><dt>Planning fill to 90%</dt><dd>${Math.round(fill)} gal</dd></div><div><dt>Chart height used</dt><dd>${height.toFixed(1)} in</dd></div></dl><p>Interpolated between the selected ${spec.manufacturer} chart rows. Manufacturer-stated chart precision is about +/-2%; gauge, stick, tank tilt, and installation add uncertainty. Do not use this result to authorize a delivery.</p><p class="result-action"><a href="/heating-oil-usage-calculator/">Estimate how many days this amount may last &rarr;</a></p>`,
      Math.round(gallons)
    );
    finish(root);
  });
}

const PASSPORT_KEY = "oilTankRoute:tankPassport:v1";

function readingHeight(spec, readingType, raw) {
  return readingType === "fraction" ? raw * chartMaximumHeight(spec) : raw;
}

function deliveryRange(spec, readingType, beforeRaw, afterRaw) {
  const maximumHeight = chartMaximumHeight(spec);
  const beforeHeight = readingHeight(spec, readingType, beforeRaw);
  const afterHeight = readingHeight(spec, readingType, afterRaw);
  const readingUncertainty = readingType === "fraction" ? maximumHeight / 16 : 0.25;
  const bounds = (height) => ({
    low: interpolateChart(spec, Math.max(0, height - readingUncertainty)) * 0.98,
    high: interpolateChart(spec, Math.min(maximumHeight, height + readingUncertainty)) * 1.02
  });
  const before = bounds(beforeHeight);
  const after = bounds(afterHeight);
  return {
    beforeHeight,
    afterHeight,
    beforeGallons: interpolateChart(spec, beforeHeight),
    afterGallons: interpolateChart(spec, afterHeight),
    center: interpolateChart(spec, afterHeight) - interpolateChart(spec, beforeHeight),
    low: Math.max(0, after.low - before.high),
    high: Math.max(0, after.high - before.low)
  };
}

function deliveryAssessment(ticket, range) {
  if (ticket >= range.low && ticket <= range.high) return {
    band: "consistent",
    heading: "Ticket is consistent with this tank estimate",
    copy: "The ticket falls inside the chart-and-reading range. Keep the printed ticket as the transaction record and this result as a planning cross-check."
  };
  const distance = ticket < range.low ? range.low - ticket : ticket - range.high;
  if (distance <= 10) return {
    band: "recheck",
    heading: "Close, but recheck the readings",
    copy: "The ticket sits just outside the estimated range. Let the tank settle, confirm the model and orientation, and repeat the reading before drawing a conclusion."
  };
  return {
    band: "outside",
    heading: "Outside this tank-reading estimate",
    copy: "Recheck the tank model, orientation, before-and-after readings, and printed ticket. A tank estimate does not override a certified meter and this result does not establish a delivery error."
  };
}

function loadPassport() {
  try {
    const parsed = JSON.parse(localStorage.getItem(PASSPORT_KEY) || "null");
    return parsed?.version === 1 && Array.isArray(parsed.entries) ? parsed : { version: 1, tankId: "", entries: [] };
  } catch (_) {
    return { version: 1, tankId: "", entries: [] };
  }
}

function storePassport(passport) {
  try {
    localStorage.setItem(PASSPORT_KEY, JSON.stringify(passport));
    return true;
  } catch (_) {
    return false;
  }
}

function renderPassport(root, passport) {
  const identity = root.querySelector("[data-passport-identity]");
  const history = root.querySelector("[data-passport-history]");
  if (!identity || !history) return;
  identity.replaceChildren();
  history.replaceChildren();
  const spec = tankSpecs.find((candidate) => candidate.id === passport.tankId);
  if (!spec || !passport.entries.length) {
    const empty = document.createElement("p");
    empty.textContent = "No delivery checks saved on this device yet.";
    identity.append(empty);
    return;
  }
  const model = document.createElement("strong");
  model.textContent = `${spec.manufacturer} ${spec.model}`;
  const details = document.createElement("span");
  details.textContent = `${spec.nominalCapacityGallons} gal / ${spec.orientation.toLowerCase()} / ${dimensionsLabel(spec)}`;
  const source = document.createElement("small");
  source.textContent = `Chart verified ${spec.verifiedOn} / ${spec.officialSourceTitle}`;
  identity.append(model, details, source);
  passport.entries.forEach((entry) => {
    const item = document.createElement("li");
    const date = document.createElement("time");
    date.dateTime = entry.date;
    date.textContent = entry.date;
    const summary = document.createElement("strong");
    summary.textContent = `${entry.ticketGallons.toFixed(1)} gal ticket / ${entry.band}`;
    const range = document.createElement("span");
    range.textContent = `Chart range ${entry.expectedLow.toFixed(1)}-${entry.expectedHigh.toFixed(1)} gal / ${entry.method === "stick" ? "stick" : "float gauge"}`;
    item.append(date, summary, range);
    history.append(item);
  });
}

function bindDelivery(root) {
  const form = root.querySelector("[data-delivery-form]");
  if (!form) return;
  bindLifecycle(root, form);
  const type = form.elements.readingType;
  const tank = form.elements.tankId;
  const before = form.elements.beforeReading;
  const after = form.elements.afterReading;
  const help = root.querySelector("[data-delivery-help]");
  const passportRoot = root.querySelector("[data-tank-passport]");
  let passport = loadPassport();
  const gauge = loadSession("gauge-calculator");
  if (gauge?.candidateId && [...tank.options].some((option) => option.value === gauge.candidateId)) {
    tank.value = gauge.candidateId;
  } else if (passport.tankId && [...tank.options].some((option) => option.value === passport.tankId)) {
    tank.value = passport.tankId;
  }
  if (gauge?.readingType && Number.isFinite(Number(gauge.reading))) {
    type.value = gauge.readingType;
    before.value = gauge.reading;
  }
  const localDate = new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 10);
  form.elements.deliveryDate.value = localDate;
  const syncReadings = () => {
    const spec = tankSpecs.find((candidate) => candidate.id === tank.value);
    const fraction = type.value === "fraction";
    const maximum = spec ? chartMaximumHeight(spec) : 44;
    [before, after].forEach((input) => {
      input.min = "0";
      input.max = fraction ? "1" : String(maximum);
      input.step = fraction ? "0.125" : "0.1";
    });
    root.querySelector("[data-before-unit]").textContent = fraction ? "0 to 1" : "inches";
    root.querySelector("[data-after-unit]").textContent = fraction ? "0 to 1" : "inches";
    help.textContent = fraction
      ? "Enter decimals such as 0.25 and 0.75. Float-gauge divisions create a wider comparison range."
      : "A clean stick depth offers a narrower planning range. Do not open or probe a tank when a leak or unsafe condition is suspected.";
    if (fraction && (Number(before.value) > 1 || Number(after.value) > 1)) {
      before.value = "0.25";
      after.value = "0.75";
    } else if (!fraction && Number(after.value) <= 1) {
      before.value = (maximum * 0.25).toFixed(1);
      after.value = (maximum * 0.8).toFixed(1);
    }
  };
  const invalidateAndSync = () => {
    const result = root.querySelector("[data-tool-result]");
    if (result) result.hidden = true;
    root.querySelector("[data-status-light]")?.removeAttribute("data-status");
    syncReadings();
  };
  type.addEventListener("change", invalidateAndSync);
  tank.addEventListener("change", invalidateAndSync);
  syncReadings();
  renderPassport(passportRoot, passport);
  form.addEventListener("submit", (event) => {
    event.preventDefault();
    const values = Object.fromEntries(new FormData(form));
    const spec = tankSpecs.find((candidate) => candidate.id === values.tankId);
    const beforeRaw = Number(values.beforeReading);
    const afterRaw = Number(values.afterReading);
    const ticket = Number(values.ticketGallons);
    const maximum = spec ? (values.readingType === "fraction" ? 1 : chartMaximumHeight(spec)) : 0;
    if (!spec?.gaugeChart?.length || ![beforeRaw, afterRaw, ticket].every(Number.isFinite) || beforeRaw < 0 || afterRaw > maximum || afterRaw <= beforeRaw || ticket <= 0) {
      announceResult(root, "Check the delivery values", "<p>Choose a chart-backed tank, enter an after reading greater than the before reading, and use a positive ticket quantity within the displayed limits.</p>");
      return;
    }
    const range = deliveryRange(spec, values.readingType, beforeRaw, afterRaw);
    const outcome = deliveryAssessment(ticket, range);
    const difference = ticket - range.center;
    const precisionCopy = values.readingType === "stick" ? "+/-0.25 inch reading resolution" : "+/-1/16 tank-height gauge resolution";
    announceResult(
      root,
      outcome.heading,
      `<dl class="result-metrics"><div><dt>Ticket quantity</dt><dd>${ticket.toFixed(1)} gal</dd></div><div><dt>Chart center</dt><dd>${range.center.toFixed(1)} gal</dd></div><div><dt>Expected range</dt><dd>${range.low.toFixed(1)}-${range.high.toFixed(1)} gal</dd></div></dl><p>${outcome.copy}</p><p class="result-method">Range uses ${precisionCopy} and the chart's stated +/-2% precision.</p>`,
      `${difference >= 0 ? "+" : ""}${difference.toFixed(1)}`
    );
    root.querySelector("[data-delivery-band]").textContent = `Assessment / ${outcome.band}`;
    root.querySelector("[data-status-light]").dataset.status = outcome.band;
    const existingEntries = passport.tankId === spec.id ? passport.entries : [];
    passport = {
      version: 1,
      tankId: spec.id,
      entries: [{
        date: values.deliveryDate,
        method: values.readingType,
        ticketGallons: ticket,
        expectedLow: range.low,
        expectedHigh: range.high,
        band: outcome.band
      }, ...existingEntries.filter((entry) => entry.date !== values.deliveryDate)].slice(0, 12)
    };
    const stored = storePassport(passport);
    renderPassport(passportRoot, passport);
    if (!stored) {
      const privacy = passportRoot.querySelector(".passport-privacy");
      privacy.textContent = "This browser blocked local storage. The comparison still works, but the passport could not be saved.";
    }
    saveSession(root.dataset.toolId, { candidateId: spec.id, lastBand: outcome.band });
    finish(root, { riskBand: outcome.band, commercialIntent: "none" });
  });
  root.querySelector("[data-passport-print]")?.addEventListener("click", () => window.print());
  root.querySelector("[data-passport-clear]")?.addEventListener("click", () => {
    if (!passport.entries.length || !window.confirm("Clear the Tank Passport history stored in this browser?")) return;
    passport = { version: 1, tankId: "", entries: [] };
    try { localStorage.removeItem(PASSPORT_KEY); } catch (_) { /* Nothing else to clear. */ }
    renderPassport(passportRoot, passport);
  });
}

function bindUsage(root) {
  const form = root.querySelector("[data-usage-form]");
  if (!form) return;
  bindLifecycle(root, form);
  const mode = form.elements.rateMode;
  const panels = root.querySelectorAll("[data-rate-panel]");
  const syncMode = () => {
    panels.forEach((panel) => {
      const active = panel.dataset.ratePanel === mode.value;
      panel.hidden = !active;
      panel.querySelectorAll("input").forEach((input) => input.required = active);
    });
  };
  mode.addEventListener("change", syncMode);
  syncMode();
  const gauge = loadSession("gauge-calculator");
  if (Number.isFinite(Number(gauge?.gallonsRemaining))) {
    form.elements.remainingGallons.value = gauge.gallonsRemaining;
  }
  form.addEventListener("submit", (event) => {
    event.preventDefault();
    const values = Object.fromEntries(new FormData(form));
    const remaining = Number(values.remainingGallons);
    const reserve = Number(values.reserveGallons);
    const daily = values.rateMode === "history"
      ? Number(values.gallonsUsed) / Number(values.daysElapsed)
      : Number(values.dailyUse);
    if (![remaining, reserve, daily].every(Number.isFinite) || remaining < 0 || reserve < 0 || daily <= 0) {
      announceResult(root, "Check the usage values", "<p>Remaining gallons and reserve must be zero or more, and the calculated daily use must be greater than zero.</p>");
      return;
    }
    const daysToEmpty = remaining / daily;
    const daysToReserve = Math.max(0, (remaining - reserve) / daily);
    const monthly = daily * 30;
    saveSession(root.dataset.toolId, { ...values, dailyUseCalculated: Math.round(daily * 100) / 100, daysToReserve: Math.round(daysToReserve * 10) / 10 });
    announceResult(
      root,
      remaining <= reserve ? "You are already inside the chosen reserve" : "Estimated days until reserve",
      `<dl class="result-metrics"><div><dt>Daily rate used</dt><dd>${daily.toFixed(2)} gal/day</dd></div><div><dt>Days to empty</dt><dd>${daysToEmpty.toFixed(1)}</dd></div><div><dt>30-day use at this rate</dt><dd>${monthly.toFixed(0)} gal</dd></div></dl><p>This is a straight consumption runway based on your rate, not a weather forecast. Delivery timing, burner condition, hot-water use, and changing temperatures can shift actual use.</p>`,
      daysToReserve.toFixed(1)
    );
    finish(root);
  });
}

function shapeGallons(shape, length, width, height) {
  if (shape === "rectangular") return length * width * height * GALLONS_PER_CUBIC_INCH;
  if (shape === "cylinder") {
    const diameter = Math.min(width, height);
    return Math.PI * (diameter / 2) ** 2 * length * GALLONS_PER_CUBIC_INCH;
  }
  const shortAxis = Math.min(width, height);
  const longAxis = Math.max(width, height);
  const area = shortAxis * (longAxis - shortAxis) + Math.PI * (shortAxis / 2) ** 2;
  return area * length * GALLONS_PER_CUBIC_INCH;
}

function bindCapacity(root) {
  const form = root.querySelector("[data-capacity-form]");
  if (!form) return;
  bindLifecycle(root, form);
  const prior = loadLastContext();
  const candidate = prior?.candidateId ? tankSpecs.find((spec) => spec.id === prior.candidateId) : null;
  if (candidate) {
    form.elements.length.value = candidate.lengthInches;
    form.elements.width.value = candidate.widthInches;
    form.elements.height.value = candidate.heightInches;
  }
  form.addEventListener("submit", (event) => {
    event.preventDefault();
    const values = Object.fromEntries(new FormData(form));
    const dimensions = ["length", "width", "height"].map((name) => Number(values[name]));
    if (dimensions.some((value) => !Number.isFinite(value) || value <= 0)) {
      announceResult(root, "Check the dimensions", "<p>All three tank-body dimensions must be positive numbers.</p>");
      return;
    }
    const [length, width, height] = dimensions;
    const estimate = shapeGallons(values.shape, length, width, height);
    const minimum = shapeGallons(values.shape, Math.max(0.1, length - 0.5), Math.max(0.1, width - 0.5), Math.max(0.1, height - 0.5));
    const maximum = shapeGallons(values.shape, length + 0.5, width + 0.5, height + 0.5);
    saveSession(root.dataset.toolId, values);
    announceResult(root, "Estimated capacity range", `<dl class="result-metrics"><div><dt>Center estimate</dt><dd>${Math.round(estimate)} gal</dd></div><div><dt>Measurement range</dt><dd>${Math.round(minimum)}-${Math.round(maximum)} gal</dd></div></dl><p>Range reflects +/-0.5 inch on each external measurement. It excludes wall thickness, fittings, internal geometry, and fill headspace. Match the label before selecting a chart.</p>`, `${Math.round(minimum)}-${Math.round(maximum)}`);
    finish(root);
  });
}

function plannerOutcome(values) {
  if (values.leak || values.wetSoil || values.odor) return {
    band: "urgent", heading: "Stop and protect people and property",
    copy: "Do not touch, drain, or test the tank. Keep ignition sources away. If an active release may be present, contact the local fire department or environmental spill line and a qualified remediation professional.",
    scenario: "leak_concern", partner: "environmental_cleanup", intent: "leak_remediation", cta: "Leak or remediation help"
  };
  if (values.sale && (values.underground || values.unknown)) return {
    band: "transaction", heading: "Verify the tank before the deal moves",
    copy: "Keep the question inside due diligence. Start with records and a qualified tank sweep or locate; move to removal only after the tank and state path are confirmed.",
    scenario: "sweep_first", partner: "sweep_or_locate", intent: "sweep_removal", cta: "Sweep or removal evaluation"
  };
  if (Number(values.age) >= 20 || values.rust || values.seepage || values.unstable || values.usageSpike) return {
    band: "inspection", heading: "Plan an inspection or replacement review",
    copy: "Age, corrosion, or indoor seepage deserves a qualified heating-oil tank inspection. A replacement contractor can confirm condition and applicable requirements before work is priced.",
    scenario: "removal_decision", partner: "closure_or_removal", intent: "inspection_replacement", cta: "Inspection or replacement help"
  };
  if (values.serviceIssue) return {
    band: "information", heading: "Start with heating-system service",
    copy: "A gauge, vent whistle, or fuel-line issue by itself does not establish that the tank needs removal. Ask a qualified heating-oil service provider to diagnose the component and inspect the tank condition."
  };
  return {
    band: "information", heading: "Monitor and keep the tank documented",
    copy: "No commercial trigger is indicated by these answers. Keep checking the gauge, protect the fill and vent, and schedule routine service with your heating provider."
  };
}

function sludgeOutcome(values) {
  if (values.odor || values.wetness || values.visibleOil || values.oilySoil || values.seepage) return {
    band: "urgent", heading: "Treat this as a possible oil release",
    copy: "Do not open, drain, pump, move, or test the tank. Keep ignition sources away. Contact the applicable local fire or spill-response authority and a qualified environmental professional; tell the fuel supplier what you observed.",
    scenario: "leak_concern", partner: "environmental_cleanup", intent: "leak_remediation", cta: "Leak or remediation help"
  };
  if (values.rust || values.unstable) return {
    band: "inspection", heading: "Inspect the tank condition before cleaning",
    copy: "Cleaning cannot restore steel lost to corrosion or correct unstable supports. Ask a qualified heating-oil tank professional to inspect the tank body, supports, piping, filter, and water or sediment source before pricing cleaning or replacement.",
    scenario: "removal_decision", partner: "closure_or_removal", intent: "inspection_replacement", cta: "Tank inspection or replacement help"
  };
  if (values.filterClogs || values.burnerStops || values.waterDebris) return {
    band: "service", heading: "Start with a heating-oil service diagnosis",
    copy: "Repeated plugging, shutdowns, or confirmed water or debris can involve the filter, line, burner, fuel condition, and tank. Ask a qualified service technician to diagnose the full system and specify whether fuel treatment, professional tank service, or tank-condition inspection is actually needed."
  };
  if (values.oneServiceIssue) return {
    band: "information", heading: "Service the isolated symptom first",
    copy: "One filter or burner issue does not prove a sludge problem or justify tank replacement. Start with qualified heating-system service and keep watching for recurrence, corrosion, odor, wetness, or visible oil."
  };
  return {
    band: "information", heading: "Do not order cleaning without a diagnosed problem",
    copy: "No cleaning, corrosion, or release trigger is indicated by these answers. Keep the annual tank and system inspection current and ask the service provider to document any water, debris, filter, line, or tank-condition finding."
  };
}

function bindSludge(root) {
  const form = root.querySelector("[data-sludge-form]");
  if (!form) return;
  bindLifecycle(root, form);
  const symptomNames = ["filterClogs", "burnerStops", "waterDebris", "oneServiceIssue", "rust", "unstable", "seepage", "odor", "wetness", "visibleOil", "oilySoil"];
  form.addEventListener("submit", (event) => {
    event.preventDefault();
    const data = new FormData(form);
    const values = {};
    symptomNames.forEach((name) => values[name] = data.has(name));
    const outcome = sludgeOutcome(values);
    saveSession(root.dataset.toolId, values);
    announceResult(root, outcome.heading, `<p>${outcome.copy}</p>`);
    const band = root.querySelector("[data-risk-band]");
    if (band) band.textContent = `Route / ${outcome.band}`;
    const context = { scenario: outcome.scenario || "records_first", partnerType: outcome.partner || "sweep_or_locate", riskBand: outcome.band, commercialIntent: outcome.intent || "none" };
    finish(root, context);
    bindCommercialResult(root, outcome);
  });
  const cta = root.querySelector("[data-result-cta]");
  if (cta) cta.addEventListener("click", () => {
    const commercial = root.querySelector("[data-commercial-route]");
    postEvent({
      ...eventPayload(root, "result_cta_click", commercial.querySelector("[data-lead-scenario]").value, commercial.querySelector("[data-lead-partner]").value),
      riskBand: commercial.querySelector("[data-lead-risk]").value,
      commercialIntent: commercial.querySelector("[data-lead-intent]").value
    });
  });
}

function bindPlanner(root) {
  const form = root.querySelector("[data-planner-form]");
  if (!form) return;
  bindLifecycle(root, form);
  const commercial = root.querySelector("[data-commercial-route]");
  form.addEventListener("submit", (event) => {
    event.preventDefault();
    const data = new FormData(form);
    const values = { age: data.get("age") || "0" };
    ["rust", "seepage", "unstable", "usageSpike", "serviceIssue", "odor", "wetSoil", "leak", "sale", "underground", "unknown"].forEach((name) => values[name] = data.has(name));
    const outcome = plannerOutcome(values);
    saveSession(root.dataset.toolId, values);
    announceResult(root, outcome.heading, `<p>${outcome.copy}</p>`);
    const band = root.querySelector("[data-risk-band]");
    if (band) band.textContent = `Risk band / ${outcome.band}`;
    const eventContext = { scenario: outcome.scenario || "records_first", partnerType: outcome.partner || "sweep_or_locate", riskBand: outcome.band, commercialIntent: outcome.intent || "none" };
    finish(root, eventContext);
    if (!outcome.intent) {
      commercial.hidden = true;
      return;
    }
    commercial.hidden = false;
    commercial.querySelector("[data-commercial-heading]").textContent = outcome.cta;
    commercial.querySelector("[data-lead-scenario]").value = outcome.scenario;
    commercial.querySelector("[data-lead-partner]").value = outcome.partner;
    commercial.querySelector("[data-lead-risk]").value = outcome.band;
    commercial.querySelector("[data-lead-intent]").value = outcome.intent;
    commercial.querySelector("[data-lead-summary]").value = outcome.heading;
    postEvent({ ...eventPayload(root, "commercial_trigger", outcome.scenario, outcome.partner), riskBand: outcome.band, commercialIntent: outcome.intent });
  });
  const cta = root.querySelector("[data-result-cta]");
  if (cta) cta.addEventListener("click", () => {
    const scenario = commercial.querySelector("[data-lead-scenario]").value;
    const partner = commercial.querySelector("[data-lead-partner]").value;
    postEvent({ ...eventPayload(root, "result_cta_click", scenario, partner), riskBand: commercial.querySelector("[data-lead-risk]").value, commercialIntent: commercial.querySelector("[data-lead-intent]").value });
  });
}

function scopeList(items) {
  return `<ol class="scope-result-list">${items.map((item) => `<li><span>${item.label}</span><p>${item.copy}</p></li>`).join("")}</ol>`;
}

function bindScopeRouteClick(root, context) {
  const link = root.querySelector("[data-result-route]");
  if (!link) return;
  link.addEventListener("click", () => postEvent({
    ...eventPayload(root, "result_cta_click", context.scenario, context.partnerType),
    riskBand: context.riskBand,
    commercialIntent: context.commercialIntent
  }), { once: true });
}

function bindReplacementCost(root) {
  const form = root.querySelector("[data-replacement-cost-form]");
  if (!form) return;
  bindLifecycle(root, form);
  form.addEventListener("submit", (event) => {
    event.preventDefault();
    const data = new FormData(form);
    const values = Object.fromEntries(data);
    ["baseWork", "pipingWork", "permitUnknown", "odor", "staining", "visibleOil"].forEach((name) => values[name] = data.has(name));
    saveSession(root.dataset.toolId, values);
    if (values.odor || values.staining || values.visibleOil) {
      const context = { scenario: "leak_concern", partnerType: "environmental_cleanup", riskBand: "urgent", commercialIntent: "leak_remediation" };
      announceResult(root, "Separate possible release work from replacement", `<p>Do not treat odor, staining, wet soil, or visible oil as an ordinary replacement allowance. Avoid touching or draining the tank, use the applicable local spill-response path, and ask a qualified environmental professional to define investigation or remediation separately.</p><p><a class="otr-button" data-result-route href="/guides/leaking-heating-oil-tank-what-to-do/">Open leak-response steps</a></p>`);
      const band = root.querySelector("[data-risk-band]");
      if (band) band.textContent = "Risk band / urgent";
      finish(root, context);
      bindScopeRouteClick(root, context);
      return;
    }
    const items = [
      { label: "New system", copy: "Name the manufacturer, model, capacity, tank construction, included accessories, installation labor, and warranty conditions." },
      { label: "Old tank", copy: values.fuel === "empty" ? "Confirm empty status, cleaning, removal or closure, disposal, and documentation." : "Confirm pump-out, usable-fuel disposition, sludge handling, cleaning, removal or closure, and documentation." },
      { label: "Access", copy: values.access === "restricted" ? "Price the restricted interior route, finished-area protection, and whether the tank must leave in sections." : values.access === "excavation" || values.location === "underground" ? "Price excavation, utility/site protection, closure or removal method, backfill, and surface restoration." : "Confirm delivery and removal routes plus protection of the home and site." },
      { label: "Base and piping", copy: `${values.baseWork ? "Include base or support correction. " : "State whether the existing base or supports are accepted. "}${values.pipingWork ? "Include fill, vent, overfill, gauge, filter, and supply-line changes." : "State which fill, vent, overfill, gauge, filter, and supply components are reused or replaced."}` },
      { label: "Permit and closeout", copy: values.permitUnknown ? "Identify the authority, permit, inspection, licensed trades, tests, photos, receipts, and completion records." : "List the confirmed permit, inspection, testing, and completion documents in the bid." }
    ];
    if (values.trigger === "sale") items.push({ label: "Property record", copy: "Keep permit, invoice, disposal, inspection, photos, and any sampling or cleanup records together for the transaction file." });
    const context = { scenario: "removal_decision", partnerType: "closure_or_removal", riskBand: "project", commercialIntent: "replacement_cost_scope" };
    announceResult(root, "Compare bids on the same six boundaries", `${scopeList(items)}<p class="scope-result-note">No price is calculated because the inputs define scope, not local labor, equipment quotes, permit fees, or discovered conditions.</p><p><a class="otr-button" data-result-route href="/heating-oil-tank-installation-cost/">Scope the new installation</a></p>`);
    const band = root.querySelector("[data-risk-band]");
    if (band) band.textContent = "Quote scope / project";
    finish(root, context);
    bindScopeRouteClick(root, context);
  });
}

function bindInstallationCost(root) {
  const form = root.querySelector("[data-installation-cost-form]");
  if (!form) return;
  bindLifecycle(root, form);
  form.addEventListener("submit", (event) => {
    event.preventDefault();
    const data = new FormData(form);
    const values = Object.fromEntries(data);
    ["oldTank", "piping", "permitUnknown", "odor", "staining", "visibleOil"].forEach((name) => values[name] = data.has(name));
    saveSession(root.dataset.toolId, values);
    if (values.odor || values.staining || values.visibleOil) {
      const context = { scenario: "leak_concern", partnerType: "environmental_cleanup", riskBand: "urgent", commercialIntent: "leak_remediation" };
      announceResult(root, "Resolve the possible release before ordinary installation", `<p>Odor, staining, wet soil, or visible oil can change the contractor, reporting, and site work. Keep investigation or remediation outside the routine installation total and do not move or drain the existing tank yourself.</p><p><a class="otr-button" data-result-route href="/guides/leaking-heating-oil-tank-what-to-do/">Open leak-response steps</a></p>`);
      const band = root.querySelector("[data-risk-band]");
      if (band) band.textContent = "Risk band / urgent";
      finish(root, context);
      bindScopeRouteClick(root, context);
      return;
    }
    const design = values.design === "unknown" ? "Require an exact manufacturer and model before accepting tank-specific installation assumptions." : values.design === "doubleWall" ? "Name the exact double-wall model, listed accessories, trained-installer or warranty conditions, and secondary-containment requirements." : "Name the exact steel obround model and apply only that manufacturer's installation instructions.";
    const location = values.location === "outdoor" ? "Price drainage, level site preparation, approved cover or weather protection, anchoring or tie-down where required, and inspection access." : values.location === "indoor" ? "Price the supporting surface, required clearances, visual inspection access, and protected delivery route." : "Confirm indoor or outdoor location before applying site and accessory requirements.";
    const items = [
      { label: "Tank and accessories", copy: design },
      { label: "Location and base", copy: `${location} ${values.base === "new" ? "Include a new supporting base." : values.base === "existing" ? "State who verifies the existing base is suitable." : "Keep base review as an explicit allowance."}` },
      { label: "Delivery and access", copy: values.access === "restricted" ? "Price restricted doors, stairs, handling, finished-area protection, and any alternate placement method." : "Confirm freight, handling, placement route, and protection of the property." },
      { label: "Piping and protection", copy: values.piping ? "Price new fill, vent, overfill protection, gauge, filter, supply line, valves, connections, and tests." : "State which fill, vent, overfill, gauge, filter, supply, valve, and connection components are included, reused, or excluded." },
      { label: "Permit and closeout", copy: values.permitUnknown ? "Identify the local authority, permit, inspection, licensed trades, completion certificate, startup tests, and homeowner documents." : "List the confirmed permit, inspection, startup test, and completion documents." },
      { label: "Existing tank", copy: values.oldTank ? "Keep pump-out, fuel and sludge handling, removal or closure, disposal, and discovered-release work as visible line items." : "Confirm that no old-tank work is included so the installed total remains comparable." }
    ];
    const context = { scenario: "records_first", partnerType: "sweep_or_locate", riskBand: "project", commercialIntent: "installation_cost_scope" };
    announceResult(root, "Ask every installer to price the same six parts", `${scopeList(items)}<p class="scope-result-note">The result is a scope comparator, not a price estimate. Local labor, selected equipment, authority fees, and site discoveries require written quotes.</p><p><a class="otr-button" data-result-route href="/heating-oil-tank-sizes-dimensions/">Compare verified tank models</a></p>`);
    const band = root.querySelector("[data-risk-band]");
    if (band) band.textContent = "Installation scope / project";
    finish(root, context);
    bindScopeRouteClick(root, context);
  });
}

function removalOutcome(values) {
  if (values.odor || values.staining || values.visibleOil || values.seepage) return {
    band: "urgent", heading: "Treat this as a possible release, not routine removal",
    copy: "Do not drain, cut, move, or test the tank yourself. Keep ignition sources away and use the applicable local emergency or spill-reporting route plus a qualified environmental professional.",
    scenario: "leak_concern", partner: "environmental_cleanup", intent: "leak_remediation", cta: "Leak or remediation help"
  };
  const constraints = [];
  if (values.tankUse === "active") constraints.push("the tank is still in service");
  if (values.fuel !== "empty") constraints.push("fuel status needs confirmation");
  if (values.access === "narrow") constraints.push("access may require disassembly and protection of finished areas");
  if (values.access === "stairs") constraints.push("the route includes interior stairs");
  const scope = constraints.length ? constraints.join("; ") : "direct access and an empty, inactive tank were reported";
  return {
    band: "project", heading: "Request a site-specific indoor removal scope",
    copy: `Tell the contractor that ${scope}. Ask the written scope to cover fuel and sludge handling, tank cleaning, how it will leave the building, fill/vent disposition, disposal documentation, permits, and what changes if staining is found.`,
    scenario: "removal_decision", partner: "closure_or_removal", intent: "basement_removal", cta: "Basement removal evaluation"
  };
}

function bindCommercialResult(root, outcome) {
  const commercial = root.querySelector("[data-commercial-route]");
  if (!commercial) return;
  if (!outcome.intent) {
    commercial.hidden = true;
    return;
  }
  commercial.hidden = false;
  commercial.querySelector("[data-commercial-heading]").textContent = outcome.cta;
  commercial.querySelector("[data-lead-scenario]").value = outcome.scenario;
  commercial.querySelector("[data-lead-partner]").value = outcome.partner;
  commercial.querySelector("[data-lead-risk]").value = outcome.band;
  commercial.querySelector("[data-lead-intent]").value = outcome.intent;
  commercial.querySelector("[data-lead-summary]").value = outcome.heading;
  postEvent({ ...eventPayload(root, "commercial_trigger", outcome.scenario, outcome.partner), riskBand: outcome.band, commercialIntent: outcome.intent });
}

function bindRemoval(root) {
  const form = root.querySelector("[data-removal-form]");
  if (!form) return;
  bindLifecycle(root, form);
  form.addEventListener("submit", (event) => {
    event.preventDefault();
    const data = new FormData(form);
    const values = Object.fromEntries(data);
    ["odor", "staining", "visibleOil", "seepage"].forEach((name) => values[name] = data.has(name));
    const outcome = removalOutcome(values);
    saveSession(root.dataset.toolId, values);
    announceResult(root, outcome.heading, `<p>${outcome.copy}</p>`);
    const band = root.querySelector("[data-risk-band]");
    if (band) band.textContent = `Risk band / ${outcome.band}`;
    finish(root, { scenario: outcome.scenario, partnerType: outcome.partner, riskBand: outcome.band, commercialIntent: outcome.intent });
    bindCommercialResult(root, outcome);
  });
  const cta = root.querySelector("[data-result-cta]");
  if (cta) cta.addEventListener("click", () => {
    const commercial = root.querySelector("[data-commercial-route]");
    postEvent({
      ...eventPayload(root, "result_cta_click", commercial.querySelector("[data-lead-scenario]").value, commercial.querySelector("[data-lead-partner]").value),
      riskBand: commercial.querySelector("[data-lead-risk]").value,
      commercialIntent: commercial.querySelector("[data-lead-intent]").value
    });
  });
}

document.querySelectorAll("[data-tool-root]").forEach((root) => {
  bindIdentifier(root);
  bindGauge(root);
  bindDelivery(root);
  bindUsage(root);
  bindCapacity(root);
  bindSludge(root);
  bindPlanner(root);
  bindReplacementCost(root);
  bindInstallationCost(root);
  bindRemoval(root);
});

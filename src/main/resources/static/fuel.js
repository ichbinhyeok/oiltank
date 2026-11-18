const supplierFilter = document.querySelector("[data-supplier-state]");
for (const button of document.querySelectorAll("[data-print-chart]")) button.addEventListener("click", () => window.print());
supplierFilter?.addEventListener("change", () => {
  let count = 0;
  for (const supplier of document.querySelectorAll("[data-supplier-states]")) {
    const visible = supplierFilter.value === "all" || supplier.dataset.supplierStates.split(" ").includes(supplierFilter.value);
    supplier.hidden = !visible;
    if (visible) count++;
  }
  document.querySelector("[data-supplier-count]").textContent = `${count} selected supplier${count === 1 ? "" : "s"}; confirm your town directly.`;
});
if (supplierFilter) {
  const initialState = new URLSearchParams(location.search).get("state");
  if ([...supplierFilter.options].some(option => option.value === initialState)) {
    supplierFilter.value = initialState;
    supplierFilter.dispatchEvent(new Event("change"));
  }
}
function trackBudget(form, tool = "fuel-budget") {
  const values = new URLSearchParams({eventType:"tool_complete", pageId:form.dataset.pageId, pagePath:location.pathname,
    stateSlug:document.querySelector("[data-price-page]")?.dataset.pricePage || "national", routeFamily:tool, toolId:tool, element:"guide-calculator", scenario:"records_first", partnerType:"sweep_or_locate"});
  fetch("/api/leads/event", {method:"POST", body:values, credentials:"same-origin", keepalive:true}).catch(() => {});
  if (typeof window.gtag === "function") window.gtag("event", "tool_complete", {tool_id:tool, page_id:form.dataset.pageId, state_slug:document.querySelector("[data-price-page]")?.dataset.pricePage || "national"});
}
for (const form of document.querySelectorAll("[data-runtime-form]")) {
  form.addEventListener("submit", event => {
    event.preventDefault();
    if (!form.reportValidity()) return;
    const data = new FormData(form);
    const [remaining, reserve, daily] = ["remaining", "reserve", "daily"].map(key => Number(data.get(key)));
    const output = form.querySelector("[data-runtime-result]");
    if (![remaining, reserve, daily].every(Number.isFinite) || remaining < 0 || reserve < 0 || daily <= 0) {
      output.textContent = "Enter nonnegative gallons and reserve, and a daily use greater than zero."; return;
    }
    const usable = Math.max(0, remaining - reserve);
    output.textContent = `${usable.toFixed(1)} planning gallons ÷ ${daily} gallons/day = ${(usable / daily).toFixed(1)} planning days. ${reserve >= remaining ? "The balance is at or below your selected reserve. " : ""}Recheck the level and supplier delivery window; this is not a guaranteed run-out date.`;
    trackBudget(form, "fuel-runtime");
  });
}
for (const form of document.querySelectorAll("[data-heat-form]")) {
  form.addEventListener("submit", event => {
    event.preventDefault();
    if (!form.reportValidity()) return;
    const data = new FormData(form);
    const [oilPrice, oilEfficiency, propanePrice, propaneEfficiency] = ["oilPrice", "oilEfficiency", "propanePrice", "propaneEfficiency"].map(key => Number(data.get(key)));
    const output = form.querySelector("[data-heat-result]");
    if (![oilPrice, oilEfficiency, propanePrice, propaneEfficiency].every(Number.isFinite) || oilPrice <= 0 || propanePrice <= 0 || oilEfficiency <= 0 || oilEfficiency > 100 || propaneEfficiency <= 0 || propaneEfficiency > 100) {
      output.textContent = "Enter positive fuel prices and efficiencies from 1% through 100%."; return;
    }
    const oilHeat = 138500 * oilEfficiency / 100;
    const propaneHeat = 91452 * propaneEfficiency / 100;
    const oilCost = oilPrice * 1000000 / oilHeat;
    const propaneCost = propanePrice * 1000000 / propaneHeat;
    const difference = Math.abs(oilCost - propaneCost);
    const comparison = difference < 0.005 ? "Both are effectively equal at the displayed precision." : `${oilCost > propaneCost ? "Propane" : "Oil"} is $${difference.toFixed(2)} lower per useful million Btu under these inputs.`;
    output.textContent = `Oil: $${oilCost.toFixed(2)}; propane: $${propaneCost.toFixed(2)} per useful million Btu. ${comparison} Break-even propane price: $${(oilPrice * propaneHeat / oilHeat).toFixed(3)}/gallon. Taxes, fees and equipment costs excluded.`;
    trackBudget(form, "fuel-heat");
  });
}
for (const form of document.querySelectorAll("[data-budget-form]")) {
  form.addEventListener("submit", event => {
    event.preventDefault();
    if (!form.reportValidity()) return;
    const data = new FormData(form);
    const [gallons, price, tax, fees] = ["gallons","price","tax","fees"].map(key => Number(data.get(key)));
    const output = form.querySelector("[data-budget-result]");
    if (![gallons,price,tax,fees].every(Number.isFinite) || gallons <= 0 || price <= 0 || tax < 0 || tax > 100 || fees < 0) {
      output.textContent = "Check the gallons, price, tax and fees."; return;
    }
    const subtotal = gallons * price;
    output.textContent = `${gallons.toLocaleString("en-US")} gallons: $${(subtotal * (1 + tax / 100) + fees).toFixed(2)} total — $${subtotal.toFixed(2)} fuel + $${(subtotal * tax / 100).toFixed(2)} quoted fuel tax + $${fees.toFixed(2)} quoted fees. Confirm any tax on fees with your supplier.`;
    trackBudget(form);
  });
}

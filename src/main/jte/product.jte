@import owner.buriedoiltank.pages.ProductPageModels.ProductPageModel
@param ProductPageModel page
@template.layout.page(meta = page.meta(), activeNav = page.activeNav(), content = @`
<script id="tank-data" type="application/json">$unsafe{page.tankDataJson()}</script>
<section class="otr-hero ${page.isTool("hub") ? "otr-hero--home" : ""}">
    <div class="otr-shell otr-hero__grid">
        <div class="otr-hero__copy">
            @if (!page.breadcrumbs().isEmpty())
                @template.partials.breadcrumbs(page.breadcrumbs())
            @endif
            <p class="otr-kicker">${page.eyebrow()}</p>
            @if (page.isTool("hub"))
                <p class="otr-brand-line">Oil Tank Route</p>
            @endif
            <h1>${page.heading()}</h1>
            <p class="otr-dek">${page.intro()}</p>
            <div class="otr-actions">
                @if (page.isTool("hub"))
                    <a class="otr-button" href="/heating-oil-tank/">Identify your tank</a>
                    <a class="otr-text-link" href="/oil-tank-gauge-calculator/">Check a gauge reading <span aria-hidden="true">&rarr;</span></a>
                @else
                    @if (page.isTool("replacement-cost") || page.isTool("installation-cost"))
                        <a class="otr-button" href="#cost-scope">Build the quote scope</a>
                        <a class="otr-text-link" href="#sources">Check sources <span aria-hidden="true">&darr;</span></a>
                    @else
                        @if (page.isTool("gauge") || page.isTool("delivery") || page.isTool("usage") || page.isTool("capacity") || page.isTool("sludge") || page.isTool("planner") || page.isTool("removal") || page.isTool("identifier") || page.isTool("heating-cost") || page.isTool("order"))
                            <a class="otr-button" href="#tool">Start the tool</a>
                            <a class="otr-text-link" href="#sources">Check sources <span aria-hidden="true">&darr;</span></a>
                        @else
                            @if (page.isTool("lifespan") || page.isTool("gauge-service") || page.isTool("repair") || page.isTool("ran-out"))
                                <a class="otr-button" href="#decision">Check the decision boundary</a>
                                <a class="otr-text-link" href="#sources">Check sources <span aria-hidden="true">&darr;</span></a>
                            @else
                                @if (page.isTool("prices"))
                                    <a class="otr-button" href="#price-benchmark">Read the dated benchmark</a>
                                @else
                                    @if (page.isTool("charts"))
                                        <a class="otr-button" href="#chart-library">Open verified charts</a>
                                    @else
                                        <a class="otr-button" href="#verified-data">Compare verified data</a>
                                    @endif
                                @endif
                            @endif
                        @endif
                    @endif
                @endif
            </div>
        </div>
        <div class="tank-graphic" aria-label="Heating-oil tank cross-section with measurement and fill-level marks" role="img">
            <svg viewBox="0 0 620 430" aria-hidden="true">
                <defs><clipPath id="tankClip"><path d="M158 82h238a84 84 0 0 1 0 168H158z"/></clipPath></defs>
                <g class="tank-graphic__grid"><path d="M30 42h560M30 98h560M30 154h560M30 210h560M30 266h560M30 322h560M30 378h560"/><path d="M70 22v384M126 22v384M182 22v384M238 22v384M294 22v384M350 22v384M406 22v384M462 22v384M518 22v384"/></g>
                <path class="tank-graphic__body" d="M158 82h238a84 84 0 0 1 0 168H158z"/>
                <g clip-path="url(#tankClip)"><rect class="tank-graphic__fuel" x="150" y="177" width="350" height="80"/></g>
                <path class="tank-graphic__seam" d="M244 82v168M330 82v168"/>
                <path class="tank-graphic__leg" d="M210 250v32h-28M354 250v32h28"/>
                <path class="tank-graphic__measure" d="M128 82v168M116 82h24M116 250h24"/>
                <path class="tank-graphic__measure" d="M158 304h322M158 292v24M480 292v24"/>
                <text x="92" y="172" transform="rotate(-90 92 172)">BODY HEIGHT</text><text x="257" y="337">BODY LENGTH</text>
                <g class="tank-graphic__ticks"><path d="M503 92h34M503 113h18M503 134h18M503 155h34M503 176h18M503 197h18M503 218h34M503 239h18"/></g>
                <text class="tank-graphic__readout" x="426" y="387">FIELD / 01</text>
            </svg>
        </div>
    </div>
</section>

@if (page.isTool("hub"))
<section class="otr-section otr-section--instrument" aria-labelledby="route-heading">
    <div class="otr-shell">
        <div class="otr-section__head otr-section__head--inverse"><p class="otr-kicker">Choose one job</p><h2 id="route-heading">A single data model. Twenty useful routes.</h2></div>
        <div class="route-list">
            @for (var link : page.nextLinks())
                <a class="route-list__item" href="${link.href()}"><span class="route-list__index">${link.badge()}</span><span><strong>${link.title()}</strong><small>${link.description()}</small></span><span aria-hidden="true">&nearr;</span></a>
            @endfor
        </div>
    </div>
</section>
@endif

<section class="otr-section" aria-labelledby="facts-heading">
    <div class="otr-shell otr-editorial-grid">
        <div><p class="otr-kicker">Field notes</p><h2 id="facts-heading">${page.factsHeading()}</h2></div>
        <ol class="field-notes">
            @for (var i = 0; i < page.facts().size(); i++)
                <li><span>${java.lang.String.format("%02d", i + 1)}</span><p>${page.facts().get(i)}</p></li>
            @endfor
        </ol>
    </div>
</section>

@if (page.isTool("prices"))
<section id="price-benchmark" class="otr-section otr-section--instrument" aria-labelledby="price-heading">
    <div class="otr-shell">
        <div class="otr-section__head otr-section__head--inverse"><p class="otr-kicker">EIA observation / ${page.priceSnapshot().observedOn().toString()}</p><h2 id="price-heading">Latest published residential benchmark</h2><p>${page.priceSnapshot().collectionStatus()} The next scheduled release is ${page.priceSnapshot().nextScheduledRelease().toString()}.</p></div>
        <div class="table-wrap"><table class="spec-table"><thead><tr><th scope="col">Area</th><th scope="col">Price</th><th scope="col">Unit</th></tr></thead><tbody>
            @for (var point : page.priceSnapshot().points())
                <tr><th scope="row">${point.areaName()}</th><td class="numeric">$${point.dollarsPerGallon()}</td><td>${page.priceSnapshot().unit()}</td></tr>
            @endfor
        </tbody></table></div>
        <p class="field-help">Observed ${page.priceSnapshot().observedOn().toString()}; EIA release ${page.priceSnapshot().releasedOn().toString()}. This is not a live supplier quote.</p>
    </div>
</section>
@endif

@if (page.isTool("heating-cost"))
<section id="tool" class="otr-section otr-section--instrument" data-tool-root data-tool-id="heating-oil-cost" data-page-id="${page.pageId()}" data-page-path="/heating-oil-cost-calculator/">
    <div class="otr-shell tool-grid">
        <div class="tool-intro"><p class="otr-kicker">Cost / quote</p><h2>Calculate the fuel subtotal</h2><p>Use a current supplier quote. The selected EIA figure is only a dated comparison.</p></div>
        <form class="instrument-form" data-heating-cost-form>
            <div class="field-pair"><label>Supplier quote<input name="localPrice" type="number" inputmode="decimal" min="0.01" step="0.001" value="5.50" required><span class="field-help">dollars per gallon</span></label><label>Order amount<input name="gallons" type="number" inputmode="decimal" min="1" step="1" value="150" required><span class="field-help">gallons</span></label></div>
            <label>Dated comparison benchmark<select name="benchmark">
                @for (var point : page.priceSnapshot().points())
                    <option value="${point.dollarsPerGallon()}">${point.areaName()} — $${point.dollarsPerGallon()}</option>
                @endfor
            </select><span class="field-help">EIA observation ${page.priceSnapshot().observedOn().toString()}, excluding taxes</span></label>
            <button class="otr-button" type="submit">Calculate delivery subtotal</button>
        </form>
        <div class="instrument-result" data-tool-result hidden tabindex="-1" aria-live="polite"><p class="instrument-result__label">Calculated fuel subtotal</p><h3 data-result-heading>Quote total</h3><div data-result-body></div></div>
    </div>
</section>
@endif

@if (page.isTool("order"))
<section id="tool" class="otr-section otr-section--instrument" data-tool-root data-tool-id="heating-oil-order" data-page-id="${page.pageId()}" data-page-path="/how-much-heating-oil-do-i-need/">
    <div class="otr-shell tool-grid">
        <div class="tool-intro"><p class="otr-kicker">Order / space</p><h2>Estimate room to a planning target</h2><p>If you just used the gauge calculator, its chart-based gallons transfer in this browser session.</p></div>
        <form class="instrument-form" data-order-form>
            <div class="field-pair"><label>Nominal tank capacity<input name="capacity" type="number" inputmode="decimal" min="1" step="1" value="275" required><span class="field-help">gallons</span></label><label>Current gallons<input name="currentGallons" type="number" inputmode="decimal" min="0" step="0.1" value="70" required><span class="field-help">prefer a verified chart result</span></label></div>
            <label>Planning target<select name="targetPercent"><option value="0.80">80% of nominal capacity</option><option value="0.85">85% of nominal capacity</option><option value="0.90">90% of nominal capacity</option></select></label>
            <button class="otr-button" type="submit">Estimate planning space</button>
        </form>
        <div class="instrument-result" data-tool-result hidden tabindex="-1" aria-live="polite"><p class="instrument-result__label">Planning estimate</p><h3 data-result-heading>Order space</h3><div data-result-body></div></div>
    </div>
</section>
@endif

@if (page.isTool("ran-out"))
<section id="decision" class="otr-section otr-section--soft" aria-labelledby="no-heat-heading">
    <div class="otr-shell scope-ledger"><div><p class="otr-kicker">No heat / safe sequence</p><h2 id="no-heat-heading">Check once. Then call the right service.</h2><p>A no-heat condition can be an empty tank, a failed gauge, loss of power, or an equipment fault.</p></div><ol class="field-notes"><li><span>01</span><p>Check thermostat setting, service switch, circuit breaker, and gauge without opening or changing the tank.</p></li><li><span>02</span><p>If one permitted reset runs only briefly and stops, do not reset again. Call the oil supplier or qualified burner service.</p></li><li><span>03</span><p>Tell them whether the gauge reads empty and whether the burner may have run dry so restart service can be planned.</p></li><li><span>04</span><p>For odor, oil, wetness, or staining, keep ignition sources away and open the leak-response guide instead of treating this as an ordinary run-out.</p></li></ol></div>
    <div class="otr-shell otr-actions"><a class="otr-button" href="/heating-oil-prices/">Check the dated price benchmark</a><a class="otr-text-link" href="/guides/leaking-heating-oil-tank-what-to-do/">Possible leak or spill <span aria-hidden="true">&rarr;</span></a></div>
</section>
@endif

@if (page.isTool("lifespan"))
<section id="decision" class="otr-section otr-section--soft" aria-labelledby="lifespan-heading">
    <div class="otr-shell scope-ledger">
        <div><p class="otr-kicker">Condition clock</p><h2 id="lifespan-heading">Tank age starts the review. Condition decides the route.</h2><p>There is no single official national replacement age for every residential heating-oil tank. Record the installation evidence, then inspect the conditions that shorten service life.</p></div>
        <ol class="scope-ledger__list">
            <li><span>01</span><strong>Known age, sound condition</strong><p>Keep dated installation records and arrange qualified periodic inspection. Age alone does not prove the tank has failed.</p></li>
            <li><span>02</span><strong>Unknown age or missing records</strong><p>Use the label, permits, invoices, prior owner records, and service history to establish a defensible range instead of guessing from appearance.</p></li>
            <li><span>03</span><strong>Corrosion or unstable support</strong><p>Pitting, flaking rust, wet seams, leaning legs, or a failing base moves the question from lifespan to tank-condition inspection and replacement planning.</p></li>
            <li><span>04</span><strong>Odor, oil, or unexplained loss</strong><p>Possible release signs override the age discussion. Avoid disturbing the tank and use the applicable spill-response route.</p></li>
        </ol>
    </div>
</section>
@endif

@if (page.isTool("gauge-service"))
<section id="decision" class="otr-section otr-section--soft" aria-labelledby="gauge-service-heading">
    <div class="otr-shell scope-ledger">
        <div><p class="otr-kicker">Gauge diagnosis</p><h2 id="gauge-service-heading">A bad reading does not identify the failed part.</h2><p>Do not force the float, open fittings, or buy a replacement gauge from tank capacity alone. A qualified oil-heat service provider should match the tank and diagnose the reading path.</p></div>
        <ol class="scope-ledger__list">
            <li><span>01</span><strong>Reading seems stuck</strong><p>Compare only with a safe, supported reading method and confirm the tank model and orientation. Do not strike or manipulate the gauge.</p></li>
            <li><span>02</span><strong>Gauge and usage disagree</strong><p>Check delivery history, elapsed days, and the matching manufacturer chart before assuming a leak or ordering a part.</p></li>
            <li><span>03</span><strong>Service scope</strong><p>Ask the technician to inspect the gauge assembly, tank fitting, vent and whistle, connected piping, and tank condition—not only the dial.</p></li>
            <li><span>04</span><strong>Release warning</strong><p>Odor, staining, visible oil, seepage, or unexplained rapid loss is not a routine gauge-replacement job.</p></li>
        </ol>
    </div>
</section>
@endif

@if (page.isTool("repair"))
<section id="decision" class="otr-section otr-section--soft" aria-labelledby="repair-heading">
    <div class="otr-shell scope-ledger">
        <div><p class="otr-kicker">Repair boundary</p><h2 id="repair-heading">Repair a service component. Review a distressed tank body.</h2><p>The word “repair” can describe very different work. Separate burner-side service, connected components, structural tank condition, and possible release response before requesting a quote.</p></div>
        <ol class="scope-ledger__list">
            <li><span>01</span><strong>Service component</strong><p>A gauge, filter, valve, vent whistle, or accessible fuel-line issue may begin with qualified oil-heat service.</p></li>
            <li><span>02</span><strong>Recurring fuel-quality symptom</strong><p>Repeated filter plugging, water, or sediment requires diagnosis of the source and tank condition; cleaning is not automatically a permanent repair.</p></li>
            <li><span>03</span><strong>Tank body or supports</strong><p>Corrosion, pitting, wet seams, seepage, unstable legs, or a failing base requires inspection and a replacement review rather than a cosmetic patch assumption.</p></li>
            <li><span>04</span><strong>Possible release</strong><p>Strong odor, visible oil, stained material, wet soil, or unexplained loss moves immediately to spill-response guidance.</p></li>
        </ol>
    </div>
</section>
@endif

@if (page.isTool("identifier"))
<section id="tool" class="otr-section otr-section--instrument" data-tool-root data-tool-id="tank-identifier" data-page-id="${page.pageId()}" data-page-path="/heating-oil-tank/">
    <div class="otr-shell tool-grid">
        <div class="tool-intro"><p class="otr-kicker">Identifier / 01</p><h2>Narrow the verified model list</h2><p>Select only what you can confirm. The result suggests candidates; the label remains the model evidence.</p></div>
        <form class="instrument-form" data-identifier-form>
            <div class="field-pair"><label>Nominal capacity<select name="capacity"><option value="any">Not sure</option><option value="138">138 gal</option><option value="275">275 gal</option><option value="330">330 gal</option></select></label><label>Orientation<select name="orientation"><option value="any">Not sure</option><option value="VERTICAL">Vertical</option><option value="HORIZONTAL">Horizontal</option></select></label></div>
            <button class="otr-button" type="submit">Show verified candidates</button>
        </form>
        <div class="instrument-result" data-tool-result hidden tabindex="-1"><p class="instrument-result__label">Candidate set</p><h3 data-result-heading>Check the model label</h3><div data-result-body></div></div>
    </div>
</section>
@endif

@if (page.isTool("gauge"))
<section id="tool" class="otr-section otr-section--instrument" data-tool-root data-tool-id="gauge-calculator" data-page-id="${page.pageId()}" data-page-path="/oil-tank-gauge-calculator/">
    <div class="otr-shell tool-grid">
        <div class="tool-intro"><p class="otr-kicker">Gauge / 04</p><h2>Enter the reading</h2><p>Only tank/orientation combinations with a reviewed chart are available. Your reading stays in this browser session and is not sent in analytics.</p></div>
        <form class="instrument-form" data-gauge-form>
            <label>Verified tank and orientation<select name="tankId">@for (var spec : page.specs())<option value="${spec.id()}">${spec.manufacturer()} ${spec.model()} / ${java.lang.String.format("%.0f", spec.nominalCapacityGallons())} gal ${spec.orientation().toString().toLowerCase()}</option>@endfor</select></label>
            <div class="field-pair"><label>Reading type<select name="readingType"><option value="fraction">Gauge fraction</option><option value="stick">Stick depth in inches</option></select></label><label>Reading<input name="reading" type="number" inputmode="decimal" min="0" max="44" step="0.125" value="0.25" aria-describedby="gauge-help"></label></div>
            <p class="field-help" id="gauge-help">For fractions enter a decimal: 0.125 = 1/8, 0.25 = 1/4, 0.5 = 1/2. For a stick, enter liquid depth from the tank bottom.</p>
            <button class="otr-button" type="submit">Calculate remaining oil</button>
        </form>
        <div class="instrument-result" data-tool-result hidden tabindex="-1"><p class="instrument-result__label">Chart-interpolated result</p><h3 data-result-heading>Estimated gallons remaining</h3><div class="result-number"><strong data-result-primary>-</strong><span>US gal</span></div><div data-result-body></div></div>
    </div>
</section>
<section class="otr-section otr-section--soft" aria-labelledby="gauge-troubleshooting-heading">
    <div class="otr-shell scope-ledger">
        <div><p class="otr-kicker">Gauge troubleshooting</p><h3 id="gauge-troubleshooting-heading">A strange reading is a diagnosis prompt, not a tank verdict</h3><p>Keep the tank closed. Match the symptom to the narrowest qualified next step.</p></div>
        <ol class="scope-ledger__list"><li><span>01</span><strong>Gauge stays on one mark</strong><p>Recheck recent deliveries and usage. Ask a heating-oil service technician to inspect the gauge rather than forcing the float.</p></li><li><span>02</span><strong>Gauge and stick disagree</strong><p>Confirm model, orientation, chart, tank tilt, and reading method. Never open or probe a tank when an unsafe condition is suspected.</p></li><li><span>03</span><strong>Cracked gauge or component drip</strong><p>Stop treating the number as reliable and request service. Visible oil, fresh staining, or strong odor enters the leak-response route.</p></li><li><span>04</span><strong>Vent whistle is weak or silent</strong><p>Do not authorize a delivery based on the gauge result alone. Tell the supplier and have the fill and vent system checked.</p></li></ol>
    </div>
</section>
@endif

@if (page.isTool("delivery"))
<section id="tool" class="otr-section otr-section--instrument delivery-workspace" data-tool-root data-tool-id="delivery-check" data-page-id="${page.pageId()}" data-page-path="/heating-oil-delivery-check/">
    <div class="otr-shell tool-grid">
        <div class="tool-intro">
            <p class="otr-kicker">Delivery / 05</p>
            <h2>Cross-check one delivery</h2>
            <p>Use the same confirmed tank before and after delivery. Let the fuel settle before the after reading and keep the printed meter ticket.</p>
            <ol class="instrument-steps" aria-label="Delivery check steps"><li><span>01</span> Match tank</li><li><span>02</span> Record levels</li><li><span>03</span> Compare ticket</li></ol>
        </div>
        <form class="instrument-form delivery-form" data-delivery-form>
            <label>Verified tank and orientation<select name="tankId">@for (var spec : page.specs())<option value="${spec.id()}">${spec.manufacturer()} ${spec.model()} / ${java.lang.String.format("%.0f", spec.nominalCapacityGallons())} gal ${spec.orientation().toString().toLowerCase()}</option>@endfor</select></label>
            <div class="field-pair"><label>Reading method<select name="readingType" data-delivery-reading-type><option value="stick">Stick depth in inches</option><option value="fraction">Float-gauge fraction</option></select></label><label>Delivery date<input name="deliveryDate" type="date" required></label></div>
            <div class="delivery-readings" aria-describedby="delivery-reading-help">
                <label><span>Before delivery</span><input name="beforeReading" type="number" inputmode="decimal" min="0" max="44" step="0.1" value="11" required><small data-before-unit>inches</small></label>
                <span class="delivery-readings__flow" aria-hidden="true">&rarr;</span>
                <label><span>After delivery</span><input name="afterReading" type="number" inputmode="decimal" min="0" max="44" step="0.1" value="36" required><small data-after-unit>inches</small></label>
            </div>
            <p class="field-help" id="delivery-reading-help" data-delivery-help>A clean stick depth offers a narrower planning range. Do not open or probe a tank when a leak or unsafe condition is suspected.</p>
            <label>Gallons on printed delivery ticket<input name="ticketGallons" type="number" inputmode="decimal" min="0.1" max="5000" step="0.1" value="170" required></label>
            <button class="otr-button" type="submit">Compare ticket with tank change</button>
        </form>
        <div class="instrument-result delivery-result" data-tool-result hidden tabindex="-1" aria-live="polite">
            <div class="delivery-result__status"><p class="instrument-result__label" data-delivery-band>Delivery check</p><span class="status-light" data-status-light aria-hidden="true"></span></div>
            <h3 data-result-heading>Comparison result</h3>
            <div class="result-number"><strong data-result-primary>-</strong><span>gal from chart center</span></div>
            <div data-result-body></div>
        </div>
        <section class="tank-passport" data-tank-passport aria-labelledby="passport-heading">
            <div class="tank-passport__head"><div><p class="otr-kicker">Private device record</p><h3 id="passport-heading">Tank Passport</h3></div><div class="passport-actions"><button type="button" class="text-button" data-passport-print>Print passport</button><button type="button" class="text-button text-button--muted" data-passport-clear>Clear history</button></div></div>
            <div class="passport-identity" data-passport-identity><p>No delivery checks saved on this device yet.</p></div>
            <ol class="passport-history" data-passport-history></ol>
            <p class="passport-privacy">Stored only in this browser. No account, address, server sync, or detailed analytics payload.</p>
        </section>
    </div>
</section>
@endif

@if (page.isTool("usage"))
<section id="tool" class="otr-section otr-section--instrument" data-tool-root data-tool-id="usage-calculator" data-page-id="${page.pageId()}" data-page-path="/heating-oil-usage-calculator/">
    <div class="otr-shell tool-grid">
        <div class="tool-intro"><p class="otr-kicker">Usage / 06</p><h2>Turn remaining gallons into a runway</h2><p>Use your household's own recent consumption. The calculator does not guess from house size or weather, and the values stay in this browser session.</p></div>
        <form class="instrument-form" data-usage-form>
            <div class="field-pair"><label>Gallons remaining<input name="remainingGallons" type="number" inputmode="decimal" min="0" max="5000" step="0.1" value="70" required></label><label>Keep in reserve<input name="reserveGallons" type="number" inputmode="decimal" min="0" max="1000" step="0.1" value="30" required><span class="field-help">A planning buffer, not a delivery rule.</span></label></div>
            <label>How do you know your use rate?<select name="rateMode" data-rate-mode><option value="daily">I know gallons per day</option><option value="history">I know gallons used over a period</option></select></label>
            <div class="mode-panel" data-rate-panel="daily"><label>Recent daily use <span>gal/day</span><input name="dailyUse" type="number" inputmode="decimal" min="0.01" max="100" step="0.01" value="4" required></label></div>
            <div class="mode-panel" data-rate-panel="history" hidden><div class="field-pair"><label>Gallons used<input name="gallonsUsed" type="number" inputmode="decimal" min="0.1" max="5000" step="0.1" value="100"></label><label>Across how many days<input name="daysElapsed" type="number" inputmode="numeric" min="1" max="365" step="1" value="30"></label></div></div>
            <button class="otr-button" type="submit">Estimate days remaining</button>
        </form>
        <div class="instrument-result" data-tool-result hidden tabindex="-1"><p class="instrument-result__label">Consumption-based estimate</p><h3 data-result-heading>Days until reserve</h3><div class="result-number"><strong data-result-primary>-</strong><span>days</span></div><div data-result-body></div></div>
    </div>
</section>
@endif

@if (page.isTool("capacity"))
<section id="tool" class="otr-section otr-section--instrument" data-tool-root data-tool-id="capacity-calculator" data-page-id="${page.pageId()}" data-page-path="/oil-tank-capacity-calculator/">
    <div class="otr-shell tool-grid">
        <div class="tool-intro"><p class="otr-kicker">Capacity / 07</p><h2>Measure the tank body</h2><p>The estimator uses the selected shape formula and repeats the calculation at +/-0.5 inch. Values remain in sessionStorage only.</p></div>
        <form class="instrument-form" data-capacity-form>
            <label>Tank shape<select name="shape"><option value="obround">Obround steel tank</option><option value="cylinder">Cylinder</option><option value="rectangular">Rectangular / box approximation</option></select></label>
            <div class="field-triple"><label>Length <span>in</span><input name="length" type="number" inputmode="decimal" min="1" max="240" step="0.1" value="60" required></label><label>Width / diameter <span>in</span><input name="width" type="number" inputmode="decimal" min="1" max="120" step="0.1" value="27" required></label><label>Height <span>in</span><input name="height" type="number" inputmode="decimal" min="1" max="120" step="0.1" value="44" required></label></div>
            <button class="otr-button" type="submit">Estimate capacity range</button>
        </form>
        <div class="instrument-result" data-tool-result hidden tabindex="-1"><p class="instrument-result__label">Geometric estimate</p><h3 data-result-heading>Estimated capacity range</h3><div class="result-number"><strong data-result-primary>-</strong><span>US gal</span></div><div data-result-body></div></div>
    </div>
</section>
@endif

@if (page.isTool("sludge"))
<section id="tool" class="otr-section otr-section--instrument" data-tool-root data-tool-id="sludge-router" data-page-id="${page.pageId()}" data-page-path="/heating-oil-tank-sludge-cleaning/">
    <div class="otr-shell tool-grid">
        <div class="tool-intro"><p class="otr-kicker">Sludge / 09</p><h2>Route the symptoms before requesting cleaning</h2><p>Choose only what you can observe or what a technician has already confirmed. The selections remain in sessionStorage and are not sent in analytics.</p></div>
        <form class="instrument-form" data-sludge-form>
            <fieldset><legend>Heating-system symptoms</legend><div class="check-grid"><label><input type="checkbox" name="filterClogs"> Filter repeatedly plugs</label><label><input type="checkbox" name="burnerStops"> Burner repeatedly shuts down</label><label><input type="checkbox" name="waterDebris"> Water or debris was confirmed by a technician</label><label><input type="checkbox" name="oneServiceIssue"> One isolated filter or burner issue</label></div></fieldset>
            <fieldset><legend>Tank-condition signs</legend><div class="check-grid"><label><input type="checkbox" name="rust"> Rust or pitting on the tank</label><label><input type="checkbox" name="unstable"> Bent, rusty, or unstable supports</label><label><input type="checkbox" name="seepage"> Oily patch or seepage on the tank</label></div></fieldset>
            <fieldset><legend>Possible release signs</legend><div class="check-grid"><label><input type="checkbox" name="odor"> Strong oil odor</label><label><input type="checkbox" name="wetness"> Fresh wetness or staining</label><label><input type="checkbox" name="visibleOil"> Visible oil</label><label><input type="checkbox" name="oilySoil"> Oily soil or sheen</label></div></fieldset>
            <button class="otr-button" type="submit">Show the safe next step</button>
        </form>
        <div class="instrument-result" data-tool-result hidden tabindex="-1" aria-live="polite"><p class="instrument-result__label" data-risk-band>Route result</p><h3 data-result-heading>Next action</h3><div data-result-body></div></div>
        <div class="commercial-route" data-commercial-route hidden>
            <div><p class="otr-kicker">Optional professional route</p><h3 data-commercial-heading>Request the right kind of follow-up</h3><p>The result is already visible. Only the route summary is attached, never the individual symptoms.</p></div>
            <form class="lead-form lead-form--instrument" action="/api/leads/capture" method="post" data-tool-lead-form>
                <input type="hidden" name="pageId" value="${page.pageId()}"><input type="hidden" name="pagePath" value="/heating-oil-tank-sludge-cleaning/"><input type="hidden" name="stateSlug" value="national"><input type="hidden" name="routeFamily" value="sludge-routing"><input type="hidden" name="scenario" value="removal_decision" data-lead-scenario><input type="hidden" name="partnerType" value="closure_or_removal" data-lead-partner><input type="hidden" name="userRole" value="homeowner"><input type="hidden" name="tankStatus" value="condition_review"><input type="hidden" name="toolId" value="sludge-router"><input type="hidden" name="riskBand" value="" data-lead-risk><input type="hidden" name="commercialIntent" value="" data-lead-intent><input type="hidden" name="resultSummary" value="" data-lead-summary>
                <div class="field-pair"><label>ZIP code<input name="zipCode" inputmode="numeric" autocomplete="postal-code" maxlength="10"></label><label>Email<input name="email" type="email" autocomplete="email" required></label></div>
                <label>Name <span>optional</span><input name="name" autocomplete="name"></label><label>Phone <span>optional</span><input name="phone" type="tel" autocomplete="tel"></label>
                <button class="otr-button" type="submit" data-result-cta>Request a qualified follow-up</button><p class="field-help">Contact details are used only for this request.</p>
            </form>
        </div>
    </div>
</section>
@endif

@if (page.isTool("planner"))
<section id="tool" class="otr-section otr-section--instrument" data-tool-root data-tool-id="replacement-planner" data-page-id="${page.pageId()}" data-page-path="/oil-tank-replacement-planner/">
    <div class="otr-shell tool-grid">
        <div class="tool-intro"><p class="otr-kicker">Risk / 10</p><h2>Describe what you can observe</h2><p>Choose current facts. Odor, wet soil, or visible oil always overrides ordinary age planning.</p></div>
        <form class="instrument-form" data-planner-form>
            <label>Approximate tank age<input name="age" type="number" inputmode="numeric" min="0" max="100" value="10"><span class="field-help">years; use 0 if unknown</span></label>
            <fieldset><legend>Condition</legend><div class="check-grid"><label><input type="checkbox" name="rust"> Visible rust or pitting</label><label><input type="checkbox" name="seepage"> Indoor seepage</label><label><input type="checkbox" name="unstable"> Unstable legs or supports</label><label><input type="checkbox" name="usageSpike"> Unexplained fuel use</label><label><input type="checkbox" name="serviceIssue"> Gauge, whistle, or line issue only</label><label><input type="checkbox" name="odor"> Strong oil odor</label><label><input type="checkbox" name="wetSoil"> Wet or oil-stained soil</label><label><input type="checkbox" name="leak"> Visible oil or active leak</label></div></fieldset>
            <fieldset><legend>Property situation</legend><div class="check-grid"><label><input type="checkbox" name="sale"> Buying or selling now</label><label><input type="checkbox" name="underground"> Tank is underground</label><label><input type="checkbox" name="unknown"> Tank location/type unknown</label></div></fieldset>
            <button class="otr-button" type="submit">Show the next route</button>
        </form>
        <div class="instrument-result" data-tool-result hidden tabindex="-1"><p class="instrument-result__label" data-risk-band>Result</p><h3 data-result-heading>Next action</h3><div data-result-body></div></div>
        <div class="commercial-route" data-commercial-route hidden>
            <div><p class="otr-kicker">Optional professional route</p><h3 data-commercial-heading>Find the right kind of help</h3><p data-commercial-copy>Send the result context without exposing your detailed answers.</p></div>
            <form class="lead-form lead-form--instrument" action="/api/leads/capture" method="post" data-tool-lead-form>
                <input type="hidden" name="pageId" value="${page.pageId()}"><input type="hidden" name="pagePath" value="/oil-tank-replacement-planner/"><input type="hidden" name="stateSlug" value="national"><input type="hidden" name="routeFamily" value="replacement-planner"><input type="hidden" name="scenario" value="removal_decision" data-lead-scenario><input type="hidden" name="partnerType" value="closure_or_removal" data-lead-partner><input type="hidden" name="userRole" value="homeowner"><input type="hidden" name="tankStatus" value="condition_review"><input type="hidden" name="toolId" value="replacement-planner"><input type="hidden" name="riskBand" value="" data-lead-risk><input type="hidden" name="commercialIntent" value="" data-lead-intent><input type="hidden" name="resultSummary" value="" data-lead-summary>
                <div class="field-pair"><label>ZIP code<input name="zipCode" inputmode="numeric" autocomplete="postal-code" maxlength="10"></label><label>Email<input name="email" type="email" autocomplete="email" required></label></div>
                <label>Name <span>optional</span><input name="name" autocomplete="name"></label><label>Phone <span>optional</span><input name="phone" type="tel" autocomplete="tel"></label>
                <button class="otr-button" type="submit" data-result-cta>Request a qualified follow-up</button><p class="field-help">The result is visible without submitting. Contact details are used only for this request.</p>
            </form>
        </div>
    </div>
</section>
<section class="otr-section otr-section--soft" aria-labelledby="repair-replace-heading">
    <div class="otr-shell scope-ledger">
        <div><p class="otr-kicker">Repair or replace</p><h3 id="repair-replace-heading">Start with the failed layer</h3><p>No single age number decides every tank. Condition and the part that failed determine the review.</p></div>
        <ol class="scope-ledger__list"><li><span>01</span><strong>Service component</strong><p>An isolated gauge, filter, vent, valve, or line problem may begin with qualified heating-system service.</p></li><li><span>02</span><strong>Tank body or supports</strong><p>Rust, pitting, seepage, unstable legs, or recurring unexplained loss calls for tank-condition inspection and replacement review.</p></li><li><span>03</span><strong>Release signal</strong><p>Odor, wet soil, or visible oil overrides repair-versus-replace planning and enters spill response.</p></li><li><span>04</span><strong>Property transaction</strong><p>An underground or unknown tank during a sale starts with records and locate work before removal pricing.</p></li></ol>
    </div>
</section>
@endif

@if (page.isTool("replacement-cost"))
<section id="cost-scope" class="otr-section otr-section--instrument" data-tool-root data-tool-id="replacement-cost-scope" data-page-id="${page.pageId()}" data-page-path="/oil-tank-replacement-cost/">
    <div class="otr-shell tool-grid">
        <div class="tool-intro"><p class="otr-kicker">Replace / 11</p><h2>Build a bid-comparison scope</h2><p>This tool identifies work categories. It does not turn broad national averages into a false site quote, and its selections stay in sessionStorage.</p></div>
        <form class="instrument-form" data-replacement-cost-form>
            <div class="field-pair"><label>Existing tank location<select name="location"><option value="basement">Basement / indoor aboveground</option><option value="outdoor">Outdoor aboveground</option><option value="underground">Underground</option><option value="unknown">Unknown</option></select></label><label>Project trigger<select name="trigger"><option value="condition">Condition or inspection concern</option><option value="planning">Planned age-based replacement</option><option value="conversion">Heating-system conversion</option><option value="sale">Buying or selling</option></select></label></div>
            <div class="field-pair"><label>Access<select name="access"><option value="direct">Direct exterior access</option><option value="interior">Ordinary interior route</option><option value="restricted">Narrow or finished interior route</option><option value="excavation">Excavation likely</option><option value="unknown">Not sure</option></select></label><label>Fuel status<select name="fuel"><option value="some">Fuel may remain</option><option value="empty">Reported empty</option><option value="unknown">Not sure</option></select></label></div>
            <fieldset><legend>Known scope and warning signs</legend><div class="check-grid"><label><input type="checkbox" name="baseWork"> Base or supports need work</label><label><input type="checkbox" name="pipingWork"> Fill, vent, or supply piping may change</label><label><input type="checkbox" name="permitUnknown"> Permit or inspection not confirmed</label><label><input type="checkbox" name="odor"> Strong oil odor</label><label><input type="checkbox" name="staining"> Fresh staining or wet soil</label><label><input type="checkbox" name="visibleOil"> Visible oil or active seepage</label></div></fieldset>
            <button class="otr-button" type="submit">Build replacement scope</button>
        </form>
        <div class="instrument-result" data-tool-result hidden tabindex="-1" aria-live="polite"><p class="instrument-result__label" data-risk-band>Quote scope</p><h3 data-result-heading>Bid-comparison checklist</h3><div data-result-body></div></div>
    </div>
    <div class="otr-shell scope-ledger" aria-labelledby="replacement-ledger-heading">
        <div><p class="otr-kicker">SSR quote ledger</p><h3 id="replacement-ledger-heading">Every written replacement quote should resolve these boundaries</h3><p>Available even when JavaScript is off. A contractor may mark a line not applicable, but the scope should not leave it ambiguous.</p></div>
        <ol class="scope-ledger__list"><li><span>01</span><strong>New tank</strong><p>Manufacturer, model, capacity, single/double wall, warranty conditions.</p></li><li><span>02</span><strong>Old tank</strong><p>Pump-out, fuel disposition, sludge, cleaning, removal or closure, documentation.</p></li><li><span>03</span><strong>Site and access</strong><p>Route protection, stairs or doors, excavation, base or foundation, outdoor protection.</p></li><li><span>04</span><strong>Connected system</strong><p>Fill, vent, overfill device, gauge, filter, supply line, burner connection.</p></li><li><span>05</span><strong>Authority and closeout</strong><p>Permit, inspection, licensed work, testing, photos, receipts, completion records.</p></li><li><span>06</span><strong>Release boundary</strong><p>What stops routine work and starts investigation, sampling, reporting, or remediation.</p></li></ol>
    </div>
</section>
@endif

@if (page.isTool("installation-cost"))
<section id="cost-scope" class="otr-section otr-section--instrument" data-tool-root data-tool-id="installation-cost-scope" data-page-id="${page.pageId()}" data-page-path="/heating-oil-tank-installation-cost/">
    <div class="otr-shell tool-grid">
        <div class="tool-intro"><p class="otr-kicker">Install / 12</p><h2>Separate equipment price from installed scope</h2><p>Use the selected manufacturer's instructions and local authority requirements. This tool does not publish an unsupported national installed price.</p></div>
        <form class="instrument-form" data-installation-cost-form>
            <div class="field-pair"><label>Proposed tank design<select name="design"><option value="steel">Steel obround</option><option value="doubleWall">Double-wall system</option><option value="unknown">Not selected</option></select></label><label>Installation location<select name="location"><option value="indoor">Indoor</option><option value="outdoor">Outdoor</option><option value="unknown">Not selected</option></select></label></div>
            <div class="field-pair"><label>Supporting base<select name="base"><option value="review">Needs contractor review</option><option value="existing">Existing base may remain</option><option value="new">New base is expected</option></select></label><label>Access<select name="access"><option value="direct">Direct delivery route</option><option value="restricted">Restricted stairs, doors, or finished area</option><option value="unknown">Not sure</option></select></label></div>
            <fieldset><legend>Other work to price separately</legend><div class="check-grid"><label><input type="checkbox" name="oldTank"> Existing tank removal or closure</label><label><input type="checkbox" name="piping"> New fill, vent, or supply piping</label><label><input type="checkbox" name="permitUnknown"> Permit or inspection not confirmed</label><label><input type="checkbox" name="odor"> Strong oil odor</label><label><input type="checkbox" name="staining"> Staining or wet soil</label><label><input type="checkbox" name="visibleOil"> Visible oil or seepage</label></div></fieldset>
            <button class="otr-button" type="submit">Build installation scope</button>
        </form>
        <div class="instrument-result" data-tool-result hidden tabindex="-1" aria-live="polite"><p class="instrument-result__label" data-risk-band>Installation scope</p><h3 data-result-heading>Installed-cost checklist</h3><div data-result-body></div></div>
    </div>
    <div class="otr-shell scope-ledger" aria-labelledby="installation-ledger-heading">
        <div><p class="otr-kicker">SSR quote ledger</p><h3 id="installation-ledger-heading">The installed total has six inspectable parts</h3><p>A product price answers only the first line. Keep manufacturer-specific requirements attached to the exact model quoted.</p></div>
        <ol class="scope-ledger__list"><li><span>01</span><strong>Tank and accessories</strong><p>Exact model, listed accessories, gauge, filter, overfill device, warranty conditions.</p></li><li><span>02</span><strong>Delivery and access</strong><p>Freight, handling, stairs, doors, finished-area protection, placement access.</p></li><li><span>03</span><strong>Base and location</strong><p>Level supporting surface, inspection clearance, drainage, cover, anchoring where applicable.</p></li><li><span>04</span><strong>Piping</strong><p>Fill, vent, supply line, valves, connections, compatibility, pressure or leak checks.</p></li><li><span>05</span><strong>Permit and inspection</strong><p>Local approval, licensed trades, fire or code inspection, completion certificate where required.</p></li><li><span>06</span><strong>Old system and commissioning</strong><p>Old tank scope, fuel handling, startup, test, labeling, homeowner documents.</p></li></ol>
    </div>
</section>
@endif

@if (page.isTool("removal"))
<section id="tool" class="otr-section otr-section--instrument" data-tool-root data-tool-id="basement-removal" data-page-id="${page.pageId()}" data-page-path="/basement-oil-tank-removal/">
    <div class="otr-shell tool-grid">
        <div class="tool-intro"><p class="otr-kicker">Remove / 13</p><h2>Define the quote scope first</h2><p>This separates a routine indoor removal conversation from a suspected release. It does not estimate a national price or tell you to handle fuel yourself.</p></div>
        <form class="instrument-form" data-removal-form>
            <div class="field-pair"><label>Tank use<select name="tankUse"><option value="inactive">Inactive / replaced</option><option value="active">Still in service</option><option value="unknown">Not sure</option></select></label><label>Access to tank<select name="access"><option value="walkout">Walkout or direct exterior access</option><option value="stairs">Ordinary interior stairs</option><option value="narrow">Narrow door, stairs, or finished area</option><option value="unknown">Not sure</option></select></label></div>
            <div class="field-pair"><label>Fuel remaining<select name="fuel"><option value="empty">Reported empty</option><option value="some">Some fuel may remain</option><option value="unknown">Not sure</option></select></label><label>Project trigger<select name="project"><option value="planning">Planning ahead</option><option value="conversion">Heating-system conversion</option><option value="sale">Buying or selling</option><option value="space">Need the space</option></select></label></div>
            <fieldset><legend>Release warning signs</legend><div class="check-grid"><label><input type="checkbox" name="odor"> Strong oil odor</label><label><input type="checkbox" name="staining"> Fresh staining</label><label><input type="checkbox" name="visibleOil"> Visible oil</label><label><input type="checkbox" name="seepage"> Floor or wall seepage</label></div></fieldset>
            <button class="otr-button" type="submit">Build the next-step scope</button>
        </form>
        <div class="instrument-result" data-tool-result hidden tabindex="-1"><p class="instrument-result__label" data-risk-band>Scope result</p><h3 data-result-heading>Next action</h3><div data-result-body></div></div>
        <div class="commercial-route" data-commercial-route hidden>
            <div><p class="otr-kicker">Optional professional route</p><h3 data-commercial-heading>Request the right kind of follow-up</h3><p data-commercial-copy>The result is already visible. This sends only the route summary, not your individual answers.</p></div>
            <form class="lead-form lead-form--instrument" action="/api/leads/capture" method="post" data-tool-lead-form>
                <input type="hidden" name="pageId" value="${page.pageId()}"><input type="hidden" name="pagePath" value="/basement-oil-tank-removal/"><input type="hidden" name="stateSlug" value="national"><input type="hidden" name="routeFamily" value="basement-removal"><input type="hidden" name="scenario" value="removal_decision" data-lead-scenario><input type="hidden" name="partnerType" value="closure_or_removal" data-lead-partner><input type="hidden" name="userRole" value="homeowner"><input type="hidden" name="tankStatus" value="confirmed_aboveground_indoor"><input type="hidden" name="toolId" value="basement-removal"><input type="hidden" name="tankType" value="aboveground_indoor"><input type="hidden" name="riskBand" value="" data-lead-risk><input type="hidden" name="commercialIntent" value="" data-lead-intent><input type="hidden" name="resultSummary" value="" data-lead-summary>
                <div class="field-pair"><label>ZIP code<input name="zipCode" inputmode="numeric" autocomplete="postal-code" maxlength="10"></label><label>Email<input name="email" type="email" autocomplete="email" required></label></div>
                <label>Name <span>optional</span><input name="name" autocomplete="name"></label><label>Phone <span>optional</span><input name="phone" type="tel" autocomplete="tel"></label>
                <button class="otr-button" type="submit" data-result-cta>Request a qualified follow-up</button><p class="field-help">Contact details are used only for this request.</p>
            </form>
        </div>
    </div>
</section>
@endif

@if (page.isTool("sizes") || page.isTool("275"))
<section class="otr-section" id="verified-data">
    <div class="otr-shell"><div class="otr-section__head"><p class="otr-kicker">Manufacturer-verified models</p><h2>${page.isTool("275") ? "275-gallon does not mean one footprint" : "Residential size comparison"}</h2><p>Dimensions are listed as length x width x height. Verify the physical label before using a model-specific chart.</p></div>
        <div class="table-wrap"><table class="spec-table"><thead><tr><th>Model</th><th>Shape / orientation</th><th>Nominal</th><th>Dimensions</th><th>Chart max / nominal*</th><th>Verified</th></tr></thead><tbody>
        @for (var spec : page.specs())<tr><th scope="row">${spec.manufacturer()}<small>${spec.model()}</small></th><td>${spec.shape().toString().toLowerCase()} / ${spec.orientation().toString().toLowerCase()}</td><td class="measure">${java.lang.String.format("%.0f", spec.nominalCapacityGallons())} gal</td><td class="measure">${spec.dimensionsLabel()}</td><td class="measure">${java.lang.String.format("%.1f", spec.usableCapacityGallons())} gal</td><td>${spec.verifiedOn().toString()}</td></tr>@endfor
        </tbody></table></div><p class="table-note">*Chart maximum is the final value on a reviewed gauge chart when available; otherwise the manufacturer nominal capacity is shown. It is not a delivery instruction.</p>
    </div>
</section>
@endif

@if (page.isTool("275"))
<section class="otr-section otr-section--soft" id="275-gallon-chart">
    <div class="otr-shell">
        <div class="otr-section__head">
            <p class="otr-kicker">Manufacturer capacity table</p>
            <h2>275-gallon vertical oil tank chart: inches to gallons</h2>
            <p>This chart belongs only to the labeled vertical model shown below. A horizontal 275-gallon tank has different geometry and must not use this table.</p>
        </div>
        @for (var spec : page.specs())
            @if (spec.hasGaugeChart())
                @template.partials.gaugeChartTable(spec = spec)
            @endif
        @endfor
        <p class="chart-next-step"><a class="otr-button" href="/oil-tank-gauge-calculator/">Calculate between chart rows</a><span>Uses interval interpolation instead of linear tank-capacity multiplication.</span></p>
    </div>
</section>
@endif

@if (page.isTool("charts"))
<section class="otr-section otr-section--soft" id="chart-library">
    <div class="otr-shell">
        <div class="otr-section__head">
            <p class="otr-kicker">Official tank chart index</p>
            <h2>Verified residential oil tank charts</h2>
            <p>Each table has one manufacturer model and orientation. For the common 275-gallon vertical chart, use the dedicated field guide instead of a copied table.</p>
        </div>
        <nav class="chart-index" aria-label="Available oil tank charts">
            <a href="/275-gallon-oil-tank/#275-gallon-chart"><strong>275 gal vertical</strong><span>Open the canonical 275 chart</span></a>
            @for (var spec : page.specs())
                <a href="#chart-${spec.id()}"><strong>${java.lang.String.format("%.0f", spec.nominalCapacityGallons())} gal ${spec.orientation().toString().toLowerCase()}</strong><span>${spec.manufacturer()} ${spec.model()}</span></a>
            @endfor
        </nav>
        <div class="chart-library">
            @for (var spec : page.specs())
                <article class="chart-library__entry" id="chart-${spec.id()}">
                    <div class="otr-section__head"><p class="otr-kicker">${java.lang.String.format("%.0f", spec.nominalCapacityGallons())}-gallon chart</p><h3>${spec.manufacturer()} ${spec.model()} inches to gallons</h3></div>
                    @template.partials.gaugeChartTable(spec = spec)
                </article>
            @endfor
        </div>
        <p class="chart-next-step"><a class="otr-button" href="/oil-tank-gauge-calculator/">Calculate between chart rows</a><span>Use the calculator only after the label and orientation match one of its reviewed charts.</span></p>
    </div>
</section>
@endif

@if (!page.isTool("hub") && !page.isTool("sizes") && !page.isTool("275") && !page.isTool("charts") && !page.specs().isEmpty())
<section class="otr-section" id="verified-data"><div class="otr-shell"><div class="otr-section__head"><p class="otr-kicker">Shared tank data</p><h2>Verified models available on this route</h2></div><div class="verified-list">@for (var spec : page.specs())<div><strong>${spec.manufacturer()} ${spec.model()}</strong><span class="measure">${spec.dimensionsLabel()} / ${java.lang.String.format("%.0f", spec.nominalCapacityGallons())} gal</span><small>${spec.officialSourceTitle()}</small></div>@endfor</div></div></section>
@endif

@if (!page.faqs().isEmpty())
<section class="otr-section otr-section--soft"><div class="otr-shell otr-editorial-grid"><div><p class="otr-kicker">Route-specific question</p><h2>One distinction that matters</h2></div><div class="faq-list">@for (var faq : page.faqs())<details><summary>${faq.question()}</summary><p>${faq.answer()}</p></details>@endfor</div></div></section>
@endif

<section class="otr-section" id="sources"><div class="otr-shell otr-editorial-grid"><div><p class="otr-kicker">Source boundary</p><h2>Official source material</h2><p>Model values require manufacturer support; safety and removal guidance uses official public-agency material. Verification date: 2026-08-02.</p></div><ol class="source-ledger">@for (var source : page.sources())<li><a href="${source.url()}">${source.title()} <span aria-hidden="true">&nearr;</span></a><p>${source.note()}</p></li>@endfor</ol></div></section>

@if (!page.isTool("hub"))
<section class="otr-section otr-section--instrument"><div class="otr-shell"><div class="otr-section__head otr-section__head--inverse"><p class="otr-kicker">Continue the route</p><h2>Use the next tool only when the job changes.</h2></div><div class="route-list route-list--compact">@for (var link : page.nextLinks())<a class="route-list__item" href="${link.href()}"><span class="route-list__index">${link.badge()}</span><span><strong>${link.title()}</strong><small>${link.description()}</small></span><span aria-hidden="true">&rarr;</span></a>@endfor</div></div></section>
@endif
`)

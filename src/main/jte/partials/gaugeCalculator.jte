@import owner.buriedoiltank.pages.ProductPageModels.ProductPageModel
@param ProductPageModel page
<section id="tool" class="otr-section otr-section--instrument" data-tool-root data-tool-id="gauge-calculator" data-page-id="${page.pageId()}" data-page-path="${java.net.URI.create(page.meta().canonicalUrl()).getPath()}">
    <div class="otr-shell tool-grid">
        <div class="tool-intro"><p class="otr-kicker">Gauge / 04</p><h2>Enter the reading</h2><p>Only tank/orientation combinations with a reviewed chart are available. Your reading stays in this browser session and is not sent in analytics.</p></div>
        <form class="instrument-form" data-gauge-form>
            <label>Verified tank and orientation<select name="tankId">@for (var spec : page.specs().stream().filter(s -> s.hasGaugeChart()).toList())<option value="${spec.id()}">${spec.manufacturer()} ${spec.model()} / ${java.lang.String.format("%.0f", spec.nominalCapacityGallons())} gal ${spec.orientation().toString().toLowerCase()}</option>@endfor</select></label>
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

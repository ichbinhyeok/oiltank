@import owner.buriedoiltank.tank.TankSpec
@param TankSpec spec
<div class="chart-identification">
    <strong>${spec.manufacturer()} ${spec.model()}</strong>
    <span class="measure">${spec.dimensionsLabel()} / ${spec.orientation().toString().toLowerCase()}</span>
    <small>Official chart verified ${spec.verifiedOn().toString()}</small>
</div>
<div class="table-wrap table-wrap--chart">
    <table class="spec-table gauge-chart-table">
        <caption>${spec.manufacturer()} ${spec.model()} ${java.lang.String.format("%.0f", spec.nominalCapacityGallons())}-gallon ${spec.orientation().toString().toLowerCase()} oil tank chart</caption>
        <thead><tr><th scope="col">Liquid depth</th><th scope="col">Gallons</th></tr></thead>
        <tbody>
        @for (var point : spec.gaugeChart())
            <tr><th scope="row" class="measure">${java.lang.String.format("%.0f", point.liquidHeightInches())} in</th><td class="measure">${java.lang.String.format("%.1f", point.gallons())} gal</td></tr>
        @endfor
        </tbody>
    </table>
</div>
<p class="table-note">Manufacturer-stated chart precision is approximately +/-2%. Tank tilt, reading method, and installation add uncertainty. This table is planning information, not a delivery-meter reading.</p>

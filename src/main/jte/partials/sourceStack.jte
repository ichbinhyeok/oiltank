@import owner.buriedoiltank.data.SourceReference
@param java.util.List<SourceReference> sources
<section class="section">
    <div class="section__inner">
        <div class="section__header">
            <div>
                <div class="section__eyebrow">Official source stack</div>
                <h2>Primary sources that anchor this surface.</h2>
            </div>
        </div>
        <ul class="source-stack source-stack--grid">
            @for (var source : sources)
                <li class="source-stack__item">
                    <a href="${source.url()}" target="_blank" rel="noreferrer">${source.title()}</a>
                    <span>${source.kind()}</span>
                </li>
            @endfor
        </ul>
    </div>
</section>

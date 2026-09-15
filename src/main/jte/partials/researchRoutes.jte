<section class="service-section service-section--paper"><div class="service-shell">
    <div class="research-directory-intro"><div><p class="service-kicker">Research starts locally</p><h2>Eight starting points. Different records behind each.</h2></div><p>Town permits, county verification letters, and city tank reports need different routes. These public-source guides show where we start, what to prepare, and what an empty search cannot tell you.</p></div>
    <div class="research-area-links">
    @for (var route : owner.buriedoiltank.data.ResearchCatalog.AREAS)
        <a href="${route.path()}">${route.locality()} <span aria-hidden="true">→</span></a>
    @endfor
    </div><div class="research-jump"><a href="/research-areas/">Explore areas and missing-record questions →</a><a href="/research-examples/">Read two source-backed walkthroughs →</a></div>
</div></section>

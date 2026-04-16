@import owner.buriedoiltank.pages.PageModels.AdminPageModel
@param AdminPageModel page
@template.layout.page(meta = page.meta(), activeNav = "admin", content = @`
<section class="section admin-shell">
    <div class="section__inner admin-shell__inner">
        <div class="admin-hero">
            <div class="admin-hero__copy">
                <div class="section__eyebrow">Admin</div>
                <h1>Operations dashboard</h1>
                <p>${page.reviewSummary()}</p>
                <div class="admin-pill-row">
                    <span class="admin-pill">Direct URL access only</span>
                    <span class="admin-pill">Basic auth required</span>
                    <span class="admin-pill">Search indexing blocked</span>
                </div>
            </div>
            <div class="admin-note-card">
                <strong>Protected surface</strong>
                <p>Keep this area off public navigation. Share the URL only with operators who need route, freshness, and lead telemetry.</p>
            </div>
        </div>
        <div class="metric-grid metric-grid--admin">
            @for (var metric : page.metrics())
                <div class="metric-card metric-card--admin">
                    <span>${metric.label()}</span>
                    <strong>${metric.value()}</strong>
                </div>
            @endfor
        </div>
    </div>
</section>

<section class="section section--muted section--admin-band">
    <div class="section__inner admin-panel-grid">
        <div class="admin-panel">
            <div class="section__eyebrow">Exports</div>
            <div class="admin-export-grid">
                <a class="admin-export-card" href="/admin/exports/routes.json">
                    <strong>Route manifest</strong>
                    <p>Current route inventory and page metadata.</p>
                </a>
                <a class="admin-export-card" href="/admin/exports/route-status.csv">
                    <strong>Route status CSV</strong>
                    <p>Freshness, index state, CTA activity, and lead outcomes.</p>
                </a>
                <a class="admin-export-card" href="/admin/exports/promotion-review.json">
                    <strong>Promotion review</strong>
                    <p>Held-route promotion summary and blockers.</p>
                </a>
                <a class="admin-export-card" href="/admin/exports/source-freshness-review.json">
                    <strong>Freshness review</strong>
                    <p>Review window, affected scopes, and source stack coverage.</p>
                </a>
                <a class="admin-export-card" href="/admin/exports/admin-metrics-snapshot.json">
                    <strong>Metrics snapshot</strong>
                    <p>Aggregate 28-day counts for operators and reporting.</p>
                </a>
                <a class="admin-export-card" href="/admin/exports/leads.csv">
                    <strong>Lead submissions CSV</strong>
                    <p>Raw checklist submissions with page, scenario, partner, and contact fields.</p>
                </a>
                <a class="admin-export-card" href="/admin/exports/lead-events.csv">
                    <strong>Lead events CSV</strong>
                    <p>Raw CTA click, lead-open, and lead-submit event log for funnel review.</p>
                </a>
            </div>
        </div>
        <div class="admin-panel">
            <div class="section__eyebrow">Console status</div>
            <div class="admin-status-list">
                <div class="admin-status-item">
                    <strong>Freshness queue</strong>
                    <p>${page.freshnessSummary()}</p>
                </div>
                <div class="admin-status-item">
                    <strong>Stale scopes</strong>
                    <p>${page.staleFreshnessRows().size()} scope(s) are currently overdue.</p>
                </div>
                <div class="admin-status-item">
                    <strong>Tracked routes</strong>
                    <p>${page.routeRows().size()} route records are shown in the current review table.</p>
                </div>
            </div>
        </div>
    </div>
</section>

<section class="section">
    <div class="section__inner admin-panel-grid">
        <div class="admin-panel">
            <div class="section__eyebrow">Leads by partner type</div>
            @if (page.partnerMetrics().isEmpty())
                <div class="callout">
                    <p>No lead submissions are recorded in the current 28-day window.</p>
                </div>
            @else
                <ul class="guardrail-list">
                    @for (var metric : page.partnerMetrics())
                        <li>${metric.partnerLabel()} - ${metric.leadCount()}</li>
                    @endfor
                </ul>
            @endif
        </div>
        <div class="admin-panel">
            <div class="section__eyebrow">CTA clicks by route family</div>
            @if (page.routeFamilyMetrics().isEmpty())
                <div class="callout">
                    <p>No CTA activity is recorded in the current 28-day window.</p>
                </div>
            @else
                <ul class="guardrail-list">
                    @for (var metric : page.routeFamilyMetrics())
                        <li>${metric.routeLabel()} - ${metric.ctaClicks()}</li>
                    @endfor
                </ul>
            @endif
        </div>
    </div>
</section>

<section class="section section--muted">
    <div class="section__inner">
        <div class="section__eyebrow">Freshness blockers</div>
        <p>${page.freshnessSummary()}</p>
        @if (page.staleFreshnessRows().isEmpty())
            <div class="callout">
                <p>No stale source scope is blocking the current route set.</p>
            </div>
        @else
            <div class="card-grid">
                @for (var row : page.staleFreshnessRows())
                    <div class="card">
                        <span class="badge badge--alert">${row.freshnessStatus()}</span>
                        <strong>${row.scopeLabel()}</strong>
                        <p>${row.scopeType()} | ${row.reviewWindow()} | next review ${row.nextReviewOn()}</p>
                        <p>${row.affectedRoutes()} routes blocked (${row.affectedIndexableRoutes()} indexable, ${row.affectedHeldRoutes()} held).</p>
                        <ul class="mini-list">
                            @for (var sourceTitle : row.sourceTitles())
                                <li>${sourceTitle}</li>
                            @endfor
                        </ul>
                    </div>
                @endfor
            </div>
        @endif
    </div>
</section>

<section class="section">
    <div class="section__inner">
        <div class="section__eyebrow">Freshness review queue</div>
        <div class="table-wrap">
            <table class="data-table data-table--admin">
                <thead>
                <tr>
                    <th>Scope</th>
                    <th>Type</th>
                    <th>Status</th>
                    <th>Verified</th>
                    <th>Next review</th>
                    <th>Window</th>
                    <th>Affected routes</th>
                    <th>Sources</th>
                </tr>
                </thead>
                <tbody>
                @for (var row : page.freshnessRows())
                    <tr>
                        <td>${row.scopeLabel()}</td>
                        <td>${row.scopeType()}</td>
                        <td>${row.freshnessStatus()}</td>
                        <td>${row.verifiedOn()}</td>
                        <td>${row.nextReviewOn()}</td>
                        <td>${row.reviewWindow()}</td>
                        <td>${row.affectedRoutes()} total (${row.affectedIndexableRoutes()} indexable, ${row.affectedHeldRoutes()} held)</td>
                        <td>
                            <div class="stack-tight">
                                @for (var sourceTitle : row.sourceTitles())
                                    <div>${sourceTitle}</div>
                                @endfor
                            </div>
                        </td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    </div>
</section>

<section class="section section--muted">
    <div class="section__inner">
        <div class="section__eyebrow">Route status review</div>
        <div class="table-wrap">
            <table class="data-table data-table--admin">
                <thead>
                <tr>
                    <th>Path</th>
                    <th>Scope</th>
                    <th>Route</th>
                    <th>Phase</th>
                    <th>Index</th>
                    <th>Freshness</th>
                    <th>CTA clicks</th>
                    <th>Lead opens</th>
                    <th>Lead submissions</th>
                    <th>Recommendation</th>
                </tr>
                </thead>
                <tbody>
                @for (var row : page.routeRows())
                    <tr>
                        <td>${row.path()}</td>
                        <td>${row.scopeLabel()}</td>
                        <td>${row.routeLabel()}</td>
                        <td>${row.phase()}</td>
                        <td>${row.indexStatus()}</td>
                        <td>${row.freshnessStatus()}</td>
                        <td>${row.ctaClicks()}</td>
                        <td>${row.leadOpens()}</td>
                        <td>${row.leadSubmissions()}</td>
                        <td>${row.recommendation()} - ${row.recommendationReason()}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    </div>
</section>
`)

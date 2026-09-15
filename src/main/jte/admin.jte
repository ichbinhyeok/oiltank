@import owner.buriedoiltank.pages.PageModels.AdminPageModel
@param AdminPageModel page
@param String csrfParameterName
@param String csrfToken
@template.layout.page(meta = page.meta(), activeNav = "admin", content = @`
<section class="section admin-shell">
    <div class="section__inner admin-shell__inner">
        <div class="admin-hero">
            <div class="admin-hero__copy">
                <div class="section__eyebrow">Founder research operations</div>
                <h1>Property research case desk</h1>
                <p>Review new property submissions, notification delivery, active case state, service demand, and route evidence from one protected surface.</p>
                <div class="admin-pill-row"><span class="admin-pill">Basic auth required</span><span class="admin-pill">Search indexing blocked</span><span class="admin-pill">PII stays inside operations</span></div>
            </div>
            <div class="admin-note-card"><strong>Current route review</strong><p>${page.reviewSummary()}</p><p>${page.freshnessSummary()}</p><p class="admin-readiness">${page.notificationReadiness()}</p></div>
        </div>
        <div class="metric-grid metric-grid--admin">
            @for (var metric : page.metrics())
                <div class="metric-card metric-card--admin"><span>${metric.label()}</span><strong>${metric.value()}</strong></div>
            @endfor
        </div>
    </div>
</section>

<section class="section" id="case-desk">
    <div class="section__inner">
        <div class="section__eyebrow">Case queue</div>
        <h2>Research intake and delivery status</h2>
        <p>Every case begins at intake. Status changes are append-only so the original submission and operating history remain intact.</p>
        @if (page.caseRows().isEmpty())
            <div class="callout"><p>No property research cases have been submitted yet.</p></div>
        @else
            <div class="lead-review-list case-review-list">
                @for (var researchCase : page.caseRows())
                    <article class="lead-review-row case-review-row">
                        <div class="case-review-row__summary">
                            <div class="case-review-row__head">
                                <span class="badge badge--case">${researchCase.caseStatus()}</span>
                                <span class="badge ${researchCase.notificationStatus().equals("sent") ? "badge--approved" : researchCase.notificationStatus().equals("failed") ? "badge--alert" : ""}">mail: ${researchCase.notificationStatus()}</span>
                                <span class="badge ${researchCase.customerReceiptStatus().equals("sent") ? "badge--approved" : researchCase.customerReceiptStatus().equals("failed") ? "badge--alert" : ""}">receipt: ${researchCase.customerReceiptStatus()}</span>
                                <span class="measure">${researchCase.submittedAt()}</span>
                            </div>
                            <h3>${researchCase.propertyAddress()}</h3>
                            <dl class="case-detail-grid">
                                <div><dt>State</dt><dd>${researchCase.stateSlug()}</dd></div>
                                <div><dt>County / municipality</dt><dd>${researchCase.countyMunicipality()}</dd></div>
                                <div><dt>Role</dt><dd>${researchCase.userRole()}</dd></div>
                                <div><dt>Tank status</dt><dd>${researchCase.tankStatus()}</dd></div>
                                <div><dt>Primary question</dt><dd>${researchCase.primaryQuestion()}</dd></div>
                                <div><dt>Deadline</dt><dd>${researchCase.deadline().isBlank() ? "Not provided" : researchCase.deadline()}</dd></div>
                                <div><dt>Documents</dt><dd>${researchCase.hasDocuments()}</dd></div>
                                <div><dt>Email</dt><dd><a href="mailto:${researchCase.email()}">${researchCase.email()}</a></dd></div>
                            </dl>
                            @if (!researchCase.notes().isBlank())<div class="case-notes"><strong>Customer notes</strong><p>${researchCase.notes()}</p></div>@endif
                            @if (!researchCase.documents().isEmpty())
                                <div class="case-evidence"><strong>Secure case documents</strong><ul>
                                    @for (var document : researchCase.documents())
                                        <li><a href="/admin/cases/${researchCase.leadId()}/documents/${document.documentId()}">${document.originalName()}</a><span>${document.contentType()} · ${document.sizeLabel()}</span></li>
                                    @endfor
                                </ul></div>
                            @endif
                            @if (!researchCase.activities().isEmpty())
                                <div class="case-timeline"><strong>Case activity</strong><ol>
                                    @for (var activity : researchCase.activities())
                                        <li><div><span>${activity.activityType()}</span><time>${activity.timestamp()}</time></div>
                                            @if (!activity.notes().isBlank())<p>${activity.notes()}</p>@endif
                                            <small>${activity.agency().isBlank() ? "" : activity.agency()} ${activity.outcome().isBlank() ? "" : "· " + activity.outcome()} ${activity.nextAction().isBlank() ? "" : "· Next: " + activity.nextAction()}</small>
                                        </li>
                                    @endfor
                                </ol></div>
                            @endif
                            @if (!researchCase.notificationError().isBlank())<p class="admin-alert">Notification issue: ${researchCase.notificationError()}. The saved case is unaffected.</p>@endif
                            @if (!researchCase.customerReceiptError().isBlank())<p class="admin-alert">Customer receipt issue: ${researchCase.customerReceiptError()}. The saved case is unaffected.</p>@endif
                        </div>
                        <div class="case-review-row__actions">
                            <form class="lead-decision-form" action="/admin/cases/status" method="post">
                                <input type="hidden" name="${csrfParameterName}" value="${csrfToken}">
                                <input type="hidden" name="leadId" value="${researchCase.leadId()}">
                                <label>Case status<select name="status">
                                    @for (var status : owner.buriedoiltank.leads.CaseStatus.values())
                                        @if (status.slug().equals(researchCase.caseStatus()))
                                            <option value="${status.slug()}" selected>${status.label()}</option>
                                        @else
                                            <option value="${status.slug()}">${status.label()}</option>
                                        @endif
                                    @endfor
                                </select></label>
                                <label>Operator note<textarea name="notes" maxlength="500" rows="3">${researchCase.statusNotes()}</textarea></label>
                                <button class="button" type="submit">Save case status</button>
                            </form>
                            @if (!researchCase.notificationStatus().equals("sent"))
                                @if (researchCase.notificationStatus().equals("interrupted"))
                                    <p role="alert">Operator email delivery is unknown after restart. Check the sent mailbox before retrying to avoid a duplicate.</p>
                                @endif
                                <form action="/admin/cases/notification/retry" method="post">
                                    <input type="hidden" name="${csrfParameterName}" value="${csrfToken}">
                                    <input type="hidden" name="leadId" value="${researchCase.leadId()}">
                                    <button class="button button--secondary" type="submit">Retry operator email</button>
                                </form>
                            @endif
                            @if (!researchCase.customerReceiptStatus().equals("sent"))
                                @if (researchCase.customerReceiptStatus().equals("interrupted"))
                                    <p role="alert">Customer receipt delivery is unknown after restart. Check the sent mailbox before retrying to avoid a duplicate.</p>
                                @endif
                                <form action="/admin/cases/customer-receipt/retry" method="post">
                                    <input type="hidden" name="${csrfParameterName}" value="${csrfToken}">
                                    <input type="hidden" name="leadId" value="${researchCase.leadId()}">
                                    <button class="button button--secondary" type="submit">Retry customer receipt</button>
                                </form>
                            @endif
                            <details class="case-activity-entry">
                                <summary>Record research activity</summary>
                                <form class="lead-decision-form" action="/admin/cases/activity" method="post">
                                    <input type="hidden" name="${csrfParameterName}" value="${csrfToken}">
                                    <input type="hidden" name="leadId" value="${researchCase.leadId()}">
                                    <label>Activity type<select name="activityType" required>
                                        @for (var type : owner.buriedoiltank.leads.CaseActivityType.values())
                                            <option value="${type.slug()}">${type.label()}</option>
                                        @endfor
                                    </select></label>
                                    <label>Route ID<input name="routeId" maxlength="120" placeholder="nj-montclair-property-records"></label>
                                    <label>Agency / custodian<input name="agency" maxlength="180"></label>
                                    <label>Channel<input name="channel" maxlength="80" placeholder="portal, email, phone, viewer"></label>
                                    <label>Outcome<input name="outcome" maxlength="200" placeholder="acknowledged, delivered, bounced, referred"></label>
                                    <label>Source / request ID<input name="sourceId" maxlength="180"></label>
                                    <label>What happened<textarea name="notes" maxlength="1000" rows="3"></textarea></label>
                                    <label>Next action<textarea name="nextAction" maxlength="500" rows="2"></textarea></label>
                                    <label>Check date<input name="checkDate" type="date"></label>
                                    <button class="button" type="submit">Add immutable activity</button>
                                </form>
                            </details>
                        </div>
                    </article>
                @endfor
            </div>
        @endif
    </div>
</section>

<section class="section section--muted">
    <div class="section__inner">
        <div class="section__eyebrow">Service funnel / 28 days</div>
        <h2>Demand by case state, page, state, and question</h2>
        <div class="admin-panel-grid admin-panel-grid--four">
            <div class="admin-panel"><h3>Case status</h3><ul class="guardrail-list">@for (var metric : page.statusMetrics())<li><span>${metric.partnerLabel()}</span><strong>${metric.leadCount()}</strong></li>@endfor</ul></div>
            <div class="admin-panel"><h3>Submission state</h3><ul class="guardrail-list">@for (var metric : page.stateMetrics())<li><span>${metric.partnerLabel()}</span><strong>${metric.leadCount()}</strong></li>@endfor</ul></div>
            <div class="admin-panel"><h3>Entry page</h3><ul class="guardrail-list">@for (var metric : page.pageMetrics())<li><span>${metric.partnerLabel()}</span><strong>${metric.leadCount()}</strong></li>@endfor</ul></div>
            <div class="admin-panel"><h3>Primary question</h3><ul class="guardrail-list">@for (var metric : page.questionMetrics())<li><span>${metric.partnerLabel()}</span><strong>${metric.leadCount()}</strong></li>@endfor</ul></div>
        </div>
    </div>
</section>

<section class="section">
    <div class="section__inner admin-panel-grid">
        <div class="admin-panel">
            <div class="section__eyebrow">Operational exports</div>
            <div class="admin-export-grid">
                <a class="admin-export-card" href="/admin/exports/routes.json"><strong>Route manifest</strong><p>Canonical route inventory and metadata.</p></a>
                <a class="admin-export-card" href="/admin/exports/admin-metrics-snapshot.json"><strong>Service metrics</strong><p>28-day research funnel and breakdowns.</p></a>
                <a class="admin-export-card" href="/admin/exports/leads.csv"><strong>Research intake CSV</strong><p>Raw submissions for recovery and analysis.</p></a>
                <a class="admin-export-card" href="/admin/exports/case-status.csv"><strong>Case status history</strong><p>Append-only operating state changes.</p></a>
                <a class="admin-export-card" href="/admin/exports/notification-attempts.csv"><strong>Notification attempts</strong><p>Queued, sent, disabled, or failed operator mail attempts.</p></a>
                <a class="admin-export-card" href="/admin/exports/customer-receipt-attempts.csv"><strong>Customer receipt attempts</strong><p>Queued, sent, disabled, or failed intake confirmations.</p></a>
                <a class="admin-export-card" href="/admin/exports/case-activity.csv"><strong>Case activity ledger</strong><p>Append-only findings, requests, replies, referrals, and reusable route lessons.</p></a>
                <a class="admin-export-card" href="/admin/exports/case-documents.csv"><strong>Document register</strong><p>Private attachment metadata and integrity hashes.</p></a>
                <a class="admin-export-card" href="/admin/exports/lead-events.csv"><strong>Analytics events</strong><p>Privacy-safe internal funnel events.</p></a>
            </div>
        </div>
        <div class="admin-panel">
            <div class="section__eyebrow">Source health</div>
            <p>${page.freshnessSummary()}</p>
            <p>${page.routeRows().size()} manifest routes are tracked. ${page.staleFreshnessRows().size()} source scope(s) are overdue.</p>
        </div>
    </div>
</section>

<section class="section section--muted">
    <div class="section__inner">
        <div class="section__eyebrow">Utility funnel / 28 days</div>
        @if (page.toolFunnels().isEmpty())
            <div class="callout"><p>No utility activity is recorded in the current window.</p></div>
        @else
            <div class="table-wrap"><table class="data-table data-table--admin"><thead><tr><th>Tool</th><th>Starts</th><th>Completes</th><th>Results</th><th>Commercial triggers</th><th>CTA clicks</th><th>Submissions</th></tr></thead><tbody>
                @for (var row : page.toolFunnels())<tr><td>${row.toolId()}</td><td>${row.starts()}</td><td>${row.completes()}</td><td>${row.resultViews()}</td><td>${row.commercialTriggers()}</td><td>${row.ctaClicks()}</td><td>${row.leadSubmissions()}</td></tr>@endfor
            </tbody></table></div>
        @endif
    </div>
</section>

<section class="section">
    <div class="section__inner">
        <div class="section__eyebrow">Route manifest performance</div>
        <div class="table-wrap"><table class="data-table data-table--admin"><thead><tr><th>Path</th><th>Scope</th><th>Route</th><th>Index</th><th>Freshness</th><th>CTA clicks</th><th>Form opens</th><th>Submissions</th></tr></thead><tbody>
            @for (var row : page.routeRows())<tr><td>${row.path()}</td><td>${row.scopeLabel()}</td><td>${row.routeLabel()}</td><td>${row.indexStatus()}</td><td>${row.freshnessStatus()}</td><td>${row.ctaClicks()}</td><td>${row.leadOpens()}</td><td>${row.leadSubmissions()}</td></tr>@endfor
        </tbody></table></div>
    </div>
</section>
`)

@import owner.buriedoiltank.pages.PageModels.CtaModel
@param CtaModel cta
<section class="section section--contrast section--cta" id="case-checklist" data-cta-root>
    <div class="section__inner cta-box">
        <div class="cta-box__copy">
            <div class="section__eyebrow section__eyebrow--inverse">Optional worksheet</div>
            <h2>${cta.heading()}</h2>
            <p data-partner-helper>${cta.helperText()}</p>
            <div class="cta-preview">
                <span class="cta-preview__label">${cta.previewTitle()}</span>
                <ul class="guardrail-list guardrail-list--dense">
                    @for (var item : cta.previewItems())
                        <li>${item}</li>
                    @endfor
                </ul>
            </div>
            <button
                    class="button"
                    type="button"
                    data-cta-open
                    data-page-id="${cta.pageId()}"
                    data-page-path="${cta.pagePath()}"
                    data-route-family="${cta.routeFamily()}"
                    data-default-scenario="${cta.defaultScenario().slug()}">
                ${cta.buttonLabel()}
            </button>
            <p class="cta-box__trust">${cta.trustNote()}</p>
        </div>
        <form class="lead-form" action="/api/leads/capture" method="post" data-lead-form data-collapsed="true">
            <input type="hidden" name="pageId" value="${cta.pageId()}">
            <input type="hidden" name="pagePath" value="${cta.pagePath()}">
            <input type="hidden" name="routeFamily" value="${cta.routeFamily()}">
            <input type="hidden" name="partnerType" value="${cta.defaultPartnerType().slug()}" data-partner-type-input>
            @if (cta.stateOptions().size() == 1)
                <input type="hidden" name="stateSlug" value="${cta.stateOptions().get(0).slug()}" data-state-input>
            @else
                <label class="field">
                    <span>State</span>
                    <select name="stateSlug" data-state-input>
                        @for (var option : cta.stateOptions())
                            <option value="${option.slug()}">${option.name()}</option>
                        @endfor
                    </select>
                </label>
            @endif
            <label class="field">
                <span>Scenario</span>
                <select name="scenario" data-scenario-input>
                    @for (var scenario : cta.scenarios())
                        <option value="${scenario.slug()}" data-partner-type="${scenario.defaultPartnerType().slug()}">${scenario.displayLabel()}</option>
                    @endfor
                </select>
            </label>
            <label class="field">
                <span>Your role in the deal</span>
                <select name="userRole" required>
                    <option value="" selected disabled>Choose your role</option>
                    <option value="buyer">Buyer</option>
                    <option value="seller">Seller</option>
                    <option value="owner_prelisting">Owner preparing to list</option>
                    <option value="agent_attorney">Agent or attorney</option>
                </select>
            </label>
            <label class="field">
                <span>What do you know right now?</span>
                <select name="tankStatus" required>
                    <option value="" selected disabled>Choose the closest match</option>
                    <option value="suspected_tank">Suspected tank</option>
                    <option value="records_missing">Records missing or closure unclear</option>
                    <option value="confirmed_tank">Confirmed tank</option>
                    <option value="leak_concern">Leak or contamination concern</option>
                </select>
            </label>
            <div class="field-row">
                <label class="field">
                    <span>ZIP (optional)</span>
                    <input name="zipCode" type="text" inputmode="numeric" autocomplete="postal-code" placeholder="Optional">
                </label>
                <label class="field">
                    <span>Timing</span>
                    <select name="closingTimeline">
                        <option value="">Not tied to a closing date</option>
                        <option value="under_7_days">Under 7 days</option>
                        <option value="one_to_three_weeks">1 to 3 weeks</option>
                        <option value="one_to_two_months">1 to 2 months</option>
                        <option value="pre_listing">Pre-listing</option>
                    </select>
                </label>
            </div>
            <label class="field">
                <span>Email (required)</span>
                <input name="email" type="email" required>
            </label>
            <label class="field">
                <span>Name (optional)</span>
                <input name="name" type="text">
            </label>
            <label class="field">
                <span>Phone (optional)</span>
                <input name="phone" type="tel">
            </label>
            <label class="field">
                <span>Notes</span>
                <textarea name="notes" rows="4"></textarea>
            </label>
            <p class="form-note">Use notes for the missing permit, disclosure issue, visible pipes, sweep result, cleanup letter, or the deadline that matters most.</p>
            <button class="button button--dark" type="submit">Email the worksheet</button>
            <div class="form-status" data-form-status hidden></div>
        </form>
    </div>
</section>

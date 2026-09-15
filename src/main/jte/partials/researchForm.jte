@param String pageId
@param String pagePath
@param String defaultState = ""
@param String defaultLocality = ""
@param String defaultQuestion = ""
<form class="research-form" action="/api/leads/capture" method="post" enctype="multipart/form-data" data-research-form>
    <input type="hidden" name="pageId" value="${pageId}">
    <input type="hidden" name="pagePath" value="${pagePath}">
    <input type="hidden" name="routeFamily" value="record-research">
    <input type="hidden" name="scenario" value="records_first">
    <input type="hidden" name="partnerType" value="record_research">
    <input type="hidden" name="submissionToken" value="" data-submission-token>
    <div class="research-form__promise" aria-label="Beta service expectations"><strong>Free beta</strong><span>Initial route review target: within 2 business days</span><small>Agency response times may extend the final timeline.</small></div>
    <div class="research-form__status" role="status" aria-live="polite" tabindex="-1" data-form-status hidden></div>
    <ol class="v2-form-progress" aria-label="Research request progress" data-form-progress hidden><li aria-current="step">01<span>Property</span></li><li>02<span>Your question</span></li><li>03<span>Contact &amp; records</span></li></ol>
    <fieldset class="v2-form-step" data-form-step><legend tabindex="-1">Which property?</legend><div class="v2-form-fields">
    <label class="research-form__wide"><span>Property address</span><input name="propertyAddress" autocomplete="street-address" required maxlength="240" placeholder="Street address"></label>
    <label><span>State</span><select name="stateSlug" required><option value="" selected="${defaultState.isEmpty()}" disabled>Choose state</option><option value="new-jersey" selected="${defaultState.equals("new-jersey")}">New Jersey</option><option value="new-york" selected="${defaultState.equals("new-york")}">New York</option><option value="other">Other / not sure</option></select></label>
    <label><span>County or municipality</span><input name="countyMunicipality" value="${defaultLocality}" required maxlength="160" placeholder="County, town, city, or borough"></label>
    </div></fieldset>
    <fieldset class="v2-form-step" data-form-step><legend tabindex="-1">What would you like to know?</legend><div class="v2-form-fields">
    <label><span>Your role</span><select name="userRole" required><option value="" selected disabled>Choose your role</option><option value="buyer">Buyer</option><option value="seller">Seller</option><option value="owner">Property owner</option><option value="agent_attorney">Agent or attorney</option><option value="other">Other</option></select></label>
    <label><span>Known tank status</span><select name="tankStatus" required><option value="" selected disabled>Closest match</option><option value="unknown">Unknown / records unclear</option><option value="suspected_tank">Tank suspected</option><option value="confirmed_tank">Tank confirmed</option><option value="removed_or_closed">Reported removed or closed</option><option value="documents_conflict">Documents conflict</option></select></label>
    <label class="research-form__wide"><span>Primary question</span><select name="primaryQuestion" required data-primary-question><option value="" selected="${defaultQuestion.isEmpty()}" disabled>What do you need answered?</option><option value="find_records" selected="${defaultQuestion.equals("find_records")}">Find records that I cannot locate</option><option value="interpret_documents" selected="${defaultQuestion.equals("interpret_documents")}">Interpret documents I already have</option><option value="choose_next_step" selected="${defaultQuestion.equals("choose_next_step")}">Decide between records, a sweep, or confirmed-tank action</option><option value="verify_claim" selected="${defaultQuestion.equals("verify_claim")}">Check a seller, listing, or prior-owner claim</option><option value="agency_follow_up" selected="${defaultQuestion.equals("agency_follow_up")}">Find or follow the correct agency request route</option></select></label>
    <label><span>Transaction deadline</span><input name="deadline" type="date"></label>
    <label><span>Already have documents?</span><select name="hasDocuments" required><option value="" selected disabled>Choose one</option><option value="yes">Yes</option><option value="no">No</option><option value="partial">Some / not sure</option></select></label>
    </div></fieldset>
    <fieldset class="v2-form-step" data-form-step><legend tabindex="-1">Where should we follow up?</legend><div class="v2-form-fields">
    <label class="research-form__wide research-form__upload"><span>Attach records <small>Optional · up to 3 PDF, JPG, or PNG files · 8 MB each · 20 MB total</small></span><input name="documents" type="file" accept="application/pdf,image/jpeg,image/png" multiple data-document-upload><small class="field-help">Permit pages, closure letters, inspection sheets, seller disclosures, or screenshots are useful. Files stay in the private case workspace.</small></label>
    <label class="research-form__wide"><span>Email</span><input name="email" type="email" autocomplete="email" required maxlength="254" placeholder="you@example.com"></label>
    <label class="research-form__wide"><span>What is unclear? <small>Optional</small></span><textarea name="notes" rows="4" maxlength="2000" placeholder="For example: seller says the tank was removed, but there is no closure record."></textarea></label>
    </div><p class="research-form__privacy">Your address and email are used to research and respond to this request. They are not sent to GA4 events. When email delivery is available, you will receive a receipt number for follow-up.</p></fieldset>
    <div class="v2-form-actions" data-form-actions hidden><button class="v2-form-back" type="button" data-form-back>Back</button><button class="v2-button" type="button" data-form-next>Continue <span aria-hidden="true">→</span></button></div>
    <button class="service-button service-button--dark research-form__wide" type="submit">Send property for research</button>
</form>

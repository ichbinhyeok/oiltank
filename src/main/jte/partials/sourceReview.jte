@import owner.buriedoiltank.pages.PageModels.SourceReviewModel
@param SourceReviewModel review
<section class="section section--soft section--tight">
    <div class="section__inner">
        <div class="section__header">
            <div>
                <div class="section__eyebrow">Source status</div>
                <h2>Checked against current public guidance.</h2>
            </div>
        </div>
        <div class="review-panel">
            <div class="review-panel__stats">
                <div>
                    <strong>Status</strong>
                    <span>${review.freshnessStatus()}</span>
                </div>
                <div>
                    <strong>Verified</strong>
                    <span>${review.verifiedOn()}</span>
                </div>
                <div>
                    <strong>Next review</strong>
                    <span>${review.nextReviewOn()}</span>
                </div>
            </div>
            <p>${review.note()}</p>
        </div>
        @template.partials.editorialTeam()
    </div>
</section>

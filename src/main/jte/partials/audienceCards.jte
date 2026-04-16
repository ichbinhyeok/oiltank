@import owner.buriedoiltank.pages.PageModels.AudienceCard
@param String eyebrow
@param String heading
@param java.util.List<AudienceCard> cards
<section class="section">
    <div class="section__inner">
        <div class="section__header">
            <div>
                <div class="section__eyebrow">${eyebrow}</div>
                <h2>${heading}</h2>
            </div>
        </div>
        <div class="persona-grid">
            @for (var card : cards)
                <div class="persona-card">
                    <span class="persona-card__label">${card.label()}</span>
                    <strong>${card.title()}</strong>
                    <p>${card.description()}</p>
                    <ul class="mini-list">
                        @for (var i = 0; i < java.lang.Math.min(card.bullets().size(), 3); i++)
                            <li>${card.bullets().get(i)}</li>
                        @endfor
                    </ul>
                </div>
            @endfor
        </div>
    </div>
</section>

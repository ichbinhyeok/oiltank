# Oil Tank Route — public service design, September 2026

## Direction

A calm, contemporary property research service: architectural photography, crisp white space, quiet deep green, human language, and purposeful working surfaces. The previous ivory/orange paper-and-instrument direction is superseded for the public service experience.

This is a composition change, not just a palette update. Home, area discovery, detail reading, service explanation, sample brief, and intake have distinct task-shaped layouts. The existing calculators retain their functional layouts and data while inheriting the public brand shell. Admin stays isolated from public styling.

## Composition and content

- Home: edge-to-edge illustrative house photo; short service proposition; a clear primary action. Then the human service explanation, three question paths, a readable brief preview, locality routes, and intake.
- Directory: compact orientation followed immediately by search, state filters, and locality rows. All eight areas and their links are server-rendered. Empty search states direct users to route review without inventing coverage.
- Detail: compact location header, source review date, sticky contents, numbered research steps, official source links, a copyable request outline, limits, related questions, and contextual intake.
- Service: concise process and scope pages. No decorative stacks of fake agency letters.
- Sample: a report reader with anchored sections for overview, evidence, open questions, and next actions. Fictional evidence is clearly labeled.
- Intake: three progressive steps (property; question; contact and documents). Back navigation retains entries, intermediate steps validate locally, and only a final valid submission enters the existing backend workflow.

## Visual system

| Role | Value |
|---|---|
| Main canvas | #FFFFFF |
| Soft canvas | #F2F5F1 |
| Primary ink | #19372E |
| Action green | #214F3D |
| Secondary text | #68756E |
| Hairline | #DEE5DF |

General Sans for headings and Source Sans 3 for text/controls; self-hosted. No monospaced service headings or simulated paper scanlines. The existing measurement tools may retain their monospaced numerical readouts.

Headlines are proportioned to their role: homepage around 74px maximum, service and directory headings around 56px, detail headings around 49px, reduced on mobile. Rows and sections carry the hierarchy; bordered surfaces are reserved for actual interactive or document objects.

## Motion and behavior

- Brief hero entrance and subtle photo settling, with content present in the server response.
- Sticky detail navigation changes active section as the reader progresses.
- Link arrows, report preview alignment, and button movement reinforce interaction.
- Native mobile navigation works without JavaScript.
- Reduced-motion preference removes nonessential animation and smooth scrolling.
- Directory filters do not issue network calls or track typed search text.
- Copy request does not send an agency message.
- Without JavaScript, all three form sections and the normal submit button remain available.
- Existing privacy-safe analytics, source attribution, receipt, upload limits, and duplicate-submission protection are retained.

## Implementation

New public compositions use the v2 component classes in site-v2.css and progressive enhancements in site-v2.js. app.css remains the compatibility layer for calculators, legacy information pages, and the protected admin; no separate public palette is applied to admin. The shared form partial is used by all research entry points.

## Imagery and provenance

Public asset: src/main/resources/static/images/property-editorial.jpg (1672 × 941; approximately 488 KB). Generated with the built-in image generation tool, then lossily encoded to JPEG for web delivery without changing the composition. Original PNG is retained outside the public bundle under ignored output/design-v2-hero-source.png and in the image tool's generated-image archive.

The hero is an illustrative property, not a customer case, verified location, or claim of a physical inspection.

Generation prompt:
> Use case: photorealistic-natural. Asset type: full-bleed website hero photograph for Oil Tank Route, an independent residential property records research service. Generate a wide landscape editorial architectural photograph of a handsome but believable 1920s northeastern American suburban house, white clapboard siding, dark forest-green shutters, a brick chimney, simple front porch, mature deciduous trees, soft early morning light. The house sits mainly in the right half of the frame, with naturally dark leafy foliage and soft deep green shade on the left third providing calm space for HTML text. View from the quiet street at slight elevated eye level, full home and modest front garden visible. Natural imperfect textures, premium architectural magazine photography, restrained greens, warm neutral whites, subtle grain, credible lived-in property not a mansion. No people, no vehicles, no text, no logos, no signage, no UI, no collage, no framed panels. Wide 16:9 landscape composition. This is an illustrative visual, not a real customer property.

## Release boundary

Local review before commit, push, merge, or deployment. A visual refresh does not claim search recovery. Preserve the existing URL catalog, source citations, limits, and private-case behavior.

## Service-depth expansion

The hero now names oil tank record research rather than a generic property promise. Composition remains image-led, white/green, with a narrow text column. The content order is service promise, delegated research work, customer questions, deliverable, jurisdiction choice, then intake. Existing entrance, sticky navigation, and link feedback are retained; no new decorative motion is added.

The shared trust/policy template is a quiet reading surface with no unrelated tank-size illustration. It preserves policy text and contact details and connects service questions to private intake. The new home evidence-standard section explains parcel matching, search/request logs, source-linked conclusions, and decision boundaries without invented credentials or customer cases.

Coverage counts are generated from the catalog: 16 area routes with distinct municipal/county roles. See ops/2026-09-16_service_expansion.md for source provenance and selection limitations.

## Local verification — 2026-09-16

### Header and service follow-up review

The owner's follow-up exposed a missed vertical-layout regression at 800 px: legacy app.css changed the header to a column while the redesign fixed its height. Horizontal-overflow checks did not detect clipped branding or vertically overlapping navigation. Public header markup now uses independent v2-site-header, v2-header-inner, and v2-desktop-nav classes; the legacy admin header is preserved. Navigation collapses at 960 px and uses native details with Escape support. Regression checks must test header child containment, not just page width.

The visual direction remains a quiet white/green property-research service. The header's job is orientation; service content explains the process, evidence, limits, then intake. Existing hero entrance, sticky reading navigation, and restrained link feedback remain; menu state now has a matching close indicator.

Service review clarified initial review versus final delivery, missing-record outcomes, request authorization and separate agency fees, deadline limits, and coverage feasibility. The shared intake no longer implies unconditional acceptance with “We'll take it from here.” The report example remains explicitly fictional. These are expectations aligned to the existing operating model, not proof of completed customer work or guaranteed search recovery.

Local clean package passed the existing 58-test suite; the updated ResearchRoutesTests then passed, bringing the report total to 59 with zero failures/errors. No production deployment or external messages were performed.

Final browser sweep: 12 representative service, locality, problem, utility, and contact pages at 320/390/768/800/960/1024/1440 px (84 checks), with no missing H1, horizontal overflow, or branding outside the header bounds. No console errors observed. Tablet menu opening/Escape closing and the agency-fee disclosure toggle were also exercised. Screenshots are in ignored output/header-tablet-fixed.png, output/header-menu-fixed.png, output/header-desktop-fixed.png, and output/service-review-mobile.png. This is local UI verification, not a new live customer fulfillment test.

- Clean packaged build: 58 tests passed, no failures or errors.
- Browser review: 20 public pages at 320, 768, and 1440 px; no horizontal overflow or console errors in those checks. Final mobile header separately verified at 390 px after its correction.
- Directory search, state filtering, and empty results verified. Three-step intake validation, backward navigation, retained input, and a local test receipt verified with notifications disabled.
- Clipboard permission was denied in the test browser; the explicit select-and-copy fallback appeared. Successful clipboard access was not verified.
- Unenhanced form availability is covered by server-rendered assertions; a JavaScript-disabled browser session was not run.
- Core service pages have new compositions. Existing utility pages retain their functional layouts under the new public shell. No production deployment performed.

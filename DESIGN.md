# Oil Tank Route design system

## Product thesis

Oil Tank Route is a residential heating-oil field instrument: a bright, readable technical manual wrapped around a dark calculation surface. It helps a homeowner identify a tank, estimate fuel, understand risk, and call the right kind of professional without turning every visit into a removal lead.

## Visual thesis

Ivory paper, ink-navy typography, safety-orange controls, measured rules, and tank-section diagrams create the feeling of a modern field instrument rather than a lifestyle blog or generic SaaS landing page.

## Content sequence

1. Identify the job and choose a tool.
2. Show verified tank dimensions and source boundaries without JavaScript.
3. Run the calculation or risk check in a dark instrument panel.
4. Explain the result, uncertainty, and safe next action.
5. Offer a commercial route only when the result supports inspection, replacement, leak response, sweep, or removal.
6. For delivery checks, preserve a printable Tank Passport locally without turning the result into an accusation or account signup.

## Interaction thesis

- A short gauge-line entrance gives the first viewport physical presence.
- Tool results reveal in place and move focus to a live result heading.
- Delivery status uses one restrained instrument light and a chart-range reveal; the paper Tank Passport becomes the stable artifact below it.
- Orange rules and underlines sharpen hover and keyboard focus; there are no ornamental carousels or continuous motion.
- `prefers-reduced-motion: reduce` removes nonessential transitions and animation.

## Tokens

| Role | Token | Value |
|---|---|---|
| Paper | `--paper` | `#F3F0E8` |
| Ink | `--ink` | `#10243B` |
| Instrument | `--instrument` | `#0D1F33` |
| Safety accent | `--safety` | `#E45B2A` |
| Muted ink | `--ink-muted` | `#536273` |
| Light rule | `--rule` | `#C8C4BA` |
| Dark rule | `--instrument-rule` | `#35506B` |
| Success | `--safe` | `#287257` |
| Warning | `--warn` | `#C34824` |

## Typography

- Headings and brand: General Sans, self-hosted WOFF2 under the Fontshare/ITF Free Font License.
- Body and controls: Source Sans 3, self-hosted WOFF2 under SIL OFL 1.1.
- Measurements and result numbers: IBM Plex Mono, self-hosted WOFF2 under SIL OFL 1.1.
- System fallbacks are declared for every family. The layout reserves stable line height and does not depend on remote font delivery.

License copies live beside the font assets in `src/main/resources/static/fonts/`.

## Layout and accessibility

- Mobile-first with breakpoints at 48rem and 72rem.
- Reading measure is capped at 72 characters; instrument panels may use the wider site grid.
- Minimum control height is 44px; focus is a visible 3px safety-orange outline.
- Body and interactive colors meet WCAG AA on their intended surfaces.
- Public dark mode is intentionally absent in v1; only calculator/result surfaces use the dark instrument token.
- Tables keep semantic markup and gain horizontal overflow only inside their own wrapper.

## Graphic language

Tank sections, dimension arrows, fill lines, calibration ticks, and crosshair marks are inline SVG. There are no stock house photographs. Diagrams are decorative when adjacent text carries the same information; meaningful SVG receives an accessible label.

## Anti-patterns

- No purple gradients, glass effects, pill clouds, card mosaics, or oversized rounded containers.
- No email gate for results.
- No normal-fuel or low-fuel removal CTA.
- No duplicated manufacturer tables or FAQs across tool routes.
- No detailed tool values in URLs or analytics events.

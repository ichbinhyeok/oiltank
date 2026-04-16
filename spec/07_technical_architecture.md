# 07 Technical Architecture

## Build goal
Implement a server-rendered decision site with a file-backed content and data pipeline, minimal operational drag, and simple lead capture.

## Suggested package root
- `src/main/java/owner/buriedoiltank`
- `src/test/java/owner/buriedoiltank`

## Suggested package map
- `owner.buriedoiltank.data`
- `owner.buriedoiltank.ingest`
- `owner.buriedoiltank.pages`
- `owner.buriedoiltank.leads`
- `owner.buriedoiltank.web`
- `owner.buriedoiltank.ops`

## Core application services

### Repository layer
- loads normalized state, route, guide, and source records
- no runtime database

### Route builder
- turns state and guide records into a canonical route inventory
- applies index or noindex defaults

### Page service
- builds home, state, guide, and route view models
- centralizes CTA logic by scenario

### Lead service
- receives lead submissions
- writes CSV or JSONL storage
- exposes simple aggregate views for admin later

### Event logger
- logs CTA clicks and form events
- keeps enough route context for later analysis

## Recommended storage
- `storage/leads/leads.csv`
- `storage/leads/lead_events.csv`
- `storage/ops/*.json`

## Recommended templates
- `home.jte`
- `state.jte`
- `route.jte`
- `guide.jte`
- `admin.jte`

## Recommended data files
- `src/main/resources/data/normalized/states/*.json`
- `src/main/resources/data/normalized/guides/*.json`
- `src/main/resources/data/derived/routes.json`

## Suggested runtime endpoints
- `GET /`
- `GET /states/{state}/`
- `GET /states/{state}/{route}/`
- `GET /guides/{slug}/`
- `POST /api/leads/capture`
- `POST /api/leads/event`
- `GET /admin`

## Technical constraints
- no address-resolution system in phase 1
- no broad provider marketplace in phase 1
- no user accounts
- no runtime relational database

## Testing requirements
- route rendering tests
- canonical and robots tests
- lead capture tests
- route inventory tests
- CTA logic tests by scenario

## Architecture principle
Keep the code aligned with the decision engine:

- trigger state in
- next action out

Do not bury that logic in templates.

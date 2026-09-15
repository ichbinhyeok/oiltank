# Route intelligence operating model

Oil Tank Route keeps agency knowledge as structured operational data rather than reconstructing it from old email threads.

## Runtime source

- Bundled baseline: `src/main/resources/data/normalized/records/lookup-sources.json`
- Persistent production override: `${BURIED_OIL_TANK_STORAGE_ROOT}/records/route-intelligence.json`
- The override is a **partial patch**, not a full snapshot. A one-record override never removes unrelated NJ/NY baseline routes.
- Records merge by normalized `stateSlug + jurisdiction + sourceType + agency`. Matching keys replace the baseline record; new keys are appended.
- Whitespace and letter case do not create a new key. Changing the agency name intentionally creates a different route, so use the reviewed agency identity consistently.
- If the override is missing or empty, the app uses the reviewed bundled baseline. If it is malformed, contains a duplicate composite key, has a blank required field, has an invalid public URL/date, or contains empty identifier/search lists, the **entire override is rejected** and the baseline remains active with a server warning.

The production override must live outside the release directory. A deployment must never overwrite it.

## One route record

Each record stores:

- `stateSlug`, `jurisdiction`, and `sourceType`
- agency and public portal title/URL
- supported search keys and why the source is useful
- evidence caveat
- identifiers that must be preserved
- request method and request conditions
- fee and response-time expectation
- fallback route
- verification date

## Case-close update

After a case produces new route knowledge:

1. Record the actual finding, request, bounce, referral, reply, or delivery in the protected case activity ledger while the evidence is fresh.
2. Add a `route-lesson` activity with the stable route ID, custodian, channel, outcome, source/request ID, next action, and check date.
3. Confirm reusable information against the agency, portal, or returned record, not only an unverified third-party claim.
4. Copy only the verified record being changed into the override file. Keep the exact composite-key fields when updating an existing route; change one only when adding a genuinely distinct route.
5. Record observed request conditions, fee, acknowledgment timing, delivery timing, and fallback.
6. Keep property addresses, customer emails, and case narratives out of route intelligence.
7. Check that no two override records share the normalized composite key and validate the JSON by running `.\mvnw.cmd test`.
8. Promote durable changes into the bundled baseline in the next reviewed release.

Request tracking belongs in the individual case record. Reusable agency knowledge belongs here.

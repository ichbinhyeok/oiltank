# Record research operations

The protected `/admin/` case desk is the working surface for every property request.

## Source of truth

- `storage/leads/leads.csv`: immutable intake facts
- `storage/leads/case-status.csv`: append-only case-state changes
- `storage/leads/notification-attempts.csv`: operator-alert delivery attempts
- `storage/leads/customer-receipt-attempts.csv`: customer intake-receipt delivery attempts
- `storage/operations/case-activity.csv`: append-only research, request, reply, delivery, and route-learning history
- `storage/operations/case-documents.csv`: private attachment register with SHA-256 integrity hashes
- `storage/cases/<lead-id>/documents/`: private uploaded source files
- `storage/records/route-intelligence.json`: verified reusable route overrides without customer PII

Email and browser history are evidence. They are not the status database.

## Interrupted email delivery

On startup, mail attempts left queued by the previous process are appended as
`interrupted / delivery_unknown_after_restart`. This does not mean delivery failed:
the SMTP server may have accepted a message before the process stopped.
No automatic resend occurs. Check the sent mailbox before using the protected
operator-email or customer-receipt Retry action. Retry only with authorization;
successful sends remain protected against duplicate retry.

## Operating sequence

1. Open the case and confirm property identity, question, deadline, and supplied documents.
2. Record each online finding with its source or identifier.
3. Record every external request only after it is actually submitted; preserve request number, custodian, channel, and check date.
4. Record bounces and referrals as separate events so failed routes remain useful intelligence.
5. Save returned files to the case, record what they establish, and separate that from interpretation.
6. The submitted form authorizes only the transactional intake receipt described beside the submit button. Record its actual delivery automatically. Send substantive findings, follow-ups, or other customer updates only with explicit authorization and record the actual delivery.
7. Close with the delivered brief, remaining gap, and reusable route lesson.

## Route-learning promotion

Use a stable route ID such as `nj-montclair-municipal-property-file`. A repeated successful or failed route belongs first in the case activity ledger. Promote it to route intelligence only after the agency, portal, or response verifies the custodian, required identifiers, fee, timing, and fallback. This keeps the historical observation and the current best route separate.

## Private document lifecycle

1. Accept only documents needed for the stated property-research question. Preserve the actual uploaded file, original filename, content type, byte size, integrity hash, and private relative path in the ignored case workspace and document register.
2. Keep uploads and unredacted evidence out of GA4, public pages, route intelligence, screenshots, logs, and repository commits. Use redacted derivatives only when a user has explicitly authorized a public example.
3. Retain a case file only while it is needed for active research, delivery, follow-up resolution, or a legitimate operational obligation. The current file-backed system does not claim automatic timed deletion.
4. When the retention need ends or a verified customer deletion request is accepted, remove the stored document files and the corresponding private document-register rows together. Record a non-document case activity describing the deletion outcome without reproducing the deleted content.
5. Before closing the deletion task, verify that the document download route returns not found and that no working copy remains in temporary, export, or research folders.

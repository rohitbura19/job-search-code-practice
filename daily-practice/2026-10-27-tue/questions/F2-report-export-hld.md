# F2. Async report export service (HLD, 12 min out loud + 5 min self-review)

A dashboard user clicks "Export" on up to 50 million rows of transactions and gets a CSV/XLSX download link by email or in-app.

Cover: API (`POST /exports` -> `202` + job id; `GET /exports/{id}` status; signed download URL), the job state machine (QUEUED, RUNNING, DONE, FAILED, EXPIRED), how workers read huge result sets (keyset pagination or a read replica / snapshot, never `OFFSET`), streaming the file to object storage in multipart chunks (no whole file in memory), idempotency (same filter + user within 5 minutes returns the same job), fairness and protection (per-tenant concurrency caps, a priority queue, row/size limits, cancel), retries and resumability (checkpoint the last key), security (the export must re-check permissions at run time, signed short-lived URLs, PII masking), retention (lifecycle rule deletes files after 7 days), and numbers: 2k exports/day, p99 50M rows in 15 minutes.
Say out loud: what you do when a worker dies at 80%, and why a database query result must not be kept open across the whole job.

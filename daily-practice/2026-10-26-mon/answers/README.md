# Reference solutions (open only after trying)

Only files you must write are here (given types stay in `src/main/java`). The Java files mirror `src/main/java`; to check them, copy `answers/java/*` over `src/main/java/` in a scratch copy and run `mvn test`.
The Spring solution is under `e-spring-refunds/src/` (copy over the project's `src/`). Each file's header comment has the idea and the complexity.

| Item | File | Time | Space |
|---|---|---|---|
| A1 | java/.../a1/MergeSorted.java | O(m + n); O(n) | O(1) |
| A2 | java/.../a2/Trees.java | O(n) both | O(h) / O(w) |
| B1 | java/.../b1/PaymentStats.java | O(n) per method, sorted maps and top-n O(n log n) | O(n) |
| C1 | java/.../c1/WordSearch.java | O(R*C*3^L) worst case, pruned by letter counts | O(L) recursion |
| C2 | java/.../c2/DecodeWays.java | O(n); listing O(count * n) | O(1); O(n) recursion |
| D1 | java/.../d1/EditDistance.java | O(n*m); banded O(n*k) | O(min(n, m)); O(m) |
| E | e-spring-refunds/src/... | - | - |
| G | java/.../g/ResumableUploadService.java | put O(chunk), missing O(chunks), complete O(bytes) | O(chunks) metadata |
| H1 | java/.../h1/H1Predictions.java | - | the reasoning is in the header |
| H2 | java/.../h2/EventBus.java | publish O(subscribers); subscribe O(subscribers) (copy-on-write) | O(subscribers) |

F1 / F2 (HLD) anchors.
**F1:** the client calls `start` (file name, size, chunk size) and gets an upload id plus pre-signed URLs per chunk (or one multipart-upload id); chunks go straight to object storage, so the app servers handle only small JSON calls; the metadata row `{uploadId, owner, size, chunkSize, state, expiresAt, digest}` lives in a database with a TTL index, received chunks are tracked by the storage's own part list (or a bitmap), so `status` is derived and a crashed client simply asks "what is missing"; each chunk carries a checksum (`Content-MD5`/`x-amz-checksum`), `complete` is idempotent, verifies the part list, assembles the object and publishes an event; a scanner (AV, content-type sniffing) moves the object from a quarantine bucket to the public one before the case shows it; a lifecycle rule aborts incomplete multipart uploads after N days; per-user quotas and rate limits at the gateway.
**F2:** services write an event to their own outbox table, a relay publishes to Kafka (`key = caseId`, so per-case order holds); a timeline writer consumes idempotently (dedupe on `eventId`) and appends to a wide-column store partitioned by `customerId` and clustered by `(time desc, eventId)`; reads use a cursor `(time, eventId)` so pages stay stable while new events arrive; fan-out on write is cheap here because each event belongs to one customer, the hybrid applies to B2B accounts (one enterprise with huge event volume: bucket the partition by day); visibility is a field on the event (`CUSTOMER` / `AGENT_ONLY`) filtered at the API, not in the UI; ordering uses the producer's event time plus a per-case sequence number, never the arrival time; late or replayed events are inserted at their time position.

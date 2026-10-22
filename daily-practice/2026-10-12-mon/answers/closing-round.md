# Answers: closing round

(Spoilers. The sheet gives only the hints inside each question; there is no longer reference.)

1. Expiry job on 10 pods, 500M rows: an index on `(status, expires_at)` so the update does not scan the table; partition the table by time and drop or archive old partitions; run the bulk update in batches with a limit so locks and transactions stay short; the query is idempotent, but ShedLock (or a single leader) avoids ten pods doing the same work.
2. Wrongly blocked at 40% of the limit: clock skew between nodes, shard imbalance (the sub-bucket of limit/N is hit first), key collisions, NAT/IP sharing, stale config.
3. 1 MB for 10^10 numbers: you give up exactness and accept an approximation with a known error; use a bounded-memory sketch (a fixed histogram for a small range, or t-digest / HDR histogram) instead of two heaps.
4. A command must not run twice when the charger reconnects and the cloud retries: a command id (idempotency key) that the device records, so a retry of an already-executed id is acknowledged without being executed again; the cloud tracks the command state (PENDING, SENT, ACKED, EXPIRED) with optimistic locking; delivery is at-least-once, execution is made idempotent by the id; commands carry an expiry.

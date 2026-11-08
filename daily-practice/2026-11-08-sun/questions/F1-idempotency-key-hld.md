# F1 - Idempotency-key service for a payments API (HLD, 12 min + 5 review)

A public `POST /payments` API must be safe to retry. Clients send an `Idempotency-Key` header; the same key + same payload must run the payment at most once and always return the same response, even if the first attempt is still running, crashed, or its response was lost.

Requirements: 20k requests/s peak, keys retained 24 h, p99 added latency < 10 ms, no double charge under any failure, a different payload under the same key is rejected (422), concurrent duplicates get 409 "in progress" (or wait briefly), multi-region active/passive.
Discuss: fingerprinting the request, the key's state machine (IN_PROGRESS with lease -> COMPLETED), where to store it (Redis vs SQL, atomic `SET NX` vs unique constraint), crash between "charge done" and "response saved", how the downstream PSP call is made idempotent too, key scope (per merchant), cleanup, and what you do when the store is down (fail closed or open?).

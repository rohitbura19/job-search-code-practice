# G - LLD of F1: in-memory `IdempotencyService` (sketch 8 min, code 22 min)

Implement `g/IdempotencyService` (given: `StoredResponse`, sealed `Outcome` = `Started(token) | InProgress | Replay(response) | Mismatch`). The class Javadoc is the full spec: begin/complete/abort with fencing tokens, lease expiry, retention, purge. Time comes from the injected `Clock`.

Must hold: exactly one `Started` among concurrent `begin` calls for a live key; a worker whose lease was taken over cannot complete; retention counts from completion. Sketch first in `my-designs/G-classes-sketch.md`. Test: `IdempotencyServiceTest` (includes 16-thread contention).

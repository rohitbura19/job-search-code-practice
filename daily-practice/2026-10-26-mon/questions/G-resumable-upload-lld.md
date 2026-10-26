> Code: `src/main/java/practice/d20261026/g/` (`UploadSpec`, `PutResult`, `UploadException`, `ChunkStore`, `InMemoryChunkStore`, `UploadService` are given; implement `ResumableUploadService`). Test: `ResumableUploadServiceTest`. **35 min (sketch 10, code 25).**

# G. LLD of F1: the resumable upload session

First fill `my-designs/G-classes-sketch.md` (classes, state, who owns which lock), then implement `ResumableUploadService`. The contract is the Javadoc of `UploadService`; read it twice before coding, the error order matters.

Rules to get right: all state-changing calls on ONE upload are atomic (two threads sending chunk 0 with different bytes: exactly one `STORED`, the other `CONFLICT`); different uploads must not block each other; expiry is checked against the injected `Clock` (never `Instant.now()`); `EXPIRED` is reported once, then the session is gone (`NOT_FOUND`) and its chunks are freed; `complete` is idempotent and returns the SHA-256 of the whole file in chunk order.

Say out loud: why per-session locking and not one big `synchronized` or only `ConcurrentHashMap`; why identical re-sends are `DUPLICATE` and not an error; what you would change to not hold chunk bytes in the service (checksums per chunk, the store verifies); what happens to a half-finished upload when the node restarts (where would the session record live).

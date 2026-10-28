> Code: `src/main/java/practice/d20261028/g/` (`AuditEvent`, `AuditQuery`, `AuditLog` and `AuditChain.hash` are given; implement `AuditChain.verify` and `InMemoryAuditLog`). Test: `InMemoryAuditLogTest`. Sketch classes first in `my-designs/G-classes-sketch.md`. **40 min (sketch 10, code 30).**

# G. LLD of F1: tamper-evident audit log

Implement the core of the F1 service in one JVM:
- `append(actor, action, resource)`: assigns `seq` 1, 2, 3, ..., links each event to the previous one by hash (`prevHash`, the first uses `GENESIS`), time never goes backwards even if the clock does. Blank or null arguments -> `IllegalArgumentException`.
- `query(AuditQuery)`: filter by actor and/or resource and a half-open time range `[from, to)`, keyset paging with `afterSeq`, ascending seq, at most `limit`. A rare actor or a late `afterSeq` must NOT cost O(log size): the test runs 15,000 queries on a 200,000-event log in 3 s.
- `AuditChain.verify(list)`: true only for an untampered segment (edited field, re-hashed edit, deleted or swapped event, forged start all fail).
- Thread-safe: 8 threads x 1000 appends must give seq 1..8000 and a valid chain.

Think about: what `hash` must cover and why the `|` separator is escaped; what `verify` can NOT detect (truncating the tail) and how an external anchor fixes that; which index serves which query.

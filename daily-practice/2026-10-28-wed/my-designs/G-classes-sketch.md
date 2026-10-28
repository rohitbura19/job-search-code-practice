# G. Audit log LLD sketch

## Classes and responsibilities (AuditLog, InMemoryAuditLog, AuditEvent, AuditQuery, AuditChain)

## State (event list, per-actor index, per-resource index) and the invariant between them

## Operations as step lists (append, query, verify), each with its atomicity boundary

## Concurrency choice (one monitor vs read-write lock vs lock-free append) and why

## Edge cases (clock going back, blank input, afterSeq past the end, empty segment, separator characters in values)

## What changes with a real store (append-only table with a sequence, per-actor index, hash column, external anchor)

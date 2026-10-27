# G. Lock service LLD sketch

## Classes and responsibilities (LockService, Lease, InMemoryLockService, per-resource Slot)

## State per resource (what must survive release and expiry, and why)

## Operations as step lists (tryAcquire, renew, release, current), each with its atomicity boundary

## Concurrency choice (compute per key vs one lock vs CAS) and the invariant it protects

## Edge cases (own owner re-acquire, release after expiry, renew after supersede, ttl <= 0, nulls)

## What changes with a real store (etcd lease / Redis SET NX PX / SQL row)

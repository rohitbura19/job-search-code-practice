> Code: `src/main/java/practice/d20261027/g/InMemoryLockService.java` (`LockService` and `Lease` are given). Test: `InMemoryLockServiceTest`. **35 min (sketch 10, code 25).**

# G. LLD of F1: an in-memory lease lock with fencing tokens

Implement `InMemoryLockService(Clock)` according to the contract in the `LockService` Javadoc:
- a lease is live while `now < expiresAt`; a live lease blocks everyone, including its own owner;
- every NEW grant on a resource gets token = previous + 1 (first is 1), also after release or expiry; renewals keep the token;
- `renew` / `release` succeed only for the current live lease (same owner and token); stale holders get empty / false;
- thread-safe: 32 threads racing for one resource produce exactly one winner; tokens are unique and increasing under churn;
- no background thread: expiry is evaluated on access with the injected `Clock` (the tests drive time).

First sketch the classes and the per-resource state in `my-designs/G-classes-sketch.md`. Say out loud: why a per-key atomic `compute` beats one global lock, and why the token (not the lease) is what protects the downstream resource.

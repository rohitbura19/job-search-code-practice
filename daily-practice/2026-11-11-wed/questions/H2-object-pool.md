# H2 - OOP: bounded object pool with leases (20 min) - `h2/ObjectPool.java`

Implement `ObjectPool<T>` (contract in the class Javadoc): lazy creation up to `capacity`, reuse of idle objects, `tryAcquire()` returning `Optional<Lease<T>>`, an `AutoCloseable` `Lease` whose `close()` is idempotent (resets the object once and returns it), `get()` after close fails, `inUse()` / `available()`, and a pool `close()` after which no new leases are granted but outstanding ones remain valid.
Think about: who owns the object after a lease is closed (a stale lease must never free someone else's object), thread safety, and what "closed pool" means for leases returned later.

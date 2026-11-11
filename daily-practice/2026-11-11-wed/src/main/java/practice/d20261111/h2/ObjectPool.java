package practice.d20261111.h2;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A bounded, thread-safe object pool with leases (OOP: encapsulation, resource ownership, idempotent close).
 *  - at most `capacity` objects are ever alive; they are created lazily with `factory` and recycled.
 *  - tryAcquire(): empty Optional when the pool is closed or all objects are leased; otherwise a Lease on an idle object (reuse before creating).
 *  - Lease.close() is idempotent: the first call runs `reset` on the object and returns it to the pool, later calls do nothing. After close, Lease.get() throws IllegalStateException.
 *    If the pool is already closed when a lease is closed, the object is just discarded (not reset, not returned).
 *  - inUse() = leases not yet closed; available() = how many more tryAcquire calls would succeed right now (0 when closed).
 *  - ObjectPool.close(): idle objects are dropped; new tryAcquire returns empty; outstanding leases stay valid until closed. Closing twice is fine.
 *  - Constructor: capacity >= 1, factory and reset non-null, else IllegalArgumentException / NullPointerException.
 */
public final class ObjectPool<T> {
    public ObjectPool(int capacity, Supplier<? extends T> factory, Consumer<? super T> reset) { throw new UnsupportedOperationException("TODO"); }

    public Optional<Lease<T>> tryAcquire() { throw new UnsupportedOperationException("TODO"); }
    public int inUse() { throw new UnsupportedOperationException("TODO"); }
    public int available() { throw new UnsupportedOperationException("TODO"); }
    public void close() { throw new UnsupportedOperationException("TODO"); }

    public static final class Lease<T> implements AutoCloseable {
        public T get() { throw new UnsupportedOperationException("TODO"); }
        @Override public void close() { throw new UnsupportedOperationException("TODO"); }
    }
}

package practice.d20261111.h2;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** One monitor guards idle/created/inUse/closed, so each operation is atomic. The Lease holds a back-reference and an `open` flag (checked under the pool lock,
 *  so concurrent double-close returns the object once). The factory runs under the lock: acceptable for cheap objects; for slow ones reserve a slot, create outside the lock, then publish. */
public final class ObjectPool<T> {
    private final int capacity;
    private final Supplier<? extends T> factory;
    private final Consumer<? super T> reset;
    private final ArrayDeque<T> idle = new ArrayDeque<>();
    private int created, inUse;
    private boolean closed;

    public ObjectPool(int capacity, Supplier<? extends T> factory, Consumer<? super T> reset) {
        if (capacity < 1) throw new IllegalArgumentException("capacity must be >= 1");
        this.capacity = capacity;
        this.factory = Objects.requireNonNull(factory);
        this.reset = Objects.requireNonNull(reset);
    }

    public synchronized Optional<Lease<T>> tryAcquire() {
        if (closed) return Optional.empty();
        T obj;
        if (!idle.isEmpty()) obj = idle.pop();
        else if (created < capacity) { obj = factory.get(); created++; }
        else return Optional.empty();
        inUse++;
        return Optional.of(new Lease<>(this, obj));
    }

    public synchronized int inUse() { return inUse; }

    public synchronized int available() { return closed ? 0 : capacity - inUse; }

    public synchronized void close() {
        closed = true;
        idle.clear();
    }

    private synchronized boolean release(T obj) {
        inUse--;
        if (closed) { created--; return false; }
        reset.accept(obj);
        idle.push(obj);
        return true;
    }

    public static final class Lease<T> implements AutoCloseable {
        private final ObjectPool<T> pool;
        private final T obj;
        private boolean open = true;

        private Lease(ObjectPool<T> pool, T obj) { this.pool = pool; this.obj = obj; }

        public synchronized T get() {
            if (!open) throw new IllegalStateException("lease closed");
            return obj;
        }

        @Override public void close() {
            synchronized (this) {
                if (!open) return;
                open = false;
            }
            pool.release(obj);
        }
    }
}

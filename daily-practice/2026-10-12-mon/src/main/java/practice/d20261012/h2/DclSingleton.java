package practice.d20261012.h2;

import java.util.concurrent.atomic.AtomicInteger;

/** H2 - Singleton way 3: double-checked locking. The field needs ONE keyword or a half-constructed object can be seen. Test: SingletonTest. */
public final class DclSingleton {
    static final AtomicInteger CREATED = new AtomicInteger();   // GIVEN - counts constructor calls

    private static DclSingleton instance;                       // TODO: what is missing here?

    private DclSingleton() { CREATED.incrementAndGet(); }

    public static DclSingleton getInstance() {
        throw new UnsupportedOperationException("TODO");
    }
}

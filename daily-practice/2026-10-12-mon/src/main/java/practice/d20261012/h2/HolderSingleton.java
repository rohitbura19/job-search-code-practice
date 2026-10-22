package practice.d20261012.h2;

import java.util.concurrent.atomic.AtomicInteger;

/** H2 - Singleton way 2: initialization-on-demand holder (lazy, lock free). Test: SingletonTest. */
public final class HolderSingleton {
    static final AtomicInteger CREATED = new AtomicInteger();   // GIVEN - counts constructor calls

    private HolderSingleton() { CREATED.incrementAndGet(); }

    // TODO: a private static nested Holder class with the instance

    public static HolderSingleton getInstance() {
        throw new UnsupportedOperationException("TODO");
    }
}

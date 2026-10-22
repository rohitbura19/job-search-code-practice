package practice.d20261012.h2;

import java.util.concurrent.atomic.AtomicLong;

/** H2 - Singleton way 1: the enum. Give it a thread-safe sequence generator: next() returns 1, 2, 3, ... Test: SingletonTest. */
public enum EnumSingleton {
    INSTANCE;

    private final AtomicLong counter = new AtomicLong();       // GIVEN

    public long next() {
        throw new UnsupportedOperationException("TODO");
    }
}

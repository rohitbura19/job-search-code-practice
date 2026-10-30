package practice.d20261030.h2;

import java.util.function.Supplier;

/**
 * A type-safe heterogeneous container (Effective Java item 33): one value per Class key, no unchecked warnings, no casts at call sites.
 * Lookup is by EXACT class: put(Number.class, 1) is not found by get(Integer.class).
 * Primitive class literals map to their wrapper: put(int.class, 5) and get(Integer.class) refer to the same entry.
 */
public final class TypedRegistry {
    /** Store value under type. Null type or null value -> NullPointerException. A value that is not an instance of type -> ClassCastException (even if the caller used a raw Class). Replaces any earlier value. */
    public <T> void put(Class<T> type, T value) { throw new UnsupportedOperationException("TODO"); }

    /** The value stored for exactly this type, or null. */
    public <T> T get(Class<T> type) { throw new UnsupportedOperationException("TODO"); }

    /** The stored value; if absent, call the factory once, store its (non-null) result and return it. The factory must not be called when a value exists. */
    public <T> T getOrCreate(Class<T> type, Supplier<? extends T> factory) { throw new UnsupportedOperationException("TODO"); }
}

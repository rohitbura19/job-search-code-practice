package practice.d20261030.h2;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * The map is Map<Class<?>, Object>; the type safety comes from the typed methods: put checks `type.cast(value)` (a dynamic check that
 * works even for raw Class arguments) and get casts back with the same token, so a ClassCastException can only happen at put.
 * int.class.cast(5) would throw (the primitive class has no instances), hence the wrapper normalisation. O(1) per call.
 */
public final class TypedRegistry {
    private static final Map<Class<?>, Class<?>> WRAPPERS = Map.of(
            int.class, Integer.class, long.class, Long.class, double.class, Double.class, boolean.class, Boolean.class,
            char.class, Character.class, byte.class, Byte.class, short.class, Short.class, float.class, Float.class);

    private final Map<Class<?>, Object> values = new HashMap<>();

    @SuppressWarnings("unchecked")
    private static <T> Class<T> normalise(Class<T> type) {
        Objects.requireNonNull(type, "type");
        return (Class<T>) WRAPPERS.getOrDefault(type, type);
    }

    public <T> void put(Class<T> type, T value) {
        Class<T> k = normalise(type);
        Objects.requireNonNull(value, "value");
        values.put(k, k.cast(value));
    }

    public <T> T get(Class<T> type) {
        Class<T> k = normalise(type);
        return k.cast(values.get(k));
    }

    public <T> T getOrCreate(Class<T> type, Supplier<? extends T> factory) {
        T existing = get(type);
        if (existing != null) return existing;
        T created = Objects.requireNonNull(factory.get(), "factory returned null");
        put(type, created);
        return created;
    }
}

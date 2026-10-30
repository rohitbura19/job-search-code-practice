package practice.d20261030.h2;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** The signatures are the exercise: read each one (PECS) before writing the body. Do not change them. */
public final class Generics {
    private Generics() {}

    /** Largest element by natural order. Empty -> NoSuchElementException. Must accept a List<java.sql.Timestamp> (Timestamp is Comparable<Date>, not Comparable<Timestamp>). */
    public static <T extends Comparable<? super T>> T max(Collection<? extends T> items) { throw new UnsupportedOperationException("TODO"); }

    /** Append every element of src to dst. dst may be a List<Number> while src is a List<Integer>; dst may be a List<Object>. */
    public static <T> void copy(List<? super T> dst, List<? extends T> src) { throw new UnsupportedOperationException("TODO"); }

    /** The key with the greatest value; ties -> the first one in iteration order; empty map -> Optional.empty(). */
    public static <K, V extends Comparable<? super V>> Optional<K> argMax(Map<K, V> m) { throw new UnsupportedOperationException("TODO"); }

    /** Sum of doubleValue() of every element (a List<Integer>, a Set<Double>, a List<? extends Number> all work). Empty -> 0.0. */
    public static double sum(Collection<? extends Number> nums) { throw new UnsupportedOperationException("TODO"); }

    /** Reverse in place. The parameter is List<?>, so you cannot call list.set(i, list.get(j)) directly: use a private generic helper (wildcard capture). */
    public static void reverseInPlace(List<?> list) { throw new UnsupportedOperationException("TODO"); }
}

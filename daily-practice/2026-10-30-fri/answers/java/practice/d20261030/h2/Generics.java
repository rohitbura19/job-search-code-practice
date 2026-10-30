package practice.d20261030.h2;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * PECS: a source we only READ is `? extends T` (producer), a sink we only WRITE is `? super T` (consumer).
 * max: `T extends Comparable<? super T>` lets a Timestamp use Date's compareTo; the argument is a producer of T.
 * reverseInPlace: List<?> hides the element type; the generic helper captures it as T so get/set typecheck.
 * All O(n).
 */
public final class Generics {
    private Generics() {}

    public static <T extends Comparable<? super T>> T max(Collection<? extends T> items) {
        Iterator<? extends T> it = items.iterator();
        if (!it.hasNext()) throw new NoSuchElementException();
        T best = it.next();
        while (it.hasNext()) {
            T x = it.next();
            if (x.compareTo(best) > 0) best = x;
        }
        return best;
    }

    public static <T> void copy(List<? super T> dst, List<? extends T> src) {
        for (T t : src) dst.add(t);
    }

    public static <K, V extends Comparable<? super V>> Optional<K> argMax(Map<K, V> m) {
        K bestK = null;
        V bestV = null;
        boolean any = false;
        for (Map.Entry<K, V> e : m.entrySet()) {
            if (!any || e.getValue().compareTo(bestV) > 0) { bestK = e.getKey(); bestV = e.getValue(); any = true; }
        }
        return any ? Optional.ofNullable(bestK) : Optional.empty();
    }

    public static double sum(Collection<? extends Number> nums) {
        double s = 0;
        for (Number n : nums) s += n.doubleValue();
        return s;
    }

    public static void reverseInPlace(List<?> list) { rev(list); }

    private static <T> void rev(List<T> list) {
        for (int i = 0, j = list.size() - 1; i < j; i++, j--) {
            T t = list.get(i);
            list.set(i, list.get(j));
            list.set(j, t);
        }
    }
}

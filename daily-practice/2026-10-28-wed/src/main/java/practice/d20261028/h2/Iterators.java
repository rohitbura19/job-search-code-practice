package practice.d20261028.h2;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/** Lazy iterator combinators. Nothing may be read from a source before it is needed, and each source element is read at most once. */
public final class Iterators {
    private Iterators() {}

    /** Elements of the source that satisfy p, lazily. remove() is unsupported. hasNext() may be called repeatedly without skipping elements. */
    public static <T> Iterator<T> filter(Iterator<T> source, Predicate<? super T> p) { throw new UnsupportedOperationException("TODO"); }

    /** Alternates between the sources (a1, b1, c1, a2, b2, ...); an exhausted source is skipped, the rest continue. */
    public static <T> Iterator<T> interleave(List<Iterator<T>> sources) { throw new UnsupportedOperationException("TODO"); }

    /** Flattens an iterator of iterators lazily; empty inner iterators are skipped. */
    public static <T> Iterator<T> flatten(Iterator<? extends Iterator<? extends T>> outer) { throw new UnsupportedOperationException("TODO"); }

    /** Sliding windows of size k over the source as immutable lists: [1,2,3,4] with k=2 -> [1,2], [2,3], [3,4]. Fewer than k elements -> empty. k < 1 -> IllegalArgumentException. */
    public static <T> Iterator<List<T>> windows(Iterator<T> source, int k) { throw new UnsupportedOperationException("TODO"); }
}

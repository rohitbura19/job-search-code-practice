package practice.d20261028.h2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * The common shape is "compute the next element ahead of time, cache it, clear on next()". hasNext() is idempotent because it only
 * advances when the cache is empty. interleave keeps a queue of live sources (rotate, drop when exhausted); flatten keeps the
 * current inner iterator; windows keeps a deque of the last k elements. All lazy, O(1) amortised per element (windows O(k) to copy).
 */
public final class Iterators {
    private Iterators() {}

    private abstract static class Lookahead<T> implements Iterator<T> {
        private T next;
        private boolean ready;

        /** Returns true and sets via offer(); false when exhausted. */
        abstract boolean advance();
        void offer(T t) { next = t; ready = true; }

        @Override public boolean hasNext() {
            if (!ready) advance();
            return ready;
        }

        @Override public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            T t = next;
            next = null;
            ready = false;
            return t;
        }
    }

    public static <T> Iterator<T> filter(Iterator<T> source, Predicate<? super T> p) {
        return new Lookahead<>() {
            @Override boolean advance() {
                while (source.hasNext()) {
                    T t = source.next();
                    if (p.test(t)) { offer(t); return true; }
                }
                return false;
            }
        };
    }

    public static <T> Iterator<T> interleave(List<Iterator<T>> sources) {
        Deque<Iterator<T>> live = new ArrayDeque<>(sources);
        return new Lookahead<>() {
            @Override boolean advance() {
                while (!live.isEmpty()) {
                    Iterator<T> it = live.pollFirst();
                    if (it.hasNext()) {
                        T t = it.next();
                        live.addLast(it);
                        offer(t);
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static <T> Iterator<T> flatten(Iterator<? extends Iterator<? extends T>> outer) {
        return new Lookahead<>() {
            private Iterator<? extends T> inner = Collections.emptyIterator();

            @Override boolean advance() {
                while (!inner.hasNext()) {
                    if (!outer.hasNext()) return false;
                    inner = outer.next();
                }
                offer(inner.next());
                return true;
            }
        };
    }

    public static <T> Iterator<List<T>> windows(Iterator<T> source, int k) {
        if (k < 1) throw new IllegalArgumentException("k must be >= 1");
        Deque<T> window = new ArrayDeque<>(k);
        return new Lookahead<>() {
            @Override boolean advance() {
                while (window.size() < k && source.hasNext()) window.addLast(source.next());
                if (window.size() < k) return false;
                offer(Collections.unmodifiableList(new ArrayList<>(window)));
                window.pollFirst();
                return true;
            }
        };
    }
}

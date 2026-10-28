package practice.d20261028.h2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class IteratorsTest {
    static <T> List<T> drain(Iterator<T> it) { List<T> out = new ArrayList<>(); it.forEachRemaining(out::add); return out; }

    /** Counts how many elements were pulled from it. */
    static final class Counting implements Iterator<Integer> {
        final AtomicInteger pulled = new AtomicInteger();
        final int n;
        int i;
        Counting(int n) { this.n = n; }
        @Override public boolean hasNext() { return i < n; }
        @Override public Integer next() { if (i >= n) throw new NoSuchElementException(); pulled.incrementAndGet(); return i++; }
    }

    @Test void peekDoesNotConsume() {
        PeekingIterator<String> it = new PeekingIterator<>(List.of("a", "b").iterator());
        assertEquals("a", it.peek());
        assertEquals("a", it.peek());
        assertTrue(it.hasNext());
        assertEquals("a", it.next());
        assertEquals("b", it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::peek);
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test void peekIsLazyAndHandlesNulls() {
        Counting c = new Counting(5);
        PeekingIterator<Integer> it = new PeekingIterator<>(c);
        assertEquals(0, c.pulled.get());
        assertTrue(it.hasNext());
        assertEquals(0, c.pulled.get(), "hasNext on a fresh iterator must not pull");
        it.peek();
        assertEquals(1, c.pulled.get());
        it.next(); it.next();
        assertEquals(2, c.pulled.get());
        PeekingIterator<String> nulls = new PeekingIterator<>(Arrays.asList(null, "x").iterator());
        assertNull(nulls.peek());
        assertNull(nulls.next());
        assertEquals("x", nulls.next());
    }

    @Test void filterIsLazyAndHasNextIdempotent() {
        Counting c = new Counting(10);
        Iterator<Integer> it = Iterators.filter(c, x -> x % 3 == 0);
        assertEquals(0, c.pulled.get());
        assertTrue(it.hasNext());
        assertTrue(it.hasNext());
        assertTrue(it.hasNext());
        assertEquals(1, c.pulled.get());
        assertEquals(List.of(0, 3, 6, 9), drain(it));
        assertThrows(NoSuchElementException.class, it::next);
        assertEquals(List.of(), drain(Iterators.filter(List.<Integer>of().iterator(), x -> true)));
        assertEquals(List.of(), drain(Iterators.filter(List.of(1, 2).iterator(), x -> false)));
        assertThrows(UnsupportedOperationException.class, () -> { Iterator<Integer> f = Iterators.filter(List.of(1).iterator(), x -> true); f.next(); f.remove(); });
    }

    @Test void interleaveSkipsExhaustedSources() {
        List<Iterator<Integer>> srcs = List.of(List.of(1, 2, 3).iterator(), List.<Integer>of().iterator(), List.of(10).iterator(), List.of(20, 21).iterator());
        assertEquals(List.of(1, 10, 20, 2, 21, 3), drain(Iterators.interleave(srcs)));
        assertEquals(List.of(), drain(Iterators.interleave(List.of())));
        assertEquals(List.of(), drain(Iterators.interleave(List.of(List.<Integer>of().iterator()))));
        assertEquals(List.of("x", "y"), drain(Iterators.interleave(List.of(List.of("x", "y").iterator()))));
    }

    @Test void interleaveIsLazy() {
        Counting a = new Counting(1000), b = new Counting(1000);
        Iterator<Integer> it = Iterators.interleave(List.of(a, b));
        it.next(); it.next(); it.next();
        assertEquals(2, a.pulled.get());
        assertEquals(1, b.pulled.get());
    }

    @Test void flattenSkipsEmptyInnersAndIsLazy() {
        List<Iterator<Integer>> inners = List.of(List.<Integer>of().iterator(), List.of(1, 2).iterator(), List.<Integer>of().iterator(), List.<Integer>of().iterator(), List.of(3).iterator());
        assertEquals(List.of(1, 2, 3), drain(Iterators.flatten(inners.iterator())));
        assertEquals(List.of(), drain(Iterators.flatten(List.<Iterator<Integer>>of().iterator())));
        AtomicInteger outerPulls = new AtomicInteger();
        Iterator<Iterator<Integer>> outer = new Iterator<>() {
            int i;
            public boolean hasNext() { return i < 3; }
            public Iterator<Integer> next() { outerPulls.incrementAndGet(); i++; return List.of(i * 10).iterator(); }
        };
        Iterator<Integer> flat = Iterators.flatten(outer);
        assertEquals(10, flat.next());
        assertEquals(1, outerPulls.get());
    }

    @Test void windows() {
        assertEquals(List.of(List.of(1, 2), List.of(2, 3), List.of(3, 4)), drain(Iterators.windows(List.of(1, 2, 3, 4).iterator(), 2)));
        assertEquals(List.of(List.of(1, 2, 3)), drain(Iterators.windows(List.of(1, 2, 3).iterator(), 3)));
        assertEquals(List.of(), drain(Iterators.windows(List.of(1, 2).iterator(), 3)));
        assertEquals(List.of(List.of(7), List.of(8)), drain(Iterators.windows(List.of(7, 8).iterator(), 1)));
        assertThrows(IllegalArgumentException.class, () -> Iterators.windows(List.of(1).iterator(), 0));
        Iterator<List<Integer>> it = Iterators.windows(List.of(1, 2, 3).iterator(), 2);
        List<Integer> first = it.next();
        it.next();
        assertEquals(List.of(1, 2), first, "earlier windows must not change when the window slides");
        assertThrows(UnsupportedOperationException.class, () -> first.add(9));
    }

    @Test void composedPipelineOnHugeSourceIsLazyAndFast() {
        Iterator<Integer> huge = new Counting(Integer.MAX_VALUE);
        Iterator<List<Integer>> pipeline = Iterators.windows(Iterators.filter(huge, x -> x % 2 == 0), 3);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertEquals(List.of(0, 2, 4), pipeline.next());
            assertEquals(List.of(2, 4, 6), pipeline.next());
            for (int i = 0; i < 200_000; i++) pipeline.next();
        });
    }
}

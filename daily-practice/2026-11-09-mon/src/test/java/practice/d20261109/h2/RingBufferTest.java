package practice.d20261109.h2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

class RingBufferTest {
    private static <T> List<T> list(RingBuffer<T> b) { List<T> l = new ArrayList<>(); for (T t : b) l.add(t); return l; }

    @Test void validation() {
        assertThrows(IllegalArgumentException.class, () -> new RingBuffer<String>(0));
        assertThrows(IllegalArgumentException.class, () -> new RingBuffer<String>(-3));
        assertThrows(NullPointerException.class, () -> new RingBuffer<String>(2).add(null));
    }

    @Test void fillsThenEvictsOldest() {
        RingBuffer<Integer> b = new RingBuffer<>(3);
        assertTrue(b.isEmpty());
        assertEquals(Optional.empty(), b.add(1));
        assertEquals(Optional.empty(), b.add(2));
        assertEquals(Optional.empty(), b.add(3));
        assertEquals(Optional.of(1), b.add(4));
        assertEquals(Optional.of(2), b.add(5));
        assertEquals(List.of(3, 4, 5), list(b));
        assertEquals(3, b.size());
        assertEquals(3, b.capacity());
        assertEquals(3, b.get(0));
        assertEquals(5, b.get(2));
        assertThrows(IndexOutOfBoundsException.class, () -> b.get(3));
        assertThrows(IndexOutOfBoundsException.class, () -> b.get(-1));
    }

    @Test void removeOldestWrapsAround() {
        RingBuffer<String> b = new RingBuffer<>(2);
        assertThrows(NoSuchElementException.class, b::removeOldest);
        b.add("a"); b.add("b");
        assertEquals("a", b.removeOldest());
        b.add("c");
        assertEquals(List.of("b", "c"), list(b));
        assertEquals("b", b.removeOldest());
        assertEquals("c", b.removeOldest());
        assertTrue(b.isEmpty());
        b.add("d");
        assertEquals("[d]", b.toString());
    }

    @Test void capacityOne() {
        RingBuffer<Integer> b = new RingBuffer<>(1);
        b.add(1);
        assertEquals(Optional.of(1), b.add(2));
        assertEquals("[2]", b.toString());
    }

    @Test void iteratorIsFailFast() {
        RingBuffer<Integer> b = new RingBuffer<>(3);
        b.add(1); b.add(2);
        Iterator<Integer> it = b.iterator();
        assertEquals(1, it.next());
        b.add(3);
        assertThrows(ConcurrentModificationException.class, it::next);

        Iterator<Integer> it2 = b.iterator();
        b.removeOldest();
        assertThrows(ConcurrentModificationException.class, it2::next);
        Iterator<Integer> it3 = b.iterator();
        b.clear();
        assertThrows(ConcurrentModificationException.class, it3::next);
        Iterator<Integer> it4 = b.iterator();
        b.add(9);
        assertThrows(ConcurrentModificationException.class, it4::next);
    }

    @Test void iteratorContract() {
        RingBuffer<Integer> b = new RingBuffer<>(3);
        b.add(1); b.add(2);
        Iterator<Integer> a = b.iterator(), c = b.iterator();
        assertEquals(1, a.next());
        assertEquals(1, c.next());
        assertEquals(2, a.next());
        assertFalse(a.hasNext());
        assertThrows(NoSuchElementException.class, a::next);
        assertEquals(2, c.next());
        assertThrows(UnsupportedOperationException.class, () -> b.iterator().remove());
        assertFalse(new RingBuffer<Integer>(2).iterator().hasNext());
    }

    @Test void equalsHashCodeIgnoreCapacityAndHeadOffset() {
        RingBuffer<Integer> x = new RingBuffer<>(3), y = new RingBuffer<>(5);
        for (int i = 1; i <= 5; i++) x.add(i);   // head has moved: holds 3,4,5
        for (int i = 3; i <= 5; i++) y.add(i);
        assertEquals(x, y);
        assertEquals(x.hashCode(), y.hashCode());
        assertEquals(x, x);
        y.add(6);
        assertNotEquals(x, y);
        assertNotEquals(x, null);
        assertNotEquals(x, List.of(3, 4, 5));
        assertEquals(new RingBuffer<String>(1), new RingBuffer<String>(9));
        assertEquals(new RingBuffer<String>(1).hashCode(), new RingBuffer<String>(9).hashCode());
        Set<RingBuffer<Integer>> set = new HashSet<>(List.of(x));
        assertTrue(set.contains(y.size() == 4 ? x : y));
    }

    @Test void toStringAndClear() {
        RingBuffer<String> b = new RingBuffer<>(3);
        assertEquals("[]", b.toString());
        b.add("a"); b.add("b"); b.add("c"); b.add("d");
        assertEquals("[b, c, d]", b.toString());
        b.clear();
        assertEquals("[]", b.toString());
        assertEquals(0, b.size());
        assertEquals(Optional.empty(), b.add("x"));
        assertEquals("[x]", b.toString());
    }

    @Test void randomAgainstArrayDeque() {
        Random r = new Random(17);
        for (int t = 0; t < 200; t++) {
            int cap = 1 + r.nextInt(6);
            RingBuffer<Integer> b = new RingBuffer<>(cap);
            ArrayDeque<Integer> ref = new ArrayDeque<>();
            for (int step = 0; step < 100; step++) {
                if (r.nextInt(4) == 0 && !ref.isEmpty()) assertEquals(ref.removeFirst(), b.removeOldest());
                else {
                    int v = r.nextInt(1000);
                    Integer evicted = ref.size() == cap ? ref.removeFirst() : null;
                    ref.addLast(v);
                    assertEquals(Optional.ofNullable(evicted), b.add(v));
                }
                assertEquals(new ArrayList<>(ref), list(b));
            }
        }
    }
}

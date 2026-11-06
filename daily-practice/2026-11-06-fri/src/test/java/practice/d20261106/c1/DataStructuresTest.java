package practice.d20261106.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class DataStructuresTest {
    @Test void fenwickExample() {
        FenwickArray f = new FenwickArray(new int[]{1, 3, 5});
        assertEquals(9, f.sumRange(0, 2));
        f.update(1, 2);
        assertEquals(8, f.sumRange(0, 2));
        assertEquals(2, f.sumRange(1, 1));
        assertEquals(1, new FenwickArray(new int[]{1}).sumRange(0, 0));
    }

    @Test void fenwickOverflowNeedsLong() {
        FenwickArray f = new FenwickArray(new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE});
        assertEquals(3L * Integer.MAX_VALUE, f.sumRange(0, 2));
        f.update(0, Integer.MIN_VALUE);
        assertEquals((long) Integer.MIN_VALUE + 2L * Integer.MAX_VALUE, f.sumRange(0, 2));
    }

    @Test void fenwickRandomVsBrute() {
        Random r = new Random(4);
        for (int t = 0; t < 100; t++) {
            int[] a = r.ints(1 + r.nextInt(20), -50, 50).toArray();
            FenwickArray f = new FenwickArray(a);
            for (int op = 0; op < 60; op++) {
                if (r.nextBoolean()) {
                    int i = r.nextInt(a.length), v = r.nextInt(100) - 50;
                    a[i] = v;
                    f.update(i, v);
                } else {
                    int l = r.nextInt(a.length), h = l + r.nextInt(a.length - l);
                    long s = 0;
                    for (int i = l; i <= h; i++) s += a[i];
                    assertEquals(s, f.sumRange(l, h));
                }
            }
        }
    }

    @Test void fenwickLarge() {
        int[] a = new int[500_000];
        Arrays.fill(a, 1);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            FenwickArray f = new FenwickArray(a);
            for (int i = 0; i < 200_000; i++) {
                f.update(i, 2);
                f.sumRange(0, a.length - 1);
            }
            assertEquals(500_000 + 200_000, f.sumRange(0, a.length - 1));
        });
    }

    @Test void randomizedSetBasics() {
        RandomizedSet s = new RandomizedSet(new Random(5));
        assertThrows(NoSuchElementException.class, s::getRandom);
        assertTrue(s.insert(1));
        assertFalse(s.insert(1));
        assertTrue(s.insert(2));
        assertTrue(s.remove(1));
        assertFalse(s.remove(1));
        assertEquals(2, s.getRandom());
        assertTrue(s.remove(2));
        assertThrows(NoSuchElementException.class, s::getRandom);
        assertTrue(s.insert(0));
        assertTrue(s.insert(-7));
        assertTrue(s.remove(0));
        assertEquals(-7, s.getRandom());
    }

    @Test void randomizedSetRandomVsHashSet() {
        Random ops = new Random(6);
        RandomizedSet s = new RandomizedSet(new Random(7));
        Set<Integer> ref = new HashSet<>();
        Set<Integer> seen = new HashSet<>();
        for (int i = 0; i < 20_000; i++) {
            int v = ops.nextInt(30);
            switch (ops.nextInt(3)) {
                case 0 -> assertEquals(ref.add(v), s.insert(v));
                case 1 -> assertEquals(ref.remove(v), s.remove(v));
                default -> {
                    if (!ref.isEmpty()) {
                        int g = s.getRandom();
                        assertTrue(ref.contains(g), "returned a removed value " + g);
                        seen.add(g);
                    }
                }
            }
        }
        assertTrue(seen.size() > 15, "getRandom should cover many members");
    }

    @Test void timeMap() {
        TimeMap m = new TimeMap();
        assertEquals("", m.get("k", 5));
        m.set("foo", "bar", 1);
        assertEquals("bar", m.get("foo", 1));
        assertEquals("bar", m.get("foo", 3));
        m.set("foo", "bar2", 4);
        assertEquals("bar2", m.get("foo", 4));
        assertEquals("bar", m.get("foo", 3));
        assertEquals("", m.get("foo", 0));
        m.set("foo", "bar3", 4);
        assertEquals("bar3", m.get("foo", 99), "later set at an equal timestamp wins");
        assertEquals("", m.get("other", 1));
        m.set("big", "x", Long.MAX_VALUE);
        assertEquals("x", m.get("big", Long.MAX_VALUE));
    }

    @Test void timeMapLarge() {
        TimeMap m = new TimeMap();
        for (int i = 0; i < 300_000; i++) m.set("k", "v" + i, i);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            for (int i = 0; i < 300_000; i++) assertEquals("v" + i, m.get("k", i));
        });
    }
}

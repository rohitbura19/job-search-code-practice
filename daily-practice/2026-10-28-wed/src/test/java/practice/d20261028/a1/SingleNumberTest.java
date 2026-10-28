package practice.d20261028.a1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class SingleNumberTest {
    @Test void onceExamples() {
        assertEquals(4, SingleNumber.once(new int[] {2, 2, 1, 1, 4}));
        assertEquals(7, SingleNumber.once(new int[] {7}));
        assertEquals(Integer.MIN_VALUE, SingleNumber.once(new int[] {Integer.MIN_VALUE, 5, 5}));
        assertEquals(-3, SingleNumber.once(new int[] {-3, 8, 8, 0, 0}));
    }

    @Test void threesExamples() {
        assertEquals(3, SingleNumber.onceAmongThrees(new int[] {2, 2, 3, 2}));
        assertEquals(99, SingleNumber.onceAmongThrees(new int[] {0, 1, 0, 1, 0, 1, 99}));
        assertEquals(-5, SingleNumber.onceAmongThrees(new int[] {-5}));
        assertEquals(Integer.MIN_VALUE, SingleNumber.onceAmongThrees(new int[] {-1, -1, Integer.MIN_VALUE, -1}));
        assertEquals(Integer.MAX_VALUE, SingleNumber.onceAmongThrees(new int[] {Integer.MAX_VALUE, 4, 4, 4}));
    }

    @Test void twoSinglesExamples() {
        assertArrayEquals(new int[] {3, 5}, SingleNumber.twoSingles(new int[] {1, 2, 1, 3, 2, 5}));
        assertArrayEquals(new int[] {-1, 0}, SingleNumber.twoSingles(new int[] {0, -1}));
        assertArrayEquals(new int[] {Integer.MIN_VALUE, 0}, SingleNumber.twoSingles(new int[] {Integer.MIN_VALUE, 0, 9, 9}));
        assertArrayEquals(new int[] {0, Integer.MAX_VALUE}, SingleNumber.twoSingles(new int[] {Integer.MAX_VALUE, 0, -4, -4}));
    }

    @Test void randomAgainstCounting() {
        Random r = new Random(28);
        for (int iter = 0; iter < 300; iter++) {
            int distinct = 1 + r.nextInt(8);
            Set<Integer> pool = new LinkedHashSet<>();
            while (pool.size() < distinct + 2) pool.add(r.nextInt(41) - 20);
            List<Integer> vals = new ArrayList<>(pool);
            int s = vals.get(0), t = vals.get(1);
            List<Integer> two = new ArrayList<>(), three = new ArrayList<>(List.of(s));
            two.add(s); two.add(t);
            for (int i = 2; i < vals.size(); i++) { two.add(vals.get(i)); two.add(vals.get(i)); for (int k = 0; k < 3; k++) three.add(vals.get(i)); }
            Collections.shuffle(two, r); Collections.shuffle(three, r);
            int[] a2 = two.stream().mapToInt(Integer::intValue).toArray();
            int[] a3 = three.stream().mapToInt(Integer::intValue).toArray();
            assertArrayEquals(new int[] {Math.min(s, t), Math.max(s, t)}, SingleNumber.twoSingles(a2));
            assertEquals(s, SingleNumber.onceAmongThrees(a3));
            // once(): drop t, keep s single and the rest paired
            List<Integer> one = new ArrayList<>(List.of(s));
            for (int i = 2; i < vals.size(); i++) { one.add(vals.get(i)); one.add(vals.get(i)); }
            Collections.shuffle(one, r);
            assertEquals(s, SingleNumber.once(one.stream().mapToInt(Integer::intValue).toArray()));
        }
    }

    @Test void largeInputIsFast() {
        int n = 3_000_001;
        int[] a = new int[n];
        for (int i = 0; i < n - 1; i++) a[i] = i / 3;
        a[n - 1] = 777_777_777;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(777_777_777, SingleNumber.onceAmongThrees(a)));
    }
}

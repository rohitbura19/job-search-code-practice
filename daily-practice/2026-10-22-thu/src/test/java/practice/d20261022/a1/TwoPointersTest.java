package practice.d20261022.a1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class TwoPointersTest {
    private static int[] zeroed(int... a) { TwoPointers.moveZeroes(a); return a; }

    @Test void moveZeroesExamples() {
        assertArrayEquals(new int[]{1, 3, 12, 0, 0}, zeroed(0, 1, 0, 3, 12));
        assertArrayEquals(new int[]{0}, zeroed(0));
        assertArrayEquals(new int[]{5}, zeroed(5));
        assertArrayEquals(new int[]{}, zeroed());
        assertArrayEquals(new int[]{0, 0, 0}, zeroed(0, 0, 0));
        assertArrayEquals(new int[]{-1, Integer.MIN_VALUE, 0}, zeroed(-1, 0, Integer.MIN_VALUE));
    }

    @Test void moveZeroesRandom() {
        Random r = new Random(1);
        for (int t = 0; t < 300; t++) {
            int[] a = r.ints(r.nextInt(12), -2, 3).toArray();
            int[] exp = Arrays.stream(a).filter(x -> x != 0).toArray();
            exp = Arrays.copyOf(exp, a.length);
            assertArrayEquals(exp, zeroed(a.clone()));
        }
    }

    private static int[] dedup(int... a) { int k = TwoPointers.removeDuplicates(a); return Arrays.copyOf(a, k); }

    @Test void removeDuplicatesExamples() {
        assertArrayEquals(new int[]{1, 1, 2, 2, 3}, dedup(1, 1, 1, 2, 2, 3));
        assertArrayEquals(new int[]{0, 0, 1, 1, 2, 3, 3}, dedup(0, 0, 1, 1, 1, 1, 2, 3, 3));
        assertArrayEquals(new int[]{}, dedup());
        assertArrayEquals(new int[]{7}, dedup(7));
        assertArrayEquals(new int[]{4, 4}, dedup(4, 4, 4, 4, 4));
        assertArrayEquals(new int[]{-3, -3, Integer.MAX_VALUE}, dedup(-3, -3, -3, Integer.MAX_VALUE));
    }

    @Test void removeDuplicatesRandom() {
        Random r = new Random(2);
        for (int t = 0; t < 300; t++) {
            int[] a = r.ints(r.nextInt(15), 0, 5).sorted().toArray();
            List<Integer> exp = new ArrayList<>();
            Map<Integer, Integer> seen = new HashMap<>();
            for (int x : a) if (seen.merge(x, 1, Integer::sum) <= 2) exp.add(x);
            assertEquals(exp, Arrays.stream(dedup(a.clone())).boxed().toList());
        }
    }

    @Test void largeInput() {
        int[] a = new int[5_000_000];
        for (int i = 0; i < a.length; i++) a[i] = i % 3 == 0 ? 0 : i / 3;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> TwoPointers.moveZeroes(a));
        int[] b = new int[5_000_000];
        for (int i = 0; i < b.length; i++) b[i] = i / 4;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(2 * (b.length / 4), TwoPointers.removeDuplicates(b)));
    }
}

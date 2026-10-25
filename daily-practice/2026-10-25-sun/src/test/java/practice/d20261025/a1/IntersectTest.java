package practice.d20261025.a1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class IntersectTest {
    static int[] brute(int[] a, int[] b) {
        List<Integer> rest = new ArrayList<>();
        for (int x : b) rest.add(x);
        List<Integer> out = new ArrayList<>();
        for (int x : a) if (rest.remove((Integer) x)) out.add(x);
        return out.stream().mapToInt(Integer::intValue).sorted().toArray();
    }

    @Test void examples() {
        assertArrayEquals(new int[]{2, 2}, Intersect.intersect(new int[]{1, 2, 2, 1}, new int[]{2, 2}));
        assertArrayEquals(new int[]{4, 9}, Intersect.intersect(new int[]{4, 9, 5}, new int[]{9, 4, 9, 8, 4}));
    }

    @Test void edgeCases() {
        assertArrayEquals(new int[]{}, Intersect.intersect(new int[]{}, new int[]{1}));
        assertArrayEquals(new int[]{}, Intersect.intersect(new int[]{}, new int[]{}));
        assertArrayEquals(new int[]{7}, Intersect.intersect(new int[]{7}, new int[]{7}));
        assertArrayEquals(new int[]{}, Intersect.intersect(new int[]{1, 2}, new int[]{3, 4}));
        assertArrayEquals(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE},
                Intersect.intersect(new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, 0}, new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}));
        assertArrayEquals(new int[]{5, 5, 5}, Intersect.intersect(new int[]{5, 5, 5, 5}, new int[]{5, 5, 5}));
    }

    @Test void doesNotModifyInputs() {
        int[] a = {3, 1, 3}, b = {3, 3, 1};
        Intersect.intersect(a, b);
        assertArrayEquals(new int[]{3, 1, 3}, a);
        assertArrayEquals(new int[]{3, 3, 1}, b);
    }

    @Test void matchesBruteForce() {
        Random rnd = new Random(5);
        for (int it = 0; it < 2000; it++) {
            int[] a = rnd.ints(rnd.nextInt(12), -4, 5).toArray(), b = rnd.ints(rnd.nextInt(12), -4, 5).toArray();
            assertArrayEquals(brute(a, b), Intersect.intersect(a, b), Arrays.toString(a) + Arrays.toString(b));
            int[] sa = a.clone(), sb = b.clone();
            Arrays.sort(sa);
            Arrays.sort(sb);
            assertArrayEquals(brute(a, b), Intersect.intersectSorted(sa, sb), Arrays.toString(sa) + Arrays.toString(sb));
        }
    }

    @Test void largeInput() {
        Random rnd = new Random(1);
        int[] a = rnd.ints(2_000_000, 0, 1000).toArray(), b = rnd.ints(2_000_000, 0, 1000).toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            assertTrue(Intersect.intersect(a, b).length >= 1_900_000);
            Arrays.sort(a);
            Arrays.sort(b);
            assertTrue(Intersect.intersectSorted(a, b).length >= 1_900_000);
        });
    }
}

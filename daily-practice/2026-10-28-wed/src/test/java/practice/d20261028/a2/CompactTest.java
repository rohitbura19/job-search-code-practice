package practice.d20261028.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class CompactTest {
    @Test void moveZeroesExamples() {
        int[] a = {0, 1, 0, 3, 12};
        Compact.moveZeroes(a);
        assertArrayEquals(new int[] {1, 3, 12, 0, 0}, a);
        int[] e = {};
        Compact.moveZeroes(e);
        assertEquals(0, e.length);
        int[] z = {0, 0};
        Compact.moveZeroes(z);
        assertArrayEquals(new int[] {0, 0}, z);
        int[] s = {5};
        Compact.moveZeroes(s);
        assertArrayEquals(new int[] {5}, s);
        int[] n = {-1, 0, Integer.MIN_VALUE, 0};
        Compact.moveZeroes(n);
        assertArrayEquals(new int[] {-1, Integer.MIN_VALUE, 0, 0}, n);
    }

    @Test void removeElementExamples() {
        int[] a = {3, 2, 2, 3};
        int k = Compact.removeElement(a, 3);
        assertEquals(2, k);
        assertArrayEquals(new int[] {2, 2}, Arrays.copyOf(a, k));
        int[] b = {0, 1, 2, 2, 3, 0, 4, 2};
        k = Compact.removeElement(b, 2);
        assertArrayEquals(new int[] {0, 1, 3, 0, 4}, Arrays.copyOf(b, k));
        assertEquals(0, Compact.removeElement(new int[] {}, 1));
        assertEquals(0, Compact.removeElement(new int[] {7, 7, 7}, 7));
        assertEquals(3, Compact.removeElement(new int[] {7, 7, 7}, 8));
    }

    @Test void atMostTwiceExamples() {
        int[] a = {1, 1, 1, 2, 2, 3};
        int k = Compact.removeDuplicatesAtMostTwice(a);
        assertArrayEquals(new int[] {1, 1, 2, 2, 3}, Arrays.copyOf(a, k));
        int[] b = {0, 0, 1, 1, 1, 1, 2, 3, 3};
        k = Compact.removeDuplicatesAtMostTwice(b);
        assertArrayEquals(new int[] {0, 0, 1, 1, 2, 3, 3}, Arrays.copyOf(b, k));
        assertEquals(0, Compact.removeDuplicatesAtMostTwice(new int[] {}));
        assertEquals(1, Compact.removeDuplicatesAtMostTwice(new int[] {4}));
        int[] c = {-5, -5, -5, -5};
        assertEquals(2, Compact.removeDuplicatesAtMostTwice(c));
    }

    @Test void randomAgainstStreams() {
        Random r = new Random(1028);
        for (int iter = 0; iter < 500; iter++) {
            int n = r.nextInt(15);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = r.nextInt(5) - 1;
            int[] z = a.clone();
            Compact.moveZeroes(z);
            int[] want = Arrays.stream(a).filter(x -> x != 0).toArray();
            assertArrayEquals(Arrays.copyOf(want, n), z);

            int val = r.nextInt(5) - 1;
            int[] c = a.clone();
            int k = Compact.removeElement(c, val);
            assertArrayEquals(Arrays.stream(a).filter(x -> x != val).toArray(), Arrays.copyOf(c, k));

            int[] s = a.clone();
            Arrays.sort(s);
            List<Integer> exp = new ArrayList<>();
            Map<Integer, Integer> cnt = new HashMap<>();
            for (int x : s) if (cnt.merge(x, 1, Integer::sum) <= 2) exp.add(x);
            int[] d = s.clone();
            k = Compact.removeDuplicatesAtMostTwice(d);
            assertEquals(exp, Arrays.stream(d, 0, k).boxed().toList());
        }
    }

    @Test void largeInputIsFast() {
        int[] a = new int[5_000_000];
        for (int i = 0; i < a.length; i += 3) a[i] = i + 1;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            Compact.moveZeroes(a);
            assertEquals(0, a[a.length - 1]);
            assertEquals(1, a[0]);
        });
    }
}

package practice.d20261014.a2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SingleNumberTest {

    static int[] shuffled(List<Integer> xs, long seed) {
        List<Integer> c = new ArrayList<>(xs);
        Collections.shuffle(c, new Random(seed));
        return c.stream().mapToInt(Integer::intValue).toArray();
    }

    @Test @DisplayName("singleNumber: sheet example, single element, negatives, MIN_VALUE")
    void single() {
        assertEquals(4, SingleNumber.singleNumber(new int[]{4, 1, 2, 1, 2}));
        assertEquals(7, SingleNumber.singleNumber(new int[]{7}));
        assertEquals(-5, SingleNumber.singleNumber(new int[]{-5, 3, 3}));
        assertEquals(Integer.MIN_VALUE, SingleNumber.singleNumber(new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE}));
        assertEquals(0, SingleNumber.singleNumber(new int[]{1, 0, 1}));
    }

    @Test @DisplayName("twoSingles: sheet example, negatives, a pair containing 0")
    void two() {
        assertEquals("[3, 5]", sorted(SingleNumber.twoSingles(new int[]{1, 2, 1, 3, 2, 5})));
        assertEquals("[-4, 9]", sorted(SingleNumber.twoSingles(new int[]{9, -4, 6, 6})));
        assertEquals("[0, 8]", sorted(SingleNumber.twoSingles(new int[]{0, 8})));
        assertEquals("[-2147483648, 2147483647]",
                sorted(SingleNumber.twoSingles(new int[]{Integer.MIN_VALUE, 1, Integer.MAX_VALUE, 1})));
    }

    static String sorted(int[] r) { int[] c = r.clone(); Arrays.sort(c); return Arrays.toString(c); }

    @Test @DisplayName("singleNumberThrice: sheet-style cases, negatives")
    void thrice() {
        assertEquals(3, SingleNumber.singleNumberThrice(new int[]{2, 2, 3, 2}));
        assertEquals(99, SingleNumber.singleNumberThrice(new int[]{0, 1, 0, 1, 0, 1, 99}));
        assertEquals(-1, SingleNumber.singleNumberThrice(new int[]{-1, 5, 5, 5}));
        assertEquals(Integer.MIN_VALUE, SingleNumber.singleNumberThrice(new int[]{Integer.MIN_VALUE, 7, 7, 7}));
        assertEquals(-3, SingleNumber.singleNumberThrice(new int[]{-3}));
    }

    @Test @DisplayName("isPowerOfTwo: 1, 2, 1<<30 yes; 0, negatives, MIN_VALUE, 6 no")
    void pow2() {
        assertTrue(SingleNumber.isPowerOfTwo(1));
        assertTrue(SingleNumber.isPowerOfTwo(2));
        assertTrue(SingleNumber.isPowerOfTwo(1 << 30));
        assertFalse(SingleNumber.isPowerOfTwo(0));
        assertFalse(SingleNumber.isPowerOfTwo(-8));
        assertFalse(SingleNumber.isPowerOfTwo(Integer.MIN_VALUE));
        assertFalse(SingleNumber.isPowerOfTwo(6));
        assertFalse(SingleNumber.isPowerOfTwo(Integer.MAX_VALUE));
    }

    @Test @DisplayName("random inputs against a counting oracle")
    void random() {
        Random rnd = new Random(14);
        for (int t = 0; t < 300; t++) {
            int single = rnd.nextInt(), other = rnd.nextInt();
            while (other == single) other = rnd.nextInt();
            java.util.Set<Integer> distinct = new java.util.LinkedHashSet<>();
            for (int i = 0, k = rnd.nextInt(10); i < k; i++) {
                int v = rnd.nextInt(2000) - 1000;
                if (v != single && v != other) distinct.add(v);
            }
            List<Integer> twice = new ArrayList<>(), thrice = new ArrayList<>();
            for (int v : distinct) { twice.add(v); twice.add(v); thrice.add(v); thrice.add(v); thrice.add(v); }
            List<Integer> a = new ArrayList<>(twice); a.add(single);
            assertEquals(single, SingleNumber.singleNumber(shuffled(a, t)));
            List<Integer> b = new ArrayList<>(thrice); b.add(single);
            assertEquals(single, SingleNumber.singleNumberThrice(shuffled(b, t)));
            List<Integer> c = new ArrayList<>(twice); c.add(single); c.add(other);
            int[] expect = {Math.min(single, other), Math.max(single, other)};
            assertEquals(Arrays.toString(expect), sorted(SingleNumber.twoSingles(shuffled(c, t))));
        }
    }

    @Test @DisplayName("a million elements in linear time")
    void large() {
        int n = 1_000_000;
        int[] a = new int[2 * n + 1];
        for (int i = 0; i < n; i++) { a[2 * i] = i + 1; a[2 * i + 1] = i + 1; }
        a[2 * n] = -42;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(-42, SingleNumber.singleNumber(a)));
    }
}

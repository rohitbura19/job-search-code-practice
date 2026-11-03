package practice.d20261014.a1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MergeSortedArrayTest {

    static int[] run(int[] a, int m, int[] b) {
        int[] x = Arrays.copyOf(a, m + b.length);
        MergeSortedArray.merge(x, m, b.clone(), b.length);
        return x;
    }

    @Test @DisplayName("example from the sheet")
    void example() {
        assertEquals("[1, 2, 2, 3, 5, 6]", Arrays.toString(run(new int[]{1, 2, 3}, 3, new int[]{2, 5, 6})));
    }

    @Test @DisplayName("m = 0: everything comes from b")
    void emptyA() {
        assertEquals("[1, 2, 3]", Arrays.toString(run(new int[]{}, 0, new int[]{1, 2, 3})));
    }

    @Test @DisplayName("n = 0: a is untouched")
    void emptyB() {
        assertEquals("[4, 5]", Arrays.toString(run(new int[]{4, 5}, 2, new int[]{})));
    }

    @Test @DisplayName("all of b smaller than a, all of b larger than a")
    void disjoint() {
        assertEquals("[1, 2, 7, 8]", Arrays.toString(run(new int[]{7, 8}, 2, new int[]{1, 2})));
        assertEquals("[1, 2, 7, 8]", Arrays.toString(run(new int[]{1, 2}, 2, new int[]{7, 8})));
    }

    @Test @DisplayName("duplicates and negatives")
    void duplicatesNegatives() {
        assertEquals("[-3, -3, -1, 0, 0, 0, 4]", Arrays.toString(run(new int[]{-3, 0, 0}, 3, new int[]{-3, -1, 0, 4})));
        assertEquals("[5, 5, 5, 5]", Arrays.toString(run(new int[]{5, 5}, 2, new int[]{5, 5})));
    }

    @Test @DisplayName("single elements")
    void single() {
        assertEquals("[1, 2]", Arrays.toString(run(new int[]{2}, 1, new int[]{1})));
        assertEquals("[-2147483648, 2147483647]",
                Arrays.toString(run(new int[]{Integer.MAX_VALUE}, 1, new int[]{Integer.MIN_VALUE})));
    }

    @Test @DisplayName("random against sort-everything oracle")
    void random() {
        Random rnd = new Random(14);
        for (int t = 0; t < 500; t++) {
            int m = rnd.nextInt(12), n = rnd.nextInt(12);
            int[] a = rnd.ints(m, -20, 20).sorted().toArray();
            int[] b = rnd.ints(n, -20, 20).sorted().toArray();
            int[] expect = new int[m + n];
            System.arraycopy(a, 0, expect, 0, m); System.arraycopy(b, 0, expect, m, n);
            Arrays.sort(expect);
            assertEquals(Arrays.toString(expect), Arrays.toString(run(a, m, b)));
        }
    }

    @Test @DisplayName("2 million elements stay linear")
    void large() {
        int m = 1_000_000, n = 1_000_000;
        int[] a = new int[m + n], b = new int[n];
        for (int i = 0; i < m; i++) a[i] = 2 * i;
        for (int i = 0; i < n; i++) b[i] = 2 * i + 1;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> MergeSortedArray.merge(a, m, b, n));
        for (int i = 0; i < m + n; i++) assertEquals((long) i, (long) a[i], "wrong at " + i);
    }
}

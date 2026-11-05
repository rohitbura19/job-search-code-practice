package practice.d20261105.a1;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class ArrayTricksTest {
    @Test void productExamples() {
        assertArrayEquals(new int[] {24, 12, 8, 6}, ArrayTricks.productExceptSelf(new int[] {1, 2, 3, 4}));
        assertArrayEquals(new int[] {0, 0, 9, 0, 0}, ArrayTricks.productExceptSelf(new int[] {-1, 1, 0, -3, 3}));
    }

    @Test void productEdges() {
        assertArrayEquals(new int[] {}, ArrayTricks.productExceptSelf(new int[] {}));
        assertArrayEquals(new int[] {1}, ArrayTricks.productExceptSelf(new int[] {7}));
        assertArrayEquals(new int[] {0, 0, 0}, ArrayTricks.productExceptSelf(new int[] {0, 5, 0}));
        assertArrayEquals(new int[] {3, 2}, ArrayTricks.productExceptSelf(new int[] {2, 3}));
        int big = 1 << 20;
        assertArrayEquals(new int[] {big, big}, ArrayTricks.productExceptSelf(new int[] {big, big}));
    }

    @Test void productMatchesBruteForce() {
        Random r = new Random(1);
        for (int t = 0; t < 300; t++) {
            int[] a = new int[r.nextInt(8)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(7) - 3;
            int[] expected = new int[a.length];
            for (int i = 0; i < a.length; i++) { int p = 1; for (int j = 0; j < a.length; j++) if (j != i) p *= a[j]; expected[i] = p; }
            assertArrayEquals(expected, ArrayTricks.productExceptSelf(a));
        }
    }

    @Test void rotateExamples() {
        int[] a = {1, 2, 3, 4, 5, 6, 7};
        ArrayTricks.rotate(a, 3);
        assertArrayEquals(new int[] {5, 6, 7, 1, 2, 3, 4}, a);
        int[] b = {1, 2};
        ArrayTricks.rotate(b, 5);
        assertArrayEquals(new int[] {2, 1}, b);
    }

    @Test void rotateEdges() {
        int[] e = {};
        ArrayTricks.rotate(e, 3);
        assertArrayEquals(new int[] {}, e);
        int[] one = {9};
        ArrayTricks.rotate(one, 100);
        assertArrayEquals(new int[] {9}, one);
        int[] c = {1, 2, 3};
        ArrayTricks.rotate(c, 0);
        assertArrayEquals(new int[] {1, 2, 3}, c);
        ArrayTricks.rotate(c, 3);
        assertArrayEquals(new int[] {1, 2, 3}, c);
        ArrayTricks.rotate(c, Integer.MAX_VALUE);
        assertArrayEquals(new int[] {3, 1, 2}, c);
        int[] d = {7, 7, 7, 1};
        ArrayTricks.rotate(d, 1);
        assertArrayEquals(new int[] {1, 7, 7, 7}, d);
    }

    @Test void rotateMatchesBruteForce() {
        Random r = new Random(2);
        for (int t = 0; t < 300; t++) {
            int n = r.nextInt(9), k = r.nextInt(30);
            int[] a = new int[n], expected = new int[n];
            for (int i = 0; i < n; i++) a[i] = r.nextInt(100);
            for (int i = 0; i < n; i++) expected[(i + k) % n] = a[i];
            ArrayTricks.rotate(a, k);
            assertArrayEquals(expected, a);
        }
    }

    @Test void pivot() {
        assertEquals(3, ArrayTricks.pivotIndex(new int[] {1, 7, 3, 6, 5, 6}));
        assertEquals(-1, ArrayTricks.pivotIndex(new int[] {1, 2, 3}));
        assertEquals(0, ArrayTricks.pivotIndex(new int[] {2, 1, -1}));
        assertEquals(2, ArrayTricks.pivotIndex(new int[] {1, -1, 5, 0, 0}));
        assertEquals(0, ArrayTricks.pivotIndex(new int[] {0}));
        assertEquals(-1, ArrayTricks.pivotIndex(new int[] {}));
        assertEquals(0, ArrayTricks.pivotIndex(new int[] {5, 0, 0, 0, 0}));
        assertEquals(1, ArrayTricks.pivotIndex(new int[] {Integer.MAX_VALUE, 0, Integer.MAX_VALUE}));
        assertEquals(-1, ArrayTricks.pivotIndex(new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE, 1}));
    }

    @Test void largeInputIsLinear() {
        int[] a = new int[2_000_000];
        for (int i = 0; i < a.length; i++) a[i] = (i % 3) - 1;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            ArrayTricks.productExceptSelf(a);
            ArrayTricks.rotate(a, 999_999);
            ArrayTricks.pivotIndex(a);
        });
    }
}

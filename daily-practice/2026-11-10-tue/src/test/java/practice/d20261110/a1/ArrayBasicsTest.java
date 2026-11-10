package practice.d20261110.a1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class ArrayBasicsTest {
    @Test void squaresExamples() {
        assertArrayEquals(new int[]{0, 1, 9, 16, 100}, ArrayBasics.sortedSquares(new int[]{-4, -1, 0, 3, 10}));
        assertArrayEquals(new int[]{4, 9, 9, 49, 121}, ArrayBasics.sortedSquares(new int[]{-7, -3, 2, 3, 11}));
    }

    @Test void squaresEdges() {
        assertArrayEquals(new int[0], ArrayBasics.sortedSquares(new int[0]));
        assertArrayEquals(new int[]{25}, ArrayBasics.sortedSquares(new int[]{-5}));
        assertArrayEquals(new int[]{4, 4, 4}, ArrayBasics.sortedSquares(new int[]{-2, -2, 2}));
        assertArrayEquals(new int[]{2147302921}, ArrayBasics.sortedSquares(new int[]{46339}));
    }

    @Test void squaresRandomAgainstSort() {
        Random r = new Random(1);
        for (int t = 0; t < 300; t++) {
            int[] a = r.ints(r.nextInt(30), -50, 50).sorted().toArray();
            int[] exp = Arrays.stream(a).map(x -> x * x).sorted().toArray();
            assertArrayEquals(exp, ArrayBasics.sortedSquares(a));
        }
    }

    @Test void intersectExamples() {
        assertArrayEquals(new int[]{2, 2}, sorted(ArrayBasics.intersect(new int[]{1, 2, 2, 1}, new int[]{2, 2})));
        assertArrayEquals(new int[]{4, 9}, sorted(ArrayBasics.intersect(new int[]{4, 9, 5}, new int[]{9, 4, 9, 8, 4})));
    }

    @Test void intersectEdges() {
        assertEquals(0, ArrayBasics.intersect(new int[0], new int[]{1}).length);
        assertEquals(0, ArrayBasics.intersect(new int[]{1}, new int[]{2}).length);
        assertArrayEquals(new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE}, sorted(ArrayBasics.intersect(
                new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, 3}, new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE})));
    }

    @Test void intersectRandomAgainstBruteForce() {
        Random r = new Random(2);
        for (int t = 0; t < 300; t++) {
            int[] a = r.ints(r.nextInt(15), 0, 6).toArray(), b = r.ints(r.nextInt(15), 0, 6).toArray();
            List<Integer> rest = new ArrayList<>();
            for (int x : b) rest.add(x);
            List<Integer> exp = new ArrayList<>();
            for (int x : a) if (rest.remove((Integer) x)) exp.add(x);
            assertArrayEquals(exp.stream().mapToInt(i -> i).sorted().toArray(), sorted(ArrayBasics.intersect(a, b)));
        }
    }

    @Test void rotate() {
        int[] a = {1, 2, 3, 4, 5, 6, 7};
        ArrayBasics.rotateRight(a, 3);
        assertArrayEquals(new int[]{5, 6, 7, 1, 2, 3, 4}, a);
        ArrayBasics.rotateRight(a, 7 * 5 + 1);
        assertArrayEquals(new int[]{4, 5, 6, 7, 1, 2, 3}, a);
        ArrayBasics.rotateRight(a, -2);
        assertArrayEquals(new int[]{6, 7, 1, 2, 3, 4, 5}, a);
        ArrayBasics.rotateRight(a, 0);
        assertArrayEquals(new int[]{6, 7, 1, 2, 3, 4, 5}, a);
    }

    @Test void rotateEdges() {
        int[] e = {};
        ArrayBasics.rotateRight(e, 5);
        assertEquals(0, e.length);
        int[] one = {9};
        ArrayBasics.rotateRight(one, Integer.MAX_VALUE);
        assertArrayEquals(new int[]{9}, one);
        int[] two = {1, 2};
        ArrayBasics.rotateRight(two, Integer.MIN_VALUE);
        assertArrayEquals(new int[]{1, 2}, two);
    }

    @Test void largeInputWithinTimeout() {
        int[] big = new int[2_000_000];
        for (int i = 0; i < big.length; i++) big[i] = i / 50 - 20_000;
        Arrays.sort(big);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            ArrayBasics.sortedSquares(big);
            ArrayBasics.rotateRight(big, 123_457);
            ArrayBasics.intersect(big, big);
        });
    }

    private static int[] sorted(int[] a) { int[] c = a.clone(); Arrays.sort(c); return c; }
}

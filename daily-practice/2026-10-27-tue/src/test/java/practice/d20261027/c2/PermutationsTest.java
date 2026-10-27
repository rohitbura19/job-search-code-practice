package practice.d20261027.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class PermutationsTest {
    @Test void withDuplicates() {
        assertEquals(List.of(List.of(1, 1, 2), List.of(1, 2, 1), List.of(2, 1, 1)), Permutations.permuteUnique(new int[]{1, 1, 2}));
    }

    @Test void distinctValuesAreInLexicographicOrder() {
        assertEquals(List.of(List.of(1, 2, 3), List.of(1, 3, 2), List.of(2, 1, 3), List.of(2, 3, 1), List.of(3, 1, 2), List.of(3, 2, 1)),
                Permutations.permuteUnique(new int[]{3, 1, 2}));
    }

    @Test void emptySingleAllEqual() {
        assertEquals(List.of(List.of()), Permutations.permuteUnique(new int[]{}));
        assertEquals(List.of(List.of(5)), Permutations.permuteUnique(new int[]{5}));
        assertEquals(List.of(List.of(2, 2, 2, 2)), Permutations.permuteUnique(new int[]{2, 2, 2, 2}));
    }

    @Test void negativesAndExtremes() {
        assertEquals(List.of(List.of(Integer.MIN_VALUE, Integer.MAX_VALUE), List.of(Integer.MAX_VALUE, Integer.MIN_VALUE)),
                Permutations.permuteUnique(new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE}));
    }

    @Test void countIsMultinomial() {
        // 8 elements: 1,1,1,2,2,3,4,4 -> 8! / (3! 2! 1! 2!) = 1680
        assertEquals(1680, Permutations.permuteUnique(new int[]{4, 1, 2, 1, 3, 1, 4, 2}).size());
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(40320, Permutations.permuteUnique(new int[]{1, 2, 3, 4, 5, 6, 7, 8}).size()));
    }

    @Test void nextPermutationExamples() {
        int[] a = {1, 2, 3};
        Permutations.nextPermutation(a);
        assertArrayEquals(new int[]{1, 3, 2}, a);
        a = new int[]{3, 2, 1};
        Permutations.nextPermutation(a);
        assertArrayEquals(new int[]{1, 2, 3}, a);
        a = new int[]{1, 1, 5};
        Permutations.nextPermutation(a);
        assertArrayEquals(new int[]{1, 5, 1}, a);
        a = new int[]{1, 3, 2};
        Permutations.nextPermutation(a);
        assertArrayEquals(new int[]{2, 1, 3}, a);
    }

    @Test void nextPermutationEdges() {
        int[] a = {};
        Permutations.nextPermutation(a);
        assertEquals(0, a.length);
        a = new int[]{9};
        Permutations.nextPermutation(a);
        assertArrayEquals(new int[]{9}, a);
        a = new int[]{2, 2, 2};
        Permutations.nextPermutation(a);
        assertArrayEquals(new int[]{2, 2, 2}, a);
    }

    @Test void nextPermutationWalksTheWholeListAndWraps() {
        Random r = new Random(3);
        for (int t = 0; t < 200; t++) {
            int[] a = new int[1 + r.nextInt(6)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(4);
            List<List<Integer>> all = Permutations.permuteUnique(a);
            int[] cur = a.clone();
            Arrays.sort(cur);
            for (List<Integer> expected : all) {
                assertEquals(expected, Arrays.stream(cur).boxed().toList());
                Permutations.nextPermutation(cur);
            }
            assertArrayEquals(a.clone().length == 0 ? a : sorted(a), cur);
        }
    }

    private static int[] sorted(int[] a) {
        int[] c = a.clone();
        Arrays.sort(c);
        return c;
    }

    @Test void nextPermutationOnLargeDescendingArray() {
        int[] a = new int[1_000_000];
        for (int i = 0; i < a.length; i++) a[i] = a.length - i;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> Permutations.nextPermutation(a));
        assertEquals(1, a[0]);
        assertEquals(1_000_000, a[a.length - 1]);
    }
}

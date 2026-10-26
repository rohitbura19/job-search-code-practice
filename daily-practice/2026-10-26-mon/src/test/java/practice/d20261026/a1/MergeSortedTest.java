package practice.d20261026.a1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class MergeSortedTest {
    static int[] run(int[] a, int[] b) {
        int[] big = Arrays.copyOf(a, a.length + b.length);
        MergeSorted.merge(big, a.length, b, b.length);
        return big;
    }

    @Test void examples() {
        assertArrayEquals(new int[]{1, 2, 2, 3, 5, 6}, run(new int[]{1, 2, 3}, new int[]{2, 5, 6}));
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, run(new int[]{4, 5, 6}, new int[]{1, 2, 3}));
    }

    @Test void emptySides() {
        assertArrayEquals(new int[]{1}, run(new int[]{1}, new int[]{}));
        assertArrayEquals(new int[]{1}, run(new int[]{}, new int[]{1}));
        assertArrayEquals(new int[]{}, run(new int[]{}, new int[]{}));
    }

    @Test void duplicatesAndExtremes() {
        assertArrayEquals(new int[]{7, 7, 7, 7}, run(new int[]{7, 7}, new int[]{7, 7}));
        assertArrayEquals(new int[]{Integer.MIN_VALUE, -1, 0, Integer.MAX_VALUE, Integer.MAX_VALUE},
                run(new int[]{Integer.MIN_VALUE, 0, Integer.MAX_VALUE}, new int[]{-1, Integer.MAX_VALUE}));
    }

    @Test void randomAgainstSort() {
        Random r = new Random(5);
        for (int t = 0; t < 2000; t++) {
            int[] a = r.ints(r.nextInt(8), -5, 6).sorted().toArray();
            int[] b = r.ints(r.nextInt(8), -5, 6).sorted().toArray();
            int[] expected = java.util.stream.IntStream.concat(Arrays.stream(a), Arrays.stream(b)).sorted().toArray();
            assertArrayEquals(expected, run(a, b));
        }
    }

    @Test void largeInputIsLinear() {
        int[] a = new int[2_000_000], b = new int[2_000_000];
        for (int i = 0; i < a.length; i++) { a[i] = 2 * i; b[i] = 2 * i + 1; }
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            int[] r = run(a, b);
            assertEquals(3_999_999, r[r.length - 1]);
        });
    }

    @Test void sortColors() {
        int[] a = {2, 0, 2, 1, 1, 0};
        MergeSorted.sortColors(a);
        assertArrayEquals(new int[]{0, 0, 1, 1, 2, 2}, a);
        int[] one = {2};
        MergeSorted.sortColors(one);
        assertArrayEquals(new int[]{2}, one);
        int[] empty = {};
        MergeSorted.sortColors(empty);
        assertArrayEquals(new int[]{}, empty);
        int[] same = {1, 1, 1};
        MergeSorted.sortColors(same);
        assertArrayEquals(new int[]{1, 1, 1}, same);
    }

    @Test void threeWayPartitionRandom() {
        Random r = new Random(9);
        for (int t = 0; t < 3000; t++) {
            int[] a = r.ints(r.nextInt(15), -3, 4).toArray();
            int[] sorted = a.clone();
            Arrays.sort(sorted);
            int pivot = r.nextInt(9) - 4;
            int[] bounds = MergeSorted.threeWayPartition(a, pivot);
            int lt = (int) Arrays.stream(sorted).filter(x -> x < pivot).count();
            int le = (int) Arrays.stream(sorted).filter(x -> x <= pivot).count();
            assertArrayEquals(new int[]{lt, le}, bounds, Arrays.toString(sorted) + " pivot " + pivot);
            for (int i = 0; i < a.length; i++) assertEquals(Integer.compare(a[i], pivot), Integer.compare(i, lt) < 0 ? -1 : i < le ? 0 : 1);
            int[] again = a.clone();
            Arrays.sort(again);
            assertArrayEquals(sorted, again, "same multiset");
        }
    }

    @Test void extremesAndLargeInput() {
        int[] a = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, Integer.MAX_VALUE, Integer.MIN_VALUE};
        assertArrayEquals(new int[]{2, 3}, MergeSorted.threeWayPartition(a, 0));
        int[] big = new Random(1).ints(3_000_000, 0, 3).toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> MergeSorted.sortColors(big));
        for (int i = 1; i < big.length; i++) assertTrue(big[i - 1] <= big[i]);
    }
}

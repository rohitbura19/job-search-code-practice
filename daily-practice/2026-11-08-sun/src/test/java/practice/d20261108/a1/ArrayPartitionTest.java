package practice.d20261108.a1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class ArrayPartitionTest {
    @Test void sortColorsExamples() {
        int[] a = {2, 0, 2, 1, 1, 0};
        ArrayPartition.sortColors(a);
        assertArrayEquals(new int[]{0, 0, 1, 1, 2, 2}, a);
        int[] b = {2, 2, 2, 0, 0};
        ArrayPartition.sortColors(b);
        assertArrayEquals(new int[]{0, 0, 2, 2, 2}, b);
    }

    @Test void sortColorsEdges() {
        int[] e = {};
        ArrayPartition.sortColors(e);
        assertEquals(0, e.length);
        int[] one = {1};
        ArrayPartition.sortColors(one);
        assertArrayEquals(new int[]{1}, one);
        int[] same = {2, 2, 2};
        ArrayPartition.sortColors(same);
        assertArrayEquals(new int[]{2, 2, 2}, same);
    }

    @Test void moveZeroes() {
        int[] a = {0, 1, 0, 3, 12};
        assertEquals(3, ArrayPartition.moveZeroes(a));
        assertArrayEquals(new int[]{1, 3, 12, 0, 0}, a);
        int[] z = {0, 0};
        assertEquals(0, ArrayPartition.moveZeroes(z));
        assertArrayEquals(new int[]{0, 0}, z);
        int[] n = {Integer.MIN_VALUE, 0, Integer.MAX_VALUE};
        assertEquals(2, ArrayPartition.moveZeroes(n));
        assertArrayEquals(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE, 0}, n);
        assertEquals(0, ArrayPartition.moveZeroes(new int[0]));
    }

    @Test void removeDuplicatesAtMostTwice() {
        int[] a = {1, 1, 1, 2, 2, 3};
        int k = ArrayPartition.removeDuplicatesAtMostTwice(a);
        assertEquals(5, k);
        assertArrayEquals(new int[]{1, 1, 2, 2, 3}, Arrays.copyOf(a, k));
        int[] b = {0, 0, 1, 1, 1, 1, 2, 3, 3};
        k = ArrayPartition.removeDuplicatesAtMostTwice(b);
        assertArrayEquals(new int[]{0, 0, 1, 1, 2, 3, 3}, Arrays.copyOf(b, k));
        assertEquals(0, ArrayPartition.removeDuplicatesAtMostTwice(new int[0]));
        assertEquals(1, ArrayPartition.removeDuplicatesAtMostTwice(new int[]{5}));
        assertEquals(2, ArrayPartition.removeDuplicatesAtMostTwice(new int[]{5, 5, 5, 5, 5}));
    }

    @Test void randomVsLibrary() {
        Random r = new Random(1);
        for (int t = 0; t < 2000; t++) {
            int[] a = r.ints(r.nextInt(12), 0, 3).toArray();
            int[] expected = a.clone();
            Arrays.sort(expected);
            ArrayPartition.sortColors(a);
            assertArrayEquals(expected, a);

            int[] z = r.ints(r.nextInt(12), -2, 3).toArray();
            int[] exp = Arrays.stream(z).filter(x -> x != 0).toArray();
            int k = ArrayPartition.moveZeroes(z);
            assertEquals(exp.length, k);
            assertArrayEquals(exp, Arrays.copyOf(z, k));
            for (int i = k; i < z.length; i++) assertEquals(0, z[i]);

            int[] s = r.ints(r.nextInt(12), 0, 4).sorted().toArray();
            int[] expDup = Arrays.stream(s).boxed().collect(java.util.stream.Collectors.groupingBy(x -> x, java.util.TreeMap::new, java.util.stream.Collectors.counting()))
                    .entrySet().stream().flatMapToInt(e -> Arrays.stream(new int[(int) Math.min(2, e.getValue())]).map(x -> e.getKey())).toArray();
            int kk = ArrayPartition.removeDuplicatesAtMostTwice(s);
            assertArrayEquals(expDup, Arrays.copyOf(s, kk));
        }
    }

    @Test void large() {
        int[] a = new Random(2).ints(2_000_000, 0, 3).toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> ArrayPartition.sortColors(a));
        for (int i = 1; i < a.length; i++) assertTrue(a[i - 1] <= a[i]);
        int[] z = new Random(3).ints(2_000_000, 0, 2).toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> ArrayPartition.moveZeroes(z));
    }
}

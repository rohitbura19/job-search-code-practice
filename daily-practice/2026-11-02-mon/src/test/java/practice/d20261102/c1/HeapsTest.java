package practice.d20261102.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class HeapsTest {
    @Test void kthLargestExample() {
        Heaps.KthLargest k = new Heaps.KthLargest(3, new int[]{4, 5, 8, 2});
        assertEquals(4, k.add(3));
        assertEquals(5, k.add(5));
        assertEquals(5, k.add(10));
        assertEquals(8, k.add(9));
        assertEquals(8, k.add(4));
    }

    @Test void kthLargestEdges() {
        Heaps.KthLargest one = new Heaps.KthLargest(1, new int[]{});
        assertEquals(-5, one.add(-5));
        assertEquals(7, one.add(7));
        assertEquals(7, one.add(3));
        Heaps.KthLargest dup = new Heaps.KthLargest(2, new int[]{5});
        assertEquals(5, dup.add(5));            // duplicates count
        assertEquals(5, dup.add(1));
        Heaps.KthLargest ext = new Heaps.KthLargest(2, new int[]{Integer.MIN_VALUE});
        assertEquals(Integer.MIN_VALUE, ext.add(Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE, ext.add(Integer.MAX_VALUE));   // 2nd largest of {MIN, MIN, MAX}
        assertEquals(Integer.MAX_VALUE, ext.add(Integer.MAX_VALUE));
    }

    @Test void kthLargestRandomAgainstSort() {
        Random r = new Random(7);
        for (int t = 0; t < 100; t++) {
            int k = 1 + r.nextInt(5);
            List<Integer> all = new ArrayList<>();
            for (int i = 0; i < k - 1; i++) all.add(r.nextInt(20) - 10);
            Heaps.KthLargest kl = new Heaps.KthLargest(k, all.stream().mapToInt(Integer::intValue).toArray());
            for (int i = 0; i < 40; i++) {
                int v = r.nextInt(20) - 10;
                all.add(v);
                List<Integer> sorted = new ArrayList<>(all);
                sorted.sort(Comparator.reverseOrder());
                assertEquals(sorted.get(k - 1), kl.add(v));
            }
        }
    }

    @Test void kClosestExamples() {
        assertArrayEquals(new int[][]{{-2, 2}}, Heaps.kClosest(new int[][]{{1, 3}, {-2, 2}}, 1));
        assertArrayEquals(new int[][]{{3, 3}, {-2, 4}}, Heaps.kClosest(new int[][]{{3, 3}, {5, -1}, {-2, 4}}, 2));
        // ties on distance broken by x then y
        assertArrayEquals(new int[][]{{-1, 0}, {0, -1}, {0, 1}, {1, 0}},
                Heaps.kClosest(new int[][]{{1, 0}, {0, 1}, {0, -1}, {-1, 0}, {5, 5}}, 4));
        assertArrayEquals(new int[][]{{0, 0}, {0, 0}}, Heaps.kClosest(new int[][]{{0, 0}, {0, 0}}, 2));
    }

    @Test void kClosestOverflow() {
        int[][] p = {{100_000, 100_000}, {-100_000, 99_999}, {1, 1}};
        assertArrayEquals(new int[][]{{1, 1}, {-100_000, 99_999}}, Heaps.kClosest(p, 2));
    }

    @Test void kClosestRandomAgainstSort() {
        Random r = new Random(8);
        for (int t = 0; t < 200; t++) {
            int n = 1 + r.nextInt(30);
            int[][] pts = new int[n][];
            for (int i = 0; i < n; i++) pts[i] = new int[]{r.nextInt(11) - 5, r.nextInt(11) - 5};
            int k = 1 + r.nextInt(n);
            int[][] sorted = Arrays.stream(pts).sorted(Comparator.<int[]>comparingLong(p -> (long) p[0] * p[0] + (long) p[1] * p[1])
                    .thenComparingInt(p -> p[0]).thenComparingInt(p -> p[1])).limit(k).toArray(int[][]::new);
            assertArrayEquals(sorted, Heaps.kClosest(pts, k));
        }
    }

    @Test void mergeExamples() {
        assertArrayEquals(new int[]{1, 1, 2, 3, 4, 4, 5, 6},
                Heaps.mergeSorted(new int[][]{{1, 4, 5}, {1, 3, 4}, {2, 6}}));
        assertArrayEquals(new int[]{}, Heaps.mergeSorted(new int[][]{}));
        assertArrayEquals(new int[]{}, Heaps.mergeSorted(new int[][]{{}, {}}));
        assertArrayEquals(new int[]{-3, 0, 7}, Heaps.mergeSorted(new int[][]{{}, {-3, 0, 7}}));
        assertArrayEquals(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}, Heaps.mergeSorted(new int[][]{{Integer.MAX_VALUE}, {Integer.MIN_VALUE}}));
    }

    @Test void mergeLargeAgainstSort() {
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            Random r = new Random(9);
            int[][] arrays = new int[1000][];
            List<Integer> all = new ArrayList<>();
            for (int i = 0; i < arrays.length; i++) {
                arrays[i] = r.ints(r.nextInt(1500), -1000, 1000).sorted().toArray();
                for (int v : arrays[i]) all.add(v);
            }
            int[] exp = all.stream().mapToInt(Integer::intValue).sorted().toArray();
            assertArrayEquals(exp, Heaps.mergeSorted(arrays));
        });
    }
}

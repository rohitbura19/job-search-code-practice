package practice.d20261011.c1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MergeIntervalsTest {

    static String str(int[][] a) { return Arrays.deepToString(a); }
    static int[][] copy(int[][] a) { int[][] c = new int[a.length][]; for (int i = 0; i < a.length; i++) c[i] = a[i].clone(); return c; }

    @Test @DisplayName("examples from the question")
    void examples() {
        assertEquals("[[1, 6], [8, 10], [15, 18]]", str(MergeIntervals.merge(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}})));
        assertEquals("[[1, 5]]", str(MergeIntervals.merge(new int[][]{{1, 4}, {4, 5}})), "touching intervals merge");
    }

    @Test @DisplayName("empty, single, unsorted input, containment, chain")
    void edges() {
        assertEquals("[]", str(MergeIntervals.merge(new int[][]{})));
        assertEquals("[[3, 4]]", str(MergeIntervals.merge(new int[][]{{3, 4}})));
        assertEquals("[[1, 10]]", str(MergeIntervals.merge(new int[][]{{2, 3}, {1, 10}, {4, 5}})));
        assertEquals("[[1, 6]]", str(MergeIntervals.merge(new int[][]{{5, 6}, {3, 4}, {1, 2}, {2, 3}, {4, 5}})));
        assertEquals("[[1, 2], [3, 4]]", str(MergeIntervals.merge(new int[][]{{3, 4}, {1, 2}})));
        assertEquals("[[-5, -1], [0, 0]]", str(MergeIntervals.merge(new int[][]{{0, 0}, {-5, -3}, {-4, -1}})));
        assertEquals("[[0, 2147483647]]", str(MergeIntervals.merge(new int[][]{{0, Integer.MAX_VALUE}, {Integer.MAX_VALUE - 1, Integer.MAX_VALUE}})));
    }

    @Test @DisplayName("meeting rooms")
    void rooms() {
        assertEquals(0, MergeIntervals.minMeetingRooms(new int[][]{}));
        assertEquals(1, MergeIntervals.minMeetingRooms(new int[][]{{1, 4}, {4, 5}}), "a room frees at the end time");
        assertEquals(2, MergeIntervals.minMeetingRooms(new int[][]{{0, 30}, {5, 10}, {15, 20}}));
        assertEquals(3, MergeIntervals.minMeetingRooms(new int[][]{{1, 5}, {2, 6}, {3, 7}}));
        assertEquals(1, MergeIntervals.minMeetingRooms(new int[][]{{7, 10}, {2, 4}}));
    }

    @Test @DisplayName("random: merged output is sorted, disjoint, covers exactly the same points; rooms match a point-count brute force")
    void randomAgainstOracle() {
        Random rnd = new Random(3);
        for (int t = 0; t < 2000; t++) {
            int n = rnd.nextInt(8); int[][] a = new int[n][];
            for (int i = 0; i < n; i++) { int s = rnd.nextInt(15), len = rnd.nextInt(4) + 1; a[i] = new int[]{s, s + len}; }
            boolean[] covered = new boolean[24];       // integer points 0..20 touched by any interval
            for (int[] x : a) for (int p = x[0]; p <= x[1]; p++) covered[p] = true;
            int[][] m = MergeIntervals.merge(copy(a));
            for (int i = 0; i < m.length; i++) {
                assertTrue(m[i][0] <= m[i][1], str(a));
                if (i > 0) assertTrue(m[i - 1][1] < m[i][0], "must be disjoint and sorted: " + str(m) + " from " + str(a));
            }
            boolean[] got = new boolean[24];
            for (int[] x : m) for (int p = x[0]; p <= x[1]; p++) got[p] = true;
            assertEquals(Arrays.toString(covered), Arrays.toString(got), str(a));
            // closed-interval rooms are NOT point overlap (a meeting [s,e] frees at e): count half-open [s, e)
            int best = 0;
            for (int p = 0; p < 24; p++) { int c = 0; for (int[] x : a) if (x[0] <= p && p < x[1]) c++; best = Math.max(best, c); }
            assertEquals(best, MergeIntervals.minMeetingRooms(copy(a)), str(a));
        }
    }

    @Test @DisplayName("200000 intervals in well under a second or two (O(n log n))")
    void largeInput() {
        int n = 200_000; int[][] a = new int[n][]; Random rnd = new Random(1);
        for (int i = 0; i < n; i++) { int s = rnd.nextInt(10_000_000); a[i] = new int[]{s, s + rnd.nextInt(20)}; }
        int[][] b = copy(a);
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            int[][] m = MergeIntervals.merge(a);
            for (int i = 1; i < m.length; i++) assertTrue(m[i - 1][1] < m[i][0]);
            assertTrue(MergeIntervals.minMeetingRooms(b) >= 1);
        });
    }
}

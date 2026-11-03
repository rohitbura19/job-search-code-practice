package practice.d20261103.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class CourseOrderTest {
    @Test void examples() {
        int[][] p = {{1, 0}, {2, 0}, {3, 1}, {3, 2}};
        assertTrue(CourseOrder.canFinish(4, p));
        assertArrayEquals(new int[]{0, 1, 2, 3}, CourseOrder.order(4, p));
        assertEquals(3, CourseOrder.minSemesters(4, p));
        assertArrayEquals(new int[]{1, 2, 0}, CourseOrder.order(3, new int[][]{{0, 1}, {0, 2}}));
        assertEquals(2, CourseOrder.minSemesters(3, new int[][]{{0, 1}, {0, 2}}));
    }

    @Test void cycleAndSelfLoop() {
        assertFalse(CourseOrder.canFinish(2, new int[][]{{0, 1}, {1, 0}}));
        assertEquals(0, CourseOrder.order(2, new int[][]{{0, 1}, {1, 0}}).length);
        assertEquals(-1, CourseOrder.minSemesters(2, new int[][]{{0, 1}, {1, 0}}));
        assertFalse(CourseOrder.canFinish(1, new int[][]{{0, 0}}));
        // a cycle hidden behind a valid prefix
        assertFalse(CourseOrder.canFinish(4, new int[][]{{1, 0}, {2, 1}, {3, 2}, {1, 3}}));
    }

    @Test void noEdgesSingleAndDuplicates() {
        assertArrayEquals(new int[]{0, 1, 2}, CourseOrder.order(3, new int[0][]));
        assertEquals(1, CourseOrder.minSemesters(3, new int[0][]));
        assertEquals(1, CourseOrder.minSemesters(1, new int[0][]));
        assertEquals(2, CourseOrder.minSemesters(2, new int[][]{{1, 0}, {1, 0}, {1, 0}}));
        assertArrayEquals(new int[]{0, 1}, CourseOrder.order(2, new int[][]{{1, 0}, {1, 0}}));
    }

    static int[][] randomDag(Random rnd, int n, int edges) {
        List<Integer> perm = new ArrayList<>();
        for (int i = 0; i < n; i++) perm.add(i);
        Collections.shuffle(perm, rnd);
        int[][] e = new int[edges][];
        for (int i = 0; i < edges; i++) {
            int a = rnd.nextInt(n - 1), b = a + 1 + rnd.nextInt(n - 1 - a);
            e[i] = new int[]{perm.get(b), perm.get(a)};   // perm[a] must precede perm[b]
        }
        return e;
    }

    /** Naive: repeatedly pick the smallest course whose prerequisites are all placed. */
    static int[] bruteOrder(int n, int[][] p) {
        boolean[] done = new boolean[n];
        int[] out = new int[n];
        for (int k = 0; k < n; k++) {
            int pick = -1;
            for (int c = 0; c < n && pick < 0; c++) {
                if (done[c]) continue;
                boolean ok = true;
                for (int[] e : p) if (e[0] == c && !done[e[1]]) { ok = false; break; }
                if (ok) pick = c;
            }
            if (pick < 0) return new int[0];
            done[pick] = true;
            out[k] = pick;
        }
        return out;
    }

    static int bruteSemesters(int n, int[][] p) {
        int[] level = new int[n];
        for (int round = 0; round <= n; round++) {
            boolean changed = false;
            for (int[] e : p) if (level[e[0]] < level[e[1]] + 1) { level[e[0]] = level[e[1]] + 1; changed = true; }
            if (!changed) return Arrays.stream(level).max().orElse(0) + 1;
        }
        return -1;
    }

    @Test void randomDagsVsBruteForce() {
        Random rnd = new Random(5);
        for (int t = 0; t < 300; t++) {
            int n = 2 + rnd.nextInt(9);
            int[][] p = randomDag(rnd, n, rnd.nextInt(2 * n));
            assertArrayEquals(bruteOrder(n, p), CourseOrder.order(n, p));
            assertEquals(bruteSemesters(n, p), CourseOrder.minSemesters(n, p));
            assertTrue(CourseOrder.canFinish(n, p));
        }
    }

    @Test void randomGraphsWithPossibleCycles() {
        Random rnd = new Random(6);
        for (int t = 0; t < 300; t++) {
            int n = 2 + rnd.nextInt(6);
            int[][] p = new int[rnd.nextInt(2 * n)][];
            for (int i = 0; i < p.length; i++) p[i] = new int[]{rnd.nextInt(n), rnd.nextInt(n)};
            int[] expect = bruteOrder(n, p);
            assertArrayEquals(expect, CourseOrder.order(n, p));
            assertEquals(expect.length > 0, CourseOrder.canFinish(n, p));
            assertEquals(expect.length > 0 ? bruteSemesters(n, p) : -1, CourseOrder.minSemesters(n, p));
        }
    }

    @Test void longChainWithinTimeout() {
        int n = 100_000;
        int[][] p = new int[n - 1][];
        for (int i = 1; i < n; i++) p[i - 1] = new int[]{i, i - 1};
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            assertEquals(n, CourseOrder.minSemesters(n, p));
            assertEquals(n, CourseOrder.order(n, p).length);
        });
    }
}

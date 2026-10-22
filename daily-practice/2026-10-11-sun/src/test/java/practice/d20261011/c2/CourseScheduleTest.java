package practice.d20261011.c2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CourseScheduleTest {

    /** true if order is a permutation of 0..n-1 that respects every prerequisite. */
    static boolean valid(int n, int[][] pre, int[] order) {
        if (order.length != n) return false;
        int[] pos = new int[n]; java.util.Arrays.fill(pos, -1);
        for (int i = 0; i < n; i++) { if (order[i] < 0 || order[i] >= n || pos[order[i]] != -1) return false; pos[order[i]] = i; }
        for (int[] p : pre) if (pos[p[1]] > pos[p[0]]) return false;
        return true;
    }

    @Test @DisplayName("examples: chain, diamond, independent courses")
    void examples() {
        int[][] chain = {{1, 0}};
        assertTrue(valid(2, chain, CourseSchedule.findOrder(2, chain)));
        int[][] diamond = {{1, 0}, {2, 0}, {3, 1}, {3, 2}};
        assertTrue(valid(4, diamond, CourseSchedule.findOrder(4, diamond)));
        assertTrue(valid(3, new int[][]{}, CourseSchedule.findOrder(3, new int[][]{})));
    }

    @Test @DisplayName("cycle gives an empty array")
    void cycles() {
        assertEquals(0, CourseSchedule.findOrder(2, new int[][]{{1, 0}, {0, 1}}).length);
        assertEquals(0, CourseSchedule.findOrder(3, new int[][]{{0, 1}, {1, 2}, {2, 0}}).length);
        assertEquals(0, CourseSchedule.findOrder(1, new int[][]{{0, 0}}).length, "self loop");
        assertEquals(0, CourseSchedule.findOrder(4, new int[][]{{1, 0}, {2, 3}, {3, 2}}).length, "cycle in a separate component");
    }

    @Test @DisplayName("edge sizes: zero courses, one course, duplicate prerequisites")
    void edges() {
        assertEquals(0, CourseSchedule.findOrder(0, new int[][]{}).length);
        assertEquals(1, CourseSchedule.findOrder(1, new int[][]{}).length);
        int[][] dup = {{1, 0}, {1, 0}, {2, 1}};
        assertTrue(valid(3, dup, CourseSchedule.findOrder(3, dup)));
    }

    @Test @DisplayName("minimum semesters (parallel levels)")
    void semesters() {
        assertEquals(0, CourseSchedule.minSemesters(0, new int[][]{}));
        assertEquals(1, CourseSchedule.minSemesters(3, new int[][]{}));
        assertEquals(2, CourseSchedule.minSemesters(4, new int[][]{{1, 0}, {2, 0}, {3, 0}}));
        assertEquals(3, CourseSchedule.minSemesters(4, new int[][]{{1, 0}, {2, 0}, {3, 1}, {3, 2}}));
        assertEquals(4, CourseSchedule.minSemesters(4, new int[][]{{1, 0}, {2, 1}, {3, 2}}));
        assertEquals(-1, CourseSchedule.minSemesters(2, new int[][]{{1, 0}, {0, 1}}));
    }

    @Test @DisplayName("random DAGs always give a valid order; adding a back edge on a chain gives a cycle")
    void randomGraphs() {
        Random rnd = new Random(5);
        for (int t = 0; t < 1500; t++) {
            int n = rnd.nextInt(9) + 1; java.util.List<int[]> e = new java.util.ArrayList<>();
            int[] rank = new int[n]; for (int i = 0; i < n; i++) rank[i] = i;
            for (int i = n - 1; i > 0; i--) { int j = rnd.nextInt(i + 1); int x = rank[i]; rank[i] = rank[j]; rank[j] = x; }
            for (int a = 0; a < n; a++) for (int b = 0; b < n; b++) if (rank[b] < rank[a] && rnd.nextInt(4) == 0) e.add(new int[]{a, b});
            int[][] pre = e.toArray(new int[0][]);
            assertTrue(valid(n, pre, CourseSchedule.findOrder(n, pre)), "n=" + n);
            assertTrue(CourseSchedule.minSemesters(n, pre) >= 1);
        }
        int[][] ring = {{1, 0}, {2, 1}, {3, 2}, {0, 3}};
        assertEquals(0, CourseSchedule.findOrder(4, ring).length);
    }

    @Test @DisplayName("long chain of 200000 courses must not recurse (no stack overflow)")
    void largeChain() {
        int n = 200_000; int[][] pre = new int[n - 1][];
        for (int i = 1; i < n; i++) pre[i - 1] = new int[]{i, i - 1};
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            assertTrue(valid(n, pre, CourseSchedule.findOrder(n, pre)));
            assertEquals(n, CourseSchedule.minSemesters(n, pre));
        });
    }
}

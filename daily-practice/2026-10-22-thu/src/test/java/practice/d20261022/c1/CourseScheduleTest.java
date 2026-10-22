package practice.d20261022.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class CourseScheduleTest {
    static void assertValid(int n, int[][] pre, int[] order) {
        assertEquals(n, order.length, "must contain every course");
        int[] pos = new int[n];
        Arrays.fill(pos, -1);
        for (int i = 0; i < n; i++) {
            assertTrue(order[i] >= 0 && order[i] < n && pos[order[i]] == -1, "each course exactly once");
            pos[order[i]] = i;
        }
        for (int[] p : pre) assertTrue(pos[p[1]] < pos[p[0]], p[1] + " must come before " + p[0]);
    }

    @Test void examples() {
        int[][] pre = {{1, 0}, {2, 0}, {3, 1}, {3, 2}};
        assertValid(4, pre, CourseSchedule.findOrder(4, pre));
        assertEquals(0, CourseSchedule.findOrder(2, new int[][]{{1, 0}, {0, 1}}).length);
    }

    @Test void edgeCases() {
        assertEquals(0, CourseSchedule.findOrder(0, new int[0][]).length);
        assertArrayEquals(new int[]{0}, CourseSchedule.findOrder(1, new int[0][]));
        assertEquals(0, CourseSchedule.findOrder(3, new int[][]{{1, 1}}).length);       // self loop
        int[][] dup = {{1, 0}, {1, 0}, {1, 0}};
        assertValid(2, dup, CourseSchedule.findOrder(2, dup));                            // duplicate edges
        assertEquals(5, CourseSchedule.findOrder(5, new int[0][]).length);               // no edges
    }

    @Test void randomDagsAndCycles() {
        Random r = new Random(5);
        for (int t = 0; t < 500; t++) {
            int n = 3 + r.nextInt(10);
            List<Integer> perm = new ArrayList<>();
            for (int i = 0; i < n; i++) perm.add(i);
            Collections.shuffle(perm, r);
            List<int[]> edges = new ArrayList<>();
            for (int e = r.nextInt(2 * n); e > 0; e--) {
                int i = r.nextInt(n), j = r.nextInt(n);
                if (i < j) edges.add(new int[]{perm.get(j), perm.get(i)});    // perm[i] before perm[j]
            }
            int[][] dag = edges.toArray(new int[0][]);
            assertValid(n, dag, CourseSchedule.findOrder(n, dag));
            edges.add(new int[]{perm.get(0), perm.get(1)});                    // add a 3-cycle a->b->c->a
            edges.add(new int[]{perm.get(1), perm.get(2)});
            edges.add(new int[]{perm.get(2), perm.get(0)});
            assertEquals(0, CourseSchedule.findOrder(n, edges.toArray(new int[0][])).length);
        }
    }

    @Test void longChain() {
        int n = 200_000;
        int[][] chain = new int[n - 1][];
        for (int i = 1; i < n; i++) chain[i - 1] = new int[]{i, i - 1};
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertValid(n, chain, CourseSchedule.findOrder(n, chain)));
    }
}

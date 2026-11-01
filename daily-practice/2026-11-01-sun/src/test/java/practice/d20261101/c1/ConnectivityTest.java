package practice.d20261101.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

class ConnectivityTest {
    private static int bfsComponents(int[][] m) {
        boolean[] seen = new boolean[m.length];
        int c = 0;
        for (int s = 0; s < m.length; s++) {
            if (seen[s]) continue;
            c++;
            var q = new java.util.ArrayDeque<Integer>();
            q.add(s); seen[s] = true;
            while (!q.isEmpty()) { int u = q.poll(); for (int v = 0; v < m.length; v++) if (m[u][v] == 1 && !seen[v]) { seen[v] = true; q.add(v); } }
        }
        return c;
    }

    @Test void provincesExamples() {
        assertEquals(2, Connectivity.findCircleNum(new int[][] {{1, 1, 0}, {1, 1, 0}, {0, 0, 1}}));
        assertEquals(3, Connectivity.findCircleNum(new int[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}));
        assertEquals(1, Connectivity.findCircleNum(new int[][] {{1}}));
        assertEquals(0, Connectivity.findCircleNum(new int[0][0]));
    }

    @Test void provincesRandomAgainstBfs() {
        Random r = new Random(7);
        for (int t = 0; t < 300; t++) {
            int n = 1 + r.nextInt(12);
            int[][] m = new int[n][n];
            for (int i = 0; i < n; i++) { m[i][i] = 1; for (int j = i + 1; j < n; j++) if (r.nextInt(8) == 0) m[i][j] = m[j][i] = 1; }
            assertEquals(bfsComponents(m), Connectivity.findCircleNum(m));
        }
    }

    @Test void provincesLargeChain() {
        int n = 1500;
        int[][] m = new int[n][n];
        for (int i = 0; i < n; i++) { m[i][i] = 1; if (i + 1 < n) m[i][i + 1] = m[i + 1][i] = 1; }
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(1, Connectivity.findCircleNum(m)));
    }

    @Test void redundantExamples() {
        assertArrayEquals(new int[] {2, 3}, Connectivity.findRedundantConnection(new int[][] {{1, 2}, {1, 3}, {2, 3}}));
        assertArrayEquals(new int[] {1, 4}, Connectivity.findRedundantConnection(new int[][] {{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}}));
        assertArrayEquals(new int[] {1, 2}, Connectivity.findRedundantConnection(new int[][] {{1, 2}, {1, 2}}));
    }

    @Test void redundantLongPathNoRecursion() {
        int n = 200_000;
        int[][] e = new int[n][];
        for (int i = 1; i < n; i++) e[i - 1] = new int[] {i, i + 1};
        e[n - 1] = new int[] {1, n};
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertArrayEquals(new int[] {1, n}, Connectivity.findRedundantConnection(e)));
    }

    @Test void longestExamples() {
        assertEquals(4, Connectivity.longestConsecutive(new int[] {100, 4, 200, 1, 3, 2}));
        assertEquals(9, Connectivity.longestConsecutive(new int[] {0, 3, 7, 2, 5, 8, 4, 6, 0, 1}));
        assertEquals(0, Connectivity.longestConsecutive(new int[0]));
        assertEquals(1, Connectivity.longestConsecutive(new int[] {5}));
        assertEquals(3, Connectivity.longestConsecutive(new int[] {1, 2, 0, 1}));
        assertEquals(3, Connectivity.longestConsecutive(new int[] {-1, -3, -2, 10}));
    }

    @Test void longestOverflow() {
        assertEquals(1, Connectivity.longestConsecutive(new int[] {Integer.MIN_VALUE, Integer.MAX_VALUE}));
        assertEquals(2, Connectivity.longestConsecutive(new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE - 1}));
        assertEquals(2, Connectivity.longestConsecutive(new int[] {Integer.MIN_VALUE, Integer.MIN_VALUE + 1}));
    }

    @Test void longestRandomAgainstSort() {
        Random r = new Random(3);
        for (int t = 0; t < 300; t++) {
            int[] a = r.ints(r.nextInt(30), -15, 15).toArray();
            int best = 0, run = 0;
            Integer prev = null;
            for (int x : new TreeSet<>(Arrays.stream(a).boxed().toList())) { run = (prev != null && x == prev + 1) ? run + 1 : 1; prev = x; best = Math.max(best, run); }
            assertEquals(best, Connectivity.longestConsecutive(a), Arrays.toString(a));
        }
    }

    @Test void longestLarge() {
        int[] a = new Random(5).ints(1_000_000, 0, 2_000_000).toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertTrue(Connectivity.longestConsecutive(a) >= 1));
    }
}

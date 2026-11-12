package practice.d20261112.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class UnionFindProblemsTest {
    @Test void provinces() {
        assertEquals(2, UnionFindProblems.findCircleNum(new int[][]{{1, 1, 0}, {1, 1, 0}, {0, 0, 1}}));
        assertEquals(3, UnionFindProblems.findCircleNum(new int[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}));
        assertEquals(1, UnionFindProblems.findCircleNum(new int[][]{{1}}));
        assertEquals(0, UnionFindProblems.findCircleNum(new int[0][0]));
    }
    @Test void provincesMatchDfsOnRandom() {
        Random r = new Random(7);
        for (int t = 0; t < 200; t++) {
            int n = 1 + r.nextInt(12);
            int[][] g = new int[n][n];
            for (int i = 0; i < n; i++) { g[i][i] = 1; for (int j = i + 1; j < n; j++) g[i][j] = g[j][i] = r.nextInt(6) == 0 ? 1 : 0; }
            boolean[] seen = new boolean[n];
            int comps = 0;
            for (int i = 0; i < n; i++) if (!seen[i]) { comps++; Deque<Integer> st = new ArrayDeque<>(List.of(i)); seen[i] = true;
                while (!st.isEmpty()) { int u = st.pop(); for (int v = 0; v < n; v++) if (g[u][v] == 1 && !seen[v]) { seen[v] = true; st.push(v); } } }
            assertEquals(comps, UnionFindProblems.findCircleNum(g));
        }
    }
    @Test void redundant() {
        assertArrayEquals(new int[]{2, 3}, UnionFindProblems.findRedundantConnection(new int[][]{{1, 2}, {1, 3}, {2, 3}}));
        assertArrayEquals(new int[]{1, 4}, UnionFindProblems.findRedundantConnection(new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}}));
    }
    @Test void redundantLongChainIsFast() {
        int n = 200_000;
        int[][] e = new int[n][];
        for (int i = 1; i < n; i++) e[i - 1] = new int[]{i, i + 1};
        e[n - 1] = new int[]{1, n};
        assertArrayEquals(new int[]{1, n}, assertTimeoutPreemptively(Duration.ofSeconds(2), () -> UnionFindProblems.findRedundantConnection(e)));
    }
    @Test void accounts() {
        List<List<String>> in = List.of(
                List.of("John", "johnsmith@mail.com", "john_newyork@mail.com"),
                List.of("John", "johnsmith@mail.com", "john00@mail.com"),
                List.of("Mary", "mary@mail.com"),
                List.of("John", "johnnybravo@mail.com"));
        List<List<String>> expected = List.of(
                List.of("John", "john00@mail.com", "john_newyork@mail.com", "johnsmith@mail.com"),
                List.of("John", "johnnybravo@mail.com"),
                List.of("Mary", "mary@mail.com"));
        assertEquals(expected, UnionFindProblems.accountsMerge(in));
    }
    @Test void accountsTransitiveAndDuplicates() {
        List<List<String>> in = List.of(
                List.of("A", "a@x", "b@x"), List.of("A", "c@x"), List.of("A", "b@x", "c@x", "b@x"));
        assertEquals(List.of(List.of("A", "a@x", "b@x", "c@x")), UnionFindProblems.accountsMerge(in));
        assertEquals(List.of(), UnionFindProblems.accountsMerge(List.of()));
    }
}

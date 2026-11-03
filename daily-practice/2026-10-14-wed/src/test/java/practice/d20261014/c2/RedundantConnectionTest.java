package practice.d20261014.c2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RedundantConnectionTest {

    @Test @DisplayName("sheet examples")
    void examples() {
        assertEquals("[2, 3]", Arrays.toString(RedundantConnection.redundant(new int[][]{{1, 2}, {1, 3}, {2, 3}})));
        assertEquals("[1, 4]", Arrays.toString(RedundantConnection.redundant(new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}})));
    }

    @Test @DisplayName("smallest case: a parallel edge")
    void parallel() {
        assertEquals("[1, 2]", Arrays.toString(RedundantConnection.redundant(new int[][]{{1, 2}, {1, 2}})));
        assertEquals("[2, 1]", Arrays.toString(RedundantConnection.redundant(new int[][]{{1, 2}, {2, 1}})));
    }

    /** Oracle: scan from the last edge; the first one whose removal leaves a connected graph is the answer. */
    static int[] brute(int[][] edges) {
        int n = edges.length;
        for (int skip = n - 1; skip >= 0; skip--) {
            List<List<Integer>> g = new ArrayList<>();
            for (int i = 0; i <= n; i++) g.add(new ArrayList<>());
            for (int i = 0; i < n; i++) if (i != skip) { g.get(edges[i][0]).add(edges[i][1]); g.get(edges[i][1]).add(edges[i][0]); }
            boolean[] seen = new boolean[n + 1];
            List<Integer> stack = new ArrayList<>(List.of(1)); seen[1] = true; int cnt = 1;
            while (!stack.isEmpty()) {
                int u = stack.remove(stack.size() - 1);
                for (int v : g.get(u)) if (!seen[v]) { seen[v] = true; cnt++; stack.add(v); }
            }
            if (cnt == n) return edges[skip];
        }
        throw new IllegalStateException();
    }

    static int[][] randomCase(Random rnd, int n) {
        List<int[]> edges = new ArrayList<>();
        for (int v = 2; v <= n; v++) edges.add(new int[]{1 + rnd.nextInt(v - 1), v});     // random tree
        int a = 1 + rnd.nextInt(n), b = 1 + rnd.nextInt(n);
        while (a == b) { a = 1 + rnd.nextInt(n); b = 1 + rnd.nextInt(n); }
        edges.add(new int[]{a, b});                                                     // the extra edge (may duplicate a tree edge)
        Collections.shuffle(edges, rnd);
        if (rnd.nextBoolean()) for (int[] e : edges) if (rnd.nextBoolean()) { int t = e[0]; e[0] = e[1]; e[1] = t; }
        return edges.toArray(new int[0][]);
    }

    @Test @DisplayName("random trees plus one extra edge against the brute-force oracle")
    void random() {
        Random rnd = new Random(14);
        for (int t = 0; t < 400; t++) {
            int[][] edges = randomCase(rnd, 2 + rnd.nextInt(12));
            assertEquals(Arrays.toString(brute(edges)), Arrays.toString(RedundantConnection.redundant(edges)),
                    Arrays.deepToString(edges));
        }
    }

    @Test @DisplayName("200000 nodes in a path plus a closing edge: linear and no stack overflow")
    void large() {
        int n = 200_000;
        int[][] edges = new int[n][];
        for (int i = 1; i < n; i++) edges[i - 1] = new int[]{i, i + 1};
        edges[n - 1] = new int[]{1, n};
        assertTimeoutPreemptively(Duration.ofSeconds(3),
                () -> assertEquals("[1, " + n + "]", Arrays.toString(RedundantConnection.redundant(edges))));
    }
}

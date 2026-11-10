package practice.d20261110.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class BipartiteGraphsTest {
    @Test void bipartiteExamples() {
        assertFalse(BipartiteGraphs.isBipartite(new int[][]{{1, 2, 3}, {0, 2}, {0, 1, 3}, {0, 2}}));
        assertTrue(BipartiteGraphs.isBipartite(new int[][]{{1, 3}, {0, 2}, {1, 3}, {0, 2}}));
    }

    @Test void bipartiteEdges() {
        assertTrue(BipartiteGraphs.isBipartite(new int[][]{}));
        assertTrue(BipartiteGraphs.isBipartite(new int[][]{{}}));
        assertTrue(BipartiteGraphs.isBipartite(new int[][]{{}, {}, {}}));                         // isolated nodes
        assertFalse(BipartiteGraphs.isBipartite(new int[][]{{}, {2, 3}, {1, 3}, {1, 2}}));         // triangle in a second component
    }

    @Test void partitionExamples() {
        assertTrue(BipartiteGraphs.possibleBipartition(4, new int[][]{{1, 2}, {1, 3}, {2, 4}}));
        assertFalse(BipartiteGraphs.possibleBipartition(3, new int[][]{{1, 2}, {1, 3}, {2, 3}}));
        assertFalse(BipartiteGraphs.possibleBipartition(5, new int[][]{{1, 2}, {2, 3}, {3, 4}, {4, 5}, {1, 5}}));
        assertTrue(BipartiteGraphs.possibleBipartition(1, new int[][]{}));
        assertTrue(BipartiteGraphs.possibleBipartition(3, new int[][]{{1, 2}, {1, 2}, {2, 1}}));
    }

    @Test void safeNodesExamples() {
        assertEquals(List.of(2, 4, 5, 6), BipartiteGraphs.eventualSafeNodes(new int[][]{{1, 2}, {2, 3}, {5}, {0}, {5}, {}, {}}));
        assertEquals(List.of(4), BipartiteGraphs.eventualSafeNodes(new int[][]{{1, 2, 3, 4}, {1, 2}, {3, 4}, {0, 4}, {}}));
        assertEquals(List.of(), BipartiteGraphs.eventualSafeNodes(new int[][]{{0}}));          // self-loop
        assertEquals(List.of(), BipartiteGraphs.eventualSafeNodes(new int[][]{}));
        assertEquals(List.of(0, 1), BipartiteGraphs.eventualSafeNodes(new int[][]{{1}, {}}));
    }

    @Test void randomBipartiteAgainstBruteForce() {
        Random r = new Random(6);
        for (int t = 0; t < 400; t++) {
            int n = 1 + r.nextInt(8);
            List<int[]> edges = new ArrayList<>();
            for (int a = 0; a < n; a++) for (int b = a + 1; b < n; b++) if (r.nextInt(4) == 0) edges.add(new int[]{a, b});
            List<List<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
            for (int[] e : edges) { adj.get(e[0]).add(e[1]); adj.get(e[1]).add(e[0]); }
            int[][] g = new int[n][];
            for (int i = 0; i < n; i++) g[i] = adj.get(i).stream().mapToInt(x -> x).toArray();
            boolean exp = false;
            for (int mask = 0; mask < (1 << n) && !exp; mask++) {
                boolean ok = true;
                for (int[] e : edges) if (((mask >> e[0]) & 1) == ((mask >> e[1]) & 1)) { ok = false; break; }
                exp = ok;
            }
            assertEquals(exp, BipartiteGraphs.isBipartite(g), Arrays.deepToString(g));
            int[][] dis = edges.stream().map(e -> new int[]{e[0] + 1, e[1] + 1}).toArray(int[][]::new);
            assertEquals(exp, BipartiteGraphs.possibleBipartition(n, dis));
        }
    }

    @Test void randomSafeNodesAgainstFixpoint() {
        Random r = new Random(7);
        for (int t = 0; t < 400; t++) {
            int n = 1 + r.nextInt(9);
            int[][] g = new int[n][];
            for (int u = 0; u < n; u++) g[u] = r.ints(r.nextInt(3), 0, n).distinct().toArray();
            // reference: repeatedly mark nodes whose successors are all safe (naive O(n^3) fixpoint)
            boolean[] safe = new boolean[n];
            boolean changed = true;
            while (changed) {
                changed = false;
                for (int u = 0; u < n; u++) {
                    if (safe[u]) continue;
                    boolean all = true;
                    for (int v : g[u]) if (!safe[v]) all = false;
                    if (all) { safe[u] = true; changed = true; }
                }
            }
            List<Integer> exp = new ArrayList<>();
            for (int u = 0; u < n; u++) if (safe[u]) exp.add(u);
            assertEquals(exp, BipartiteGraphs.eventualSafeNodes(g), Arrays.deepToString(g));
        }
    }

    @Test void longChainWithinTimeoutAndNoStackOverflow() {
        int n = 200_000;
        int[][] path = new int[n][];
        int[][] dir = new int[n][];
        for (int i = 0; i < n; i++) {
            path[i] = i == 0 ? new int[]{1} : i == n - 1 ? new int[]{n - 2} : new int[]{i - 1, i + 1};
            dir[i] = i == n - 1 ? new int[]{} : new int[]{i + 1};
        }
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertTrue(BipartiteGraphs.isBipartite(path));
            assertEquals(n, BipartiteGraphs.eventualSafeNodes(dir).size());
        });
    }
}

package practice.d20261103.c1;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Kahn's algorithm: repeatedly take nodes with indegree 0. If fewer than n are taken there is a cycle.
 * A min-heap of ready nodes gives the lexicographically smallest order: O((n + e) log n). A plain queue processed level by level gives
 * the number of semesters (the longest chain): O(n + e). Duplicate edges are counted in the indegree and relaxed again, so they cancel out.
 */
public final class CourseOrder {
    private CourseOrder() {}

    private static List<List<Integer>> graph(int n, int[][] prereq, int[] indeg) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());
        for (int[] e : prereq) { g.get(e[1]).add(e[0]); indeg[e[0]]++; }
        return g;
    }

    public static boolean canFinish(int n, int[][] prereq) { return order(n, prereq).length == n; }

    public static int[] order(int n, int[][] prereq) {
        int[] indeg = new int[n];
        List<List<Integer>> g = graph(n, prereq, indeg);
        PriorityQueue<Integer> ready = new PriorityQueue<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) ready.add(i);
        int[] out = new int[n];
        int k = 0;
        while (!ready.isEmpty()) {
            int u = ready.poll();
            out[k++] = u;
            for (int v : g.get(u)) if (--indeg[v] == 0) ready.add(v);
        }
        return k == n ? out : new int[0];
    }

    public static int minSemesters(int n, int[][] prereq) {
        int[] indeg = new int[n];
        List<List<Integer>> g = graph(n, prereq, indeg);
        int[] queue = new int[n];
        int head = 0, tail = 0, semesters = 0;
        for (int i = 0; i < n; i++) if (indeg[i] == 0) queue[tail++] = i;
        while (head < tail) {
            semesters++;
            for (int levelEnd = tail; head < levelEnd; head++)
                for (int v : g.get(queue[head])) if (--indeg[v] == 0) queue[tail++] = v;
        }
        return tail == n ? semesters : -1;
    }
}

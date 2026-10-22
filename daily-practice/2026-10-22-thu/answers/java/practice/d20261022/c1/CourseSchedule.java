package practice.d20261022.c1;

/**
 * Kahn's algorithm (BFS topological sort): O(V + E) time and space.
 * Repeatedly take a course with in-degree 0; if fewer than n courses come out, the rest sit on or behind a cycle.
 * Iterative on purpose: a recursive DFS overflows the stack on a 200,000-long chain.
 * The graph is stored as arrays (head/next/to) to avoid 200k small lists.
 */
public final class CourseSchedule {
    private CourseSchedule() {}

    public static int[] findOrder(int n, int[][] prereq) {
        int[] head = new int[n], next = new int[prereq.length], to = new int[prereq.length], indeg = new int[n];
        java.util.Arrays.fill(head, -1);
        for (int i = 0; i < prereq.length; i++) {
            int course = prereq[i][0], first = prereq[i][1];
            to[i] = course;
            next[i] = head[first];
            head[first] = i;
            indeg[course]++;
        }
        int[] order = new int[n];
        int qh = 0, qt = 0;
        for (int c = 0; c < n; c++) if (indeg[c] == 0) order[qt++] = c;
        while (qh < qt) {
            int c = order[qh++];
            for (int e = head[c]; e != -1; e = next[e]) {
                if (--indeg[to[e]] == 0) order[qt++] = to[e];
            }
        }
        return qt == n ? order : new int[0];
    }
}

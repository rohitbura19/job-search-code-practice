# Answers: C2-course-schedule-ii

(Spoilers. Open only after your attempt.)

**Reference (Kahn's algorithm)**
```java
public int[] findOrder(int n, int[][] pre) {
    List<List<Integer>> g = new ArrayList<>();
    for (int i = 0; i < n; i++) g.add(new ArrayList<>());
    int[] indeg = new int[n];
    for (int[] p : pre) { g.get(p[1]).add(p[0]); indeg[p[0]]++; }
    Deque<Integer> q = new ArrayDeque<>();
    for (int i = 0; i < n; i++) if (indeg[i] == 0) q.add(i);
    int[] order = new int[n]; int idx = 0;
    while (!q.isEmpty()) {
        int u = q.poll(); order[idx++] = u;
        for (int v : g.get(u)) if (--indeg[v] == 0) q.add(v);
    }
    return idx == n ? order : new int[0];
}
```
O(V+E). Semesters = process the queue level by level. Cycle report = DFS with colours (white/grey/black) and a parent stack.

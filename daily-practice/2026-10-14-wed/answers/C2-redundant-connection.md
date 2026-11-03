# Answers: C2-redundant-connection

(Spoilers. Open only after your attempt.)

**Reference**
```java
static class DSU {
    final int[] p, r;
    DSU(int n) { p = new int[n + 1]; r = new int[n + 1]; for (int i = 0; i <= n; i++) p[i] = i; }
    int find(int x) { while (p[x] != x) { p[x] = p[p[x]]; x = p[x]; } return x; }   // path halving, iterative: no stack overflow
    boolean union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra == rb) return false;                       // already connected: this edge closes a cycle
        if (r[ra] < r[rb]) { int t = ra; ra = rb; rb = t; }
        p[rb] = ra; if (r[ra] == r[rb]) r[ra]++;          // attach the shorter tree under the taller one
        return true;
    }
}
static int[] redundant(int[][] edges) {
    DSU d = new DSU(edges.length);
    for (int[] e : edges) if (!d.union(e[0], e[1])) return e;     // the first edge that joins two already-joined nodes
    return new int[0];
}
```
Verified: `[2, 3]` and `[1, 4]`. O(n α(n)) which is effectively O(n). Dynamic components: start with `count = n`, decrement on each successful `union`. Directed graph version (Redundant Connection II) has two failure modes, a node with two parents and a cycle, and needs candidate edge elimination. Keep the iterative `find`; the textbook recursive one overflows on a long chain.

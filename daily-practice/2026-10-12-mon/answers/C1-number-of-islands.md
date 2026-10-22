# Answers: C1-number-of-islands

(Spoilers. Open only after your attempt.)

**Reference**
```java
public int numIslands(char[][] g) {
    int count = 0;
    for (int r = 0; r < g.length; r++)
        for (int c = 0; c < g[0].length; c++)
            if (g[r][c] == '1') { count++; sink(g, r, c); }
    return count;
}
private void sink(char[][] g, int r, int c) {
    if (r < 0 || c < 0 || r >= g.length || c >= g[0].length || g[r][c] != '1') return;
    g[r][c] = '0';                                  // mark visited in place
    sink(g, r + 1, c); sink(g, r - 1, c); sink(g, r, c + 1); sink(g, r, c - 1);
}
```
O(R·C) time. Recursion depth can reach R·C, so for huge grids use an explicit `ArrayDeque` BFS/DFS. Do not mutate the input if it must be preserved (use a `boolean[][]`). Dynamic additions: Union-Find (DSU) with path compression and union by rank, count-- on each successful union, near O(1) per add. Distributed grid: split into tiles, count islands per tile, then merge across tile borders with DSU on border cells.

Note: the test in this kit uses a 1000 x 1000 all-land grid and requires the input to stay unchanged, so the recursive in-place version above is the textbook baseline, not what passes: use an explicit stack (an `int[]` of cell ids works) with a `boolean[][]` visited array. `IslandCounter`: `land[]`, `parent[]`, `size[]`; on `addLand` set `count++`, then for each land neighbour `if (union(id, neighbour)) count--`; ignore a cell that is already land.

> Code: `src/main/java/practice/d20261014/c2/Dsu.java`, `RedundantConnection.java`. Tests: `DsuTest`, `RedundantConnectionTest`.

### C2. Redundant Connection (Union-Find / DSU)
A tree with `n` nodes (labelled 1..n) had one extra edge added. `edges[i] = [a, b]`. Return the edge that can be removed so the graph becomes a tree; if several answers, return the one that occurs last in the input. `[[1,2],[1,3],[2,3]] -> [2,3]`; `[[1,2],[2,3],[3,4],[1,4],[1,5]] -> [1,4]`.

Interviewer follow-up: explain path compression and union by rank, and the resulting complexity. Where else is DSU the right tool (Kruskal's MST, connected components as edges arrive, network partitions)? How would the directed version differ? Count connected components after each edge arrives. (`Dsu` has a `count()` for that last one.)

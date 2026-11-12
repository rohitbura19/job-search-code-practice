# C1 - Union-find: provinces, redundant edge, accounts merge (25 min) - `c1/UnionFindProblems.java`

1. `findCircleNum(int[][] isConnected)`: number of connected groups of cities. `{{1,1,0},{1,1,0},{0,0,1}}` -> 2.
2. `findRedundantConnection(int[][] edges)`: a tree on nodes 1..n plus one extra edge; return the last edge in the input that closes a cycle. `{{1,2},{1,3},{2,3}}` -> `[2,3]`. n up to 200,000 (a path-compression-less chain walk times out).
3. `accountsMerge(List<List<String>> accounts)`: `[name, email...]`; accounts sharing any email are one person (transitively; the same name does not merge people). Return `[name, sorted distinct emails...]` rows ordered by name then first email.

Say the complexity of each: `find`/`union` amortised, and the overall bound for (3) in terms of the number of emails.

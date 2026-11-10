# C1 - Bipartite and safe nodes (25 min) - `c1/BipartiteGraphs.java`

1. `isBipartite(int[][] graph)`: undirected adjacency lists (symmetric, maybe disconnected, no self-loops). Can the nodes be 2-coloured? `[[1,2,3],[0,2],[0,1,3],[0,2]]` -> false (triangle); `[[1,3],[0,2],[1,3],[0,2]]` -> true.
2. `possibleBipartition(int n, int[][] dislikes)`: people 1..n, `dislikes[i] = {a, b}`: they cannot share a group. Can everyone be split in two groups? Duplicate pairs possible. `n=4, [[1,2],[1,3],[2,4]]` -> true; `n=3, [[1,2],[1,3],[2,3]]` -> false.
3. `eventualSafeNodes(int[][] graph)`: directed graph; a node is safe if every path from it reaches a terminal node (no out-edges). Return the safe nodes ascending. `[[1,2],[2,3],[5],[0],[5],[],[]]` -> `[2,4,5,6]`.

Constraints: up to 200,000 nodes - recursion will overflow the stack; use iterative BFS / Kahn. O(V + E).

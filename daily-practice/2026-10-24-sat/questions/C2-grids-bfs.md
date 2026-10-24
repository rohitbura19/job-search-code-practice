> Code: `src/main/java/practice/d20261024/c2/Grids.java`. Test: `GridsTest`.

# C2. Rotting Oranges (multi-source BFS) and Shortest Path in a Binary Matrix (25 min)

(a) `minutesToRot(grid)`: `0` empty, `1` fresh, `2` rotten. Every minute each fresh orange 4-adjacent to a rotten one rots. Return the minutes until none is fresh, `0` if there was none, `-1` if one can never rot. `[[2,1,1],[1,1,0],[0,1,1]] -> 4`. Do not mutate the input.
(b) `shortestPath(grid)`: `n x n` of `0` (open) and `1` (blocked); the length in CELLS of the shortest 8-directional path from `(0,0)` to `(n-1,n-1)` through open cells, else `-1`. `[[0,1],[1,0]] -> 2`.

Constraints: up to `1500 x 1500`, O(cells) time. Queue of cell indices in an `int[]`.

Say out loud: why all sources go into the queue at distance 0 (instead of one BFS per source); why you mark visited when ENQUEUING, not when dequeuing; how you count levels without storing the distance per cell; why a DFS cannot give shortest paths in an unweighted grid; what changes for weighted edges (Dijkstra) or 0/1 weights (0-1 BFS with a deque).

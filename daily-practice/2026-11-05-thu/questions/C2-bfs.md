# C2. BFS family (20 min)

File `c2/BfsProblems.java`. Test: `BfsProblemsTest`.

1. `orangesRotting(int[][] grid)` - 0 empty, 1 fresh, 2 rotten; rot spreads 4-directionally each minute. Minutes until none fresh, -1 if impossible, 0 if none fresh. Do not mutate the input.
2. `nearestZero(int[][] mat)` - distance of every cell to the nearest 0 (4-directional steps). `[[0,0,0],[0,1,0],[1,1,1]] -> [[0,0,0],[0,1,0],[1,2,1]]`.
3. `openLock(String[] deadends, String target)` - wheels `0000..9999`, one move turns one wheel one step with wrap; start `"0000"`; fewest moves to `target` avoiding dead ends, else -1. A dead-end start or target gives -1.

Constraints: grid up to 1500 x 1500; up to 5000 dead ends.

# C2. Grid graphs (20 min) - `c2/Grids.java`

1. `numIslands(char[][] grid)` - connected groups of `'1'` (4 directions). You may modify the grid. 1000 x 1000 all land must not overflow the stack.
2. `maxAreaOfIsland(int[][] grid)` - size of the largest group of 1s.
3. `pacificAtlantic(int[][] heights)` - Pacific borders the top row and left column, Atlantic the bottom row and right column. Water flows to 4-neighbours with height <= current. Return all `[r, c]` that reach both oceans, sorted by row then column. Required: O(R*C), so search FROM the oceans, uphill.
Examples: `11110/11010/11000/00000 -> 1 island`; `11000/11000/00100/00011 -> 3`; `101/010/101 -> 5` (diagonals do not connect).
Paper: when would union-find beat DFS here? (a stream of cells turning to land: "number of islands II")

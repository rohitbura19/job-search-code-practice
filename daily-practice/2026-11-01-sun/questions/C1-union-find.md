# C1. Connectivity: provinces, redundant connection, longest consecutive sequence (20 min)

`c1/Connectivity.java`

1. `findCircleNum(int[][] isConnected)`: number of connected groups in an adjacency matrix (n up to 1500).
2. `findRedundantConnection(int[][] edges)`: nodes `1..n`, n edges = a tree plus one extra edge; return the edge that closes the cycle (the last one in the input if several qualify). n up to 200 000: no recursion.
3. `longestConsecutive(int[] nums)`: O(n), duplicates allowed, any order, values may be the `int` limits. `[100,4,200,1,3,2] -> 4`.

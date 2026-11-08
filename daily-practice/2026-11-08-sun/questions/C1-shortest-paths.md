# C1 - Weighted shortest paths (25 min) - `c1/ShortestPaths.java`

1. `networkDelayTime(int[][] times, int n, int k)`: nodes 1..n, `times[i] = {u, v, w}` directed, w >= 0. Time until all nodes get a signal sent from k, or -1. `[[2,1,1],[2,3,1],[3,4,1]], n=4, k=2` -> 2.
2. `minimumEffortPath(int[][] heights)`: 4-directional moves from top-left to bottom-right; effort of a route = max absolute difference of consecutive cells; minimise it. `[[1,2,2],[3,8,2],[5,3,5]]` -> 2.
3. `cheapestFlightWithinKStops(n, flights, src, dst, k)`: `flights[i] = {from, to, price}`; at most k stops (k+1 flights); -1 if impossible. 4 cities, `[[0,1,100],[1,2,100],[2,0,100],[1,3,600],[2,3,200]]`, 0 -> 3: k=1 gives 700, k=2 gives 400.

Constraints: up to 10^5 nodes and 3*10^5 edges; grid up to 1000 x 1000; heights are non-negative `int`s (differences must not overflow).

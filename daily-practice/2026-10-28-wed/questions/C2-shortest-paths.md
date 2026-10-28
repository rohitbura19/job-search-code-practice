> Code: `src/main/java/practice/d20261028/c2/ShortestPaths.java`. Test: `ShortestPathsTest` (Floyd-Warshall and DFS comparison on random graphs, 10^5-node graph). **25 min.**

# C2. Network delay (Dijkstra) and cheapest flight within k stops (Bellman-Ford)

1. `networkDelay(times, n, k)`: directed weighted edges `{u, v, w}`, nodes `1..n`. Time until every node got the signal sent from `k`, or `-1`. `[[2,1,1],[2,3,1],[3,4,1]]`, n=4, k=2 -> `2`.
2. `cheapestWithStops(n, flights, src, dst, k)`: cheapest price with at most `k` stops (at most `k+1` flights), else `-1`. Flights `[[0,1,100],[1,2,100],[2,0,100],[1,3,600],[2,3,200]]`, 0 -> 3: `k=1` -> `700`, `k=2` -> `400`.

Constraints: item 1 up to `10^5` nodes and `2*10^5` edges (weights 0..100); item 2 up to `5000` cities, `2*10^5` flights, `k <= 5`. Parallel edges, self-loops and zero weights occur.
Say out loud: why a plain Dijkstra is wrong for item 2 (the cheapest path may use more stops than allowed), why Bellman-Ford must relax from a copy of the previous round, and what breaks in Dijkstra with a negative edge.

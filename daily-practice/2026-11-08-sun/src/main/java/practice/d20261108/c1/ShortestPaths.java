package practice.d20261108.c1;

public final class ShortestPaths {
    private ShortestPaths() {}

    /**
     * Nodes are 1..n, times[i] = {from, to, weight} (directed, weight >= 0). A signal starts at k.
     * Return the time until every node has received it, or -1 if some node is unreachable.
     */
    public static int networkDelayTime(int[][] times, int n, int k) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Grid of heights. Move up/down/left/right from (0,0) to (rows-1, cols-1). The effort of a route is the
     * maximum absolute height difference between two consecutive cells. Return the minimum effort over all routes.
     */
    public static int minimumEffortPath(int[][] heights) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Cities 0..n-1, flights[i] = {from, to, price} (directed). Cheapest price from src to dst using at most k stops
     * (at most k+1 flights), or -1 if impossible.
     */
    public static int cheapestFlightWithinKStops(int n, int[][] flights, int src, int dst, int k) { throw new UnsupportedOperationException("TODO"); }
}

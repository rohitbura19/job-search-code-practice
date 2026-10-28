package practice.d20261028.c2;

public final class ShortestPaths {
    private ShortestPaths() {}

    /**
     * times[i] = {u, v, w}: a signal from u reaches v after w. Nodes are 1..n. Return the time until all nodes have
     * received a signal sent from k, or -1 if some node is unreachable. Weights are >= 0.
     */
    public static int networkDelay(int[][] times, int n, int k) { throw new UnsupportedOperationException("TODO"); }

    /**
     * flights[i] = {from, to, price}; cities are 0..n-1. Cheapest price from src to dst using at most k stops
     * (so at most k + 1 flights), or -1 if impossible. Prices are >= 0.
     */
    public static int cheapestWithStops(int n, int[][] flights, int src, int dst, int k) { throw new UnsupportedOperationException("TODO"); }
}

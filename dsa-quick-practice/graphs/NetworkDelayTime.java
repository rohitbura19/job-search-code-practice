package graphs;

// Network Delay Time  [Medium | Dijkstra]
// times[i] = {u, v, w}, nodes 1..n. Time until all nodes get a signal from k, or -1.
//
// [[2,1,1],[2,3,1],[3,4,1]], n=4, k=2 -> 2
// [[1,2,1]], n=2, k=2                 -> -1

public class NetworkDelayTime {

    static int networkDelayTime(int[][] times, int n, int k) {
        // TODO
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(networkDelayTime(new int[][]{{2, 1, 1}, {2, 3, 1}, {3, 4, 1}}, 4, 2)); // 2
        System.out.println(networkDelayTime(new int[][]{{1, 2, 1}}, 2, 2));                       // -1
    }
}

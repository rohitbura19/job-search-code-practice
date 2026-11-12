package graphs;

// Cheapest Flights Within K Stops  [Medium | Bellman-Ford limited to k+1 rounds]
// flights[i] = {from, to, price}. Cheapest src -> dst with at most k stops, or -1.
//
// n=4, [[0,1,100],[1,2,100],[2,0,100],[1,3,600],[2,3,200]], 0 -> 3, k=1 -> 700
// n=3, [[0,1,100],[1,2,100],[0,2,500]], 0 -> 2, k=1 -> 200, k=0 -> 500

public class CheapestFlightsKStops {

    static int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        // TODO
        return -1;
    }

    public static void main(String[] args) {
        int[][] f1 = {{0, 1, 100}, {1, 2, 100}, {2, 0, 100}, {1, 3, 600}, {2, 3, 200}};
        int[][] f2 = {{0, 1, 100}, {1, 2, 100}, {0, 2, 500}};
        System.out.println(findCheapestPrice(4, f1, 0, 3, 1)); // 700
        System.out.println(findCheapestPrice(3, f2, 0, 2, 1)); // 200
        System.out.println(findCheapestPrice(3, f2, 0, 2, 0)); // 500
    }
}

package graphs;

// Path With Minimum Effort  [Medium | Dijkstra on max-edge cost]
// Effort of a route = max |height difference| between consecutive cells. Minimise it, (0,0) -> bottom-right.
//
// [[1,2,2],[3,8,2],[5,3,5]] -> 2
// [[1,2,3],[3,8,4],[5,3,5]] -> 1

public class MinimumEffortPath {

    static int minimumEffortPath(int[][] heights) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(minimumEffortPath(new int[][]{{1, 2, 2}, {3, 8, 2}, {5, 3, 5}})); // 2
        System.out.println(minimumEffortPath(new int[][]{{1, 2, 3}, {3, 8, 4}, {5, 3, 5}})); // 1
    }
}

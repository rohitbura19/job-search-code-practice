package graphs;

// Number of Provinces  [Medium | Union-Find or DFS]
// isConnected[i][j] == 1 if cities i and j are directly connected. Count connected groups.
//
// [[1,1,0],[1,1,0],[0,0,1]] -> 2
// [[1,0,0],[0,1,0],[0,0,1]] -> 3

public class NumberOfProvinces {

    static int findCircleNum(int[][] isConnected) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(findCircleNum(new int[][]{{1, 1, 0}, {1, 1, 0}, {0, 0, 1}})); // 2
        System.out.println(findCircleNum(new int[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}})); // 3
    }
}

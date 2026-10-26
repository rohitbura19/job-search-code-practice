package graphs;

// Number of Islands  [Medium | Grid DFS/BFS]
// '1' land, '0' water. Count islands (4-directional).
//
// 11000
// 11000
// 00100
// 00011   -> 3

public class NumberOfIslands {

    static int numIslands(char[][] grid) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        char[][] grid = {
                "11000".toCharArray(),
                "11000".toCharArray(),
                "00100".toCharArray(),
                "00011".toCharArray()};
        System.out.println(numIslands(grid)); // 3
    }
}

package dp;

// Maximal Square  [Medium | dp = 1 + min(top, left, top-left)]
// Area of the largest square of '1's.
//
// ["10100","10111","11111","10010"] -> 4
// ["01","10"]                       -> 1

public class MaximalSquare {

    static int maximalSquare(char[][] matrix) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(maximalSquare(grid("10100", "10111", "11111", "10010"))); // 4
        System.out.println(maximalSquare(grid("01", "10")));                         // 1
    }

    static char[][] grid(String... rows) {
        char[][] g = new char[rows.length][];
        for (int i = 0; i < rows.length; i++) g[i] = rows[i].toCharArray();
        return g;
    }
}

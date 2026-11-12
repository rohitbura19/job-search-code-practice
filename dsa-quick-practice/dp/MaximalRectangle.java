package dp;

// Maximal Rectangle  [Hard | Histogram per row + monotonic stack]
// Area of the largest rectangle of '1's.
//
// ["10100","10111","11111","10010"] -> 6
// ["0"]                             -> 0

public class MaximalRectangle {

    static int maximalRectangle(char[][] matrix) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(maximalRectangle(grid("10100", "10111", "11111", "10010"))); // 6
        System.out.println(maximalRectangle(grid("0")));                                // 0
    }

    static char[][] grid(String... rows) {
        char[][] g = new char[rows.length][];
        for (int i = 0; i < rows.length; i++) g[i] = rows[i].toCharArray();
        return g;
    }
}

package practice.d20261104.d1;

/**
 * "Strictly increasing path" edges point from a smaller cell to a larger neighbour, so the graph is a DAG and the answer is its longest path (in cells).
 * Recursive DFS + memo is correct but a 200 x 200 snake is 40_000 deep and overflows the Java stack. Instead peel the DAG layer by layer (Kahn from the sinks):
 * outDegree[c] = number of strictly larger neighbours; cells with outDegree 0 are the first layer; removing them lowers the outDegree of their smaller neighbours.
 * The number of layers is the longest path. O(mn) time, O(mn) space, no recursion.
 */
public final class LongestIncreasingPath {
    private LongestIncreasingPath() {}

    private static final int[] DR = {1, -1, 0, 0}, DC = {0, 0, 1, -1};

    public static int longestIncreasingPath(int[][] m) {
        int rows = m.length, cols = m[0].length;
        int[] out = new int[rows * cols];
        int[] queue = new int[rows * cols];
        int head = 0, tail = 0;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                for (int d = 0; d < 4; d++) {
                    int nr = r + DR[d], nc = c + DC[d];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && m[nr][nc] > m[r][c]) out[r * cols + c]++;
                }
                if (out[r * cols + c] == 0) queue[tail++] = r * cols + c;
            }
        int layers = 0;
        while (head < tail) {
            layers++;
            for (int end = tail; head < end; ) {
                int cell = queue[head++], r = cell / cols, c = cell % cols;
                for (int d = 0; d < 4; d++) {
                    int nr = r + DR[d], nc = c + DC[d];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && m[nr][nc] < m[r][c] && --out[nr * cols + nc] == 0) queue[tail++] = nr * cols + nc;
                }
            }
        }
        return layers;
    }
}

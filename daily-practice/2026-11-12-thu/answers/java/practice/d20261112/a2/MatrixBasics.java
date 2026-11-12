package practice.d20261112.a2;

import java.util.ArrayList;
import java.util.List;

/** Matrix walks. spiralOrder O(mn) with four shrinking bounds; rotate = transpose + reverse each row, O(n^2), O(1) extra; isToeplitz compares each cell to its up-left neighbour, O(mn). */
public class MatrixBasics {
    public static List<Integer> spiralOrder(int[][] m) {
        List<Integer> out = new ArrayList<>();
        if (m.length == 0) return out;
        int top = 0, bottom = m.length - 1, left = 0, right = m[0].length - 1;
        while (top <= bottom && left <= right) {
            for (int c = left; c <= right; c++) out.add(m[top][c]);
            top++;
            for (int r = top; r <= bottom; r++) out.add(m[r][right]);
            right--;
            if (top <= bottom) { for (int c = right; c >= left; c--) out.add(m[bottom][c]); bottom--; }
            if (left <= right) { for (int r = bottom; r >= top; r--) out.add(m[r][left]); left++; }
        }
        return out;
    }

    public static void rotate(int[][] m) {
        int n = m.length;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) { int t = m[i][j]; m[i][j] = m[j][i]; m[j][i] = t; }
        for (int[] row : m)
            for (int i = 0, j = n - 1; i < j; i++, j--) { int t = row[i]; row[i] = row[j]; row[j] = t; }
    }

    public static boolean isToeplitz(int[][] m) {
        for (int r = 1; r < m.length; r++)
            for (int c = 1; c < m[0].length; c++)
                if (m[r][c] != m[r - 1][c - 1]) return false;
        return true;
    }
}

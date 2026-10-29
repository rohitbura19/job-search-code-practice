package practice.d20261029.c1;

import java.util.ArrayList;
import java.util.List;

/**
 * spiral: four shrinking borders (top, bottom, left, right); re-check after each side so a single row/column is not walked twice. O(mn).
 * rotate (clockwise, in place): transpose, then reverse each row; the swap pairs touch each cell once. O(n^2) / O(1).
 * setZeroes (in place, O(1) extra): use the first row and first column as the marker arrays, remembering separately whether they
 * themselves contained a zero; fill from the inside out, then the first row/column last. O(mn) / O(1).
 */
public final class Matrices {
    private Matrices() {}

    public static List<Integer> spiral(int[][] a) {
        List<Integer> out = new ArrayList<>();
        if (a.length == 0 || a[0].length == 0) return out;
        int top = 0, bottom = a.length - 1, left = 0, right = a[0].length - 1;
        while (top <= bottom && left <= right) {
            for (int j = left; j <= right; j++) out.add(a[top][j]);
            top++;
            for (int i = top; i <= bottom; i++) out.add(a[i][right]);
            right--;
            if (top <= bottom) { for (int j = right; j >= left; j--) out.add(a[bottom][j]); bottom--; }
            if (left <= right) { for (int i = bottom; i >= top; i--) out.add(a[i][left]); left++; }
        }
        return out;
    }

    public static void rotate(int[][] a) {
        int n = a.length;
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) { int t = a[i][j]; a[i][j] = a[j][i]; a[j][i] = t; }
        for (int[] row : a)
            for (int i = 0, j = n - 1; i < j; i++, j--) { int t = row[i]; row[i] = row[j]; row[j] = t; }
    }

    public static void setZeroes(int[][] a) {
        int m = a.length;
        if (m == 0) return;
        int n = a[0].length;
        boolean row0 = false, col0 = false;
        for (int j = 0; j < n; j++) if (a[0][j] == 0) row0 = true;
        for (int i = 0; i < m; i++) if (a[i][0] == 0) col0 = true;
        for (int i = 1; i < m; i++)
            for (int j = 1; j < n; j++)
                if (a[i][j] == 0) { a[i][0] = 0; a[0][j] = 0; }
        for (int i = 1; i < m; i++)
            for (int j = 1; j < n; j++)
                if (a[i][0] == 0 || a[0][j] == 0) a[i][j] = 0;
        if (row0) for (int j = 0; j < n; j++) a[0][j] = 0;
        if (col0) for (int i = 0; i < m; i++) a[i][0] = 0;
    }
}

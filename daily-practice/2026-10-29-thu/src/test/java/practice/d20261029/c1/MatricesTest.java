package practice.d20261029.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class MatricesTest {
    static int[][] m(int[]... rows) { return rows; }

    @Test void spiralExamples() {
        assertEquals(List.of(1, 2, 3, 6, 9, 8, 7, 4, 5), Matrices.spiral(m(new int[]{1, 2, 3}, new int[]{4, 5, 6}, new int[]{7, 8, 9})));
        assertEquals(List.of(1, 2, 3, 4, 8, 12, 11, 10, 9, 5, 6, 7), Matrices.spiral(m(new int[]{1, 2, 3, 4}, new int[]{5, 6, 7, 8}, new int[]{9, 10, 11, 12})));
        assertEquals(List.of(1, 2, 3), Matrices.spiral(m(new int[]{1, 2, 3})));
        assertEquals(List.of(1, 2, 3), Matrices.spiral(m(new int[]{1}, new int[]{2}, new int[]{3})));
        assertEquals(List.of(5), Matrices.spiral(m(new int[]{5})));
        assertEquals(List.of(), Matrices.spiral(new int[0][]));
        assertEquals(List.of(1, 2, 4, 3), Matrices.spiral(m(new int[]{1, 2}, new int[]{3, 4})));
        assertEquals(List.of(1, 2, 4, 6, 5, 3), Matrices.spiral(m(new int[]{1, 2}, new int[]{3, 4}, new int[]{5, 6})));
    }

    @Test void spiralRandomVisitsEveryCellOnce() {
        Random rnd = new Random(29);
        for (int round = 0; round < 500; round++) {
            int r = 1 + rnd.nextInt(7), c = 1 + rnd.nextInt(7);
            int[][] a = new int[r][c];
            for (int i = 0; i < r; i++) for (int j = 0; j < c; j++) a[i][j] = i * c + j;
            List<Integer> s = Matrices.spiral(a);
            assertEquals(r * c, s.size());
            assertEquals(r * c, new HashSet<>(s).size());
            for (int k = 1; k < s.size(); k++) {   // consecutive cells are neighbours
                int p = s.get(k - 1), q = s.get(k);
                assertEquals(1, Math.abs(p / c - q / c) + Math.abs(p % c - q % c), r + "x" + c);
            }
        }
    }

    @Test void rotateExamples() {
        int[][] a = m(new int[]{1, 2}, new int[]{3, 4});
        Matrices.rotate(a);
        assertArrayEquals(m(new int[]{3, 1}, new int[]{4, 2}), a);
        int[][] b = m(new int[]{1, 2, 3}, new int[]{4, 5, 6}, new int[]{7, 8, 9});
        Matrices.rotate(b);
        assertArrayEquals(m(new int[]{7, 4, 1}, new int[]{8, 5, 2}, new int[]{9, 6, 3}), b);
        int[][] one = m(new int[]{1});
        Matrices.rotate(one);
        assertArrayEquals(m(new int[]{1}), one);
        Matrices.rotate(new int[0][]);
    }

    @Test void rotateMatchesTheIndexFormulaAndFourTimesIsIdentity() {
        for (int n = 1; n <= 9; n++) {
            int[][] a = new int[n][n], orig = new int[n][n];
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) a[i][j] = orig[i][j] = i * 31 + j;
            Matrices.rotate(a);
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) assertEquals(orig[n - 1 - j][i], a[i][j]);
            for (int k = 0; k < 3; k++) Matrices.rotate(a);
            assertArrayEquals(orig, a);
        }
    }

    @Test void setZeroesExamples() {
        int[][] a = m(new int[]{1, 1, 1}, new int[]{1, 0, 1}, new int[]{1, 1, 1});
        Matrices.setZeroes(a);
        assertArrayEquals(m(new int[]{1, 0, 1}, new int[]{0, 0, 0}, new int[]{1, 0, 1}), a);
        int[][] b = m(new int[]{0, 1, 2, 0}, new int[]{3, 4, 5, 2}, new int[]{1, 3, 1, 5});
        Matrices.setZeroes(b);
        assertArrayEquals(m(new int[]{0, 0, 0, 0}, new int[]{0, 4, 5, 0}, new int[]{0, 3, 1, 0}), b);
        int[][] c = m(new int[]{1, 2, 3}, new int[]{4, 0, 6});             // zero in the middle of the last row
        Matrices.setZeroes(c);
        assertArrayEquals(m(new int[]{1, 0, 3}, new int[]{0, 0, 0}), c);
        int[][] d = m(new int[]{1, 0});                                      // zero in the first row only
        Matrices.setZeroes(d);
        assertArrayEquals(m(new int[]{0, 0}), d);
        int[][] e = m(new int[]{1}, new int[]{0}, new int[]{3});             // zero in the first column only
        Matrices.setZeroes(e);
        assertArrayEquals(m(new int[]{0}, new int[]{0}, new int[]{0}), e);
    }

    @Test void setZeroesRandomAgainstCopyBasedVersion() {
        Random rnd = new Random(1029);
        for (int round = 0; round < 2000; round++) {
            int r = 1 + rnd.nextInt(6), c = 1 + rnd.nextInt(6);
            int[][] a = new int[r][c], expected = new int[r][c];
            for (int i = 0; i < r; i++) for (int j = 0; j < c; j++) a[i][j] = expected[i][j] = rnd.nextInt(4) == 0 ? 0 : 1 + rnd.nextInt(9);
            boolean[] rowZero = new boolean[r], colZero = new boolean[c];
            for (int i = 0; i < r; i++) for (int j = 0; j < c; j++) if (a[i][j] == 0) { rowZero[i] = true; colZero[j] = true; }
            for (int i = 0; i < r; i++) for (int j = 0; j < c; j++) if (rowZero[i] || colZero[j]) expected[i][j] = 0;
            Matrices.setZeroes(a);
            assertArrayEquals(expected, a);
        }
    }

    @Test void largeMatrices() {
        int n = 3000;
        int[][] a = new int[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) a[i][j] = 1 + (i ^ j) % 7;
        a[n / 2][n / 3] = 0;
        assertTimeoutPreemptively(Duration.ofSeconds(4), () -> {
            assertEquals(n * n, Matrices.spiral(a).size());
            Matrices.rotate(a);
            Matrices.setZeroes(a);
        });
        int zeros = 0;
        for (int[] row : a) for (int v : row) if (v == 0) zeros++;
        assertEquals(2 * n - 1, zeros);
    }
}

package practice.d20261112.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;

class MatrixBasicsTest {
    @Test void spiralExamples() {
        assertEquals(List.of(1, 2, 3, 6, 9, 8, 7, 4, 5), MatrixBasics.spiralOrder(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}));
        assertEquals(List.of(1, 2, 3, 4, 8, 12, 11, 10, 9, 5, 6, 7), MatrixBasics.spiralOrder(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}}));
    }
    @Test void spiralEdges() {
        assertEquals(List.of(), MatrixBasics.spiralOrder(new int[0][0]));
        assertEquals(List.of(7), MatrixBasics.spiralOrder(new int[][]{{7}}));
        assertEquals(List.of(1, 2, 3), MatrixBasics.spiralOrder(new int[][]{{1, 2, 3}}));
        assertEquals(List.of(1, 2, 3), MatrixBasics.spiralOrder(new int[][]{{1}, {2}, {3}}));
        assertEquals(List.of(1, 2, 4, 3), MatrixBasics.spiralOrder(new int[][]{{1, 2}, {3, 4}}));
    }
    @Test void spiralLargeVisitsEveryCellOnce() {
        int[][] m = new int[700][900];
        int k = 0;
        for (int[] row : m) for (int c = 0; c < row.length; c++) row[c] = k++;
        List<Integer> out = assertTimeoutPreemptively(Duration.ofSeconds(2), () -> MatrixBasics.spiralOrder(m));
        assertEquals(700 * 900, out.size());
        assertEquals(700 * 900, out.stream().distinct().count());
    }
    @Test void rotateExamples() {
        int[][] m = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        MatrixBasics.rotate(m);
        assertArrayEquals(new int[][]{{7, 4, 1}, {8, 5, 2}, {9, 6, 3}}, m);
        int[][] e = {{5, 1, 9, 11}, {2, 4, 8, 10}, {13, 3, 6, 7}, {15, 14, 12, 16}};
        MatrixBasics.rotate(e);
        assertArrayEquals(new int[][]{{15, 13, 2, 5}, {14, 3, 4, 1}, {12, 6, 8, 9}, {16, 7, 10, 11}}, e);
    }
    @Test void rotateFourTimesIsIdentity() {
        int[][] m = {{1, 2}, {3, 4}};
        for (int i = 0; i < 4; i++) MatrixBasics.rotate(m);
        assertArrayEquals(new int[][]{{1, 2}, {3, 4}}, m);
        int[][] one = {{9}};
        MatrixBasics.rotate(one);
        assertArrayEquals(new int[][]{{9}}, one);
    }
    @Test void toeplitz() {
        assertTrue(MatrixBasics.isToeplitz(new int[][]{{1, 2, 3, 4}, {5, 1, 2, 3}, {9, 5, 1, 2}}));
        assertFalse(MatrixBasics.isToeplitz(new int[][]{{1, 2}, {2, 2}}));
        assertTrue(MatrixBasics.isToeplitz(new int[][]{{1, 2, 3}}));
        assertTrue(MatrixBasics.isToeplitz(new int[][]{{1}, {2}}));
    }
}

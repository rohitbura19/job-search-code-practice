package practice.d20261101.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class GridAndDigitsTest {
    private static char[][] board(String... rows) {
        char[][] b = new char[9][];
        for (int i = 0; i < 9; i++) b[i] = rows[i].toCharArray();
        return b;
    }

    private static final String[] OK = {"53..7....", "6..195...", ".98....6.", "8...6...3", "4..8.3..1", "7...2...6", ".6....28.", "...419..5", "....8..79"};

    @Test void sudokuValid() { assertTrue(GridAndDigits.isValidSudoku(board(OK))); }

    @Test void sudokuEmptyIsValid() {
        String[] rows = new String[9];
        Arrays.fill(rows, ".........");
        assertTrue(GridAndDigits.isValidSudoku(board(rows)));
    }

    @Test void sudokuRowColumnBoxDuplicates() {
        String[] rowDup = OK.clone(); rowDup[0] = "53..7...5";           // 5 twice in row 0
        assertFalse(GridAndDigits.isValidSudoku(board(rowDup)));
        String[] colDup = OK.clone(); colDup[0] = "83..7....";           // 8 twice in column 0 (and in the box)
        assertFalse(GridAndDigits.isValidSudoku(board(colDup)));
        String[] boxDup = OK.clone(); boxDup[1] = "6..195..."; boxDup[2] = "598....6.";  // 5 repeats in column 0 and box 0
        assertFalse(GridAndDigits.isValidSudoku(board(boxDup)));
    }

    @Test void sudokuDoesNotMutate() {
        char[][] b = board(OK);
        GridAndDigits.isValidSudoku(b);
        assertArrayEquals(OK[0].toCharArray(), b[0]);
    }

    @Test void plusOneExamples() {
        assertArrayEquals(new int[] {1, 2, 4}, GridAndDigits.plusOne(new int[] {1, 2, 3}));
        assertArrayEquals(new int[] {1, 0}, GridAndDigits.plusOne(new int[] {9}));
        assertArrayEquals(new int[] {1}, GridAndDigits.plusOne(new int[] {0}));
        assertArrayEquals(new int[] {1, 0, 0, 0}, GridAndDigits.plusOne(new int[] {9, 9, 9}));
        assertArrayEquals(new int[] {1, 0, 0}, GridAndDigits.plusOne(new int[] {9, 9}));
    }

    @Test void plusOneDoesNotMutateInput() {
        int[] in = {1, 9};
        assertArrayEquals(new int[] {2, 0}, GridAndDigits.plusOne(in));
        assertArrayEquals(new int[] {1, 9}, in);
    }

    @Test void plusOneLarge() {
        int[] nines = new int[200_000];
        Arrays.fill(nines, 9);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            int[] r = GridAndDigits.plusOne(nines);
            assertEquals(nines.length + 1, r.length);
            assertEquals(1, r[0]);
            assertEquals(0, r[r.length - 1]);
        });
    }

    @Test void commonPrefix() {
        assertEquals("fl", GridAndDigits.longestCommonPrefix(new String[] {"flower", "flow", "flight"}));
        assertEquals("", GridAndDigits.longestCommonPrefix(new String[] {"dog", "racecar", "car"}));
        assertEquals("abc", GridAndDigits.longestCommonPrefix(new String[] {"abc"}));
        assertEquals("", GridAndDigits.longestCommonPrefix(new String[0]));
        assertEquals("", GridAndDigits.longestCommonPrefix(new String[] {"abc", "", "abd"}));
        assertEquals("aa", GridAndDigits.longestCommonPrefix(new String[] {"aa", "aa", "aaa"}));
    }
}

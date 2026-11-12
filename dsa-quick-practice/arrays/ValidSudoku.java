package arrays;

// Valid Sudoku  [Medium | Sets per row, column, box]
// 9x9, '.' empty. Valid if no digit repeats in a row, column or 3x3 box (need not be solvable).
//
// (board below) -> true; change board[0][0] to '8' -> false

public class ValidSudoku {

    static boolean isValidSudoku(char[][] board) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        String[] rows = {
                "53..7....", "6..195...", ".98....6.",
                "8...6...3", "4..8.3..1", "7...2...6",
                ".6....28.", "...419..5", "....8..79"};
        char[][] board = new char[9][];
        for (int i = 0; i < 9; i++) board[i] = rows[i].toCharArray();
        System.out.println(isValidSudoku(board)); // true
        board[0][0] = '8';
        System.out.println(isValidSudoku(board)); // false
    }
}

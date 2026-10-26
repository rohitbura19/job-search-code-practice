package backtracking;

// Word Search  [Medium | Backtracking on a grid]
// Can word be spelled by adjacent cells (up/down/left/right), each cell used once?
//
// ABCE
// SFCS     "ABCCED" -> true,  "SEE" -> true,  "ABCB" -> false
// ADEE

public class WordSearch {

    static boolean exist(char[][] board, String word) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        char[][] board = {
                "ABCE".toCharArray(),
                "SFCS".toCharArray(),
                "ADEE".toCharArray()};
        System.out.println(exist(board, "ABCCED")); // true
        System.out.println(exist(board, "SEE"));    // true
        System.out.println(exist(board, "ABCB"));   // false
    }
}

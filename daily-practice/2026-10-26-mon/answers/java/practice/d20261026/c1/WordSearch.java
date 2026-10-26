package practice.d20261026.c1;

/**
 * Backtracking DFS: from every cell matching word[0], mark the cell visited (in place, restore on the way back), try 4 neighbours
 * for word[k+1]. Worst case O(R*C*3^L) (3 choices after the first step). Pruning that matters:
 * 1. letter-count check: if the board has fewer of some letter than the word needs, answer false immediately (the test with a
 *    missing 'b' is exponential without it);
 * 2. for exist(), if the word's last letter is rarer than its first, search the reversed word (fewer starting points / dead ends).
 * countPaths has no early exit, so only the in-place visited marker and the count pre-check apply.
 */
public final class WordSearch {
    private WordSearch() {}

    public static boolean exist(char[][] b, String word) {
        if (!enough(b, word)) return false;
        char[] w = word.toCharArray();
        if (count(b, w[w.length - 1]) < count(b, w[0])) reverse(w);
        for (int r = 0; r < b.length; r++)
            for (int c = 0; c < b[0].length; c++)
                if (dfs(b, w, r, c, 0)) return true;
        return false;
    }

    public static long countPaths(char[][] b, String word) {
        if (!enough(b, word)) return 0;
        char[] w = word.toCharArray();
        long total = 0;
        for (int r = 0; r < b.length; r++)
            for (int c = 0; c < b[0].length; c++) total += paths(b, w, r, c, 0);
        return total;
    }

    private static final int[] DR = {1, -1, 0, 0}, DC = {0, 0, 1, -1};

    private static boolean dfs(char[][] b, char[] w, int r, int c, int k) {
        if (b[r][c] != w[k]) return false;
        if (k == w.length - 1) return true;
        char saved = b[r][c];
        b[r][c] = '\0';
        try {
            for (int d = 0; d < 4; d++) {
                int nr = r + DR[d], nc = c + DC[d];
                if (nr >= 0 && nc >= 0 && nr < b.length && nc < b[0].length && dfs(b, w, nr, nc, k + 1)) return true;
            }
            return false;
        } finally {
            b[r][c] = saved;
        }
    }

    private static long paths(char[][] b, char[] w, int r, int c, int k) {
        if (b[r][c] != w[k]) return 0;
        if (k == w.length - 1) return 1;
        char saved = b[r][c];
        b[r][c] = '\0';
        long n = 0;
        for (int d = 0; d < 4; d++) {
            int nr = r + DR[d], nc = c + DC[d];
            if (nr >= 0 && nc >= 0 && nr < b.length && nc < b[0].length) n += paths(b, w, nr, nc, k + 1);
        }
        b[r][c] = saved;
        return n;
    }

    private static boolean enough(char[][] b, String word) {
        int[] need = new int[65536];
        for (char ch : word.toCharArray()) need[ch]++;
        for (char[] row : b) for (char ch : row) need[ch]--;
        for (char ch : word.toCharArray()) if (need[ch] > 0) return false;
        return true;
    }

    private static int count(char[][] b, char ch) {
        int n = 0;
        for (char[] row : b) for (char x : row) if (x == ch) n++;
        return n;
    }

    private static void reverse(char[] w) {
        for (int i = 0, j = w.length - 1; i < j; i++, j--) { char t = w[i]; w[i] = w[j]; w[j] = t; }
    }
}

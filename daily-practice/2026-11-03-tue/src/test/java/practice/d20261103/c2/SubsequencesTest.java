package practice.d20261103.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class SubsequencesTest {
    @Test void lisExamples() {
        assertEquals(0, Subsequences.lis(new int[0]));
        assertEquals(1, Subsequences.lis(new int[]{7}));
        assertEquals(4, Subsequences.lis(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));
        assertEquals(1, Subsequences.lis(new int[]{5, 5, 5, 5}));        // strict
        assertEquals(4, Subsequences.lis(new int[]{0, 1, 0, 3, 2, 3}));
        assertEquals(2, Subsequences.lis(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}));
        assertEquals(1, Subsequences.lis(new int[]{3, 2, 1}));
    }

    static int lisBrute(int[] a) {
        int[] dp = new int[a.length];
        int best = 0;
        for (int i = 0; i < a.length; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) if (a[j] < a[i]) dp[i] = Math.max(dp[i], dp[j] + 1);
            best = Math.max(best, dp[i]);
        }
        return best;
    }

    @Test void lisRandom() {
        Random rnd = new Random(1);
        for (int t = 0; t < 400; t++) {
            int[] a = new int[rnd.nextInt(30)];
            for (int i = 0; i < a.length; i++) a[i] = rnd.nextInt(12) - 6;
            assertEquals(lisBrute(a), Subsequences.lis(a));
        }
    }

    @Test void lisLarge() {
        int[] a = new int[200_000];
        for (int i = 0; i < a.length; i++) a[i] = i;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(200_000, Subsequences.lis(a)));
    }

    @Test void lcsExamples() {
        assertEquals(3, Subsequences.lcs("abcde", "ace"));
        assertEquals(0, Subsequences.lcs("abc", "def"));
        assertEquals(0, Subsequences.lcs("", "abc"));
        assertEquals(0, Subsequences.lcs("", ""));
        assertEquals(3, Subsequences.lcs("abc", "abc"));
        assertEquals(2, Subsequences.lcs("aab", "ab"));
        assertEquals(4, Subsequences.lcs("AGGTAB", "GXTXAYB"));
    }

    static int lcsBrute(String a, String b) {
        if (a.isEmpty() || b.isEmpty()) return 0;
        if (a.charAt(0) == b.charAt(0)) return 1 + lcsBrute(a.substring(1), b.substring(1));
        return Math.max(lcsBrute(a.substring(1), b), lcsBrute(a, b.substring(1)));
    }

    static String rs(Random rnd, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append((char) ('a' + rnd.nextInt(3)));
        return sb.toString();
    }

    @Test void lcsRandomBothOrders() {
        Random rnd = new Random(2);
        for (int t = 0; t < 300; t++) {
            String a = rs(rnd, rnd.nextInt(9)), b = rs(rnd, rnd.nextInt(9));
            assertEquals(lcsBrute(a, b), Subsequences.lcs(a, b));
        }
    }

    @Test void lcsLargeWithinTimeout() {
        String a = "ab".repeat(1500), b = "ba".repeat(1500);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(2999, Subsequences.lcs(a, b)));
    }

    @Test void robExamples() {
        assertEquals(0, Subsequences.robCircular(new int[0]));
        assertEquals(5, Subsequences.robCircular(new int[]{5}));
        assertEquals(3, Subsequences.robCircular(new int[]{2, 3, 2}));
        assertEquals(4, Subsequences.robCircular(new int[]{1, 2, 3, 1}));
        assertEquals(3, Subsequences.robCircular(new int[]{1, 2, 3}));
        assertEquals(5, Subsequences.robCircular(new int[]{5, 1}));
        assertEquals(0, Subsequences.robCircular(new int[]{0, 0, 0}));
    }

    static int robBrute(int[] a) {
        int n = a.length, best = 0;
        for (int mask = 0; mask < (1 << n); mask++) {
            boolean ok = true;
            int sum = 0;
            for (int i = 0; i < n && ok; i++) {
                if ((mask >> i & 1) == 0) continue;
                sum += a[i];
                if (n > 1 && (mask >> ((i + 1) % n) & 1) == 1) ok = false;
            }
            if (ok) best = Math.max(best, sum);
        }
        return best;
    }

    @Test void robRandom() {
        Random rnd = new Random(4);
        for (int t = 0; t < 400; t++) {
            int[] a = new int[rnd.nextInt(12)];
            for (int i = 0; i < a.length; i++) a[i] = rnd.nextInt(10);
            assertEquals(robBrute(a), Subsequences.robCircular(a));
        }
    }
}

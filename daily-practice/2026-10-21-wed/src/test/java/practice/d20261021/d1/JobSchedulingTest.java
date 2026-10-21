package practice.d20261021.d1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class JobSchedulingTest {

    @Test @DisplayName("examples from the question")
    void examples() {
        assertEquals(120, JobScheduling.maxProfit(new int[]{1, 2, 3, 3}, new int[]{3, 4, 5, 6}, new int[]{50, 10, 40, 70}));
        assertEquals(150, JobScheduling.maxProfit(new int[]{1, 2, 3, 4, 6}, new int[]{3, 5, 10, 6, 9}, new int[]{20, 20, 100, 70, 60}));
        assertEquals(6, JobScheduling.maxProfit(new int[]{1, 1, 1}, new int[]{2, 3, 4}, new int[]{5, 6, 4}));
    }

    @Test @DisplayName("no jobs and one job")
    void tiny() {
        assertEquals(0, JobScheduling.maxProfit(new int[]{}, new int[]{}, new int[]{}));
        assertEquals(7, JobScheduling.maxProfit(new int[]{1}, new int[]{2}, new int[]{7}));
    }

    @Test @DisplayName("touching intervals are compatible (end == next start)")
    void touching() {
        assertEquals(20, JobScheduling.maxProfit(new int[]{1, 3}, new int[]{3, 5}, new int[]{10, 10}));
        assertEquals(9, JobScheduling.maxProfit(new int[]{1, 2}, new int[]{2, 3}, new int[]{4, 5}));
    }

    @Test @DisplayName("greedy trap: earliest-end-first would pick the two small jobs")
    void greedyTrap() {
        assertEquals(100, JobScheduling.maxProfit(new int[]{0, 0, 3}, new int[]{10, 2, 5}, new int[]{100, 1, 1}));
    }

    @Test @DisplayName("input not sorted in any order")
    void unsortedInput() {
        assertEquals(120, JobScheduling.maxProfit(new int[]{3, 1, 3, 2}, new int[]{6, 3, 5, 4}, new int[]{70, 50, 40, 10}));
    }

    @Test @DisplayName("huge times and a total above Integer.MAX_VALUE")
    void bigValues() {
        int big = 2_000_000_000;
        assertEquals(4_000_000_000L, JobScheduling.maxProfit(new int[]{0, 1_000_000_000}, new int[]{1_000_000_000, big}, new int[]{big, big}));
    }

    @Test @DisplayName("random cases match a 2^n brute force")
    void randomAgainstBrute() {
        Random r = new Random(11);
        for (int t = 0; t < 3000; t++) {
            int n = r.nextInt(9); int[] s = new int[n], e = new int[n], p = new int[n];
            for (int i = 0; i < n; i++) { s[i] = r.nextInt(12); e[i] = s[i] + 1 + r.nextInt(6); p[i] = r.nextInt(20) + 1; }
            assertEquals(brute(s, e, p), JobScheduling.maxProfit(s, e, p));
        }
    }

    @Test @DisplayName("50,000 jobs in well under 2 seconds (O(n log n))")
    void performance() {
        Random r = new Random(5); int n = 50_000; int[] s = new int[n], e = new int[n], p = new int[n];
        for (int i = 0; i < n; i++) { s[i] = r.nextInt(1_000_000_000); e[i] = s[i] + 1 + r.nextInt(1000); p[i] = r.nextInt(10_000) + 1; }
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertTrue(JobScheduling.maxProfit(s, e, p) > 0));
    }

    static long brute(int[] s, int[] e, int[] p) {
        int n = s.length; long best = 0;
        for (int mask = 0; mask < (1 << n); mask++) {
            boolean ok = true; long sum = 0;
            for (int i = 0; i < n && ok; i++) if ((mask >> i & 1) == 1) {
                sum += p[i];
                for (int j = i + 1; j < n; j++) if ((mask >> j & 1) == 1 && s[i] < e[j] && s[j] < e[i]) { ok = false; break; }
            }
            if (ok) best = Math.max(best, sum);
        }
        return best;
    }
}

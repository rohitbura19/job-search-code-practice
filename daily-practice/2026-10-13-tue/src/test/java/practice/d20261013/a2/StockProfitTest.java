package practice.d20261013.a2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class StockProfitTest {

    @Test @DisplayName("sheet examples")
    void examples() {
        assertEquals(5, StockProfit.maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
        assertEquals(0, StockProfit.maxProfit(new int[]{7, 6, 4, 3, 1}));
    }

    @Test @DisplayName("empty, single day, equal prices -> 0")
    void edges() {
        assertEquals(0, StockProfit.maxProfit(new int[]{}));
        assertEquals(0, StockProfit.maxProfit(new int[]{5}));
        assertEquals(0, StockProfit.maxProfit(new int[]{3, 3, 3}));
    }

    @Test @DisplayName("the low point comes AFTER the high point: you cannot sell before you buy")
    void order() { assertEquals(4, StockProfit.maxProfit(new int[]{9, 1, 2, 5, 3})); }

    static int brute(int[] p) {
        int best = 0;
        for (int i = 0; i < p.length; i++) for (int j = i + 1; j < p.length; j++) best = Math.max(best, p[j] - p[i]);
        return best;
    }

    @Test @DisplayName("random arrays agree with the O(n^2) brute force")
    void random() {
        Random rnd = new Random(13);
        for (int t = 0; t < 500; t++) {
            int[] p = new int[rnd.nextInt(30)];
            for (int i = 0; i < p.length; i++) p[i] = rnd.nextInt(100);
            assertEquals(brute(p), StockProfit.maxProfit(p), java.util.Arrays.toString(p));
        }
    }

    @Test @DisplayName("10 million prices in one pass")
    void large() {
        int[] p = new int[10_000_000];
        Random rnd = new Random(1);
        for (int i = 0; i < p.length; i++) p[i] = rnd.nextInt(1_000_000);
        int expected = brute10(p);
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> assertEquals(expected, StockProfit.maxProfit(p)));
    }
    static int brute10(int[] p) { int min = Integer.MAX_VALUE, best = 0; for (int x : p) { min = Math.min(min, x); best = Math.max(best, x - min); } return best; }

    // ---------- follow-ups ----------

    @Test @DisplayName("unlimited transactions: sum of positive deltas")
    void unlimited() {
        assertEquals(7, StockProfit.maxProfitUnlimited(new int[]{7, 1, 5, 3, 6, 4}));
        assertEquals(4, StockProfit.maxProfitUnlimited(new int[]{1, 2, 3, 4, 5}));
        assertEquals(0, StockProfit.maxProfitUnlimited(new int[]{7, 6, 4, 3, 1}));
        assertEquals(0, StockProfit.maxProfitUnlimited(new int[]{}));
    }

    @Test @DisplayName("cooldown: sheet-style examples")
    void cooldown() {
        assertEquals(3, StockProfit.maxProfitCooldown(new int[]{1, 2, 3, 0, 2}));
        assertEquals(0, StockProfit.maxProfitCooldown(new int[]{1}));
        assertEquals(0, StockProfit.maxProfitCooldown(new int[]{}));
        assertEquals(1, StockProfit.maxProfitCooldown(new int[]{1, 2}));
    }

    static int cd(int[] p, int i, boolean holding) {   // exponential oracle
        if (i >= p.length) return 0;
        int skip = cd(p, i + 1, holding);
        if (holding) return Math.max(skip, p[i] + cd(p, i + 2, false));
        return Math.max(skip, -p[i] + cd(p, i + 1, true));
    }

    @Test @DisplayName("cooldown: random small arrays agree with an exhaustive oracle")
    void cooldownRandom() {
        Random rnd = new Random(7);
        for (int t = 0; t < 300; t++) {
            int[] p = new int[rnd.nextInt(10)];
            for (int i = 0; i < p.length; i++) p[i] = rnd.nextInt(10);
            assertEquals(cd(p, 0, false), StockProfit.maxProfitCooldown(p), java.util.Arrays.toString(p));
        }
    }
}

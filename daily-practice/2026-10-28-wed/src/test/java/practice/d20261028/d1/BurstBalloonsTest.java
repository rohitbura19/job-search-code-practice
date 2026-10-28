package practice.d20261028.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class BurstBalloonsTest {
    @Test void examples() {
        assertEquals(167, BurstBalloons.maxCoins(new int[] {3, 1, 5, 8}));
        assertEquals(10, BurstBalloons.maxCoins(new int[] {1, 5}));
        assertEquals(5, BurstBalloons.maxCoins(new int[] {5}));
        assertEquals(0, BurstBalloons.maxCoins(new int[] {}));
    }

    @Test void zerosAndDuplicates() {
        assertEquals(0, BurstBalloons.maxCoins(new int[] {0, 0, 0}));
        assertEquals(1, BurstBalloons.maxCoins(new int[] {1}));
        assertEquals(brute(new ArrayList<>(List.of(7, 0, 3))), BurstBalloons.maxCoins(new int[] {7, 0, 3}));
        assertEquals(brute(new ArrayList<>(List.of(2, 2, 2, 2))), BurstBalloons.maxCoins(new int[] {2, 2, 2, 2}));
    }

    static int brute(List<Integer> a) {
        if (a.isEmpty()) return 0;
        int best = 0;
        for (int i = 0; i < a.size(); i++) {
            int gain = (i == 0 ? 1 : a.get(i - 1)) * a.get(i) * (i == a.size() - 1 ? 1 : a.get(i + 1));
            List<Integer> rest = new ArrayList<>(a);
            rest.remove(i);
            best = Math.max(best, gain + brute(rest));
        }
        return best;
    }

    @Test void randomAgainstBruteForce() {
        Random r = new Random(28);
        for (int iter = 0; iter < 200; iter++) {
            int n = r.nextInt(8);
            int[] a = new int[n];
            List<Integer> l = new ArrayList<>();
            for (int i = 0; i < n; i++) { a[i] = r.nextInt(10); l.add(a[i]); }
            assertEquals(brute(l), BurstBalloons.maxCoins(a), Arrays.toString(a));
        }
    }

    @Test void maxSizeIsFastAndDoesNotOverflow() {
        int[] a = new int[300];
        Arrays.fill(a, 100);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertTrue(BurstBalloons.maxCoins(a) > 290_000_000));
    }
}

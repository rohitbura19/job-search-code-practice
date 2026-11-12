package practice.d20261112.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class BurstBalloonsTest {
    static int brute(List<Integer> a) {
        int best = 0;
        for (int i = 0; i < a.size(); i++) {
            int gain = (i == 0 ? 1 : a.get(i - 1)) * a.get(i) * (i == a.size() - 1 ? 1 : a.get(i + 1));
            List<Integer> rest = new ArrayList<>(a);
            rest.remove(i);
            best = Math.max(best, gain + brute(rest));
        }
        return best;
    }
    @Test void examples() {
        assertEquals(167, BurstBalloons.maxCoins(new int[]{3, 1, 5, 8}));
        assertEquals(10, BurstBalloons.maxCoins(new int[]{1, 5}));
    }
    @Test void edges() {
        assertEquals(0, BurstBalloons.maxCoins(new int[]{}));
        assertEquals(7, BurstBalloons.maxCoins(new int[]{7}));
        assertEquals(0, BurstBalloons.maxCoins(new int[]{0, 0, 0}));
        assertEquals(16, BurstBalloons.maxCoins(new int[]{3, 4}));
        assertEquals(3, BurstBalloons.maxCoins(new int[]{1, 1, 1}));
    }
    @Test void matchesBruteForce() {
        Random r = new Random(11);
        for (int t = 0; t < 150; t++) {
            int n = r.nextInt(8);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = r.nextInt(10);
            assertEquals(brute(Arrays.stream(a).boxed().toList()), BurstBalloons.maxCoins(a), Arrays.toString(a));
        }
    }
    @Test void maxSizeIsFastAndFitsInt() {
        int[] a = new int[300];
        Arrays.fill(a, 100);
        int ans = assertTimeoutPreemptively(Duration.ofSeconds(2), () -> BurstBalloons.maxCoins(a));
        assertEquals(298_010_100, ans);
    }
}

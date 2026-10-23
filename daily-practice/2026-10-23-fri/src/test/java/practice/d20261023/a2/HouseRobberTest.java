package practice.d20261023.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class HouseRobberTest {
    @Test void examples() {
        assertEquals(4, HouseRobber.rob(new int[]{1, 2, 3, 1}));
        assertEquals(12, HouseRobber.rob(new int[]{2, 7, 9, 3, 1}));
        assertEquals(0, HouseRobber.rob(new int[]{}));
        assertEquals(5, HouseRobber.rob(new int[]{5}));
        assertEquals(5, HouseRobber.rob(new int[]{5, 5}));
        assertEquals(0, HouseRobber.rob(new int[]{0, 0, 0}));
    }

    @Test void circularExamples() {
        assertEquals(3, HouseRobber.robCircular(new int[]{2, 3, 2}));
        assertEquals(4, HouseRobber.robCircular(new int[]{1, 2, 3, 1}));
        assertEquals(3, HouseRobber.robCircular(new int[]{1, 2, 3}));
        assertEquals(0, HouseRobber.robCircular(new int[]{}));
        assertEquals(9, HouseRobber.robCircular(new int[]{9}));
        assertEquals(9, HouseRobber.robCircular(new int[]{9, 4}));
    }

    @Test void answerNeedsLong() {
        int[] a = new int[10];
        Arrays.fill(a, 1_000_000_000);
        assertEquals(5_000_000_000L, HouseRobber.rob(a));
        assertEquals(3_000_000_000L, HouseRobber.robCircular(new int[]{1_000_000_000, 0, 1_000_000_000, 0, 1_000_000_000, 1_000_000_000}));
    }

    static long brute(int[] a, int i, boolean prevTaken, boolean firstTaken, boolean circular) {
        if (i == a.length) return 0;
        long best = brute(a, i + 1, false, firstTaken, circular);
        boolean lastBlocked = circular && i == a.length - 1 && firstTaken && a.length > 1;
        if (!prevTaken && !lastBlocked) best = Math.max(best, a[i] + brute(a, i + 1, true, i == 0 || firstTaken, circular));
        return best;
    }

    @Test void randomAgainstBruteForce() {
        Random rnd = new Random(11);
        for (int t = 0; t < 1500; t++) {
            int[] a = new int[rnd.nextInt(13)];
            for (int i = 0; i < a.length; i++) a[i] = rnd.nextInt(30);
            assertEquals(brute(a, 0, false, false, false), HouseRobber.rob(a), Arrays.toString(a));
            assertEquals(brute(a, 0, false, false, true), HouseRobber.robCircular(a), Arrays.toString(a));
        }
    }

    @Test void largeInput() {
        int[] a = new int[1_000_000];
        Arrays.fill(a, 1_000_000_000);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertEquals(500_000L * 1_000_000_000L, HouseRobber.rob(a));
            assertEquals(500_000L * 1_000_000_000L, HouseRobber.robCircular(a));
        });
    }
}

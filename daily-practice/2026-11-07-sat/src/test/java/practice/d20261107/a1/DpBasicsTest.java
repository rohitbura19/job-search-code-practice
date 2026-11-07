package practice.d20261107.a1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class DpBasicsTest {
    @Test void climbExamples() {
        assertEquals(1, DpBasics.climbStairs(0));
        assertEquals(1, DpBasics.climbStairs(1));
        assertEquals(2, DpBasics.climbStairs(2));
        assertEquals(3, DpBasics.climbStairs(3));
        assertEquals(8, DpBasics.climbStairs(5));
        assertEquals(4660046610375530309L, DpBasics.climbStairs(90)); // exceeds int, fits long
    }

    @Test void minCostExamples() {
        assertEquals(15, DpBasics.minCostClimbingStairs(new int[]{10, 15, 20}));
        assertEquals(6, DpBasics.minCostClimbingStairs(new int[]{1, 100, 1, 1, 1, 100, 1, 1, 100, 1}));
        assertEquals(0, DpBasics.minCostClimbingStairs(new int[]{5, 5}) - 5);
        assertEquals(0, DpBasics.minCostClimbingStairs(new int[]{0, 0, 0, 0}));
    }

    @Test void minCostVsBrute() {
        Random r = new Random(1);
        for (int t = 0; t < 2000; t++) {
            int[] c = r.ints(2 + r.nextInt(10), 0, 50).toArray();
            assertEquals(brute(c), DpBasics.minCostClimbingStairs(c));
        }
    }

    @Test void minCostLarge() {
        int[] c = new Random(2).ints(5_000_000, 0, 1000).toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> DpBasics.minCostClimbingStairs(c));
    }

    @Test void tribonacci() {
        assertEquals(0, DpBasics.tribonacci(0));
        assertEquals(1, DpBasics.tribonacci(1));
        assertEquals(1, DpBasics.tribonacci(2));
        assertEquals(2, DpBasics.tribonacci(3));
        assertEquals(4, DpBasics.tribonacci(4));
        assertEquals(1389537, DpBasics.tribonacci(25));
        assertEquals(2082876103, DpBasics.tribonacci(37));
    }

    /** min over starting on step 0 or 1. */
    private static int brute(int[] c) {
        return Math.min(go(c, 0), go(c, 1));
    }

    private static int go(int[] c, int i) {
        if (i >= c.length) return 0;
        return c[i] + Math.min(go(c, i + 1), go(c, i + 2));
    }
}

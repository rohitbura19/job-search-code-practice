package practice.d20261107.a1;

/** O(n) time, O(1) space for all three: keep only the last 2-3 states of the recurrence. */
public final class DpBasics {
    private DpBasics() {}

    /** Distinct ways to climb n steps taking 1 or 2 at a time (n in [0, 90], fits in long). ways(0)=1. */
    public static long climbStairs(int n) {
        long a = 1, b = 1; // ways(0), ways(1)
        for (int i = 2; i <= n; i++) { long c = a + b; a = b; b = c; }
        return n == 0 ? 1 : b;
    }

    /** Pay cost[i] to leave step i, then move 1 or 2 steps. Start at step 0 or 1, reach index cost.length (the top). */
    public static int minCostClimbingStairs(int[] cost) {
        int a = 0, b = 0; // min cost to stand on step i-2, i-1
        for (int i = 2; i <= cost.length; i++) {
            int c = Math.min(b + cost[i - 1], a + cost[i - 2]);
            a = b; b = c;
        }
        return b;
    }

    /** T0=0, T1=1, T2=1, Tn = Tn-1 + Tn-2 + Tn-3; n in [0, 37] fits in int. */
    public static int tribonacci(int n) {
        if (n == 0) return 0;
        int a = 0, b = 1, c = 1;
        for (int i = 3; i <= n; i++) { int d = a + b + c; a = b; b = c; c = d; }
        return c;
    }
}

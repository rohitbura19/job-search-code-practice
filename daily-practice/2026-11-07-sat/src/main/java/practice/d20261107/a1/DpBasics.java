package practice.d20261107.a1;

public final class DpBasics {
    private DpBasics() {}

    /** Distinct ways to climb n steps taking 1 or 2 at a time (n in [0, 90], fits in long). ways(0)=1. */
    public static long climbStairs(int n) { throw new UnsupportedOperationException("TODO"); }

    /** Pay cost[i] to leave step i, then move 1 or 2 steps. Start at step 0 or 1, reach index cost.length (the top). O(1) space. */
    public static int minCostClimbingStairs(int[] cost) { throw new UnsupportedOperationException("TODO"); }

    /** T0=0, T1=1, T2=1, Tn = Tn-1 + Tn-2 + Tn-3; n in [0, 37] fits in int. */
    public static int tribonacci(int n) { throw new UnsupportedOperationException("TODO"); }
}

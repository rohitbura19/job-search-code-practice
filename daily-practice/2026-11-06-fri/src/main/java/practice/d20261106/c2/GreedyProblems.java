package practice.d20261106.c2;

import java.util.List;

public class GreedyProblems {
    /**
     * nums[i] = the maximum jump length from i. Fewest jumps from index 0 to the last index; 0 for length <= 1; -1 if unreachable.
     * O(n) time, O(1) space. Values 0..10^5, length up to 10^6.
     */
    public static int minJumps(int[] nums) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Circular route; station i gives gas[i] and the leg to i+1 costs cost[i]. Start with an empty tank.
     * Return the start index that completes the circle clockwise, or -1 if none exists (the answer is unique when it exists).
     * Empty input -> -1. O(n); sums can exceed int.
     */
    public static int canCompleteCircuit(int[] gas, int[] cost) { throw new UnsupportedOperationException("TODO"); }

    /** Lowercase letters only. Cut s into as many parts as possible so that each letter appears in at most one part. Return the part lengths. */
    public static List<Integer> partitionLabels(String s) { throw new UnsupportedOperationException("TODO"); }
}

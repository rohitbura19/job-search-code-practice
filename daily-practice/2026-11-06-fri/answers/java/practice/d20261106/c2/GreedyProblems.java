package practice.d20261106.c2;

import java.util.ArrayList;
import java.util.List;

/**
 * minJumps: BFS by levels without a queue. `end` is the furthest index reachable with the current jump count, `far` with one more.
 * Crossing `end` means we must jump again. O(n) / O(1).
 * canCompleteCircuit: if total gas < total cost no start works; otherwise the start is the index after the last point where the
 * running tank went negative (everything before it cannot be a start, because it failed before reaching it). O(n) / O(1).
 * partitionLabels: last occurrence of every letter; extend the current part to the max last index of letters seen; cut when i reaches it. O(n).
 */
public class GreedyProblems {
    public static int minJumps(int[] nums) {
        int jumps = 0, end = 0, far = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            if (i > far) return -1;
            far = Math.max(far, i + nums[i]);
            if (i == end) {
                jumps++;
                end = far;
                if (end >= nums.length - 1) return jumps;
            }
        }
        return nums.length <= 1 ? 0 : (far >= nums.length - 1 ? jumps : -1);
    }

    public static int canCompleteCircuit(int[] gas, int[] cost) {
        long total = 0, tank = 0;
        int start = 0;
        for (int i = 0; i < gas.length; i++) {
            long d = (long) gas[i] - cost[i];
            total += d;
            tank += d;
            if (tank < 0) {
                start = i + 1;
                tank = 0;
            }
        }
        return total >= 0 && gas.length > 0 ? start : -1;
    }

    public static List<Integer> partitionLabels(String s) {
        int[] last = new int[26];
        for (int i = 0; i < s.length(); i++) last[s.charAt(i) - 'a'] = i;
        List<Integer> parts = new ArrayList<>();
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            end = Math.max(end, last[s.charAt(i) - 'a']);
            if (i == end) {
                parts.add(i - start + 1);
                start = i + 1;
            }
        }
        return parts;
    }
}

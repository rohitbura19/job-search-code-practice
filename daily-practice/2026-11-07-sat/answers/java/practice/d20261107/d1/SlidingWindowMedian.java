package practice.d20261107.d1;

import java.util.*;

/**
 * Two TreeSets of INDICES ordered by (value, index): the tie-break makes every element unique, so removal and duplicates are
 * exact. lo holds the smaller ceil(k/2) elements, hi the rest; after each slide, rebalance with at most one move.
 * O(n log k) time, O(k) space. (Alternative: two heaps with lazy deletion and a balance counter, same bound but fiddlier.)
 */
public final class SlidingWindowMedian {
    private SlidingWindowMedian() {}

    public static double[] medianSlidingWindow(int[] nums, int k) {
        Comparator<Integer> cmp = (a, b) -> nums[a] != nums[b] ? Integer.compare(nums[a], nums[b]) : Integer.compare(a, b);
        TreeSet<Integer> lo = new TreeSet<>(cmp), hi = new TreeSet<>(cmp);
        double[] out = new double[nums.length - k + 1];
        for (int i = 0; i < nums.length; i++) {
            if (!lo.isEmpty() && cmp.compare(i, lo.last()) <= 0) lo.add(i); else hi.add(i);
            if (i >= k) {
                int gone = i - k;
                if (!lo.remove(gone)) hi.remove(gone);
            }
            while (lo.size() > (k + 1) / 2) hi.add(lo.pollLast());
            while (lo.size() < (k + 1) / 2 && !hi.isEmpty()) lo.add(hi.pollFirst());
            if (i >= k - 1) {
                out[i - k + 1] = (k & 1) == 1 ? nums[lo.last()] : ((double) nums[lo.last()] + nums[hi.first()]) / 2;
            }
        }
        return out;
    }
}

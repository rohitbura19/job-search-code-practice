package practice.d20261106.c1;

/**
 * Fenwick (binary indexed) tree over long sums. tree[i] covers the range (i - lowbit(i), i]. add/prefix walk O(log n) nodes.
 * Build in O(n) by pushing each node's value to its parent. update(i, v) adds the DELTA v - current.
 */
public class FenwickArray {
    private final int[] vals;
    private final long[] tree;

    public FenwickArray(int[] nums) {
        vals = nums.clone();
        tree = new long[nums.length + 1];
        for (int i = 1; i <= nums.length; i++) {
            tree[i] += nums[i - 1];
            int p = i + (i & -i);
            if (p <= nums.length) tree[p] += tree[i];
        }
    }

    public void update(int index, int value) {
        long delta = (long) value - vals[index];
        vals[index] = value;
        for (int i = index + 1; i < tree.length; i += i & -i) tree[i] += delta;
    }

    public long sumRange(int left, int right) { return prefix(right + 1) - prefix(left); }

    private long prefix(int n) {
        long s = 0;
        for (int i = n; i > 0; i -= i & -i) s += tree[i];
        return s;
    }
}

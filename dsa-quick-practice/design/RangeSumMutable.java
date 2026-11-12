package design;

// Range Sum Query - Mutable  [Medium | Fenwick tree (Binary Indexed Tree)]
// update and sumRange in O(log n).
//
// [1,3,5]: sumRange(0,2)=9; update(1,2); sumRange(0,2)=8

public class RangeSumMutable {

    public RangeSumMutable(int[] nums) {
        // TODO
    }

    void update(int index, int val) {
        // TODO
    }

    long sumRange(int left, int right) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        RangeSumMutable r = new RangeSumMutable(new int[]{1, 3, 5});
        System.out.println(r.sumRange(0, 2)); // 9
        r.update(1, 2);
        System.out.println(r.sumRange(0, 2)); // 8
    }
}

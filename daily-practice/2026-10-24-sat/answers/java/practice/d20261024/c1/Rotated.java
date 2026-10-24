package practice.d20261024.c1;

/**
 * Rotated sorted array: at every step one half of [lo, hi] is sorted. search: if nums[lo] <= nums[mid] the left half is sorted;
 * keep it iff target lies in [nums[lo], nums[mid]); otherwise the right half is sorted. findMin with duplicates: compare mid with
 * hi; mid > hi means the minimum is right of mid; mid < hi means mid or left; equal means we cannot tell, drop hi by one (worst
 * case O(n) for an array of equal values, which is unavoidable). Use lo + (hi - lo) / 2.
 */
public final class Rotated {
    private Rotated() {}

    public static int search(int[] nums, int target) {
        int lo = 0, hi = nums.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] == target) return mid;
            if (nums[lo] <= nums[mid]) {
                if (nums[lo] <= target && target < nums[mid]) hi = mid - 1;
                else lo = mid + 1;
            } else {
                if (nums[mid] < target && target <= nums[hi]) lo = mid + 1;
                else hi = mid - 1;
            }
        }
        return -1;
    }

    public static int findMin(int[] nums) {
        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (nums[mid] > nums[hi]) lo = mid + 1;
            else if (nums[mid] < nums[hi]) hi = mid;
            else hi--;
        }
        return nums[lo];
    }
}

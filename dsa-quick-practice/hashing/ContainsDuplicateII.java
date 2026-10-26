package hashing;

// Contains Duplicate II  [Easy | Hashing, sliding window]
// Return true if two indices i != j have nums[i] == nums[j] and |i - j| <= k.
//
// [1,2,3,1], k=3       -> true
// [1,2,3,1,2,3], k=2   -> false

public class ContainsDuplicateII {

    static boolean containsNearbyDuplicate(int[] nums, int k) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(containsNearbyDuplicate(new int[]{1, 2, 3, 1}, 3));       // true
        System.out.println(containsNearbyDuplicate(new int[]{1, 2, 3, 1, 2, 3}, 2)); // false
    }
}

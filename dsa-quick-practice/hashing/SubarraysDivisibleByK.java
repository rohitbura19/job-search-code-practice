package hashing;

// Subarray Sums Divisible by K  [Medium | Prefix sum mod k counts]
// Count contiguous subarrays whose sum is divisible by k. Negatives allowed (use floorMod).
//
// [4,5,0,-2,-3,1], k=5 -> 7
// [5], k=9             -> 0

public class SubarraysDivisibleByK {

    static int subarraysDivByK(int[] nums, int k) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(subarraysDivByK(new int[]{4, 5, 0, -2, -3, 1}, 5)); // 7
        System.out.println(subarraysDivByK(new int[]{5}, 9));                  // 0
    }
}

package arrays;

import java.util.HashMap;
import java.util.Map;

// Majority Element  [Easy | Boyer-Moore voting]
// Return the value that appears more than n/2 times (always exists). O(1) extra space.
//
// [3,2,3]         -> 3
// [2,2,1,1,1,2,2] -> 2

public class MajorityElement {

    // Approach 1: HashMap of counts.  Time O(n), Space O(n).
    static int majorityElementHashMap(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();
        int half = nums.length / 2;

        for (int num : nums) {
            // Increase this number's count by 1 (start at 0 if unseen).
            int c = count.getOrDefault(num, 0) + 1;
            count.put(num, c);

            // Return as soon as a count crosses n/2: no need to finish the loop.
            if (c > half) {
                return num;
            }
        }
        return -1; // unreachable: the problem guarantees a majority exists
    }

    // Approach 2: Boyer-Moore voting.  Time O(n), Space O(1).  <- optimal
    // Idea: pair each element with a different one and cancel both out.
    // The majority appears more than n/2 times, so it can never be fully cancelled
    // and is the candidate left standing at the end.
    static int majorityElement(int[] nums) {
        int candidate = 0;
        int count = 0;

        for (int num : nums) {
            // Count dropped to 0: everything so far cancelled out, start fresh with this number.
            if (count == 0) {
                candidate = num;
            }
            // Same as candidate -> one more vote; different -> one vote cancelled.
            count += (num == candidate) ? 1 : -1;
        }
        // No verification pass needed here because a majority is guaranteed.
        // (If it were not guaranteed, count occurrences of candidate in a second pass.)
        return candidate;
    }

    public static void main(String[] args) {
        System.out.println(majorityElement(new int[]{3, 2, 3}));                    // 3
        System.out.println(majorityElement(new int[]{2, 2, 1, 1, 1, 2, 2}));        // 2
        System.out.println(majorityElementHashMap(new int[]{3, 2, 3}));             // 3
        System.out.println(majorityElementHashMap(new int[]{2, 2, 1, 1, 1, 2, 2})); // 2
    }
}

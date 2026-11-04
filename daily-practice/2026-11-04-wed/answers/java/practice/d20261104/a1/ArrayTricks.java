package practice.d20261104.a1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * missingNumber: XOR of all indices 0..n and all values cancels pairs, leaving the missing one. O(n) time, O(1) space, and no overflow risk (a sum of 0..n can overflow int for large n).
 * findDuplicates: values are in 1..n, so value v "owns" slot v-1; the first visit negates it, a second visit finds it negative. The array is restored (abs) afterwards.
 * firstUniqChar: one counting pass, one scan for the first count of 1.
 */
public final class ArrayTricks {
    private ArrayTricks() {}

    public static int missingNumber(int[] nums) {
        int x = nums.length;
        for (int i = 0; i < nums.length; i++) x ^= i ^ nums[i];
        return x;
    }

    public static List<Integer> findDuplicates(int[] nums) {
        List<Integer> res = new ArrayList<>();
        for (int k = 0; k < nums.length; k++) {
            int i = Math.abs(nums[k]) - 1;
            if (nums[i] < 0) res.add(i + 1); else nums[i] = -nums[i];
        }
        for (int k = 0; k < nums.length; k++) nums[k] = Math.abs(nums[k]);
        Collections.sort(res);
        return res;
    }

    public static int firstUniqChar(String s) {
        int[] count = new int[26];
        for (int i = 0; i < s.length(); i++) count[s.charAt(i) - 'a']++;
        for (int i = 0; i < s.length(); i++) if (count[s.charAt(i) - 'a'] == 1) return i;
        return -1;
    }
}

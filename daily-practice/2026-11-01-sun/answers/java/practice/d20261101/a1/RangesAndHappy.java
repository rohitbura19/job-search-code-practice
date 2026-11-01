package practice.d20261101.a1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * isHappy: the digit-square sequence either reaches 1 or enters a cycle, so Floyd (slow/fast) detects it with O(1) space.
 * summaryRanges: one scan; compare with long arithmetic so MIN/MAX neighbours do not overflow.
 * isIsomorphic: the mapping must be a bijection, so keep it in both directions.
 * Time O(log n * steps) / O(n) / O(n); space O(1) / O(1) / O(alphabet).
 */
public final class RangesAndHappy {
    private RangesAndHappy() {}

    private static int next(int n) {
        int sum = 0;
        while (n > 0) { int d = n % 10; sum += d * d; n /= 10; }
        return sum;
    }

    public static boolean isHappy(int n) {
        int slow = n, fast = next(n);
        while (fast != 1 && slow != fast) { slow = next(slow); fast = next(next(fast)); }
        return fast == 1;
    }

    public static List<String> summaryRanges(int[] nums) {
        List<String> out = new ArrayList<>();
        int i = 0;
        while (i < nums.length) {
            int j = i;
            while (j + 1 < nums.length && (long) nums[j + 1] - nums[j] == 1) j++;
            out.add(i == j ? String.valueOf(nums[i]) : nums[i] + "->" + nums[j]);
            i = j + 1;
        }
        return out;
    }

    public static boolean isIsomorphic(String s, String t) {
        if (s.length() != t.length()) return false;
        Map<Character, Character> st = new HashMap<>(), ts = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char a = s.charAt(i), b = t.charAt(i);
            Character x = st.putIfAbsent(a, b), y = ts.putIfAbsent(b, a);
            if ((x != null && x != b) || (y != null && y != a)) return false;
        }
        return true;
    }
}

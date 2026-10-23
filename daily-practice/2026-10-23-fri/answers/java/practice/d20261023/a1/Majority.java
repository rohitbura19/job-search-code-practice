package practice.d20261023.a1;

import java.util.ArrayList;
import java.util.List;

/**
 * Boyer-Moore voting: a candidate with a counter; a different element cancels one vote. A true majority cannot be cancelled out
 * completely. For > n/3 keep TWO candidates (at most two such values exist) and cancel triples of distinct values; then a second
 * pass verifies, because the survivors are only candidates. O(n) time, O(1) space.
 */
public final class Majority {
    private Majority() {}

    public static int majorityElement(int[] nums) {
        int cand = 0, count = 0;
        for (int x : nums) {
            if (count == 0) cand = x;
            count += x == cand ? 1 : -1;
        }
        return cand;
    }

    public static List<Integer> majorityThird(int[] nums) {
        int x1 = 0, x2 = 1, c1 = 0, c2 = 0;
        for (int x : nums) {
            if (x == x1) c1++;
            else if (x == x2) c2++;
            else if (c1 == 0) { x1 = x; c1 = 1; }
            else if (c2 == 0) { x2 = x; c2 = 1; }
            else { c1--; c2--; }
        }
        int n1 = 0, n2 = 0;
        for (int x : nums) {
            if (x == x1) n1++;
            else if (x == x2) n2++;
        }
        List<Integer> out = new ArrayList<>();
        if (n1 * 3L > nums.length) out.add(x1);
        if (n2 * 3L > nums.length) out.add(x2);
        out.sort(null);
        return out;
    }
}

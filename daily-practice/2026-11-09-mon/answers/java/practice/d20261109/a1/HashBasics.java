package practice.d20261109.a1;

import java.util.*;

/** All three are O(n) expected time with hashing; majority uses O(1) space (Boyer-Moore voting). */
public final class HashBasics {
    private HashBasics() {}

    /** One pass with a value->index map. The complement is computed in long: int subtraction can overflow and match a wrong element. */
    public static int[] twoSum(int[] nums, int target) {
        Map<Long, Integer> seen = new HashMap<>();
        for (int j = 0; j < nums.length; j++) {
            Integer i = seen.get((long) target - nums[j]);
            if (i != null) return new int[] {i, j};
            seen.putIfAbsent((long) nums[j], j);
        }
        throw new IllegalArgumentException("no solution");
    }

    /** Key = sorted letters (O(k log k) per word). A 26-count signature gives O(k) per word. */
    public static List<List<String>> groupAnagrams(String[] words) {
        Map<String, List<String>> groups = new HashMap<>();
        for (String w : words) {
            char[] c = w.toCharArray();
            Arrays.sort(c);
            groups.computeIfAbsent(new String(c), k -> new ArrayList<>()).add(w);
        }
        return new ArrayList<>(groups.values());
    }

    /** Boyer-Moore: pairs of different elements cancel out; a strict majority survives. */
    public static int majorityElement(int[] a) {
        int cand = 0, votes = 0;
        for (int x : a) {
            if (votes == 0) cand = x;
            votes += x == cand ? 1 : -1;
        }
        return cand;
    }
}

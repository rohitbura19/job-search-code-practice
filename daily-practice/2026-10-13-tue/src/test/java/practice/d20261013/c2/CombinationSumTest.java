package practice.d20261013.c2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CombinationSumTest {

    /** Order-insensitive form: every combination sorted, then the list of combinations sorted. */
    static List<String> norm(List<List<Integer>> in) {
        List<String> out = new ArrayList<>();
        for (List<Integer> c : in) { List<Integer> s = new ArrayList<>(c); Collections.sort(s); out.add(s.toString()); }
        Collections.sort(out);
        return out;
    }
    static List<String> exp(String... combos) { List<String> l = new ArrayList<>(Arrays.asList(combos)); Collections.sort(l); return l; }

    @Test @DisplayName("[2,3,6,7], 7 -> [[2,2,3],[7]]")
    void example1() { assertEquals(exp("[2, 2, 3]", "[7]"), norm(CombinationSum.combinationSum(new int[]{2, 3, 6, 7}, 7))); }

    @Test @DisplayName("[2,3,5], 8 -> [[2,2,2,2],[2,3,3],[3,5]]")
    void example2() { assertEquals(exp("[2, 2, 2, 2]", "[2, 3, 3]", "[3, 5]"), norm(CombinationSum.combinationSum(new int[]{2, 3, 5}, 8))); }

    @Test @DisplayName("no combination -> empty list; target 0 -> one empty combination")
    void edges() {
        assertEquals(0, CombinationSum.combinationSum(new int[]{2}, 1).size());
        assertEquals(0, CombinationSum.combinationSum(new int[]{}, 5).size());
        assertEquals(exp("[]"), norm(CombinationSum.combinationSum(new int[]{2, 3}, 0)));
    }

    @Test @DisplayName("unsorted input is fine and the caller's array is not modified")
    void unsorted() {
        int[] c = {7, 3, 2, 6};
        assertEquals(exp("[2, 2, 3]", "[7]"), norm(CombinationSum.combinationSum(c, 7)));
        assertEquals("[7, 3, 2, 6]", Arrays.toString(c));
    }

    @Test @DisplayName("a single candidate that divides the target")
    void single() { assertEquals(exp("[1, 1, 1, 1, 1]"), norm(CombinationSum.combinationSum(new int[]{1}, 5))); }

    @Test @DisplayName("each returned combination really sums to the target and there are no duplicates")
    void validity() {
        List<List<Integer>> r = CombinationSum.combinationSum(new int[]{2, 3, 4, 7}, 14);
        for (List<Integer> c : r) assertEquals(14, c.stream().mapToInt(Integer::intValue).sum());
        assertEquals(r.size(), norm(r).stream().distinct().count());
        assertEquals(countBrute(new int[]{2, 3, 4, 7}, 14), r.size());
    }
    static long countBrute(int[] c, int target) {   // counts multisets by recursion on the index
        return rec(c, 0, target);
    }
    static long rec(int[] c, int i, int rem) {
        if (rem == 0) return 1;
        if (i == c.length || rem < 0) return 0;
        return rec(c, i, rem - c[i]) + rec(c, i + 1, rem);
    }

    // ---------- follow-ups ----------

    @Test @DisplayName("Combination Sum II: duplicates in the input, each element used once, no duplicate answers")
    void sum2() {
        assertEquals(exp("[1, 1, 6]", "[1, 2, 5]", "[1, 7]", "[2, 6]"), norm(CombinationSum.combinationSum2(new int[]{10, 1, 2, 7, 6, 1, 5}, 8)));
        assertEquals(exp("[1, 2, 2]", "[5]"), norm(CombinationSum.combinationSum2(new int[]{2, 5, 2, 1, 2}, 5)));
        assertEquals(0, CombinationSum.combinationSum2(new int[]{3, 3}, 9).size());
    }

    @Test @DisplayName("count only: matches the enumeration, and handles target 10,000 quickly")
    void count() {
        assertEquals(2, CombinationSum.countCombinations(new int[]{2, 3, 6, 7}, 7));
        assertEquals(3, CombinationSum.countCombinations(new int[]{2, 3, 5}, 8));
        assertEquals(1, CombinationSum.countCombinations(new int[]{2}, 0));
        assertEquals(0, CombinationSum.countCombinations(new int[]{2}, 3));
        assertEquals(CombinationSum.combinationSum(new int[]{2, 3, 4, 7}, 14).size(), CombinationSum.countCombinations(new int[]{2, 3, 4, 7}, 14));
        // coins 1, 2, 5 with target 10_000: closed form is not needed; compare with the textbook DP written here
        long[] dp = new long[10_001]; dp[0] = 1;
        for (int coin : new int[]{1, 2, 5}) for (int s = coin; s <= 10_000; s++) dp[s] += dp[s - coin];
        long expected = dp[10_000];
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> assertEquals(expected, CombinationSum.countCombinations(new int[]{1, 2, 5}, 10_000)));
    }
}

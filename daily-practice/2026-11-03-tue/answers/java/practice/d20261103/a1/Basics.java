package practice.d20261103.a1;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Majority: Boyer-Moore pairing - every non-majority element cancels one majority vote, so the survivor is the majority. O(n) time, O(1) space.
 * Pascal row: one list updated right-to-left so each cell still reads last row's values. O(r^2) time, O(r) space.
 * Brackets: stack of expected closers; valid iff never mismatched and empty at the end. O(n).
 */
public final class Basics {
    private Basics() {}

    public static int majorityElement(int[] nums) {
        int cand = 0, votes = 0;
        for (int x : nums) {
            if (votes == 0) { cand = x; votes = 1; }
            else if (x == cand) votes++;
            else votes--;
        }
        return cand;
    }

    public static List<Integer> pascalRow(int r) {
        List<Integer> row = new ArrayList<>(r + 1);
        row.add(1);
        for (int i = 1; i <= r; i++) {
            for (int j = i - 1; j >= 1; j--) row.set(j, row.get(j) + row.get(j - 1));
            row.add(1);
        }
        return row;
    }

    public static boolean isValid(String s) {
        Deque<Character> expect = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '(' -> expect.push(')');
                case '[' -> expect.push(']');
                case '{' -> expect.push('}');
                default -> { if (expect.isEmpty() || expect.pop() != c) return false; }
            }
        }
        return expect.isEmpty();
    }
}

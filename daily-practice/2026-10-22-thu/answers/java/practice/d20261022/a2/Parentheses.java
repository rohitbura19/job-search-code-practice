package practice.d20261022.a2;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * isValid: stack of the EXPECTED closers; O(n) time, O(n) space. Odd length can fail fast.
 * minAdd: one kind of bracket needs no stack, only a counter of unmatched '('.
 * A ')' with no open '(' is unfixable by earlier characters, so it costs one insertion at once; leftover opens cost one each.
 * O(n) time, O(1) space.
 */
public final class Parentheses {
    private Parentheses() {}

    public static boolean isValid(String s) {
        if ((s.length() & 1) == 1) return false;
        Deque<Character> expect = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '(' -> expect.push(')');
                case '[' -> expect.push(']');
                case '{' -> expect.push('}');
                default -> {
                    if (expect.isEmpty() || expect.pop() != c) return false;
                }
            }
        }
        return expect.isEmpty();
    }

    public static int minAddToMakeValid(String s) {
        int open = 0, add = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') open++;
            else if (open > 0) open--;
            else add++;
        }
        return open + add;
    }
}

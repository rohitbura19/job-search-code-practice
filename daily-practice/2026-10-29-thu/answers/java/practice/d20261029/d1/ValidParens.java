package practice.d20261029.d1;

/**
 * longest: two passes with counters and no stack. Left to right: open/close counts; when close > open reset both to 0; when equal
 * the span is 2*close. That misses "(()" (open never catches up), so repeat right to left with the roles swapped. O(n) / O(1).
 * minAddToMakeValid: the same balance idea: unmatched closes + final open balance.
 */
public final class ValidParens {
    private ValidParens() {}

    public static int longest(String s) {
        int best = 0, open = 0, close = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') open++; else close++;
            if (open == close) best = Math.max(best, 2 * close);
            else if (close > open) { open = 0; close = 0; }
        }
        open = 0; close = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == ')') close++; else open++;
            if (open == close) best = Math.max(best, 2 * open);
            else if (open > close) { open = 0; close = 0; }
        }
        return best;
    }

    public static int minAddToMakeValid(String s) {
        int bal = 0, added = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') bal++;
            else if (bal > 0) bal--;
            else added++;
        }
        return added + bal;
    }
}

package practice.d20261029.d1;

public final class ValidParens {
    private ValidParens() {}

    /**
     * Length of the longest well-formed (balanced) contiguous substring of a string of '(' and ')'.
     * "(()" -> 2, ")()())" -> 4, "" -> 0, "()(()" -> 2, "(()())" -> 6. n up to 10^6: no recursion; aim for O(n) time, O(1) extra space.
     */
    public static int longest(String s) { throw new UnsupportedOperationException("TODO"); }

    /** Follow-up: the minimum number of characters to insert so the whole string becomes balanced. "())" -> 1, "(((" -> 3, "()" -> 0. */
    public static int minAddToMakeValid(String s) { throw new UnsupportedOperationException("TODO"); }
}

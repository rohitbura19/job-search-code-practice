package stack;

// Longest Valid Parentheses  [Hard | Stack of indices, or two counter scans for O(1) space]
// Length of the longest balanced substring of '(' and ')'.
//
// "(()"     -> 2
// ")()())"  -> 4
// ""        -> 0

public class LongestValidParentheses {

    static int longestValidParentheses(String s) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(longestValidParentheses("(()"));    // 2
        System.out.println(longestValidParentheses(")()())")); // 4
        System.out.println(longestValidParentheses(""));       // 0
    }
}

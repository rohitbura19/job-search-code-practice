package backtracking;

import java.util.List;

// Generate Parentheses  [Medium | Backtrack with open/close counts]
// All well-formed strings of n pairs.
//
// 3 -> [((())), (()()), (())(), ()(()), ()()()]
// 1 -> [()]

public class GenerateParentheses {

    static List<String> generateParenthesis(int n) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(generateParenthesis(3)); // [((())), (()()), (())(), ()(()), ()()()]
        System.out.println(generateParenthesis(1)); // [()]
    }
}

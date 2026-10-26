package stack;

// Minimum Add to Make Parentheses Valid  [Medium | Counter]
// Only ( and ). Minimum characters to insert to make it valid.
//
// "())" -> 1
// ")("  -> 2
// "(((" -> 3

public class MinAddToMakeValid {

    static int minAddToMakeValid(String s) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(minAddToMakeValid("())")); // 1
        System.out.println(minAddToMakeValid(")("));  // 2
        System.out.println(minAddToMakeValid("(((")); // 3
    }
}

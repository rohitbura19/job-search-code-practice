package stack;

// Valid Parentheses  [Easy | Stack]
// String of ()[]{}. True if every bracket closes in the right order and type.
//
// "{[()]}" -> true
// "([)]"   -> false
// "(("     -> false

public class ValidParentheses {

    static boolean isValid(String s) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(isValid("{[()]}")); // true
        System.out.println(isValid("([)]"));   // false
        System.out.println(isValid("(("));     // false
    }
}

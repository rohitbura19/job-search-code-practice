package strings;

// Backspace String Compare  [Easy | Two pointers from the end]
// '#' is backspace. Are the typed strings equal? O(1) extra space.
//
// "ab#c", "ad#c" -> true
// "ab##", "c#d#" -> true
// "a#c", "b"     -> false

public class BackspaceCompare {

    static boolean backspaceCompare(String s, String t) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(backspaceCompare("ab#c", "ad#c")); // true
        System.out.println(backspaceCompare("ab##", "c#d#")); // true
        System.out.println(backspaceCompare("a#c", "b"));     // false
    }
}

package hashing;

// Isomorphic Strings  [Easy | Two-way mapping]
// Can s be mapped one-to-one onto t, character by character?
//
// "egg", "add"     -> true
// "foo", "bar"     -> false
// "badc", "baba"   -> false

public class IsomorphicStrings {

    static boolean isIsomorphic(String s, String t) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(isIsomorphic("egg", "add"));   // true
        System.out.println(isIsomorphic("foo", "bar"));   // false
        System.out.println(isIsomorphic("badc", "baba")); // false
    }
}

package hashing;

// Valid Anagram  [Easy | Character counts]
// Same multiset of characters? O(n).
//
// "anagram", "nagaram" -> true
// "rat", "car"         -> false

public class ValidAnagram {

    static boolean isAnagram(String s, String t) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(isAnagram("anagram", "nagaram")); // true
        System.out.println(isAnagram("rat", "car"));         // false
    }
}

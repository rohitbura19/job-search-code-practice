package dp;

// Regular Expression Matching  [Hard | 2-D DP on (i, j)]
// '.' = any one char, '*' = zero or more of the PRECEDING element. Whole string must match.
//
// ("aa", "a")            -> false
// ("aa", "a*")           -> true
// ("ab", ".*")           -> true
// ("aab", "c*a*b")       -> true
// ("mississippi", "mis*is*p*.") -> false

public class RegexMatching {

    static boolean isMatch(String s, String p) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(isMatch("aa", "a"));                  // false
        System.out.println(isMatch("aa", "a*"));                 // true
        System.out.println(isMatch("ab", ".*"));                 // true
        System.out.println(isMatch("aab", "c*a*b"));             // true
        System.out.println(isMatch("mississippi", "mis*is*p*.")); // false
    }
}

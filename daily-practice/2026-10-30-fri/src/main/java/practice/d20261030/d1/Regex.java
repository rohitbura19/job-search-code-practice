package practice.d20261030.d1;

public final class Regex {
    private Regex() {}

    /**
     * Does the pattern match the WHOLE string s? Pattern symbols: a lowercase letter (itself), '.' (any one char),
     * and '*' (zero or more of the PRECEDING element, so ".*" is anything). No other regex features.
     * The pattern is valid: '*' never comes first and never follows another '*'.
     * ("aa","a") false; ("aa","a*") true; ("ab",".*") true; ("aab","c*a*b") true; ("mississippi","mis*is*p*.") false; ("", "a*b*") true.
     * Must stay fast on ("a" x 30, "a*" x 15 + "b") (a plain recursion is exponential) and on 2000 x 2000 inputs.
     */
    public static boolean isMatch(String s, String p) { throw new UnsupportedOperationException("TODO"); }
}

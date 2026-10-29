package practice.d20261029.a2;

import java.util.List;

public final class Anagrams {
    private Anagrams() {}

    /** Same multiset of characters (any char, case-sensitive). "listen"/"silent" true; "aab"/"abb" false; "" and "" true. O(n). */
    public static boolean isAnagram(String a, String b) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Group words that are anagrams of each other. Groups in order of the first word that started them, words inside a group in input order.
     * ["eat","tea","tan","ate","nat","bat"] -> [["eat","tea","ate"],["tan","nat"],["bat"]]. Duplicates stay (two "a" -> one group of two).
     */
    public static List<List<String>> groupAnagrams(List<String> words) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Follow-up (lowercase a-z only): every start index i such that s.substring(i, i + p.length()) is an anagram of p, ascending.
     * s = "cbaebabacd", p = "abc" -> [0, 6]; "abab", "ab" -> [0, 1, 2]. Empty p or p longer than s -> []. Target O(|s| + |p|).
     */
    public static List<Integer> findAnagrams(String s, String p) { throw new UnsupportedOperationException("TODO"); }
}

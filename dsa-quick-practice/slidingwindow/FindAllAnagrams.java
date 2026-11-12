package slidingwindow;

import java.util.List;

// Find All Anagrams in a String  [Medium | Fixed window + counts]
// Every start index where a substring of s is an anagram of p. O(|s| + |p|).
//
// "cbaebabacd", "abc" -> [0,6]
// "abab", "ab"        -> [0,1,2]

public class FindAllAnagrams {

    static List<Integer> findAnagrams(String s, String p) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(findAnagrams("cbaebabacd", "abc")); // [0, 6]
        System.out.println(findAnagrams("abab", "ab"));        // [0, 1, 2]
    }
}

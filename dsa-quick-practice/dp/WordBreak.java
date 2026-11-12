package dp;

import java.util.List;

// Word Break  [Medium | dp[i] = prefix of length i is breakable]
// Can s be split into dictionary words (reusable)?
//
// "leetcode", [leet, code]                   -> true
// "applepenapple", [apple, pen]              -> true
// "catsandog", [cats, dog, sand, and, cat]   -> false

public class WordBreak {

    static boolean wordBreak(String s, List<String> dict) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(wordBreak("leetcode", List.of("leet", "code")));                        // true
        System.out.println(wordBreak("applepenapple", List.of("apple", "pen")));                   // true
        System.out.println(wordBreak("catsandog", List.of("cats", "dog", "sand", "and", "cat")));  // false
    }
}

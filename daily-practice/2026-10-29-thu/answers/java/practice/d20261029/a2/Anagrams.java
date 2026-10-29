package practice.d20261029.a2;

import java.util.*;

/**
 * isAnagram: a char -> count map (HashMap, so any Unicode char works); a mismatch in length is an early exit. O(n).
 * groupAnagrams: key = the string's sorted characters; LinkedHashMap keeps the groups in order of first appearance. O(n * L log L).
 * findAnagrams (a-z only): sliding window of length |p| with 26 counters and a "matches" counter so each step is O(1). O(n + |p|).
 */
public final class Anagrams {
    private Anagrams() {}

    public static boolean isAnagram(String a, String b) {
        if (a.length() != b.length()) return false;
        Map<Character, Integer> count = new HashMap<>();
        for (int i = 0; i < a.length(); i++) {
            count.merge(a.charAt(i), 1, Integer::sum);
            count.merge(b.charAt(i), -1, Integer::sum);
        }
        for (int v : count.values()) if (v != 0) return false;
        return true;
    }

    public static List<List<String>> groupAnagrams(List<String> words) {
        Map<String, List<String>> groups = new LinkedHashMap<>();
        for (String w : words) {
            char[] c = w.toCharArray();
            Arrays.sort(c);
            groups.computeIfAbsent(new String(c), k -> new ArrayList<>()).add(w);
        }
        return new ArrayList<>(groups.values());
    }

    public static List<Integer> findAnagrams(String s, String p) {
        List<Integer> out = new ArrayList<>();
        int n = s.length(), m = p.length();
        if (m == 0 || m > n) return out;
        int[] need = new int[26];
        for (int i = 0; i < m; i++) need[p.charAt(i) - 'a']++;
        int matched = 0;                           // number of letters whose window count equals need
        for (int c = 0; c < 26; c++) if (need[c] == 0) matched++;
        int[] have = new int[26];
        for (int i = 0; i < n; i++) {
            int in = s.charAt(i) - 'a';
            have[in]++;
            if (have[in] == need[in]) matched++; else if (have[in] == need[in] + 1) matched--;
            if (i >= m) {
                int outc = s.charAt(i - m) - 'a';
                have[outc]--;
                if (have[outc] == need[outc]) matched++; else if (have[outc] == need[outc] - 1) matched--;
            }
            if (i >= m - 1 && matched == 26) out.add(i - m + 1);
        }
        return out;
    }
}

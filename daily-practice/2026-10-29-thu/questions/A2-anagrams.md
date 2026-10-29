> Code: `src/main/java/practice/d20261029/a2/Anagrams.java`. Test: `AnagramsTest` (brute-force comparison, Unicode, 10^6 characters). **15 min** (the third method is a stretch: 10 more).

# A2. Anagrams: check, group, find

1. `isAnagram(a, b)`: same multiset of characters (any `char`, case-sensitive). `"listen"`/`"silent"` true; `"aab"`/`"abb"` false.
2. `groupAnagrams(words)`: `["eat","tea","tan","ate","nat","bat"]` -> `[["eat","tea","ate"],["tan","nat"],["bat"]]` (groups in order of first appearance, members in input order).
3. `findAnagrams(s, p)` (lowercase a-z): all start indices of anagrams of `p` in `s`. `"cbaebabacd"`, `"abc"` -> `[0, 6]`.

Targets: part 1 O(n); part 2 O(n L log L) with the sorted word as key (what is the O(n L) key, and when does it pay?); part 3 O(|s| + |p|) with a sliding window of counters. Say out loud: why `int[26]` is not enough for part 1 as specified, and how a "number of letters that match" counter makes each window step O(1) instead of comparing 26 counters.

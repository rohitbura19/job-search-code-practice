# Answers: C1-longest-substring-no-repeat

(Spoilers. Open only after your attempt.)

**Reference**
```java
public int lengthOfLongestSubstring(String s) {
    int[] last = new int[128];             // last index + 1 where each ASCII char was seen
    int best = 0, left = 0;
    for (int r = 0; r < s.length(); r++) {
        char c = s.charAt(r);
        left = Math.max(left, last[c]);    // jump left past the previous occurrence
        last[c] = r + 1;
        best = Math.max(best, r - left + 1);
    }
    return best;
}
```
O(n) time, O(alphabet) space. Using "last index + jump" instead of a shrinking `while` loop makes each character touched once. Substring: remember `bestLeft`. At most k distinct: a `HashMap<Character,Integer>` count and shrink while `map.size() > k`. Unicode: use a `HashMap<Integer,Integer>` over code points (`s.codePoints()`). Stream: memory is bounded by the alphabet size, not the stream length.

Kit note: `int[128]` throws `ArrayIndexOutOfBoundsException` for any char above 127 (the tests use some). Use `new int[65536]` for every UTF-16 char, or a `HashMap`.

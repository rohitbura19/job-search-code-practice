package practice.d20261013.c1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LongestSubstringTest {

    @Test @DisplayName("sheet examples")
    void examples() {
        assertEquals(3, LongestSubstring.lengthOfLongestSubstring("abcabcbb"));
        assertEquals(3, LongestSubstring.lengthOfLongestSubstring("pwwkew"));
        assertEquals(0, LongestSubstring.lengthOfLongestSubstring(""));
    }

    @Test @DisplayName("single char, all same, all distinct, repeat at the far end")
    void edges() {
        assertEquals(1, LongestSubstring.lengthOfLongestSubstring("a"));
        assertEquals(1, LongestSubstring.lengthOfLongestSubstring("bbbbb"));
        assertEquals(6, LongestSubstring.lengthOfLongestSubstring("abcdef"));
        assertEquals(2, LongestSubstring.lengthOfLongestSubstring("abba"));      // the window must never move left again
        assertEquals(5, LongestSubstring.lengthOfLongestSubstring("tmmzuxt"));
        assertEquals(2, LongestSubstring.lengthOfLongestSubstring("abab"));
    }

    @Test @DisplayName("not only ASCII: Latin-1 / BMP characters")
    void nonAscii() {
        assertEquals(3, LongestSubstring.lengthOfLongestSubstring("éèéêè"));   // e-acute, e-grave, e-acute, e-circumflex, e-grave
        assertEquals(4, LongestSubstring.lengthOfLongestSubstring("a中文b中"));
    }

    static int brute(String s) {
        int best = 0;
        for (int i = 0; i < s.length(); i++) {
            Set<Character> seen = new HashSet<>();
            int j = i;
            while (j < s.length() && seen.add(s.charAt(j))) j++;
            best = Math.max(best, j - i);
        }
        return best;
    }

    static String randomString(Random rnd, int len, int alphabet) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) sb.append((char) ('a' + rnd.nextInt(alphabet)));
        return sb.toString();
    }

    @Test @DisplayName("random strings agree with the brute force")
    void random() {
        Random rnd = new Random(13);
        for (int t = 0; t < 500; t++) {
            String s = randomString(rnd, rnd.nextInt(40), 1 + rnd.nextInt(8));
            assertEquals(brute(s), LongestSubstring.lengthOfLongestSubstring(s), s);
        }
    }

    @Test @DisplayName("2 million characters in linear time")
    void large() {
        String s = randomString(new Random(5), 2_000_000, 26);
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> assertEquals(brute26(s), LongestSubstring.lengthOfLongestSubstring(s)));
    }
    static int brute26(String s) {   // window never exceeds 26, so the brute force is O(26 n)
        int best = 0;
        for (int i = 0; i < s.length(); i++) {
            int mask = 0, j = i;
            while (j < s.length() && (mask & (1 << (s.charAt(j) - 'a'))) == 0) { mask |= 1 << (s.charAt(j) - 'a'); j++; }
            best = Math.max(best, j - i);
        }
        return best;
    }

    // ---------- follow-ups ----------

    @Test @DisplayName("return the substring itself (leftmost on ties)")
    void substring() {
        assertEquals("abc", LongestSubstring.longestSubstring("abcabcbb"));
        assertEquals("wke", LongestSubstring.longestSubstring("pwwkew"));
        assertEquals("", LongestSubstring.longestSubstring(""));
        assertEquals("b", LongestSubstring.longestSubstring("bbbb"));
        assertEquals("bcdef", LongestSubstring.longestSubstring("abbcdef"));
    }

    @Test @DisplayName("substring: length always matches lengthOfLongestSubstring and has no repeated char")
    void substringRandom() {
        Random rnd = new Random(3);
        for (int t = 0; t < 300; t++) {
            String s = randomString(rnd, rnd.nextInt(30), 1 + rnd.nextInt(6));
            String r = LongestSubstring.longestSubstring(s);
            assertEquals(brute(s), r.length(), s);
            assertTrue(s.contains(r), s);
            assertEquals(r.length(), r.chars().distinct().count(), s);
        }
    }

    @Test @DisplayName("at most k distinct characters")
    void kDistinct() {
        assertEquals(3, LongestSubstring.lengthAtMostKDistinct("eceba", 2));
        assertEquals(2, LongestSubstring.lengthAtMostKDistinct("aa", 1));
        assertEquals(0, LongestSubstring.lengthAtMostKDistinct("", 2));
        assertEquals(5, LongestSubstring.lengthAtMostKDistinct("abcde", 5));
        assertEquals(4, LongestSubstring.lengthAtMostKDistinct("aabbcc", 2));
    }

    @Test @DisplayName("k distinct agrees with brute force")
    void kDistinctRandom() {
        Random rnd = new Random(9);
        for (int t = 0; t < 300; t++) {
            String s = randomString(rnd, rnd.nextInt(30), 1 + rnd.nextInt(6));
            int k = 1 + rnd.nextInt(4);
            int best = 0;
            for (int i = 0; i < s.length(); i++) for (int j = i; j < s.length(); j++)
                if (s.substring(i, j + 1).chars().distinct().count() <= k) best = Math.max(best, j - i + 1);
            assertEquals(best, LongestSubstring.lengthAtMostKDistinct(s, k), s + " k=" + k);
        }
    }

    @Test @DisplayName("code points: a surrogate pair is ONE character")
    void codePoints() {
        String grin = new String(Character.toChars(0x1F600)), cat = new String(Character.toChars(0x1F431));
        assertEquals(3, LongestSubstring.lengthByCodePoints(grin + cat + "a" + grin));      // grin cat a | cat a grin -> 3
        assertEquals(1, LongestSubstring.lengthByCodePoints(grin + grin));
        assertEquals(0, LongestSubstring.lengthByCodePoints(""));
        assertEquals(3, LongestSubstring.lengthByCodePoints("abcabcbb"));
    }
}

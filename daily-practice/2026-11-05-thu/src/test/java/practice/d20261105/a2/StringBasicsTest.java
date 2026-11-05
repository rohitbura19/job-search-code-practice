package practice.d20261105.a2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class StringBasicsTest {
    @Test void strStrExamples() {
        assertEquals(2, StringBasics.strStr("hello", "ll"));
        assertEquals(-1, StringBasics.strStr("aaaaa", "bba"));
        assertEquals(0, StringBasics.strStr("abc", ""));
        assertEquals(0, StringBasics.strStr("", ""));
        assertEquals(-1, StringBasics.strStr("", "a"));
        assertEquals(-1, StringBasics.strStr("ab", "abc"));
        assertEquals(3, StringBasics.strStr("aaabaab", "baab"));
        assertEquals(1, StringBasics.strStr("mississippi", "issi"));
        assertEquals(4, StringBasics.strStr("mississippi", "issip"));
        assertEquals(3, StringBasics.strStr("aaab", "b"));
    }

    @Test void strStrMatchesIndexOf() {
        Random r = new Random(3);
        for (int t = 0; t < 2000; t++) {
            String h = rnd(r, r.nextInt(12)), nd = rnd(r, r.nextInt(4));
            assertEquals(h.indexOf(nd), StringBasics.strStr(h, nd), h + " / " + nd);
        }
    }

    static String rnd(Random r, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append((char) ('a' + r.nextInt(2)));
        return sb.toString();
    }

    @Test void strStrLargeAdversarial() {
        String hay = "a".repeat(20_000) + "b";
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(-1, StringBasics.strStr(hay, "a".repeat(300) + "c")));
    }

    @Test void reverseWordsExamples() {
        assertEquals("blue is sky the", StringBasics.reverseWords("the sky is blue"));
        assertEquals("world hello", StringBasics.reverseWords("  hello   world  "));
        assertEquals("", StringBasics.reverseWords(""));
        assertEquals("", StringBasics.reverseWords("     "));
        assertEquals("a", StringBasics.reverseWords("a"));
        assertEquals("a", StringBasics.reverseWords("  a"));
        assertEquals("c b a", StringBasics.reverseWords("a b c"));
        assertEquals("tab\there x", StringBasics.reverseWords("x tab\there"));
    }

    @Test void zigzagExamples() {
        assertEquals("PAHNAPLSIIGYIR", StringBasics.zigzag("PAYPALISHIRING", 3));
        assertEquals("PINALSIGYAHRPI", StringBasics.zigzag("PAYPALISHIRING", 4));
        assertEquals("ABC", StringBasics.zigzag("ABC", 1));
        assertEquals("AB", StringBasics.zigzag("AB", 5));
        assertEquals("", StringBasics.zigzag("", 3));
        assertEquals("ACBD", StringBasics.zigzag("ABCD", 2));
    }

    @Test void zigzagMatchesDirectFormula() {
        Random r = new Random(4);
        for (int t = 0; t < 300; t++) {
            String s = rnd(r, r.nextInt(30)).replace('a', 'x').replace('b', 'y') + "0123456789".substring(0, r.nextInt(10));
            int rows = 1 + r.nextInt(6);
            StringBuilder expected = new StringBuilder();
            int cycle = rows == 1 ? 1 : 2 * rows - 2;
            for (int row = 0; row < rows; row++)
                for (int i = 0; i < s.length(); i++) {
                    int m = i % cycle;
                    if (rows == 1 || m == row || m == cycle - row) expected.append(s.charAt(i));
                }
            assertEquals(expected.toString(), StringBasics.zigzag(s, rows));
        }
    }
}

package practice.d20261112.a1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class RomanAndPrefixTest {
    @Test void romanExamples() {
        assertEquals(3, RomanAndPrefix.romanToInt("III"));
        assertEquals(58, RomanAndPrefix.romanToInt("LVIII"));
        assertEquals(1994, RomanAndPrefix.romanToInt("MCMXCIV"));
        assertEquals(4, RomanAndPrefix.romanToInt("IV"));
        assertEquals(3999, RomanAndPrefix.romanToInt("MMMCMXCIX"));
    }
    @Test void intToRomanExamples() {
        assertEquals("III", RomanAndPrefix.intToRoman(3));
        assertEquals("LVIII", RomanAndPrefix.intToRoman(58));
        assertEquals("MCMXCIV", RomanAndPrefix.intToRoman(1994));
        assertEquals("MMMCMXCIX", RomanAndPrefix.intToRoman(3999));
        assertEquals("I", RomanAndPrefix.intToRoman(1));
    }
    @Test void roundTripAll() {
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            for (int n = 1; n <= 3999; n++) assertEquals(n, RomanAndPrefix.romanToInt(RomanAndPrefix.intToRoman(n)));
        });
    }
    @Test void prefixExamples() {
        assertEquals("fl", RomanAndPrefix.longestCommonPrefix(new String[]{"flower", "flow", "flight"}));
        assertEquals("", RomanAndPrefix.longestCommonPrefix(new String[]{"dog", "racecar", "car"}));
    }
    @Test void prefixEdges() {
        assertEquals("", RomanAndPrefix.longestCommonPrefix(new String[]{}));
        assertEquals("solo", RomanAndPrefix.longestCommonPrefix(new String[]{"solo"}));
        assertEquals("", RomanAndPrefix.longestCommonPrefix(new String[]{"", "abc"}));
        assertEquals("ab", RomanAndPrefix.longestCommonPrefix(new String[]{"ab", "abc", "ab"}));
        assertEquals("aaa", RomanAndPrefix.longestCommonPrefix(new String[]{"aaa", "aaa", "aaa"}));
    }
}

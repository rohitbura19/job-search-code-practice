package practice.d20261110.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class StringBasicsTest {
    private static String comp(String s) {
        char[] c = s.toCharArray();
        int n = StringBasics.compress(c);
        return new String(c, 0, n);
    }

    @Test void compressExamples() {
        assertEquals("a2b2c3", comp("aabbccc"));
        assertEquals("a", comp("a"));
        assertEquals("ab12", comp("abbbbbbbbbbbb"));
        assertEquals("x12", comp("xxxxxxxxxxxx"));
        assertEquals("abc", comp("abc"));
    }

    @Test void compressEdges() {
        assertEquals("", comp(""));
        assertEquals("a100", comp("a".repeat(100)));
        assertEquals("a2b2a2", comp("aabbaa"));
    }

    @Test void compressRandomAgainstReference() {
        Random r = new Random(3);
        for (int t = 0; t < 300; t++) {
            StringBuilder sb = new StringBuilder(), ref = new StringBuilder();
            for (int k = 0; k < r.nextInt(6); k++) {
                char c = (char) ('a' + r.nextInt(3));
                if (sb.length() > 0 && sb.charAt(sb.length() - 1) == c) continue;
                int run = 1 + r.nextInt(14);
                sb.append(String.valueOf(c).repeat(run));
                ref.append(c);
                if (run > 1) ref.append(run);
            }
            assertEquals(ref.toString(), comp(sb.toString()));
        }
    }

    @Test void ipv4() {
        for (String ok : new String[]{"0.0.0.0", "255.255.255.255", "192.168.1.1", "1.10.100.200"}) assertTrue(StringBasics.isValidIPv4(ok), ok);
        for (String bad : new String[]{"", "1.2.3", "1.2.3.4.5", "256.1.1.1", "01.1.1.1", "1..1.1", "1.1.1.", ".1.1.1", "a.b.c.d", "1.2.3.-4",
                "1.2.3.4 ", "0001.1.1.1", "1.1.1.1000", "+1.2.3.4", "1.2.3.4\n"})
            assertFalse(StringBasics.isValidIPv4(bad), "[" + bad + "]");
    }

    @Test void addStringsExamples() {
        assertEquals("134", StringBasics.addStrings("11", "123"));
        assertEquals("533", StringBasics.addStrings("456", "77"));
        assertEquals("0", StringBasics.addStrings("0", "0"));
        assertEquals("1000", StringBasics.addStrings("999", "1"));
        assertEquals("1" + "0".repeat(50), StringBasics.addStrings("9".repeat(50), "1"));
    }

    @Test void addStringsRandomAgainstBigInteger() {
        Random r = new Random(4);
        for (int t = 0; t < 300; t++) {
            BigInteger x = new BigInteger(r.nextInt(200) + 1, r), y = new BigInteger(r.nextInt(200) + 1, r);
            assertEquals(x.add(y).toString(), StringBasics.addStrings(x.toString(), y.toString()));
        }
    }

    @Test void largeInputWithinTimeout() {
        String a = "9".repeat(1_000_000), b = "1";
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(1_000_001, StringBasics.addStrings(a, b).length()));
        char[] big = new char[2_000_000];
        Arrays.fill(big, 'z');
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(8, StringBasics.compress(big)));
    }
}

package practice.d20261026.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class DecodeWaysTest {
    @Test void examples() {
        assertEquals(2, DecodeWays.numDecodings("12"));
        assertEquals(3, DecodeWays.numDecodings("226"));
        assertEquals(0, DecodeWays.numDecodings("06"));
        assertEquals(1, DecodeWays.numDecodings("10"));
        assertEquals(1, DecodeWays.numDecodings("2101"));
    }

    @Test void zerosAndEdges() {
        assertEquals(1, DecodeWays.numDecodings(""));
        assertEquals(0, DecodeWays.numDecodings("0"));
        assertEquals(0, DecodeWays.numDecodings("100"));
        assertEquals(0, DecodeWays.numDecodings("30"));
        assertEquals(1, DecodeWays.numDecodings("27"));
        assertEquals(1, DecodeWays.numDecodings("9"));
        assertEquals(2, DecodeWays.numDecodings("26"));
        assertEquals(1, DecodeWays.numDecodings("20"));
    }

    @Test void listing() {
        assertEquals(List.of("BBF", "BZ", "VF"), DecodeWays.decodeAll("226"));
        assertEquals(List.of("J"), DecodeWays.decodeAll("10"));
        assertEquals(List.of(), DecodeWays.decodeAll("06"));
        assertEquals(List.of(""), DecodeWays.decodeAll(""));
        assertEquals(List.of("AAA", "AK", "KA"), DecodeWays.decodeAll("111"));
    }

    @Test void largeCountsDoNotOverflowALongAtLength90() {
        long a = 1, b = 1;                                    // F(1), F(2); n ones decode in F(n+1) ways
        for (int i = 0; i < 89; i++) { long t = a + b; a = b; b = t; }
        assertEquals(b, DecodeWays.numDecodings("1".repeat(90)));
    }

    @Test void randomCountMatchesListing() {
        Random r = new Random(6);
        for (int t = 0; t < 3000; t++) {
            StringBuilder sb = new StringBuilder();
            int n = r.nextInt(13);
            for (int i = 0; i < n; i++) sb.append((char) ('0' + (r.nextInt(3) == 0 ? 0 : 1 + r.nextInt(9))));
            String s = sb.toString();
            List<String> all = DecodeWays.decodeAll(s);
            assertEquals(all.size(), DecodeWays.numDecodings(s), s);
            assertEquals(all.stream().sorted().toList(), all, "lexicographic order for " + s);
            assertEquals(all.size(), all.stream().distinct().count(), s);
        }
    }

    @Test void longInputIsLinear() {
        String s = "12".repeat(500_000);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> DecodeWays.numDecodings(s));
    }
}

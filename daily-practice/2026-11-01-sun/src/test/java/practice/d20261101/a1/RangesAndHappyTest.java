package practice.d20261101.a1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RangesAndHappyTest {
    private static boolean brute(int n) {
        Set<Integer> seen = new HashSet<>();
        while (n != 1 && seen.add(n)) { int s = 0; for (int m = n; m > 0; m /= 10) s += (m % 10) * (m % 10); n = s; }
        return n == 1;
    }

    @Test void happyExamples() {
        assertTrue(RangesAndHappy.isHappy(19));
        assertTrue(RangesAndHappy.isHappy(1));
        assertTrue(RangesAndHappy.isHappy(7));
        assertFalse(RangesAndHappy.isHappy(2));
        assertFalse(RangesAndHappy.isHappy(4));
    }

    @Test void happyMatchesBruteForce() {
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            for (int n = 1; n <= 100_000; n++) assertEquals(brute(n), RangesAndHappy.isHappy(n), "n=" + n);
            assertEquals(brute(Integer.MAX_VALUE), RangesAndHappy.isHappy(Integer.MAX_VALUE));
        });
    }

    @Test void rangesExamples() {
        assertEquals(List.of("0->2", "4->5", "7"), RangesAndHappy.summaryRanges(new int[] {0, 1, 2, 4, 5, 7}));
        assertEquals(List.of("-1->1", "3"), RangesAndHappy.summaryRanges(new int[] {-1, 0, 1, 3}));
    }

    @Test void rangesEdges() {
        assertEquals(List.of(), RangesAndHappy.summaryRanges(new int[0]));
        assertEquals(List.of("5"), RangesAndHappy.summaryRanges(new int[] {5}));
        assertEquals(List.of("1", "3", "5"), RangesAndHappy.summaryRanges(new int[] {1, 3, 5}));
    }

    @Test void rangesOverflow() {
        assertEquals(List.of("-2147483648", "2147483647"), RangesAndHappy.summaryRanges(new int[] {Integer.MIN_VALUE, Integer.MAX_VALUE}));
        assertEquals(List.of("2147483646->2147483647"), RangesAndHappy.summaryRanges(new int[] {Integer.MAX_VALUE - 1, Integer.MAX_VALUE}));
    }

    @Test void rangesLarge() {
        int[] a = new int[1_000_000];
        for (int i = 0; i < a.length; i++) a[i] = i * 2;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(a.length, RangesAndHappy.summaryRanges(a).size()));
    }

    @Test void isomorphic() {
        assertTrue(RangesAndHappy.isIsomorphic("egg", "add"));
        assertTrue(RangesAndHappy.isIsomorphic("paper", "title"));
        assertTrue(RangesAndHappy.isIsomorphic("", ""));
        assertFalse(RangesAndHappy.isIsomorphic("foo", "bar"));
        assertFalse(RangesAndHappy.isIsomorphic("badc", "baba"));   // two letters map to the same target
        assertFalse(RangesAndHappy.isIsomorphic("ab", "aa"));
        assertFalse(RangesAndHappy.isIsomorphic("ab", "abc"));
    }
}

package practice.d20261011.a1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ContainsDuplicateIITest {

    @Test @DisplayName("examples from the question")
    void examples() {
        assertTrue(ContainsDuplicateII.containsNearbyDuplicate(new int[]{1, 2, 3, 1}, 3));
        assertFalse(ContainsDuplicateII.containsNearbyDuplicate(new int[]{1, 2, 3, 1, 2, 3}, 2));
    }

    @Test @DisplayName("empty, single, k = 0")
    void edges() {
        assertFalse(ContainsDuplicateII.containsNearbyDuplicate(new int[]{}, 5));
        assertFalse(ContainsDuplicateII.containsNearbyDuplicate(new int[]{7}, 5));
        assertFalse(ContainsDuplicateII.containsNearbyDuplicate(new int[]{1, 1}, 0), "distinct indices must be within 0: impossible");
        assertTrue(ContainsDuplicateII.containsNearbyDuplicate(new int[]{1, 1}, 1));
    }

    @Test @DisplayName("distance exactly k and k + 1")
    void boundary() {
        assertTrue(ContainsDuplicateII.containsNearbyDuplicate(new int[]{5, 1, 2, 5}, 3));
        assertFalse(ContainsDuplicateII.containsNearbyDuplicate(new int[]{5, 1, 2, 5}, 2));
    }

    @Test @DisplayName("negatives, extreme values and a later closer pair")
    void negativesAndExtremes() {
        assertTrue(ContainsDuplicateII.containsNearbyDuplicate(new int[]{-1, -1}, 1));
        assertTrue(ContainsDuplicateII.containsNearbyDuplicate(new int[]{Integer.MIN_VALUE, 0, Integer.MIN_VALUE}, 2));
        assertTrue(ContainsDuplicateII.containsNearbyDuplicate(new int[]{9, 1, 2, 3, 9, 9}, 1), "last two 9s are adjacent");
        assertTrue(ContainsDuplicateII.containsNearbyDuplicate(new int[]{1, 0, 1, 1}, 1));
    }

    @Test @DisplayName("random arrays match the brute force")
    void randomAgainstBruteForce() {
        Random rnd = new Random(11);
        for (int t = 0; t < 4000; t++) {
            int n = rnd.nextInt(12); int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt(8) - 3;
            int k = rnd.nextInt(6);
            boolean expect = false;
            for (int i = 0; i < n && !expect; i++) for (int j = i + 1; j < n && j - i <= k; j++) if (a[i] == a[j]) { expect = true; break; }
            assertEquals(expect, ContainsDuplicateII.containsNearbyDuplicate(a, k), java.util.Arrays.toString(a) + " k=" + k);
        }
    }

    @Test @DisplayName("2 million distinct values with a huge k stay linear")
    void largeInput() {
        int n = 2_000_000; int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> assertFalse(ContainsDuplicateII.containsNearbyDuplicate(a, n)));
        int[] b = new int[n];
        for (int i = 0; i < n; i++) b[i] = i % 1000;
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> assertFalse(ContainsDuplicateII.containsNearbyDuplicate(b, 998)));
    }
}

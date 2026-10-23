package practice.d20261023.a1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class MajorityTest {
    @Test void majorityExamples() {
        assertEquals(3, Majority.majorityElement(new int[]{3, 2, 3}));
        assertEquals(2, Majority.majorityElement(new int[]{2, 2, 1, 1, 1, 2, 2}));
        assertEquals(7, Majority.majorityElement(new int[]{7}));
        assertEquals(-5, Majority.majorityElement(new int[]{-5, 1, -5, 2, -5}));
        assertEquals(Integer.MIN_VALUE, Majority.majorityElement(new int[]{Integer.MIN_VALUE, 0, Integer.MIN_VALUE}));
    }

    @Test void thirdExamples() {
        assertEquals(List.of(3), Majority.majorityThird(new int[]{3, 2, 3}));
        assertEquals(List.of(1), Majority.majorityThird(new int[]{1}));
        assertEquals(List.of(1, 2), Majority.majorityThird(new int[]{1, 2}));
        assertEquals(List.of(), Majority.majorityThird(new int[]{}));
        assertEquals(List.of(), Majority.majorityThird(new int[]{1, 2, 3}));
        assertEquals(List.of(1, 2), Majority.majorityThird(new int[]{2, 1, 1, 3, 2, 1, 2, 4}));
        assertEquals(List.of(0), Majority.majorityThird(new int[]{0, 0, 0}));
    }

    @Test void randomAgainstHashMap() {
        Random rnd = new Random(7);
        for (int t = 0; t < 2000; t++) {
            int n = rnd.nextInt(15), range = 1 + rnd.nextInt(5);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt(range) - 2;
            Map<Integer, Integer> cnt = new TreeMap<>();
            for (int x : a) cnt.merge(x, 1, Integer::sum);
            List<Integer> expect = new ArrayList<>();
            cnt.forEach((k, v) -> { if (v * 3 > n) expect.add(k); });
            assertEquals(expect, Majority.majorityThird(a), Arrays.toString(a));
        }
    }

    @Test void randomMajorityGuaranteed() {
        Random rnd = new Random(3);
        for (int t = 0; t < 500; t++) {
            int n = 1 + rnd.nextInt(21), m = rnd.nextInt(100) - 50;
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = i <= n / 2 ? m : rnd.nextInt(100) - 50;
            for (int i = n - 1; i > 0; i--) { int j = rnd.nextInt(i + 1); int s = a[i]; a[i] = a[j]; a[j] = s; }
            assertEquals(m, Majority.majorityElement(a), Arrays.toString(a));
        }
    }

    @Test void largeInput() {
        int n = 5_000_000;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i % 3 == 0 ? 9 : i % 3 == 1 ? 4 : -1;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertEquals(List.of(4, 9), Majority.majorityThird(a));      // counts 1666667, 1666667, 1666666 of 5000000
            int[] b = new int[n];
            Arrays.fill(b, 0, n / 2 + 1, 5);
            assertEquals(5, Majority.majorityElement(b));
        });
    }
}

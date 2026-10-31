package practice.d20261031.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MissingPositiveTest {
    static int f(int... a) { return MissingPositive.firstMissingPositive(a.clone()); }

    @Test void examples() {
        assertEquals(3, f(1, 2, 0));
        assertEquals(2, f(3, 4, -1, 1));
        assertEquals(1, f(7, 8, 9, 11, 12));
        assertEquals(4, f(1, 2, 3));
    }

    @Test void edges() {
        assertEquals(1, f());
        assertEquals(2, f(1));
        assertEquals(1, f(2));
        assertEquals(1, f(0));
        assertEquals(1, f(-5));
        assertEquals(2, f(1, 1));                       // duplicates must not loop forever
        assertEquals(3, f(1, 2, 2, 1));
        assertEquals(1, f(Integer.MAX_VALUE));
        assertEquals(2, f(1, Integer.MAX_VALUE, Integer.MIN_VALUE));
    }

    @Test void randomVsHashSet() {
        Random r = new Random(8);
        for (int t = 0; t < 1000; t++) {
            int[] a = new int[r.nextInt(12)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(16) - 3;
            Set<Integer> s = new HashSet<>();
            for (int x : a) s.add(x);
            int expect = 1;
            while (s.contains(expect)) expect++;
            assertEquals(expect, f(a));
        }
    }

    @Test void largeInputLinearTime() {
        int n = 2_000_000;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = n - i;       // reversed 1..n
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(n + 1, MissingPositive.firstMissingPositive(a)));
        int[] b = new int[n];
        java.util.Arrays.fill(b, 1);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(2, MissingPositive.firstMissingPositive(b)));
    }
}

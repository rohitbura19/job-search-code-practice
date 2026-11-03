package practice.d20261103.a1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class BasicsTest {
    @Test void majorityExamples() {
        assertEquals(3, Basics.majorityElement(new int[]{3, 2, 3}));
        assertEquals(2, Basics.majorityElement(new int[]{2, 2, 1, 1, 1, 2, 2}));
        assertEquals(-7, Basics.majorityElement(new int[]{-7}));
        assertEquals(Integer.MIN_VALUE, Basics.majorityElement(new int[]{Integer.MIN_VALUE, 1, Integer.MIN_VALUE}));
    }

    @Test void majorityRandomVsCounting() {
        Random rnd = new Random(7);
        for (int t = 0; t < 300; t++) {
            int n = 1 + rnd.nextInt(41), maj = rnd.nextInt(5) - 2;
            int[] a = new int[n];
            int need = n / 2 + 1;
            List<Integer> pos = new ArrayList<>();
            for (int i = 0; i < n; i++) pos.add(i);
            java.util.Collections.shuffle(pos, rnd);
            for (int i = 0; i < n; i++) a[pos.get(i)] = i < need ? maj : rnd.nextInt(7) - 3;
            assertEquals(maj, Basics.majorityElement(a));
        }
    }

    @Test void majorityLargeInput() {
        int[] a = new int[2_000_001];
        for (int i = 0; i < a.length; i++) a[i] = i % 2 == 0 ? 9 : i;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(9, Basics.majorityElement(a)));
    }

    @Test void pascalRows() {
        assertEquals(List.of(1), Basics.pascalRow(0));
        assertEquals(List.of(1, 1), Basics.pascalRow(1));
        assertEquals(List.of(1, 3, 3, 1), Basics.pascalRow(3));
        assertEquals(List.of(1, 4, 6, 4, 1), Basics.pascalRow(4));
    }

    @Test void pascalLastRowNoOverflowAndSymmetric() {
        List<Integer> row = Basics.pascalRow(33);
        assertEquals(34, row.size());
        long c = 1;
        for (int k = 0; k <= 33; k++) {
            assertEquals(c, row.get(k).longValue(), "k=" + k);
            c = c * (33 - k) / (k + 1);
        }
    }

    @Test void bracketsExamples() {
        assertTrue(Basics.isValid(""));
        assertTrue(Basics.isValid("()[]{}"));
        assertTrue(Basics.isValid("{[()]}"));
        assertFalse(Basics.isValid("(]"));
        assertFalse(Basics.isValid("([)]"));
        assertFalse(Basics.isValid("("));
        assertFalse(Basics.isValid(")"));
        assertFalse(Basics.isValid("(()"));
    }

    @Test void bracketsDeepNesting() {
        String open = "(".repeat(500_000), close = ")".repeat(500_000);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertTrue(Basics.isValid(open + close));
            assertFalse(Basics.isValid(open + close.substring(1)));
        });
    }
}

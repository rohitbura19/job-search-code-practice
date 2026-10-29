package practice.d20261029.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class ValidParensTest {
    @Test void examples() {
        assertEquals(2, ValidParens.longest("(()"));
        assertEquals(4, ValidParens.longest(")()())"));
        assertEquals(0, ValidParens.longest(""));
        assertEquals(2, ValidParens.longest("()(()"));
        assertEquals(6, ValidParens.longest("(()())"));
        assertEquals(0, ValidParens.longest("))(("));
        assertEquals(2, ValidParens.longest("(()(((()"));
        assertEquals(4, ValidParens.longest("(()((()()"));
    }

    @Test void addExamples() {
        assertEquals(1, ValidParens.minAddToMakeValid("())"));
        assertEquals(3, ValidParens.minAddToMakeValid("((("));
        assertEquals(0, ValidParens.minAddToMakeValid("()"));
        assertEquals(0, ValidParens.minAddToMakeValid(""));
        assertEquals(4, ValidParens.minAddToMakeValid("))(("));
    }

    static boolean balanced(String s, int from, int to) {
        int bal = 0;
        for (int i = from; i < to; i++) { bal += s.charAt(i) == '(' ? 1 : -1; if (bal < 0) return false; }
        return bal == 0;
    }

    @Test void randomAgainstBruteForce() {
        Random rnd = new Random(29);
        for (int round = 0; round < 3000; round++) {
            StringBuilder sb = new StringBuilder();
            int n = rnd.nextInt(16);
            for (int i = 0; i < n; i++) sb.append(rnd.nextBoolean() ? '(' : ')');
            String s = sb.toString();
            int best = 0;
            for (int i = 0; i < n; i++) for (int j = i + 2; j <= n; j += 2) if (balanced(s, i, j)) best = Math.max(best, j - i);
            assertEquals(best, ValidParens.longest(s), s);
            int bal = 0, add = 0;
            for (char c : s.toCharArray()) { if (c == '(') bal++; else if (bal > 0) bal--; else add++; }
            assertEquals(add + bal, ValidParens.minAddToMakeValid(s), s);
        }
    }

    @Test void largeInputsNoStackOverflow() {
        int n = 1_000_000;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertEquals(n, ValidParens.longest("(".repeat(n / 2) + ")".repeat(n / 2)));
            assertEquals(n, ValidParens.longest("()".repeat(n / 2)));
            assertEquals(0, ValidParens.longest(")".repeat(n)));
            assertEquals(2, ValidParens.longest("(".repeat(n - 2) + "()"));
        });
    }
}

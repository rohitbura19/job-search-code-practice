package practice.d20261022.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class ParenthesesTest {
    @Test void validExamples() {
        assertTrue(Parentheses.isValid(""));
        assertTrue(Parentheses.isValid("()[]{}"));
        assertTrue(Parentheses.isValid("{[()]}"));
        assertFalse(Parentheses.isValid("(]"));
        assertFalse(Parentheses.isValid("([)]"));
        assertFalse(Parentheses.isValid("("));
        assertFalse(Parentheses.isValid("]"));
        assertFalse(Parentheses.isValid("(("));
    }

    @Test void minAddExamples() {
        assertEquals(0, Parentheses.minAddToMakeValid(""));
        assertEquals(1, Parentheses.minAddToMakeValid("())"));
        assertEquals(3, Parentheses.minAddToMakeValid("((("));
        assertEquals(2, Parentheses.minAddToMakeValid(")("));
        assertEquals(4, Parentheses.minAddToMakeValid("()))(("));
    }

    private static boolean bruteValid(String s) {
        String p;
        do { p = s; s = s.replace("()", "").replace("[]", "").replace("{}", ""); } while (!s.equals(p));
        return s.isEmpty();
    }

    @Test void validRandom() {
        Random r = new Random(3);
        for (int t = 0; t < 2000; t++) {
            StringBuilder sb = new StringBuilder();
            for (int i = r.nextInt(10); i > 0; i--) sb.append("()[]{}".charAt(r.nextInt(6)));
            assertEquals(bruteValid(sb.toString()), Parentheses.isValid(sb.toString()), sb.toString());
        }
    }

    @Test void minAddRandom() {
        Random r = new Random(4);
        for (int t = 0; t < 2000; t++) {
            StringBuilder sb = new StringBuilder();
            for (int i = r.nextInt(14); i > 0; i--) sb.append(r.nextBoolean() ? '(' : ')');
            String s = sb.toString(), p;
            do { p = s; s = s.replace("()", ""); } while (!s.equals(p));
            assertEquals(s.length(), Parentheses.minAddToMakeValid(sb.toString()), sb.toString());
        }
    }

    @Test void largeInput() {
        int n = 1_000_000;
        String deep = "(".repeat(n) + ")".repeat(n);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertTrue(Parentheses.isValid(deep)));
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(2 * n, Parentheses.minAddToMakeValid(")".repeat(n) + "(".repeat(n))));
    }
}

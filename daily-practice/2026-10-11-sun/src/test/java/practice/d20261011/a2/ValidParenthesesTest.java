package practice.d20261011.a2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class ValidParenthesesTest {

    @Test @DisplayName("examples from the question")
    void examples() {
        assertTrue(ValidParentheses.isValid("{[()]}"));
        assertFalse(ValidParentheses.isValid("(]"));
        assertFalse(ValidParentheses.isValid("(("));
    }

    @Test @DisplayName("empty, single, closer first, wrong order")
    void edges() {
        assertTrue(ValidParentheses.isValid(""));
        assertFalse(ValidParentheses.isValid("("));
        assertFalse(ValidParentheses.isValid(")"));
        assertFalse(ValidParentheses.isValid("}{"));
        assertFalse(ValidParentheses.isValid("([)]"));
        assertTrue(ValidParentheses.isValid("()[]{}"));
        assertTrue(ValidParentheses.isValid("(([]){})"));
        assertFalse(ValidParentheses.isValid("()]"));
    }

    @Test @DisplayName("follow-up: first mismatch index, log lines ignored")
    void firstMismatch() {
        assertEquals(-1, ValidParentheses.firstMismatch(""));
        assertEquals(-1, ValidParentheses.firstMismatch("{[()]}"));
        assertEquals(1, ValidParentheses.firstMismatch("(]"));
        assertEquals(0, ValidParentheses.firstMismatch(")"));
        assertEquals(2, ValidParentheses.firstMismatch("()]"));
        assertEquals(2, ValidParentheses.firstMismatch("(("), "unclosed openers: s.length()");
        assertEquals(-1, ValidParentheses.firstMismatch("foo(bar[1]) { baz }"));
        assertEquals(10, ValidParentheses.firstMismatch("a(b[c]d) e) f"));
        assertEquals(4, ValidParentheses.firstMismatch("ab(c"), "unclosed, length 4");
    }

    @Test @DisplayName("1 million nested brackets stay linear and do not overflow the call stack")
    void largeInput() {
        int n = 1_000_000;
        String open = "(".repeat(n), close = ")".repeat(n);
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            assertTrue(ValidParentheses.isValid(open + close));
            assertFalse(ValidParentheses.isValid(open + close + ")"));
            assertEquals(2 * n, ValidParentheses.firstMismatch(open + close + ")"));
        });
    }
}

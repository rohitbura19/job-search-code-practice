package practice.d20261013.h1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class H1PredictionsTest {

    static String norm(String s) { return s.trim().replaceAll("\\s+", " "); }
    static void check(String predicted, String actual) {
        assertFalse("TODO".equals(predicted), "fill in your prediction in H1Predictions first");
        assertEquals(norm(actual), norm(predicted));
    }

    @Test @DisplayName("f(): finally runs, return value 1") void line1() { check(H1Predictions.LINE1, H1Snippets.line1()); }
    @Test @DisplayName("g(): return value is saved before finally") void line2() { check(H1Predictions.LINE2, H1Snippets.line2()); }
    @Test @DisplayName("h(): return in finally swallows the exception") void line3() { check(H1Predictions.LINE3, H1Snippets.line3()); }
    @Test @DisplayName("String pool: six comparisons") void line4() { check(H1Predictions.LINE4, H1Snippets.line4()); }
    @Test @DisplayName("try-with-resources: order and suppressed") void line5() { check(H1Predictions.LINE5, H1Snippets.line5()); }
    @Test @DisplayName("hashCode without equals") void line6() { check(H1Predictions.LINE6, H1Snippets.line6()); }
}

package practice.d20261012.h1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class H1PredictionsTest {

    static String norm(String s) { return s.trim().replaceAll("\\s+", " "); }
    static void check(String predicted, String actual) {
        assertFalse("TODO".equals(predicted), "fill in your prediction in H1Predictions first");
        assertEquals(norm(actual), norm(predicted));
    }

    @Test @DisplayName("line 1: bounded queue, AbortPolicy") void line1() throws Exception { check(H1Predictions.LINE1, H1Snippets.line1()); }
    @Test @DisplayName("line 2: unbounded queue") void line2() throws Exception { check(H1Predictions.LINE2, H1Snippets.line2()); }
    @Test @DisplayName("line 3: CallerRunsPolicy") void line3() throws Exception { check(H1Predictions.LINE3, H1Snippets.line3()); }
    @Test @DisplayName("line 4: execute vs submit") void line4() throws Exception { check(H1Predictions.LINE4, H1Snippets.line4()); }
    @Test @DisplayName("line 5: shutdownNow") void line5() throws Exception { check(H1Predictions.LINE5, H1Snippets.line5()); }
}

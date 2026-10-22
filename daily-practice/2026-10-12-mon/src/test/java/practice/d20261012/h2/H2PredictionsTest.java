package practice.d20261012.h2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class H2PredictionsTest {

    static String norm(String s) { return s.trim().replaceAll("\\s+", " "); }
    static void check(String predicted, String actual) {
        assertFalse("TODO".equals(predicted), "fill in your prediction in H2Predictions first");
        assertEquals(norm(actual), norm(predicted));
    }

    @Test @DisplayName("line 1: a.who(o), static type Object") void line1() { check(H2Predictions.LINE1, H2Snippets.line1()); }
    @Test @DisplayName("line 2: a.who(\"hi\")") void line2() { check(H2Predictions.LINE2, H2Snippets.line2()); }
    @Test @DisplayName("line 3: a.who(5)") void line3() { check(H2Predictions.LINE3, H2Snippets.line3()); }
    @Test @DisplayName("line 4: static and field hiding") void line4() { check(H2Predictions.LINE4, H2Snippets.line4()); }
}

package practice.d20261021.h1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class H1PredictionsTest {

    static String norm(String s) { return s.trim().replaceAll("\\s+", " "); }
    static void check(String predicted, String actual) {
        assertFalse("TODO".equals(predicted), "fill in your prediction in H1Predictions first");
        assertEquals(norm(actual), norm(predicted));
    }

    @Test @DisplayName("line 1: loop capture") void line1() { check(H1Predictions.LINE1, H1Snippets.line1()); }
    @Test @DisplayName("line 2: captured array element") void line2() { check(H1Predictions.LINE2, H1Snippets.line2()); }
    @Test @DisplayName("line 3: this in lambda vs anonymous class") void line3() { check(H1Predictions.LINE3, H1Snippets.line3()); }
    @Test @DisplayName("line 4: method reference on null") void line4() { check(H1Predictions.LINE4, H1Snippets.line4()); }
    @Test @DisplayName("line 5: bound receiver") void line5() { check(H1Predictions.LINE5, H1Snippets.line5()); }
    @Test @DisplayName("line 6: Optional") void line6() { check(H1Predictions.LINE6, H1Snippets.line6()); }
    @Test @DisplayName("line 7: andThen vs compose") void line7() { check(H1Predictions.LINE7, H1Snippets.line7()); }
    @Test @DisplayName("line 8: Runnable vs Callable") void line8() { check(H1Predictions.LINE8, H1Snippets.line8()); }
    @Test @DisplayName("line 10: exception in submit") void line10() throws Exception { check(H1Predictions.LINE10, H1Snippets.line10()); }
}

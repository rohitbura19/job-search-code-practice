package practice.d20261014.h2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class H2PredictionsTest {

    static String norm(String s) { return s.replaceAll("\\s*\\|\\s*", "|").trim(); }
    static void check(String predicted, String actual) {
        assertFalse("TODO".equals(predicted), "fill in your prediction in H2Predictions first");
        assertEquals(norm(actual), norm(predicted));
    }

    @Test @DisplayName("first new Child(): statics run, v is 0 inside show()")
    void first() { check(H2Predictions.FIRST_INSTANCE, H2Snippets.firstInstance()); }

    @Test @DisplayName("second new Child(): no static blocks any more")
    void second() { check(H2Predictions.SECOND_INSTANCE, H2Snippets.secondInstance()); }
}

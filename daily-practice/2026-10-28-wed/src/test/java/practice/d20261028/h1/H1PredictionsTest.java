package practice.d20261028.h1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class H1PredictionsTest {
    @Test void s1InitOrder() { assertEquals(H1Snippets.s1(), H1Predictions.s1()); }
    @Test void s2CharIntAndShifts() { assertEquals(H1Snippets.s2(), H1Predictions.s2()); }
    @Test void s3Collections() { assertEquals(H1Snippets.s3(), H1Predictions.s3()); }
    @Test void s4Strings() { assertEquals(H1Snippets.s4(), H1Predictions.s4()); }
    @Test void s5GenericsAndArrays() { assertEquals(H1Snippets.s5(), H1Predictions.s5()); }
    @Test void s6ControlFlow() { assertEquals(H1Snippets.s6(), H1Predictions.s6()); }
}

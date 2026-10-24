package practice.d20261024.h1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class H1PredictionsTest {
    @Test void boxingAndEquality() { assertEquals(H1Snippets.s1(), H1Predictions.s1()); }
    @Test void removeOverloadsAndIteration() { assertEquals(H1Snippets.s2(), H1Predictions.s2()); }
    @Test void initialisationOrder() { assertEquals(H1Snippets.s3(), H1Predictions.s3()); }
    @Test void numericTraps() { assertEquals(H1Snippets.s4(), H1Predictions.s4()); }
    @Test void stringApi() { assertEquals(H1Snippets.s5(), H1Predictions.s5()); }
    @Test void equalsHashCode() { assertEquals(H1Snippets.s6(), H1Predictions.s6()); }
}

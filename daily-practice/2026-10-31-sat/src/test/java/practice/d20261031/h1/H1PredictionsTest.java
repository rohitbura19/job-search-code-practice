package practice.d20261031.h1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class H1PredictionsTest {
    @Test void s1BoxingAndTernary() { assertEquals(H1Snippets.s1(), H1Predictions.s1()); }
    @Test void s2Overloads() { assertEquals(H1Snippets.s2(), H1Predictions.s2()); }
    @Test void s3HidingVsOverriding() { assertEquals(H1Snippets.s3(), H1Predictions.s3()); }
    @Test void s4StringIdentity() { assertEquals(H1Snippets.s4(), H1Predictions.s4()); }
    @Test void s5Collections() { assertEquals(H1Snippets.s5(), H1Predictions.s5()); }
    @Test void s6TryWithResources() { assertEquals(H1Snippets.s6(), H1Predictions.s6()); }
}

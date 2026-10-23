package practice.d20261023.h1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class H1PredictionsTest {
    @Test void tryWithResources() { assertEquals(H1Snippets.s1(), H1Predictions.s1()); }
    @Test void ternaryTyping() { assertEquals(H1Snippets.s2(), H1Predictions.s2()); }
    @Test void mutableKey() { assertEquals(H1Snippets.s3(), H1Predictions.s3()); }
    @Test void streamLaziness() { assertEquals(H1Snippets.s4(), H1Predictions.s4()); }
    @Test void hidingVsOverriding() { assertEquals(H1Snippets.s5(), H1Predictions.s5()); }
    @Test void views() { assertEquals(H1Snippets.s6(), H1Predictions.s6()); }
}

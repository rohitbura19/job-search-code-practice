package practice.d20261106.h1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class H1PredictionsTest {
    @Test void s1HidingAndSuperConstructor() { assertEquals(H1Snippets.s1(), H1Predictions.s1()); }
    @Test void s2TryWithResources() { assertEquals(H1Snippets.s2(), H1Predictions.s2()); }
    @Test void s3LazyStreams() { assertEquals(H1Snippets.s3(), H1Predictions.s3()); }
    @Test void s4ConditionalTyping() { assertEquals(H1Snippets.s4(), H1Predictions.s4()); }
    @Test void s5Arithmetic() { assertEquals(H1Snippets.s5(), H1Predictions.s5()); }
    @Test void s6Collections() { assertEquals(H1Snippets.s6(), H1Predictions.s6()); }
}

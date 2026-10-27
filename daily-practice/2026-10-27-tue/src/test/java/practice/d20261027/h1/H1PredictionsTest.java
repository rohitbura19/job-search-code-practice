package practice.d20261027.h1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class H1PredictionsTest {
    @Test void s1FinallyAndReturn() { assertEquals(H1Snippets.s1(), H1Predictions.s1()); }
    @Test void s2NumericEdges() { assertEquals(H1Snippets.s2(), H1Predictions.s2()); }
    @Test void s3Collections() { assertEquals(H1Snippets.s3(), H1Predictions.s3()); }
    @Test void s4OverloadsAndHiding() { assertEquals(H1Snippets.s4(), H1Predictions.s4()); }
    @Test void s5TryWithResources() { assertEquals(H1Snippets.s5(), H1Predictions.s5()); }
    @Test void s6ConditionalTyping() { assertEquals(H1Snippets.s6(), H1Predictions.s6()); }
}

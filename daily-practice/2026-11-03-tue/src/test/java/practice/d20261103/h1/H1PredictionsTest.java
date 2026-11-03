package practice.d20261103.h1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class H1PredictionsTest {
    // s1 must run first in a fresh JVM: static initialisers only run once. Keep the snippets class untouched until this test calls it.
    @Test void s1InitOrder() { assertEquals(H1Snippets.s1(), H1Predictions.s1()); }
    @Test void s2Finally() { assertEquals(H1Snippets.s2(), H1Predictions.s2()); }
    @Test void s3WrappersPoolAndTernary() { assertEquals(H1Snippets.s3(), H1Predictions.s3()); }
    @Test void s4EqualsHashCodeAndLists() { assertEquals(H1Snippets.s4(), H1Predictions.s4()); }
    @Test void s5ErasureAndOverloads() { assertEquals(H1Snippets.s5(), H1Predictions.s5()); }
    @Test void s6Operators() { assertEquals(H1Snippets.s6(), H1Predictions.s6()); }
}

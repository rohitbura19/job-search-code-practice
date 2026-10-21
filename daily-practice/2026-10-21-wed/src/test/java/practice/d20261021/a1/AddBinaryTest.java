package practice.d20261021.a1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class AddBinaryTest {

    @Test @DisplayName("examples from the question")
    void examples() {
        assertEquals("100", AddBinary.addBinary("11", "1"));
        assertEquals("10101", AddBinary.addBinary("1010", "1011"));
    }

    @Test @DisplayName("zero plus zero is a single 0")
    void zeros() { assertEquals("0", AddBinary.addBinary("0", "0")); }

    @Test @DisplayName("final carry creates a new leading digit")
    void finalCarry() {
        assertEquals("10", AddBinary.addBinary("1", "1"));
        assertEquals("10000", AddBinary.addBinary("1111", "1"));
    }

    @Test @DisplayName("different lengths, in both argument orders")
    void differentLengths() {
        assertEquals("1000000", AddBinary.addBinary("111111", "1"));
        assertEquals("1000000", AddBinary.addBinary("1", "111111"));
        assertEquals("101", AddBinary.addBinary("101", "0"));
    }

    @Test @DisplayName("long inputs (beyond long) match BigInteger")
    void matchesBigInteger() {
        Random rnd = new Random(42);
        for (int t = 0; t < 2000; t++) {
            BigInteger p = new BigInteger(rnd.nextInt(200) + 1, rnd), q = new BigInteger(rnd.nextInt(200) + 1, rnd);
            assertEquals(p.add(q).toString(2), AddBinary.addBinary(p.toString(2), q.toString(2)), p + " + " + q);
        }
    }
}

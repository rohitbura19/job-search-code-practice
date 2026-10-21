package practice.d20261021.a1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExcelColumnTest {

    @Test @DisplayName("title: single letters")
    void singleLetters() {
        assertEquals("A", ExcelColumn.title(1));
        assertEquals("Z", ExcelColumn.title(26));
    }

    @Test @DisplayName("title: the bijective boundaries (no zero digit)")
    void boundaries() {
        assertEquals("AA", ExcelColumn.title(27));
        assertEquals("AB", ExcelColumn.title(28));
        assertEquals("AZ", ExcelColumn.title(52));
        assertEquals("ZY", ExcelColumn.title(701));
        assertEquals("ZZ", ExcelColumn.title(702));
        assertEquals("AAA", ExcelColumn.title(703));
    }

    @Test @DisplayName("title: Integer.MAX_VALUE")
    void maxValue() { assertEquals("FXSHRXW", ExcelColumn.title(Integer.MAX_VALUE)); }

    @Test @DisplayName("number: inverse examples")
    void number() {
        assertEquals(1, ExcelColumn.number("A"));
        assertEquals(28, ExcelColumn.number("AB"));
        assertEquals(701, ExcelColumn.number("ZY"));
        assertEquals(Integer.MAX_VALUE, ExcelColumn.number("FXSHRXW"));
    }

    @Test @DisplayName("round trip number(title(n)) == n for 1..100000")
    void roundTrip() {
        for (int n = 1; n <= 100_000; n++) assertEquals(n, ExcelColumn.number(ExcelColumn.title(n)), "n=" + n);
    }
}

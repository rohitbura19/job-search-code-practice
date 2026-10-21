package practice.d20261021.a2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PascalTriangleTest {

    @Test @DisplayName("generate: 5 rows")
    void fiveRows() {
        assertEquals(List.of(List.of(1), List.of(1, 1), List.of(1, 2, 1), List.of(1, 3, 3, 1), List.of(1, 4, 6, 4, 1)),
                PascalTriangle.generate(5));
    }

    @Test @DisplayName("generate: 0 and 1 rows")
    void smallInputs() {
        assertEquals(List.of(), PascalTriangle.generate(0));
        assertEquals(List.of(List.of(1)), PascalTriangle.generate(1));
    }

    @Test @DisplayName("getRow: small rows")
    void getRowSmall() {
        assertEquals(List.of(1), PascalTriangle.getRow(0));
        assertEquals(List.of(1, 1), PascalTriangle.getRow(1));
        assertEquals(List.of(1, 3, 3, 1), PascalTriangle.getRow(3));
        assertEquals(List.of(1, 4, 6, 4, 1), PascalTriangle.getRow(4));
    }

    @Test @DisplayName("getRow(33): the largest row that fits in int")
    void row33() {
        List<Integer> r = PascalTriangle.getRow(33);
        assertEquals(34, r.size());
        assertEquals(Integer.valueOf(1166803110), r.get(16));
        assertEquals(Integer.valueOf(1166803110), r.get(17));
    }

    @Test @DisplayName("every row is symmetric and sums to 2^k")
    void symmetricAndSum() {
        for (int k = 0; k <= 30; k++) {
            List<Integer> r = PascalTriangle.getRow(k);
            long sum = 0;
            for (int c = 0; c <= k; c++) { assertEquals(r.get(c), r.get(k - c)); sum += r.get(c); }
            assertEquals(1L << k, sum, "k=" + k);
        }
    }

    @Test @DisplayName("getRow(k) equals row k of generate")
    void consistent() {
        List<List<Integer>> all = PascalTriangle.generate(25);
        for (int k = 0; k < 25; k++) assertEquals(all.get(k), PascalTriangle.getRow(k));
    }
}

package practice.d20261014.c2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DsuTest {

    @Test @DisplayName("union reports whether it merged; find agrees")
    void basic() {
        Dsu d = new Dsu(5);
        assertTrue(d.union(1, 2));
        assertTrue(d.union(2, 3));
        assertFalse(d.union(1, 3));
        assertEquals(d.find(1), d.find(3));
        assertFalse(d.find(1) == d.find(4));
        assertFalse(d.union(4, 4));
    }

    @Test @DisplayName("count starts at n and drops only on successful unions")
    void count() {
        Dsu d = new Dsu(6);
        assertEquals(6, d.count());
        d.union(1, 2); d.union(3, 4); d.union(2, 1);
        assertEquals(4, d.count());
        d.union(1, 4); d.union(5, 6);
        assertEquals(2, d.count());
        d.union(1, 6);
        assertEquals(1, d.count());
    }

    @Test @DisplayName("a chain of 300000 unions: no stack overflow (iterative find), fast")
    void longChain() {
        int n = 300_000;
        Dsu d = new Dsu(n);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            for (int i = 1; i < n; i++) assertTrue(d.union(i, i + 1));
            assertEquals(d.find(1), d.find(n));
            assertEquals(1, d.count());
        });
    }
}

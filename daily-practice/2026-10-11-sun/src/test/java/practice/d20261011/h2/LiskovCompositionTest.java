package practice.d20261011.h2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LiskovCompositionTest {

    @Test @DisplayName("the broken design: grow(Rectangle) gives 20, grow(Square) gives 16 (the Liskov violation)")
    void brokenDesign() {
        assertEquals(20, Client.grow(new BrokenRectangle()));
        assertEquals(16, Client.grow(new BrokenSquare()));
    }

    @Test @DisplayName("Rect and Sq compute area and perimeter")
    void shapes() {
        Shape r = new Rect(5, 4), q = new Sq(3);
        assertEquals(20, r.area());
        assertEquals(18, r.perimeter());
        assertEquals(9, q.area());
        assertEquals(12, q.perimeter());
        assertEquals(0, new Rect(0, 7).area());
    }

    @Test @DisplayName("immutability: 'setters' return a new object and leave the original alone")
    void immutable() {
        Rect r = new Rect(1, 1);
        Rect grown = r.withW(5).withH(4);
        assertEquals(20, grown.area());
        assertEquals(1, r.area());
        assertNotSame(r, grown);
        Sq s = new Sq(2);
        assertEquals(25, s.withSide(5).area());
        assertEquals(4, s.area());
        assertEquals(new Rect(5, 4), grown);
    }

    @Test @DisplayName("any Shape can be used by a client without surprises")
    void substitutable() {
        List<Shape> shapes = new ArrayList<>(List.of(new Rect(2, 3), new Sq(4), new Rect(1, 1)));
        int total = 0; for (Shape s : shapes) total += s.area();
        assertEquals(6 + 16 + 1, total);
    }

    @Test @DisplayName("diamond of default methods: the class must resolve it explicitly")
    void diamond() {
        Amphibian a = new Amphibian();
        assertEquals("walk+swim", a.move());
        Walker w = a;
        assertEquals("walk+swim", w.move(), "the class override wins on the Walker view too");
    }

    static class FlakyClient implements HttpClient {
        final int failuresBeforeSuccess; int calls = 0; final RuntimeException error = new IllegalStateException("503");
        FlakyClient(int f) { failuresBeforeSuccess = f; }
        public String get(String url) { calls++; if (calls <= failuresBeforeSuccess) throw error; return "ok:" + url; }
    }

    @Test @DisplayName("RetryingHttpClient wraps a client and retries it")
    void retriesThenSucceeds() {
        FlakyClient f = new FlakyClient(2);
        HttpClient c = new RetryingHttpClient(f, 3);
        assertEquals("ok:/a", c.get("/a"));
        assertEquals(3, f.calls);
    }

    @Test @DisplayName("RetryingHttpClient gives up after maxAttempts and rethrows the last error")
    void givesUp() {
        FlakyClient f = new FlakyClient(100);
        HttpClient c = new RetryingHttpClient(f, 3);
        assertSame(f.error, assertThrows(IllegalStateException.class, () -> c.get("/x")));
        assertEquals(3, f.calls);
    }

    @Test @DisplayName("RetryingHttpClient: first success needs one call; maxAttempts 1 means no retry; maxAttempts < 1 is rejected")
    void edges() {
        FlakyClient f = new FlakyClient(0);
        assertEquals("ok:/b", new RetryingHttpClient(f, 5).get("/b"));
        assertEquals(1, f.calls);
        FlakyClient g = new FlakyClient(1);
        assertThrows(IllegalStateException.class, () -> new RetryingHttpClient(g, 1).get("/c"));
        assertEquals(1, g.calls);
        assertThrows(IllegalArgumentException.class, () -> new RetryingHttpClient(new FlakyClient(0), 0));
    }

    @Test @DisplayName("composition: retry logic is reusable over any HttpClient (it can wrap another decorator)")
    void composes() {
        FlakyClient f = new FlakyClient(1);
        HttpClient twice = new RetryingHttpClient(new RetryingHttpClient(f, 1), 2);
        assertEquals("ok:/d", twice.get("/d"));
    }
}

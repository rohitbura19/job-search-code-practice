package practice.d20261031.h2;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class RequestTest {
    @Test void basicsAndFullUrl() {
        Request r = Request.builder("get", "/items").query("q", "a b&c").query("page", "2").header("Accept", "json").build();
        assertEquals("GET", r.method());
        assertEquals("/items?q=a+b%26c&page=2", r.fullUrl());
        assertEquals("GET /items?q=a+b%26c&page=2 headers=1 body=0B", r.toString());
        assertEquals("/plain", Request.builder("GET", "/plain").build().fullUrl());
        assertEquals(List.of("q", "page"), List.copyOf(r.query().keySet()));
    }

    @Test void headersAreCaseInsensitiveAndLastWins() {
        Request r = Request.builder("POST", "/x").header("Content-Type", "a").header("content-type", "b").body(new byte[] {1}).build();
        assertEquals("b", r.headers().get("CONTENT-TYPE"));
        assertEquals(1, r.headers().size());
    }

    @Test void validation() {
        assertThrows(IllegalStateException.class, () -> Request.builder("PATCH", "/x").build());
        assertThrows(IllegalStateException.class, () -> Request.builder(null, "/x").build());
        assertThrows(IllegalStateException.class, () -> Request.builder("GET", "x").build());
        assertThrows(IllegalStateException.class, () -> Request.builder("GET", null).build());
        assertThrows(IllegalStateException.class, () -> Request.builder("GET", "/x").body(new byte[] {1}).build());
        assertThrows(IllegalStateException.class, () -> Request.builder("delete", "/x").body(new byte[] {1}).build());
        assertThrows(IllegalArgumentException.class, () -> Request.builder("GET", "/x").header(" ", "v"));
        assertThrows(IllegalArgumentException.class, () -> Request.builder("GET", "/x").header("h", null));
        assertThrows(IllegalArgumentException.class, () -> Request.builder("GET", "/x").query("", "v"));
        Request.builder("POST", "/x").body(null).build();          // null body means no body
    }

    @Test void defensiveCopies() {
        byte[] in = {1, 2, 3};
        Request.Builder b = Request.builder("PUT", "/x").body(in);
        Request r = b.build();
        in[0] = 99;
        assertArrayEquals(new byte[] {1, 2, 3}, r.body());           // copy on the way in
        r.body()[1] = 99;
        assertArrayEquals(new byte[] {1, 2, 3}, r.body());           // copy on the way out
        b.header("late", "x").query("k", "v").body(new byte[] {7});
        assertEquals(0, r.headers().size());                          // builder changes after build() do not leak
        assertEquals(0, r.query().size());
        assertArrayEquals(new byte[] {1, 2, 3}, r.body());
        assertThrows(UnsupportedOperationException.class, () -> r.headers().put("a", "b"));
        assertThrows(UnsupportedOperationException.class, () -> r.query().clear());
    }

    @Test void toBuilderMakesAnIndependentCopy() {
        Request a = Request.builder("POST", "/x").header("A", "1").query("k", "v").body(new byte[] {5}).build();
        Request b = a.toBuilder().header("B", "2").body(new byte[] {6, 7}).build();
        assertEquals(1, a.headers().size());
        assertArrayEquals(new byte[] {5}, a.body());
        assertEquals(2, b.headers().size());
        assertArrayEquals(new byte[] {6, 7}, b.body());
        assertEquals("v", b.query().get("k"));
        assertEquals(a, a.toBuilder().build());
    }

    @Test void equalsAndHashCode() {
        Request a = Request.builder("POST", "/x").header("Accept", "j").body(new byte[] {1, 2}).build();
        Request b = Request.builder("post", "/x").header("ACCEPT", "j").body(new byte[] {1, 2}).build();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, Request.builder("POST", "/x").header("Accept", "j").body(new byte[] {1, 3}).build());
        assertNotEquals(a, Request.builder("PUT", "/x").header("Accept", "j").body(new byte[] {1, 2}).build());
        assertNotEquals(a, null);
        assertNotEquals(a, "POST /x");
        assertEquals(1, new java.util.HashSet<>(List.of(a, b)).size());
    }
}

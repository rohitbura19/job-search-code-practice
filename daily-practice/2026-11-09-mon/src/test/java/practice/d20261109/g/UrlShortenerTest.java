package practice.d20261109.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UrlShortenerTest {
    static class MutableClock extends Clock {
        Instant now = Instant.parse("2026-11-09T00:00:00Z");
        public ZoneId getZone() { return ZoneOffset.UTC; }
        public Clock withZone(ZoneId z) { return this; }
        public Instant instant() { return now; }
        void advance(Duration d) { now = now.plus(d); }
    }

    MutableClock clock;
    AtomicLong seq;
    UrlShortenerService svc;

    @BeforeEach void setUp() {
        clock = new MutableClock();
        seq = new AtomicLong(1);
        svc = new UrlShortenerService(seq::getAndIncrement, clock);
    }

    @Test void base62() {
        assertEquals("0", Base62.encode(0));
        assertEquals("1", Base62.encode(1));
        assertEquals("A", Base62.encode(10));
        assertEquals("a", Base62.encode(36));
        assertEquals("z", Base62.encode(61));
        assertEquals("10", Base62.encode(62));
        assertEquals(62L * 62 * 62 + 5, Base62.decode("1005"));
        assertThrows(IllegalArgumentException.class, () -> Base62.encode(-1));
        assertThrows(IllegalArgumentException.class, () -> Base62.decode(""));
        assertThrows(IllegalArgumentException.class, () -> Base62.decode("a-b"));
        for (long v : new long[] {0, 1, 61, 62, 3843, 3844, 1L << 40, Long.MAX_VALUE}) assertEquals(v, Base62.decode(Base62.encode(v)));
        assertEquals(11, Base62.encode(Long.MAX_VALUE).length());
        Random r = new Random(1);
        for (int i = 0; i < 2000; i++) { long v = r.nextLong() >>> 1; assertEquals(v, Base62.decode(Base62.encode(v))); }
    }

    @Test void shortenGeneratesCodesAndResolves() {
        ShortLink a = svc.shorten("https://example.com/a", null, null);
        ShortLink b = svc.shorten("https://example.com/a", null, null);
        assertEquals("1", a.code());
        assertEquals("2", b.code());
        assertEquals(Optional.of("https://example.com/a"), svc.resolve("1"));
        assertEquals(Optional.empty(), svc.resolve("nope"));
        assertNull(a.expiresAt());
        assertEquals(clock.now, a.createdAt());
    }

    @Test void rejectsBadInput() {
        for (String bad : new String[] {null, "", "not a url", "ftp://example.com/x", "example.com/x", "https:///nohost", "mailto:a@b.c"})
            assertThrows(IllegalArgumentException.class, () -> svc.shorten(bad, null, null), String.valueOf(bad));
        assertThrows(IllegalArgumentException.class, () -> svc.shorten("https://e.com", "ab", null));
        assertThrows(IllegalArgumentException.class, () -> svc.shorten("https://e.com", "has space", null));
        assertThrows(IllegalArgumentException.class, () -> svc.shorten("https://e.com", "x".repeat(31), null));
        assertThrows(IllegalArgumentException.class, () -> svc.shorten("https://e.com", null, Duration.ZERO));
        assertThrows(IllegalArgumentException.class, () -> svc.shorten("https://e.com", null, Duration.ofSeconds(-5)));
        assertEquals(1, seq.get(), "no id may be consumed by a rejected request");
    }

    @Test void customAliasAndConflicts() {
        assertEquals("my-link_1", svc.shorten("https://e.com/1", "my-link_1", null).code());
        assertThrows(AliasTakenException.class, () -> svc.shorten("https://e.com/2", "my-link_1", null));
        assertEquals(Optional.of("https://e.com/1"), svc.resolve("my-link_1"));
    }

    @Test void generatedCodeSkipsCodesAlreadyUsedByAliases() {
        AtomicLong ids = new AtomicLong(3844); // Base62 "100"
        UrlShortenerService s = new UrlShortenerService(ids::getAndIncrement, clock);
        s.shorten("https://e.com/x", "100", null);
        assertEquals("101", s.shorten("https://e.com/z", null, null).code());
        assertEquals(Optional.of("https://e.com/x"), s.resolve("100"));
    }

    @Test void expiryIsExclusiveAndFreesTheAlias() {
        svc.shorten("https://e.com/old", "promo", Duration.ofHours(1));
        clock.advance(Duration.ofMinutes(59));
        assertTrue(svc.resolve("promo").isPresent());
        clock.advance(Duration.ofMinutes(1));
        assertEquals(Optional.empty(), svc.resolve("promo"));
        ShortLink fresh = svc.shorten("https://e.com/new", "promo", null);
        assertEquals("promo", fresh.code());
        assertEquals(Optional.of("https://e.com/new"), svc.resolve("promo"));
        assertEquals(1, svc.clicks("promo"), "only the click after the replacement counts, the old link's click is gone");
    }

    @Test void clicksCountOnlySuccessfulResolves() {
        ShortLink l = svc.shorten("https://e.com/a", null, Duration.ofMinutes(5));
        svc.resolve(l.code()); svc.resolve(l.code()); svc.resolve("missing");
        assertEquals(2, svc.clicks(l.code()));
        clock.advance(Duration.ofMinutes(5));
        svc.resolve(l.code());
        assertEquals(2, svc.clicks(l.code()));
        assertEquals(0, svc.clicks("missing"));
    }

    @Test void purgeRemovesOnlyExpired() {
        svc.shorten("https://e.com/1", null, Duration.ofMinutes(1));
        svc.shorten("https://e.com/2", null, Duration.ofMinutes(1));
        ShortLink keep = svc.shorten("https://e.com/3", null, null);
        clock.advance(Duration.ofMinutes(2));
        assertEquals(2, svc.purgeExpired());
        assertEquals(0, svc.purgeExpired());
        assertTrue(svc.resolve(keep.code()).isPresent());
    }

    @Test void concurrentResolvesAreExact() throws Exception {
        ShortLink l = svc.shorten("https://e.com/hot", null, null);
        ExecutorService pool = Executors.newFixedThreadPool(8);
        List<Future<?>> fs = new ArrayList<>();
        for (int t = 0; t < 8; t++) fs.add(pool.submit(() -> { for (int i = 0; i < 5000; i++) svc.resolve(l.code()); }));
        for (Future<?> f : fs) f.get(10, TimeUnit.SECONDS);
        pool.shutdown();
        assertEquals(40_000, svc.clicks(l.code()));
    }

    @Test void concurrentSameAliasHasExactlyOneWinner() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(8);
        CountDownLatch go = new CountDownLatch(1);
        List<Future<Boolean>> fs = new ArrayList<>();
        for (int t = 0; t < 8; t++) {
            String url = "https://e.com/" + t;
            fs.add(pool.submit(() -> {
                go.await();
                try { svc.shorten(url, "race", null); return true; } catch (AliasTakenException e) { return false; }
            }));
        }
        go.countDown();
        int winners = 0;
        for (Future<Boolean> f : fs) if (f.get(10, TimeUnit.SECONDS)) winners++;
        pool.shutdown();
        assertEquals(1, winners);
    }
}

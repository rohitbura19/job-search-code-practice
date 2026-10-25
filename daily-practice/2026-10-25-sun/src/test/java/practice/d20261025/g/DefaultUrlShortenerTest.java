package practice.d20261025.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultUrlShortenerTest {
    static class MutableClock extends Clock {
        volatile Instant now = Instant.parse("2026-10-25T00:00:00Z");
        public ZoneId getZone() { return ZoneOffset.UTC; }
        public Clock withZone(ZoneId z) { return this; }
        public Instant instant() { return now; }
        void advance(Duration d) { now = now.plus(d); }
    }

    MutableClock clock;
    AtomicLong counter;
    UrlStore store;
    UrlShortener s;
    static final long START = 100_000;   // 3 base62 chars: long enough to be a valid alias
    static final String URL = "https://example.com/a/very/long/path?x=1";

    @BeforeEach void setUp() {
        clock = new MutableClock();
        counter = new AtomicLong(START);
        store = new InMemoryUrlStore();
        s = new DefaultUrlShortener(store, counter::getAndIncrement, clock);
    }

    // ---- Base62 ----
    @Test void base62Examples() {
        assertEquals("0", Base62.encode(0));
        assertEquals("1", Base62.encode(1));
        assertEquals("A", Base62.encode(10));
        assertEquals("a", Base62.encode(36));
        assertEquals("z", Base62.encode(61));
        assertEquals("10", Base62.encode(62));
        assertEquals("AzL8n0Y58m7", Base62.encode(Long.MAX_VALUE));
        assertEquals(Long.MAX_VALUE, Base62.decode("AzL8n0Y58m7"));
    }

    @Test void base62RoundTripAndErrors() {
        Random rnd = new Random(1);
        for (int i = 0; i < 10000; i++) {
            long n = rnd.nextLong() & Long.MAX_VALUE;
            assertEquals(n, Base62.decode(Base62.encode(n)));
        }
        assertThrows(IllegalArgumentException.class, () -> Base62.encode(-1));
        assertThrows(IllegalArgumentException.class, () -> Base62.decode(""));
        assertThrows(IllegalArgumentException.class, () -> Base62.decode("ab-c"));
        assertThrows(ArithmeticException.class, () -> Base62.decode("zzzzzzzzzzzz"));
    }

    // ---- store ----
    @Test void storePutIfAbsentClaimsOnce() {
        ShortUrl a = new ShortUrl("abc", "http://a.com", Instant.EPOCH, null), b = new ShortUrl("abc", "http://b.com", Instant.EPOCH, null);
        assertTrue(store.putIfAbsent(a));
        assertFalse(store.putIfAbsent(b));
        assertEquals("http://a.com", store.find("abc").orElseThrow().longUrl());
        assertTrue(store.find("nope").isEmpty());
        assertEquals(0, store.clicks("abc"));
        assertEquals(1, store.incrementClicks("abc"));
        assertEquals(2, store.incrementClicks("abc"));
        assertThrows(NoSuchElementException.class, () -> store.incrementClicks("nope"));
        assertThrows(NoSuchElementException.class, () -> store.clicks("nope"));
    }

    // ---- shorten / resolve ----
    @Test void generatedCodeComesFromTheIdAndResolves() {
        String code = s.shorten(URL, null, null);
        assertEquals(Base62.encode(START), code);
        assertEquals(Optional.of(URL), s.resolve(code));
        assertEquals(Base62.encode(START + 1), s.shorten(URL, null, null), "the same url gets a new code each time");
    }

    @Test void customAliasAndCollisions() {
        assertEquals("my-link_1", s.shorten(URL, "my-link_1", null));
        assertThrows(AliasTakenException.class, () -> s.shorten("https://other.com", "my-link_1", null));
        assertEquals(Optional.of(URL), s.resolve("my-link_1"), "the first mapping survived");
    }

    @Test void generatedCodeSkipsAnAliasThatOwnsIt() {
        String taken = Base62.encode(START);
        s.shorten(URL, taken, null);
        String code = s.shorten("https://other.com/x", null, null);
        assertEquals(Base62.encode(START + 1), code);
        assertEquals(Optional.of(URL), s.resolve(taken));
    }

    @Test void invalidInputIsRejected() {
        for (String bad : new String[]{null, "", "  ", "ftp://x.com/a", "example.com/a", "/relative", "http://", "https:///path", "javascript:alert(1)", "http://a b.com", "https://" + "a".repeat(2050)})
            assertThrows(IllegalArgumentException.class, () -> s.shorten(bad, null, null), String.valueOf(bad));
        for (String bad : new String[]{"ab", "has space", "a".repeat(21), "bad/slash", "ünï", ""})
            assertThrows(IllegalArgumentException.class, () -> s.shorten(URL, bad, null), bad);
        assertThrows(IllegalArgumentException.class, () -> s.shorten(URL, null, Duration.ZERO));
        assertThrows(IllegalArgumentException.class, () -> s.shorten(URL, null, Duration.ofSeconds(-1)));
        assertEquals(START, counter.get(), "validation happens before an id is consumed");
        assertEquals("abc", s.shorten("HTTP://Example.com", "abc", null), "scheme is case-insensitive");
    }

    @Test void unknownAndNullCodesResolveToEmpty() {
        assertTrue(s.resolve("zzz").isEmpty());
        assertTrue(s.resolve(null).isEmpty());
    }

    @Test void expiryIsExclusiveAndExpiredLinksDoNotCount() {
        String code = s.shorten(URL, null, Duration.ofMinutes(10));
        clock.advance(Duration.ofMinutes(10).minusMillis(1));
        assertTrue(s.resolve(code).isPresent());
        clock.advance(Duration.ofMillis(1));
        assertTrue(s.resolve(code).isEmpty(), "expired exactly at expiresAt");
        assertTrue(s.resolve(code).isEmpty());
        assertEquals(1, s.clicks(code), "only the successful resolve counted");
    }

    @Test void clicksCountSuccessfulResolvesOnly() {
        String code = s.shorten(URL, null, null);
        assertEquals(0, s.clicks(code));
        s.resolve(code);
        s.resolve(code);
        s.resolve("missing");
        assertEquals(2, s.clicks(code));
        assertThrows(NoSuchElementException.class, () -> s.clicks("missing"));
    }

    // ---- concurrency ----
    @Test void concurrentClicksAreNotLost() throws Exception {
        String code = s.shorten(URL, null, null);
        ExecutorService pool = Executors.newFixedThreadPool(8);
        List<Future<?>> fs = new ArrayList<>();
        for (int t = 0; t < 8; t++) fs.add(pool.submit(() -> { for (int i = 0; i < 5000; i++) s.resolve(code); }));
        for (Future<?> f : fs) f.get(10, TimeUnit.SECONDS);
        pool.shutdown();
        assertEquals(40_000, s.clicks(code));
    }

    @Test void exactlyOneThreadWinsAnAlias() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(8);
        CountDownLatch go = new CountDownLatch(1);
        AtomicInteger wins = new AtomicInteger(), losses = new AtomicInteger();
        List<Future<?>> fs = new ArrayList<>();
        for (int t = 0; t < 8; t++) {
            String target = "https://example.com/" + t;
            fs.add(pool.submit(() -> {
                go.await();
                try { s.shorten(target, "race", null); wins.incrementAndGet(); } catch (AliasTakenException e) { losses.incrementAndGet(); }
                return null;
            }));
        }
        go.countDown();
        for (Future<?> f : fs) f.get(10, TimeUnit.SECONDS);
        pool.shutdown();
        assertEquals(1, wins.get());
        assertEquals(7, losses.get());
    }

    @Test void generatedCodesAreUniqueUnderConcurrency() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(8);
        List<Future<String>> fs = IntStream.range(0, 4000).mapToObj(i -> pool.submit(() -> s.shorten(URL, null, null))).toList();
        Set<String> codes = new HashSet<>();
        for (Future<String> f : fs) codes.add(f.get(10, TimeUnit.SECONDS));
        pool.shutdown();
        assertEquals(4000, codes.size());
    }
}

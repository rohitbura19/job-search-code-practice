package practice.d20261026.g;

import static org.junit.jupiter.api.Assertions.*;
import static practice.d20261026.g.UploadException.Code.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResumableUploadServiceTest {
    /** A clock the test can move. */
    static final class MutableClock extends Clock {
        volatile Instant now = Instant.parse("2026-10-26T04:00:00Z");
        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId z) { return this; }
        @Override public Instant instant() { return now; }
    }

    MutableClock clock = new MutableClock();
    ChunkStore store = new InMemoryChunkStore();
    AtomicInteger seq = new AtomicInteger();
    UploadService svc;

    @BeforeEach void setUp() { svc = new ResumableUploadService(store, clock, Duration.ofHours(1), () -> "u" + seq.incrementAndGet()); }

    static byte[] bytes(String s) { return s.getBytes(StandardCharsets.UTF_8); }

    static String sha(byte[]... parts) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        for (byte[] p : parts) md.update(p);
        return HexFormat.of().formatHex(md.digest());
    }

    static UploadException fails(org.junit.jupiter.api.function.Executable e) { return assertThrows(UploadException.class, e); }

    @Test void specValidation() {
        for (UploadSpec bad : new UploadSpec[]{new UploadSpec(" ", 10, 4), new UploadSpec("f", 0, 4), new UploadSpec("f", 10, 0),
                new UploadSpec("f", 10, UploadSpec.MAX_CHUNK + 1), new UploadSpec(null, 10, 4)})
            assertEquals(INVALID_SPEC, fails(() -> svc.start(bad)).code());
        assertEquals("u1", svc.start(new UploadSpec("f", 10, 4)));
    }

    @Test void lastChunkIsTheRemainder() {
        UploadSpec s = new UploadSpec("f", 10, 4);
        assertEquals(3, s.chunkCount());
        assertEquals(2, s.lengthOf(2));
        assertEquals(1, new UploadSpec("f", 1, 4).chunkCount());
        assertEquals(1, new UploadSpec("f", 4, 4).chunkCount());
    }

    @Test void happyPathOutOfOrder() throws Exception {
        String id = svc.start(new UploadSpec("f.bin", 10, 4));
        assertEquals(List.of(0, 1, 2), svc.missing(id));
        assertEquals(PutResult.STORED, svc.putChunk(id, 2, bytes("ij")));
        assertEquals(PutResult.STORED, svc.putChunk(id, 0, bytes("abcd")));
        assertEquals(List.of(1), svc.missing(id));
        assertEquals(INCOMPLETE, fails(() -> svc.complete(id)).code());
        svc.putChunk(id, 1, bytes("efgh"));
        assertEquals(List.of(), svc.missing(id));
        String digest = svc.complete(id);
        assertEquals(sha(bytes("abcdefghij")), digest);
        assertEquals(digest, svc.complete(id), "complete is idempotent");
        assertEquals(List.of(), svc.missing(id));
        assertEquals(ALREADY_COMPLETED, fails(() -> svc.putChunk(id, 0, bytes("abcd"))).code());
    }

    @Test void singleChunkAndExactMultiple() throws Exception {
        String one = svc.start(new UploadSpec("a", 1, 1024));
        svc.putChunk(one, 0, new byte[]{7});
        assertEquals(sha(new byte[]{7}), svc.complete(one));
        String exact = svc.start(new UploadSpec("b", 8, 4));
        svc.putChunk(exact, 1, bytes("5678"));
        svc.putChunk(exact, 0, bytes("1234"));
        assertEquals(sha(bytes("12345678")), svc.complete(exact));
    }

    @Test void retriesAreIdempotentConflictsAreNot() {
        String id = svc.start(new UploadSpec("f", 8, 4));
        assertEquals(PutResult.STORED, svc.putChunk(id, 0, bytes("abcd")));
        assertEquals(PutResult.DUPLICATE, svc.putChunk(id, 0, bytes("abcd")));
        assertEquals(CONFLICT, fails(() -> svc.putChunk(id, 0, bytes("abce"))).code());
        assertEquals(List.of(1), svc.missing(id));
    }

    @Test void badIndexAndBadLength() {
        String id = svc.start(new UploadSpec("f", 10, 4));
        assertEquals(BAD_INDEX, fails(() -> svc.putChunk(id, -1, bytes("abcd"))).code());
        assertEquals(BAD_INDEX, fails(() -> svc.putChunk(id, 3, bytes("abcd"))).code());
        assertEquals(BAD_LENGTH, fails(() -> svc.putChunk(id, 0, bytes("abc"))).code());
        assertEquals(BAD_LENGTH, fails(() -> svc.putChunk(id, 2, bytes("ijk"))).code(), "last chunk must be exactly the remainder");
        assertEquals(BAD_LENGTH, fails(() -> svc.putChunk(id, 0, null)).code());
        assertEquals(List.of(0, 1, 2), svc.missing(id), "failed puts store nothing");
    }

    @Test void unknownAndAbortedIdsAreNotFound() {
        assertEquals(NOT_FOUND, fails(() -> svc.missing("nope")).code());
        assertEquals(NOT_FOUND, fails(() -> svc.abort("nope")).code());
        assertEquals(NOT_FOUND, fails(() -> svc.complete(null)).code());
        String id = svc.start(new UploadSpec("f", 8, 4));
        svc.putChunk(id, 0, bytes("abcd"));
        svc.abort(id);
        assertEquals(NOT_FOUND, fails(() -> svc.putChunk(id, 1, bytes("efgh"))).code());
        assertTrue(store.indexes(id).isEmpty(), "abort frees the chunks");
    }

    @Test void expiryIsInclusiveAndFreesTheChunks() {
        String id = svc.start(new UploadSpec("f", 8, 4));
        svc.putChunk(id, 0, bytes("abcd"));
        clock.now = clock.now.plus(Duration.ofMinutes(59)).plusSeconds(59);
        assertEquals(PutResult.STORED, svc.putChunk(id, 1, bytes("efgh")), "one second before the ttl is still alive");
        clock.now = clock.now.plusSeconds(1);
        assertEquals(EXPIRED, fails(() -> svc.complete(id)).code());
        assertTrue(store.indexes(id).isEmpty());
        assertEquals(NOT_FOUND, fails(() -> svc.missing(id)).code(), "after the EXPIRED answer the session is gone");
    }

    @Test void completedUploadsAlsoExpireOnlyThroughTheClock() throws Exception {
        String id = svc.start(new UploadSpec("f", 4, 4));
        svc.putChunk(id, 0, bytes("abcd"));
        String d = svc.complete(id);
        clock.now = clock.now.plus(Duration.ofMinutes(10));
        assertEquals(d, svc.complete(id));
    }

    @Test void concurrentUploadersOfOneFile() throws Exception {
        int chunks = 200;
        String id = svc.start(new UploadSpec("big", chunks * 4L, 4));
        ExecutorService pool = Executors.newFixedThreadPool(8);
        try {
            List<Future<Integer>> futures = new ArrayList<>();
            for (int t = 0; t < 8; t++) {
                futures.add(pool.submit(() -> {
                    int stored = 0;
                    for (int i = 0; i < chunks; i++) {                               // every thread sends every chunk: 8x duplicates
                        if (svc.putChunk(id, i, bytes(String.format("%04d", i))) == PutResult.STORED) stored++;
                    }
                    return stored;
                }));
            }
            int storedTotal = 0;
            for (Future<Integer> f : futures) storedTotal += f.get(10, TimeUnit.SECONDS);
            assertEquals(chunks, storedTotal, "exactly one thread stores each chunk");
        } finally {
            pool.shutdownNow();
        }
        StringBuilder all = new StringBuilder();
        for (int i = 0; i < chunks; i++) all.append(String.format("%04d", i));
        assertEquals(sha(bytes(all.toString())), svc.complete(id));
    }

    @Test void concurrentConflictingWritersOnlyOneWins() throws Exception {
        String id = svc.start(new UploadSpec("f", 4, 4));
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<String> a = pool.submit(() -> outcome(id, "aaaa"));
            Future<String> b = pool.submit(() -> outcome(id, "bbbb"));
            List<String> r = new ArrayList<>(List.of(a.get(5, TimeUnit.SECONDS), b.get(5, TimeUnit.SECONDS)));
            Collections.sort(r);
            assertEquals(List.of("CONFLICT", "STORED"), r);
        } finally {
            pool.shutdownNow();
        }
    }

    private String outcome(String id, String payload) {
        try { return svc.putChunk(id, 0, bytes(payload)).name(); } catch (UploadException e) { return e.code().name(); }
    }
}

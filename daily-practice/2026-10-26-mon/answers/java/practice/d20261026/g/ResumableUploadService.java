package practice.d20261026.g;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * One Session object per upload holds the immutable spec, expiry and the completed digest; every state-changing call
 * synchronizes on THAT session, so different uploads never contend and "check stored bytes, then write" is atomic per upload.
 * The map is a ConcurrentHashMap; abort/expiry remove the session first, then delete the chunks under the session lock.
 * The store is a port: the same code runs on object storage. Costs: putChunk O(chunk size) (compare bytes), missing O(chunks),
 * complete O(total bytes). Idempotence (DUPLICATE, repeated complete) is what makes client retries safe.
 */
public class ResumableUploadService implements UploadService {
    private static final class Session {
        final UploadSpec spec;
        final Instant expiresAt;
        String digest;                       // set once, when completed
        boolean gone;                        // set under the lock when aborted/expired, so racing callers see NOT_FOUND
        Session(UploadSpec spec, Instant expiresAt) { this.spec = spec; this.expiresAt = expiresAt; }
    }

    private final ChunkStore store;
    private final Clock clock;
    private final Duration ttl;
    private final Supplier<String> ids;
    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    public ResumableUploadService(ChunkStore store, Clock clock, Duration ttl, Supplier<String> ids) {
        this.store = store;
        this.clock = clock;
        this.ttl = ttl;
        this.ids = ids;
    }

    @Override public String start(UploadSpec spec) {
        if (spec == null || spec.fileName() == null || spec.fileName().isBlank() || spec.totalBytes() < 1
                || spec.chunkSize() < 1 || spec.chunkSize() > UploadSpec.MAX_CHUNK) {
            throw new UploadException(UploadException.Code.INVALID_SPEC, "bad upload spec: " + spec);
        }
        String id = ids.get();
        sessions.put(id, new Session(spec, clock.instant().plus(ttl)));
        return id;
    }

    @Override public PutResult putChunk(String id, int index, byte[] data) {
        Session s = live(id);
        synchronized (s) {
            check(id, s);
            if (s.digest != null) throw new UploadException(UploadException.Code.ALREADY_COMPLETED, id);
            if (index < 0 || index >= s.spec.chunkCount()) throw new UploadException(UploadException.Code.BAD_INDEX, "index " + index);
            if (data == null || data.length != s.spec.lengthOf(index)) throw new UploadException(UploadException.Code.BAD_LENGTH, "chunk " + index);
            Optional<byte[]> old = store.get(id, index);
            if (old.isPresent()) {
                if (Arrays.equals(old.get(), data)) return PutResult.DUPLICATE;
                throw new UploadException(UploadException.Code.CONFLICT, "chunk " + index + " already stored with different bytes");
            }
            store.put(id, index, data);
            return PutResult.STORED;
        }
    }

    @Override public List<Integer> missing(String id) {
        Session s = live(id);
        synchronized (s) {
            check(id, s);
            Set<Integer> have = store.indexes(id);
            List<Integer> out = new ArrayList<>();
            for (int i = 0; i < s.spec.chunkCount(); i++) if (!have.contains(i)) out.add(i);
            return out;
        }
    }

    @Override public String complete(String id) {
        Session s = live(id);
        synchronized (s) {
            check(id, s);
            if (s.digest != null) return s.digest;
            Set<Integer> have = store.indexes(id);
            if (have.size() != s.spec.chunkCount()) throw new UploadException(UploadException.Code.INCOMPLETE, (s.spec.chunkCount() - have.size()) + " chunks missing");
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                for (int i = 0; i < s.spec.chunkCount(); i++) md.update(store.get(id, i).orElseThrow());
                s.digest = HexFormat.of().formatHex(md.digest());
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(e);
            }
            return s.digest;
        }
    }

    @Override public void abort(String id) {
        Session s = live(id);
        synchronized (s) {
            discard(id, s);
        }
    }

    private Session live(String id) {
        Session s = id == null ? null : sessions.get(id);
        if (s == null) throw new UploadException(UploadException.Code.NOT_FOUND, String.valueOf(id));
        return s;
    }

    /** Called with the session lock held: expiry and abort are decided against the same state as the operation. */
    private void check(String id, Session s) {
        if (s.gone) throw new UploadException(UploadException.Code.NOT_FOUND, id);
        if (!clock.instant().isBefore(s.expiresAt)) {
            discard(id, s);
            throw new UploadException(UploadException.Code.EXPIRED, id);
        }
    }

    private void discard(String id, Session s) {
        s.gone = true;
        sessions.remove(id, s);
        store.deleteAll(id);
    }
}

package practice.d20261026.g;

import java.time.Clock;
import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

/**
 * Implement UploadService. Thread-safe: two threads may put different (or the same) chunks of one upload at the same time, and
 * "stored vs duplicate vs conflict" must be decided atomically. ids come from `ids`; a session expires at start + ttl.
 */
public class ResumableUploadService implements UploadService {
    public ResumableUploadService(ChunkStore store, Clock clock, Duration ttl, Supplier<String> ids) {}

    @Override public String start(UploadSpec spec) { throw new UnsupportedOperationException("TODO"); }
    @Override public PutResult putChunk(String uploadId, int index, byte[] data) { throw new UnsupportedOperationException("TODO"); }
    @Override public List<Integer> missing(String uploadId) { throw new UnsupportedOperationException("TODO"); }
    @Override public String complete(String uploadId) { throw new UnsupportedOperationException("TODO"); }
    @Override public void abort(String uploadId) { throw new UnsupportedOperationException("TODO"); }
}

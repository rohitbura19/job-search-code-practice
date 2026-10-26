package practice.d20261026.g;

import java.util.Optional;
import java.util.SortedSet;

/** Where chunk bytes live (in production: object storage). Implementations must be thread-safe. */
public interface ChunkStore {
    void put(String uploadId, int index, byte[] data);

    Optional<byte[]> get(String uploadId, int index);

    /** Indexes stored so far, ascending. */
    SortedSet<Integer> indexes(String uploadId);

    void deleteAll(String uploadId);
}

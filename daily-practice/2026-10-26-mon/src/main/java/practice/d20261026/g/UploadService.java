package practice.d20261026.g;

import java.util.List;

public interface UploadService {
    /** Validate the spec (fileName not blank, totalBytes >= 1, 1 <= chunkSize <= UploadSpec.MAX_CHUNK, else INVALID_SPEC) and open a session. */
    String start(UploadSpec spec);

    /**
     * Store chunk `index`. Errors (in this order): NOT_FOUND (unknown or aborted id), EXPIRED (clock >= expiry; the session and its
     * chunks are discarded, later calls say NOT_FOUND), ALREADY_COMPLETED, BAD_INDEX (outside 0..chunkCount-1), BAD_LENGTH (length differs
     * from spec.lengthOf(index)). Re-sending identical bytes is DUPLICATE (idempotent retry); different bytes for a stored index is CONFLICT.
     */
    PutResult putChunk(String uploadId, int index, byte[] data);

    /** Indexes not yet received, ascending (same errors as putChunk for unknown / expired ids). Works after completion (empty list). */
    List<Integer> missing(String uploadId);

    /**
     * All chunks present -> lowercase hex SHA-256 of the chunks concatenated in index order; otherwise INCOMPLETE. Idempotent:
     * calling again returns the same digest. After completion putChunk fails with ALREADY_COMPLETED.
     */
    String complete(String uploadId);

    /** Discard the session and every stored chunk. Unknown id -> NOT_FOUND. */
    void abort(String uploadId);
}

package practice.d20261108.g;

/** Result of IdempotencyService.begin. */
public sealed interface Outcome {
    /** The caller owns the key and must process the request, then call complete or abort with this token. */
    record Started(long token) implements Outcome {}
    /** Another attempt with the same payload is still running (live lease). Caller answers 409/"retry later". */
    record InProgress() implements Outcome {}
    /** A finished attempt exists: return its saved response without processing again. */
    record Replay(StoredResponse response) implements Outcome {}
    /** The key was used with a different payload fingerprint: caller answers 422. */
    record Mismatch() implements Outcome {}
}

package practice.d20261108.g;

/** The response saved for an idempotency key and replayed to retries. */
public record StoredResponse(int status, String body) {}

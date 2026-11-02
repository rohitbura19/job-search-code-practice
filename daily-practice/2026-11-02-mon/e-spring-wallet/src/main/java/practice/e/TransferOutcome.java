package practice.e;

/** Given: created = false means an earlier request with the same Idempotency-Key is being replayed. */
public record TransferOutcome(TransferView view, boolean created) {}

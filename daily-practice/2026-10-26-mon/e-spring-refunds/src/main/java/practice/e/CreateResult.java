package practice.e;

/** replayed = true when the Idempotency-Key had been seen with the same request (nothing new was created). */
public record CreateResult(RefundView view, boolean replayed) {}

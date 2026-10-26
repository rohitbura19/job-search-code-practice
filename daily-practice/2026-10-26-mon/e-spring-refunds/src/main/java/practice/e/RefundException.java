package practice.e;

import org.springframework.http.HttpStatus;

/** One exception type for every business failure; the handler turns status + title into a ProblemDetail. */
public class RefundException extends RuntimeException {
    private final HttpStatus status;
    private final String title;

    public RefundException(HttpStatus status, String title, String detail) {
        super(detail);
        this.status = status;
        this.title = title;
    }

    public HttpStatus status() { return status; }
    public String title() { return title; }

    public static RefundException notFound(long id) { return new RefundException(HttpStatus.NOT_FOUND, "Refund not found", String.valueOf(id)); }
    public static RefundException tooLarge(long amount, long max) {
        return new RefundException(HttpStatus.UNPROCESSABLE_ENTITY, "Refund too large", amount + " exceeds the maximum of " + max);
    }
    public static RefundException keyReused(String key) {
        return new RefundException(HttpStatus.CONFLICT, "Idempotency key reused", "key " + key + " was used with a different request");
    }
    public static RefundException alreadyDecided(long id, RefundStatus status) {
        return new RefundException(HttpStatus.CONFLICT, "Already decided", "refund " + id + " is " + status);
    }
}

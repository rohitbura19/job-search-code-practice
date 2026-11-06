package practice.d20261106.h2;

public interface Subscription {
    /** Stops delivery to this handler. Idempotent. */
    void cancel();
}

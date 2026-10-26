package practice.d20261026.h2;

public interface Subscription {
    /** Stop delivery. Idempotent. Takes effect immediately, also for an event that is being dispatched right now. */
    void cancel();

    boolean isActive();
}

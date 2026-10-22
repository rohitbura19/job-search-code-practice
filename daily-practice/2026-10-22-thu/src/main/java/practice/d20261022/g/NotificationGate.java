package practice.d20261022.g;

public interface NotificationGate {
    /** Decide AND record: only a SEND decision changes state. Check order: duplicate, quiet hours, rate limit. */
    Decision decide(Notification n);
}

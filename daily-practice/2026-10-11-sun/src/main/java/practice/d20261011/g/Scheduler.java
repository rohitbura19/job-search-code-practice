package practice.d20261011.g;

/** GIVEN - do not edit. Holds a request until quiet hours end (durable in a real system, a fake in tests). */
public interface Scheduler {
    void defer(NotificationRequest req, Channel channel);
}

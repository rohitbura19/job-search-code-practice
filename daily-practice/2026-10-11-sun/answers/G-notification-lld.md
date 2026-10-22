# Answers: G-notification-lld

(Spoilers. Open only after your attempt.)

**Reference**
```java
public enum Channel { PUSH, SMS, EMAIL }
public enum Priority { URGENT, NORMAL, BULK }

public record NotificationRequest(String eventId, String userId, String type,
                                  Map<String, String> params, Priority priority) {}

public interface ChannelSender {                       // Strategy
    Channel channel();
    SendResult send(Recipient r, RenderedMessage m);
}
public class SmsSender implements ChannelSender { /* uses ProviderClient list with failover */ }

public class SenderRegistry {                          // Factory / registry, Open-Closed
    private final Map<Channel, ChannelSender> map;
    public SenderRegistry(List<ChannelSender> senders) {
        map = senders.stream().collect(Collectors.toMap(ChannelSender::channel, s -> s));
    }
    public ChannelSender get(Channel c) { return map.get(c); }
}

public interface PreferenceService { Set<Channel> allowedChannels(String userId, String type); boolean inQuietHours(String userId, Instant now); }
public interface TemplateEngine   { RenderedMessage render(String type, Channel c, Map<String,String> params); }
public interface DedupeStore      { boolean firstTime(String key); }          // conditional put

public class RetryPolicy {                              // exponential backoff + jitter
    public Duration delay(int attempt) { return Duration.ofMillis((long) (200 * Math.pow(2, attempt) * (0.5 + Math.random()))); }
    public boolean shouldRetry(int attempt, Throwable t) { return attempt < 5 && !(t instanceof PermanentFailure); }
}

public class NotificationService {                      // orchestrator, depends only on interfaces
    public void handle(NotificationRequest req) {
        if (!dedupe.firstTime(req.eventId() + ":" + req.userId())) return;
        for (Channel c : prefs.allowedChannels(req.userId(), req.type())) {
            if (req.priority() != Priority.URGENT && prefs.inQuietHours(req.userId(), clock.instant())) { scheduler.defer(req, c); continue; }
            RenderedMessage msg = templates.render(req.type(), c, req.params());
            dispatcher.dispatchWithRetry(registry.get(c), recipients.load(req.userId()), msg);
        }
    }
}
```
Patterns: Strategy (senders), Factory/Registry (channel lookup), Template Method or Decorator (logging/metrics around senders), Chain of Responsibility (provider failover list), Observer (status events to audit). `Clock` injected for time-based tests.

Test plan: unit test `NotificationService` with fakes (in-memory DedupeStore, stub senders); parameterised tests for quiet hours across time zones; retry test using a fake sender that fails N times; contract test per `ChannelSender`.

Principal-level changes to discuss: make retry state durable (it must survive restart — retries via queue, not `Thread.sleep`); avoid a God `NotificationService` by splitting into pipeline stages (filter -> render -> route -> send); make `Priority` drive separate thread pools to prevent head-of-line blocking; thread safety of `SenderRegistry` (immutable map).

Kit note: the reference above leaves `Dispatcher` and the provider-failover chain undefined; the kit's `Dispatcher` (retry loop with the GIVEN `Sleeper`) and `FailoverSender` (tries providers in order) fill those gaps.

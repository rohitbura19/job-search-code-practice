package practice.d20261022.g;

import java.time.Clock;

public final class RateLimitedGate implements NotificationGate {
    public RateLimitedGate(Clock clock, GateConfig config) {
        // TODO: keep per-(user, channel) send timestamps and per-(user, dedupeKey) last-sent instants
    }

    @Override
    public synchronized Decision decide(Notification n) {
        throw new UnsupportedOperationException("TODO");
    }
}

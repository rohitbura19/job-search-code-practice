package practice.d20261011.g;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {

    static final Instant NOW = Instant.parse("2026-10-11T23:30:00Z");

    static class Fakes {
        final Set<String> seen = new HashSet<>();
        Set<Channel> allowed = new LinkedHashSet<>(List.of(Channel.PUSH, Channel.SMS));
        boolean quiet = false;
        final List<String> sent = new ArrayList<>();          // "CHANNEL:subject"
        final List<String> deferred = new ArrayList<>();      // "CHANNEL:type"
        final List<Instant> quietChecks = new ArrayList<>();
        final List<String> rendered = new ArrayList<>();
        FailureMode failure = null;
        interface FailureMode { void maybeFail(Channel c); }

        DedupeStore dedupe = seen::add;
        PreferenceService prefs = new PreferenceService() {
            public Set<Channel> allowedChannels(String userId, String type) { return allowed; }
            public boolean inQuietHours(String userId, Instant now) { quietChecks.add(now); return quiet; }
        };
        TemplateEngine templates = (type, c, params) -> { rendered.add(c + ":" + type); return new RenderedMessage(type + "/" + c, params.getOrDefault("charger", "?")); };
        RecipientRepository recipients = userId -> new Recipient(userId, "tok-" + userId, "+91000", userId + "@example.com");
        Scheduler scheduler = (req, ch) -> deferred.add(ch + ":" + req.type());
        Sleeper sleeper = d -> { };

        ChannelSender sender(Channel ch) {
            return new ChannelSender() {
                public Channel channel() { return ch; }
                public SendResult send(Recipient r, RenderedMessage m) {
                    if (failure != null) failure.maybeFail(ch);
                    sent.add(ch + ":" + m.subject() + ":" + m.body() + ":" + r.userId());
                    return new SendResult(true, ch.name());
                }
            };
        }

        NotificationService service() {
            SenderRegistry reg = new SenderRegistry(List.of(sender(Channel.PUSH), sender(Channel.SMS), sender(Channel.EMAIL)));
            return new NotificationService(dedupe, prefs, templates, reg, recipients,
                new Dispatcher(new RetryPolicy(), sleeper), scheduler, Clock.fixed(NOW, ZoneOffset.UTC));
        }
    }

    static NotificationRequest req(String eventId, String user, String type, Priority p) {
        return new NotificationRequest(eventId, user, type, Map.of("charger", "CH-7"), p);
    }

    @Test @DisplayName("faulted alert goes out on every allowed channel with the rendered text")
    void faultedAlert() {
        Fakes f = new Fakes();
        f.service().handle(req("e1", "u1", "charger-faulted", Priority.URGENT));
        assertEquals(List.of("PUSH:charger-faulted/PUSH:CH-7:u1", "SMS:charger-faulted/SMS:CH-7:u1"), f.sent);
        assertEquals(List.of("PUSH:charger-faulted", "SMS:charger-faulted"), f.rendered);
        assertTrue(f.deferred.isEmpty());
    }

    @Test @DisplayName("only the channels the user allows are used")
    void respectsPreferences() {
        Fakes f = new Fakes(); f.allowed = new LinkedHashSet<>(List.of(Channel.EMAIL));
        f.service().handle(req("e1", "u1", "charging-complete", Priority.NORMAL));
        assertEquals(1, f.sent.size());
        assertTrue(f.sent.get(0).startsWith("EMAIL:"));
        f.allowed = new LinkedHashSet<>();
        f.service().handle(req("e2", "u1", "charging-complete", Priority.NORMAL));
        assertEquals(1, f.sent.size(), "no allowed channel = nothing sent");
    }

    @Test @DisplayName("the same event for the same user is handled once, a different user or event is not a duplicate")
    void dedupe() {
        Fakes f = new Fakes(); NotificationService s = f.service();
        s.handle(req("e1", "u1", "payment-failed", Priority.NORMAL));
        s.handle(req("e1", "u1", "payment-failed", Priority.NORMAL));
        assertEquals(2, f.sent.size(), "two channels, sent once");
        s.handle(req("e1", "u2", "payment-failed", Priority.NORMAL));
        s.handle(req("e2", "u1", "payment-failed", Priority.NORMAL));
        assertEquals(6, f.sent.size());
        assertTrue(f.seen.contains("e1:u1"), "dedupe key is eventId + ':' + userId");
    }

    @Test @DisplayName("quiet hours: NORMAL and BULK are deferred (per channel) and not sent")
    void quietHoursDefers() {
        Fakes f = new Fakes(); f.quiet = true; NotificationService s = f.service();
        s.handle(req("e1", "u1", "charging-complete", Priority.NORMAL));
        s.handle(req("e2", "u1", "newsletter", Priority.BULK));
        assertTrue(f.sent.isEmpty());
        assertEquals(List.of("PUSH:charging-complete", "SMS:charging-complete", "PUSH:newsletter", "SMS:newsletter"), f.deferred);
        assertTrue(f.rendered.isEmpty(), "nothing is rendered for a deferred message");
        assertEquals(NOW, f.quietChecks.get(0), "the injected clock supplies 'now'");
    }

    @Test @DisplayName("quiet hours do not hold back URGENT alerts")
    void urgentIgnoresQuietHours() {
        Fakes f = new Fakes(); f.quiet = true;
        f.service().handle(req("e1", "u1", "charger-faulted", Priority.URGENT));
        assertEquals(2, f.sent.size());
        assertTrue(f.deferred.isEmpty());
    }

    @Test @DisplayName("a transient provider error is retried inside handle()")
    void retriesInside() {
        Fakes f = new Fakes(); int[] smsCalls = {0};
        f.failure = ch -> { if (ch == Channel.SMS && ++smsCalls[0] <= 2) throw new TransientFailure("vendor timeout"); };
        f.service().handle(req("e1", "u1", "charger-faulted", Priority.URGENT));
        assertEquals(3, smsCalls[0]);
        assertEquals(2, f.sent.size());
    }

    @Test @DisplayName("a permanent failure surfaces to the caller and is not retried")
    void permanentSurfaces() {
        Fakes f = new Fakes(); int[] calls = {0};
        f.failure = ch -> { if (ch == Channel.PUSH) { calls[0]++; throw new PermanentFailure("token revoked"); } };
        assertThrows(PermanentFailure.class, () -> f.service().handle(req("e1", "u1", "charger-faulted", Priority.URGENT)));
        assertEquals(1, calls[0]);
    }

    @Test @DisplayName("a channel without a registered sender is a clear error")
    void missingSender() {
        Fakes f = new Fakes(); f.allowed = new LinkedHashSet<>(List.of(Channel.EMAIL));
        NotificationService s = new NotificationService(f.dedupe, f.prefs, f.templates, new SenderRegistry(List.of(f.sender(Channel.PUSH))),
            f.recipients, new Dispatcher(new RetryPolicy(), f.sleeper), f.scheduler, Clock.fixed(NOW, ZoneOffset.UTC));
        assertThrows(IllegalArgumentException.class, () -> s.handle(req("e1", "u1", "x", Priority.NORMAL)));
    }

    @Test @DisplayName("1000 distinct events are handled quickly")
    void manyEvents() {
        Fakes f = new Fakes(); NotificationService s = f.service();
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            for (int i = 0; i < 1000; i++) s.handle(req("e" + i, "u" + (i % 10), "charging-started", Priority.NORMAL));
        });
        assertEquals(2000, f.sent.size());
    }
}

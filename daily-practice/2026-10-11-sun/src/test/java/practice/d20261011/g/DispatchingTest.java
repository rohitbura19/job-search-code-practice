package practice.d20261011.g;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DispatchingTest {

    static final Recipient R = new Recipient("u1", "tok", "+911234567890", "u1@example.com");
    static final RenderedMessage M = new RenderedMessage("subj", "body");

    /** Fails the first `failures` calls with `error`, then succeeds. */
    static class FlakySender implements ChannelSender {
        final Channel ch; int failures; final RuntimeException error; int calls = 0; final String id;
        FlakySender(Channel ch, String id, int failures, RuntimeException error) { this.ch = ch; this.id = id; this.failures = failures; this.error = error; }
        public Channel channel() { return ch; }
        public SendResult send(Recipient r, RenderedMessage m) {
            calls++;
            if (calls <= failures) throw error;
            return new SendResult(true, id);
        }
    }

    static class RecordingSleeper implements Sleeper {
        final List<Duration> slept = new ArrayList<>();
        public void sleep(Duration d) { slept.add(d); }
    }

    @Test @DisplayName("RetryPolicy.shouldRetry: transient errors up to 4 failed attempts, never a permanent failure")
    void shouldRetry() {
        RetryPolicy p = new RetryPolicy();
        TransientFailure t = new TransientFailure("timeout");
        for (int attempt = 1; attempt <= 4; attempt++) assertTrue(p.shouldRetry(attempt, t), "attempt " + attempt);
        assertFalse(p.shouldRetry(5, t));
        assertFalse(p.shouldRetry(9, t));
        assertFalse(p.shouldRetry(1, new PermanentFailure("bad number")));
        assertTrue(p.shouldRetry(1, new IllegalStateException("anything else is retried")));
    }

    @Test @DisplayName("RetryPolicy.delay: 200ms * 2^attempt * [0.5, 1.5)")
    void delayRange() {
        RetryPolicy p = new RetryPolicy();
        for (int attempt = 0; attempt <= 6; attempt++) {
            long base = 200L << attempt;
            long min = base / 2, max = base * 3 / 2;
            boolean differs = false; long first = p.delay(attempt).toMillis();
            for (int i = 0; i < 200; i++) {
                long ms = p.delay(attempt).toMillis();
                assertTrue(ms >= min && ms <= max, "attempt " + attempt + " gave " + ms + " ms, expected " + min + ".." + max);
                if (ms != first) differs = true;
            }
            assertTrue(differs, "the delay must be jittered, not constant");
        }
    }

    @Test @DisplayName("SenderRegistry looks senders up by channel")
    void registryLookup() {
        ChannelSender sms = new FlakySender(Channel.SMS, "sms", 0, null);
        ChannelSender email = new FlakySender(Channel.EMAIL, "email", 0, null);
        SenderRegistry reg = new SenderRegistry(List.of(sms, email));
        assertSame(sms, reg.get(Channel.SMS));
        assertSame(email, reg.get(Channel.EMAIL));
    }

    @Test @DisplayName("SenderRegistry: unknown channel and duplicate channel are errors")
    void registryErrors() {
        SenderRegistry reg = new SenderRegistry(List.of(new FlakySender(Channel.SMS, "sms", 0, null)));
        assertThrows(IllegalArgumentException.class, () -> reg.get(Channel.PUSH));
        assertThrows(IllegalStateException.class, () -> new SenderRegistry(List.of(
            new FlakySender(Channel.SMS, "a", 0, null), new FlakySender(Channel.SMS, "b", 0, null))));
    }

    @Test @DisplayName("Dispatcher: success on the first try never sleeps")
    void dispatchFirstTry() {
        RecordingSleeper sl = new RecordingSleeper(); FlakySender s = new FlakySender(Channel.SMS, "ok", 0, null);
        SendResult res = new Dispatcher(new RetryPolicy(), sl).dispatchWithRetry(s, R, M);
        assertEquals("ok", res.providerId());
        assertEquals(1, s.calls);
        assertTrue(sl.slept.isEmpty());
    }

    @Test @DisplayName("Dispatcher: two transient failures then success = 3 calls and 2 growing sleeps")
    void dispatchRetries() {
        RecordingSleeper sl = new RecordingSleeper(); FlakySender s = new FlakySender(Channel.SMS, "ok", 2, new TransientFailure("5xx"));
        SendResult res = new Dispatcher(new RetryPolicy(), sl).dispatchWithRetry(s, R, M);
        assertTrue(res.delivered());
        assertEquals(3, s.calls);
        assertEquals(2, sl.slept.size());
        assertTrue(sl.slept.get(0).toMillis() >= 200 && sl.slept.get(0).toMillis() <= 600, "first delay is about 400 ms +- 50%: " + sl.slept.get(0));
        assertTrue(sl.slept.get(1).toMillis() >= 400 && sl.slept.get(1).toMillis() <= 1200, "second delay is about 800 ms +- 50%: " + sl.slept.get(1));
    }

    @Test @DisplayName("Dispatcher: gives up after 5 attempts and rethrows the last error")
    void dispatchGivesUp() {
        RecordingSleeper sl = new RecordingSleeper(); TransientFailure boom = new TransientFailure("always down");
        FlakySender s = new FlakySender(Channel.SMS, "never", 100, boom);
        TransientFailure thrown = assertThrows(TransientFailure.class, () -> new Dispatcher(new RetryPolicy(), sl).dispatchWithRetry(s, R, M));
        assertSame(boom, thrown);
        assertEquals(5, s.calls);
        assertEquals(4, sl.slept.size());
    }

    @Test @DisplayName("Dispatcher: a permanent failure is not retried")
    void dispatchPermanent() {
        RecordingSleeper sl = new RecordingSleeper(); FlakySender s = new FlakySender(Channel.SMS, "x", 100, new PermanentFailure("unsubscribed"));
        assertThrows(PermanentFailure.class, () -> new Dispatcher(new RetryPolicy(), sl).dispatchWithRetry(s, R, M));
        assertEquals(1, s.calls);
        assertTrue(sl.slept.isEmpty());
    }

    @Test @DisplayName("FailoverSender: first provider wins when healthy, second is not called")
    void failoverHealthy() {
        FlakySender a = new FlakySender(Channel.SMS, "vendorA", 0, null), b = new FlakySender(Channel.SMS, "vendorB", 0, null);
        FailoverSender f = new FailoverSender(Channel.SMS, List.of(a, b));
        assertEquals(Channel.SMS, f.channel());
        assertEquals("vendorA", f.send(R, M).providerId());
        assertEquals(0, b.calls);
    }

    @Test @DisplayName("FailoverSender: transient failure moves to the next provider")
    void failoverNext() {
        FlakySender a = new FlakySender(Channel.SMS, "vendorA", 1, new TransientFailure("A down")), b = new FlakySender(Channel.SMS, "vendorB", 0, null);
        assertEquals("vendorB", new FailoverSender(Channel.SMS, List.of(a, b)).send(R, M).providerId());
        assertEquals(1, a.calls);
        assertEquals(1, b.calls);
    }

    @Test @DisplayName("FailoverSender: permanent failure stops the chain; all down rethrows the last transient failure")
    void failoverErrors() {
        FlakySender a = new FlakySender(Channel.SMS, "A", 1, new PermanentFailure("invalid number")), b = new FlakySender(Channel.SMS, "B", 0, null);
        assertThrows(PermanentFailure.class, () -> new FailoverSender(Channel.SMS, List.of(a, b)).send(R, M));
        assertEquals(0, b.calls);

        TransientFailure ea = new TransientFailure("A down"), eb = new TransientFailure("B down");
        FailoverSender all = new FailoverSender(Channel.SMS, List.of(
            new FlakySender(Channel.SMS, "A", 1, ea), new FlakySender(Channel.SMS, "B", 1, eb)));
        assertSame(eb, assertThrows(TransientFailure.class, () -> all.send(R, M)));
    }
}

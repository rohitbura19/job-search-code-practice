package practice.d20261014.h2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotifierDecoratorTest {

    /** Fails the first 'failures' calls with an IllegalStateException, then succeeds. Records every call. */
    static final class Flaky implements Notifier {
        final List<String> calls = new ArrayList<>(); final int failures;
        Flaky(int failures) { this.failures = failures; }
        @Override public void send(String message) {
            calls.add(message);
            if (calls.size() <= failures) throw new IllegalStateException("boom " + calls.size());
        }
    }

    @Test @DisplayName("logging: before and after around a successful call")
    void loggingOk() {
        List<String> log = new ArrayList<>();
        new LoggingNotifier(new Flaky(0), log).send("hi");
        assertEquals("[before:hi, after:hi]", log.toString());
    }

    @Test @DisplayName("logging: failed instead of after, and the same exception is rethrown")
    void loggingFail() {
        List<String> log = new ArrayList<>();
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> new LoggingNotifier(new Flaky(1), log).send("hi"));
        assertEquals("boom 1", e.getMessage());
        assertEquals("[before:hi, failed:hi]", log.toString());
    }

    @Test @DisplayName("retrying: succeeds on the third attempt")
    void retrySucceeds() {
        Flaky f = new Flaky(2);
        new RetryingNotifier(f, 3).send("x");
        assertEquals("[x, x, x]", f.calls.toString());
    }

    @Test @DisplayName("retrying: gives up after maxAttempts and rethrows the LAST exception")
    void retryGivesUp() {
        Flaky f = new Flaky(10);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> new RetryingNotifier(f, 3).send("x"));
        assertEquals("boom 3", e.getMessage());
        assertEquals(3, f.calls.size());
    }

    @Test @DisplayName("retrying: no retry after success, and maxAttempts must be at least 1")
    void retryEdges() {
        Flaky f = new Flaky(0);
        new RetryingNotifier(f, 5).send("x");
        assertEquals(1, f.calls.size());
        assertThrows(IllegalArgumentException.class, () -> new RetryingNotifier(f, 0));
    }

    @Test @DisplayName("metric: counts sent and failed, rethrows")
    void metric() {
        MetricNotifier m = new MetricNotifier(new Flaky(1));
        assertThrows(IllegalStateException.class, () -> m.send("a"));
        m.send("b"); m.send("c");
        assertEquals(2, m.sent());
        assertEquals(1, m.failed());
    }

    @Test @DisplayName("composition order matters: Metric(Retry(Logging(real)))")
    void metricOutside() {
        List<String> log = new ArrayList<>();
        MetricNotifier m = new MetricNotifier(new RetryingNotifier(new LoggingNotifier(new Flaky(2), log), 3));
        m.send("n1");
        assertEquals("[before:n1, failed:n1, before:n1, failed:n1, before:n1, after:n1]", log.toString());
        assertEquals(1, m.sent());
        assertEquals(0, m.failed());
    }

    @Test @DisplayName("composition order matters: Retry(Metric(Logging(real))) sees every attempt")
    void metricInside() {
        List<String> log = new ArrayList<>();
        MetricNotifier m = new MetricNotifier(new LoggingNotifier(new Flaky(2), log));
        new RetryingNotifier(m, 3).send("n1");
        assertEquals(1, m.sent());
        assertEquals(2, m.failed());
    }

    @Test @DisplayName("Logging outside Retry logs ONE before and ONE failed when all attempts fail")
    void loggingOutside() {
        List<String> log = new ArrayList<>();
        Flaky f = new Flaky(10);
        assertThrows(IllegalStateException.class, () -> new LoggingNotifier(new RetryingNotifier(f, 2), log).send("z"));
        assertEquals("[before:z, failed:z]", log.toString());
        assertEquals(2, f.calls.size());
    }
}

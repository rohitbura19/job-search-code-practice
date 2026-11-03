package practice.e;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/** E - OutboxRelay: publish in order, stop at the first failure, count failures. Mocks only, no broker. Run: mvn test */
class OutboxRelayTest {

    static final Instant NOW = Instant.parse("2026-10-14T10:00:00Z");
    static final Clock CLOCK = Clock.fixed(NOW, ZoneOffset.UTC);

    final OutboxRepository repo = mock(OutboxRepository.class);
    final SimpleMeterRegistry registry = new SimpleMeterRegistry();

    @SuppressWarnings("unchecked")
    final KafkaTemplate<String, String> kafka = mock(KafkaTemplate.class);

    OutboxRelay relay(int batchSize) {
        return new OutboxRelay(kafka, repo, new OutboxProps("alerts", batchSize), CLOCK, registry);
    }

    static OutboxEvent row(long id, String site) {
        OutboxEvent e = new OutboxEvent();
        e.setId(id); e.setAggregateId(site); e.setPayload("{\"n\":" + id + "}"); e.setCreatedAt(NOW.minusSeconds(60));
        return e;
    }

    static CompletableFuture<SendResult<String, String>> ok() { return CompletableFuture.completedFuture(null); }
    static CompletableFuture<SendResult<String, String>> failed() {
        CompletableFuture<SendResult<String, String>> f = new CompletableFuture<>();
        f.completeExceptionally(new RuntimeException("broker down"));
        return f;
    }
    double failures() { return registry.get("outbox.relay.failures").counter().count(); }

    @Test @DisplayName("sends every row to the topic with siteId as key and payload as value, then marks sentAt")
    void allSucceed() {
        List<OutboxEvent> rows = List.of(row(1, "site-1"), row(2, "site-2"), row(3, "site-1"));
        when(repo.lockBatch(100)).thenReturn(rows);
        when(kafka.send(anyString(), anyString(), anyString())).thenAnswer(inv -> ok());
        relay(100).relay();
        verify(kafka).send("alerts", "site-1", "{\"n\":1}");
        verify(kafka).send("alerts", "site-2", "{\"n\":2}");
        verify(kafka).send("alerts", "site-1", "{\"n\":3}");
        for (OutboxEvent e : rows) assertEquals(NOW, e.getSentAt());
        assertEquals(0.0, failures(), 0.0);
    }

    @Test @DisplayName("asks the repository for batchSize rows")
    void batchSize() {
        when(repo.lockBatch(50)).thenReturn(List.of());
        relay(50).relay();
        verify(repo).lockBatch(50);
        verifyNoInteractions(kafka);
    }

    @Test @DisplayName("first failure stops the batch: earlier rows sent, failing and later rows untouched, counter = 1")
    void stopsAtFirstFailure() {
        List<OutboxEvent> rows = List.of(row(1, "site-1"), row(2, "site-2"), row(3, "site-3"));
        when(repo.lockBatch(100)).thenReturn(rows);
        when(kafka.send(eq("alerts"), eq("site-2"), anyString())).thenAnswer(inv -> failed());
        when(kafka.send(eq("alerts"), eq("site-1"), anyString())).thenAnswer(inv -> ok());
        when(kafka.send(eq("alerts"), eq("site-3"), anyString())).thenAnswer(inv -> ok());
        relay(100).relay();
        assertEquals(NOW, rows.get(0).getSentAt());
        assertNull(rows.get(1).getSentAt());
        assertNull(rows.get(2).getSentAt());
        verify(kafka, never()).send(eq("alerts"), eq("site-3"), anyString());
        assertEquals(1.0, failures(), 0.0);
    }

    @Test @DisplayName("an ack that never arrives is a failure after about 5 seconds (it does not hang)")
    void ackTimeout() {
        List<OutboxEvent> rows = List.of(row(1, "site-1"), row(2, "site-2"));
        when(repo.lockBatch(100)).thenReturn(rows);
        when(kafka.send(anyString(), anyString(), anyString())).thenAnswer(inv -> new CompletableFuture<SendResult<String, String>>());
        assertTimeoutPreemptively(Duration.ofSeconds(9), () -> relay(100).relay());
        assertNull(rows.get(0).getSentAt());
        assertNull(rows.get(1).getSentAt());
        assertEquals(1.0, failures(), 0.0);
        verify(kafka, times(1)).send(anyString(), anyString(), anyString());
    }

    @Test @DisplayName("failures accumulate across runs")
    void counterAccumulates() {
        when(repo.lockBatch(100)).thenReturn(List.of(row(1, "site-1")));
        when(kafka.send(anyString(), anyString(), anyString())).thenAnswer(inv -> failed());
        OutboxRelay r = relay(100);
        r.relay(); r.relay();
        assertEquals(2.0, failures(), 0.0);
    }

    @Test @DisplayName("relay() is @Scheduled every 500 ms and @Transactional (the row locks live for the whole method)")
    void annotations() throws Exception {
        var m = OutboxRelay.class.getMethod("relay");
        Scheduled s = m.getAnnotation(Scheduled.class);
        assertNotNull(s, "add @Scheduled");
        long ms = s.fixedDelay() >= 0 ? s.fixedDelay()
                : s.fixedRate() >= 0 ? s.fixedRate()
                : millis(s.fixedDelayString().isEmpty() ? s.fixedRateString() : s.fixedDelayString());
        assertEquals(500L, ms);
        assertTrue(m.isAnnotationPresent(Transactional.class) || OutboxRelay.class.isAnnotationPresent(Transactional.class),
                "add @Transactional");
    }

    static long millis(String text) {
        if (text.isEmpty()) return -1;
        try { return Duration.parse(text).toMillis(); } catch (RuntimeException e) { return Long.parseLong(text); }
    }
}

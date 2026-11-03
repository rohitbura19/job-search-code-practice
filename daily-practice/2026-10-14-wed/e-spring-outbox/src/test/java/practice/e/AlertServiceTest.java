package practice.e;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/** E - AlertService: Alert + outbox row, one transaction. Plain unit test, mocks only. Run: mvn test */
class AlertServiceTest {

    static final Clock CLOCK = Clock.fixed(Instant.parse("2026-10-14T10:00:00Z"), ZoneOffset.UTC);
    static final AlertRequest REQ = new AlertRequest("site-1", Alert.Severity.CRITICAL, "inverter offline");

    final AlertRepository alerts = mock(AlertRepository.class);
    final OutboxRepository outbox = mock(OutboxRepository.class);

    AlertService service(ObjectMapper mapper) {
        when(alerts.save(any(Alert.class))).thenAnswer(inv -> inv.getArgument(0));
        return new AlertService(alerts, outbox, mapper, CLOCK);
    }

    @Test @DisplayName("saves the Alert with the request fields and the clock time, and returns it")
    void savesAlert() {
        Alert result = service(new ObjectMapper()).create(REQ);
        ArgumentCaptor<Alert> captor = ArgumentCaptor.forClass(Alert.class);
        verify(alerts).save(captor.capture());
        Alert saved = captor.getValue();
        assertEquals("site-1", saved.getSiteId());
        assertEquals(Alert.Severity.CRITICAL, saved.getSeverity());
        assertEquals("inverter offline", saved.getMessage());
        assertEquals(Instant.parse("2026-10-14T10:00:00Z"), saved.getCreatedAt());
        assertNotNull(saved.getId());
        assertSame(saved, result);
    }

    @Test @DisplayName("saves ONE outbox row keyed by siteId whose payload is the alert as JSON; alert is saved first")
    void savesOutbox() throws Exception {
        Alert result = service(new ObjectMapper()).create(REQ);
        ArgumentCaptor<OutboxEvent> captor = ArgumentCaptor.forClass(OutboxEvent.class);
        verify(outbox, times(1)).save(captor.capture());
        OutboxEvent e = captor.getValue();
        assertEquals("site-1", e.getAggregateId());
        assertEquals(Instant.parse("2026-10-14T10:00:00Z"), e.getCreatedAt());
        assertNull(e.getSentAt());
        JsonNode json = new ObjectMapper().readTree(e.getPayload());
        assertEquals(result.getId().toString(), json.get("id").asText());
        assertEquals("site-1", json.get("siteId").asText());
        assertEquals("CRITICAL", json.get("severity").asText());
        assertEquals("inverter offline", json.get("message").asText());
        InOrder order = inOrder(alerts, outbox);
        order.verify(alerts).save(any(Alert.class));
        order.verify(outbox).save(any(OutboxEvent.class));
    }

    @Test @DisplayName("if the JSON cannot be written: IllegalStateException and NO outbox row is saved")
    void jsonFailure() throws Exception {
        ObjectMapper broken = mock(ObjectMapper.class);
        when(broken.writeValueAsString(any())).thenThrow(new JsonProcessingException("cannot write") { });
        assertThrows(IllegalStateException.class, () -> service(broken).create(REQ));
        verify(outbox, never()).save(any(OutboxEvent.class));
    }

    @Test @DisplayName("create() runs in a transaction (the annotation is what makes both rows commit or roll back together)")
    void transactional() throws Exception {
        assertTrue(AlertService.class.getMethod("create", AlertRequest.class).isAnnotationPresent(Transactional.class)
                || AlertService.class.isAnnotationPresent(Transactional.class));
    }
}

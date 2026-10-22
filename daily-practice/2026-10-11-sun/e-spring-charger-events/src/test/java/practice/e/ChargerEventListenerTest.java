package practice.e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/** E - manual acknowledgement order and the listener wiring, with no broker. */
class ChargerEventListenerTest {

    static final EventDto DTO = new EventDto("evt-1", "CH-1", ChargerEvent.Status.AVAILABLE, 0, Instant.parse("2026-10-11T09:00:00Z"));

    @Test @DisplayName("saves first, acknowledges only afterwards")
    void ackAfterSave() {
        ChargerEventService service = mock(ChargerEventService.class);
        Acknowledgment ack = mock(Acknowledgment.class);
        new ChargerEventListener(service).onMessage(DTO, ack);
        var order = inOrder(service, ack);
        order.verify(service).save(DTO);
        order.verify(ack).acknowledge();
    }

    @Test @DisplayName("if the save fails the message is NOT acknowledged and the exception propagates")
    void noAckOnFailure() {
        ChargerEventService service = mock(ChargerEventService.class);
        Acknowledgment ack = mock(Acknowledgment.class);
        doThrow(new IllegalStateException("db down")).when(service).save(any(EventDto.class));
        assertThrows(IllegalStateException.class, () -> new ChargerEventListener(service).onMessage(DTO, ack));
        verify(ack, never()).acknowledge();
    }

    @Test @DisplayName("onMessage is a @KafkaListener on topic charger-events, group charger-svc")
    void listenerAnnotation() throws Exception {
        Method m = ChargerEventListener.class.getMethod("onMessage", EventDto.class, Acknowledgment.class);
        KafkaListener kl = m.getAnnotation(KafkaListener.class);
        assertNotNull(kl, "annotate onMessage with @KafkaListener");
        assertEquals(1, kl.topics().length);
        assertEquals("charger-events", kl.topics()[0]);
        assertEquals("charger-svc", kl.groupId());
    }

    @Test @DisplayName("the listener is a Spring component")
    void isComponent() {
        assertNotNull(ChargerEventListener.class.getAnnotation(Component.class));
    }
}

package practice.e;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CarrierListenerTest {
    ShipmentService service = mock(ShipmentService.class);
    CarrierListener listener = new CarrierListener(service, new ObjectMapper());

    @Test void validPayloadIsForwarded() {
        listener.onMessage("{\"eventId\":\"e1\",\"trackingId\":\"TRK-1\",\"status\":\"IN_TRANSIT\"}");
        verify(service).applyCarrierUpdate(new CarrierUpdate("e1", "TRK-1", ShipmentStatus.IN_TRANSIT));
    }

    @Test void malformedPayloadIsDroppedNotRetried() {
        listener.onMessage("not json");
        listener.onMessage("{\"eventId\":\"e1\",\"trackingId\":\"TRK-1\",\"status\":\"TELEPORTED\"}");
        verify(service, never()).applyCarrierUpdate(any());
    }

    @Test void serviceFailuresPropagateSoTheContainerCanRetry() {
        doThrow(new IllegalStateException("db down")).when(service).applyCarrierUpdate(any());
        assertThatThrownBy(() -> listener.onMessage("{\"eventId\":\"e1\",\"trackingId\":\"T\",\"status\":\"DELIVERED\"}")).isInstanceOf(IllegalStateException.class);
    }

    @Test void isWiredToTheConfiguredTopic() throws Exception {
        Method m = CarrierListener.class.getMethod("onMessage", String.class);
        KafkaListener ann = m.getAnnotation(KafkaListener.class);
        assertThat(ann).isNotNull();
        assertThat(ann.topics()).containsExactly("${shipments.carrier-topic}");
        assertThat(ann.groupId()).isEqualTo("${spring.application.name}");
        assertThat(CarrierListener.class.isAnnotationPresent(Component.class)).isTrue();
    }
}

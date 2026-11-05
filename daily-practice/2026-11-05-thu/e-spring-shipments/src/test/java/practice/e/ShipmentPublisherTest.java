package practice.e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

class ShipmentPublisherTest {
    @SuppressWarnings("unchecked")
    @Test void sendsJsonKeyedByOrderId() throws Exception {
        KafkaTemplate<String, String> template = mock(KafkaTemplate.class);
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        ShipmentPublisher publisher = new ShipmentPublisher(template, mapper, "shipments.events");
        Shipment s = new Shipment("o-7", "addr", "TRK-o-7", Instant.parse("2026-11-05T10:00:00Z"));

        publisher.publish(s);

        ArgumentCaptor<String> json = ArgumentCaptor.forClass(String.class);
        verify(template).send(eq("shipments.events"), eq("o-7"), json.capture());
        JsonNode node = mapper.readTree(json.getValue());
        assertThat(node.get("orderId").asText()).isEqualTo("o-7");
        assertThat(node.get("status").asText()).isEqualTo("CREATED");
        assertThat(node.has("at")).isTrue();
    }
}

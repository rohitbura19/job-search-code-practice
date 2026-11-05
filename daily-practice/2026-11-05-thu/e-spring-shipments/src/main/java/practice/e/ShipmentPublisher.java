package practice.e;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;

// TODO: @Component. The @Value constructor parameter reads "${shipments.topic}".
//  publish(s): build ShipmentEvent(s.getId(), s.getOrderId(), s.getStatus(), s.getUpdatedAt()), serialise with the ObjectMapper
//  (JsonProcessingException -> IllegalStateException), and kafkaTemplate.send(topic, s.getOrderId(), json). The key keeps all events of one
//  order on one partition, so they stay ordered. (ObjectMapper from Spring Boot already handles java.time via jackson-datatype-jsr310.)
public class ShipmentPublisher {
    public ShipmentPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, String topic) { throw new UnsupportedOperationException("TODO"); }

    public void publish(Shipment s) { throw new UnsupportedOperationException("TODO"); }
}

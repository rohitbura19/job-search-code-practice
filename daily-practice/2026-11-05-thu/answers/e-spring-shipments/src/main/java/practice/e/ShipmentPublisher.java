package practice.e;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ShipmentPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic;

    public ShipmentPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper, @Value("${shipments.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    public void publish(Shipment s) {
        ShipmentEvent event = new ShipmentEvent(s.getId(), s.getOrderId(), s.getStatus(), s.getUpdatedAt());
        try {
            kafkaTemplate.send(topic, s.getOrderId(), objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}

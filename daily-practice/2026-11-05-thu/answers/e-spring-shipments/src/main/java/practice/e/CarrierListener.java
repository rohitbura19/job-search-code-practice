package practice.e;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CarrierListener {
    private static final Logger log = LoggerFactory.getLogger(CarrierListener.class);
    private final ShipmentService service;
    private final ObjectMapper objectMapper;

    public CarrierListener(ShipmentService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${shipments.carrier-topic}", groupId = "${spring.application.name}")
    public void onMessage(String payload) {
        CarrierUpdate update;
        try {
            update = objectMapper.readValue(payload, CarrierUpdate.class);
        } catch (JsonProcessingException e) {
            log.warn("Dropping malformed carrier message: {}", payload);
            return;
        }
        service.applyCarrierUpdate(update);
    }
}

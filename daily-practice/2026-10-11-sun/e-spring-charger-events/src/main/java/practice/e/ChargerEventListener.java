package practice.e;

import org.springframework.kafka.support.Acknowledgment;

/**
 * TODO: a Spring @Component. Constructor injection of the service.
 * onMessage: @KafkaListener(topics = "charger-events", groupId = "charger-svc"); call service.save(dto) and ONLY THEN ack.acknowledge().
 * If the service throws, do not ack and let the exception propagate (the container's error handler decides about retry / DLT).
 */
public class ChargerEventListener {
    public ChargerEventListener(ChargerEventService service) {
        // TODO
    }

    public void onMessage(EventDto dto, Acknowledgment ack) {
        throw new UnsupportedOperationException("TODO");
    }
}

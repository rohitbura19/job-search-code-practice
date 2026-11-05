package practice.e;

import com.fasterxml.jackson.databind.ObjectMapper;

// TODO: @Component. onMessage is a @KafkaListener(topics = "${shipments.carrier-topic}", groupId = "${spring.application.name}").
//  Parse the JSON payload into CarrierUpdate with the ObjectMapper and call service.applyCarrierUpdate.
//  A malformed payload can never succeed on retry: log a warning and return (drop it; in real life send it to a dead-letter topic).
//  Do NOT catch exceptions thrown by the service: let them propagate so the container's error handler retries/backs off.
public class CarrierListener {
    public CarrierListener(ShipmentService service, ObjectMapper objectMapper) { throw new UnsupportedOperationException("TODO"); }

    public void onMessage(String payload) { throw new UnsupportedOperationException("TODO"); }
}

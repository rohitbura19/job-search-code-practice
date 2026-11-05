package practice.e;

import java.time.Instant;

/** Given. Message written to the shipments topic (JSON), keyed by orderId. */
public record ShipmentEvent(Long shipmentId, String orderId, ShipmentStatus status, Instant at) {}

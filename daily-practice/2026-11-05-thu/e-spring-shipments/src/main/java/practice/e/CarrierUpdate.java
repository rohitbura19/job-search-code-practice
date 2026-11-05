package practice.e;

/** Given. Message read from the carrier topic (JSON). */
public record CarrierUpdate(String eventId, String trackingId, ShipmentStatus status) {}

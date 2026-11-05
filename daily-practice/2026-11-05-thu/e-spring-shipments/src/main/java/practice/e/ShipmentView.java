package practice.e;

import java.time.Instant;

/** Given. */
public record ShipmentView(Long id, String orderId, String address, ShipmentStatus status, String trackingId, Instant updatedAt) {
    public static ShipmentView from(Shipment s) {
        return new ShipmentView(s.getId(), s.getOrderId(), s.getAddress(), s.getStatus(), s.getTrackingId(), s.getUpdatedAt());
    }
}

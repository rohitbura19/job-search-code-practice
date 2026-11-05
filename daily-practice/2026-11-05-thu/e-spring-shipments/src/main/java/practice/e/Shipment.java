package practice.e;

import java.time.Instant;

// TODO: JPA mapping. Nothing here is annotated yet.
//  @Entity; id: @Id + @GeneratedValue(IDENTITY); orderId: unique, not null, length 40;
//  address: not null, length 200; status: @Enumerated(STRING), not null; trackingId: unique, not null;
//  updatedAt: not null; version: @Version (optimistic locking). Keep the protected no-arg constructor (JPA needs it).
public class Shipment {
    private Long id;
    private String orderId;
    private String address;
    private ShipmentStatus status;
    private String trackingId;
    private Instant updatedAt;
    private long version;

    protected Shipment() {}

    public Shipment(String orderId, String address, String trackingId, Instant now) {
        this.orderId = orderId;
        this.address = address;
        this.trackingId = trackingId;
        this.status = ShipmentStatus.CREATED;
        this.updatedAt = now;
    }

    /** Moves forward only. Returns true if the status changed. */
    public boolean advanceTo(ShipmentStatus next, Instant now) {
        if (next.ordinal() <= status.ordinal()) return false;
        status = next;
        updatedAt = now;
        return true;
    }

    public Long getId() { return id; }
    public String getOrderId() { return orderId; }
    public String getAddress() { return address; }
    public ShipmentStatus getStatus() { return status; }
    public String getTrackingId() { return trackingId; }
    public Instant getUpdatedAt() { return updatedAt; }
    public long getVersion() { return version; }
}

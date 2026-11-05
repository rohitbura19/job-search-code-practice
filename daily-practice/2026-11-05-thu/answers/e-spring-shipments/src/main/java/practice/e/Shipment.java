package practice.e;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import java.time.Instant;

@Entity
public class Shipment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 40) private String orderId;
    @Column(nullable = false, length = 200) private String address;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private ShipmentStatus status;
    @Column(nullable = false, unique = true) private String trackingId;
    @Column(nullable = false) private Instant updatedAt;
    @Version private long version;

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

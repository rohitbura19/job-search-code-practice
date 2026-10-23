package practice.e;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "slot", uniqueConstraints = @UniqueConstraint(name = "uk_slot_station_start", columnNames = {"stationId", "startsAt"}))
public class Slot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 40)
    private String stationId;
    @Column(nullable = false)
    private Instant startsAt;
    @Column(nullable = false)
    private int capacity;
    @Column(nullable = false)
    private int booked;
    @Version
    private Long version;      // bumped on every entity flush that changes the row; a bulk JPQL update does NOT touch it unless "update versioned"

    protected Slot() {}

    public Slot(String stationId, Instant startsAt, int capacity) {
        this.stationId = stationId;
        this.startsAt = startsAt;
        this.capacity = capacity;
    }

    public Long getId() { return id; }
    public String getStationId() { return stationId; }
    public Instant getStartsAt() { return startsAt; }
    public int getCapacity() { return capacity; }
    public int getBooked() { return booked; }
    public Long getVersion() { return version; }
}

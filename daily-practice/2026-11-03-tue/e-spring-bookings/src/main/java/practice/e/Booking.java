package practice.e;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;

/** Given. Half-open interval [startAt, endAt): a booking ending at 10:00 does not clash with one starting at 10:00. */
@Entity
@Table(name = "bookings", indexes = @Index(name = "ix_room_start", columnList = "room,startAt"))
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 40) private String room;
    @Column(nullable = false) private Instant startAt;
    @Column(nullable = false) private Instant endAt;
    @Column(nullable = false) private String bookedBy;

    protected Booking() {}

    public Booking(String room, Instant startAt, Instant endAt, String bookedBy) {
        this.room = room; this.startAt = startAt; this.endAt = endAt; this.bookedBy = bookedBy;
    }

    public Long getId() { return id; }
    public String getRoom() { return room; }
    public Instant getStartAt() { return startAt; }
    public Instant getEndAt() { return endAt; }
    public String getBookedBy() { return bookedBy; }
}

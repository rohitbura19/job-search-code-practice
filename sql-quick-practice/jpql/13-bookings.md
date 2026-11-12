# 13. bookings - repository queries

_From daily-practice/2026-11-03-tue. E. Spring Boot: room booking API with overlap check and paging (30 min)_

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-11-03-tue/answers/`.

## Entity `Booking`

```java
@Entity
@Table(name = "bookings", indexes = @Index(name = "ix_room_start", columnList = "room,startAt"))
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 40) private String room;
    @Column(nullable = false) private Instant startAt;
    @Column(nullable = false) private Instant endAt;
    @Column(nullable = false) private String bookedBy;
```

## `BookingRepository`

```java
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // TODO: true iff a booking in `room` overlaps the half-open interval [start, end).
    //  Overlap means existing.startAt < end AND existing.endAt > start. Explicit JPQL @Query with @Param, returning "count(b) > 0".
    boolean existsOverlap(String room, Instant start, Instant end);
    // TODO: bookings of `room` that end after `since`, ordered by startAt ascending, paged.
    //  Either a derived query name or @Query (then add a countQuery).
    Page<Booking> upcoming(String room, Instant since, Pageable pageable);
    // TODO: bulk delete of bookings that ended before `cutoff`; returns the number of rows deleted.
    //  Needs @Modifying (and @Query); callers must run it inside a transaction.
    int purgeEndedBefore(Instant cutoff);
}
```

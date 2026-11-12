# 02. slot-booking - repository queries

_From daily-practice/2026-10-23-fri. > Project: `e-spring-slot-booking/` (own `pom.xml`). Tests: `SlotRepositoryTest` (`@DataJpaTest`), `SlotControllerTest` (`@WebMvcTest`), `SlotServiceTest` (Mockito)._

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-10-23-fri/answers/`.

## Entity `Slot`

```java
    private Long id;
    private String stationId;
    private Instant startsAt;
    private int capacity;
    private int booked;
```

## `SlotRepository`

```java
public interface SlotRepository extends JpaRepository<Slot, Long> {
    // TODO 1: slots of one station that start at or after :from and still have room (booked < capacity), earliest first.
    @Query("TODO")
    List<Slot> available(@Param("stationId") String stationId, @Param("from") Instant from);
    // TODO 2: ATOMIC reservation in one statement: booked = booked + 1 where the id matches AND booked < capacity.
    //   It returns the number of rows changed (1 = reserved, 0 = full or missing). Why is this safer than read-then-save?
    //   Which @Modifying attribute keeps a later findById from returning a stale entity, and what does it cost?
    @Query("TODO")
    int reserve(@Param("id") long id);
}
```

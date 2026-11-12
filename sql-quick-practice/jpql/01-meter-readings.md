# 01. meter-readings - repository queries

_From daily-practice/2026-10-22-thu. > Project: `e-spring-meter-readings/` (Spring Boot 3.3, Java 21, JPA + H2 for tests). Files with TODOs: `MeterReading`, `MeterReadingRepository`, `ReadingRequest`, `ReadingController`, `ApiExceptionHandler`, `application.yml`._

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-10-22-thu/answers/`.

## Entity `MeterReading`

```java
    private Long id;
    private String meterId;
    private BigDecimal kwh;
    private Instant readAt;
```

## `MeterReadingRepository`

```java
// TODO 1: the first two method NAMES are already derived-query names. Replace the @Query("TODO") on them: delete it, then say
//   why Spring Data can build the query from the name, whether "Between" includes both ends, and what the Pageable on a List does.
// TODO 2: write the JPQL for totalKwh: the sum of kwh of one meter, and 0 (not null) for a meter with no readings.
public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {
    @Query("TODO")
    List<MeterReading> findByMeterIdAndReadAtBetweenOrderByReadAtAsc(String meterId, Instant from, Instant to);
    @Query("TODO")
    List<MeterReading> findByMeterIdOrderByReadAtDesc(String meterId, Pageable page);
    @Query("TODO")
    BigDecimal totalKwh(@Param("meterId") String meterId);
}
```

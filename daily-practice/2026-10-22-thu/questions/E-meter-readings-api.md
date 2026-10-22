> Project: `e-spring-meter-readings/` (Spring Boot 3.3, Java 21, JPA + H2 for tests). Files with TODOs: `MeterReading`, `MeterReadingRepository`, `ReadingRequest`, `ReadingController`, `ApiExceptionHandler`, `application.yml`.
> Tests: `MeterReadingRepositoryTest` (`@DataJpaTest`) and `ReadingControllerTest` (`@WebMvcTest`). Run: `cd e-spring-meter-readings && mvn test`. **30 min, no IDE hints.**

# E. Smart-meter readings: JPA entity, derived and JPQL queries, validation, `ProblemDetail`

**Task.** Complete the TODOs. `POST /api/readings` stores a reading (201 + `Location` + body); `GET /api/meters/{id}/readings?from=&to=` returns the readings in an inclusive time range, oldest first; `GET /api/meters/{id}/latest?limit=` returns the newest ones (default 5, allowed 1 to 100, else 400); `GET /api/meters/{id}/total` returns the summed kWh (0 for an unknown meter). Bad input is `400 application/problem+json` with an `errors` array of `{field, message}` and the title `Validation failed`.

Say out loud while you write: what `@Entity` needs (no-arg constructor, id, no final fields); why `IDENTITY` and what it costs for batch inserts (compare `SEQUENCE` with `allocationSize`); why `BigDecimal`; what a composite index `(meterId, readAt)` serves; what `open-in-view: false` changes; why `@WebMvcTest` does not load the repository (and what `@MockBean` replaces); why `@DataJpaTest` rolls back each test; the N+1 problem (not present here: what would introduce it?).

Interviewer follow-up: 10^8 readings per day: partitioning, keyset pagination instead of `OFFSET`, batch inserts (`saveAll` + `hibernate.jdbc.batch_size`, why IDENTITY disables JDBC batching in Hibernate), idempotent ingestion (unique `(meterId, readAt)` and `ON CONFLICT DO NOTHING`), a Kafka consumer in front of the table, `@Transactional(readOnly = true)` on reads, time-zone handling (`Instant` in, `timestamptz` out).

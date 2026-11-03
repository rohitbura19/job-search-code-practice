# E. Spring Boot: room booking API with overlap check and paging (30 min)

> Project: `e-spring-bookings/` (own `pom.xml`; `cd e-spring-bookings && mvn test`). Tests: `BookingRepositoryTest` (`@DataJpaTest`), `BookingControllerTest` (`@WebMvcTest`), `BookingServiceTest` (Mockito + fixed `Clock`), `BookingPropsTest` (`ApplicationContextRunner`). **30 min.**

Complete the `TODO` gaps (the entity, view, exceptions and application class are given):

1. `BookingRepository`: JPQL `@Query` overlap test with `@Param`; paged `upcoming` query (derived or `@Query` + `countQuery`); `@Modifying` bulk delete.
2. `BookingRequest`: Bean Validation incl. a cross-field `@AssertTrue` rule.
3. `BookingProps`: `@ConfigurationProperties("booking")` record, `@Validated`, `@DefaultValue`, `@Min/@Max`.
4. `BookingService`: `@Service`, `@Transactional`, rules in order: in the past (422), too long (422), overlap (409).
5. `BookingController`: `201 + Location`, list with `X-Total-Count`, `204` on delete, `@Valid @RequestBody`.
6. `ApiExceptionHandler`: `@RestControllerAdvice` returning `ProblemDetail` (404 / 409 / 422).

Paper: why is `[10:00,11:00)` followed by `[11:00,12:00)` not a clash? Two requests for the same room arrive together; both pass `existsOverlap`. How do you make the second one fail (row lock, serializable, DB exclusion constraint)?

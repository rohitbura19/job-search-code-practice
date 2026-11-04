# E. Spring Boot: support tickets API (30 min)

> Project: `e-spring-tickets/` (own `pom.xml`; `cd e-spring-tickets && mvn test`). Tests: `StatusAndValidationTest` (enum + `Validator`), `TicketRepositoryTest` (`@DataJpaTest`), `TicketServiceTest` (Mockito + fixed `Clock`), `TicketControllerTest` (`@WebMvcTest`), `EscalationJobTest` (`ApplicationContextRunner`). **30 min.**

Complete the `TODO` gaps (entity, enums, view/page records, exceptions and application class are given):

1. `Status.canMoveTo`: the state machine as an exhaustive `switch` expression.
2. `NotAllCaps` + `NotAllCapsValidator`: a custom Bean Validation constraint (`@Constraint`, `ConstraintValidator`); `CreateTicketRequest` annotations.
3. `TicketRepository`: optional-filter `@Query` returning a `Slice`, interface projection with `group by`, `@Modifying(clearAutomatically = true)` bulk update.
4. `TicketService`: `@Service`, `@Transactional` (and `readOnly`), `@Value` with defaults, dirty checking instead of `save`.
5. `TicketController`: `201 + Location`, `PATCH` sub-resource, optional `@RequestParam`s with `defaultValue`.
6. `ApiExceptionHandler`: `ProblemDetail` for 404, 409 (illegal transition) and 409 (`ObjectOptimisticLockingFailureException`).
7. `EscalationJob`: `@ConditionalOnProperty` + `@Scheduled(cron = "${...}")`.

Paper: two agents press "resolve" and "close" on the same ticket at once; walk through what `@Version` does and what the client sees. Why does the bulk `escalateStale` need `clearAutomatically`, and what does it skip compared with loading and saving entities?

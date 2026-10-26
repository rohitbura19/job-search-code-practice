> Project: `e-spring-refunds/` (own `pom.xml`). Tests: `RefundRepositoryTest` (`@DataJpaTest`), `RefundControllerTest` (`@WebMvcTest`), `RefundServiceTest` (Mockito + fixed `Clock`), `RefundPropertiesTest` (`ApplicationContextRunner`).
> Run: `cd e-spring-refunds && mvn test`. Offline H2 only; no Kafka or Docker needed.

# E. Refunds: idempotency key, optimistic locking, paging, problem details (35 min)

Complete every `TODO` in this order, running the tests as you go:
1. `Refund` - `@Entity`, named unique constraint on the idempotency key, `@Enumerated(STRING)`, `@Version`. What does Hibernate add to the `UPDATE`? Test: `RefundRepositoryTest`.
2. `RefundRepository` - derived `findByIdempotencyKey`, paged `findByPaymentId`, and the JPQL `findStalePending` with an enum literal. `Page` vs `Slice`?
3. `CreateRefundRequest` - Bean Validation incl. `@Size`, `@Positive`. Why `Long`?
4. `RefundProperties` - validated `@ConfigurationProperties` record with defaults and a cross-field `@AssertTrue`. Test: `RefundPropertiesTest`.
5. `RefundService` - the idempotent `create` (replay vs key reuse vs the unique-constraint race), the transaction boundaries (which method must NOT be transactional, and why?), `decide`, `list`.
6. `RefundController` - `Idempotency-Key` header with `@Pattern` WITHOUT `@Validated` on the class, `201` vs `200` + `Idempotent-Replayed`, a `Pageable` parameter.
7. `ApiExceptionHandler` - `ProblemDetail` for body validation, **and** for `HandlerMethodValidationException` (find out in the test why the body errors take that route here), the business exception, and the optimistic-lock failure (409).
8. `application.yml` - answer on paper, then compare with `answers/`. The controller test needs the paging caps from it.

Say out loud: why a client-supplied key makes a POST safely retryable and what the server must compare on a replay; why the unique constraint, not the pre-check, is the guard; what `@Version` protects that a transaction alone does not (two reviewers click Approve/Reject); `409` vs `422` for the two kinds of conflict; why unbounded `?size=` is an outage waiting to happen.

# E - Spring Boot: subscriptions API (30 min) - project `e-spring-subscriptions/` (find `TODO(E`)

Boot 3.3, Java 21, H2. `application.yml` (given): `open-in-view: false`, `billing.period-days: 30`. A `Clock` bean is injected; tests freeze "now" (2026-11-10T08:00Z).

| Gap | File | Topic |
|---|---|---|
| E1a | `Account.java` | entity, unique column constraint |
| E1b | `Subscription.java` | `@ManyToOne(fetch = LAZY, optional = false)` + `@JoinColumn`, `@Enumerated(STRING)`, `@Table(indexes = ...)` |
| E2a-d | `SubscriptionRepository.java` | derived `existsBy`, nested-property derived query + `@EntityGraph`, JPQL interface projection with `group by`, derived `Between` query |
| E2e | `AccountRepository.java` | JPQL `not exists` correlated subquery |
| E3 | `CreateSubscriptionRequest.java` | `@NotBlank @Email @Size @NotNull` on record components |
| E4 | `SubscriptionService.java` | `@Service`, class-level read-only `@Transactional` with write overrides, get-or-create, idempotent cancel, `EnumMap` |
| E5 | `SubscriptionController.java` | `@RequestHeader(required = false)` echoed back, 201 + Location, 204 `DELETE`, `@Positive/@Min/@Max/@Email` on parameters, literal paths next to `/{id}` |
| E6 | `ApiExceptionHandler.java` | `ResponseEntityExceptionHandler` + `ProblemDetail`, `DataIntegrityViolationException` -> 409 without leaking SQL |
| E7 | `BillingProperties.java` | `@ConfigurationProperties` record, `@DefaultValue`, `@Validated` + range constraints (test with `ApplicationContextRunner`) |

Tests: `SubscriptionRepositoryTest` (`@DataJpaTest`), `SubscriptionServiceTest` (`@DataJpaTest` + `@Import`), `SubscriptionControllerTest` (`@WebMvcTest`, `@MockBean`), `BillingPropertiesTest`. The tests do not compile until you add the E2a method. Run: `cd e-spring-subscriptions && mvn test`. Do entities, repositories and properties first.

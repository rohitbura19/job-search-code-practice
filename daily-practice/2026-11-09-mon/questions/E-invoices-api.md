# E - Spring Boot: invoices API (30 min) - project `e-spring-invoices/` (find `TODO(E`)

Boot 3.3, Java 21, H2. `application.yml` (given): `open-in-view: false`. A `Clock` bean is injected so tests freeze "now" (2026-11-09T12:00Z).

| Gap | File | Topic |
|---|---|---|
| E1a | `InvoiceStatus.java` | enum state machine with a switch expression |
| E1b | `LineItem.java` | `@Embeddable` record with column names |
| E1c | `Invoice.java` | entity, `@Enumerated(STRING)`, `@ElementCollection` + `@CollectionTable` + `@OrderColumn` |
| E2 | `InvoiceRepository.java` | derived paged query, JPQL constructor expression with `sum`/`group by`, `@Modifying` bulk update (flush + clear), derived `findTop3By...OrderBy...Desc` (you add it) |
| E3 | `CreateInvoiceRequest.java` | Bean Validation incl. `List<@Valid Item>` cascade |
| E4 | `InvoiceService.java` | `@Service`, class-level read-only `@Transactional` with write overrides, idempotent transitions, `Clock` |
| E5 | `InvoiceController.java` | mappings, `@Valid`, `@Positive/@Min/@Max` on parameters, required enum param, 201 + Location, `X-Total-Count`, literal path vs `/{id}` |
| E6 | `ApiExceptionHandler.java` | `ResponseEntityExceptionHandler` + `ProblemDetail` for 404 / 409 |

Tests: `InvoiceRepositoryTest` (`@DataJpaTest`), `InvoiceServiceTest` (`@DataJpaTest` + `@Import`), `InvoiceControllerTest` (`@WebMvcTest`, `@MockBean`). The repository test does not compile until you add the E2d method. Run: `cd e-spring-invoices && mvn test`. Do entities and repository first.

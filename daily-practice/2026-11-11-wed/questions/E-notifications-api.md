# E - Spring Boot: notifications inbox API (30 min) - project `e-spring-notifications/` (find `TODO(E`)

Boot 3.3, Java 21, H2. `application.yml` (given): `open-in-view: false`, `inbox.stale-days: 30`. A `Clock` bean is injected; tests freeze "now" (2026-11-11T08:00Z).

| Gap | File | Topic |
|---|---|---|
| E1a | `Notification.java` | abstract `@Entity`, `@Inheritance(SINGLE_TABLE)`, `@DiscriminatorColumn`, `@Table(indexes)`, `@Column(name=...)` |
| E1b/c | `EmailNotification`, `SmsNotification` | `@DiscriminatorValue`, subclass columns that must stay nullable |
| E2a-d | `NotificationRepository.java` | derived paged queries with a tie-break order, `IsNull`, `countBy`, owner-scoped `findByIdAndUserId` |
| E2e | same | `@Modifying(clearAutomatically, flushAutomatically)` JPQL bulk update with `@Param` |
| E2f | same | JPQL `type(n) = EmailNotification` over the hierarchy |
| E3 | `SendRequest.java` | `@NotNull @Positive @NotBlank @Size` on record components |
| E4 | `NotificationService.java` | `@Service`, read-only `@Transactional` + write overrides, `@Value("${inbox.stale-days:30}")`, switch expression, dirty checking (no `save`) |
| E5 | `NotificationController.java` | required `@RequestHeader` + `@Positive`, `@RequestParam(defaultValue)` + `@Min/@Max`, 201 + Location, `@PatchMapping`, literal paths |
| E6 | `ApiExceptionHandler.java` | `@RestControllerAdvice` extending `ResponseEntityExceptionHandler`, `ProblemDetail` 404 / 422 |

Tests: `NotificationRepositoryTest` (`@DataJpaTest`), `NotificationServiceTest` (`@DataJpaTest` + `@Import` + `@TestPropertySource`), `NotificationControllerTest` (`@WebMvcTest`, `@MockBean`). The tests do not compile until the E2 methods exist. Run: `cd e-spring-notifications && mvn test`. Do entities, repository, then service, then web.

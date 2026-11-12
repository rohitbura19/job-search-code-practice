# E - Spring Boot: product reviews API (30 min) - project `e-spring-reviews/` (find `TODO(E`)

Boot 3.3, Java 21, H2. `application.yml` (given): `open-in-view: false`, `reviews.banned-words: [scam, spam]`, `reviews.max-page-size: 20`. A `Clock` bean exists. Given: `ReviewsApplication` (`@ConfigurationPropertiesScan`), `ReviewStatus`, `RatingCount` (interface projection), `DuplicateReviewException`, `Views` (records).

| Gap | File | Topic |
|---|---|---|
| E1 | `Review.java` | `@Entity`, `@Table(uniqueConstraints, indexes)`, `@Column`, `@Enumerated(STRING)`, IDENTITY id |
| E2a-c | `ReviewRepository.java` | derived paged query with tie-break order, `existsBy`, `countBy` |
| E2d-e | same | `@Query` JPQL `avg` (nullable `Double`) and a grouped interface projection with `@Param` |
| E3 | `ReviewProperties.java` | `@ConfigurationProperties` record, `@Validated`, `@DefaultValue`, `@Min/@Max` |
| E4 | `CreateReview.java` | `@Min @Max @NotBlank @Size` on record components |
| E5 | `ReviewService.java` | `@Service`, read-only `@Transactional` with write overrides, banned-word rule, page cap, rounded summary, owner check |
| E6 | `ReviewController.java` | header `@Positive`, `@Valid @RequestBody`, defaults + `@Min/@Max` on params, 201 + Location, 204 |
| E7 | `ReviewNotFoundException`, `NotOwnerException` | `@ResponseStatus` (with and without `reason`) |
| E8 | `ApiExceptionHandler.java` | `@RestControllerAdvice` extends `ResponseEntityExceptionHandler`, `ProblemDetail` 409 for duplicate and for `DataIntegrityViolationException` |

Tests: `ReviewRepositoryTest` (`@DataJpaTest`), `ReviewServiceTest` (`@DataJpaTest`, service built by hand), `ReviewPropertiesTest` (`ApplicationContextRunner`), `ReviewControllerTest` (`@WebMvcTest`, `@MockBean`). They do not compile until the E2 methods exist. Run: `cd e-spring-reviews && mvn test`. Order: entity, repository, properties, service, web.

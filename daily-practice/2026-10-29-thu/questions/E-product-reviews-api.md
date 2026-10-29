> Project: `e-spring-reviews/` (own `pom.xml`). Tests: `ReviewRepositoryTest` (`@DataJpaTest`), `ReviewControllerTest` (`@WebMvcTest`), `ReviewServiceTest` (Mockito + fixed `Clock`), `ReviewPropsTest` (`ApplicationContextRunner`).
> Run: `cd e-spring-reviews && mvn test`. Offline H2 only; no Kafka or Docker needed.

# E. Product reviews: unique constraint, JPQL aggregate, bulk update, validation, paging, problem details (35 min)

Complete every `TODO` in this order, running the tests as you go:
1. `Review` - entity: `@Table` with a table name that is not reserved, a NAMED unique constraint on `(product_id, author)` and an index, `IDENTITY` id, snake_case columns, `@Enumerated(STRING)`, nullability.
2. `ReviewRepository` - a derived paged query, a derived `exists`, a constructor-expression JPQL aggregate (`count`, `avg`) over PUBLISHED reviews, and a `@Modifying` bulk update. What do `clearAutomatically` and `flushAutomatically` fix? (Test: `ReviewRepositoryTest`.)
3. `CreateReviewRequest` - `@NotBlank`, `@Size`, `@Min`/`@Max`. Why is `rating` an `int` here, and what does a missing JSON number do?
4. `ReviewService` - `@Service`, class-level read-only transaction with write overrides, check-then-insert (friendly path only), the bulk update instead of load-modify-save. (Test: `ReviewServiceTest`.)
5. `ReviewController` - `201` + body, `@Valid @RequestBody`, `page` clamped to >= 0, `size` default 10 clamped to `1..props.maxPageSize()`, `204` on hide. (Test: `ReviewControllerTest`.)
6. `ApiExceptionHandler` - `@RestControllerAdvice`, `ProblemDetail` bodies, an `errors` map keyed by field, the lost race as 409 without SQL in `detail`.
7. `application.yml` - on paper, then compare with `answers/`. The controller test relies on `reviews.max-page-size`. `ReviewPropsTest` already passes.

Say out loud: why a bulk `UPDATE` is right for `hide` but wrong when the entity has callbacks, `@Version` or listeners; what `avg(int)` returns in JPQL and in SQL; why the unique constraint, not `existsBy...`, is the guard; offset paging cost and the keyset alternative; why `findByProductIdAndStatus` needs an index on `(product_id, status, created_at)` and in which order.

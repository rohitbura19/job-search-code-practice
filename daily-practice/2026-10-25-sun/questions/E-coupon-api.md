> Project: `e-spring-coupons/` (own `pom.xml`). Tests: `CouponRepositoryTest` (`@DataJpaTest`), `CouponControllerTest` (`@WebMvcTest`), `CouponServiceTest` (Mockito + fixed `Clock`), `CouponPropertiesTest` (`ApplicationContextRunner`).
> Run: `cd e-spring-coupons && mvn test`. Offline H2 only; no Kafka or Docker needed.

# E. Coupons: atomic redeem, clock, validation, one business exception (35 min)

Complete every `TODO` in this order, running the tests as you go:
1. `Coupon` - `@Entity`, named unique constraint on `code`, column constraints. Why no setter for `redemptions`? Test: `CouponRepositoryTest`.
2. `CouponRepository` - derived `findByCode` / `existsByCode` and the ONE conditional `@Modifying` update `redeem`. What do two concurrent redeems of the last slot do with read-check-save?
3. `CreateCouponRequest` - Bean Validation incl. `@Pattern`, `@Min/@Max`, `@Future`. Why `Integer`?
4. `CouponProperties` - validated `@ConfigurationProperties` record with defaults. Test: `CouponPropertiesTest`.
5. `CouponService` - transactions, limits from the properties, duplicate handling (check AND constraint), `redeem` that classifies a failed update (404 / 410 / 409) using the injected `Clock`.
6. `CouponController` - `201 + Location`, a `@Pattern` on the path variable WITHOUT `@Validated` on the class.
7. `ApiExceptionHandler` - `ProblemDetail` for body validation (override the base-class hook), the business exception and `IllegalArgumentException`.
8. `application.yml` - answer on paper, then compare with `answers/`.

Say out loud: why `LocalDate.now()` without a `Clock` makes the service untestable; why the update's `WHERE` clause is the lock; 409 vs 410 vs 422 and which one a client can retry; why the uniqueness check is a courtesy and the constraint is the guard.

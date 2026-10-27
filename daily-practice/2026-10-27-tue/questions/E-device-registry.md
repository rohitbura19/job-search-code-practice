> Project: `e-spring-devices/` (own `pom.xml`). Tests: `DeviceRepositoryTest` (`@DataJpaTest`), `DeviceControllerTest` (`@WebMvcTest`), `DeviceServiceTest` (Mockito + fixed `Clock`), `SerialValidatorTest` (plain JUnit).
> Run: `cd e-spring-devices && mvn test`. Offline H2 only; no Kafka or Docker needed.

# E. Device registry: custom constraint, embedded value, partial update, bulk update, problem details (35 min)

Complete every `TODO` in this order, running the tests as you go:
1. `ValidSerial` + `SerialValidator` - write a Bean Validation constraint annotation (`@Constraint`, `message`/`groups`/`payload`) and its `ConstraintValidator` (format + check digit; `null` is valid). Test: `SerialValidatorTest`.
2. `Firmware` + `Device` - `@Embeddable` record with explicit column names, `@Entity` with a NAMED unique constraint, `@Enumerated(STRING)`, protected no-arg constructor.
3. `DeviceRepository` - three derived queries (one through the embedded property) and a bulk `@Modifying` JPQL update. Why `clearAutomatically`? Test: `DeviceRepositoryTest`.
4. `RegisterDeviceRequest` / `PatchDeviceRequest` - validation annotations; why does `@Size` accept a missing value?
5. `DeviceService` - `@Transactional` boundaries (class level + `readOnly` reads), the check-then-insert and its race, `patch` with NO `save()` (dirty checking). Test: `DeviceServiceTest`.
6. `DeviceController` - `201` + `Location`, `PATCH`, query-param defaults (`ACTIVE`, `""`), `@Positive` on a request param WITHOUT `@Validated`.
7. `ApiExceptionHandler` - extend `ResponseEntityExceptionHandler`, override the body-validation hook to add an `errors` map, map the domain exceptions and `DataIntegrityViolationException` (the lost race) to 404 / 409.
8. `application.yml` - answer on paper, then compare with `answers/`. The controller test needs the JSON inclusion setting.

Say out loud: why the unique constraint, not the `existsBySerial` pre-check, is the guard; what a bulk update skips (entity lifecycle, dirty checking, `@Version`, the first-level cache); why `ORDINAL` enums are a trap; why a catch-all `@ExceptionHandler(Exception.class)` is worse than the base class.

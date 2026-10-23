> Project: `e-spring-slot-booking/` (own `pom.xml`). Tests: `SlotRepositoryTest` (`@DataJpaTest`), `SlotControllerTest` (`@WebMvcTest`), `SlotServiceTest` (Mockito).
> Run: `cd e-spring-slot-booking && mvn test`. Offline H2 only; no Kafka or Docker needed.

# E. Slot booking API: entity + atomic update + service + controller + ProblemDetail (35 min)

Complete every `TODO` (annotations, queries, bodies) in this order, running the tests as you go:
1. `Slot` - `@Entity`, unique constraint on `(stationId, startsAt)`, `@Version`, protected constructor. Test: `SlotRepositoryTest`.
2. `SlotRepository` - the `available` JPQL and the ATOMIC `reserve` update (`@Modifying`). Why is `update ... where booked < capacity` safer than read-then-save under concurrency?
3. `CreateSlotRequest` - Bean Validation. Why `Integer` and not `int` for `capacity`?
4. `SlotService` - `@Service`, `@Transactional` / `readOnly = true`; 0 rows updated means NotFound or Full.
5. `SlotController` - `@RestController`, `@RequestMapping`, `@Valid @RequestBody`, `201 + Location`, `@RequestParam` + `@DateTimeFormat`.
6. `ApiExceptionHandler` - `@RestControllerAdvice` returning `ProblemDetail` (400 with field errors, 404, 409). Which class must you extend to customise the validation error, and why does a plain `@ExceptionHandler` for it not work?
7. `application.yml` - answer the questions on paper, then compare with `answers/`.

Say out loud: why the controller never touches the repository; what `@Transactional` does on a `private` or self-invoked method; optimistic (`@Version`) vs pessimistic (`PESSIMISTIC_WRITE`) vs the atomic update here and when each wins; what `open-in-view` does.

> Project: `e-spring-orders/` (own `pom.xml`). Tests: `OrderRepositoryTest` (`@DataJpaTest`), `OrderControllerTest` (`@WebMvcTest`), `OrderServiceTest` (Mockito + fixed `Clock`), `OrderPropsTest` (`ApplicationContextRunner`).
> Run: `cd e-spring-orders && mvn test`. Offline H2 only; no Kafka or Docker needed.

# E. Purchase orders: one-to-many aggregate, entity graph, aggregate query, nested validation, paging, problem details (35 min)

Complete every `TODO` in this order, running the tests as you go:
1. `OrderLine` + `PurchaseOrder` - the bidirectional `@OneToMany(mappedBy)` / `@ManyToOne(LAZY)` pair, `cascade = ALL` + `orphanRemoval`, the helper that syncs both sides, a NAMED unique constraint, a table name that is not a reserved word, `@Enumerated(STRING)`, snake_case columns. Then `addLine`, `totalPaise`, `cancel`.
2. `PurchaseOrderRepository` - `@EntityGraph` for the single-order read, a derived paged query with `In`, and a JPQL constructor-expression aggregate (`select new ... sum(...) group by ... order by sum(...) desc`). Why does the list endpoint return summaries and not orders with lines? (Hibernate warns "HHH90003004: firstResult/maxResults specified with collection fetch; applying in memory".) Test: `OrderRepositoryTest`.
3. `LineRequest` / `CreateOrderRequest` - validation annotations, including cascading into the list elements. What happens if you forget `@Valid` on the element type?
4. `OrderService` - `@Service`, class-level `readOnly` transactions with write overrides, check-then-insert and its race, `cancel` WITHOUT `save()` (dirty checking). Test: `OrderServiceTest`.
5. `OrderController` - `201` + `Location`, `@Valid @RequestBody`, defaults and clamping of `size`/`n` (the max comes from `OrderProps`), an optional repeatable enum parameter. Test: `OrderControllerTest`.
6. `ApiExceptionHandler` - `@RestControllerAdvice`, `ProblemDetail` bodies, an `errors` map keyed by field path such as `lines[0].quantity`, the lost race (`DataIntegrityViolationException`) as 409 without leaking SQL.
7. `application.yml` - answer on paper, then compare with `answers/`. The controller test relies on `orders.max-page-size`. `OrderPropsTest` already passes (it shows relaxed binding).

Say out loud: what N+1 is and the three ways to avoid it (entity graph, fetch join, batch size); why `EAGER` `@ManyToOne` default is a trap; what `orphanRemoval` does that `cascade REMOVE` does not; why `sum(int * long)` is safe in JPQL here but the Java `int * long` needs care; why the unique constraint, not `existsByOrderNo`, is the guard.

# 05. refunds - repository queries

_From daily-practice/2026-10-26-mon. > Project: `e-spring-refunds/` (own `pom.xml`). Tests: `RefundRepositoryTest` (`@DataJpaTest`), `RefundControllerTest` (`@WebMvcTest`), `RefundServiceTest` (Mockito + fixed `Clock`), `RefundPropertiesTest` (`ApplicationContextRunner`)._

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-10-26-mon/answers/`.

## Entity `Refund`

```java
    private Long id;
    private String paymentId;
    private long amountPaise;
    private String reason;
    private String idempotencyKey;
    private RefundStatus status;
    private Instant createdAt;
    private long version;
```

## `RefundRepository`

```java
// TODO: complete the three methods (the signatures are given):
//   1. findByIdempotencyKey: derived query.
//   2. findByPaymentId: derived, paged and sorted by the Pageable the caller passes. What extra query does Page<T> run, and what
//      would Slice<T> save?
//   3. findStalePending: a @Query (JPQL) returning PENDING refunds created BEFORE :cutoff, oldest first. Use the enum literal
//      in JPQL (fully qualified) and a named @Param.
public interface RefundRepository extends JpaRepository<Refund, Long> {
    Optional<Refund> findByIdempotencyKey(String idempotencyKey);
    Page<Refund> findByPaymentId(String paymentId, Pageable pageable);
    List<Refund> findStalePending(Instant cutoff);
}
```

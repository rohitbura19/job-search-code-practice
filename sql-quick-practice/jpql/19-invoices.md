# 19. invoices - repository queries

_From daily-practice/2026-11-09-mon. E - Spring Boot: invoices API (30 min) - project `e-spring-invoices/` (find `TODO(E`)_

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-11-09-mon/answers/`.

## Entity `Invoice`

```java
    private Long id;
    private String customer;
    private InvoiceStatus status = InvoiceStatus.DRAFT;
    private List<LineItem> items = new ArrayList<>();
    private Instant createdAt;
    private Instant issuedAt;
```

## `InvoiceRepository`

```java
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    /** TODO(E2a): derived query, invoices with the given status, paged. (nothing to add except making sure the name is right) */
    Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable);
    /**
     * TODO(E2b): JPQL with a constructor expression: new practice.e.CustomerTotal(customer, sum(quantity * unitPriceCents)) over
     *  the invoices with the given status (join the items collection), grouped by customer, ordered by customer.
     */
    List<CustomerTotal> totalsByCustomer(InvoiceStatus status);
    /**
     * TODO(E2c): bulk JPQL update: set status VOID on every DRAFT invoice created before the given instant; return the row count.
     *  The persistence context must be flushed first and cleared afterwards, otherwise stale entities are returned.
     */
    int voidStaleDrafts(Instant before);
    // TODO(E2d): derived query without @Query: the 3 newest invoices of a customer (by createdAt, newest first).
    //            Name it findTop3ByCustomerOrderByCreatedAtDesc (return List<Invoice>); the service does not use it, the test does.
}
```

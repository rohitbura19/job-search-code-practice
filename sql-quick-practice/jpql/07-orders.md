# 07. orders - repository queries

_From daily-practice/2026-10-28-wed. > Project: `e-spring-orders/` (own `pom.xml`). Tests: `OrderRepositoryTest` (`@DataJpaTest`), `OrderControllerTest` (`@WebMvcTest`), `OrderServiceTest` (Mockito + fixed `Clock`), `OrderPropsTest` (`ApplicationContextRunner`)._

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-10-28-wed/answers/`.

## Entity `PurchaseOrder`

```java
    private Long id;
    private String orderNo;
    private String customerId;
    private OrderStatus status = OrderStatus.NEW;
    private Instant createdAt;
    private List<OrderLine> lines = new ArrayList<>();
```

## `PurchaseOrderRepository`

```java
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    // TODO: load the order TOGETHER with its lines in one query (no lazy loading later): @EntityGraph.
    Optional<PurchaseOrder> findByOrderNo(String orderNo);
    boolean existsByOrderNo(String orderNo);
    // TODO: derived query: this customer's orders whose status is in the given collection, paged.
    Page<PurchaseOrder> findByCustomerIdAndStatusIn(String customerId, Collection<OrderStatus> statuses, Pageable pageable);
    // TODO: @Query (JPQL, constructor expression `select new practice.e.CustomerTotal(...)`): per customer the sum of
    //       quantity * unitPricePaise over the lines of orders that are NOT cancelled, biggest total first, ties by customerId.
    //       The Pageable only limits the number of rows (top N).
    List<CustomerTotal> topCustomers(Pageable limit);
}
```

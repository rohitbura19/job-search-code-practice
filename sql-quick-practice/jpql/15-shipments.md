# 15. shipments - repository queries

_From daily-practice/2026-11-05-thu. E. Spring Boot + Kafka: shipments (30 min)_

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-11-05-thu/answers/`.

## Entity `Shipment`

```java
    private Long id;
    private String orderId;
    private String address;
    private ShipmentStatus status;
    private String trackingId;
    private Instant updatedAt;
    private long version;
```

## `ShipmentRepository`

```java
// TODO: @Param is needed on query parameters (import org.springframework.data.jpa.repository.Query / org.springframework.data.repository.query.Param).
//  findByOrderId / findByTrackingId: derived queries, nothing to add.
//  search: @Query returning all shipments, or only those with the given status when it is null-free; ordered by id ascending.
//          JPQL: select s from Shipment s where (:status is null or s.status = :status) order by s.id
//          Also add a countQuery so the Page total is right: select count(s) from Shipment s where (:status is null or s.status = :status)
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Optional<Shipment> findByOrderId(String orderId);
    Optional<Shipment> findByTrackingId(String trackingId);
    Page<Shipment> search(ShipmentStatus status, Pageable pageable);
}
```

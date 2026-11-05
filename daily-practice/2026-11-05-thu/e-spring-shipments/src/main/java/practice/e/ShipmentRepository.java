package practice.e;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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

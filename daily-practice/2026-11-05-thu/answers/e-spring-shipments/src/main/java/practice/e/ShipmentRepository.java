package practice.e;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Optional<Shipment> findByOrderId(String orderId);

    Optional<Shipment> findByTrackingId(String trackingId);

    @Query(value = "select s from Shipment s where (:status is null or s.status = :status) order by s.id",
           countQuery = "select count(s) from Shipment s where (:status is null or s.status = :status)")
    Page<Shipment> search(@Param("status") ShipmentStatus status, Pageable pageable);
}

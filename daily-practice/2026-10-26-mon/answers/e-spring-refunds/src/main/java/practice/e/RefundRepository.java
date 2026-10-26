package practice.e;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefundRepository extends JpaRepository<Refund, Long> {
    Optional<Refund> findByIdempotencyKey(String idempotencyKey);

    // Page<T> runs a second COUNT query for totalElements; Slice<T> would fetch size+1 rows instead and skip the count.
    Page<Refund> findByPaymentId(String paymentId, Pageable pageable);

    @Query("select r from Refund r where r.status = practice.e.RefundStatus.PENDING and r.createdAt < :cutoff order by r.createdAt")
    List<Refund> findStalePending(@Param("cutoff") Instant cutoff);
}

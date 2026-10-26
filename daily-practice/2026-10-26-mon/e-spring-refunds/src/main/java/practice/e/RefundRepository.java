package practice.e;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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

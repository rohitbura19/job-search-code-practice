package practice.e;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/** Given. */
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    Optional<Transfer> findByIdempotencyKey(String idempotencyKey);
}

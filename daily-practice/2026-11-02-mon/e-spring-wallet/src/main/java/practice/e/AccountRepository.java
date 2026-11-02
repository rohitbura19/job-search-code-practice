package practice.e;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByOwner(String owner);

    // TODO: findByIdForUpdate(Long id): SELECT ... FOR UPDATE through a PESSIMISTIC_WRITE lock and an explicit JPQL @Query with @Param.
    Optional<Account> findByIdForUpdate(Long id);
}

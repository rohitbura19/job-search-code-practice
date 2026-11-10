package practice.e;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    /**
     * TODO(E2e): JPQL: accounts that have NO subscription with status ACTIVE (never subscribed counts too), ordered by email.
     *  Use `not exists` with a correlated subquery over Subscription.
     */
    List<Account> findChurned();
}

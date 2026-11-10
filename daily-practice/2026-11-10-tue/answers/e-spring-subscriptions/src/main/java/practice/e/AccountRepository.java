package practice.e;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    @Query("select a from Account a where not exists (select 1 from Subscription s where s.account = a and s.status = practice.e.SubscriptionStatus.ACTIVE) order by a.email")
    List<Account> findChurned();
}

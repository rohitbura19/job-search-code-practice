package practice.e;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    boolean existsByAccountIdAndStatus(Long accountId, SubscriptionStatus status);

    @EntityGraph(attributePaths = "account")
    List<Subscription> findByAccountEmailOrderByStartedAtDesc(String email);

    @Query("select s.plan as plan, count(s) as total from Subscription s where s.status = :status group by s.plan order by s.plan")
    List<PlanCount> countByPlan(@Param("status") SubscriptionStatus status);

    @EntityGraph(attributePaths = "account")
    List<Subscription> findByStatusAndRenewsAtBetweenOrderByRenewsAtAsc(SubscriptionStatus status, Instant from, Instant to);
}

package practice.e;

import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // TODO: optional filters, newest first, as a Slice (no count query). A null status / null assignee means "do not filter on it".
    //  JPQL @Query with @Param, e.g.  (:status is null or t.status = :status)  and  (:assignee is null or t.assignee = :assignee),
    //  order by t.createdAt desc, t.id desc (the id makes the order stable for equal timestamps).
    Slice<Ticket> search(Status status, String assignee, Pageable pageable);

    // TODO: how many tickets per status: JPQL "select t.status as status, count(t) as total ... group by t.status", mapped to the StatusCount projection.
    List<StatusCount> countByStatus();

    // TODO: bulk update. Every OPEN ticket created before `cutoff` whose priority is not yet URGENT becomes URGENT; return the number of rows changed.
    //  Needs @Modifying and @Query. Make the persistence context forget stale entities after the update (clearAutomatically).
    //  (A bulk JPQL update does not touch @Version and fires no entity callbacks - say why that matters.)
    int escalateStale(Instant cutoff);
}

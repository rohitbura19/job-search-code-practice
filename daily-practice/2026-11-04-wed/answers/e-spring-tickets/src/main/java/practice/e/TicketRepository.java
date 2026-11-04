package practice.e;

import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    /** "(:p is null or ...)" turns each filter off when the parameter is null. A Slice asks the database for size + 1 rows instead of running a count query. */
    @Query("""
            select t from Ticket t
            where (:status is null or t.status = :status)
              and (:assignee is null or t.assignee = :assignee)
            order by t.createdAt desc, t.id desc""")
    Slice<Ticket> search(@Param("status") Status status, @Param("assignee") String assignee, Pageable pageable);

    /** The aliases must match the projection's getter names. */
    @Query("select t.status as status, count(t) as total from Ticket t group by t.status")
    List<StatusCount> countByStatus();

    /** clearAutomatically: after the bulk statement the first-level cache would still hold the old priorities. */
    @Modifying(clearAutomatically = true)
    @Query("""
            update Ticket t set t.priority = practice.e.Priority.URGENT
            where t.status = practice.e.Status.OPEN and t.createdAt < :cutoff and t.priority <> practice.e.Priority.URGENT""")
    int escalateStale(@Param("cutoff") Instant cutoff);
}

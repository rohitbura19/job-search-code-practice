package practice.e;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {
    /** One query for the task and its comments (fetch join avoids the lazy load / N+1). */
    @Query("select t from Task t left join fetch t.comments where t.id = :id")
    Optional<Task> findWithComments(@Param("id") Long id);

    /** Derived query + Pageable: Spring adds order by / limit and a count query. */
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    @Query("select t.status as status, count(t) as total from Task t group by t.status")
    List<StatusCount> countByStatus();
}

package practice.e;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    /** TODO(E3): ONE query that loads the task together with its comments (JPQL with a fetch join; keep tasks without comments). */
    Optional<Task> findWithComments(Long id);

    /** TODO(E3): derived query - tasks with that status, paged and sorted by the Pageable. */
    Page<Task> findByStatus(TaskStatus status, Pageable pageable);

    /** TODO(E3): JPQL group by status; the aliases must be "status" and "total" so the StatusCount projection works. */
    List<StatusCount> countByStatus();
}

package practice.e;

import org.springframework.data.jpa.repository.JpaRepository;

/** Given. */
public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, String> {}

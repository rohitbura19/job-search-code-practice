package practice.e;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TODO: the derived queries are declared; finish the other two (add annotations/parameter names):
 *   search:  @Query JPQL: owner matches AND (title OR body contains q, case-insensitive)
 *   restore: NATIVE @Modifying update that sets deleted = false (bypasses the soft-delete filter);
 *            the persistence context must be flushed before and cleared after; returns rows changed
 */
public interface NoteRepository extends JpaRepository<Note, Long> {
    long countByOwner(String owner);

    Page<Note> findByOwner(String owner, Pageable pageable);

    Page<Note> search(String owner, String q, Pageable pageable);

    int restore(long id);
}

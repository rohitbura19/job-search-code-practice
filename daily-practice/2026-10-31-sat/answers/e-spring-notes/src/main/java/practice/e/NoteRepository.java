package practice.e;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoteRepository extends JpaRepository<Note, Long> {
    long countByOwner(String owner);

    Page<Note> findByOwner(String owner, Pageable pageable);

    @Query("select n from Note n where n.owner = :owner and (lower(n.title) like lower(concat('%', :q, '%')) or lower(n.body) like lower(concat('%', :q, '%')))")
    Page<Note> search(@Param("owner") String owner, @Param("q") String q, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "update notes set deleted = false where id = :id", nativeQuery = true)
    int restore(@Param("id") long id);
}

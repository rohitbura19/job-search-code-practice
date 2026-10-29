package practice.e;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByProductIdAndStatus(String productId, ReviewStatus status, Pageable pageable);

    boolean existsByProductIdAndAuthor(String productId, String author);

    @Query("select new practice.e.RatingSummary(r.productId, count(r), avg(r.rating)) from Review r "
            + "where r.productId = :productId and r.status = practice.e.ReviewStatus.PUBLISHED group by r.productId")
    Optional<RatingSummary> summary(@Param("productId") String productId);

    /** clearAutomatically: the bulk update bypasses the first-level cache, so evict it; flushAutomatically: do not lose pending writes. */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Review r set r.status = :status where r.id = :id")
    int setStatus(@Param("id") Long id, @Param("status") ReviewStatus status);
}

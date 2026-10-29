package practice.e;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // TODO: derived query: the product's reviews with this status, paged (the caller supplies the sort).
    Page<Review> findByProductIdAndStatus(String productId, ReviewStatus status, Pageable pageable);

    // TODO: derived existence query on productId + author.
    boolean existsByProductIdAndAuthor(String productId, String author);

    // TODO: @Query (JPQL, constructor expression `select new practice.e.RatingSummary(...)`): count and avg(rating) of the PUBLISHED
    //       reviews of one product (named parameter). Empty Optional when the product has none (group by makes no row).
    Optional<RatingSummary> summary(String productId);

    // TODO: a bulk UPDATE query that sets the status of ONE review by id and returns the number of rows changed. It bypasses the
    //       persistence context: what must you add so a later find in the same transaction does not return the stale entity?
    int setStatus(Long id, ReviewStatus status);
}

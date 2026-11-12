package practice.e;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // TODO(E2a): Page<Review> findByProductIdAndStatusOrderByCreatedAtDescIdDesc(Long productId, ReviewStatus status, Pageable pageable)
    //            derived query; the "IdDesc" tie-break keeps pages stable when two reviews share a timestamp
    // TODO(E2b): boolean existsByProductIdAndUserId(Long productId, Long userId)
    // TODO(E2c): long countByProductIdAndStatus(Long productId, ReviewStatus status)
    // TODO(E2d): Double averageRating(Long productId, ReviewStatus status)  - @Query JPQL avg(r.rating), @Param names; Double (nullable) because avg of no rows is null
    // TODO(E2e): List<RatingCount> ratingHistogram(Long productId, ReviewStatus status) - @Query selecting `r.rating as rating, count(r) as total`,
    //            group by rating, order by rating (interface projection RatingCount is given)
}

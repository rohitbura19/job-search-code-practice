package practice.e;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByProductIdAndStatusOrderByCreatedAtDescIdDesc(Long productId, ReviewStatus status, Pageable pageable);

    boolean existsByProductIdAndUserId(Long productId, Long userId);

    long countByProductIdAndStatus(Long productId, ReviewStatus status);

    @Query("select avg(r.rating) from Review r where r.productId = :productId and r.status = :status")
    Double averageRating(@Param("productId") Long productId, @Param("status") ReviewStatus status);

    @Query("select r.rating as rating, count(r) as total from Review r where r.productId = :productId and r.status = :status group by r.rating order by r.rating")
    List<RatingCount> ratingHistogram(@Param("productId") Long productId, @Param("status") ReviewStatus status);
}

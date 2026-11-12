package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class ReviewRepositoryTest {
    @Autowired ReviewRepository repo;

    private Review save(long product, long user, int rating, ReviewStatus st, String at) {
        return repo.saveAndFlush(new Review(product, user, rating, "t", null, st, Instant.parse(at)));
    }

    @Test void pagesPublishedNewestFirstWithStableTieBreak() {
        save(1, 1, 5, ReviewStatus.PUBLISHED, "2026-11-01T10:00:00Z");
        Review tieA = save(1, 2, 4, ReviewStatus.PUBLISHED, "2026-11-02T10:00:00Z");
        Review tieB = save(1, 3, 3, ReviewStatus.PUBLISHED, "2026-11-02T10:00:00Z");
        save(1, 4, 1, ReviewStatus.REJECTED, "2026-11-03T10:00:00Z");
        save(2, 5, 5, ReviewStatus.PUBLISHED, "2026-11-04T10:00:00Z");
        Page<Review> p0 = repo.findByProductIdAndStatusOrderByCreatedAtDescIdDesc(1L, ReviewStatus.PUBLISHED, PageRequest.of(0, 2));
        assertEquals(3, p0.getTotalElements());
        assertEquals(List.of(tieB.getId(), tieA.getId()), p0.getContent().stream().map(Review::getId).toList());
        Page<Review> p1 = repo.findByProductIdAndStatusOrderByCreatedAtDescIdDesc(1L, ReviewStatus.PUBLISHED, PageRequest.of(1, 2));
        assertEquals(1, p1.getContent().size());
        assertEquals(1L, p1.getContent().get(0).getUserId());
    }
    @Test void existsAndCount() {
        save(1, 1, 5, ReviewStatus.PUBLISHED, "2026-11-01T10:00:00Z");
        save(1, 2, 5, ReviewStatus.REJECTED, "2026-11-01T11:00:00Z");
        assertTrue(repo.existsByProductIdAndUserId(1L, 1L));
        assertFalse(repo.existsByProductIdAndUserId(1L, 3L));
        assertFalse(repo.existsByProductIdAndUserId(2L, 1L));
        assertEquals(1, repo.countByProductIdAndStatus(1L, ReviewStatus.PUBLISHED));
        assertEquals(1, repo.countByProductIdAndStatus(1L, ReviewStatus.REJECTED));
    }
    @Test void oneReviewPerUserPerProductIsEnforcedByTheDatabase() {
        save(1, 1, 5, ReviewStatus.PUBLISHED, "2026-11-01T10:00:00Z");
        assertThrows(DataIntegrityViolationException.class, () -> save(1, 1, 2, ReviewStatus.PUBLISHED, "2026-11-02T10:00:00Z"));
    }
    @Test void aggregatesIgnoreRejectedAndOtherProducts() {
        save(1, 1, 5, ReviewStatus.PUBLISHED, "2026-11-01T10:00:00Z");
        save(1, 2, 4, ReviewStatus.PUBLISHED, "2026-11-01T11:00:00Z");
        save(1, 3, 4, ReviewStatus.PUBLISHED, "2026-11-01T12:00:00Z");
        save(1, 4, 1, ReviewStatus.REJECTED, "2026-11-01T13:00:00Z");
        save(2, 5, 1, ReviewStatus.PUBLISHED, "2026-11-01T14:00:00Z");
        assertEquals(13.0 / 3, repo.averageRating(1L, ReviewStatus.PUBLISHED), 1e-9);
        List<RatingCount> h = repo.ratingHistogram(1L, ReviewStatus.PUBLISHED);
        assertEquals(List.of(4, 5), h.stream().map(RatingCount::getRating).toList());
        assertEquals(List.of(2L, 1L), h.stream().map(RatingCount::getTotal).toList());
    }
    @Test void aggregatesOfNothing() {
        assertNull(repo.averageRating(9L, ReviewStatus.PUBLISHED));
        assertTrue(repo.ratingHistogram(9L, ReviewStatus.PUBLISHED).isEmpty());
    }
}

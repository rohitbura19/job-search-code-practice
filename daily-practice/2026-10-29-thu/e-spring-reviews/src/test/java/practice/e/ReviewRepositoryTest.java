package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class ReviewRepositoryTest {
    static final Instant T0 = Instant.parse("2026-10-29T04:00:00Z");
    @Autowired ReviewRepository repo;
    @Autowired EntityManager em;
    Long hiddenCandidateId;

    static Review review(String product, String author, int rating, int minute) {
        return new Review(product, author, rating, "text by " + author, T0.plusSeconds(60L * minute));
    }

    @BeforeEach void seed() {
        repo.save(review("p1", "asha", 5, 1));
        repo.save(review("p1", "ben", 4, 2));
        hiddenCandidateId = repo.save(review("p1", "cy", 1, 3)).getId();
        repo.save(review("p2", "asha", 3, 4));
        repo.flush();
        repo.setStatus(hiddenCandidateId, ReviewStatus.HIDDEN);
        em.clear();
    }

    @Test void statusIsStoredAsItsNameAndDefaultsToPublished() {
        Object raw = em.createNativeQuery("select status from product_review where id = " + hiddenCandidateId).getSingleResult();
        assertEquals("HIDDEN", raw);
        assertEquals(ReviewStatus.PUBLISHED, repo.findAll().stream().filter(r -> r.getAuthor().equals("asha")).findFirst().orElseThrow().getStatus());
    }

    @Test void pagedQueryFiltersByProductAndStatus() {
        Page<Review> p = repo.findByProductIdAndStatus("p1", ReviewStatus.PUBLISHED, PageRequest.of(0, 1, Sort.by(Sort.Order.desc("createdAt"))));
        assertEquals(2, p.getTotalElements());
        assertEquals(2, p.getTotalPages());
        assertEquals("ben", p.getContent().get(0).getAuthor());
        assertEquals(List.of("cy"), repo.findByProductIdAndStatus("p1", ReviewStatus.HIDDEN, PageRequest.of(0, 10)).map(Review::getAuthor).getContent());
        assertTrue(repo.findByProductIdAndStatus("nope", ReviewStatus.PUBLISHED, PageRequest.of(0, 10)).isEmpty());
    }

    @Test void existsByProductAndAuthor() {
        assertTrue(repo.existsByProductIdAndAuthor("p1", "asha"));
        assertTrue(repo.existsByProductIdAndAuthor("p2", "asha"));
        assertFalse(repo.existsByProductIdAndAuthor("p2", "ben"));
    }

    @Test void summaryCountsPublishedOnly() {
        RatingSummary s = repo.summary("p1").orElseThrow();
        assertEquals("p1", s.productId());
        assertEquals(2, s.count());
        assertEquals(4.5, s.average(), 1e-9);          // the hidden 1-star review is ignored
        assertEquals(3.0, repo.summary("p2").orElseThrow().average(), 1e-9);
        assertTrue(repo.summary("nope").isEmpty());
    }

    @Test void setStatusReturnsRowCountAndEvictsStaleEntities() {
        Review loaded = repo.findAll().stream().filter(r -> r.getProductId().equals("p2")).findFirst().orElseThrow();
        assertEquals(1, repo.setStatus(loaded.getId(), ReviewStatus.HIDDEN));
        assertEquals(0, repo.setStatus(99_999L, ReviewStatus.HIDDEN));
        assertEquals(ReviewStatus.HIDDEN, repo.findById(loaded.getId()).orElseThrow().getStatus(), "stale first-level cache: clearAutomatically is missing");
        assertTrue(repo.summary("p2").isEmpty());
    }

    @Test void secondReviewByTheSameAuthorViolatesTheNamedConstraint() {
        DataIntegrityViolationException e = assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(review("p1", "asha", 2, 9)));
        assertTrue(e.getMessage().toLowerCase().contains("uq_review_product_author"), e.getMessage());
    }

    @Test void sameAuthorOnAnotherProductIsFine() {
        repo.saveAndFlush(review("p3", "asha", 2, 9));
        assertEquals(1, repo.summary("p3").orElseThrow().count());
    }

    @Test void notNullColumnsAreEnforced() {
        assertThrows(DataIntegrityViolationException.class, () -> repo.saveAndFlush(new Review("p9", null, 3, "t", T0)));
    }
}

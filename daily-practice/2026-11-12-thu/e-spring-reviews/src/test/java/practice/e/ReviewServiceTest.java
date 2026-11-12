package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import practice.e.Views.*;

@DataJpaTest
class ReviewServiceTest {
    @Autowired ReviewRepository repo;
    ReviewService service;
    Instant now = Instant.parse("2026-11-12T08:00:00Z");

    @BeforeEach void setUp() {
        service = new ReviewService(repo, new ReviewProperties(List.of("scam", "Spam"), 2), Clock.fixed(now, ZoneOffset.UTC));
    }

    @Test void createPublishesCleanTextAndStampsTime() {
        ReviewView v = service.create(1, 10, new CreateReview(5, "Great", null));
        assertEquals(ReviewStatus.PUBLISHED, v.status());
        assertEquals(now, v.createdAt());
        assertEquals(10, v.userId());
        assertTrue(v.id() > 0);
    }
    @Test void bannedWordInTitleOrBodyIsRejectedCaseInsensitively() {
        assertEquals(ReviewStatus.REJECTED, service.create(1, 10, new CreateReview(1, "total SCAM", "x")).status());
        assertEquals(ReviewStatus.REJECTED, service.create(1, 11, new CreateReview(1, "ok", "this is SpAm!")).status());
        assertEquals(0, service.list(1, 0, 10).total());
    }
    @Test void secondReviewBySameUserIsADuplicate() {
        service.create(1, 10, new CreateReview(5, "a", "b"));
        assertThrows(DuplicateReviewException.class, () -> service.create(1, 10, new CreateReview(1, "c", "d")));
        assertDoesNotThrow(() -> service.create(2, 10, new CreateReview(1, "other product", "ok")));
    }
    @Test void listIsNewestFirstAndSizeIsCapped() {
        for (int i = 1; i <= 3; i++) {
            Clock c = Clock.fixed(now.plusSeconds(i), ZoneOffset.UTC);
            new ReviewService(repo, new ReviewProperties(List.of(), 2), c).create(1, i, new CreateReview(3, "r" + i, null));
        }
        ReviewPage p = service.list(1, 0, 50);
        assertEquals(2, p.size());
        assertEquals(3, p.total());
        assertEquals(List.of("r3", "r2"), p.items().stream().map(ReviewView::title).toList());
        assertEquals(List.of("r1"), service.list(1, 1, 50).items().stream().map(ReviewView::title).toList());
    }
    @Test void summaryRoundsAndFillsHistogram() {
        service.create(1, 1, new CreateReview(5, "a", null));
        service.create(1, 2, new CreateReview(4, "b", null));
        service.create(1, 3, new CreateReview(4, "c", null));
        service.create(1, 4, new CreateReview(1, "spam", null));   // rejected: ignored
        Summary s = service.summary(1);
        assertEquals(4.3, s.average(), 1e-9);
        assertEquals(3, s.count());
        assertEquals(List.of(1, 2, 3, 4, 5), List.copyOf(s.histogram().keySet()));
        assertEquals(List.of(0L, 0L, 0L, 2L, 1L), List.copyOf(s.histogram().values()));
        Summary empty = service.summary(99);
        assertEquals(0.0, empty.average());
        assertEquals(0, empty.count());
        assertEquals(5, empty.histogram().size());
    }
    @Test void getAndDelete() {
        long id = service.create(1, 10, new CreateReview(5, "a", null)).id();
        assertEquals("a", service.get(id).title());
        assertThrows(NotOwnerException.class, () -> service.delete(id, 11));
        assertDoesNotThrow(() -> service.delete(id, 10));
        assertThrows(ReviewNotFoundException.class, () -> service.get(id));
        assertThrows(ReviewNotFoundException.class, () -> service.delete(id, 10));
    }
}

package practice.e;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

class ReviewServiceTest {
    static final Instant NOW = Instant.parse("2026-10-29T05:00:00Z");
    ReviewRepository repo = mock(ReviewRepository.class);
    ReviewService service = new ReviewService(repo, Clock.fixed(NOW, ZoneOffset.UTC));

    @Test void createStampsTheClockAndReturnsTheView() {
        when(repo.existsByProductIdAndAuthor("p1", "asha")).thenReturn(false);
        when(repo.save(any(Review.class))).thenAnswer(inv -> inv.getArgument(0));
        ReviewView v = service.create("p1", new CreateReviewRequest("asha", 5, "great"));
        ArgumentCaptor<Review> saved = ArgumentCaptor.forClass(Review.class);
        verify(repo).save(saved.capture());
        assertEquals(NOW, saved.getValue().getCreatedAt());
        assertEquals("p1", v.productId());
        assertEquals("asha", v.author());
        assertEquals(5, v.rating());
        assertEquals(NOW, v.createdAt());
    }

    @Test void duplicateAuthorIsRejectedBeforeSaving() {
        when(repo.existsByProductIdAndAuthor("p1", "asha")).thenReturn(true);
        assertThrows(DuplicateReviewException.class, () -> service.create("p1", new CreateReviewRequest("asha", 5, "again")));
        verify(repo, never()).save(any());
    }

    @Test void listAsksForPublishedNewestFirst() {
        when(repo.findByProductIdAndStatus(eq("p1"), eq(ReviewStatus.PUBLISHED), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(new Review("p1", "ben", 4, "ok", NOW))));
        Page<ReviewView> page = service.list("p1", 2, 5);
        assertEquals("ben", page.getContent().get(0).author());
        ArgumentCaptor<Pageable> pr = ArgumentCaptor.forClass(Pageable.class);
        verify(repo).findByProductIdAndStatus(eq("p1"), eq(ReviewStatus.PUBLISHED), pr.capture());
        assertEquals(2, pr.getValue().getPageNumber());
        assertEquals(5, pr.getValue().getPageSize());
        assertEquals(Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id")), pr.getValue().getSort());
    }

    @Test void summaryFallsBackToZero() {
        when(repo.summary("p1")).thenReturn(Optional.empty());
        assertEquals(new RatingSummary("p1", 0, 0.0), service.summary("p1"));
        when(repo.summary("p2")).thenReturn(Optional.of(new RatingSummary("p2", 3, 4.0)));
        assertEquals(new RatingSummary("p2", 3, 4.0), service.summary("p2"));
    }

    @Test void hideUsesOneUpdateAndNeverSaves() {
        when(repo.setStatus(7L, ReviewStatus.HIDDEN)).thenReturn(1);
        service.hide(7);
        verify(repo).setStatus(7L, ReviewStatus.HIDDEN);
        verify(repo, never()).save(any());
        verify(repo, never()).findById(any());
        when(repo.setStatus(8L, ReviewStatus.HIDDEN)).thenReturn(0);
        assertThrows(ReviewNotFoundException.class, () -> service.hide(8));
    }

    @Test void transactionAnnotations() throws Exception {
        assertTrue(ReviewService.class.isAnnotationPresent(org.springframework.stereotype.Service.class));
        Transactional cls = ReviewService.class.getAnnotation(Transactional.class);
        assertNotNull(cls);
        assertTrue(cls.readOnly(), "class level must be read-only");
        Transactional create = ReviewService.class.getMethod("create", String.class, CreateReviewRequest.class).getAnnotation(Transactional.class);
        Transactional hide = ReviewService.class.getMethod("hide", long.class).getAnnotation(Transactional.class);
        assertNotNull(create);
        assertFalse(create.readOnly());
        assertNotNull(hide);
        assertFalse(hide.readOnly());
    }
}

package practice.e;

import java.time.Clock;
import practice.e.Views.*;

// TODO(E5a): make this a Spring @Service, read-only transactional by default; write methods override with a plain @Transactional
public class ReviewService {
    private final ReviewRepository repo;
    private final ReviewProperties props;
    private final Clock clock;

    public ReviewService(ReviewRepository repo, ReviewProperties props, Clock clock) {
        this.repo = repo; this.props = props; this.clock = clock;
    }

    /**
     * TODO(E5b): one review per (product, user): existing -> DuplicateReviewException. Text (title + " " + body, body may be null) containing any
     * banned word, case-insensitive, is saved as REJECTED, else PUBLISHED. createdAt = Instant.now(clock). Returns ReviewView.from(saved).
     */
    public ReviewView create(long productId, long userId, CreateReview req) { throw new UnsupportedOperationException("TODO"); }

    /** TODO(E5c): ReviewView by id, ReviewNotFoundException when missing. */
    public ReviewView get(long id) { throw new UnsupportedOperationException("TODO"); }

    /** TODO(E5d): PUBLISHED reviews of the product, newest first; size capped at props.maxPageSize(); ReviewPage(items, page, cappedSize, totalElements). */
    public ReviewPage list(long productId, int page, int size) { throw new UnsupportedOperationException("TODO"); }

    /** TODO(E5e): average of PUBLISHED ratings rounded to one decimal (0.0 if none), count, histogram with keys 1..5 always present (0 when empty). */
    public Summary summary(long productId) { throw new UnsupportedOperationException("TODO"); }

    /** TODO(E5f): delete by id; missing -> ReviewNotFoundException; someone else's review -> NotOwnerException. No explicit save needed. */
    public void delete(long id, long userId) { throw new UnsupportedOperationException("TODO"); }
}

package practice.e;

import java.time.Clock;
import org.springframework.data.domain.Page;

// TODO: make it a Spring bean with read-only transactions by default; writes override that.
public class ReviewService {
    private final ReviewRepository repo;
    private final Clock clock;

    public ReviewService(ReviewRepository repo, Clock clock) {
        this.repo = repo;
        this.clock = clock;
    }

    /** Same author twice for a product -> DuplicateReviewException. Stamps createdAt from the clock, saves, returns the view. */
    public ReviewView create(String productId, CreateReviewRequest r) { throw new UnsupportedOperationException("TODO"); }

    /** PUBLISHED reviews only, newest first (createdAt desc, id desc). */
    public Page<ReviewView> list(String productId, int page, int size) { throw new UnsupportedOperationException("TODO"); }

    /** Never null: a product without published reviews gives count 0 and average 0.0. */
    public RatingSummary summary(String productId) { throw new UnsupportedOperationException("TODO"); }

    /** Unknown id -> ReviewNotFoundException. One UPDATE statement, no load-modify-save. */
    public void hide(long id) { throw new UnsupportedOperationException("TODO"); }
}

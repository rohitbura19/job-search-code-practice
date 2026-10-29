package practice.e;

import java.time.Clock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Read-only by default (no dirty checking, replica-friendly). check-then-insert is only the friendly path: the unique constraint is the guard. */
@Service
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository repo;
    private final Clock clock;

    public ReviewService(ReviewRepository repo, Clock clock) {
        this.repo = repo;
        this.clock = clock;
    }

    @Transactional
    public ReviewView create(String productId, CreateReviewRequest r) {
        if (repo.existsByProductIdAndAuthor(productId, r.author())) throw new DuplicateReviewException(productId, r.author());
        return toView(repo.save(new Review(productId, r.author(), r.rating(), r.text(), clock.instant())));
    }

    public Page<ReviewView> list(String productId, int page, int size) {
        PageRequest pr = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id")));
        return repo.findByProductIdAndStatus(productId, ReviewStatus.PUBLISHED, pr).map(ReviewService::toView);
    }

    public RatingSummary summary(String productId) {
        return repo.summary(productId).orElseGet(() -> new RatingSummary(productId, 0, 0.0));
    }

    @Transactional
    public void hide(long id) {
        if (repo.setStatus(id, ReviewStatus.HIDDEN) == 0) throw new ReviewNotFoundException(id);
    }

    private static ReviewView toView(Review r) {
        return new ReviewView(r.getId(), r.getProductId(), r.getAuthor(), r.getRating(), r.getText(), r.getCreatedAt());
    }
}

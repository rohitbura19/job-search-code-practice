package practice.e;

import java.time.Clock;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.e.Views.*;

@Service
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository repo;
    private final ReviewProperties props;
    private final Clock clock;

    public ReviewService(ReviewRepository repo, ReviewProperties props, Clock clock) {
        this.repo = repo; this.props = props; this.clock = clock;
    }

    /** One review per (product, user). Text containing a banned word (case-insensitive, title or body) is stored as REJECTED, otherwise PUBLISHED. */
    @Transactional
    public ReviewView create(long productId, long userId, CreateReview req) {
        if (repo.existsByProductIdAndUserId(productId, userId)) throw new DuplicateReviewException(productId, userId);
        String text = (req.title() + " " + (req.body() == null ? "" : req.body())).toLowerCase(Locale.ROOT);
        boolean banned = props.bannedWords().stream().anyMatch(w -> text.contains(w.toLowerCase(Locale.ROOT)));
        Review saved = repo.save(new Review(productId, userId, req.rating(), req.title(), req.body(),
                banned ? ReviewStatus.REJECTED : ReviewStatus.PUBLISHED, Instant.now(clock)));
        return ReviewView.from(saved);
    }

    public ReviewView get(long id) {
        return repo.findById(id).map(ReviewView::from).orElseThrow(() -> new ReviewNotFoundException(id));
    }

    /** Published reviews only, newest first; the page size is capped at props.maxPageSize(). */
    public ReviewPage list(long productId, int page, int size) {
        int capped = Math.min(size, props.maxPageSize());
        Page<Review> p = repo.findByProductIdAndStatusOrderByCreatedAtDescIdDesc(productId, ReviewStatus.PUBLISHED, PageRequest.of(page, capped));
        return new ReviewPage(p.getContent().stream().map(ReviewView::from).toList(), page, capped, p.getTotalElements());
    }

    public Summary summary(long productId) {
        Double avg = repo.averageRating(productId, ReviewStatus.PUBLISHED);
        Map<Integer, Long> histogram = new LinkedHashMap<>();
        for (int i = 1; i <= 5; i++) histogram.put(i, 0L);
        repo.ratingHistogram(productId, ReviewStatus.PUBLISHED).forEach(rc -> histogram.put(rc.getRating(), rc.getTotal()));
        long count = histogram.values().stream().mapToLong(Long::longValue).sum();
        return new Summary(avg == null ? 0.0 : Math.round(avg * 10) / 10.0, count, histogram);
    }

    @Transactional
    public void delete(long id, long userId) {
        Review r = repo.findById(id).orElseThrow(() -> new ReviewNotFoundException(id));
        if (!r.getUserId().equals(userId)) throw new NotOwnerException();
        repo.delete(r);
    }
}

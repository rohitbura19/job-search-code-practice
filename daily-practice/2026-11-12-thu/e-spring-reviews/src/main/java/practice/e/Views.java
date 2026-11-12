package practice.e;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public final class Views {
    private Views() {}

    public record ReviewView(long id, long userId, int rating, String title, String body, ReviewStatus status, Instant createdAt) {
        public static ReviewView from(Review r) {
            return new ReviewView(r.getId(), r.getUserId(), r.getRating(), r.getTitle(), r.getBody(), r.getStatus(), r.getCreatedAt());
        }
    }

    public record ReviewPage(List<ReviewView> items, int page, int size, long total) {}

    /** average is rounded to one decimal, 0.0 when there are no published reviews; histogram always has keys 1..5. */
    public record Summary(double average, long count, Map<Integer, Long> histogram) {}
}

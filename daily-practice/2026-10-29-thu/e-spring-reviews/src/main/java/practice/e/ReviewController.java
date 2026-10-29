package practice.e;

import org.springframework.http.ResponseEntity;

// TODO: @RestController, base path /api (the paths below are absolute under it).
public class ReviewController {
    private final ReviewService service;
    private final ReviewProps props;

    public ReviewController(ReviewService service, ReviewProps props) {
        this.service = service;
        this.props = props;
    }

    // TODO POST /api/products/{productId}/reviews: validate the body; 201 with the view as the body.
    public ResponseEntity<ReviewView> create(String productId, CreateReviewRequest request) { throw new UnsupportedOperationException("TODO"); }

    // TODO GET /api/products/{productId}/reviews?page=&size=  page default 0 and never negative (clamp, no error);
    //      size default 10, CLAMPED to 1..props.maxPageSize().
    public PagedResponse<ReviewView> list(String productId, int page, int size) { throw new UnsupportedOperationException("TODO"); }

    // TODO GET /api/products/{productId}/rating
    public RatingSummary rating(String productId) { throw new UnsupportedOperationException("TODO"); }

    // TODO POST /api/reviews/{id}/hide -> 204 No Content.
    public ResponseEntity<Void> hide(long id) { throw new UnsupportedOperationException("TODO"); }
}

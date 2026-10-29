package practice.e;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService service;
    private final ReviewProps props;

    public ReviewController(ReviewService service, ReviewProps props) {
        this.service = service;
        this.props = props;
    }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<ReviewView> create(@PathVariable String productId, @Valid @RequestBody CreateReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(productId, request));
    }

    @GetMapping("/products/{productId}/reviews")
    public PagedResponse<ReviewView> list(@PathVariable String productId,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int size) {
        return PagedResponse.of(service.list(productId, Math.max(page, 0), Math.min(Math.max(size, 1), props.maxPageSize())));
    }

    @GetMapping("/products/{productId}/rating")
    public RatingSummary rating(@PathVariable String productId) { return service.summary(productId); }

    @PostMapping("/reviews/{id}/hide")
    public ResponseEntity<Void> hide(@PathVariable long id) {
        service.hide(id);
        return ResponseEntity.noContent().build();
    }
}

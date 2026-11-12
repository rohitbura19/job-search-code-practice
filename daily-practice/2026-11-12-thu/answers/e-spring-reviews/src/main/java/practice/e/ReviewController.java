package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import practice.e.Views.*;

@RestController
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService service;

    public ReviewController(ReviewService service) { this.service = service; }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<ReviewView> create(@PathVariable @Positive long productId,
                                             @RequestHeader("X-User-Id") @Positive long userId,
                                             @Valid @RequestBody CreateReview body) {
        ReviewView v = service.create(productId, userId, body);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/reviews/{id}").buildAndExpand(v.id()).toUri();
        return ResponseEntity.created(location).body(v);
    }

    @GetMapping("/products/{productId}/reviews")
    public ReviewPage list(@PathVariable long productId,
                           @RequestParam(defaultValue = "0") @Min(0) int page,
                           @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return service.list(productId, page, size);
    }

    @GetMapping("/products/{productId}/summary")
    public Summary summary(@PathVariable long productId) { return service.summary(productId); }

    @GetMapping("/reviews/{id}")
    public ReviewView get(@PathVariable long id) { return service.get(id); }

    @DeleteMapping("/reviews/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id, @RequestHeader("X-User-Id") @Positive long userId) { service.delete(id, userId); }
}

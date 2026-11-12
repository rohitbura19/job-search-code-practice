package practice.e;

import practice.e.Views.*;

// TODO(E6): REST controller under /api:
//  POST   /products/{productId}/reviews  header X-User-Id (required, @Positive), @Valid @RequestBody CreateReview -> 201, Location /api/reviews/{id}, body ReviewView
//  GET    /products/{productId}/reviews  ?page (default 0, >= 0) &size (default 10, 1..100) -> ReviewPage
//  GET    /products/{productId}/summary  -> Summary
//  GET    /reviews/{id}                  -> ReviewView
//  DELETE /reviews/{id}                  header X-User-Id (required, @Positive) -> 204 No Content
//  Validation annotations on @PathVariable/@RequestParam/@RequestHeader parameters must produce 400.
public class ReviewController {
    private final ReviewService service;

    public ReviewController(ReviewService service) { this.service = service; }

    // add the handler methods here (use ServletUriComponentsBuilder for the Location header)
}

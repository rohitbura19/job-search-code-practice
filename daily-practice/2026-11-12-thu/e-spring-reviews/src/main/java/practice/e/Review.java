package practice.e;

import java.time.Instant;

// TODO(E1): make this a JPA entity.
//  - @Entity, @Table(name = "reviews") with a unique constraint "uk_review_product_user" on (product_id, user_id)
//    and an index on (product_id, status, created_at)
//  - @Id with IDENTITY generation; @Column(name = ...) for snake_case names, nullable = false where the field is required,
//    length 80 for title and 2000 for body (body is optional)
//  - status stored as a STRING (never ORDINAL)
public class Review {
    private Long id;
    private Long productId;
    private Long userId;
    private int rating;
    private String title;
    private String body;
    private ReviewStatus status;
    private Instant createdAt;

    protected Review() {}

    public Review(Long productId, Long userId, int rating, String title, String body, ReviewStatus status, Instant createdAt) {
        this.productId = productId; this.userId = userId; this.rating = rating; this.title = title;
        this.body = body; this.status = status; this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public Long getUserId() { return userId; }
    public int getRating() { return rating; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public ReviewStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}

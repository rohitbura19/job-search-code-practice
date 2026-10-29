package practice.e;

import java.time.Instant;

// TODO: make this a JPA entity.
//   - table `product_review` (not a reserved word) with a NAMED unique constraint uq_review_product_author on (product_id, author)
//   - an index ix_review_product_status on (product_id, status, created_at)
//   - id: identity-generated Long; snake_case column names; rating/text/author/product_id NOT NULL; text length 500, ids length 40
//   - status stored as its NAME (not the ordinal), default PUBLISHED; JPA needs a protected no-arg constructor
public class Review {
    private Long id;
    private String productId;
    private String author;
    private int rating;
    private String text;
    private ReviewStatus status = ReviewStatus.PUBLISHED;
    private Instant createdAt;

    protected Review() {}

    public Review(String productId, String author, int rating, String text, Instant createdAt) {
        this.productId = productId;
        this.author = author;
        this.rating = rating;
        this.text = text;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getProductId() { return productId; }
    public String getAuthor() { return author; }
    public int getRating() { return rating; }
    public String getText() { return text; }
    public ReviewStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}

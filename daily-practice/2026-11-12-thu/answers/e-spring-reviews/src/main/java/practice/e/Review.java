package practice.e;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "reviews",
        uniqueConstraints = @UniqueConstraint(name = "uk_review_product_user", columnNames = {"product_id", "user_id"}),
        indexes = @Index(name = "ix_review_product_status_created", columnList = "product_id, status, created_at"))
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_id", nullable = false)
    private Long productId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(nullable = false)
    private int rating;
    @Column(nullable = false, length = 80)
    private String title;
    @Column(length = 2000)
    private String body;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 12)
    private ReviewStatus status;
    @Column(name = "created_at", nullable = false)
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

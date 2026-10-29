package practice.e;

import jakarta.persistence.*;
import java.time.Instant;

/** Named constraint so the 409 handler / DBA can recognise it; IDENTITY ids; STRING enums survive reordering of the enum. */
@Entity
@Table(name = "product_review",
        uniqueConstraints = @UniqueConstraint(name = "uq_review_product_author", columnNames = {"product_id", "author"}),
        indexes = @Index(name = "ix_review_product_status", columnList = "product_id, status, created_at"))
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "product_id", nullable = false, length = 40)
    private String productId;
    @Column(nullable = false, length = 40)
    private String author;
    @Column(nullable = false)
    private int rating;
    @Column(nullable = false, length = 500)
    private String text;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 12)
    private ReviewStatus status = ReviewStatus.PUBLISHED;
    @Column(name = "created_at", nullable = false)
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

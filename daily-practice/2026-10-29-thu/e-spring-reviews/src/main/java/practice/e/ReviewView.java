package practice.e;

import java.time.Instant;

public record ReviewView(Long id, String productId, String author, int rating, String text, Instant createdAt) {}

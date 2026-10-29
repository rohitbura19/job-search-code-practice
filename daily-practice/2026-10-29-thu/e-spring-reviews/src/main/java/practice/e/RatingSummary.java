package practice.e;

/** average is over PUBLISHED reviews only; a product without reviews has count 0 and average 0.0. */
public record RatingSummary(String productId, long count, double average) {}

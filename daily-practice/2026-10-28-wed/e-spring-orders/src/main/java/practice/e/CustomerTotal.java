package practice.e;

/** Result row of the "top customers" aggregate query. */
public record CustomerTotal(String customerId, long totalPaise) {}

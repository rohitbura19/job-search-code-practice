package s06_orders;

/** Order total in cents = qty * unitCents. */
public record Order(String id, String customer, String category, int qty, long unitCents) {}

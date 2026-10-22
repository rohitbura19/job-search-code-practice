package practice.d20261022.b1;

/** Order total in cents = qty * unitCents. */
public record Order(String id, String customer, String category, int qty, long unitCents) {}

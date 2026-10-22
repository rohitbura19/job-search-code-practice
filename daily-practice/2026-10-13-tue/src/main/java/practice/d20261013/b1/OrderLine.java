package practice.d20261013.b1;

/** GIVEN. */
public record OrderLine(String orderId, String customerId, String sku, int qty, double price, String city) {}

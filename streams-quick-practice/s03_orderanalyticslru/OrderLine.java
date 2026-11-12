package s03_orderanalyticslru;

/** GIVEN. */
public record OrderLine(String orderId, String customerId, String sku, int qty, double price, String city) {}

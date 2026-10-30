package practice.e;

/**
 * TODO: bind prefix "stock" as an immutable record (constructor binding). Defaults: lowThreshold 5 (>= 0), maxPageSize 50 (>= 1),
 * topics.reserved "stock.reserved", topics.lowStock "stock.low", topics.restock "stock.restock" (not blank).
 * Defaults must apply when NOTHING is configured, including the nested Topics record. Invalid values must stop the context.
 */
public record StockProps(int lowThreshold, int maxPageSize, Topics topics) {
    public record Topics(String reserved, String lowStock, String restock) {}
}

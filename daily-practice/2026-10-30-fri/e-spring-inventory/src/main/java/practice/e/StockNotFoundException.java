package practice.e;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(String sku) { super("No stock item " + sku); }
}

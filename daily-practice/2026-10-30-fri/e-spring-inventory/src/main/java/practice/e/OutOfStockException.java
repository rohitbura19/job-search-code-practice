package practice.e;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String sku, int qty) { super("Cannot reserve " + qty + " of " + sku); }
}

package practice.e;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String orderNo) { super("order " + orderNo + " not found"); }
}

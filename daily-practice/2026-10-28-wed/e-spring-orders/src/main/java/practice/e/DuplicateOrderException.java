package practice.e;

public class DuplicateOrderException extends RuntimeException {
    public DuplicateOrderException(String orderNo) { super("order " + orderNo + " already exists"); }
}

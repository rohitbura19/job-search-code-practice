package practice.e;

public class IllegalStateTransitionException extends RuntimeException {
    public IllegalStateTransitionException(String orderNo, OrderStatus from, OrderStatus to) {
        super("order " + orderNo + ": cannot go from " + from + " to " + to);
    }
}

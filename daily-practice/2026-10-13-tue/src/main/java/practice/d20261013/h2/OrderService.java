package practice.d20261013.h2;

/**
 * H2 part 3 - Dependency Inversion. Depend on the two interfaces (constructor injection, both non-null).
 * place(o): save the order FIRST, then send the message "Placed" to the customer's email. If save fails, nothing is sent.
 */
public class OrderService {
    public OrderService(OrderRepository repo, Notifier notifier) {
        throw new UnsupportedOperationException("TODO");
    }

    public void place(Order o) {
        throw new UnsupportedOperationException("TODO");
    }
}

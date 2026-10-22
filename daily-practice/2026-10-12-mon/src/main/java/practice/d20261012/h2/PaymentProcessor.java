package practice.d20261012.h2;

/**
 * H2 question 4 - Open/Closed: no if/else on the payment type. A new payment type is a new PaymentStrategy
 * registered here; this class never changes. Test: PaymentProcessorTest.
 */
public class PaymentProcessor {

    /** Registering a second strategy with the same type throws IllegalStateException. */
    public void register(PaymentStrategy strategy) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Delegates to the strategy registered for `type`. Unknown type: IllegalArgumentException. */
    public String pay(String type, long amountPaise) {
        throw new UnsupportedOperationException("TODO");
    }
}

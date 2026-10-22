package practice.d20261012.h2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentProcessorTest {

    static PaymentStrategy strategy(String type, String prefix) {
        return new PaymentStrategy() {
            public String type() { return type; }
            public String pay(long amountPaise) { return prefix + amountPaise; }
        };
    }

    @Test @DisplayName("routes to the registered strategy")
    void routes() {
        PaymentProcessor p = new PaymentProcessor();
        p.register(strategy("UPI", "upi:"));
        p.register(strategy("CARD", "card:"));
        assertEquals("upi:100", p.pay("UPI", 100));
        assertEquals("card:250", p.pay("CARD", 250));
    }

    @Test @DisplayName("open/closed: a brand-new payment type works without touching PaymentProcessor")
    void newTypeWithoutEdits() {
        PaymentProcessor p = new PaymentProcessor();
        p.register(strategy("UPI", "upi:"));
        p.register(strategy("WALLET", "wallet:"));
        assertEquals("wallet:5", p.pay("WALLET", 5));
    }

    @Test @DisplayName("unknown type -> IllegalArgumentException; duplicate registration -> IllegalStateException")
    void errors() {
        PaymentProcessor p = new PaymentProcessor();
        p.register(strategy("UPI", "upi:"));
        assertThrows(IllegalArgumentException.class, () -> p.pay("CASH", 1));
        assertThrows(IllegalStateException.class, () -> p.register(strategy("UPI", "again:")));
        assertEquals("upi:1", p.pay("UPI", 1));
    }
}

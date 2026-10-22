package practice.d20261012.h2;

/** GIVEN - one payment type. type() is its key, for example "UPI" or "CARD". */
public interface PaymentStrategy {
    String type();
    /** Returns a receipt text. */
    String pay(long amountPaise);
}

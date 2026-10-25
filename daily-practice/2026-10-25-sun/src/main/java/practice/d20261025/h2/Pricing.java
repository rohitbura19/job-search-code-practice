package practice.d20261025.h2;

/** Computes the payable amount of an order in paise. */
public interface Pricing {
    long priceOf(Order order);
}

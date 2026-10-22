package practice.d20261022.b1;

import java.util.*;

public final class OrderStats {
    private OrderStats() {}

    /** Revenue per customer, keys sorted. */
    public static Map<String, Long> revenueByCustomer(List<Order> orders) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Ids in input order. Key true: total >= thresholdCents. Both keys are always present. */
    public static Map<Boolean, List<String>> partitionByTotal(List<Order> orders, long thresholdCents) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Distinct categories per customer; customers sorted, categories sorted. */
    public static Map<String, TreeSet<String>> categoriesByCustomer(List<Order> orders) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Customer with the highest revenue; a tie goes to the alphabetically first name. */
    public static Optional<String> topCustomer(List<Order> orders) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Smallest and largest order total, computed in ONE pass with one collector; empty when no orders. */
    public static Optional<MinMax> minMaxTotal(List<Order> orders) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Ids of the n biggest orders: total descending, then id ascending. */
    public static List<String> topIds(List<Order> orders, int n) {
        throw new UnsupportedOperationException("TODO");
    }
}

package practice.d20261022.b1;

import static java.util.stream.Collectors.*;

import java.util.*;

/**
 * Each method is one pass over the list: O(n) (topIds O(n log n)).
 * groupingBy(classifier, TreeMap::new, downstream) is how you get a sorted map; the default is a HashMap.
 * partitioningBy always has both keys (groupingBy(boolean) would drop an empty group).
 * teeing feeds one stream into two collectors and merges; minBy/maxBy return Optional, so empty input is handled.
 * Cast qty to long BEFORE multiplying: int * int overflows first.
 */
public final class OrderStats {
    private OrderStats() {}

    private static long total(Order o) { return (long) o.qty() * o.unitCents(); }

    public static Map<String, Long> revenueByCustomer(List<Order> orders) {
        return orders.stream().collect(groupingBy(Order::customer, TreeMap::new, summingLong(OrderStats::total)));
    }

    public static Map<Boolean, List<String>> partitionByTotal(List<Order> orders, long thresholdCents) {
        return orders.stream().collect(partitioningBy(o -> total(o) >= thresholdCents, mapping(Order::id, toList())));
    }

    public static Map<String, TreeSet<String>> categoriesByCustomer(List<Order> orders) {
        return orders.stream().collect(groupingBy(Order::customer, TreeMap::new,
                mapping(Order::category, toCollection(TreeSet::new))));
    }

    public static Optional<String> topCustomer(List<Order> orders) {
        return revenueByCustomer(orders).entrySet().stream()
                .max(Map.Entry.<String, Long>comparingByValue()
                        .thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder())))
                .map(Map.Entry::getKey);
    }

    public static Optional<MinMax> minMaxTotal(List<Order> orders) {
        Comparator<Order> byTotal = Comparator.comparingLong(OrderStats::total);
        return orders.stream().collect(teeing(minBy(byTotal), maxBy(byTotal),
                (mn, mx) -> mn.isPresent() ? Optional.of(new MinMax(total(mn.get()), total(mx.get()))) : Optional.empty()));
    }

    public static List<String> topIds(List<Order> orders, int n) {
        return orders.stream()
                .sorted(Comparator.<Order>comparingLong(OrderStats::total).reversed().thenComparing(Order::id))
                .limit(n).map(Order::id).toList();
    }
}

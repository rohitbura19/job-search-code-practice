package practice.d20261026.b1;

import static java.util.stream.Collectors.*;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Each method is one stream pipeline, O(n) (O(n log n) for the sorted maps / top-n). Notes:
 * - filter SETTLED first, then group: downstream collectors never see other statuses.
 * - groupingBy(..., TreeMap::new, ...) gives a sorted map; counting() returns Long, summingLong a Long.
 * - toMap needs a merge function (duplicate keys otherwise throw) and a map supplier for ordering.
 * - teeing runs two collectors over the same single pass and combines them.
 * - sort(comparator).limit(n) is a full sort; for huge input and small n keep a bounded heap.
 */
public final class PaymentStats {
    private PaymentStats() {}

    private static boolean settled(Payment p) { return p.status() == Status.SETTLED; }

    public static Map<String, Long> settledTotalByMerchant(List<Payment> payments) {
        return payments.stream().filter(PaymentStats::settled)
                .collect(groupingBy(Payment::merchant, TreeMap::new, summingLong(Payment::amountPaise)));
    }

    public static Map<Status, Long> countByStatus(List<Payment> payments) {
        Map<Status, Long> out = new EnumMap<>(Status.class);
        for (Status s : Status.values()) out.put(s, 0L);
        out.putAll(payments.stream().collect(groupingBy(Payment::status, () -> new EnumMap<>(Status.class), counting())));
        return out;
    }

    public static Optional<String> topMerchant(List<Payment> payments) {
        return settledTotalByMerchant(payments).entrySet().stream()
                .max(Map.Entry.<String, Long>comparingByValue().thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder())))
                .map(Map.Entry::getKey);
    }

    public static Map<String, Payment> largestSettledPerMethod(List<Payment> payments) {
        Comparator<Payment> order = Comparator.comparingLong(Payment::amountPaise).thenComparing(Payment::id, Comparator.reverseOrder());
        return payments.stream().filter(PaymentStats::settled)
                .collect(toMap(Payment::method, p -> p, BinaryOperator.maxBy(order), TreeMap::new));
    }

    public static SortedMap<LocalDate, Double> averageSettledPerDay(List<Payment> payments) {
        return payments.stream().filter(PaymentStats::settled)
                .collect(groupingBy(Payment::date, TreeMap::new, averagingLong(Payment::amountPaise)));
    }

    public static Optional<AmountRange> settledRange(List<Payment> payments) {
        return payments.stream().filter(PaymentStats::settled).map(Payment::amountPaise)
                .collect(Collectors.teeing(minBy(Long::compare), maxBy(Long::compare),
                        (lo, hi) -> lo.isPresent() ? Optional.of(new AmountRange(lo.get(), hi.get())) : Optional.empty()));
    }

    public static Map<String, Set<String>> methodsByMerchant(List<Payment> payments) {
        return payments.stream().collect(groupingBy(Payment::merchant, TreeMap::new, mapping(Payment::method, toCollection(TreeSet::new))));
    }

    public static String topIds(List<Payment> payments, int n) {
        return payments.stream().filter(PaymentStats::settled)
                .sorted(Comparator.comparingLong(Payment::amountPaise).reversed().thenComparing(Payment::id))
                .limit(n).map(Payment::id).collect(joining(","));
    }
}

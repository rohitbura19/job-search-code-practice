package practice.d20261024.b1;

import static java.util.stream.Collectors.*;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BinaryOperator;

/**
 * 1: toMap WITHOUT a merge function throws IllegalStateException on a duplicate key; the 3-arg form merges, the 4-arg form picks
 * the map type. 2: groupingBy + mapping into a TreeSet (sorted, distinct), then wrap with List::copyOf via collectingAndThen.
 * 3: filter + min with a two-key comparator (Optional, never null). 4: a TreeMap (NavigableMap) keeps keys sorted and
 * subMap(from, true, to, true) is an O(log n + k) view. 5: toMap with BinaryOperator.maxBy.
 */
public final class InventoryStats {
    private InventoryStats() {}

    public static Map<String, Integer> totalBySku(List<Stock> stock) {
        return stock.stream().collect(toMap(Stock::sku, Stock::qty, Integer::sum, TreeMap::new));
    }

    public static Map<String, List<String>> warehousesBySku(List<Stock> stock) {
        return stock.stream().collect(groupingBy(Stock::sku, TreeMap::new,
                collectingAndThen(mapping(Stock::warehouse, toCollection(TreeSet::new)), List::copyOf)));
    }

    public static Optional<Stock> earliestExpiring(List<Stock> stock, LocalDate today) {
        return stock.stream()
                .filter(s -> !s.expiry().isBefore(today))
                .min(Comparator.comparing(Stock::expiry).thenComparing(Stock::sku).thenComparing(Stock::warehouse));
    }

    public static NavigableMap<LocalDate, Integer> qtyByExpiry(List<Stock> stock) {
        return stock.stream().collect(toMap(Stock::expiry, Stock::qty, Integer::sum, TreeMap::new));
    }

    public static int qtyExpiringBetween(NavigableMap<LocalDate, Integer> byExpiry, LocalDate from, LocalDate to) {
        if (from.isAfter(to)) return 0;
        return byExpiry.subMap(from, true, to, true).values().stream().mapToInt(Integer::intValue).sum();
    }

    public static Map<String, Stock> largestPerWarehouse(List<Stock> stock) {
        Comparator<Stock> bigger = Comparator.comparingInt(Stock::qty).thenComparing(Stock::sku, Comparator.reverseOrder());
        return stock.stream().collect(toMap(Stock::warehouse, s -> s, BinaryOperator.maxBy(bigger), TreeMap::new));
    }
}

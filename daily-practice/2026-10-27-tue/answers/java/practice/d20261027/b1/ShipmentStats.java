package practice.d20261027.b1;

import static java.util.stream.Collectors.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Each method is one pipeline, O(n) (sorted outputs O(n log n)).
 * Key tools: groupingBy with a downstream (summingLong, mapping, collectingAndThen), a TreeMap factory for sorted keys,
 * partitioningBy (always both keys), flatMap + distinct + sorted + joining, summarizingInt, counting + a tie-break comparator.
 */
public final class ShipmentStats {
    private ShipmentStats() {}

    public static Map<String, Long> totalCostByCarrier(List<Shipment> s) {
        return s.stream().collect(groupingBy(Shipment::carrier, summingLong(Shipment::costPaise)));
    }

    public static Map<String, Set<String>> carriersByWarehouse(List<Shipment> s) {
        return s.stream().collect(groupingBy(Shipment::warehouse, TreeMap::new, mapping(Shipment::carrier, toCollection(TreeSet::new))));
    }

    public static Map<String, String> heaviestIdByWarehouse(List<Shipment> s) {
        Comparator<Shipment> heavier = Comparator.comparingInt(Shipment::weightKg).thenComparing(Shipment::id, Comparator.reverseOrder());
        return s.stream().collect(groupingBy(Shipment::warehouse,
                collectingAndThen(maxBy(heavier), o -> o.orElseThrow().id())));
    }

    public static Map<Boolean, Long> delayedSplit(List<Shipment> s) {
        return s.stream().collect(partitioningBy(x -> x.status() == Shipment.Status.DELAYED, counting()));
    }

    public static String tagsCsv(List<Shipment> s) {
        return s.stream().flatMap(x -> x.tags().stream()).distinct().sorted().collect(joining(","));
    }

    public static Map<YearMonth, IntSummaryStatistics> weightStatsByMonth(List<Shipment> s) {
        return s.stream().collect(groupingBy(x -> YearMonth.from(x.shippedOn()), TreeMap::new, summarizingInt(Shipment::weightKg)));
    }

    public static List<String> topCarriersByCost(List<Shipment> s, int n) {
        return totalCostByCarrier(s).entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(n).map(Map.Entry::getKey).toList();
    }

    public static Optional<LocalDate> busiestDay(List<Shipment> s) {
        return s.stream().collect(groupingBy(Shipment::shippedOn, counting())).entrySet().stream()
                .max(Map.Entry.<LocalDate, Long>comparingByValue().thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder())))
                .map(Map.Entry::getKey);
    }
}

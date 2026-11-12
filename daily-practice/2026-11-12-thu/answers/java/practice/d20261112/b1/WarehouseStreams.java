package practice.d20261112.b1;

import static java.util.stream.Collectors.*;

import java.time.LocalDate;
import java.util.*;

/** One collector chain each; O(n) except sorting steps (O(s log s) over distinct SKUs). */
public class WarehouseStreams {
    public record Totals(int in, int out) {}

    public static Map<String, Integer> stockBySku(List<Movement> ms) {
        return ms.stream().collect(groupingBy(Movement::sku, TreeMap::new, summingInt(Movement::signedQty)));
    }

    public static Map<String, String> busiestWarehouse(List<Movement> ms) {
        Map<String, Map<String, Integer>> moved = ms.stream().collect(groupingBy(Movement::sku, TreeMap::new,
                groupingBy(Movement::warehouse, summingInt(Movement::qty))));
        Map<String, String> out = new TreeMap<>();
        moved.forEach((sku, byWh) -> out.put(sku, byWh.entrySet().stream()
                .max(Map.Entry.<String, Integer>comparingByValue().thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder())))
                .get().getKey()));
        return out;
    }

    public static List<String> negativeStockSkus(List<Movement> ms) {
        return stockBySku(ms).entrySet().stream().filter(e -> e.getValue() < 0).map(Map.Entry::getKey).toList();
    }

    public static Totals totals(List<Movement> ms) {
        return ms.stream().collect(teeing(
                filtering(m -> m.type().equals("IN"), summingInt(Movement::qty)),
                filtering(m -> m.type().equals("OUT"), summingInt(Movement::qty)),
                Totals::new));
    }

    public static Map<LocalDate, Set<String>> skusPerDay(List<Movement> ms) {
        return ms.stream().collect(groupingBy(Movement::date, TreeMap::new, mapping(Movement::sku, toCollection(TreeSet::new))));
    }

    public static String topOutgoing(List<Movement> ms, int k) {
        return ms.stream().filter(m -> m.type().equals("OUT"))
                .collect(groupingBy(Movement::sku, summingInt(Movement::qty)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(k).map(e -> e.getKey() + ":" + e.getValue()).collect(joining(","));
    }
}

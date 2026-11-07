package practice.d20261107.b1;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.function.Function;

/** One pass each. Key idea: downstream collectors (summingLong, collectingAndThen+maxBy, averagingLong) keep grouping to one traversal. */
public final class InventoryStreams {
    private InventoryStreams() {}

    public static Map<String, Long> stockByCategory(List<Product> ps) {
        return ps.stream().collect(groupingBy(Product::category, TreeMap::new, summingLong(Product::stock)));
    }

    public static Map<String, String> priciestPerCategory(List<Product> ps) {
        Comparator<Product> cmp = Comparator.comparingLong(Product::priceCents).thenComparing(Product::name, Comparator.reverseOrder());
        return ps.stream().collect(groupingBy(Product::category, TreeMap::new,
                collectingAndThen(maxBy(cmp), o -> o.orElseThrow().name())));
    }

    public static List<String> topTags(List<Product> ps, int n) {
        return ps.stream().flatMap(p -> p.tags().stream())
                .collect(groupingBy(Function.identity(), counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(n).map(Map.Entry::getKey).toList();
    }

    public static Map<Boolean, List<String>> splitByLowStock(List<Product> ps, int threshold) {
        return ps.stream().collect(partitioningBy(p -> p.stock() < threshold,
                collectingAndThen(mapping(Product::name, toList()), l -> l.stream().sorted().toList())));
    }

    public static Map<String, Double> averagePriceInStock(List<Product> ps) {
        return ps.stream().filter(p -> p.stock() > 0)
                .collect(groupingBy(Product::category, TreeMap::new, averagingLong(Product::priceCents)));
    }
}

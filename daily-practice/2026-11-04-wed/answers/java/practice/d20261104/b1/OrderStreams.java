package practice.d20261104.b1;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * groupingBy with a TreeMap factory fixes the key order; summingLong avoids int overflow; collectingAndThen post-processes a downstream result;
 * mapping + toCollection(TreeSet::new) gives distinct sorted values; the streak is a linear scan over the distinct sorted dates.
 */
public final class OrderStreams {
    private OrderStreams() {}

    public record Order(String id, String customer, String category, int cents, LocalDate date) {}

    public static Map<String, Long> totalByCustomer(List<Order> orders) {
        return orders.stream().collect(Collectors.groupingBy(Order::customer, TreeMap::new, Collectors.summingLong(Order::cents)));
    }

    public static List<String> topCustomers(List<Order> orders, int k) {
        return totalByCustomer(orders).entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(k).map(Map.Entry::getKey).toList();
    }

    public static Map<String, String> largestOrderIdPerCategory(List<Order> orders) {
        Comparator<Order> best = Comparator.comparingInt(Order::cents).reversed().thenComparing(Order::date).thenComparing(Order::id);
        return orders.stream().collect(Collectors.groupingBy(Order::category, TreeMap::new,
                Collectors.collectingAndThen(Collectors.minBy(best), o -> o.orElseThrow().id())));
    }

    public static Map<YearMonth, Long> revenueByMonth(List<Order> orders) {
        return orders.stream().collect(Collectors.groupingBy(o -> YearMonth.from(o.date()), TreeMap::new, Collectors.summingLong(Order::cents)));
    }

    public static String customersPerCategory(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::category, TreeMap::new, Collectors.mapping(Order::customer, Collectors.toCollection(TreeSet::new))))
                .entrySet().stream().map(e -> e.getKey() + ": " + String.join(",", e.getValue()))
                .collect(Collectors.joining("; "));
    }

    public static List<String> repeatCustomers(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::customer, Collectors.mapping(Order::date, Collectors.toSet())))
                .entrySet().stream().filter(e -> e.getValue().size() >= 2).map(Map.Entry::getKey).sorted().toList();
    }

    public static int longestDailyStreak(List<Order> orders) {
        List<LocalDate> days = orders.stream().map(Order::date).distinct().sorted().toList();
        int best = 0, run = 0;
        for (int i = 0; i < days.size(); i++) {
            run = i > 0 && days.get(i - 1).plusDays(1).equals(days.get(i)) ? run + 1 : 1;
            best = Math.max(best, run);
        }
        return best;
    }
}

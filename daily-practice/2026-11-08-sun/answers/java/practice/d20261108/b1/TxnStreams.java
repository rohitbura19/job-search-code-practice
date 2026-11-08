package practice.d20261108.b1;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.function.BinaryOperator;

/**
 * groupingBy with a TreeMap factory keeps keys sorted without a second sort; collectingAndThen post-processes a
 * downstream result; toMap with BinaryOperator.maxBy resolves collisions in one pass; partitioningBy always has both keys.
 * All O(n) except sorted outputs, O(n log n).
 */
public final class TxnStreams {
    private TxnStreams() {}

    public static Map<String, Long> totalByAccount(List<Txn> txns) {
        return txns.stream().collect(groupingBy(Txn::account, TreeMap::new, summingLong(Txn::cents)));
    }

    public static Map<String, List<String>> topSpendCategories(List<Txn> txns, int n) {
        return txns.stream().collect(groupingBy(Txn::account, TreeMap::new,
                collectingAndThen(groupingBy(Txn::category, summingLong(Txn::cents)),
                        byCategory -> byCategory.entrySet().stream()
                                .sorted(Map.Entry.<String, Long>comparingByValue().thenComparing(Map.Entry.comparingByKey()))
                                .limit(n).map(Map.Entry::getKey).toList())));
    }

    public static List<Txn> latestPerAccountAndCategory(List<Txn> txns) {
        Comparator<Txn> newer = Comparator.comparing(Txn::date).thenComparingLong(Txn::cents);
        return txns.stream()
                .collect(toMap(t -> t.account() + "\u0000" + t.category(), t -> t, BinaryOperator.maxBy(newer)))
                .values().stream()
                .sorted(Comparator.comparing(Txn::account).thenComparing(Txn::category))
                .toList();
    }

    public static Map<Boolean, Long> countLargeAndSmall(List<Txn> txns, long threshold) {
        return txns.stream().collect(partitioningBy(t -> Math.abs(t.cents()) >= threshold, counting()));
    }

    public static String summary(List<Txn> txns) {
        return totalByAccount(txns).entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue()).collect(joining(", ", "[", "]"));
    }
}

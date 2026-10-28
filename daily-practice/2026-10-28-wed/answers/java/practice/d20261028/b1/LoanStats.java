package practice.d20261028.b1;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * mapping + toCollection(TreeSet) for sorted sets; takeWhile stops at the first failure (filter would not);
 * summingLong then filter+sort the entries into a LinkedHashMap to fix the iteration order;
 * a tie-break is part of the comparator (count desc, title asc); a nested collector (collectingAndThen) post-processes each group.
 * All O(n log n) at worst.
 */
public final class LoanStats {
    private LoanStats() {}

    public static Map<String, Set<String>> genresByMember(List<Loan> loans) {
        return loans.stream().collect(Collectors.groupingBy(Loan::member, TreeMap::new,
                Collectors.mapping(Loan::genre, Collectors.toCollection(TreeSet::new))));
    }

    public static List<Loan> leadingReturned(List<Loan> loans) {
        return loans.stream().takeWhile(l -> l.returned() != null).toList();
    }

    public static Map<String, Long> overdueDays(List<Loan> loans, LocalDate today, int allowedDays) {
        Map<String, Long> total = loans.stream().collect(Collectors.groupingBy(Loan::member, Collectors.summingLong(l -> {
            LocalDate due = l.borrowed().plusDays(allowedDays);
            LocalDate end = l.returned() != null ? l.returned() : today;
            return Math.max(0, ChronoUnit.DAYS.between(due, end));
        })));
        return total.entrySet().stream().filter(e -> e.getValue() > 0)
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
    }

    public static Optional<String> mostBorrowed(List<Loan> loans) {
        return loans.stream().collect(Collectors.groupingBy(Loan::book, Collectors.counting())).entrySet().stream()
                .max(Map.Entry.<String, Long>comparingByValue().thenComparing(Map.Entry.<String, Long>comparingByKey().reversed()))
                .map(Map.Entry::getKey);
    }

    public static Map<String, List<String>> topBooksPerGenre(List<Loan> loans, int k) {
        return loans.stream().collect(Collectors.groupingBy(Loan::genre, TreeMap::new,
                Collectors.collectingAndThen(Collectors.groupingBy(Loan::book, Collectors.counting()),
                        counts -> counts.entrySet().stream()
                                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()).thenComparing(Map.Entry.comparingByKey()))
                                .limit(k).map(Map.Entry::getKey).toList())));
    }

    public static String holdersCsv(List<Loan> loans) {
        return loans.stream().filter(l -> l.returned() == null).map(Loan::member).distinct().sorted()
                .collect(Collectors.joining(", ", "[", "]"));
    }
}

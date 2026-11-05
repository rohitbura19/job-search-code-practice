package practice.d20261105.b1;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Notes: distinctTags lower-cases first, then distinct (otherwise "Java" and "java" both survive). titlesByAuthor sorts the whole list once by
 * (year, title) so each group's list is already ordered; TreeMap::new gives the author order. toMap needs a merge function or it throws
 * IllegalStateException on a duplicate key; the 4-argument form picks the map type. Comparator.nullsLast wraps the rating comparator.
 * All O(n log n) at worst (sorting), others O(n).
 */
public final class LibraryStreams {
    private LibraryStreams() {}

    public record Book(String title, String author, int year, int pages, List<String> tags, Double rating) {}

    public static List<String> distinctTags(List<Book> books) {
        return books.stream().flatMap(b -> b.tags().stream()).map(t -> t.toLowerCase(Locale.ROOT)).distinct().sorted().toList();
    }

    public static Map<String, String> titlesByAuthor(List<Book> books) {
        return books.stream()
            .sorted(Comparator.comparingInt(Book::year).thenComparing(Book::title))
            .collect(Collectors.groupingBy(Book::author, TreeMap::new, Collectors.mapping(Book::title, Collectors.joining("; "))));
    }

    public static Map<Integer, Long> booksPerDecade(List<Book> books) {
        return books.stream().collect(Collectors.groupingBy(b -> b.year() / 10 * 10, TreeMap::new, Collectors.counting()));
    }

    public static IntSummaryStatistics pageStats(List<Book> books) {
        return books.stream().mapToInt(Book::pages).summaryStatistics();
    }

    public static List<Book> byRatingDescNullsLast(List<Book> books) {
        return books.stream()
            .sorted(Comparator.comparing(Book::rating, Comparator.nullsLast(Comparator.<Double>reverseOrder())).thenComparing(Book::title))
            .toList();
    }

    public static Map<String, Book> longestPerAuthor(List<Book> books) {
        Comparator<Book> better = Comparator.comparingInt(Book::pages).thenComparing(Book::title, Comparator.reverseOrder());
        return books.stream().collect(Collectors.toMap(Book::author, b -> b, (x, y) -> better.compare(x, y) >= 0 ? x : y, LinkedHashMap::new));
    }

    public static OptionalDouble averageRating(List<Book> books) {
        return books.stream().map(Book::rating).filter(Objects::nonNull).mapToDouble(Double::doubleValue).average();
    }
}

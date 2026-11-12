package s20_library;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

public final class LibraryStreams {
    private LibraryStreams() {}

    /** Given. rating may be null (unrated). */
    public record Book(String title, String author, int year, int pages, List<String> tags, Double rating) {}

    /** All distinct tags across the books, case-insensitively distinct (keep the lower-case form), sorted alphabetically. flatMap + map + distinct + sorted. */
    public static List<String> distinctTags(List<Book> books) { throw new UnsupportedOperationException("TODO"); }

    /** author -> titles joined with "; ", titles ordered by year then title. Authors in alphabetical order (TreeMap). groupingBy + mapping + collectingAndThen/joining. */
    public static Map<String, String> titlesByAuthor(List<Book> books) { throw new UnsupportedOperationException("TODO"); }

    /** decade (year / 10 * 10) -> number of books, ascending decades. */
    public static Map<Integer, Long> booksPerDecade(List<Book> books) { throw new UnsupportedOperationException("TODO"); }

    /** count, min, max, sum, average of pages in one pass. */
    public static IntSummaryStatistics pageStats(List<Book> books) { throw new UnsupportedOperationException("TODO"); }

    /** Highest rating first, unrated last, ties by title ascending. Does not modify the input. Comparator.nullsLast. */
    public static List<Book> byRatingDescNullsLast(List<Book> books) { throw new UnsupportedOperationException("TODO"); }

    /** author -> the book with most pages (tie: the alphabetically smaller title). Iteration order = order of first appearance of each author. toMap with merge + LinkedHashMap. */
    public static Map<String, Book> longestPerAuthor(List<Book> books) { throw new UnsupportedOperationException("TODO"); }

    /** Average rating of the rated books only; empty if none are rated. */
    public static OptionalDouble averageRating(List<Book> books) { throw new UnsupportedOperationException("TODO"); }
}

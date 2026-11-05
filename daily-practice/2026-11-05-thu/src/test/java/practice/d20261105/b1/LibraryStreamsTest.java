package practice.d20261105.b1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import org.junit.jupiter.api.Test;
import practice.d20261105.b1.LibraryStreams.Book;

class LibraryStreamsTest {
    static Book b(String t, String a, int y, int p, Double r, String... tags) { return new Book(t, a, y, p, List.of(tags), r); }

    final List<Book> books = List.of(
        b("Dune", "Herbert", 1965, 412, 4.5, "SciFi", "classic"),
        b("Emma", "Austen", 1815, 474, 3.9, "classic", "Romance"),
        b("Children of Dune", "Herbert", 1976, 444, null, "scifi"),
        b("Persuasion", "Austen", 1817, 249, 4.1, "romance"),
        b("Neuromancer", "Gibson", 1984, 271, 4.1),
        b("Dune Messiah", "Herbert", 1969, 331, 3.8, "SCIFI"));

    @Test void distinctTags() {
        assertEquals(List.of("classic", "romance", "scifi"), LibraryStreams.distinctTags(books));
        assertEquals(List.of(), LibraryStreams.distinctTags(List.of()));
        assertEquals(List.of(), LibraryStreams.distinctTags(List.of(b("x", "y", 2000, 1, null))));
    }

    @Test void titlesByAuthor() {
        Map<String, String> m = LibraryStreams.titlesByAuthor(books);
        assertEquals(List.of("Austen", "Gibson", "Herbert"), List.copyOf(m.keySet()));
        assertEquals("Emma; Persuasion", m.get("Austen"));
        assertEquals("Dune; Dune Messiah; Children of Dune", m.get("Herbert"));
        assertEquals(Map.of(), LibraryStreams.titlesByAuthor(List.of()));
    }

    @Test void sameYearOrdersByTitle() {
        var m = LibraryStreams.titlesByAuthor(List.of(b("B", "X", 2000, 1, null), b("A", "X", 2000, 1, null), b("C", "X", 1999, 1, null)));
        assertEquals("C; A; B", m.get("X"));
    }

    @Test void booksPerDecade() {
        Map<Integer, Long> m = LibraryStreams.booksPerDecade(books);
        assertEquals(List.of(1810, 1960, 1970, 1980), List.copyOf(m.keySet()));
        assertEquals(2L, m.get(1810));
        assertEquals(2L, m.get(1960));
        assertEquals(1L, m.get(1970));
        assertEquals(Map.of(), LibraryStreams.booksPerDecade(List.of()));
    }

    @Test void pageStats() {
        var s = LibraryStreams.pageStats(books);
        assertEquals(6, s.getCount());
        assertEquals(249, s.getMin());
        assertEquals(474, s.getMax());
        assertEquals(2181L, s.getSum());
        assertEquals(2181 / 6.0, s.getAverage(), 1e-9);
        assertEquals(0, LibraryStreams.pageStats(List.of()).getCount());
    }

    @Test void byRatingDescNullsLast() {
        List<Book> copy = new ArrayList<>(books);
        List<String> titles = LibraryStreams.byRatingDescNullsLast(books).stream().map(Book::title).toList();
        assertEquals(List.of("Dune", "Neuromancer", "Persuasion", "Emma", "Dune Messiah", "Children of Dune"), titles);
        assertEquals(copy, books);
        List<Book> shuffled = new ArrayList<>(books);
        Collections.reverse(shuffled);
        assertEquals(titles, LibraryStreams.byRatingDescNullsLast(shuffled).stream().map(Book::title).toList());
    }

    @Test void longestPerAuthorKeepsFirstAppearanceOrder() {
        Map<String, Book> m = LibraryStreams.longestPerAuthor(books);
        assertEquals(List.of("Herbert", "Austen", "Gibson"), List.copyOf(m.keySet()));
        assertEquals("Children of Dune", m.get("Herbert").title());
        assertEquals("Emma", m.get("Austen").title());
    }

    @Test void longestPerAuthorTieTakesSmallerTitle() {
        var tie1 = List.of(b("Zed", "A", 2000, 100, null), b("Alpha", "A", 2001, 100, null));
        var tie2 = List.of(b("Alpha", "A", 2001, 100, null), b("Zed", "A", 2000, 100, null));
        assertEquals("Alpha", LibraryStreams.longestPerAuthor(tie1).get("A").title());
        assertEquals("Alpha", LibraryStreams.longestPerAuthor(tie2).get("A").title());
    }

    @Test void averageRatingIgnoresUnrated() {
        assertEquals((4.5 + 3.9 + 4.1 + 4.1 + 3.8) / 5, LibraryStreams.averageRating(books).getAsDouble(), 1e-9);
        assertEquals(OptionalDouble.empty(), LibraryStreams.averageRating(List.of(b("x", "y", 1, 1, null))));
        assertFalse(LibraryStreams.averageRating(List.of()).isPresent());
        assertTrue(LibraryStreams.averageRating(List.of(b("x", "y", 1, 1, 0.0))).isPresent());
    }

    @Test void bigInputPagesSumDoesNotOverflowInStats() {
        List<Book> many = new ArrayList<>();
        for (int i = 0; i < 10; i++) many.add(b("t" + i, "a", 2000, Integer.MAX_VALUE, null));
        assertEquals(10L * Integer.MAX_VALUE, LibraryStreams.pageStats(many).getSum());
    }
}

package practice.d20261031.b1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MovieStatsTest {
    static Rating r(String u, String m, String g, int s) { return new Rating(u, m, g, s); }

    static final List<Rating> DATA = List.of(
            r("ann", "Heat", "crime", 5), r("ann", "Alien", "scifi", 3), r("ann", "Up", "family", 4),
            r("bob", "Heat", "crime", 4), r("bob", "Alien", "scifi", 2),
            r("cy", "Up", "family", 1), r("cy", "Heat", "crime", 5), r("cy", "Dune", "scifi", 5));

    @Test void avgByGenreSorted() {
        Map<String, Double> m = MovieStats.avgByGenre(DATA);
        assertEquals(List.of("crime", "family", "scifi"), List.copyOf(m.keySet()));
        assertEquals(14.0 / 3, m.get("crime"), 1e-9);
        assertEquals(2.5, m.get("family"), 1e-9);
        assertEquals(10.0 / 3, m.get("scifi"), 1e-9);
        assertTrue(MovieStats.avgByGenre(List.of()).isEmpty());
    }

    @Test void likedKeepsUsersWithNoLikes() {
        Map<String, Set<String>> m = MovieStats.likedMoviesByUser(DATA);
        assertEquals(List.of("ann", "bob", "cy"), List.copyOf(m.keySet()));
        assertEquals(List.of("Heat", "Up"), List.copyOf(m.get("ann")));
        assertEquals(List.of("Heat"), List.copyOf(m.get("bob")));
        Map<String, Set<String>> none = MovieStats.likedMoviesByUser(List.of(r("zed", "X", "g", 1)));
        assertEquals(Map.of("zed", Set.of()), none);
    }

    @Test void histogramHasAllBuckets() {
        assertEquals("{1=1, 2=1, 3=1, 4=2, 5=3}", MovieStats.histogram(DATA).toString());
        assertEquals("{1=0, 2=0, 3=0, 4=0, 5=0}", MovieStats.histogram(List.of()).toString());
    }

    @Test void topMovieRules() {
        assertEquals(Optional.of("Heat"), MovieStats.topMovie(DATA, 2));        // Heat 4.67 (3 votes); Up 2.5; Alien 2.5
        assertEquals(Optional.of("Dune"), MovieStats.topMovie(DATA, 1));        // Dune 5.0 with one vote
        assertEquals(Optional.empty(), MovieStats.topMovie(DATA, 4));
        assertEquals(Optional.empty(), MovieStats.topMovie(List.of(), 1));
        // tie on average: more votes wins, then name
        List<Rating> tie = List.of(r("a", "B", "g", 4), r("b", "B", "g", 4), r("a", "A", "g", 4), r("a", "C", "g", 4), r("b", "C", "g", 4));
        assertEquals(Optional.of("B"), MovieStats.topMovie(tie, 1));
        assertEquals(Optional.of("A"), MovieStats.topMovie(List.of(r("a", "B", "g", 4), r("a", "A", "g", 4)), 1));
    }

    @Test void bestAndWorst() {
        assertEquals("best=crime worst=family", MovieStats.bestAndWorstGenre(DATA));
        assertEquals("none", MovieStats.bestAndWorstGenre(List.of()));
        assertEquals("best=a worst=a", MovieStats.bestAndWorstGenre(List.of(r("u", "m", "a", 3), r("u", "n", "b", 3))));
    }

    @Test void explorers() {
        assertEquals(List.of("ann", "cy"), MovieStats.genreExplorers(DATA, 3));
        assertEquals(List.of("ann", "bob", "cy"), MovieStats.genreExplorers(DATA, 2));
        assertEquals(List.of(), MovieStats.genreExplorers(DATA, 4));
        assertEquals(List.of(), MovieStats.genreExplorers(List.of(), 1));
    }

    @Test void largeInputIsFast() {
        Random rnd = new Random(5);
        List<Rating> big = new ArrayList<>();
        for (int i = 0; i < 300_000; i++) big.add(r("u" + rnd.nextInt(1000), "m" + rnd.nextInt(500), "g" + rnd.nextInt(8), 1 + rnd.nextInt(5)));
        org.junit.jupiter.api.Assertions.assertTimeoutPreemptively(java.time.Duration.ofSeconds(5), () -> {
            MovieStats.avgByGenre(big); MovieStats.histogram(big); MovieStats.topMovie(big, 10);
            MovieStats.bestAndWorstGenre(big); MovieStats.genreExplorers(big, 5); MovieStats.likedMoviesByUser(big);
        });
    }
}

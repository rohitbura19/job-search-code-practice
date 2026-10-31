package practice.d20261031.b1;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class MovieStats {
    private MovieStats() {}

    /** genre -> average stars, keys sorted. */
    public static Map<String, Double> avgByGenre(List<Rating> ratings) { throw new UnsupportedOperationException("TODO"); }

    /** every user -> sorted set of movies they rated >= 4. A user with no such movie still appears, with an empty set. Keys sorted. */
    public static Map<String, java.util.Set<String>> likedMoviesByUser(List<Rating> ratings) { throw new UnsupportedOperationException("TODO"); }

    /** stars 1..5 -> count; all five keys present, in order, zero when absent. */
    public static Map<Integer, Long> histogram(List<Rating> ratings) { throw new UnsupportedOperationException("TODO"); }

    /** movie with the highest average among movies with >= minVotes ratings; ties: more votes, then name ascending. */
    public static Optional<String> topMovie(List<Rating> ratings, int minVotes) { throw new UnsupportedOperationException("TODO"); }

    /** "best=<genre> worst=<genre>" by average stars (ties: name ascending); "none" for empty input. Use Collectors.teeing in ONE pass over the averages. */
    public static String bestAndWorstGenre(List<Rating> ratings) { throw new UnsupportedOperationException("TODO"); }

    /** users who rated movies in at least n distinct genres, sorted. Use groupingBy + Collectors.flatMapping (or mapping). */
    public static List<String> genreExplorers(List<Rating> ratings, int n) { throw new UnsupportedOperationException("TODO"); }
}

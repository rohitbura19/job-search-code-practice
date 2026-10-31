package practice.d20261031.b1;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Each method is one pass over the ratings (plus a sort over distinct keys). The points of the exercise:
 * - Collectors.filtering vs Stream.filter: filtering inside groupingBy keeps the group (empty set) instead of dropping the key.
 * - histogram: seed 1..5 with zeros, then merge counts, so absent keys show as 0.
 * - teeing: two downstream collectors, one traversal, then a merger.
 * - flatMapping: flatten each group's elements before collecting.
 * Complexity: O(n) for the grouping, O(m log m) when sorting m distinct keys.
 */
public final class MovieStats {
    private MovieStats() {}

    public static Map<String, Double> avgByGenre(List<Rating> ratings) {
        return ratings.stream().collect(Collectors.groupingBy(Rating::genre, TreeMap::new, Collectors.averagingInt(Rating::stars)));
    }

    public static Map<String, Set<String>> likedMoviesByUser(List<Rating> ratings) {
        return ratings.stream().collect(Collectors.groupingBy(Rating::user, TreeMap::new,
                Collectors.filtering(r -> r.stars() >= 4, Collectors.mapping(Rating::movie, Collectors.toCollection(TreeSet::new)))));
    }

    public static Map<Integer, Long> histogram(List<Rating> ratings) {
        Map<Integer, Long> h = new TreeMap<>();
        IntStream.rangeClosed(1, 5).forEach(s -> h.put(s, 0L));
        ratings.forEach(r -> h.merge(r.stars(), 1L, Long::sum));
        return h;
    }

    public static Optional<String> topMovie(List<Rating> ratings, int minVotes) {
        record Agg(String movie, double avg, long votes) {}
        return ratings.stream()
                .collect(Collectors.groupingBy(Rating::movie, Collectors.summarizingInt(Rating::stars)))
                .entrySet().stream()
                .filter(e -> e.getValue().getCount() >= minVotes)
                .map(e -> new Agg(e.getKey(), e.getValue().getAverage(), e.getValue().getCount()))
                .min(Comparator.comparingDouble(Agg::avg).reversed().thenComparing(Comparator.comparingLong(Agg::votes).reversed()).thenComparing(Agg::movie))
                .map(Agg::movie);
    }

    public static String bestAndWorstGenre(List<Rating> ratings) {
        if (ratings.isEmpty()) return "none";
        Comparator<Map.Entry<String, Double>> byAvgThenName = Map.Entry.<String, Double>comparingByValue().thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder()));
        return avgByGenre(ratings).entrySet().stream().collect(Collectors.teeing(
                Collectors.maxBy(byAvgThenName),          // highest avg; on a tie the smaller name is "greater" under reverseOrder
                Collectors.minBy(Map.Entry.<String, Double>comparingByValue().thenComparing(Map.Entry.comparingByKey())),
                (best, worst) -> "best=" + best.orElseThrow().getKey() + " worst=" + worst.orElseThrow().getKey()));
    }

    public static List<String> genreExplorers(List<Rating> ratings, int n) {
        return ratings.stream()
                .collect(Collectors.groupingBy(Rating::user, Collectors.mapping(Rating::genre, Collectors.toSet())))
                .entrySet().stream().filter(e -> e.getValue().size() >= n).map(Map.Entry::getKey).sorted().toList();
    }
}

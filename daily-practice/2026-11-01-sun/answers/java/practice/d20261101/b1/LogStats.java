package practice.d20261101.b1;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.maxBy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * countByLevel: groupingBy with a TreeMap factory + counting. slowest: maxBy with a two-key comparator, unwrapped by collectingAndThen.
 * topWords: flatMap to tokens, group+count, sort the entries (count desc, word asc), limit n.
 * firstBurst: dropWhile/takeWhile on an ordered stream (they stop early; filter would collect all bursts).
 * fibonacciBelow: iterate over pairs; the saturating add keeps the sequence monotone, so takeWhile ends before MAX_VALUE.
 * expandRepeats: mapMulti pushes 0..k elements per input without creating a stream per element.
 * All O(n) except sorting O(m log m) for m distinct words.
 */
public final class LogStats {
    private LogStats() {}

    public static SortedMap<String, Long> countByLevel(List<LogLine> logs) {
        return logs.stream().collect(groupingBy(LogLine::level, TreeMap::new, counting()));
    }

    public static Map<String, String> slowestMessagePerService(List<LogLine> logs) {
        Comparator<LogLine> slower = Comparator.comparingInt(LogLine::ms).thenComparing(LogLine::msg, Comparator.reverseOrder());
        return logs.stream().collect(groupingBy(LogLine::service,
                collectingAndThen(maxBy(slower), o -> o.orElseThrow().msg())));
    }

    public static List<String> topWords(List<LogLine> logs, int n) {
        if (n <= 0) return List.of();
        return logs.stream()
                .flatMap(l -> Arrays.stream(l.msg().toLowerCase().split("[^a-z]+")))
                .filter(w -> w.length() >= 3)
                .collect(groupingBy(Function.identity(), counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }

    public static List<Integer> firstBurst(int[] ms, int limit) {
        return IntStream.of(ms).boxed().dropWhile(v -> v <= limit).takeWhile(v -> v > limit).toList();
    }

    public static List<Long> fibonacciBelow(long limit) {
        return Stream.iterate(new long[] {0, 1}, p -> new long[] {p[1], satAdd(p[0], p[1])})
                .map(p -> p[0])
                .takeWhile(v -> v < limit)
                .toList();
    }

    private static long satAdd(long a, long b) {
        long s = a + b;
        return s < 0 ? Long.MAX_VALUE : s;
    }

    public static List<String> expandRepeats(List<String> specs) {
        return specs.stream().<String>mapMulti((spec, sink) -> {
            int star = spec.lastIndexOf('*');
            if (star < 0) { sink.accept(spec); return; }
            String s = spec.substring(0, star);
            for (int i = Integer.parseInt(spec.substring(star + 1)); i > 0; i--) sink.accept(s);
        }).toList();
    }
}

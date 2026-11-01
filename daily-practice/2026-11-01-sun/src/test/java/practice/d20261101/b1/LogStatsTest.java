package practice.d20261101.b1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Test;

class LogStatsTest {
    static final List<LogLine> LOGS = List.of(
            new LogLine("auth", "ERROR", 120, "Token expired for user"),
            new LogLine("auth", "INFO", 15, "User login ok"),
            new LogLine("pay", "WARN", 900, "Slow gateway call"),
            new LogLine("pay", "ERROR", 900, "Gateway timeout"),
            new LogLine("pay", "INFO", 40, "Charge ok for user"),
            new LogLine("search", "INFO", 8, "Query ok"));

    @Test void countByLevelIsSortedAndCounts() {
        var m = LogStats.countByLevel(LOGS);
        assertEquals(List.of("ERROR", "INFO", "WARN"), new ArrayList<>(m.keySet()));
        assertEquals(Map.of("ERROR", 2L, "INFO", 3L, "WARN", 1L), m);
        assertTrue(LogStats.countByLevel(List.of()).isEmpty());
    }

    @Test void slowestMessageTieBreaksOnSmallerMessage() {
        assertEquals(Map.of("auth", "Token expired for user", "pay", "Gateway timeout", "search", "Query ok"),
                LogStats.slowestMessagePerService(LOGS));
        assertTrue(LogStats.slowestMessagePerService(List.of()).isEmpty());
    }

    @Test void topWords() {
        assertEquals(List.of("user", "for", "gateway"), LogStats.topWords(LOGS, 3));
        assertEquals(List.of("user"), LogStats.topWords(LOGS, 1));
        assertEquals(List.of(), LogStats.topWords(LOGS, 0));
        assertEquals(List.of(), LogStats.topWords(List.of(), 5));
        assertEquals(List.of("abc", "xyz"), LogStats.topWords(List.of(new LogLine("s", "I", 1, "1 abc, ABC! xyz-- a to")), 10).subList(0, 2));
    }

    @Test void firstBurst() {
        assertEquals(List.of(7, 9, 8), LogStats.firstBurst(new int[] {1, 2, 7, 9, 8, 1, 10, 11}, 5));
        assertEquals(List.of(), LogStats.firstBurst(new int[] {1, 2}, 5));
        assertEquals(List.of(), LogStats.firstBurst(new int[0], 5));
        assertEquals(List.of(6, 7), LogStats.firstBurst(new int[] {6, 7}, 5));
        assertEquals(List.of(), LogStats.firstBurst(new int[] {5, 5}, 5));
    }

    @Test void fibonacci() {
        assertEquals(List.of(), LogStats.fibonacciBelow(0));
        assertEquals(List.of(0L), LogStats.fibonacciBelow(1));
        assertEquals(List.of(0L, 1L, 1L, 2L, 3L, 5L, 8L), LogStats.fibonacciBelow(10));
    }

    @Test void fibonacciNearOverflow() {
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            List<Long> all = LogStats.fibonacciBelow(Long.MAX_VALUE);
            assertEquals(93, all.size());                          // F(0)..F(92)
            assertEquals(7540113804746346429L, all.get(all.size() - 1));
            for (int i = 1; i < all.size(); i++) assertTrue(all.get(i) >= all.get(i - 1));
        });
    }

    @Test void expandRepeats() {
        assertEquals(List.of("ab", "ab", "ab", "x", "q"), LogStats.expandRepeats(List.of("ab*3", "x", "z*0", "q*1")));
        assertEquals(List.of(), LogStats.expandRepeats(List.of()));
        assertEquals(List.of("a*b", "a*b"), LogStats.expandRepeats(List.of("a*b*2")));   // split at the LAST star
    }

    @Test void largeInputWithinTimeout() {
        Random r = new Random(1);
        List<LogLine> big = new ArrayList<>();
        for (int i = 0; i < 200_000; i++) big.add(new LogLine("s" + r.nextInt(50), "L" + r.nextInt(5), r.nextInt(1000), "word" + (char) ('a' + r.nextInt(26)) + " other"));
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            assertEquals(200_000L, LogStats.countByLevel(big).values().stream().mapToLong(Long::longValue).sum());
            assertEquals(50, LogStats.slowestMessagePerService(big).size());
            assertEquals("other", LogStats.topWords(big, 1).get(0));
        });
    }
}

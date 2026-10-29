package practice.d20261029.b1;

import static java.util.stream.Collectors.*;

import java.util.*;
import java.util.stream.Collectors;

/** Reference solutions. Each method is one stream pipeline; sorted maps/lists make the output deterministic. */
public final class TicketStats {
    private TicketStats() {}

    public static Map<String, Long> openCountByTeam(List<Ticket> tickets) {
        return tickets.stream().filter(t -> !t.resolved())
                .collect(groupingBy(Ticket::team, TreeMap::new, counting()));
    }

    public static Map<Boolean, List<String>> splitUrgent(List<Ticket> tickets) {
        return tickets.stream().collect(partitioningBy(t -> t.priority() <= 2,
                mapping(Ticket::id, collectingAndThen(toList(), l -> l.stream().sorted().toList()))));
    }

    public static Map<String, Double> avgResolveHoursByTeam(List<Ticket> tickets) {
        return tickets.stream().filter(Ticket::resolved)
                .collect(groupingBy(Ticket::team, TreeMap::new, averagingInt(Ticket::hoursToResolve)));
    }

    public static Optional<String> busiestAssignee(List<Ticket> tickets) {
        return tickets.stream().filter(t -> t.assignee() != null && !t.resolved())
                .collect(groupingBy(Ticket::assignee, counting()))
                .entrySet().stream()
                .min(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .map(Map.Entry::getKey);
    }

    public static Optional<Range> resolveRange(List<Ticket> tickets) {
        return tickets.stream().filter(Ticket::resolved).map(Ticket::hoursToResolve)
                .collect(Collectors.teeing(
                        minBy(Comparator.<Integer>naturalOrder()),
                        maxBy(Comparator.<Integer>naturalOrder()),
                        (lo, hi) -> lo.isPresent() ? Optional.of(new Range(lo.get(), hi.get())) : Optional.empty()));
    }

    public static List<String> topAssignees(List<Ticket> tickets, int k) {
        return tickets.stream().filter(t -> t.assignee() != null && !t.resolved())
                .collect(groupingBy(Ticket::assignee, counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(Math.max(k, 0)).map(Map.Entry::getKey).toList();
    }
}

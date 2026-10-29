package practice.d20261029.b1;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/** One stream pipeline per method. "Open" means not resolved. Do not mutate the input. */
public final class TicketStats {
    private TicketStats() {}

    /** Number of open tickets per team, keys in alphabetical order; teams with no open ticket are absent. */
    public static Map<String, Long> openCountByTeam(List<Ticket> tickets) { throw new UnsupportedOperationException("TODO"); }

    /** partitioningBy: key true = priority 1 or 2, key false = the rest; values are ticket ids sorted ascending. Both keys always present. */
    public static Map<Boolean, List<String>> splitUrgent(List<Ticket> tickets) { throw new UnsupportedOperationException("TODO"); }

    /** Average hoursToResolve of RESOLVED tickets per team, keys alphabetical; teams with no resolved ticket are absent. */
    public static Map<String, Double> avgResolveHoursByTeam(List<Ticket> tickets) { throw new UnsupportedOperationException("TODO"); }

    /** Assignee with the most OPEN tickets (ties: alphabetically first); unassigned tickets ignored; empty if none. */
    public static Optional<String> busiestAssignee(List<Ticket> tickets) { throw new UnsupportedOperationException("TODO"); }

    /** Min and max hoursToResolve over resolved tickets in ONE pass using Collectors.teeing; empty if nothing is resolved. */
    public static Optional<Range> resolveRange(List<Ticket> tickets) { throw new UnsupportedOperationException("TODO"); }

    /** The k assignees with most open tickets, count desc then name asc; unassigned ignored; k <= 0 -> empty list. */
    public static List<String> topAssignees(List<Ticket> tickets, int k) { throw new UnsupportedOperationException("TODO"); }
}

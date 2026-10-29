> Code: `src/main/java/practice/d20261029/b1/TicketStats.java` (`Ticket` and `Range` are given). Test: `TicketStatsTest`. Then answer (a) to (d) on paper. **20 min.**

# B1. Streams and collectors on support tickets

`Ticket(id, team, assignee, priority 1..4, resolved, hoursToResolve)`; assignee may be null; "open" = not resolved. One pipeline per method (see the Javadoc in the stub):
`openCountByTeam`, `splitUrgent` (`partitioningBy`), `avgResolveHoursByTeam`, `busiestAssignee`, `resolveRange` (one pass with `Collectors.teeing`), `topAssignees`.

Paper:
(a) `partitioningBy` vs `groupingBy(t -> t.priority() <= 2)`: what is different when one side is empty?
(b) `Stream.of(1,2,3).collect(teeing(counting(), summingInt(x -> x), (c, s) -> s * 1.0 / c))`: result? Why is `teeing` better than two passes over a `List` and the only choice for a one-shot stream?
(c) `groupingBy(..., averagingInt(...))` on an `int` field returns `Double`; what does `summingInt` return on an empty group, and what does `averagingInt` return on an empty stream?
(d) What goes wrong with `Collectors.toMap(Ticket::assignee, ...)` when the assignee is null, in the key and in the value?

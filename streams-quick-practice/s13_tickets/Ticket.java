package s13_tickets;

/** priority 1 (urgent) to 4 (low); assignee is null while unassigned; hoursToResolve is meaningful only when resolved. */
public record Ticket(String id, String team, String assignee, int priority, boolean resolved, int hoursToResolve) {}

package practice.e;

/** Given. */
public final class TicketExceptions {
    private TicketExceptions() {}

    public static class NotFound extends RuntimeException { public NotFound(Long id) { super("ticket " + id + " not found"); } }

    public static class IllegalTransition extends RuntimeException {
        public IllegalTransition(Status from, Status to) { super("cannot move a ticket from " + from + " to " + to); }
    }
}

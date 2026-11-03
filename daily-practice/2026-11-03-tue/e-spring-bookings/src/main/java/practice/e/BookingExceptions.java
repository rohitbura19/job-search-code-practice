package practice.e;

/** Given. */
public final class BookingExceptions {
    private BookingExceptions() {}

    public static class NotFound extends RuntimeException { public NotFound(Long id) { super("booking " + id + " not found"); } }
    public static class Overlap extends RuntimeException { public Overlap(String room) { super("room " + room + " is already booked in that period"); } }
    public static class TooLong extends RuntimeException { public TooLong(java.time.Duration max) { super("a booking may last at most " + max); } }
    public static class InPast extends RuntimeException { public InPast() { super("a booking must start in the future"); } }
}

package practice.e.estimate;

/** GIVEN. */
public class FxUnavailableException extends RuntimeException {
    public FxUnavailableException(String from, String to, Throwable cause) {
        super("FX rate " + from + "->" + to + " unavailable", cause);
    }
}

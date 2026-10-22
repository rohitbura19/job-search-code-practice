package practice.d20261013.g;

/** GIVEN. attempted = devices that left QUEUED; failed = FAILED + TIMED_OUT; succeeded = SUCCEEDED. */
public record Stats(int attempted, int failed, int succeeded) {}

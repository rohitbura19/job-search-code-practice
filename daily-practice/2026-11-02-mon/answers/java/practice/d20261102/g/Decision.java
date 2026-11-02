package practice.d20261102.g;

import java.time.Duration;

/** Given. */
public record Decision(boolean allowed, int remaining, Duration retryAfter) {}

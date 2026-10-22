package practice.d20261012.g;

import java.time.Duration;

/** GIVEN - result of one rate-limit check. */
public record Decision(boolean allowed, long remaining, Duration retryAfter) {}

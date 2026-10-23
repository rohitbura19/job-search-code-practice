package practice.d20261023.g;

import java.time.Duration;

/** A failed job is retried; attempt n (1-based) that fails is retried after baseBackoff * 2^(n-1); after maxAttempts failures it is dead. */
public record SchedulerConfig(int maxAttempts, Duration baseBackoff) {}

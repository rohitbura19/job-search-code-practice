package practice.d20261103.g;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Delayed job scheduler (LLD of F1). Single in-memory node; time comes ONLY from the injected Clock. Thread-safe, but a job must run outside any lock.
 * Ids are "job-1", "job-2", ... in scheduling order. Jobs run in order of (due time, scheduling order).
 */
public final class JobScheduler {
    public JobScheduler(Clock clock) { throw new UnsupportedOperationException("TODO"); }

    /** No retries (RetryPolicy.NONE). IllegalArgumentException for a negative delay, NullPointerException for null arguments. */
    public String schedule(Job job, Duration delay) { throw new UnsupportedOperationException("TODO"); }

    /** Due at now + delay. A job that throws is retried at (time of the failure + policy.backoff(failedAttempts)) until maxAttempts executions were made; then its id goes to deadLetters(). */
    public String schedule(Job job, Duration delay, RetryPolicy policy) { throw new UnsupportedOperationException("TODO"); }

    /** True iff the job was still pending (not run, not dead, not already cancelled). O(1) is possible (lazy deletion). */
    public boolean cancel(String id) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Run, in order, every job that is due at the moment of the call (due <= now) and was scheduled before the call began; returns how many EXECUTIONS ran
     * (failed attempts count). A retry or a job scheduled by a running job is never run again inside the same call, even with zero delay.
     * A job may call schedule/cancel on this scheduler.
     */
    public int runDue() { throw new UnsupportedOperationException("TODO"); }

    /** Due time of the earliest pending job, ignoring cancelled ones. */
    public Optional<Instant> nextDue() { throw new UnsupportedOperationException("TODO"); }

    /** Number of jobs waiting (including those waiting for a retry). */
    public int pending() { throw new UnsupportedOperationException("TODO"); }

    /** Ids of jobs that failed their last allowed attempt, in the order they died. Returns a copy. */
    public List<String> deadLetters() { throw new UnsupportedOperationException("TODO"); }
}

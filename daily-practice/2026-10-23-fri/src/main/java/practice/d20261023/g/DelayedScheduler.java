package practice.d20261023.g;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/** A deterministic, single-process delayed job queue. Nothing runs by itself: the caller (a ticker thread) calls runDue(). */
public interface DelayedScheduler {
    /** Register a job under a unique id. Returns false (and changes nothing) if that id is already pending or being retried. */
    boolean schedule(String id, Instant runAt, Job job);

    /** Remove a pending job (including one waiting for a retry). True if something was removed. */
    boolean cancel(String id);

    /**
     * Run, in order, every job with runAt <= now (ties by scheduling order) and return how many executions were attempted
     * (retries count). A job that throws is rescheduled per the config, or moved to the dead letters after maxAttempts. A retry
     * is always in the future, so it never runs again in the same call. After a job succeeds or dies its id is free again.
     */
    int runDue();

    /** Ids of jobs that failed maxAttempts times, in the order they died. */
    List<String> deadLetters();

    /** Number of ids currently pending (including waiting for a retry). */
    int pending();

    /** Earliest runAt among pending jobs, so the ticker knows how long to sleep. */
    Optional<Instant> nextRunAt();
}

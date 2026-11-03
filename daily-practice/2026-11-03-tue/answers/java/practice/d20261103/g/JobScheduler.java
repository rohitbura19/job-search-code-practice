package practice.d20261103.g;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;

/**
 * Min-heap ordered by (due, seq) plus a map of live entries: cancel removes from the map and the heap entry is skipped lazily (O(1) cancel).
 * runDue takes ONE entry at a time under the lock, runs it OUTSIDE the lock (a job may call schedule/cancel), and only runs entries that
 * existed when the call started (seq <= startSeq), so a retry with zero backoff cannot spin inside one call.
 * Time and space: O(log n) per schedule/run, O(n) memory. Time only comes from the injected Clock.
 */
public final class JobScheduler {
    private static final class Entry {
        final String id; final Job job; final RetryPolicy policy; final Instant due; final long seq; final int attempts;
        Entry(String id, Job job, RetryPolicy policy, Instant due, long seq, int attempts) {
            this.id = id; this.job = job; this.policy = policy; this.due = due; this.seq = seq; this.attempts = attempts;
        }
    }

    private final Clock clock;
    private final PriorityQueue<Entry> heap = new PriorityQueue<>(
            (a, b) -> a.due.equals(b.due) ? Long.compare(a.seq, b.seq) : a.due.compareTo(b.due));
    private final Map<String, Entry> live = new HashMap<>();
    private final List<String> dead = new ArrayList<>();
    private long seq;
    private long idCounter;

    public JobScheduler(Clock clock) { this.clock = Objects.requireNonNull(clock); }

    public String schedule(Job job, Duration delay) { return schedule(job, delay, RetryPolicy.NONE); }

    public synchronized String schedule(Job job, Duration delay, RetryPolicy policy) {
        Objects.requireNonNull(job);
        Objects.requireNonNull(policy);
        if (delay.isNegative()) throw new IllegalArgumentException("negative delay");
        String id = "job-" + (++idCounter);
        add(new Entry(id, job, policy, clock.instant().plus(delay), ++seq, 0));
        return id;
    }

    private void add(Entry e) { heap.add(e); live.put(e.id, e); }

    public synchronized boolean cancel(String id) { return live.remove(id) != null; }

    public synchronized int pending() { return live.size(); }

    public synchronized List<String> deadLetters() { return List.copyOf(dead); }

    public synchronized Optional<Instant> nextDue() {
        while (!heap.isEmpty() && live.get(heap.peek().id) != heap.peek()) heap.poll();
        return heap.isEmpty() ? Optional.empty() : Optional.of(heap.peek().due);
    }

    public int runDue() {
        Instant now;
        long startSeq;
        synchronized (this) { now = clock.instant(); startSeq = seq; }
        int ran = 0;
        while (true) {
            Entry e;
            synchronized (this) {
                while (!heap.isEmpty() && live.get(heap.peek().id) != heap.peek()) heap.poll();
                if (heap.isEmpty() || heap.peek().due.isAfter(now) || heap.peek().seq > startSeq) return ran;
                e = heap.poll();
                live.remove(e.id);
            }
            ran++;
            try {
                e.job.run();
            } catch (Exception ex) {
                synchronized (this) {
                    int failed = e.attempts + 1;
                    if (failed >= e.policy.maxAttempts()) dead.add(e.id);
                    else add(new Entry(e.id, e.job, e.policy, now.plus(e.policy.backoff(failed)), ++seq, failed));
                }
            }
        }
    }
}

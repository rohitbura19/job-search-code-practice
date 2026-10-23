package practice.d20261023.g;

import java.time.Clock;
import java.time.Instant;
import java.util.*;

/**
 * A TreeSet ordered by (runAt, seq) gives O(log n) schedule / cancel / pop and O(1) peek; a HashMap by id gives O(1) duplicate
 * detection and cancel lookup. Entries are immutable; a retry is a NEW entry with attempts + 1 and a fresh seq. The job is removed
 * from both structures before it runs, so a job that throws leaves no half-state. Jobs run under the lock here (simple, ordered);
 * the review section says how to run them outside it.
 */
public final class InMemoryScheduler implements DelayedScheduler {
    private record Entry(String id, Instant runAt, long seq, Job job, int attempts) {}

    private final Clock clock;
    private final SchedulerConfig config;
    private final TreeSet<Entry> queue = new TreeSet<>(Comparator.comparing(Entry::runAt).thenComparingLong(Entry::seq));
    private final Map<String, Entry> byId = new HashMap<>();
    private final List<String> dead = new ArrayList<>();
    private long seq;

    public InMemoryScheduler(Clock clock, SchedulerConfig config) {
        this.clock = clock;
        this.config = config;
    }

    private void add(Entry e) {
        queue.add(e);
        byId.put(e.id(), e);
    }

    @Override public synchronized boolean schedule(String id, Instant runAt, Job job) {
        if (byId.containsKey(id)) return false;
        add(new Entry(id, runAt, seq++, job, 0));
        return true;
    }

    @Override public synchronized boolean cancel(String id) {
        Entry e = byId.remove(id);
        if (e == null) return false;
        queue.remove(e);
        return true;
    }

    @Override public synchronized int runDue() {
        Instant now = clock.instant();
        int ran = 0;
        while (!queue.isEmpty() && !queue.first().runAt().isAfter(now)) {
            Entry e = queue.pollFirst();
            byId.remove(e.id());
            ran++;
            try {
                e.job().run();
            } catch (Exception ex) {
                int attempts = e.attempts() + 1;
                if (attempts >= config.maxAttempts()) {
                    dead.add(e.id());
                } else {
                    Instant next = now.plus(config.baseBackoff().multipliedBy(1L << Math.min(attempts - 1, 20)));
                    add(new Entry(e.id(), next, seq++, e.job(), attempts));
                }
            }
        }
        return ran;
    }

    @Override public synchronized List<String> deadLetters() { return List.copyOf(dead); }

    @Override public synchronized int pending() { return byId.size(); }

    @Override public synchronized Optional<Instant> nextRunAt() {
        return queue.isEmpty() ? Optional.empty() : Optional.of(queue.first().runAt());
    }
}

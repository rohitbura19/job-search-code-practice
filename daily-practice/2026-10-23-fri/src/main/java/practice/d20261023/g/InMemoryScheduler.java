package practice.d20261023.g;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public final class InMemoryScheduler implements DelayedScheduler {
    public InMemoryScheduler(Clock clock, SchedulerConfig config) {
        // TODO: a TreeSet (or PriorityQueue + lazy deletion) ordered by (runAt, sequence) and a Map<String, Entry> by id
    }

    @Override public synchronized boolean schedule(String id, Instant runAt, Job job) { throw new UnsupportedOperationException("TODO"); }
    @Override public synchronized boolean cancel(String id) { throw new UnsupportedOperationException("TODO"); }
    @Override public synchronized int runDue() { throw new UnsupportedOperationException("TODO"); }
    @Override public synchronized List<String> deadLetters() { throw new UnsupportedOperationException("TODO"); }
    @Override public synchronized int pending() { throw new UnsupportedOperationException("TODO"); }
    @Override public synchronized Optional<Instant> nextRunAt() { throw new UnsupportedOperationException("TODO"); }
}

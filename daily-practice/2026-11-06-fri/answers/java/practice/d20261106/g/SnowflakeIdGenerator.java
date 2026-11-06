package practice.d20261106.g;

import java.util.function.LongSupplier;

/**
 * 64-bit id = 41 bits (ms since a custom epoch) | 10 bits worker | 12 bits sequence. Sortable by time, unique per worker.
 * synchronized nextId(): same ms -> sequence + 1 (wrap to 0 means 4096 ids used up: spin until the next ms);
 * new ms -> sequence 0; clock behind the last used ms -> throw (never hand out an id that could repeat).
 */
public class SnowflakeIdGenerator {
    static final int WORKER_BITS = 10, SEQ_BITS = 12;
    static final long MAX_WORKER = (1L << WORKER_BITS) - 1, SEQ_MASK = (1L << SEQ_BITS) - 1;

    private final long workerId, epoch;
    private final LongSupplier clock;
    private long lastMs = -1, sequence = 0;

    public SnowflakeIdGenerator(long workerId, long epochMillis, LongSupplier clockMillis) {
        if (workerId < 0 || workerId > MAX_WORKER) throw new IllegalArgumentException("workerId " + workerId);
        this.workerId = workerId;
        this.epoch = epochMillis;
        this.clock = clockMillis;
    }

    public synchronized long nextId() {
        long now = clock.getAsLong();
        if (now < epoch || now < lastMs) throw new IllegalStateException("clock moved backwards: " + now);
        if (now == lastMs) {
            sequence = (sequence + 1) & SEQ_MASK;
            if (sequence == 0) {
                while ((now = clock.getAsLong()) <= lastMs) Thread.onSpinWait();
            }
        } else {
            sequence = 0;
        }
        lastMs = now;
        return ((now - epoch) << (WORKER_BITS + SEQ_BITS)) | (workerId << SEQ_BITS) | sequence;
    }

    public static long timestampOf(long id, long epochMillis) { return (id >>> (WORKER_BITS + SEQ_BITS)) + epochMillis; }
    public static long workerOf(long id) { return (id >>> SEQ_BITS) & MAX_WORKER; }
    public static long sequenceOf(long id) { return id & SEQ_MASK; }
}

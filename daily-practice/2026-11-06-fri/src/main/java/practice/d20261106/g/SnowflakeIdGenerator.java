package practice.d20261106.g;

import java.util.function.LongSupplier;

/**
 * Distributed unique id generator (Snowflake style). Layout of the 64-bit id, high to low: 41 bits milliseconds since `epochMillis`,
 * 10 bits workerId, 12 bits sequence. Ids from one generator are strictly increasing; ids from different workers never collide.
 * Thread-safe. See my-designs/G-classes-sketch.md first.
 */
public class SnowflakeIdGenerator {
    /** IllegalArgumentException unless 0 <= workerId <= 1023. clockMillis returns the current time in epoch milliseconds. */
    public SnowflakeIdGenerator(long workerId, long epochMillis, LongSupplier clockMillis) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Same millisecond as the previous id: sequence + 1. If that is the 4097th id in the millisecond, busy-wait (Thread.onSpinWait) until
     * the clock shows a later millisecond. New millisecond: sequence restarts at 0.
     * IllegalStateException if the clock is earlier than the last used millisecond or earlier than the epoch.
     */
    public long nextId() { throw new UnsupportedOperationException("TODO"); }

    public static long timestampOf(long id, long epochMillis) { throw new UnsupportedOperationException("TODO"); }
    public static long workerOf(long id) { throw new UnsupportedOperationException("TODO"); }
    public static long sequenceOf(long id) { throw new UnsupportedOperationException("TODO"); }
}

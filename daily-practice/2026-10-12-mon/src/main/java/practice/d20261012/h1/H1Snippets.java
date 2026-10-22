package practice.d20261012.h1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GIVEN CODE - do not edit. Each method runs one ThreadPoolExecutor puzzle from questions/H1 and returns what it
 * observed. Read the code, predict the returned text, and write it into {@link H1Predictions}.
 * All tasks are named T1..T7 and (except where noted) block on a latch, so the observation is deterministic.
 */
public final class H1Snippets {
    private H1Snippets() {}

    static final class Job implements Runnable {
        final String name; final Set<String> started; final CountDownLatch gate; final boolean blockOnlyOnPool;
        Job(String name, Set<String> started, CountDownLatch gate, boolean blockOnlyOnPool) {
            this.name = name; this.started = started; this.gate = gate; this.blockOnlyOnPool = blockOnlyOnPool;
        }
        @Override public void run() {
            boolean onPool = Thread.currentThread().getName().startsWith("w-");
            started.add(name + (blockOnlyOnPool ? (onPool ? "@pool" : "@caller") : ""));
            if (blockOnlyOnPool && !onPool) return;           // never block the submitting thread
            try { gate.await(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        @Override public String toString() { return name; }
    }

    static ThreadFactory namedFactory() {
        AtomicInteger n = new AtomicInteger();
        return r -> new Thread(r, "w-" + n.incrementAndGet());
    }

    private static void waitFor(java.util.function.BooleanSupplier cond) throws InterruptedException {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        while (!cond.getAsBoolean()) {
            if (System.nanoTime() > deadline) throw new IllegalStateException("timeout");
            Thread.sleep(1);
        }
    }

    private static String submitSeven(ThreadPoolExecutor ex) throws InterruptedException {
        CountDownLatch gate = new CountDownLatch(1);
        Set<String> started = new ConcurrentSkipListSet<>();
        List<String> rejected = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            String name = "T" + i;
            try { ex.execute(new Job(name, started, gate, false)); }
            catch (RejectedExecutionException e) { rejected.add(name); }
        }
        waitFor(() -> started.size() >= ex.getPoolSize());
        String snapshot = "started=" + started + " queued=" + ex.getQueue() + " rejected=" + rejected + " poolSize=" + ex.getPoolSize();
        gate.countDown();
        ex.shutdown();
        ex.awaitTermination(5, TimeUnit.SECONDS);
        return snapshot;
    }

    /** core 2, max 4, ArrayBlockingQueue(2), AbortPolicy, 7 blocking tasks. */
    public static String line1() throws InterruptedException {
        return submitSeven(new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2),
                namedFactory(), new ThreadPoolExecutor.AbortPolicy()));
    }

    /** Same pool, but the queue is an unbounded LinkedBlockingQueue. */
    public static String line2() throws InterruptedException {
        return submitSeven(new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
                namedFactory(), new ThreadPoolExecutor.AbortPolicy()));
    }

    /** Same bounded pool as line 1 but CallerRunsPolicy. Tasks on pool threads block; a task run by the caller returns at once.
     *  The returned text lists, after everything finished, where each task ran. */
    public static String line3() throws InterruptedException {
        ThreadPoolExecutor ex = new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2),
                namedFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        CountDownLatch gate = new CountDownLatch(1);
        Set<String> started = new ConcurrentSkipListSet<>();
        for (int i = 1; i <= 7; i++) ex.execute(new Job("T" + i, started, gate, true));
        gate.countDown();
        ex.shutdown();
        ex.awaitTermination(5, TimeUnit.SECONDS);
        return started.toString();
    }

    /** One pool thread whose uncaught-exception handler appends to the result. execute(...) first, then submit(...). */
    public static String line4() throws InterruptedException {
        StringBuilder out = new StringBuilder();
        CountDownLatch handled = new CountDownLatch(1);
        ThreadFactory tf = r -> {
            Thread t = new Thread(r, "w-x");
            t.setUncaughtExceptionHandler((th, e) -> { synchronized (out) { out.append("handler got ").append(e.getMessage()).append("; "); } handled.countDown(); });
            return t;
        };
        ThreadPoolExecutor ex = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), tf);
        ex.execute(() -> { throw new IllegalStateException("from execute"); });
        handled.await(5, TimeUnit.SECONDS);
        Future<?> f = ex.submit(() -> { throw new IllegalStateException("from submit"); });
        try { f.get(5, TimeUnit.SECONDS); }
        catch (ExecutionException e) { synchronized (out) { out.append("get threw ExecutionException caused by ").append(e.getCause().getMessage()); } }
        catch (java.util.concurrent.TimeoutException e) { out.append("timeout"); }
        ex.shutdown();
        ex.awaitTermination(5, TimeUnit.SECONDS);
        synchronized (out) { return out.toString(); }
    }

    /** One worker, unbounded queue, three tasks that wait on a latch, then shutdownNow(). */
    public static String line5() throws InterruptedException {
        ThreadPoolExecutor ex = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), namedFactory());
        CountDownLatch gate = new CountDownLatch(1);
        CountDownLatch running = new CountDownLatch(1);
        StringBuilder out = new StringBuilder();
        ex.execute(() -> {
            running.countDown();
            try { gate.await(); synchronized (out) { out.append("T1 finished normally; "); } }
            catch (InterruptedException e) { synchronized (out) { out.append("T1 interrupted; "); } }
        });
        ex.execute(new Job("T2", new ConcurrentSkipListSet<>(), gate, false));
        ex.execute(new Job("T3", new ConcurrentSkipListSet<>(), gate, false));
        running.await();
        List<Runnable> never = ex.shutdownNow();
        ex.awaitTermination(5, TimeUnit.SECONDS);
        synchronized (out) { return out + "never started: " + never; }
    }
}

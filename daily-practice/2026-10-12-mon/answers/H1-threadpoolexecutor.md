# Answers: H1-threadpoolexecutor

(Spoilers. Open only after your attempt.)

**Reference**
- Order of preference: core threads first, then the queue, then extra threads up to max, then reject. T1, T2 -> core threads. T3, T4 -> queue (capacity 2). T5, T6 -> new threads (max 4 reached). T7 -> queue full and max threads busy -> `RejectedExecutionException` (AbortPolicy).
- Unbounded queue: tasks 3+ always queue, so max threads is never used (pool stays at core size) and the queue can grow until OOM. `newFixedThreadPool` uses an unbounded `LinkedBlockingQueue`, `newCachedThreadPool` allows unbounded threads. Prefer an explicit bounded queue, named threads, and a deliberate rejection policy.
- Policies: Abort (throw, default; fail fast), CallerRuns (caller executes it, natural backpressure), Discard (silently drop, only for best-effort metrics), DiscardOldest (drop the oldest queued, for "latest wins" data).
- `execute(Runnable)` lets an uncaught exception reach the thread's uncaught handler; `submit` captures it inside the `Future` and it is silently lost unless you call `get()`. `shutdown()` stops accepting and finishes queued work; `shutdownNow()` interrupts workers and returns the list of unstarted tasks; follow with `awaitTermination`.
- Sizing: CPU-bound about N cores (+1). IO-bound about N × (1 + wait/compute), then measure. Virtual threads (JDK 21) make a thread-per-task model cheap for blocking IO, so for IO-heavy services you stop pooling threads and instead limit concurrency with a `Semaphore` to protect downstream systems. CPU-bound work still needs a bounded pool.

What the snippets print (verified on JDK 21):
- line 1: `started=[T1, T2, T5, T6] queued=[T3, T4] rejected=[T7] poolSize=4`
- line 2: `started=[T1, T2] queued=[T3, T4, T5, T6, T7] rejected=[] poolSize=2`
- line 3: `[T1@pool, T2@pool, T3@pool, T4@pool, T5@pool, T6@pool, T7@caller]`
- line 4: `handler got from execute; get threw ExecutionException caused by from submit`
- line 5: `T1 interrupted; never started: [T2, T3]`

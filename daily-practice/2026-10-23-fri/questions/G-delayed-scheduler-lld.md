> Code: `src/main/java/practice/d20261023/g/` (`DelayedScheduler`, `SchedulerConfig`, `Job` are given; implement `InMemoryScheduler`). Test: `InMemorySchedulerTest`.

# G. LLD of F1: an in-memory delayed scheduler (sketch 10 min, code 30 min)

Sketch the classes in `my-designs/G-classes-sketch.md` first (entry type, ordering, id index, retry state), then implement `InMemoryScheduler`.

Contract (details in the Javadoc of `DelayedScheduler`): `schedule` rejects a duplicate pending id; `cancel`; `runDue` runs every job with `runAt <= now` in `(runAt, scheduling order)` order and returns the number of executions; a job that throws is retried with exponential backoff (`base * 2^(attempt-1)`), after `maxAttempts` failures it goes to the dead letters; a retry never runs again within the same `runDue`; ids are reusable after success, death, or cancel. The clock is injected, nothing sleeps. Performance: 200k jobs, half cancelled, in under 3 s.

Say out loud: why a TreeSet plus a HashMap (or PriorityQueue with lazy deletion and its cost); what happens if a job calls `schedule`/`cancel` itself; why running jobs under the lock is a problem in production and how to fix it (copy the due batch under the lock, run outside, re-lock to record the outcome).

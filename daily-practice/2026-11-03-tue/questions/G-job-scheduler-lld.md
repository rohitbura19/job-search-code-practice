# G. LLD of F1: in-memory delayed job scheduler (sketch 8 + code 22 min)

> Sketch in `my-designs/G-classes-sketch.md` first. Code: `src/main/java/practice/d20261103/g/JobScheduler.java`, `RetryPolicy.java` (`Job` is given). Test: `JobSchedulerTest`.

- `schedule(job, delay[, policy])` returns an id `job-1`, `job-2`, ...; `cancel(id)`; `runDue()`; `nextDue()`; `pending()`; `deadLetters()`.
- Jobs run in `(due, scheduling order)` order. Time comes only from the injected `Clock`.
- A failing job is retried at `failure time + policy.backoff(failedAttempts)` (`base * 2^(n-1)`, capped at 1 h) until `maxAttempts` executions; then its id goes to the dead letters.
- `runDue` runs jobs OUTSIDE the lock, only those scheduled before the call began (no spinning on zero backoff), and returns the number of executions.
- Complete the compact constructor validation of `RetryPolicy` too.

Think about: lazy deletion for O(1) cancel; why the id/seq tie-break matters; what a real worker loop would do with `nextDue()`.

> Practice: read `h1/H1Snippets.java` (the pool puzzles as runnable code, lines 1 to 5), type your predicted outputs into `h1/H1Predictions.java`, run `H1PredictionsTest`. Questions 3 (policy uses), 5 (sizing, virtual threads) are out loud. Snippet line 1 = question 1, line 2 = question 2 (unbounded queue), line 3 = CallerRunsPolicy from question 3, line 4 = question 4 (`execute` vs `submit`), line 5 = `shutdownNow`.

### H1. Core Java: ThreadPoolExecutor behaviour
```java
ExecutorService ex = new ThreadPoolExecutor(
    2, 4, 60, TimeUnit.SECONDS,
    new ArrayBlockingQueue<>(2),
    new ThreadPoolExecutor.AbortPolicy());
// submit 7 long running tasks, T1..T7
```
1. Which threads run which tasks, and what happens to T7?
2. What would change with `new LinkedBlockingQueue<>()` (unbounded)? Why is `Executors.newFixedThreadPool` dangerous in production?
3. Name the four built-in rejection policies and one use of each.
4. Difference between `execute` and `submit` regarding exceptions. `shutdown()` vs `shutdownNow()`.
5. How do you size a pool for CPU-bound vs IO-bound work? Where do virtual threads (Java 21) change this?

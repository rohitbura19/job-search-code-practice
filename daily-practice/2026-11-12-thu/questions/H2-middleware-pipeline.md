# H2 - OOP: middleware pipeline, Chain of Responsibility (20 min) - `h2/Pipeline.java`

`Handler<R,S>` (given) receives the request and a `Chain` whose `proceed(request)` runs the remaining handlers and finally the terminal function. Implement `Pipeline<R,S>`: `execute`, plus two ready-made handlers `retry(maxAttempts)` and `guard(reject, fallback)`.
Rules: handlers run in order and unwind in reverse; a handler may rewrite the request/response or skip `proceed`; `proceed` may be called several times (retry re-runs everything after it); the list is copied and null handlers are rejected; `execute` is thread-safe and re-entrant (hint: where does "current position" live?); a 1000-handler chain works.

> Code: `src/main/java/practice/d20261029/h2/` (`Expense`, `ApprovalResult`, `Approver` are given; write `AbstractApprover`, `LimitApprover`, `CategoryBlocker`, `LoggingApprover`). Test: `ApprovalChainTest`. **30 min.**

# H2. OOP: chain of responsibility with a template method, plus a decorator

An expense goes through a chain: a `CategoryBlocker` (policy), then `LimitApprover`s with growing limits (lead 500, manager 5,000, director 50,000 rupees). The first link that decides wins; if nobody decides the result is rejected by `chain-end`.

1. `AbstractApprover.handle` is `final` (template method): ask `decide`; an empty `Optional` means "pass to the next link". `then(next)` returns the SAME object so chains read `a.then(b).then(c)`.
2. `LimitApprover` (limit inclusive) and `CategoryBlocker` (case-insensitive) implement only `decide`.
3. `LoggingApprover` is a decorator over any `Approver`, not a link: it appends `"<id>:<by>:approved|rejected"` to a log and returns the inner result unchanged.

Say out loud: why `handle` is final and `decide` protected; why the chain stays thread-safe only if the links are immutable after building; chain of responsibility vs a `List<Rule>` loop (when is the chain worth it?); decorator vs inheritance for logging; what changes if a link must be able to ESCALATE after approving (a two-person rule).

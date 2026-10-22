> Code: sketch in `my-designs/G-classes-sketch.md` first (5 min), then implement `g/RetryPolicy.java`, `g/SenderRegistry.java`, `g/Dispatcher.java`, `g/FailoverSender.java`, `g/NotificationService.java` (the interfaces and records are GIVEN). Tests: `NotificationServiceTest`, `DispatchingTest`.

## G) LLD of the same question — Notification service (25 min)

Design the in-process object model of the **Notification Service** (F1). Requirements: multiple channels (Push, SMS, Email) and easily add WhatsApp later; per-user preferences and quiet hours; templating; retry with backoff; provider failover; testable.

Write the classes and interfaces, the main flow, and one sequence for "send faulted alert". Then discuss patterns used and how you would test it.

Kit contract for the code part (the discussion part stays on paper):
- `RetryPolicy`: `shouldRetry(attempt, t)` where `attempt` is the number of attempts already made (1 after the first failure): true while `attempt < 5` and `t` is not a `PermanentFailure`. `delay(attempt)` = `200 ms * 2^attempt * (0.5 + random)` with `random` in `[0, 1)`.
- `SenderRegistry`: built from a `List<ChannelSender>`; `get(channel)` throws `IllegalArgumentException` for an unregistered channel; two senders for the same channel make the constructor throw (`IllegalStateException`, what `Collectors.toMap` does).
- `Dispatcher.dispatchWithRetry(sender, recipient, message)`: send; on a failure ask the policy (`shouldRetry(attempt, t)`), sleep `delay(attempt)` through the GIVEN `Sleeper`, try again; when the policy says no, rethrow that exception. Returns the `SendResult` of the successful attempt.
- `FailoverSender(channel, providers)`: chain of responsibility. Tries providers in order; a `TransientFailure` moves on to the next provider; a `PermanentFailure` is rethrown at once; if all providers fail, rethrow the last `TransientFailure`.
- `NotificationService.handle(req)`: dedupe key `eventId + ":" + userId`; for each allowed channel, a non-URGENT request during quiet hours is handed to the `Scheduler` (`defer(req, channel)`) instead of being sent; otherwise render, load the recipient, `dispatchWithRetry` through the registry's sender. The clock is injected.

# Sun 11 Oct 2026 - practice

## Order for the day
1. **Minimum set (about 80 min):** `questions/00-warmup.md` (5 min) -> **C1** Merge Intervals (15 min timer) -> **D1** Sliding Window Maximum (20 min) -> **E** (25 min, Spring project) -> **H1** predictions (10 min) -> closing question 1 out loud (5 min).
2. Extended set: A1, A2, B1, C2, F1, F2, G, H2, closing questions 2 to 4.

| Item | Question | Where you write code / answer | Test |
|---|---|---|---|
| Warm-up | questions/00-warmup.md | paper | - |
| A1 | questions/A1-contains-duplicate-ii.md | a1/ContainsDuplicateII.java | ContainsDuplicateIITest |
| A2 | questions/A2-valid-parentheses.md | a2/ValidParentheses.java | ValidParenthesesTest |
| B1 | questions/B1-streams-top-chargers.md | b1/SessionStats.java | SessionStatsTest |
| C1 | questions/C1-merge-intervals.md | c1/MergeIntervals.java | MergeIntervalsTest |
| C2 | questions/C2-course-schedule-ii.md | c2/CourseSchedule.java | CourseScheduleTest |
| D1 | questions/D1-sliding-window-maximum.md | d1/SlidingWindowMax.java | SlidingWindowMaxTest |
| E | questions/E-charger-events-api.md | e-spring-charger-events/ | ChargerControllerTest, ChargerEventServiceTest, ChargerEventListenerTest |
| F1 | questions/F1-notification-service-hld.md | my-designs/F1-notification-service.md | say it out loud |
| F2 | questions/F2-flash-sale-reservation-hld.md | my-designs/F2-flash-sale-reservation.md | say it out loud |
| G | questions/G-notification-lld.md | my-designs/G-classes-sketch.md, then g/RetryPolicy.java, SenderRegistry.java, Dispatcher.java, FailoverSender.java, NotificationService.java | NotificationServiceTest, DispatchingTest |
| H1 | questions/H1-concurrenthashmap.md | h1/H1Predictions.java (read H1Snippets.java) | H1PredictionsTest |
| H2 | questions/H2-liskov-composition.md | h2/Rect.java, Sq.java, Amphibian.java, RetryingHttpClient.java | LiskovCompositionTest |
| Close | questions/closing-round.md | out loud | - |

Code paths are under `src/main/java/practice/d20261011/`. Run `mvn test` in this folder (a failing test is expected until you write the code); the Spring part has its own `mvn test` inside `e-spring-charger-events/`. Spaced repetition: re-attempt anything marked "needed hints" on Tue 13 Oct and Sun 18 Oct.

## Score card

| Section | Done? | Tests green without hints? | Minutes | Weak point to revisit |
|---|---|---|---|---|
| Warm-up | | - | | |
| A1 / A2 | | | | |
| B1 | | | | |
| C1 / C2 | | | | |
| D1 | | | | |
| E | | | | |
| F1 / F2 | | - | | |
| G | | | | |
| H1 / H2 | | | | |

Minutes actually spent: ____

> Sketch in `my-designs/G-classes-sketch.md` first (5 min), then code in `src/main/java/practice/d20261014/g/` (`Schedule.java`, `AllOf.java`, `ScheduleHolder.java`; `Window` and `ChargingConstraint` are GIVEN). Test: `ScheduleTest`.

## G) LLD of the same question — Schedule engine on the charger / Schedule Service (25 min)

Design the in-process model that answers: "is charging allowed at instant `t`?" and "when does that next change?". Requirements: rules with days of week, start/end local time, windows may cross midnight; one IANA zone per schedule; correct across DST; versioned and immutable; combinable with other constraints (tariff peak, grid curtailment); no real clock inside the logic (testable).

Write the classes, the `isAllowed` flow, the next-boundary computation, and say how DST and midnight are handled. Then discuss patterns, concurrency and tests.

Rules the tests assume: a window belongs to the day it STARTS on; start is inclusive, end is exclusive; compute boundaries from the local date and zone (a gap start shifts forward, an overlap picks the earlier offset, as `LocalDateTime.atZone` does); `nextBoundary` is strictly after `now` and looks at most 8 days ahead; `ScheduleHolder.offer` accepts only a strictly newer version.

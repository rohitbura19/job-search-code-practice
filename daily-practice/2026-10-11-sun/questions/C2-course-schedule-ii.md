> Code: `c2/CourseSchedule.java` (`findOrder`, then the follow-up `minSemesters`). Test: `CourseScheduleTest`.

### C2. Course Schedule II (Graph / Topological sort)
`numCourses` and `prerequisites[i] = [a, b]` (take b before a). Return a valid order, or an empty array if there is a cycle.

Interviewer follow-up: map this to a service startup order / build pipeline. How do you report the actual cycle? How do you get the minimum number of semesters (parallel levels)?

(Kit contract: `minSemesters` returns `-1` on a cycle and `0` for zero courses.)

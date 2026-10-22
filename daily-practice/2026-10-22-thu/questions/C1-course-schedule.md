> Code: `c1/CourseSchedule.java`. Test: `CourseScheduleTest` (random DAGs and cycles, 200,000-node chain). **20 min timer.**

# C1. Course Schedule II (topological order)

There are `n` courses `0..n-1`. `prerequisites[i] = {course, mustTakeFirst}`. Return ANY order in which all courses can be finished, or an empty array when a cycle makes it impossible.
`n=4, [[1,0],[2,0],[3,1],[3,2]] -> [0,1,2,3]` or `[0,2,1,3]`; `n=2, [[1,0],[0,1]] -> []`.

Constraints: `0 <= n <= 2*10^5`, up to `2*10^5` pairs; duplicates possible; `{a, a}` is a cycle.

Interviewer follow-up: Kahn (BFS) vs DFS with colours: pros and cons, and which one overflows the stack here; how do you detect which courses are IN the cycle; return the minimum number of semesters when you can take any number of courses per semester (BFS levels); lexicographically smallest order (min-heap instead of a queue: complexity?); how does a build system (Maven reactor, Gradle task graph) use this; how do you do it when the graph does not fit in memory or edges keep arriving (incremental topological order)?

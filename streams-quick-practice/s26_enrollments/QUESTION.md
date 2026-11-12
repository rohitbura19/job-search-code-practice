<!-- From daily-practice/2026-11-11-wed -->
# B1 - Streams on enrollments (15 min)

`Enrollment(student, course, term, grade)` is given. Use streams and collectors only; map iteration order is part of the contract.

1. `averageByCourse` - course -> exact average, natural key order.
2. `topStudentPerCourse` - course -> `Optional<String>` best student (highest grade, ties -> alphabetically smallest).
3. `honourRoll(list, minCourses, minAvg)` - students with >= minCourses enrollments and average >= minAvg; average descending, then name.
4. `passFailCounts(list, passMark)` - both `true` and `false` present even when 0.
5. `coursesByStudent` - student -> distinct sorted courses (unmodifiable list), students in natural order.
6. `gradeBuckets` - key `grade/10*10` (100 goes to 90), non-empty buckets only, ascending.

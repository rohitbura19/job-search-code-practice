# Q07. University Academic Tracking System

**Duration:** 40 minutes (the core first, then follow-ups added one by one) · **Language:** Java · **Source:** IDFC AVP round, per Medium ("University Academic Tracking System with 5–6 follow-up conditions")

## Problem Statement

Build an in-memory service that tracks students, courses, enrollments and grades.

**Core requirements (code these first):**

1. `addStudent(id, name)` and `addCourse(code, title, credits, capacity, Set<String> prerequisites)`.
2. `enroll(studentId, courseCode, semester)` returns an `EnrollmentResult` and rejects the enrollment if:
   - the student or course doesn't exist (`NOT_FOUND`)
   - the course is full for that semester (`COURSE_FULL`)
   - a prerequisite hasn't been passed. Pass means any grade except `F` (`PREREQUISITE_NOT_MET`)
   - the student is already enrolled in it this semester, or has already passed it (`ALREADY_ENROLLED`, `ALREADY_PASSED`)
   - the semester's credits would go over **24** (`CREDIT_LIMIT_EXCEEDED`)
3. `recordGrade(studentId, courseCode, semester, Grade)`.
4. `sgpa(studentId, semester)` and `cgpa(studentId)` are credit-weighted and rounded to 2 decimals, half up. If a course is **retaken**, only the **latest** attempt counts in the CGPA.

Grade points: `O=10, A+=9, A=8, B+=7, B=6, C=5, P=4, F=0`.

## Example

```
Courses: CS101 (4 credits), MA101 (3 credits), CS201 (4 credits, prereq CS101)

S1, sem 1: CS101 = A+, MA101 = B
  sgpa(S1, 1) = (9*4 + 6*3) / 7 = 54/7 = 7.71

S2 enrolls in CS201 without CS101 -> PREREQUISITE_NOT_MET

S3, sem 1: CS101 = F, MA101 = O       -> sgpa(S3,1) = 30/7 = 4.29
S3, sem 2: retakes CS101 = A          -> sgpa(S3,2) = 32/4 = 8.00
  cgpa(S3) = (8*4 + 10*3) / 7 = 62/7 = 8.86   // the old F is dropped
S3 then enrolls in CS201 in sem 3 -> OK (CS101 is now passed)
```

## Expected Design

- Entities: `Student`, `Course`, `Enrollment(studentId, courseCode, semester, Grade grade)`. Use `enum Grade` with `points()` and `isPass()`.
- Repositories: `Map<String, Student>`, `Map<String, Course>`, and enrollments indexed by student and by `(course, semester)` for capacity counts.
- Validation as a list of `EnrollmentRule` objects (`CapacityRule`, `PrerequisiteRule`, `CreditLimitRule`, …), each returning an optional failure. New rules then plug in without editing `enroll()`.
- `GpaCalculator` is kept separate from storage (single responsibility).

## Follow-ups (added one at a time, the way the AVP round did it)

1. **Waitlist:** when a course is full, put the student on a FIFO waitlist. `drop()` frees a seat and automatically enrolls the next student on the waitlist who still passes every rule.
2. **Attendance:** the student must have ≥ 75% attendance to receive a grade; below that the grade is recorded as `F`.
3. **Transcript:** list every attempt, grouped by semester and sorted by course code.
4. **Rank list:** the top `k` students by CGPA for a department. Ties go to more credits earned, then to the student id.
5. **Prerequisite cycles:** reject `addCourse` if it would create a cycle (DFS or topological sort).
6. **Concurrency:** two students enroll for the last seat at the same moment. How do you guarantee only one gets it, in memory and then in SQL? (Row lock, `SELECT … FOR UPDATE`, or optimistic version column.)

## Discussion

The DB schema for this (tables, keys, a unique constraint on `(student, course, semester)`), and the SQL for the CGPA computation.

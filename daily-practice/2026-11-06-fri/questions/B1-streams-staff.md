# B1 - Streams / collections: staff (15 min)
File: `b1/StaffStreams.java` (record `Employee(name, dept, city, salary, skills)`).

Implement five methods with `Collectors`: `spread` (teeing min/max in one pass), `skillsByDept` (flatMapping into sorted sets), `highEarnersByCity` (filtering keeps empty groups), `headcount` (partitioningBy + counting), `topNPerDept` (collectingAndThen with sort + limit). Exact contracts are in the Javadoc of each stub. No loops that fill maps by hand; do not mutate the input.

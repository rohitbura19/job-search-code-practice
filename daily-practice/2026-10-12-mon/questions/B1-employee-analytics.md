> Code: `b1/EmployeeAnalytics.java` (`Employee` is given). Test: `EmployeeAnalyticsTest`. The follow-up questions about `map`/`flatMap`, `HashMap` internals and `ConcurrentModificationException` are paper / out loud.

### B1. Employee analytics (Collectors deep dive)
```java
record Employee(String name, String dept, double salary, List<String> skills) {}
```
Given `List<Employee> emps`:
1. Partition into those earning >= 30 lakh and the rest.
2. Highest paid employee per department as `Map<String, Employee>`.
3. All distinct skills across employees, sorted.
4. Min and max salary in a single pass.

Interviewer follow-up: `map` vs `flatMap`? Why does reusing a consumed stream throw? What is the difference between `HashMap`, `LinkedHashMap` and `TreeMap`, and what happens inside `HashMap` when many keys collide? Why can modifying a list inside `forEach` throw `ConcurrentModificationException`?

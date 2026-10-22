# Answers: B1-employee-analytics

(Spoilers. Open only after your attempt.)

**Reference**
```java
Map<Boolean, List<Employee>> split = emps.stream()
    .collect(Collectors.partitioningBy(e -> e.salary() >= 3_000_000));

Map<String, Employee> topPerDept = emps.stream().collect(Collectors.toMap(
    Employee::dept, Function.identity(),
    BinaryOperator.maxBy(Comparator.comparingDouble(Employee::salary))));

List<String> skills = emps.stream()
    .flatMap(e -> e.skills().stream()).distinct().sorted().toList();

Comparator<Employee> bySalary = Comparator.comparingDouble(Employee::salary);
List<Employee> minMax = emps.stream().collect(Collectors.teeing(
    Collectors.minBy(bySalary), Collectors.maxBy(bySalary),
    (mn, mx) -> List.of(mn.orElseThrow(), mx.orElseThrow())));
// or: DoubleSummaryStatistics s = emps.stream().mapToDouble(Employee::salary).summaryStatistics();
```
Notes: `partitioningBy` always has both `true` and `false` keys, `groupingBy` only has keys that occur. `flatMap` flattens one-to-many. A stream is single-use (`IllegalStateException: stream has already been operated upon or closed`). HashMap: array of buckets, index = `(n-1) & hash`, collisions form a linked list, treeified (red-black) at 8 entries when table >= 64, which makes worst case O(log n). Resize doubles at load factor 0.75. Mutable keys break lookups. Fail-fast iterators check `modCount`.

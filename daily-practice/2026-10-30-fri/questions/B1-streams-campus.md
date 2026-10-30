# B1. Streams: flatMap, toMap, Comparator chains, IntStream (15 min)

`Student(id, name, city, List<Course>)` and `Course(code, credits)` are given. Implement the six methods of `b1/CampusStats.java` using streams only.

Then answer on paper (compare with `answers/README.md`):

- (a) `Collectors.toMap(k, v)` meets a duplicate key. What happens? Which overload fixes it, and which map type does `toMap` return by default?
- (b) `map` vs `flatMap`: what is the type of `students.stream().map(Student::courses)` and why is it not what method 1 needs?
- (c) `Stream.toList()` vs `collect(Collectors.toList())`: mutability and null elements.
- (d) Why is `Comparator.comparing(Student::name).reversed().thenComparing(...)` a different order from `comparing(Student::name, reverseOrder()).thenComparing(...)` in the tie-break? (Hint: what does `reversed()` apply to?)

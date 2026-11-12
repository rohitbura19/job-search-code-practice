# 18. courses - repository queries

_From daily-practice/2026-11-08-sun. E - Spring Boot: courses API (30 min) - project `e-spring-courses/` (find `TODO(E`)_

Write the annotation + query for every TODO (JPQL unless it says native). Answers: `daily-practice/2026-11-08-sun/answers/`.

## Entity `Course`

```java
    private Long id;
    private String title;
    private int capacity;
    private Schedule schedule;
    private Set<Student> students = new LinkedHashSet<>();
```

## `CourseRepository`

```java
public interface CourseRepository extends JpaRepository<Course, Long> {
    /** TODO(E3a): JPQL, load the course AND its students in one query (left join fetch, so empty courses are found too). */
    Optional<Course> findWithStudents(Long id);
    /** TODO(E3b): JPQL projection: courseId, title, capacity, enrolled = size(c.students), ordered by id. Use aliases matching EnrollmentCount. */
    List<EnrollmentCount> enrollmentCounts();
    /** TODO(E3c): JPQL: courses the student is enrolled in, ordered by title. */
    List<Course> findByStudent(Long studentId);
    // TODO(E3d): add a derived query (no @Query) returning Page<Course>: courses whose schedule (embedded) ends on or after a LocalDate; takes a Pageable.
    //            The service calls it from upcoming().
    // TODO(E3e): add a derived query: boolean "does a course with this title exist, ignoring case?". The service calls it from create()/patch().
}
```

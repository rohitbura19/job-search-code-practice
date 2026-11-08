package practice.e;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

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

package practice.e;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("select c from Course c left join fetch c.students where c.id = :id")
    Optional<Course> findWithStudents(@Param("id") Long id);

    @Query("select c.id as courseId, c.title as title, c.capacity as capacity, size(c.students) as enrolled from Course c order by c.id")
    List<EnrollmentCount> enrollmentCounts();

    @Query("select c from Course c join c.students s where s.id = :studentId order by c.title")
    List<Course> findByStudent(@Param("studentId") Long studentId);

    /** Property path through the embedded record: schedule.endDate. */
    Page<Course> findByScheduleEndDateGreaterThanEqual(LocalDate date, Pageable pageable);

    boolean existsByTitleIgnoreCase(String title);
}

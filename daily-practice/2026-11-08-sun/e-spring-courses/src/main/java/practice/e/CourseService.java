package practice.e;

import java.time.Clock;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * TODO(E5): annotate as a Spring service, transactional read-only by default (write methods override with @Transactional).
 * Rules: see the Javadoc on each method. Throw Exceptions.NotFoundException / Exceptions.ConflictException.
 */
public class CourseService {
    public CourseService(CourseRepository courses, StudentRepository students, Clock clock) { throw new UnsupportedOperationException("TODO"); }

    /** Title must be unique ignoring case (ConflictException). Title is stripped before saving. */
    public Views.CourseView create(CreateCourseRequest r) { throw new UnsupportedOperationException("TODO"); }

    /** Email unique ignoring case (ConflictException); store the email lower-cased and the name stripped. */
    public Views.StudentView createStudent(CreateStudentRequest r) { throw new UnsupportedOperationException("TODO"); }

    /** NotFoundException when missing. */
    public Views.CourseView get(long id) { throw new UnsupportedOperationException("TODO"); }

    /**
     * NotFound if course or student is missing. Idempotent: already enrolled -> no change, no error.
     * Otherwise Conflict if the course already ended (endDate before today in the injected clock) or is full.
     */
    public void enroll(long courseId, long studentId) { throw new UnsupportedOperationException("TODO"); }

    /** NotFound if course or student is missing. Removing a student who is not enrolled is a silent no-op. */
    public void unenroll(long courseId, long studentId) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Partial update. Non-null title: stripped, must stay unique ignoring case against OTHER courses (keeping the same title is fine).
     * Non-null capacity: Conflict if lower than the number of enrolled students.
     */
    public Views.CourseView patch(long id, PatchCourseRequest r) { throw new UnsupportedOperationException("TODO"); }

    /** Courses not yet ended (today included), paged; summaries only. */
    public Page<Views.CourseSummary> upcoming(Pageable pageable) { throw new UnsupportedOperationException("TODO"); }

    public List<EnrollmentCount> enrollmentCounts() { throw new UnsupportedOperationException("TODO"); }
}

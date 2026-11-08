package practice.e;

import java.util.List;
import org.springframework.http.ResponseEntity;

/**
 * TODO(E6): REST controller under /api. Do NOT put @Validated on the class (Spring 6.1 built-in method validation is used).
 *   POST   /api/courses                              @Valid body -> 201 + Location /api/courses/{id} + body
 *   GET    /api/courses/{id}                         id must be positive
 *   PATCH  /api/courses/{id}                         @Valid PatchCourseRequest -> 200 + body
 *   GET    /api/courses?page=0&size=20               page >= 0, size 1..100 (400 otherwise); sort by schedule.endDate then id;
 *                                                    body = list of CourseSummary, header X-Total-Count = total elements
 *   GET    /api/courses/enrollment-counts            list of EnrollmentCount (literal path must win over /{id})
 *   PUT    /api/courses/{courseId}/students/{studentId}     -> 204
 *   DELETE /api/courses/{courseId}/students/{studentId}     -> 204
 *   POST   /api/students                             @Valid body -> 201 + Location /api/students/{id} + body
 */
public class CourseController {
    public CourseController(CourseService service) {}

    public ResponseEntity<Views.CourseView> create(CreateCourseRequest r) { throw new UnsupportedOperationException("TODO"); }
    public Views.CourseView get(Long id) { throw new UnsupportedOperationException("TODO"); }
    public Views.CourseView patch(Long id, PatchCourseRequest r) { throw new UnsupportedOperationException("TODO"); }
    public ResponseEntity<List<Views.CourseSummary>> list(int page, int size) { throw new UnsupportedOperationException("TODO"); }
    public List<EnrollmentCount> enrollmentCounts() { throw new UnsupportedOperationException("TODO"); }
    public ResponseEntity<Void> enroll(Long courseId, Long studentId) { throw new UnsupportedOperationException("TODO"); }
    public ResponseEntity<Void> unenroll(Long courseId, Long studentId) { throw new UnsupportedOperationException("TODO"); }
    public ResponseEntity<Views.StudentView> createStudent(CreateStudentRequest r) { throw new UnsupportedOperationException("TODO"); }
}

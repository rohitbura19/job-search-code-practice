package practice.e;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.e.Exceptions.ConflictException;
import practice.e.Exceptions.NotFoundException;

@Service
@Transactional(readOnly = true)
public class CourseService {
    private final CourseRepository courses;
    private final StudentRepository students;
    private final Clock clock;

    public CourseService(CourseRepository courses, StudentRepository students, Clock clock) {
        this.courses = courses; this.students = students; this.clock = clock;
    }

    @Transactional
    public Views.CourseView create(CreateCourseRequest r) {
        String title = r.title().strip();
        if (courses.existsByTitleIgnoreCase(title)) throw new ConflictException("course title already exists: " + title);
        return Views.CourseView.of(courses.save(new Course(title, r.capacity(), new Schedule(r.startDate(), r.endDate()))));
    }

    @Transactional
    public Views.StudentView createStudent(CreateStudentRequest r) {
        String email = r.email().strip().toLowerCase();
        if (students.findByEmailIgnoreCase(email).isPresent()) throw new ConflictException("email already registered: " + email);
        return Views.StudentView.of(students.save(new Student(r.name().strip(), email)));
    }

    public Views.CourseView get(long id) { return Views.CourseView.of(load(id)); }

    @Transactional
    public void enroll(long courseId, long studentId) {
        Course c = load(courseId);
        Student s = student(studentId);
        if (c.getStudents().contains(s)) return;
        if (c.getSchedule().endDate().isBefore(LocalDate.now(clock))) throw new ConflictException("course already ended");
        if (c.isFull()) throw new ConflictException("course is full");
        c.enroll(s);
    }

    @Transactional
    public void unenroll(long courseId, long studentId) { load(courseId).unenroll(student(studentId)); }

    @Transactional
    public Views.CourseView patch(long id, PatchCourseRequest r) {
        Course c = load(id);
        if (r.title() != null) {
            String title = r.title().strip();
            if (!title.equalsIgnoreCase(c.getTitle()) && courses.existsByTitleIgnoreCase(title))
                throw new ConflictException("course title already exists: " + title);
            c.setTitle(title);
        }
        if (r.capacity() != null) {
            if (r.capacity() < c.getStudents().size()) throw new ConflictException("capacity below enrolled students");
            c.setCapacity(r.capacity());
        }
        return Views.CourseView.of(c);
    }

    public Page<Views.CourseSummary> upcoming(Pageable pageable) {
        return courses.findByScheduleEndDateGreaterThanEqual(LocalDate.now(clock), pageable).map(Views.CourseSummary::of);
    }

    public List<EnrollmentCount> enrollmentCounts() { return courses.enrollmentCounts(); }

    private Course load(long id) { return courses.findWithStudents(id).orElseThrow(() -> new NotFoundException("course " + id)); }
    private Student student(long id) { return students.findById(id).orElseThrow(() -> new NotFoundException("student " + id)); }
}

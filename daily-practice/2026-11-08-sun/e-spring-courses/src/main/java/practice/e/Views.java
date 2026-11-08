package practice.e;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/** Response DTOs. Entities never leave the service, so no lazy collection is touched outside the transaction. */
public final class Views {
    private Views() {}

    public record StudentView(Long id, String name, String email) {
        static StudentView of(Student s) { return new StudentView(s.getId(), s.getName(), s.getEmail()); }
    }

    public record CourseSummary(Long id, String title, int capacity, LocalDate startDate, LocalDate endDate) {
        static CourseSummary of(Course c) {
            return new CourseSummary(c.getId(), c.getTitle(), c.getCapacity(), c.getSchedule().startDate(), c.getSchedule().endDate());
        }
    }

    public record CourseView(Long id, String title, int capacity, LocalDate startDate, LocalDate endDate, List<StudentView> students) {
        static CourseView of(Course c) {
            return new CourseView(c.getId(), c.getTitle(), c.getCapacity(), c.getSchedule().startDate(), c.getSchedule().endDate(),
                    c.getStudents().stream().sorted(Comparator.comparing(Student::getId)).map(StudentView::of).toList());
        }
    }
}

package practice.e;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CourseController {
    private final CourseService service;

    public CourseController(CourseService service) { this.service = service; }

    @PostMapping("/courses")
    public ResponseEntity<Views.CourseView> create(@Valid @RequestBody CreateCourseRequest r) {
        Views.CourseView v = service.create(r);
        return ResponseEntity.created(URI.create("/api/courses/" + v.id())).body(v);
    }

    @GetMapping("/courses/{id}")
    public Views.CourseView get(@PathVariable @Positive Long id) { return service.get(id); }

    @PatchMapping("/courses/{id}")
    public Views.CourseView patch(@PathVariable @Positive Long id, @Valid @RequestBody PatchCourseRequest r) { return service.patch(id, r); }

    @GetMapping("/courses")
    public ResponseEntity<List<Views.CourseSummary>> list(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                          @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        Page<Views.CourseSummary> p = service.upcoming(PageRequest.of(page, size, Sort.by("schedule.endDate", "id")));
        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(p.getTotalElements())).body(p.getContent());
    }

    @GetMapping("/courses/enrollment-counts")
    public List<EnrollmentCount> enrollmentCounts() { return service.enrollmentCounts(); }

    @PutMapping("/courses/{courseId}/students/{studentId}")
    public ResponseEntity<Void> enroll(@PathVariable @Positive Long courseId, @PathVariable @Positive Long studentId) {
        service.enroll(courseId, studentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/courses/{courseId}/students/{studentId}")
    public ResponseEntity<Void> unenroll(@PathVariable @Positive Long courseId, @PathVariable @Positive Long studentId) {
        service.unenroll(courseId, studentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/students")
    public ResponseEntity<Views.StudentView> createStudent(@Valid @RequestBody CreateStudentRequest r) {
        Views.StudentView v = service.createStudent(r);
        return ResponseEntity.created(URI.create("/api/students/" + v.id())).body(v);
    }
}

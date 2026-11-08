package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import java.time.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import practice.e.Exceptions.ConflictException;
import practice.e.Exceptions.NotFoundException;

@DataJpaTest
@Import({CourseService.class, CourseServiceTest.FixedClock.class})
class CourseServiceTest {
    /** "Today" is 2026-11-08. */
    @TestConfiguration static class FixedClock {
        @Bean @Primary Clock fixedClock() { return Clock.fixed(Instant.parse("2026-11-08T08:00:00Z"), ZoneOffset.UTC); }
    }

    @Autowired CourseService service;
    @Autowired EntityManager em;

    long ann, bob;

    @BeforeEach void students() {
        ann = service.createStudent(new CreateStudentRequest("  Ann ", "Ann@X.org")).id();
        bob = service.createStudent(new CreateStudentRequest("Bob", "bob@x.org")).id();
    }

    private long course(String title, int cap, String start, String end) {
        return service.create(new CreateCourseRequest(title, cap, LocalDate.parse(start), LocalDate.parse(end))).id();
    }

    private void sync() { em.flush(); em.clear(); }

    @Test void studentIsNormalisedAndEmailUnique() {
        Views.StudentView v = service.createStudent(new CreateStudentRequest(" Cy ", "  CY@x.org "));
        assertEquals("Cy", v.name());
        assertEquals("cy@x.org", v.email());
        assertThrows(ConflictException.class, () -> service.createStudent(new CreateStudentRequest("Other", "cy@X.ORG")));
    }

    @Test void createStripsTitleAndRejectsDuplicatesIgnoringCase() {
        long id = course("  Java  ", 3, "2026-11-01", "2026-12-01");
        sync();
        assertEquals("Java", service.get(id).title());
        assertThrows(ConflictException.class, () -> course("JAVA", 3, "2026-11-01", "2026-12-01"));
    }

    @Test void getMissingIsNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(12345));
    }

    @Test void enrollAndViewSortedByStudentId() {
        long c = course("Java", 3, "2026-11-01", "2026-12-01");
        service.enroll(c, bob);
        service.enroll(c, ann);
        sync();
        Views.CourseView v = service.get(c);
        assertEquals(List.of(ann, bob), v.students().stream().map(Views.StudentView::id).toList());
    }

    @Test void enrollIsIdempotentEvenWhenFull() {
        long c = course("Java", 1, "2026-11-01", "2026-12-01");
        service.enroll(c, ann);
        sync();
        service.enroll(c, ann);
        sync();
        assertEquals(1, service.get(c).students().size());
    }

    @Test void fullCourseRejectsNewStudent() {
        long c = course("Java", 1, "2026-11-01", "2026-12-01");
        service.enroll(c, ann);
        sync();
        assertThrows(ConflictException.class, () -> service.enroll(c, bob));
    }

    @Test void endedCourseRejectsEnrollmentButTodayIsStillOpen() {
        long ended = course("Old", 5, "2026-01-01", "2026-11-07");
        long lastDay = course("LastDay", 5, "2026-10-01", "2026-11-08");
        assertThrows(ConflictException.class, () -> service.enroll(ended, ann));
        service.enroll(lastDay, ann);
    }

    @Test void enrollNotFound() {
        long c = course("Java", 1, "2026-11-01", "2026-12-01");
        assertThrows(NotFoundException.class, () -> service.enroll(c, 9999));
        assertThrows(NotFoundException.class, () -> service.enroll(9999, ann));
    }

    @Test void unenrollFreesASeatAndIsSilentWhenAbsent() {
        long c = course("Java", 1, "2026-11-01", "2026-12-01");
        service.enroll(c, ann);
        sync();
        service.unenroll(c, bob);
        service.unenroll(c, ann);
        sync();
        service.enroll(c, bob);
        sync();
        assertEquals(List.of(bob), service.get(c).students().stream().map(Views.StudentView::id).toList());
    }

    @Test void patchIsPartial() {
        long c = course("Java", 3, "2026-11-01", "2026-12-01");
        Views.CourseView v = service.patch(c, new PatchCourseRequest(null, 10));
        assertEquals("Java", v.title());
        assertEquals(10, v.capacity());
        v = service.patch(c, new PatchCourseRequest("  Java 21 ", null));
        assertEquals("Java 21", v.title());
        assertEquals(10, v.capacity());
        assertEquals(LocalDate.parse("2026-12-01"), v.endDate());
        assertThrows(NotFoundException.class, () -> service.patch(999, new PatchCourseRequest("x", null)));
    }

    @Test void patchTitleUniquenessIgnoresSelf() {
        long a = course("Java", 3, "2026-11-01", "2026-12-01");
        course("Spring", 3, "2026-11-01", "2026-12-01");
        assertEquals("JAVA", service.patch(a, new PatchCourseRequest("JAVA", null)).title());
        assertThrows(ConflictException.class, () -> service.patch(a, new PatchCourseRequest("spring", null)));
    }

    @Test void patchCapacityCannotDropBelowEnrolled() {
        long c = course("Java", 3, "2026-11-01", "2026-12-01");
        service.enroll(c, ann);
        service.enroll(c, bob);
        sync();
        assertThrows(ConflictException.class, () -> service.patch(c, new PatchCourseRequest(null, 1)));
        assertEquals(2, service.patch(c, new PatchCourseRequest(null, 2)).capacity());
    }

    @Test void upcomingExcludesEndedAndSortsByEndDateThenId() {
        course("Ended", 5, "2026-01-01", "2026-11-07");
        long b = course("B", 5, "2026-10-01", "2026-12-31");
        long a = course("A", 5, "2026-10-01", "2026-11-08");
        long c = course("C", 5, "2026-10-01", "2026-12-31");
        sync();
        var page = service.upcoming(PageRequest.of(0, 2, Sort.by("schedule.endDate", "id")));
        assertEquals(3, page.getTotalElements());
        assertEquals(List.of(a, b), page.getContent().stream().map(Views.CourseSummary::id).toList());
        assertEquals(List.of(c), service.upcoming(PageRequest.of(1, 2, Sort.by("schedule.endDate", "id"))).getContent().stream().map(Views.CourseSummary::id).toList());
    }

    @Test void enrollmentCountsDelegates() {
        long c = course("Java", 3, "2026-11-01", "2026-12-01");
        service.enroll(c, ann);
        sync();
        assertEquals(1, service.enrollmentCounts().get(0).getEnrolled());
    }
}

package practice.e;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

@DataJpaTest
class CourseRepositoryTest {
    @Autowired CourseRepository courses;
    @Autowired StudentRepository students;
    @Autowired EntityManager em;
    @Autowired JdbcTemplate jdbc;

    Course java, spring;
    Student ann, bob;

    @BeforeEach void data() {
        ann = students.save(new Student("Ann", "ann@x.org"));
        bob = students.save(new Student("Bob", "bob@x.org"));
        java = new Course("Java", 2, new Schedule(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 3, 1)));
        spring = new Course("Advanced Spring", 5, new Schedule(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 12, 1)));
        java.enroll(ann); java.enroll(bob); spring.enroll(ann);
        courses.save(java); courses.save(spring);
        em.flush();
        em.clear();
    }

    @Test void joinTableAndEmbeddedColumnsAreMapped() {
        assertEquals(3, jdbc.queryForObject("select count(*) from enrollment", Integer.class));
        assertEquals(LocalDate.of(2026, 3, 1), jdbc.queryForObject("select end_date from course where title = 'Java'", LocalDate.class));
    }

    @Test void fetchJoinInitialisesStudentsInOneQuery() {
        Course c = courses.findWithStudents(java.getId()).orElseThrow();
        assertTrue(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(c, "students"));
        assertEquals(2, c.getStudents().size());
        assertTrue(courses.findWithStudents(999L).isEmpty());
    }

    @Test void fetchJoinFindsCoursesWithoutStudents() {
        Course empty = courses.saveAndFlush(new Course("Empty", 3, new Schedule(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 2))));
        em.clear();
        assertTrue(courses.findWithStudents(empty.getId()).orElseThrow().getStudents().isEmpty());
    }

    @Test void lazyByDefault() {
        Course c = courses.findById(java.getId()).orElseThrow();
        assertFalse(em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(c, "students"));
    }

    @Test void enrollmentCountsProjection() {
        List<EnrollmentCount> counts = courses.enrollmentCounts();
        assertEquals(2, counts.size());
        assertEquals("Java", counts.get(0).getTitle());
        assertEquals(2, counts.get(0).getEnrolled());
        assertEquals(2, counts.get(0).getCapacity());
        assertEquals(1, counts.get(1).getEnrolled());
        assertEquals(spring.getId(), counts.get(1).getCourseId());
    }

    @Test void coursesOfStudentOrderedByTitle() {
        assertEquals(List.of("Advanced Spring", "Java"), courses.findByStudent(ann.getId()).stream().map(Course::getTitle).toList());
        assertEquals(List.of("Java"), courses.findByStudent(bob.getId()).stream().map(Course::getTitle).toList());
        assertTrue(courses.findByStudent(999L).isEmpty());
    }

    @Test void uniqueTitleAndEmailAreEnforcedByTheDatabase() {
        assertThrows(DataIntegrityViolationException.class,
                () -> courses.saveAndFlush(new Course("Java", 1, new Schedule(LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 2)))));
    }

    @Test void uniqueEmail() {
        assertThrows(DataIntegrityViolationException.class, () -> students.saveAndFlush(new Student("Ann 2", "ann@x.org")));
    }

    @Test void findByEmailIgnoreCase() {
        assertEquals("Ann", students.findByEmailIgnoreCase("ANN@x.org").orElseThrow().getName());
    }
}

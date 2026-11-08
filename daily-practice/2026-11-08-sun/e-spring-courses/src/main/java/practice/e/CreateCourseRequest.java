package practice.e;

import java.time.LocalDate;

/**
 * TODO(E4a): add Bean Validation: title not blank, max 100; capacity 1..500; both dates not null;
 * plus a cross-field rule: endDate must not be before startDate (an @AssertTrue boolean method, null-safe).
 */
public record CreateCourseRequest(String title, int capacity, LocalDate startDate, LocalDate endDate) {}

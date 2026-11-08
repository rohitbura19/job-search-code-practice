package practice.e;

import java.time.LocalDate;

/** TODO(E1a): make this a JPA embeddable (a record works with Hibernate 6.2+). Columns become start_date / end_date. */
public record Schedule(LocalDate startDate, LocalDate endDate) {}

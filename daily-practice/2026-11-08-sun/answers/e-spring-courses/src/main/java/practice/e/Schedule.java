package practice.e;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public record Schedule(LocalDate startDate, LocalDate endDate) {}

package practice.e.estimate;

import java.math.BigDecimal;
import java.time.Instant;

/** GIVEN - stands in for the JPA entity (the @Entity / @Column / @LastModifiedDate version is a paper exercise). */
public record Tariff(Long id, String regionCode, BigDecimal ratePerKwh, String currency, Instant updatedAt) {}

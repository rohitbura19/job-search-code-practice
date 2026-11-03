package practice.e;

import java.time.Duration;

// TODO: bind the "booking" prefix as an immutable properties record.
//  @ConfigurationProperties("booking") + @Validated.
//  maxDuration: defaults to PT4H, must not be null.
//  maxPageSize: defaults to 50, between 1 and 200 (a bad value must make the application context fail to start).
public record BookingProps(Duration maxDuration, int maxPageSize) {
}
